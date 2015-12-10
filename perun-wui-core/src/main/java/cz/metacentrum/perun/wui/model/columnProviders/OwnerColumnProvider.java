package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.thirdparty.guava.common.collect.Sets;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.Owner;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@link ColumnProvider ColumnProvider}
 * for {@link Owner Owner}.
 *
 * @author Kristyna Kysela
 */
public class OwnerColumnProvider extends ColumnProvider<Owner> {

	private static ArrayList<PerunColumnType> defaultColumns = new ArrayList<>();

	static {
		defaultColumns.add(PerunColumnType.ID);
		defaultColumns.add(PerunColumnType.NAME);
		defaultColumns.add(PerunColumnType.OWNER_CONTACT);
		defaultColumns.add(PerunColumnType.OWNER_TYPE);
	}

	@Override
	public ArrayList<PerunColumnType> getDefaultColumns() {
		return defaultColumns;
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
	public PerunDataGrid.PerunSelectionEvent<Owner> getDefaultSelectionEvent() {
		return new PerunDataGrid.PerunSelectionEvent<Owner>() {
			@Override
			public boolean canSelectObject(Owner object) {
				return (object != null);
			}
		};
	}

	@Override
	public PerunDataGrid.PerunFilterEvent<Owner> getDefaultFilterEvent() {
		return new PerunDataGrid.PerunFilterEvent<Owner>() {
			@Override
			public boolean filterOnObject(Set<PerunColumnType> columnTypeSet, String text, Owner object) {

				if (object == null || text == null) return false;

				if (columnTypeSet == null || columnTypeSet.isEmpty()) {
					columnTypeSet = new HashSet<PerunColumnType>(Arrays.asList(PerunColumnType.NAME));
				}

				for (PerunColumnType columnType : columnTypeSet) {
					if (columnType.equals(PerunColumnType.ID) && Integer.toString(object.getId()).contains(text)) {
						return true;
					} else if (columnType.equals(PerunColumnType.NAME) && object.getName() != null &&
							object.getName().toLowerCase().contains(text.toLowerCase())) {
						return true;
					} else if (columnType.equals(PerunColumnType.OWNER_CONTACT) && object.getContact() != null &&
							object.getContact().toLowerCase().contains(text.toLowerCase())) {
						return true;
					} else if (columnType.equals(PerunColumnType.OWNER_TYPE) && object.getType() != null &&
							object.getType().toLowerCase().contains(text.toLowerCase())) {
						return true;
					}
				}
				return false;
			}
		};
	}

	@Override
	public void addColumnToTable(PerunDataGrid<Owner> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<Owner> table, PerunColumnType column, FieldUpdater<Owner, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<Owner> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<Owner> table, PerunColumnType column, FieldUpdater<Owner, C> updater, int widthInPixels) {

		if (PerunColumnType.ID.equals(column)) {

			PerunColumn<Owner, String> idColumn = createColumn(column,
					new GetValue<Owner, String>() {
						@Override
						public String getValue(Owner object) {
							return String.valueOf(object.getId());
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<Owner>(PerunColumnType.ID));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Id");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "10%");
			}

		}else if(PerunColumnType.NAME.equals(column)){

			PerunColumn<Owner, String> idColumn = createColumn(column,
					new GetValue<Owner, String>() {
						@Override
						public String getValue(Owner object) {
							return String.valueOf(object.getName());
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<Owner>(PerunColumnType.NAME));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Name");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "50%");
			}

		}else if(PerunColumnType.OWNER_CONTACT.equals(column)){

			PerunColumn<Owner, String> idColumn = createColumn(column,
					new GetValue<Owner, String>() {
						@Override
						public String getValue(Owner object) {
							return object.getContact();
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<Owner>(PerunColumnType.OWNER_CONTACT));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Contact");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "50%");
			}

		}else if(PerunColumnType.OWNER_TYPE.equals(column)){

			PerunColumn<Owner, String> idColumn = createColumn(column,
					new GetValue<Owner, String>() {
						@Override
						public String getValue(Owner object) {
							return object.getType();
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<Owner>(PerunColumnType.OWNER_TYPE));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Type");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "30%");
			}

		}
	}
}
