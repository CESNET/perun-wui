package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.Facility;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.cells.PerunLinkCell;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@link ColumnProvider ColumnProvider}
 * for {@link Facility Facility}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FacilityColumnProvider extends ColumnProvider<Facility> {

	private static ArrayList<PerunColumnType> defaultColumns = new ArrayList<>();

	static {
		defaultColumns.add(PerunColumnType.ID);
		defaultColumns.add(PerunColumnType.NAME);
		defaultColumns.add(PerunColumnType.DESCRIPTION);
		defaultColumns.add(PerunColumnType.FACILITY_OWNERS);
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
	public PerunDataGrid.PerunSelectionEvent<Facility> getDefaultSelectionEvent() {

		// by default all VOs can be selected
		return new PerunDataGrid.PerunSelectionEvent<Facility>() {
			@Override
			public boolean canSelectObject(Facility object) {
				return (object != null);
			}
		};

	}

	@Override
	public PerunDataGrid.PerunFilterEvent<Facility> getDefaultFilterEvent() {
		return new PerunDataGrid.PerunFilterEvent<Facility>() {
			@Override
			public boolean filterOnObject(Set<PerunColumnType> columnTypeSet, String text, Facility object) {

				if (object == null || text == null) return false;

				if (columnTypeSet == null || columnTypeSet.isEmpty()) {
					columnTypeSet = new HashSet<PerunColumnType>(Arrays.asList(PerunColumnType.NAME, PerunColumnType.DESCRIPTION, PerunColumnType.FACILITY_OWNERS));
				}

				for (PerunColumnType columnType : columnTypeSet) {
					if (columnType.equals(PerunColumnType.ID) && Integer.toString(object.getId()).contains(text)) {
						return true;
					} else if (columnType.equals(PerunColumnType.NAME) && object.getName() != null &&
							object.getName().toLowerCase().contains(text.toLowerCase())) {
						return true;
					} else if (columnType.equals(PerunColumnType.DESCRIPTION) && object.getDescription() != null &&
							object.getDescription().toLowerCase().contains(text.toLowerCase())) {
						return true;
					} else if (columnType.equals(PerunColumnType.FACILITY_OWNERS) && object.getTechnicalOwnersString() != null &&
							object.getTechnicalOwnersString().toLowerCase().contains(text.toLowerCase())) {
						return true;
					}
				}
				return false;
			}
		};
	}


	@Override
	public void addColumnToTable(PerunDataGrid<Facility> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<Facility> table, PerunColumnType column, FieldUpdater<Facility, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<Facility> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(final PerunDataGrid<Facility> table, PerunColumnType column, FieldUpdater<Facility, C> updater, int widthInPixels) {

		if (PerunColumnType.ID.equals(column)) {

			PerunColumn<Facility, String> idColumn = createColumn(column,
					new GetValue<Facility, String>() {
						@Override
						public String getValue(Facility object) {
							return String.valueOf(object.getId());
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<Facility>(PerunColumnType.ID));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Id");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "10%");
			}

		} else if (PerunColumnType.NAME.equals(column)) {

			PerunColumn<Facility, PerunLinkCell.PerunLinkCellHandler<Facility>> nameColumn = createColumn(
					new PerunLinkCell<PerunLinkCell.PerunLinkCellHandler<Facility>>(),
					column,
					new GetValue<Facility, PerunLinkCell.PerunLinkCellHandler<Facility>>() {
						@Override
						public PerunLinkCell.PerunLinkCellHandler<Facility> getValue(final Facility object) {
							return new PerunLinkCell.PerunLinkCellHandler<Facility>() {
								@Override
								public boolean isAuthorized() {
									return PerunSession.getInstance().isFacilityAdmin(object.getId());
								}

								@Override
								public String getUrl() {
									return PlaceTokens.FACILITY_DETAIL+";id="+object.getId();
								}

								@Override
								public void onClick() {
								}

								@Override
								public String getCellValue() {
									return object.getName();
								}

								@Override
								public Facility getObject() {
									return object;
								}

							};
						}
					},
					this.<PerunLinkCell.PerunLinkCellHandler<Facility>>getFieldUpdater(table)
			);

			nameColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(nameColumn, new PerunComparator<Facility>(PerunColumnType.NAME));
			nameColumn.setColumnType(column);
			table.addColumn(nameColumn, "Name");
			if (widthInPixels > 0) {
				table.setColumnWidth(nameColumn, widthInPixels + "px");
			} else {
				// by default not with fixed width
				table.setColumnWidth(nameColumn, "30%");
			}

		} else if (PerunColumnType.DESCRIPTION.equals(column)) {

			PerunColumn<Facility, String> shortNameColumn = createColumn(
					column,
					new GetValue<Facility, String>() {
						@Override
						public String getValue(Facility object) {
							return object.getDescription();
						}
					},
					this.<String>getFieldUpdater(table)
			);

			shortNameColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(shortNameColumn, new PerunComparator<Facility>(PerunColumnType.DESCRIPTION));
			shortNameColumn.setColumnType(column);
			table.addColumn(shortNameColumn, "Description");
			if (widthInPixels > 0) {
				table.setColumnWidth(shortNameColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(shortNameColumn, "30%");
			}

		} else if (PerunColumnType.FACILITY_OWNERS.equals(column)) {

			PerunColumn<Facility, String> ownersColumn = createColumn(
					column,
					new GetValue<Facility, String>() {
						@Override
						public String getValue(Facility object) {
							String owners = object.getTechnicalOwnersString();
							if (owners == null) return "";
							return owners;
						}
					},
					this.<String>getFieldUpdater(table)
			);

			ownersColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(ownersColumn, new PerunComparator<Facility>(PerunColumnType.FACILITY_OWNERS));
			ownersColumn.setColumnType(column);
			table.addColumn(ownersColumn, "Technical owners");
			if (widthInPixels > 0) {
				table.setColumnWidth(ownersColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(ownersColumn, "30%");
			}

		}

	}

}