package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.cells.PerunLinkCell;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.Set;

/**
 * Implementation of {@link ColumnProvider ColumnProvider}
 * for {@link Attribute Attribute}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AttributeColumnProvider extends ColumnProvider<Attribute> {

	@Override
	public ArrayList<PerunColumnType> getDefaultColumns() {

		ArrayList<PerunColumnType> columns = new ArrayList<>();
		columns.add(PerunColumnType.ID);
		columns.add(PerunColumnType.NAME);
		columns.add(PerunColumnType.DESCRIPTION);
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
	public PerunDataGrid.PerunSelectionEvent<Attribute> getDefaultSelectionEvent() {

		// by default all VOs can be selected
		return new PerunDataGrid.PerunSelectionEvent<Attribute>() {
			@Override
			public boolean canSelectObject(Attribute object) {
				return (object != null);
			}
		};

	}

	@Override
	public PerunDataGrid.PerunFilterEvent<Attribute> getDefaultFilterEvent() {
		return new PerunDataGrid.PerunFilterEvent<Attribute>() {
			@Override
			public boolean filterOnObject(Set<PerunColumnType> columnTypeSet, String text, Attribute object) {
				if (object != null){
					if (columnTypeSet.isEmpty() && object.getName().toLowerCase().contains(text.toLowerCase())){
						return true;
					}
					for (PerunColumnType columnType : columnTypeSet) {
						if (columnType.equals(PerunColumnType.ID) && Integer.toString(object.getId()).toLowerCase().startsWith(text.toLowerCase())) {
							return true;
						} else if (columnType.equals(PerunColumnType.NAME) && object.getName().toLowerCase().contains(text.toLowerCase())) {
							return true;
						} else if (columnType.equals(PerunColumnType.DESCRIPTION) && object.getDescription().toLowerCase().contains(text.toLowerCase())) {
							return true;
						}
					}
				}
				return false;
			}
		};
	}

	@Override
	public void addColumnToTable(PerunDataGrid<Attribute> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<Attribute> table, PerunColumnType column, FieldUpdater<Attribute, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<Attribute> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(final PerunDataGrid<Attribute> table, PerunColumnType column, FieldUpdater<Attribute, C> updater, int widthInPixels) {

		if (PerunColumnType.ID.equals(column)) {

			PerunColumn<Attribute, String> idColumn = createColumn(column,
					new GetValue<Attribute, String>() {
						@Override
						public String getValue(Attribute object) {
							return String.valueOf(object.getId());
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<Attribute>(PerunColumnType.ID));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Id");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "10%");
			}

		} else if (PerunColumnType.NAME.equals(column)) {

			PerunColumn<Attribute, PerunLinkCell.PerunLinkCellHandler<Attribute>> nameColumn = createColumn(
					new PerunLinkCell<PerunLinkCell.PerunLinkCellHandler<Attribute>>(),
					column,
					new GetValue<Attribute, PerunLinkCell.PerunLinkCellHandler<Attribute>>() {
						@Override
						public PerunLinkCell.PerunLinkCellHandler<Attribute> getValue(final Attribute object) {
							return new PerunLinkCell.PerunLinkCellHandler<Attribute>() {
								@Override
								public boolean isAuthorized() {
									return PerunSession.getInstance().isPerunAdmin();
								}

								@Override
								public String getUrl() {
									return PlaceTokens.ATTRIBUTE_DETAIL+";id="+object.getId();
								}

								@Override
								public void onClick() {
								}

								@Override
								public String getCellValue() {
									return object.getName();
								}

								@Override
								public Attribute getObject() {
									return object;
								}

							};
						}
					},
					this.<PerunLinkCell.PerunLinkCellHandler<Attribute>>getFieldUpdater(table)
			);

			nameColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(nameColumn, new PerunComparator<Attribute>(PerunColumnType.NAME));
			nameColumn.setColumnType(column);
			table.addColumn(nameColumn, "Name");
			if (widthInPixels > 0) {
				table.setColumnWidth(nameColumn, widthInPixels + "px");
			} else {
				// by default not with fixed width
				table.setColumnWidth(nameColumn, "30%");
			}

		} else if (PerunColumnType.DESCRIPTION.equals(column)) {

			PerunColumn<Attribute, String> shortNameColumn = createColumn(
					column,
					new GetValue<Attribute, String>() {
						@Override
						public String getValue(Attribute object) {
							return object.getDescription();
						}
					},
					this.<String>getFieldUpdater(table)
			);

			shortNameColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(shortNameColumn, new PerunComparator<Attribute>(PerunColumnType.DESCRIPTION));
			shortNameColumn.setColumnType(column);
			table.addColumn(shortNameColumn, "Description");
			if (widthInPixels > 0) {
				table.setColumnWidth(shortNameColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(shortNameColumn, "60%");
			}

		} else if (PerunColumnType.ATTR_VALUE.equals(column)) {

			// TODO

		}

	}

}