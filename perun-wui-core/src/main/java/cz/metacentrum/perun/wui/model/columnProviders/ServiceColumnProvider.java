package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.Service;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.cells.PerunLinkCell;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.Set;

/**
 * Implementation of {@link cz.metacentrum.perun.wui.model.ColumnProvider ColumnProvider}
 * for {@link cz.metacentrum.perun.wui.model.beans.Service Service}.
 *
 * @author Kristyna Kysela
 */
public class ServiceColumnProvider extends ColumnProvider<Service> {

	@Override
	public ArrayList<PerunColumnType> getDefaultColumns() {

		ArrayList<PerunColumnType> columns = new ArrayList<>();
		columns.add(PerunColumnType.ID);
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
	public PerunDataGrid.PerunSelectionEvent<Service> getDefaultSelectionEvent() {

		return new PerunDataGrid.PerunSelectionEvent<Service>() {
			@Override
			public boolean canSelectObject(Service object) {
				return (object != null);
			}
		};
	}

	@Override
	public PerunDataGrid.PerunFilterEvent<Service> getDefaultFilterEvent() {

		return new PerunDataGrid.PerunFilterEvent<Service>() {
			@Override
			public boolean filterOnObject(Set<PerunColumnType> columnTypeSet, String text, Service object) {
				if (object != null) {
					if (columnTypeSet.isEmpty() && object.getName().toLowerCase().contains(text.toLowerCase())) {
						return true;
					}
					for (PerunColumnType columnType : columnTypeSet) {
						if (columnType.equals(PerunColumnType.ID) && Integer.toString(object.getId()).toLowerCase().startsWith(text.toLowerCase())) {
							return true;
						} else if (columnType.equals(PerunColumnType.NAME) && object.getName().toLowerCase().contains(text.toLowerCase())) {
							return true;
						}
					}
				}
				return false;
			}
		};

	}

	@Override
	public void addColumnToTable(PerunDataGrid<Service> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<Service> table, PerunColumnType column, FieldUpdater<Service, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<Service> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<Service> table, PerunColumnType column, FieldUpdater<Service, C> updater, int widthInPixels) {

		if (PerunColumnType.ID.equals(column)) {

			PerunColumn<Service, String> idColumn = createColumn(column,
					new GetValue<Service, String>() {
						@Override
						public String getValue(Service object) {
							return String.valueOf(object.getId());
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<Service>(PerunColumnType.ID));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Id");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "10%");
			}

		} else if (PerunColumnType.NAME.equals(column)) {

			PerunColumn<Service, PerunLinkCell.PerunLinkCellHandler<Service>> nameColumn = createColumn(
					new PerunLinkCell<PerunLinkCell.PerunLinkCellHandler<Service>>(),
					column,
					new GetValue<Service, PerunLinkCell.PerunLinkCellHandler<Service>>() {
						@Override
						public PerunLinkCell.PerunLinkCellHandler<Service> getValue(final Service object) {
							return new PerunLinkCell.PerunLinkCellHandler<Service>() {
								@Override
								public boolean isAuthorized() {
									return PerunSession.getInstance().isPerunAdmin();
								}

								@Override
								public String getUrl() {
									return PlaceTokens.SERVICE_DETAIL + ";id=" + object.getId();
								}

								@Override
								public void onClick() {
								}

								@Override
								public String getCellValue() {
									return object.getName();
								}

								@Override
								public Service getObject() {
									return object;
								}

							};
						}
					},
					this.<PerunLinkCell.PerunLinkCellHandler<Service>>getFieldUpdater(table)
			);

			nameColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(nameColumn, new PerunComparator<Service>(PerunColumnType.NAME));
			nameColumn.setColumnType(column);
			table.addColumn(nameColumn, "Name");
			if (widthInPixels > 0) {
				table.setColumnWidth(nameColumn, widthInPixels + "px");
			} else {
				// by default not with fixed width
				table.setColumnWidth(nameColumn, "100%");
			}

		}
	}
}
