package cz.metacentrum.perun.wui.registrar.model;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarPlaceTokens;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@link cz.metacentrum.perun.wui.model.ColumnProvider ColumnProvider}
 * for {@link cz.metacentrum.perun.wui.model.beans.Application Application} which is specific to registrar.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ApplicationColumnProvider extends cz.metacentrum.perun.wui.model.columnProviders.ApplicationColumnProvider {

	private PerunRegistrarTranslation translation = GWT.create(PerunRegistrarTranslation.class);

	private static ArrayList<PerunColumnType> defaultColumns = new ArrayList<>();

	static {
		defaultColumns.add(PerunColumnType.CREATED_AT);
		defaultColumns.add(PerunColumnType.APPLICATION_STATE);
		defaultColumns.add(PerunColumnType.APPLICATION_TYPE);
		defaultColumns.add(PerunColumnType.APPLICATION_DETAIL);
		defaultColumns.add(PerunColumnType.APPLICATION_VO_NAME);
		defaultColumns.add(PerunColumnType.APPLICATION_GROUP_NAME);
	}

	@Override
	public ArrayList<PerunColumnType> getDefaultColumns() {
		return defaultColumns;
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
			public boolean filterOnObject(Set<PerunColumnType> columnTypeSet, String text, Application object) {
				if (object == null || text == null) return false;

				// TODO - we should re-thing this

				if (columnTypeSet == null || columnTypeSet.isEmpty()) {
					columnTypeSet = new HashSet<PerunColumnType>(Arrays.asList(PerunColumnType.APPLICATION_USER, PerunColumnType.APPLICATION_VO_NAME, PerunColumnType.APPLICATION_GROUP_NAME));
				}
				for (PerunColumnType columnType : columnTypeSet) {
					if (columnType.equals(PerunColumnType.ID) && Integer.toString(object.getId()).contains(text)) {
						return true;
					} else if (columnType.equals(PerunColumnType.APPLICATION_USER)) {

						if (object.getUser() != null) {
							return object.getUser().getFullName().toLowerCase().contains(text.toLowerCase());
						} else {
							if ((Utils.convertCertCN(object.getCreatedBy()) + " / " + Utils.translateIdp(Utils.convertCertCN(object.getExtSourceName()))).toLowerCase().contains(text.toLowerCase())) {
								return true;
							}
						}

					} else if (columnType.equals(PerunColumnType.APPLICATION_VO_NAME) && object.getVo() != null) {
						if (object.getVo().getName().toLowerCase().contains(text.toLowerCase()) ||
								object.getVo().getShortName().toLowerCase().contains(text.toLowerCase())) return true;
					} else if (columnType.equals(PerunColumnType.APPLICATION_GROUP_NAME) && object.getGroup() != null &&
							object.getGroup().getShortName().toLowerCase().contains(text.toLowerCase())) {
						return true;
					}
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
			table.addColumn(createdAtColumn, translation.submittedOn());
			if (widthInPixels > 0) {
				table.setColumnWidth(createdAtColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(createdAtColumn, "10%");
			}

		} else if (PerunColumnType.APPLICATION_STATE.equals(column)) {

			PerunColumn<Application, String> stateColumn = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							return String.valueOf(object.getTranslatedState());
						}
					}, this.<String>getFieldUpdater(table)
			);
			stateColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(stateColumn, new Comparator<Application>() {
				@Override
				public int compare(Application o1, Application o2) {
					if (o1 == null) return -1;
					if (o2 == null) return 1;
					return PerunComparator.getNativeComparator().compare(o1.getTranslatedState(), o2.getTranslatedState());
				}
			});
			stateColumn.setColumnType(column);
			table.addColumn(stateColumn, translation.state());
			if (widthInPixels > 0) {
				table.setColumnWidth(stateColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(stateColumn, "10%");
			}

		} else if (PerunColumnType.APPLICATION_TYPE.equals(column)) {

			PerunColumn<Application, String> typeColumn = createColumn(column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							return String.valueOf(object.getTranslatedType());
						}
					}, this.<String>getFieldUpdater(table)
			);
			typeColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(typeColumn, new Comparator<Application>() {
				@Override
				public int compare(Application o1, Application o2) {
					if (o1 == null) return -1;
					if (o2 == null) return 1;
					return PerunComparator.getNativeComparator().compare(o1.getTranslatedType(), o2.getTranslatedType());
				}
			});
			typeColumn.setColumnType(column);
			table.addColumn(typeColumn, translation.type());
			if (widthInPixels > 0) {
				table.setColumnWidth(typeColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(typeColumn, "10%");
			}

		} if (PerunColumnType.APPLICATION_DETAIL.equals(column)) {

			ButtonCell buttonCell = new ButtonCell(IconType.FILE_TEXT_O);
			PerunColumn<Application, String> appDetailButtonColumn = createColumn(buttonCell, column,
					new GetValue<Application, String>() {
						@Override
						public String getValue(Application object) {
							return translation.showDetail();
						}
					}, new FieldUpdater<Application, String>() {
						@Override
						public void update(int index, Application object, String value) {
							History.newItem(PerunRegistrarPlaceTokens.APP_DETAIL+";id="+object.getId());
						}
					}
			);
			appDetailButtonColumn.setColumnType(column);
			table.addColumn(appDetailButtonColumn, "");
			if (widthInPixels > 0) {
				table.setColumnWidth(appDetailButtonColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(appDetailButtonColumn, "10%");
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
			table.addColumn(voColumn, translation.virtualOrganization());
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
			table.addColumn(groupColumn, translation.group());
			if (widthInPixels > 0) {
				table.setColumnWidth(groupColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(groupColumn, "20%");
			}

		}

	}

}