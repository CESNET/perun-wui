package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;

/**
 * Implementation of {@link cz.metacentrum.perun.wui.model.ColumnProvider ColumnProvider}
 * for {@link cz.metacentrum.perun.wui.model.beans.Application Application}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ApplicationColumnProvider extends ColumnProvider<Application> {

	@Override
	public ArrayList<PerunColumnType> getDefaultColumns() {

		ArrayList<PerunColumnType> columns = new ArrayList<>();
		columns.add(PerunColumnType.CREATED_AT);
		columns.add(PerunColumnType.APPLICATION_STATE);
		columns.add(PerunColumnType.APPLICATION_TYPE);
		columns.add(PerunColumnType.APPLICATION_USER);
		columns.add(PerunColumnType.APPLICATION_LOA);
		columns.add(PerunColumnType.APPLICATION_VO_NAME);
		columns.add(PerunColumnType.MODIFIED_BY);

		return columns;

	}

	@Override
	public PerunColumnType getDefaultSortColumn() {
		return PerunColumnType.CREATED_AT;
	}

	@Override
	public boolean isDefaultSortColumnDescending() {
		return true;
	}

	@Override
	public PerunDataGrid.PerunSelectionEvent<Application> getDefaultSelectionEvent() {

		// by default all Applications can be selected
		return new PerunDataGrid.PerunSelectionEvent<Application>() {
			@Override
			public boolean canSelectObject(Application object) {
				return false;
			}
		};

	}

	@Override
	public PerunDataGrid.PerunFilterEvent<Application> getDefaultFilterEvent() {
		return new PerunDataGrid.PerunFilterEvent<Application>() {
			@Override
			public boolean filterOnObject(String text, Application object) {
				if (object != null) {

					// compare by vo
					if (object.getVo().getName().toLowerCase().startsWith(text.toLowerCase()) ||
							object.getVo().getShortName().toLowerCase().startsWith(text.toLowerCase()))
						return true;

					// compare by group
					if (object.getGroup() != null && object.getGroup().getShortName().toLowerCase().startsWith(text.toLowerCase()))
						return true;

				}
				return false;
			}
		};
	}

	@Override
	public void addColumnToTable(PerunDataGrid<Application> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<Application> table, PerunColumnType column, FieldUpdater<Application, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<Application> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(final PerunDataGrid<Application> table, PerunColumnType column, FieldUpdater<Application, C> updater, int widthInPixels) {

		if (PerunColumnType.ID.equals(column)) {

			PerunColumn<Application, String> idColumn = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							return String.valueOf(object.getId());
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<Application>(PerunColumnType.ID));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Id");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "10%");
			}

		} else if (PerunColumnType.CREATED_AT.equals(column)) {

			PerunColumn<Application, String> createdAtColumn = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							if (object.getCreatedAt() != null) {
								// return only date
								return object.getCreatedAt().split(" ")[0];
							}
							return "N/A";
						}
					}, this.<String>getFieldUpdater(table)
			);
			createdAtColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(createdAtColumn, new PerunComparator<Application>(PerunColumnType.CREATED_AT));
			createdAtColumn.setColumnType(column);
			table.addColumn(createdAtColumn, "Submitted");
			if (widthInPixels > 0) {
				table.setColumnWidth(createdAtColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(createdAtColumn, "5%");
			}

		} else if (PerunColumnType.APPLICATION_STATE.equals(column)) {

			PerunColumn<Application, String> stateColumn = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							return String.valueOf(object.getState());
						}
					}, this.<String>getFieldUpdater(table)
			);
			stateColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(stateColumn, new PerunComparator<Application>(PerunColumnType.APPLICATION_STATE));
			stateColumn.setColumnType(column);
			table.addColumn(stateColumn, "State");
			if (widthInPixels > 0) {
				table.setColumnWidth(stateColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(stateColumn, "5%");
			}

		} else if (PerunColumnType.APPLICATION_TYPE.equals(column)) {

			PerunColumn<Application, String> typeColumn = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							return String.valueOf(object.getType());
						}
					}, this.<String>getFieldUpdater(table)
			);
			typeColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(typeColumn, new PerunComparator<Application>(PerunColumnType.APPLICATION_TYPE));
			typeColumn.setColumnType(column);
			table.addColumn(typeColumn, "Type");
			if (widthInPixels > 0) {
				table.setColumnWidth(typeColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(typeColumn, "5%");
			}

		} else if (PerunColumnType.APPLICATION_LOA.equals(column)) {

			PerunColumn<Application, String> typeColumn = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							return String.valueOf(object.getExtSourceLoa());
						}
					}, this.<String>getFieldUpdater(table)
			);
			typeColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(typeColumn, new PerunComparator<Application>(PerunColumnType.APPLICATION_LOA));
			typeColumn.setColumnType(column);
			table.addColumn(typeColumn, "LoA");
			if (widthInPixels > 0) {
				table.setColumnWidth(typeColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(typeColumn, "3%");
			}

		} else if (PerunColumnType.APPLICATION_VO_NAME.equals(column)) {

			PerunColumn<Application, String> voColumn = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							return String.valueOf(object.getVo().getName());
						}
					}, this.<String>getFieldUpdater(table)
			);
			voColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(voColumn, new PerunComparator<Application>(PerunColumnType.APPLICATION_VO_NAME));
			voColumn.setColumnType(column);
			table.addColumn(voColumn, "Project");
			if (widthInPixels > 0) {
				table.setColumnWidth(voColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(voColumn, "20%");
			}

		} else if (PerunColumnType.APPLICATION_GROUP_NAME.equals(column)) {

			PerunColumn<Application, String> groupColumn = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							if (object.getGroup() != null) {
								return String.valueOf(object.getGroup().getShortName());
							} else {
								return "";
							}
						}
					}, this.<String>getFieldUpdater(table)
			);
			groupColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(groupColumn, new PerunComparator<Application>(PerunColumnType.APPLICATION_GROUP_NAME));
			groupColumn.setColumnType(column);
			table.addColumn(groupColumn, "Group");
			if (widthInPixels > 0) {
				table.setColumnWidth(groupColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(groupColumn, "20%");
			}

		} else if (PerunColumnType.MODIFIED_BY.equals(column)) {

			PerunColumn<Application, String> modifiedByColumn = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							return String.valueOf(Utils.convertCertCN(object.getModifiedBy()));
						}
					}, this.<String>getFieldUpdater(table)
			);
			modifiedByColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(modifiedByColumn, new PerunComparator<Application>(PerunColumnType.MODIFIED_BY));
			modifiedByColumn.setColumnType(column);
			table.addColumn(modifiedByColumn, "Modified by");
			if (widthInPixels > 0) {
				table.setColumnWidth(modifiedByColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(modifiedByColumn, "10%");
			}

		} else if (PerunColumnType.APPLICATION_USER.equals(column)) {

			PerunColumn<Application, String> idColumn = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							if (object.getUser() != null) {
								return object.getUser().getFullName();
							}
							return Utils.convertCertCN(object.getCreatedBy()) + " / " + Utils.translateIdp(Utils.convertCertCN(object.getExtSourceName()));
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<Application>(PerunColumnType.APPLICATION_USER));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Submitted by");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "10%");
			}

		}

	}

}