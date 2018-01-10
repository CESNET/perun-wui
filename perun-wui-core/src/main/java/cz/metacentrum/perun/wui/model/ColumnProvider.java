package cz.metacentrum.perun.wui.model;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;

/**
 * Abstract class defining methods for all column providers with base implementation of some of them.
 * <p/>
 * Column provider is used to provide default columns and other default behavior
 * to {@link cz.metacentrum.perun.wui.widgets.PerunDataGrid table} with respect to object's type.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public abstract class ColumnProvider<T extends JavaScriptObject> {

	/**
	 * Get list of default column types, in default order.
	 *
	 * @return list of column types in default order.
	 */
	public abstract ArrayList<PerunColumnType> getDefaultColumns();

	/**
	 * Get column, by which should be table sorted by default.
	 *
	 * @return column to sort table by
	 */
	public abstract PerunColumnType getDefaultSortColumn();

	/**
	 * Return TRUE if sort order for {@link #getDefaultSortColumn() default sort column}
	 * is descending. FALSE if ascending (default).
	 *
	 * @return TRUE = descending / FALSE = ascending
	 */
	public abstract boolean isDefaultSortColumnDescending();

	/**
	 * Return default {@link cz.metacentrum.perun.wui.widgets.PerunDataGrid.PerunSelectionEvent selection event}, which is used in
	 * {@link cz.metacentrum.perun.wui.widgets.PerunDataGrid table} to decide, if object can be selected or not.
	 *
	 * @return default selection event
	 */
	public abstract PerunDataGrid.PerunSelectionEvent<T> getDefaultSelectionEvent();

	/**
	 * Return default {@link cz.metacentrum.perun.wui.widgets.PerunDataGrid.PerunFilterEvent filter event}, which is used in
	 * {@link cz.metacentrum.perun.wui.widgets.PerunDataGrid table} to decide, on which property and by what rule, objects are filtered.
	 *
	 * @return default filtering  event
	 */
	public abstract PerunDataGrid.PerunFilterEvent<T> getDefaultFilterEvent();

	/**
	 * Create default column of specified type and add it to table. Default field updater is used.
	 * If column provider doesn't provide specified column, no column is added to table.
	 *
	 * @param table PerunDataGrid to add column to
	 * @param column type of column to add
	 */
	public abstract void addColumnToTable(PerunDataGrid<T> table, PerunColumnType column);

	/**
	 * Create default column of specified type and add it to table. Column will use custom fieldUpdater.
	 * If column provider doesn't provide specified column, no column is added to table.
	 *
	 * @param <C> class of data returned as value for cell (also used in updater)
	 * @param table PerunDataGrid to add column to
	 * @param column type of column to add
	 * @param updater custom field updater
	 */
	public abstract <C> void addColumnToTable(PerunDataGrid<T> table, PerunColumnType column, FieldUpdater<T, C> updater);

	/**
	 * Create default column of specified type and add it to table. Default field updater is used.
	 * If column provider doesn't provide specified column, no column is added to table.
	 *
	 * @param table PerunDataGrid to add column to
	 * @param column type of column to add
	 * @param widthInPixels custom column width
	 */
	public abstract void addColumnToTable(PerunDataGrid<T> table, PerunColumnType column, int widthInPixels);

	/**
	 * Create default column of specified type and add it to table. Column will use custom fieldUpdater.
	 * If column provider doesn't provide specified column, no column is added to table.
	 *
	 * @param table PerunDataGrid to add column to
	 * @param column type of column to add
	 * @param updater custom field updater
	 * @param widthInPixels custom column width
	 */
	public abstract <C> void addColumnToTable(PerunDataGrid<T> table, PerunColumnType column, FieldUpdater<T, C> updater, int widthInPixels);

	/**
	 * Provides default field updater implementation, which is to select / deselect object (row) in table.
	 * Selection decision is based on tables selection filter and fact, if selection is enabled at all.
	 *
	 * @param table table you want field updater for
	 * @param <C>   Second FieldUpdater parameter type (defined by column you want to use it)
	 * @return default field update
	 */
	public <C> FieldUpdater<T, C> getFieldUpdater(final PerunDataGrid<T> table) {

		return new FieldUpdater<T, C>() {
			@Override
			public void update(int i, T t, C c) {
				if (table.isSelectionEnabled() && table.getSelectionFilter().canSelectObject(t)) {
					table.getSelectionModel().setSelected(t, !table.getSelectionModel().isSelected(t));
				}
			}
		};

	}

	/**
	 * Interface for anonymous classes used to retrieve value from Row for Cell.
	 *
	 * @param <C> the cell type
	 */
	static public interface GetValue<T, C> {
		C getValue(T object);
	}

	/**
	 * Create standard column displaying String value retrieved from object.
	 * TextCell or ClickableTextCell is used to render data.
	 *
	 * @param type         store type of PerunColumn inside itself
	 * @param getter       implementation used to retrieve data from row for each cell
	 * @param fieldUpdater updater called when user clicks on cell
	 * @return PerunColumn displaying string value
	 */
	public static <T extends JavaScriptObject> PerunColumn<T, String> createColumn(PerunColumnType type, final GetValue<T, String> getter, final FieldUpdater<T, String> fieldUpdater) {

		Cell<String> cell;

		if (fieldUpdater == null) {
			cell = new TextCell();
		} else {
			// ensure cell calls fieldUpdater if exists
			cell = new ClickableTextCell() {
				@Override
				public boolean handlesSelection() {
					return true;
				}
			};
		}

		PerunColumn<T, String> column = new PerunColumn<T, String>(cell, type) {
			@Override
			public String getValue(T object) {
				return getter.getValue(object);
			}
		};

		if (fieldUpdater != null) {
			column.setFieldUpdater(fieldUpdater);
		}

		return column;

	}

	/**
	 * Create standard column with custom cell to render data.
	 *
	 * @param cell         cell implementation which renders data
	 * @param type         store type of PerunColumn inside itself
	 * @param getter       implementation used to retrieve data from row for each cell
	 * @param fieldUpdater updater called when user clicks on cell
	 * @return PerunColumn displaying string value
	 */
	public static <T extends JavaScriptObject, C> PerunColumn<T, C> createColumn(Cell<C> cell, PerunColumnType type, final GetValue<T, C> getter, final FieldUpdater<T, C> fieldUpdater) {

		PerunColumn<T, C> column = new PerunColumn<T, C>(cell, type) {
			@Override
			public C getValue(T object) {
				return getter.getValue(object);
			}
		};

		if (fieldUpdater != null) {
			column.setFieldUpdater(fieldUpdater);
		}

		return column;

	}

}