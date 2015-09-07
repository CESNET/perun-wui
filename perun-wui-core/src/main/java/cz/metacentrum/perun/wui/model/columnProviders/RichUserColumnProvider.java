package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;

/**
 * Implementation of {@link cz.metacentrum.perun.wui.model.ColumnProvider ColumnProvider}
 * for {@link cz.metacentrum.perun.wui.model.beans.Vo RichUser}.
 *
 * Created by Kristyna Kysela
 */
public class RichUserColumnProvider extends ColumnProvider<RichUser> {


	@Override
	public ArrayList<PerunColumnType> getDefaultColumns() {

		ArrayList<PerunColumnType> columns = new ArrayList<>();
		columns.add(PerunColumnType.ID);
		columns.add(PerunColumnType.NAME);
		columns.add(PerunColumnType.USER_ORGANIZATION);
		columns.add(PerunColumnType.USER_EMAIL);
		columns.add(PerunColumnType.USER_LOGIN);
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
	public PerunDataGrid.PerunSelectionEvent<RichUser> getDefaultSelectionEvent() {

		return new PerunDataGrid.PerunSelectionEvent<RichUser>() {
			@Override
			public boolean canSelectObject(RichUser object) {
				return (object != null);
			}
		};
	}

	@Override
	public PerunDataGrid.PerunFilterEvent<RichUser> getDefaultFilterEvent() {

		return new PerunDataGrid.PerunFilterEvent<RichUser>() {
			@Override
			public boolean filterOnObject(String text, RichUser object) {
				if (object != null) {
					if (object.getName().toLowerCase().startsWith(text.toLowerCase())) {
						return true;
					}
				}
				return false;
			}
		};
	}

	@Override
	public void addColumnToTable(PerunDataGrid<RichUser> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<RichUser> table, PerunColumnType column, FieldUpdater<RichUser, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<RichUser> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<RichUser> table, PerunColumnType column, FieldUpdater<RichUser, C> updater, int widthInPixels) {

		if (PerunColumnType.ID.equals(column)) {

			PerunColumn<RichUser, String> idColumn = createColumn(column,
					new GetValue<RichUser, String>() {
						@Override
						public String getValue(RichUser object) {
							return String.valueOf(object.getId());
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<RichUser>(PerunColumnType.ID));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Id");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "15%");
			}

		} else if (PerunColumnType.NAME.equals(column)) {

			PerunColumn<RichUser, String> nameColumn = createColumn(column,
					new GetValue<RichUser, String>() {
						@Override
						public String getValue(RichUser object) {
							if (object.getFullName() == null) {
								return "-";
							} else {
								return String.valueOf(object.getFullName());
							}
						}
					}, this.<String>getFieldUpdater(table)
			);

			nameColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(nameColumn, new PerunComparator<RichUser>(PerunColumnType.NAME));
			nameColumn.setColumnType(column);
			table.addColumn(nameColumn, "Name");
			if (widthInPixels > 0) {
				table.setColumnWidth(nameColumn, widthInPixels + "px");
			} else {
				// by default not with fixed width
				table.setColumnWidth(nameColumn, "70%");
			}

		} else if (PerunColumnType.USER_ORGANIZATION.equals(column)) {

			PerunColumn<RichUser, String> organizationColumn = createColumn(
					column,
					new GetValue<RichUser, String>() {
						@Override
						public String getValue(RichUser object) {
							if (object.getOrganization() == null) {
								return "-";
							} else {
								return String.valueOf(object.getOrganization());
							}
						}
					},
					this.<String>getFieldUpdater(table)
			);

			organizationColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(organizationColumn, new PerunComparator<RichUser>(PerunColumnType.USER_ORGANIZATION));
			organizationColumn.setColumnType(column);
			table.addColumn(organizationColumn, "Organization");
			if (widthInPixels > 0) {
				table.setColumnWidth(organizationColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(organizationColumn, "40%");
			}


		} else if (PerunColumnType.USER_EMAIL.equals(column)) {

			PerunColumn<RichUser, String> emailColumn = createColumn(
					column,
					new GetValue<RichUser, String>() {
						@Override
						public String getValue(RichUser object) {
							if (object.getPreferredEmail() == null) {
								return "-";
							} else {
								return String.valueOf(object.getPreferredEmail());
							}
						}
					},
					this.<String>getFieldUpdater(table)
			);

			emailColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(emailColumn, new PerunComparator<RichUser>(PerunColumnType.USER_EMAIL));
			emailColumn.setColumnType(column);
			table.addColumn(emailColumn, "Email");
			if (widthInPixels > 0) {
				table.setColumnWidth(emailColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(emailColumn, "30%");
			}

		} else if (PerunColumnType.USER_LOGIN.equals(column)) {

			PerunColumn<RichUser, String> loginColumn = createColumn(
					column,
					new GetValue<RichUser, String>() {
						@Override
						public String getValue(RichUser object) {
							return String.valueOf(object.getLogins());
						}
					},
					this.<String>getFieldUpdater(table)
			);

			loginColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(loginColumn, new PerunComparator<RichUser>(PerunColumnType.USER_LOGIN));
			loginColumn.setColumnType(column);
			table.addColumn(loginColumn, "Login");
			if (widthInPixels > 0) {
				table.setColumnWidth(loginColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(loginColumn, "30%");
			}
		} else if (PerunColumnType.USER_TYPE.equals(column)) {

			PerunColumn<RichUser, String> typeColumn = createColumn(
					column,
					new GetValue<RichUser, String>() {
						@Override
						public String getValue(RichUser object) {
							if (object.isServiceUser()) {
								return "service";
							}
							else if (!object.isServiceUser()){
								return "normal";
							}else{
								return "-";
							}


						}
					},
					this.<String>getFieldUpdater(table)
			);

			typeColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(typeColumn, new PerunComparator<RichUser>(PerunColumnType.USER_TYPE));
			typeColumn.setColumnType(column);
			table.addColumn(typeColumn, "Type");
			if (widthInPixels > 0) {
				table.setColumnWidth(typeColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(typeColumn, "20%");
			}
		}

	}
}
