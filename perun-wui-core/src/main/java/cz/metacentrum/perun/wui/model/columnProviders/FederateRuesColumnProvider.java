package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.ExtSource;
import cz.metacentrum.perun.wui.model.beans.RichUserExtSource;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Implementation of {@link ColumnProvider ColumnProvider}
 * for {@link RichUserExtSource RichUserExtSource} for federated identities.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class FederateRuesColumnProvider extends ColumnProvider<RichUserExtSource> {

	private static ArrayList<PerunColumnType> defaultColumns = new ArrayList<>();

	private PerunTranslation translation = GWT.create(PerunTranslation.class);

	static {
		defaultColumns.add(PerunColumnType.NAME);
		defaultColumns.add(PerunColumnType.USER_LOGIN);
		defaultColumns.add(PerunColumnType.USER_EMAIL);
		defaultColumns.add(PerunColumnType.REMOVE);
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
	public PerunDataGrid.PerunSelectionEvent<RichUserExtSource> getDefaultSelectionEvent() {
		return object -> (object != null);
	}

	@Override
	public PerunDataGrid.PerunFilterEvent<RichUserExtSource> getDefaultFilterEvent() {
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
	public void addColumnToTable(PerunDataGrid<RichUserExtSource> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<RichUserExtSource> table, PerunColumnType column, FieldUpdater<RichUserExtSource, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<RichUserExtSource> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<RichUserExtSource> table, PerunColumnType column, FieldUpdater<RichUserExtSource, C> updater, int widthInPixels) {
		switch (column) {
			case NAME:
				PerunColumn<RichUserExtSource, String> nameCol = createColumn(column, rues -> {
					if (ExtSource.ExtSourceType.IDP.getType().equals(rues.getExtSource().getType())) {
						if (rues.getExtSource().getName().equals("https://extidp.cesnet.cz/idp/shibboleth")) {
							// hack our social IdP so we can tell from where identity is
							return Utils.translateIdp("@"+rues.getLogin().split("@")[1]);
						}
						return Utils.translateIdp(rues.getExtSource().getName());
					} else if (ExtSource.ExtSourceType.X509.getType().equals(rues.getExtSource().getType())) {
						return getCertParam(rues.getExtSource().getName(), "O") +
									   ", " +
									   getCertParam(rues.getExtSource().getName(), "CN");
					} else {
						return rues.getExtSource().getName();
					}
				}, this.getFieldUpdater(table));
				nameCol.setSortable(true);
				table.getColumnSortHandler().setComparator(nameCol, new PerunComparator<RichUserExtSource>(column));
				nameCol.setColumnType(column);
				table.addColumn(nameCol, translation.federatedIdp());
				if (widthInPixels > 0) {
					table.setColumnWidth(nameCol, widthInPixels + "px");
				} else {
					table.setColumnWidth(nameCol, "20%");
				}
				break;
			case USER_LOGIN:
				PerunColumn<RichUserExtSource, String> loginCol = createColumn(column, rues -> {
					if (ExtSource.ExtSourceType.IDP.getType().equals(rues.getExtSource().getType())) {
						return rues.getLogin().split("@")[0];
					} else if (ExtSource.ExtSourceType.X509.getType().equals(rues.getExtSource().getType())) {
						return Utils.convertCertCN(rues.getLogin());
					} else {
						return rues.getLogin();
					}
				}, this.getFieldUpdater(table));
				loginCol.setSortable(true);
				table.getColumnSortHandler().setComparator(loginCol, new PerunComparator<RichUserExtSource>(column));
				loginCol.setColumnType(column);
				table.addColumn(loginCol, translation.federatedLogin());
				if (widthInPixels > 0) {
					table.setColumnWidth(loginCol, widthInPixels + "px");
				} else {
					table.setColumnWidth(loginCol, "20%");
				}
				break;
			case USER_EMAIL:
				PerunColumn<RichUserExtSource, String> emailCol = createColumn(column, rues -> {
					if (rues.getEmail() == null || rues.getEmail().isEmpty()) {
						return "N/A";
					}
					return rues.getEmail();
				}, this.getFieldUpdater(table));
				emailCol.setSortable(true);
				table.getColumnSortHandler().setComparator(emailCol, new PerunComparator<RichUserExtSource>(column));
				emailCol.setColumnType(column);
				table.addColumn(emailCol, translation.email());
				if (widthInPixels > 0) {
					table.setColumnWidth(emailCol, widthInPixels + "px");
				} else {
					table.setColumnWidth(emailCol, "20%");
				}
				break;
			case REMOVE:
				Column<RichUserExtSource, String> removeColumn = new Column<RichUserExtSource, String>(
						new ButtonCell(ButtonType.DANGER, ButtonSize.EXTRA_SMALL)) {
					@Override
					public String getValue(final RichUserExtSource extSource) {
						((ButtonCell) this.getCell()).setEnabled(!extSource.getPersistent());
						return "✖";
					}
				};
				PerunColumn<RichUserExtSource, String> perunRemoveColumn =
						new PerunColumn<RichUserExtSource, String>(removeColumn.getCell(), column) {
					@Override
					public String getValue(RichUserExtSource richUserExtSource) {
						return "✖";
					}
				};

				perunRemoveColumn.setSortable(true);
				table.getColumnSortHandler().setComparator(perunRemoveColumn, new PerunComparator<RichUserExtSource>(column));
				perunRemoveColumn.setColumnType(column);
				table.addColumn(perunRemoveColumn, "");
				if (widthInPixels > 0) {
					table.setColumnWidth(perunRemoveColumn, widthInPixels + "px");
				} else {
					table.setColumnWidth(perunRemoveColumn, "20%");
				}
				break;
		}
	}

	private String getCertParam(String string, String param) {

		for (String s : string.split("/")) {
			if (s.startsWith(param+"=")) {
				return Utils.unescapeDN(s.split("=")[1]);
			}
		}
		return "";
	}
}
