package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Implementation of {@link ColumnProvider ColumnProvider}
 * for {@link Group Group} to display info to user.
 *
 * @author Vojtěch Sassmann <vojtech.sassmann@gmail.com>
 */
public class GroupColumnProvider extends ColumnProvider<Group> {

	private static ArrayList<PerunColumnType> defaultColumns = new ArrayList<>();

	static {
		defaultColumns.add(PerunColumnType.NAME);
		defaultColumns.add(PerunColumnType.DESCRIPTION);
	}

	private PerunTranslation translation = GWT.create(PerunTranslation.class);

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
	public PerunDataGrid.PerunSelectionEvent<Group> getDefaultSelectionEvent() {

		return object -> (object != null);
	}

	@Override
	public PerunDataGrid.PerunFilterEvent<Group> getDefaultFilterEvent() {
		return (columnTypeSet, text, object) -> {
			if (object == null || text == null) return false;

			if (columnTypeSet == null || columnTypeSet.isEmpty()) {
				columnTypeSet = new HashSet<>(Collections.singletonList(PerunColumnType.NAME));
			}
			for (PerunColumnType columnType : columnTypeSet) {
				if (columnType.equals(PerunColumnType.ID) && Integer.toString(object.getId()).contains(text)) {
					return true;
				} else if (columnType.equals(PerunColumnType.NAME) && object.getName() != null &&
								   object.getName().toLowerCase().contains(text.toLowerCase())) {
					return true;
				}
			}
			return false;
		};
	}

	@Override
	public void addColumnToTable(PerunDataGrid<Group> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<Group> table, PerunColumnType column, FieldUpdater<Group, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<Group> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<Group> table, PerunColumnType column, FieldUpdater<Group, C> updater, int widthInPixels) {
		switch (column) {

			case NAME:
				PerunColumn<Group, String> nameColumn = createColumn(column,
						group -> {
							if (group.getShortName() == null) {
								return "-";
							} else {
								return group.getShortName();
							}
						}, this.getFieldUpdater(table)
				);

				nameColumn.setSortable(true);
				table.getColumnSortHandler().setComparator(nameColumn, new PerunComparator<Group>(PerunColumnType.NAME));
				nameColumn.setColumnType(column);
				table.addColumn(nameColumn, translation.title());
				if (widthInPixels > 0) {
					table.setColumnWidth(nameColumn, widthInPixels + "px");
				} else {
					// by default not with fixed width
					table.setColumnWidth(nameColumn, "45%");
				}
				break;

			case DESCRIPTION:
				PerunColumn<Group, String> descriptionColumn = createColumn(column, GeneralObject::getDescription,
						this.getFieldUpdater(table)
				);

				descriptionColumn.setSortable(true);
				table.getColumnSortHandler().setComparator(descriptionColumn, new PerunComparator<Group>(PerunColumnType.DESCRIPTION));
				descriptionColumn.setColumnType(column);
				table.addColumn(descriptionColumn, translation.description());
				if (widthInPixels > 0) {
					table.setColumnWidth(descriptionColumn, widthInPixels + "px");
				} else {
					// by default not with fixed width
					table.setColumnWidth(descriptionColumn, "55%");
				}
				break;
		}
	}
}
