package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.cells.PerunLinkCell;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;

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
		columns.add(PerunColumnType.SHORT_NAME);
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
			public boolean filterOnObject(String text, Vo object) {
				if (object != null) {
					if (object.getName().toLowerCase().startsWith(text.toLowerCase()) ||
							object.getShortName().toLowerCase().startsWith(text.toLowerCase()))
						return true;
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
									return ""; //VoDetailTabItem.getStaticUrl(object);
								}

								@Override
								public void onClick() {
									// FIXME - PerunSession.getInstance().getContentManager().setState(new State(getUrl(),vo));
									//PerunWui.getContent().setTab(new VoDetailTabItem(object));
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
			nameColumn.setColumnType(column);
			table.addColumn(nameColumn, "Name");
			if (widthInPixels > 0) {
				table.setColumnWidth(nameColumn, widthInPixels + "px");
			} else {
				// by default not with fixed width
				table.setColumnWidth(nameColumn, "100%");
			}

		} else if (PerunColumnType.SHORT_NAME.equals(column)) {

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