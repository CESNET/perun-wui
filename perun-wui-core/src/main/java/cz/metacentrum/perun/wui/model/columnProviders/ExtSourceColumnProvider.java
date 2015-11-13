package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.ExtSource;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.Set;

/**
 * Implementation of {@link ColumnProvider ColumnProvider}
 * for {@link ExtSource ExtSource}.
 *
 * @author Kristyna Kysela
 */
public class ExtSourceColumnProvider extends ColumnProvider<ExtSource> {

	@Override
	public ArrayList<PerunColumnType> getDefaultColumns() {

		ArrayList<PerunColumnType> columns = new ArrayList<>();
		columns.add(PerunColumnType.ID);
		columns.add(PerunColumnType.EXT_SOURCE_TYPE);
		columns.add(PerunColumnType.NAME);
		return columns;
	}

	@Override
	public PerunColumnType getDefaultSortColumn() {
		return PerunColumnType.NAME;
	}

	@Override
	public boolean isDefaultSortColumnDescending() {
		return false;
	}

	@Override
	public PerunDataGrid.PerunSelectionEvent<ExtSource> getDefaultSelectionEvent() {

		return new PerunDataGrid.PerunSelectionEvent<ExtSource>() {
			@Override
			public boolean canSelectObject(ExtSource object) {
				return (object != null);
			}
		};
	}

	@Override
	public PerunDataGrid.PerunFilterEvent<ExtSource> getDefaultFilterEvent() {
		return new PerunDataGrid.PerunFilterEvent<ExtSource>() {
			@Override
			public boolean filterOnObject(Set<PerunColumnType> columnTypeSet, String text, ExtSource object) {
				if (object != null) {
					if (columnTypeSet.isEmpty() && object.getName().toLowerCase().contains(text.toLowerCase())) {
						return true;
					}
					for (PerunColumnType columnType : columnTypeSet) {
						if (columnType.equals(PerunColumnType.ID) && Integer.toString(object.getId()).toLowerCase().startsWith(text.toLowerCase())) {
							return true;
						} else if (columnType.equals(PerunColumnType.NAME) && object.getName().toLowerCase().contains(text.toLowerCase())) {
							return true;
						} else if (columnType.equals(PerunColumnType.EXT_SOURCE_TYPE) && object.getType().toLowerCase().contains(text.toLowerCase())) {
							return true;
						}
					}
				}
				return false;

			}
		};
	}

	@Override
	public void addColumnToTable(PerunDataGrid<ExtSource> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<ExtSource> table, PerunColumnType column, FieldUpdater<ExtSource, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<ExtSource> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<ExtSource> table, PerunColumnType column, FieldUpdater<ExtSource, C> updater, int widthInPixels) {

		if (PerunColumnType.ID.equals(column)) {

			PerunColumn<ExtSource, String> idColumn = createColumn(column,
					new GetValue<ExtSource, String>() {
						@Override
						public String getValue(ExtSource object) {
							return String.valueOf(object.getId());
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<ExtSource>(PerunColumnType.ID));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Id");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "10%");
			}

		} else if (PerunColumnType.NAME.equals(column)) {

			PerunColumn<ExtSource, String> idColumn = createColumn(column,
					new GetValue<ExtSource, String>() {
						@Override
						public String getValue(ExtSource object) {
							return String.valueOf(object.getName());
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<ExtSource>(PerunColumnType.NAME));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Name");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "90%");
			}

		} else if (PerunColumnType.EXT_SOURCE_TYPE.equals(column)) {

			PerunColumn<ExtSource, String> idColumn = createColumn(column,
					new GetValue<ExtSource, String>() {
						@Override
						public String getValue(ExtSource object) {
							return object.getType().substring(40).toUpperCase();
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<ExtSource>(PerunColumnType.EXT_SOURCE_TYPE));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Type");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "10%");
			}

		}
	}

}
