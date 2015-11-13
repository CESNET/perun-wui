package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.cells.PerunLinkCell;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.Set;

/**
 * Implementation of {@link cz.metacentrum.perun.wui.model.ColumnProvider ColumnProvider}
 * for {@link cz.metacentrum.perun.wui.model.beans.Vo VirtualOrganization}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class VoColumnProvider extends ColumnProvider<Vo> {

	@Override
	public ArrayList<PerunColumnType> getDefaultColumns() {

		ArrayList<PerunColumnType> columns = new ArrayList<>();
		columns.add(PerunColumnType.ID);
		columns.add(PerunColumnType.NAME);
		columns.add(PerunColumnType.VO_SHORT_NAME);
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
	public PerunDataGrid.PerunSelectionEvent<Vo> getDefaultSelectionEvent() {

		// by default all VOs can be selected
		return new PerunDataGrid.PerunSelectionEvent<Vo>() {
			@Override
			public boolean canSelectObject(Vo object) {
				return (object != null);
			}
		};

	}

	@Override
	public PerunDataGrid.PerunFilterEvent<Vo> getDefaultFilterEvent() {
		return new PerunDataGrid.PerunFilterEvent<Vo>() {
			@Override
			public boolean filterOnObject(Set<PerunColumnType> columnTypeSet, String text, Vo object) {
				if (object != null) {
					if (columnTypeSet.isEmpty() && object.getName().toLowerCase().contains(text.toLowerCase())) {
						return true;
					}
					for (PerunColumnType columnType : columnTypeSet) {
						if (columnType.equals(PerunColumnType.ID) && Integer.toString(object.getId()).toLowerCase().startsWith(text.toLowerCase())) {
							return true;
						} else if (columnType.equals(PerunColumnType.NAME) && object.getName().toLowerCase().contains(text.toLowerCase())) {
							return true;
						} else if (columnType.equals(PerunColumnType.VO_SHORT_NAME) && object.getShortName().toLowerCase().contains(text.toLowerCase())) {
							return true;
						}
					}
				}
				return false;
			}
		};
	}

	@Override
	public void addColumnToTable(PerunDataGrid<Vo> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<Vo> table, PerunColumnType column, FieldUpdater<Vo, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<Vo> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(final PerunDataGrid<Vo> table, PerunColumnType column, FieldUpdater<Vo, C> updater, int widthInPixels) {

		if (PerunColumnType.ID.equals(column)) {

			PerunColumn<Vo, String> idColumn = createColumn(column,
					new GetValue<Vo, String>() {
						@Override
						public String getValue(Vo object) {
							return String.valueOf(object.getId());
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<Vo>(PerunColumnType.ID));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Id");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "10%");
			}

		} else if (PerunColumnType.NAME.equals(column)) {

			PerunColumn<Vo, PerunLinkCell.PerunLinkCellHandler<Vo>> nameColumn = createColumn(
					new PerunLinkCell<PerunLinkCell.PerunLinkCellHandler<Vo>>(),
					column,
					new GetValue<Vo, PerunLinkCell.PerunLinkCellHandler<Vo>>() {
						@Override
						public PerunLinkCell.PerunLinkCellHandler<Vo> getValue(final Vo object) {
							return new PerunLinkCell.PerunLinkCellHandler<Vo>() {
								@Override
								public boolean isAuthorized() {
									return PerunSession.getInstance().isVoAdmin(object.getId()) || PerunSession.getInstance().isVoObserver(object.getId());
								}

								@Override
								public String getUrl() {
									return PlaceTokens.VOS_DETAIL+";id="+object.getId();
								}

								@Override
								public void onClick() {
								}

								@Override
								public String getCellValue() {
									return object.getName();
								}

								@Override
								public Vo getObject() {
									return object;
								}

							};
						}
					},
					this.<PerunLinkCell.PerunLinkCellHandler<Vo>>getFieldUpdater(table)
			);

			nameColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(nameColumn, new PerunComparator<Vo>(PerunColumnType.NAME));
			nameColumn.setColumnType(column);
			table.addColumn(nameColumn, "Name");
			if (widthInPixels > 0) {
				table.setColumnWidth(nameColumn, widthInPixels + "px");
			} else {
				// by default not with fixed width
				table.setColumnWidth(nameColumn, "100%");
			}

		} else if (PerunColumnType.VO_SHORT_NAME.equals(column)) {

			PerunColumn<Vo, String> shortNameColumn = createColumn(
					column,
					new GetValue<Vo, String>() {
						@Override
						public String getValue(Vo object) {
							return object.getShortName();
						}
					},
					this.<String>getFieldUpdater(table)
			);

			shortNameColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(shortNameColumn, new PerunComparator<Vo>(PerunColumnType.VO_SHORT_NAME));
			shortNameColumn.setColumnType(column);
			table.addColumn(shortNameColumn, "Short name");
			if (widthInPixels > 0) {
				table.setColumnWidth(shortNameColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(shortNameColumn, "20%");
			}

		}

	}

}