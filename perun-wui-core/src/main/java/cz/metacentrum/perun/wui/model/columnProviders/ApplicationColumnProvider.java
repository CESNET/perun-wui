package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.Application;
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
		columns.add(PerunColumnType.APPLICATION_VO_NAME);
		columns.add(PerunColumnType.APPLICATION_GROUP_NAME);
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

					// compate by group
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

		if (PerunColumnType.CREATED_AT.equals(column)) {

			PerunColumn<Application, String> submitted = createColumn(column,
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
			submitted.setSortable(true);
			submitted.setColumnType(column);
			table.addColumn(submitted, "Submitted");
			if (widthInPixels > 0) {
				table.setColumnWidth(submitted, widthInPixels + "px");
			} else {
				table.setColumnWidth(submitted, "5%");
			}

			submitted.setSortable(true);


		} else if (PerunColumnType.APPLICATION_STATE.equals(column)) {

			PerunColumn<Application, String> state = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							return String.valueOf(object.getState());
						}
					}, this.<String>getFieldUpdater(table)
			);
			state.setSortable(true);
			state.setColumnType(column);
			table.addColumn(state, "State");
			if (widthInPixels > 0) {
				table.setColumnWidth(state, widthInPixels + "px");
			} else {
				table.setColumnWidth(state, "5%");
			}

		} else if (PerunColumnType.APPLICATION_TYPE.equals(column)) {

			PerunColumn<Application, String> type = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							return String.valueOf(object.getType());
						}
					}, this.<String>getFieldUpdater(table)
			);
			type.setSortable(true);
			type.setColumnType(column);
			table.addColumn(type, "Type");
			if (widthInPixels > 0) {
				table.setColumnWidth(type, widthInPixels + "px");
			} else {
				table.setColumnWidth(type, "5%");
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
			modifiedByColumn.setColumnType(column);
			table.addColumn(modifiedByColumn, "Modified by");
			if (widthInPixels > 0) {
				table.setColumnWidth(modifiedByColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(modifiedByColumn, "10%");
			}

		}

	}

}