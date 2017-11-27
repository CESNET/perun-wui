package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.RichPublication;
import cz.metacentrum.perun.wui.model.beans.RichResource;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Implementation of {@link cz.metacentrum.perun.wui.model.ColumnProvider ColumnProvider}
 * for {@link cz.metacentrum.perun.wui.model.beans.RichResource RichResource}.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class ResourceColumnProvider extends ColumnProvider<RichResource> {

	private static ArrayList<PerunColumnType> defaultColumns = new ArrayList<>();

	static {
		defaultColumns.add(PerunColumnType.NAME);
		defaultColumns.add(PerunColumnType.FACILITY_NAME);
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
	public PerunDataGrid.PerunSelectionEvent<RichResource> getDefaultSelectionEvent() {
		return object -> (object != null);
	}

	@Override
	public PerunDataGrid.PerunFilterEvent<RichResource> getDefaultFilterEvent() {
		return (columnTypeSet, text, object) -> {
			if (object == null || text == null) return false;

			if (columnTypeSet == null || columnTypeSet.isEmpty()) {
				columnTypeSet = new HashSet<>(Collections.singletonList(PerunColumnType.NAME));
			}
			for (PerunColumnType columnType : columnTypeSet) {
				if (columnType.equals(PerunColumnType.NAME) && object.getName() != null &&
							object.getName().toLowerCase().contains(text.toLowerCase())) {
					return true;
				}
			}
			return false;
		};
	}

	@Override
	public void addColumnToTable(PerunDataGrid<RichResource> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<RichResource> table, PerunColumnType column, FieldUpdater<RichResource, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<RichResource> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<RichResource> table, PerunColumnType column, FieldUpdater<RichResource, C> updater, int widthInPixels) {
		switch (column) {
			case NAME:
				PerunColumn<RichResource, String> nameColumn = createColumn(column,
						richResource -> String.valueOf(richResource.getName()), this.getFieldUpdater(table)
				);
				nameColumn.setSortable(true);
				table.getColumnSortHandler().setComparator(nameColumn, new PerunComparator<RichPublication>(PerunColumnType.NAME));
				nameColumn.setColumnType(column);
				table.addColumn(nameColumn, translation.name());
				if (widthInPixels > 0) {
					table.setColumnWidth(nameColumn, widthInPixels + "px");
				} else {
					table.setColumnWidth(nameColumn, "30%");
				}
				break;
			case FACILITY_NAME:
				PerunColumn<RichResource, String> facilityNameColumn = createColumn(column,
						richResource -> String.valueOf(richResource.getFacility().getName()), this.getFieldUpdater(table)
				);
				facilityNameColumn.setSortable(true);
				table.getColumnSortHandler().setComparator(facilityNameColumn, new PerunComparator<RichPublication>(PerunColumnType.FACILITY_NAME));
				facilityNameColumn.setColumnType(column);
				table.addColumn(facilityNameColumn, translation.facilityName());
				if (widthInPixels > 0) {
					table.setColumnWidth(facilityNameColumn, widthInPixels + "px");
				} else {
					table.setColumnWidth(facilityNameColumn, "30%");
				}
				break;
			case DESCRIPTION:
				PerunColumn<RichResource, String> descriptionColumn = createColumn(column,
						richResource -> String.valueOf(richResource.getDescription()), this.getFieldUpdater(table)
				);
				descriptionColumn.setSortable(true);
				table.getColumnSortHandler().setComparator(descriptionColumn, new PerunComparator<RichPublication>(PerunColumnType.DESCRIPTION));
				descriptionColumn.setColumnType(column);
				table.addColumn(descriptionColumn, translation.description());
				if (widthInPixels > 0) {
					table.setColumnWidth(descriptionColumn, widthInPixels + "px");
				} else {
					table.setColumnWidth(descriptionColumn, "40%");
				}
				break;
		}
	}
}
