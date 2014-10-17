package cz.metacentrum.perun.wui.widgets;

import cz.metacentrum.perun.wui.client.resources.Translatable;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import cz.metacentrum.perun.wui.client.utils.TableSorter;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.resources.PerunKeyProvider;
import cz.metacentrum.perun.wui.widgets.cells.PerunCheckboxCell;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is wrapper around standard GridTable which provides necessary and most common functionality used in Perun GUI.
 * <p/>
 * Allows to simply handle tables content as list of objects, providing add/remove/set/get/clear/filter/sort methods.
 * <p/>
 * Unifies handling above single/multi selection models, provides CheckBox column based on selection model
 * (if selection is enabled). Provides getSelectedList().
 * <p/>
 * Columns can be added manually or default set of columns is provided by table's ColumnProvider.
 * It's best to pass ColumnProvider in table constructor. Please note, that column provider is specific
 * to each object type, therefore 'default' depends entirely on ColumnProvider implementation.
 * <p/>
 * Filtering and selection on table content can be dynamically modified by anonymous event classes.
 * <p/>
 * Sorting is dependant on specified column, default table sort on setList() is defined by ColumnProvider.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunDataGrid<T extends JavaScriptObject> extends DataGrid<T> implements Translatable {

	// list of items displayed in table
	ArrayList<T> content = new ArrayList<>();
	// list of all items passed to table
	ArrayList<T> backup = new ArrayList<>();

	// selection models
	SingleSelectionModel<T> singleSelectionModel = new SingleSelectionModel<T>(new PerunKeyProvider<T>());
	MultiSelectionModel<T> multiSelectionModel = new MultiSelectionModel<T>(new PerunKeyProvider<T>());

	// by default data grid is multi-selection
	boolean singleSelection = false;
	// by default data grid allows selection and show checkbox columns
	boolean selectionEnabled = true;

	// by default selection on objects is not restricted
	PerunSelectionEvent<T> selectionFilter = new PerunSelectionEvent<T>() {
		@Override
		public boolean canSelectObject(T object) {
			return selectionEnabled && (object != null);
		}
	};

	// provider of default columns, if null, no column is added to table and must be set manually.
	ColumnProvider<T> columnProvider;

	// support of sorting on columns
	ColumnSortEvent.Handler columnSortHandler = new ColumnSortEvent.Handler() {
		@Override
		public void onColumnSort(ColumnSortEvent columnSortEvent) {
			PerunColumnType type = ((PerunColumn) columnSortEvent.getColumn()).getColumnType();
			if (type != null) {
				if (columnSortEvent.isSortAscending()) {
					sortTable(type, true, false);
				} else {
					sortTable(type, true, true);
				}
			}
		}
	};

	// we must store all columns and their headers/footers by reference, since add/insert/remove methods with
	// index doesn't work properly on table redraws (when keep existing columns is used).
	ArrayList<Column<T, ?>> columns = new ArrayList<>();
	Column<T, T> checkBoxColumn;
	Map<Column<T, ?>, Header<?>> columnHeaders = new HashMap<>();
	Map<Column<T, ?>, Header<?>> columnFooters = new HashMap<>();

	// loading widget used for table loading / filtering / error actionsignoreEmptyFilter
	PerunLoader loaderWidget;

	/**
	 * Interface for Anonymous classes, which provides filtering
	 * decision on object.
	 *
	 * @param <T>
	 */
	public interface PerunFilterEvent<T extends JavaScriptObject> {

		/**
		 * Return TRUE if filtering rule should add object to table.
		 * FALSE otherwise.
		 *
		 * @param text   filtering input
		 * @param object object to filter on
		 * @return TRUE if object should be added to table
		 */
		public boolean filterOnObject(String text, T object);

	}

	/**
	 * Interface for anonymous classes, which provides selection
	 * decision on object.
	 *
	 * @param <T>
	 */
	public interface PerunSelectionEvent<T extends JavaScriptObject> {

		/**
		 * Return true, if object can be selected/unselected
		 *
		 * @param object object to make decision about
		 * @return TRUE if object can be selected on users try
		 */
		public boolean canSelectObject(T object);

	}

	/**
	 * Create instance of table with following default settings:
	 * - Only Checkbox column is added to table by default.
	 * - Table has selection enabled with multi-selection model.
	 * - Other columns must be added manually (or you must set ColumnProvider later and call drawTableColumns()).
	 */
	public PerunDataGrid() {
		this(true, false, null, null);
	}

	/**
	 * Create instance of table with following default settings:
	 * - Only CheckBox column is added to table (if selectionEnabled == TRUE).
	 * - Selection (if enabled) is in multi-selection mode.
	 * - Other columns must be added manually (or you must set ColumnProvider later and call drawTableColumns()).
	 *
	 * @param selectionEnabled TRUE = selection in table is enabled / FALSE = selection in table is disabled
	 */
	public PerunDataGrid(boolean selectionEnabled) {
		this(selectionEnabled, false, null, null);
	}

	/**
	 * Create instance of table with following default settings:
	 * - No columns are added to table with exception of checkbox column (if selectionEnabled == TRUE).
	 *
	 * @param selectionEnabled TRUE = selection in table is enabled / FALSE = selection in table is disabled
	 * @param singleSelection  TRUE = use single selection model / FALSE = use multi selection model
	 */
	public PerunDataGrid(boolean selectionEnabled, boolean singleSelection) {
		this(selectionEnabled, singleSelection, null, null);
	}

	/**
	 * Create instance of table with following default settings:
	 * - Selection is enabled and multi-selection model is used.
	 * - Checkbox column is added to table by default.
	 *
	 * @param columnProvider ColumnProvider, which provides default columns, default sorting and
	 *                       filtering and selection rules implementation.
	 */
	public PerunDataGrid(ColumnProvider<T> columnProvider) {
		this(true, false, columnProvider, null);
	}

	/**
	 * Create instance of table with following default settings:
	 * - Table is set to multi-selection model.
	 *
	 * @param selectionEnabled TRUE = selection is enabled / FALSE = selection disabled
	 * @param columnProvider   ColumnProvider, which provides default columns, default sorting and
	 *                         filtering and selection rules implementation.
	 */
	public PerunDataGrid(boolean selectionEnabled, ColumnProvider<T> columnProvider) {
		this(selectionEnabled, false, columnProvider, null);
	}

	/**
	 * Create instance of table with following default settings.
	 *
	 * @param selectionEnabled TRUE = selection in table is enabled / FALSE = selection disabled
	 * @param singleSelection  TRUE = use single selection model / FALSE = use multi selection model
	 * @param columnProvider   ColumnProvider, which provides default columns, default sorting and
	 *                         filtering and selection rules implementation.
	 */
	public PerunDataGrid(boolean selectionEnabled, final boolean singleSelection, ColumnProvider<T> columnProvider) {
		this(selectionEnabled, singleSelection, columnProvider, null);
	}

	/**
	 * Create instance of table with following default settings.
	 *
	 * @param selectionEnabled TRUE = selection in table is enabled / FALSE = selection disabled
	 * @param singleSelection  TRUE = use single selection model / FALSE = use multi selection model
	 * @param columnProvider   ColumnProvider, which provides default columns, default sorting and
	 *                         filtering and selection rules implementation.
	 * @param selectionFilter  custom implementation of selection filter (what items can be selected)
	 *                         if null, then ColumnProvider's default selection filter is used.
	 */
	public PerunDataGrid(boolean selectionEnabled, boolean singleSelection, ColumnProvider columnProvider, PerunSelectionEvent<T> selectionFilter) {

		//this.setAutoHeaderRefreshDisabled(false);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		// set necessary params
		this.selectionEnabled = selectionEnabled;
		this.singleSelection = singleSelection;
		this.columnProvider = columnProvider;

		// make table sortable if columns supports it
		addColumnSortHandler(columnSortHandler);

		// set table style
		this.setCondensed(true);
		this.setStriped(true);
		this.setHover(true);

		if (selectionFilter != null) {
			// custom selection filter
			setSelectionFilter(selectionFilter);
		} else {
			// or use default set by column provider
			if (this.columnProvider != null) {
				setSelectionFilter(this.columnProvider.getDefaultSelectionEvent());
			}
		}

		// default loader
		setLoaderWidget(new PerunLoader());

		// draw default columns
		drawTableColumns();

	}

	/**
	 * Get current table's empty/loading widget.
	 *
	 * @return PerunLoader widget associated with this table or null
	 */
	public PerunLoader getLoaderWidget() {
		return loaderWidget;
	}

	/**
	 * Set custom PerunLoader widget for this table and empty/loading state.
	 *
	 * @param loaderWidget PerunLoader widget to set.
	 */
	public void setLoaderWidget(PerunLoader loaderWidget) {
		if (loaderWidget != null) {
			this.loaderWidget = loaderWidget;
			setEmptyTableWidget(loaderWidget);
			setLoadingIndicator(loaderWidget);
		}
	}

	/**
	 * Tells if data grid is in singles selection model or not.
	 *
	 * @return TRUE = single selection / FALSE = multi selection (default)
	 */
	public boolean isSingleSelection() {
		return singleSelection;
	}

	/**
	 * Set table to single / multi selection model.
	 * <p/>
	 * After setting this, table columns must be redrawn by calling
	 * drawTableColumns() or manually cleared and added for change to take effect !!
	 *
	 * @param singleSelection TRUE = single selection / FALSE = multi selection model (default)
	 */
	public void setSingleSelection(boolean singleSelection) {
		this.singleSelection = singleSelection;
	}

	/**
	 * Set if table content can be selected or not.
	 * <p/>
	 * After setting this, all table columns must be redrawn by calling
	 * drawTableColumns() or manually cleared and added for change to take effect !!
	 * <p/>
	 * When set to false, Checkbox column is not added to table on redraw.
	 *
	 * @param selectionEnabled TRUE = selection is enabled (default) / FALSE = selection is disabled
	 */
	public void setSelectionEnabled(boolean selectionEnabled) {
		this.selectionEnabled = selectionEnabled;
	}

	/**
	 * Return PerunSelectionEvent<T> anonymous class associated with table,
	 * which is used for decision, if object can be selected.
	 * <p/>
	 * If not set from outside, then default implementation is returned.
	 * Meaning, that any object in table can be selected.
	 *
	 * @return implementation of selection filter
	 */
	public PerunSelectionEvent<T> getSelectionFilter() {
		return selectionFilter;
	}

	/**
	 * Sets selection filter used in this table. If selection in table
	 * is allowed, then only method defined in PerunSelectionEvent<T>
	 * can decide if object in table can be selected. If not, no selection
	 * change on this object is performed.
	 *
	 * @param selectionFilter anonymous class defining selection rule
	 */
	public void setSelectionFilter(PerunSelectionEvent<T> selectionFilter) {
		if (selectionFilter != null) {
			this.selectionFilter = selectionFilter;
		}
	}

	/**
	 * Return TRUE, if selection is enabled on table items (rows).
	 *
	 * @return TRUE = selection enabled / FALSE = selection disabled
	 */
	public boolean isSelectionEnabled() {
		return selectionEnabled;
	}

	/**
	 * Return ColumnSortHandler which can be used to add sorting
	 * capability to specified column.
	 *
	 * @return ColumnSortHandler
	 */
	public ColumnSortEvent.Handler getColumnSortHandler() {
		return columnSortHandler;
	}

	/**
	 * Return ColumnProvider<T> used in this table. Can be used to
	 * generate columns based on type of objects displayed in table.
	 *
	 * @return ColumnProvider or null if not present
	 */
	public ColumnProvider<T> getColumnProvider() {
		return columnProvider;
	}

	/**
	 * Sets column provider for this table.
	 * <p/>
	 * Change of column provider doesn't update selection filter or any other
	 * setting already set to table. If you wish to use such setting (provided by
	 * column provider), either create table with provider in a constructor or set
	 * all necessary parameters manually.
	 * <p/>
	 * After setting this, all table columns must be redrawn by calling
	 * drawTableColumns() or manually cleared and added for change to take effect !!
	 *
	 * @param columnProvider
	 */
	public void setColumnProvider(ColumnProvider<T> columnProvider) {
		setColumnProvider(columnProvider, false);
	}

	/**
	 * Sets column provider for this table. If useColumnProviderSettings is set to TRUE,
	 * then any default setting provided by ColumnProvider is set to this table.
	 * <p/>
	 * After setting this, all table columns must be redrawn by calling
	 * drawTableColumns() or manually cleared and added for change to take effect !!
	 *
	 * @param columnProvider
	 * @param useColumnProviderSettings TRUE = to load default settings from column provider / FALSE = only set provider to table property
	 */
	public void setColumnProvider(ColumnProvider<T> columnProvider, boolean useColumnProviderSettings) {

		this.columnProvider = columnProvider;

		if (useColumnProviderSettings) {
			// load default settings
			if (this.columnProvider != null) {
				setSelectionFilter(this.columnProvider.getDefaultSelectionEvent());
			}
		}

	}

	/**
	 * Add object to the end of table.
	 * <p/>
	 * No sorting or filtering is performed, object is added to current table state as it is.
	 *
	 * @param object object to add to table
	 */
	public void addToTable(T object) {
		if (object != null) {
			backup.add(object);
			content.add(object);
			this.setRowData(content);
			this.flush();
			this.redraw();
			// TODO - update oracle
		}
	}

	/**
	 * Add objects to the end of table.
	 * <p/>
	 * No sorting or filtering is performed, objects are added to current table state as it is.
	 *
	 * @param objects objects to add to table
	 */
	public void addToTable(ArrayList<T> objects) {
		if (objects != null && !objects.isEmpty()) {
			for (int i = 0; i < objects.size(); i++) {
				if (objects.get(i) != null) {
					backup.add(objects.get(i));
					content.add(objects.get(i));
				}
			}
			this.setRowData(content);
			this.flush();
			this.redraw();
			// TODO - update oracle
		}
	}

	/**
	 * Insert data to table on specified index. If index is out of bound, it's added to the end of list.
	 * <p/>
	 * No sorting or filtering is performed, object is added to current table state as it is.
	 *
	 * @param index  index to add object to (starting with 0)
	 * @param object object to add
	 */
	public void insertToTable(int index, T object) {

		if (object != null) {

			// insert value safely
			if (index < 0 || index > backup.size()) {
				backup.add(backup.size(), object);
			} else {
				backup.add(index, object);
			}
			if (index < 0 || index > content.size()) {
				content.add(content.size(), object);
			} else {
				content.add(index, object);
			}
			this.setRowData(content);
			this.flush();
			this.redraw();
			// TODO - update oracle

		}
	}

	/**
	 * Sorts table by selected column (ascending order). If column is not supported for sorting,
	 * then it tries to sort by default column (column provider must be set).
	 * <p/>
	 * If no match found, then table is not sorted.
	 *
	 * @param column column to sort by
	 */
	public void sortTable(PerunColumnType column) {
		sortTable(column, false);
	}

	/**
	 * Sorts table by selected column. If column is not supported for sorting,
	 * then it tries to sort by default column (column provider must be set).
	 * <p/>
	 * If no match found, then table is not sorted.
	 *
	 * @param column     column to sort by
	 * @param descending TRUE = descending order / FALSE = ascending (default)
	 */
	public void sortTable(PerunColumnType column, boolean descending) {
		sortTable(column, true, descending);
	}

	/**
	 * Sorts table by selected column. If column is not supported for sorting,
	 * then it tries to sort by default column (column provider must be set).
	 * <p/>
	 * If no match found, then table is not sorted.
	 *
	 * @param column     column to sort by
	 * @param refresh    TRUE = refresh displayed items based on sort / FALSE = do not refresh displayed items
	 * @param descending TRUE = descending order / FALSE = ascending (default)
	 */
	public void sortTable(PerunColumnType column, boolean refresh, boolean descending) {

		if (PerunColumnType.ID.equals(column)) {
			backup = TableSorter.sortById(backup, descending);
			content = TableSorter.sortById(content, descending);
		} else if (PerunColumnType.NAME.equals(column)) {
			backup = TableSorter.sortByName(backup, descending);
			content = TableSorter.sortByName(content, descending);
		} else if (PerunColumnType.DESCRIPTION.equals(column)) {
			backup = TableSorter.sortByDescription(backup, descending);
			content = TableSorter.sortByDescription(content, descending);
		} else if (PerunColumnType.SHORT_NAME.equals(column)) {
			backup = TableSorter.sortByShortName(backup, descending);
			content = TableSorter.sortByShortName(content, descending);
		} else if (PerunColumnType.OWNERS.equals(column)) {
			backup = TableSorter.sortByOwnersNames(backup, descending);
			content = TableSorter.sortByOwnersNames(content, descending);
		} else if (PerunColumnType.CREATED_AT.equals(column)) {
			backup = TableSorter.sortByCreatedAt(backup, descending);
			content = TableSorter.sortByCreatedAt(content, descending);
		} else {
			// if column for sorting not found, use columns provider's default, if possible
			if (columnProvider != null) {
				sortTable(columnProvider.getDefaultSortColumn(), refresh, descending);
				return;
			}
		}

		if (refresh) {
			if (content.isEmpty()) {
				// do not set if is empty
				this.setRowCount(0, false);
			} else {
				this.setRowData(content);
			}
			this.flush();
			this.redraw();
		}

	}

	/**
	 * Removes object from table.
	 * <p/>
	 * No sorting or filtering is performed, object is removed from current table state as it is.
	 *
	 * @param object object to remove
	 */
	public void removeFromTable(T object) {

		backup.remove(object);
		content.remove(object);
		// remove object from selection
		getSelectionModel().setSelected(object, false);
		if (content.isEmpty()) {
			this.setRowCount(0, false);
		} else {
			this.setRowData(content);
		}
		this.flush();
		this.redraw();
		// TODO - clear & rebuild oracle
	}

	/**
	 * Removes objects from table.
	 * <p/>
	 * No sorting or filtering is performed, objects are removed from current table state as it is.
	 *
	 * @param objects objects to remove
	 */
	public void removeFromTable(ArrayList<T> objects) {
		// FIXME - save remove from list, we cant use object's equals().
		backup.removeAll(objects);
		content.removeAll(objects);
		// remove object from selection
		for (T object : objects) {
			getSelectionModel().setSelected(object, false);
		}
		if (content.isEmpty()) {
			this.setRowCount(0, false);
		} else {
			this.setRowData(content);
		}
		this.flush();
		this.redraw();
		// TODO - clear & rebuild oracle
	}

	/**
	 * Convenient method to set new table content all at once.
	 * <p/>
	 * This method clears all table state:
	 * - clear displayed items
	 * - clear all backed-up items (used when filtering)
	 * - clear all selection made to table
	 * <p/>
	 * All passed items are added to table and then sorted by
	 * table's default sort column (based on object type).
	 * <p/>
	 * Loading widget is set to onFinished() state.
	 *
	 * @param list content to set to table
	 */
	public void setList(ArrayList<T> list) {

		// TODO - suggest oracle + default sort ?

		clearTable();
		if (list != null && !list.isEmpty()) {
			backup.addAll(list);
			content.addAll(list);
		}

		if (columnProvider != null) {
			sortTable(columnProvider.getDefaultSortColumn(), columnProvider.isDefaultSortColumnDescending());
		}

		loaderWidget.onFinished();

		if (content.isEmpty()) {
			this.setRowCount(0, false);
		} else {
			this.setRowData(content);
		}
		this.flush();
		this.redraw();

	}

	/**
	 * Clears all data from table.
	 * <p/>
	 * - clear displayed items
	 * - clear backed-up items (used when filtering)
	 * - clear all selection made to table
	 */
	public void clearTable() {

		// TODO clear oracle

		content.clear();
		backup.clear();

		// clear selection model !!
		if (singleSelection) {
			singleSelectionModel.clear();
		} else {
			multiSelectionModel.clear();
		}

		loaderWidget.onEmpty();
		this.setRowCount(0, false);
		this.flush();
		this.redraw();

		// TODO - set to loading state ??

	}

	/**
	 * Filter content of table based on user input. Default filtering rule (provided by ColumnProvider) is used.
	 * If ColumnProvider is not set to table, then no filtering is performed.
	 * <p/>
	 * All selection made to table is lost !!
	 *
	 * @param text filtering input (null or empty to show all = ignore filtering rules)
	 */
	public void filterTable(String text) {

		if (columnProvider != null) {
			filterTable(text, columnProvider.getDefaultFilterEvent());
		}

	}

	/**
	 * Filter content of table based on user input and custom filtering rule.
	 * <p/>
	 * All selection made to table is lost !!
	 *
	 * @param text   filtering input (null or empty to show all = ignore filtering rules)
	 * @param filter own implementation of filtering rule
	 */
	public void filterTable(String text, PerunFilterEvent<T> filter) {

		// clear selection model !!
		if (singleSelection) {
			singleSelectionModel.clear();
		} else {
			multiSelectionModel.clear();
		}

		// filter table content
		content.clear();
		if (text == null || text.isEmpty()) {
			// show all items in table
			content.addAll(backup);
		} else {
			// do filtering
			for (T object : backup) {
				if (filter.filterOnObject(text, object)) {
					content.add(object);
				}
			}
		}

		loaderWidget.onFilter(text);

		// fill table
		if (content.isEmpty()) {
			this.setRowCount(0, false);
		} else {
			this.setRowData(content);
		}
		this.flush();
		this.redraw();

	}

	/**
	 * Return list of objects selected in table. Handles both (single/multi) selection models.
	 * <p/>
	 * Only items currently displayed in table can be considered as selected. Meaning, that if table content
	 * is currently filtered, you won't get any 'not displayed' items you might have selected before.
	 *
	 * @return ArrayList of selected items
	 */
	public ArrayList<T> getSelectedList() {

		ArrayList<T> result = new ArrayList<>();
		if (singleSelection) {
			result.add(singleSelectionModel.getSelectedObject());
		} else {
			// keep objects sorted as in table
			for (T object : getList()) {
				if (multiSelectionModel.getSelectedSet().contains(object)) {
					result.add(object);
				}
			}
		}
		return result;

	}

	/**
	 * Return instance of list of objects, which are displayed in table.
	 * List content can change based on add/remove/clear/filter events made on table.
	 * Do not change list content directly, use methods above table.
	 *
	 * @return instance of list of displayed objects
	 */
	public ArrayList<T> getList() {
		return this.content;
	}

	/**
	 * Register any widget for table selection to manage enable/disable state.
	 * Widget is also disabled, when table is in LOADING state and can be enabled only by selection change.
	 * If widget is instance of PerunButton and is in processing state, than it's not enabled on selection.
	 * <p/>
	 * If no item in table is selected, widget is disabled.
	 * If any item in table is selected, widget is enabled.
	 * <p/>
	 * Original widget state is set to disabled.
	 *
	 * @param widget widget to be handled by table
	 */
	public void addTableManagedWidget(final HasEnabled widget) {

		widget.setEnabled(false);

		if (widget instanceof PerunButton) {
			((PerunButton)widget).setTableManaged(this);
		}

		if (singleSelection) {
			singleSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				@Override
				public void onSelectionChange(SelectionChangeEvent selectionChangeEvent) {
					if (singleSelectionModel.getSelectedObject() != null) {
						if (widget instanceof PerunButton) {
							if (!((PerunButton)widget).isProcessing()) widget.setEnabled(true);
						} else {
							widget.setEnabled(true);
						}
					} else {
						widget.setEnabled(false);
					}
				}
			});
		} else {
			multiSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				@Override
				public void onSelectionChange(SelectionChangeEvent selectionChangeEvent) {
					if (multiSelectionModel.getSelectedSet() != null && !multiSelectionModel.getSelectedSet().isEmpty()) {
						if (widget instanceof PerunButton) {
							if (!((PerunButton)widget).isProcessing()) widget.setEnabled(true);
						} else {
							widget.setEnabled(true);
						}
					} else {
						widget.setEnabled(false);
					}
				}
			});
		}

		addLoadingStateChangeHandler(new LoadingStateChangeEvent.Handler() {
			@Override
			public void onLoadingStateChanged(LoadingStateChangeEvent event) {

				if (event.getLoadingState().equals(LoadingStateChangeEvent.LoadingState.LOADING)) {
					widget.setEnabled(false);
				}

			}
		});

	}

	/**
	 * Register any widget for table loading state change to manage widget enabled/disabled state.
	 * If ignoreEmptyFilter == TRUE, then widget is kept enabled when table filtering results in
	 * an empty table (which is LOADING state too).
	 * If widget is instance of PerunButton and is in processing state, than it's not enabled on selection.
	 * <p/>
	 * If table is loading, widget is disabled.
	 * If table is presenting data, widget is enabled.
	 * <p/>
	 * Original widget state is set to disabled when registering.
	 *
	 * @param widget widget to be handled by table
	 * @param ignoreEmptyFilter TRUE = don't disable widget on empty filtering / FALSE = disable widget normally
	 */
	public void addTableLoadingManagedWidget(final HasEnabled widget, final boolean ignoreEmptyFilter) {

		widget.setEnabled(false);

		addLoadingStateChangeHandler(new LoadingStateChangeEvent.Handler() {
			@Override
			public void onLoadingStateChanged(LoadingStateChangeEvent event) {

				if (event.getLoadingState().equals(LoadingStateChangeEvent.LoadingState.LOADING)) {
					if (loaderWidget.getLoaderState().equals(PerunLoader.PerunLoaderState.filter) &&
							ignoreEmptyFilter) {
						if (widget instanceof PerunButton) {
							if (!((PerunButton)widget).isProcessing()) widget.setEnabled(true);
						} else {
							widget.setEnabled(true);
						}
					} else {
						widget.setEnabled(false);
					}
				} else {
					if (widget instanceof PerunButton) {
						if (!((PerunButton)widget).isProcessing()) widget.setEnabled(true);
					} else {
						widget.setEnabled(true);
					}
				}

			}
		});

	}

	/**
	 * Register any widget for table loading state change to manage widget enabled/disabled state.
	 * If ignoreEmptyFilter == TRUE, then widget is kept enabled when table filtering results in
	 * an empty table (which is LOADING state too).
	 * If widget is instance of PerunButton and is in processing state, than it's not enabled on selection.
	 * <p/>
	 * If table is loading, widget is disabled.
	 * If table is presenting data, widget is enabled.
	 * <p/>
	 * Original widget state is set to disabled when registering.
	 *
	 * @param widget widget to be handled by table
	 * @param ignoreEmptyFilter TRUE = don't disable widget on empty filtering / FALSE = disable widget normally
	 * @param focus TRUE = focus widget when enabled / FALSE = blur widget when disabled
	 */
	public void addTableLoadingManagedWidget(final HasEnabled widget, final boolean ignoreEmptyFilter, boolean focus) {

		widget.setEnabled(false);

		addLoadingStateChangeHandler(new LoadingStateChangeEvent.Handler() {
			@Override
			public void onLoadingStateChanged(LoadingStateChangeEvent event) {

				if (event.getLoadingState().equals(LoadingStateChangeEvent.LoadingState.LOADING)) {
					if (loaderWidget.getLoaderState().equals(PerunLoader.PerunLoaderState.filter) &&
							ignoreEmptyFilter) {

						if (widget instanceof PerunButton) {
							if (!((PerunButton)widget).isProcessing()) widget.setEnabled(true);
						} else {
							widget.setEnabled(true);
						}

						if (widget instanceof Focusable && widget.isEnabled()) {
							((Focusable) widget).setFocus(true);
						}

					} else {

						widget.setEnabled(false);

						if (widget instanceof Focusable) {
							((Focusable) widget).setFocus(false);
						}

					}
				} else {

					if (widget instanceof PerunButton) {
						if (!((PerunButton)widget).isProcessing()) widget.setEnabled(true);
					} else {
						widget.setEnabled(true);
					}

					if (widget instanceof Focusable && widget.isEnabled()) {
						((Focusable) widget).setFocus(true);
					}

				}

			}
		});

	}

	/**
	 * Removes all current columns from table and draw new columns based on current table settings
	 * (selection model, enabled/disabled, selection filter, column provider = defines standard set of columns).
	 * <p/>
	 * All selection made to table is lost!
	 */
	public void drawTableColumns() {
		drawTableColumns(false);
	}

	/**
	 * Removes all current columns from table and draw new columns based on current table settings
	 * (selection model, enabled/disabled, selection filter, column provider = defines standard set of columns).
	 * <p/>
	 * All selection made to table is lost!
	 * <p/>
	 * If keepExisting is set to TRUE, then any column present in table is kept (based on it's implementation
	 * it might not use new table setting !!). If set to FALSE, then all columns are replaced by current ColumnProvider.
	 * <p/>
	 * CheckBoxColumn is present in table only if selection is enabled and is always replaced when drawing table.
	 *
	 * @param keepExisting TRUE = existing columns are kept / FALSE = columns are replaced by column provider default
	 */
	public void drawTableColumns(boolean keepExisting) {

		// clear any selection in table
		if (singleSelection) {
			singleSelectionModel.clear();
			this.setSelectionModel(singleSelectionModel);
		} else {
			multiSelectionModel.clear();
			this.setSelectionModel(multiSelectionModel);
		}

		// remove all columns from table display (keep them in our storage for now)
		for (Column c : columns) {
			this.removeColumn(c);
		}
		// remove checkbox column also from stored, since it will be recreated based on current selection model
		if (checkBoxColumn != null) {
			columns.remove(checkBoxColumn);
		}

		// define new checkbox column
		checkBoxColumn = new Column<T, T>(new PerunCheckboxCell<T>(true, selectionFilter)) {
			@Override
			public T getValue(T object) {
				// Get the value from the selection model.
				((GeneralObject) object).setChecked(getSelectionModel().isSelected(object));
				return object;
			}
		};

		// IF ITEM "CAN" BE SELECTED, THEN UPDATE VALUE, set to false (not selected) otherwise
		checkBoxColumn.setFieldUpdater(new FieldUpdater<T, T>() {
			@Override
			public void update(int i, T object, T selected) {
				if (selectionEnabled && selectionFilter.canSelectObject(object)) {
					getSelectionModel().setSelected(object, !getSelectionModel().isSelected(object));
				}
			}
		});

		// define checkbox column
		CheckboxCell cb = new CheckboxCell();

		Header<Boolean> checkBoxHeader = new Header<Boolean>(cb) {
			public Boolean getValue() {
				return false; //return true to see a checked checkbox.
			}
		};

		// update value only for multi-selection model !!
		checkBoxHeader.setUpdater(new ValueUpdater<Boolean>() {
			public void update(Boolean value) {
				if (!isSingleSelection()) {
					for (T obj : content) {
						// sets selected to all, if value = true, unselect otherwise
						if (selectionEnabled && selectionFilter.canSelectObject(obj)) {
							getSelectionModel().setSelected(obj, value);
						}
					}
				}
			}
		});

		if (keepExisting) {

			// put back existing columns with their headers/footers
			for (int i = 0; i < columns.size(); i++) {
				Column<T, ?> column = columns.get(i);
				// use super implementation for adding to prevent storing duplicity in our storage (list of columns)
				super.insertColumn(i, column, columnHeaders.get(column), columnFooters.get(column));
			}

		} else {

			// clear all stored columns info and create new one
			columns.clear();
			columnHeaders.clear();
			columnFooters.clear();

			// add standard columns, if columnProvider is defined
			if (this.columnProvider != null) {
				for (PerunColumnType col : this.columnProvider.getDefaultColumns()) {
					// prevent form adding columns of extended info
					this.columnProvider.addColumnToTable(this, col);
				}
			}

		}

		// set checkbox column as first
		if (selectionEnabled) {
			insertColumn(0, checkBoxColumn, checkBoxHeader);
			this.setColumnWidth(checkBoxColumn, 30.0, com.google.gwt.dom.client.Style.Unit.PX);
		}

	}

	@Override
	public void setRowData(int start, List<? extends T> values) {
		super.setRowData(start, values);
		Scheduler.get().scheduleDeferred(new Command() {
			@Override
			public void execute() {
				disableLinks();
			}
		});
	}

	/**
	 * Prevent default click action on links in menu. Href is present but ignored, only
	 * GWT's click handler is activated.
	 */
	private final native void disableLinks() /*-{
		$wnd.jQuery('a.linkCell').click(function (e) {
			e.preventDefault();
		});
	}-*/;

	/* ======== FIX STANDARD ADD/INSERT/REMOVE COLUMN BEHAVIOR
				IN ORDER TO SUPPORT REDRAWS ON COLUMN CHANGE ============== */

	// TODO - SafeHtml headers support

	@Override
	public void addColumn(Column<T, ?> col) {
		addColumn(col, "");
	}

	@Override
	public void addColumn(Column<T, ?> col, String headerString) {
		addColumn(col, new TextHeader(headerString), null);
	}

	@Override
	public void addColumn(Column<T, ?> col, String headerString, String footerString) {
		addColumn(col, new TextHeader(headerString), new TextHeader(footerString));
	}

	@Override
	public void addColumn(Column<T, ?> col, Header<?> header) {
		addColumn(col, header, null);
	}

	@Override
	public void addColumn(Column<T, ?> col, Header<?> header, Header<?> footer) {
		insertColumn(columns.size(), col, header, footer);
	}

	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col) {
		insertColumn(beforeIndex, col, "");
	}

	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col, String headerString) {
		insertColumn(beforeIndex, col, new TextHeader(headerString));
	}

	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col, String headerString, String footerString) {
		insertColumn(beforeIndex, col, new TextHeader(headerString), new TextHeader(footerString));
	}

	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col, Header<?> header) {
		insertColumn(beforeIndex, col, header, null);
	}

	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col, Header<?> header, Header<?> footer) {
		columns.add(beforeIndex, col);
		columnHeaders.put(col, header);
		columnFooters.put(col, footer);
		super.insertColumn(beforeIndex, col, header, footer);
	}

	@Override
	public void changeLanguage() {

		// TODO - redraw headers which can't be simple string, must be also translatable
		this.flush();
		this.redraw();

	}

}