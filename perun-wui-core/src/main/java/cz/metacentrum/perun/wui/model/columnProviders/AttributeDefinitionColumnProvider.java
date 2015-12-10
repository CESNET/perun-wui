package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.AttributeDefinition;
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
 * for {@link AttributeDefinition AttributeDefinition}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AttributeDefinitionColumnProvider extends ColumnProvider<AttributeDefinition> {

	private static ArrayList<PerunColumnType> defaultColumns = new ArrayList<>();

	static {
		defaultColumns.add(PerunColumnType.ID);
		defaultColumns.add(PerunColumnType.ATTR_FRIENDLY_NAME);
		defaultColumns.add(PerunColumnType.ATTR_ENTITY);
		defaultColumns.add(PerunColumnType.ATTR_DEF);
		defaultColumns.add(PerunColumnType.ATTR_TYPE);
		defaultColumns.add(PerunColumnType.DESCRIPTION);
	}

	@Override
	public ArrayList<PerunColumnType> getDefaultColumns() {
		return defaultColumns;
	}

	@Override
	public PerunColumnType getDefaultSortColumn() {
		return PerunColumnType.ATTR_FRIENDLY_NAME;
	}

	@Override
	public boolean isDefaultSortColumnDescending() {
		return false;
	}

	@Override
	public PerunDataGrid.PerunSelectionEvent<AttributeDefinition> getDefaultSelectionEvent() {

		// by default all VOs can be selected
		return new PerunDataGrid.PerunSelectionEvent<AttributeDefinition>() {
			@Override
			public boolean canSelectObject(AttributeDefinition object) {
				return (object != null);
			}
		};

	}

	@Override
	public PerunDataGrid.PerunFilterEvent<AttributeDefinition> getDefaultFilterEvent() {
		return new PerunDataGrid.PerunFilterEvent<AttributeDefinition>() {
			@Override
			public boolean filterOnObject(Set<PerunColumnType> columnTypeSet, String text, AttributeDefinition object) {

				if (object == null || text == null) return false;

				if (columnTypeSet == null || columnTypeSet.isEmpty()) {
					columnTypeSet = new HashSet<PerunColumnType>(Arrays.asList(PerunColumnType.NAME, PerunColumnType.ATTR_URN));
				}
				for (PerunColumnType columnType : columnTypeSet) {
					if (columnType.equals(PerunColumnType.ID) && Integer.toString(object.getId()).contains(text)) {
						return true;
					} else if (columnType.equals(PerunColumnType.NAME) && object.getName() != null &&
							object.getName().toLowerCase().contains(text.toLowerCase())) {
						return true;
					} else if (columnType.equals(PerunColumnType.ATTR_FRIENDLY_NAME) && object.getFriendlyName() != null &&
							object.getFriendlyName().toLowerCase().contains(text.toLowerCase())) {
						return true;
					} else if (columnType.equals(PerunColumnType.ATTR_ENTITY) && object.getEntity() != null &&
							object.getEntity().toLowerCase().contains(text.toLowerCase())) {
						return true;
					} else if (columnType.equals(PerunColumnType.ATTR_DEF) && object.getDefinition() != null &&
							object.getDefinition().toLowerCase().contains(text.toLowerCase())) {
						return true;
					} else if (columnType.equals(PerunColumnType.ATTR_TYPE) && object.getType() != null &&
							object.getType().toLowerCase().contains(text.toLowerCase())) {
						return true;
					} else if (columnType.equals(PerunColumnType.DESCRIPTION) && object.getDescription() != null &&
							object.getDescription().toLowerCase().contains(text.toLowerCase())) {
						return true;
					} else if (columnType.equals(PerunColumnType.ATTR_URN) && object.getURN() != null &&
							object.getURN().toLowerCase().contains(text.toLowerCase())) {
						return true;
					}
				}
				return false;
			}
		};
	}

	@Override
	public void addColumnToTable(PerunDataGrid<AttributeDefinition> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<AttributeDefinition> table, PerunColumnType column, FieldUpdater<AttributeDefinition, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<AttributeDefinition> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(final PerunDataGrid<AttributeDefinition> table, PerunColumnType column, FieldUpdater<AttributeDefinition, C> updater, int widthInPixels) {

		if (PerunColumnType.ID.equals(column)) {

			PerunColumn<AttributeDefinition, String> idColumn = createColumn(column,
					new GetValue<AttributeDefinition, String>() {
						@Override
						public String getValue(AttributeDefinition object) {
							return String.valueOf(object.getId());
						}
					}, this.<String>getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<AttributeDefinition>(PerunColumnType.ID));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Id");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "10%");
			}

		} else if (PerunColumnType.NAME.equals(column)) {

			PerunColumn<AttributeDefinition, PerunLinkCell.PerunLinkCellHandler<AttributeDefinition>> nameColumn = createColumn(
					new PerunLinkCell<PerunLinkCell.PerunLinkCellHandler<AttributeDefinition>>(),
					column,
					new GetValue<AttributeDefinition, PerunLinkCell.PerunLinkCellHandler<AttributeDefinition>>() {
						@Override
						public PerunLinkCell.PerunLinkCellHandler<AttributeDefinition> getValue(final AttributeDefinition object) {
							return new PerunLinkCell.PerunLinkCellHandler<AttributeDefinition>() {
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
								public AttributeDefinition getObject() {
									return object;
								}

							};
						}
					},
					this.<PerunLinkCell.PerunLinkCellHandler<AttributeDefinition>>getFieldUpdater(table)
			);

			nameColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(nameColumn, new PerunComparator<AttributeDefinition>(PerunColumnType.NAME));
			nameColumn.setColumnType(column);
			table.addColumn(nameColumn, "Name");
			if (widthInPixels > 0) {
				table.setColumnWidth(nameColumn, widthInPixels + "px");
			} else {
				// by default not with fixed width
				table.setColumnWidth(nameColumn, "30%");
			}

		} else if (PerunColumnType.DESCRIPTION.equals(column)) {

			PerunColumn<AttributeDefinition, String> shortNameColumn = createColumn(
					column,
					new GetValue<AttributeDefinition, String>() {
						@Override
						public String getValue(AttributeDefinition object) {
							return object.getDescription();
						}
					},
					this.<String>getFieldUpdater(table)
			);

			shortNameColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(shortNameColumn, new PerunComparator<AttributeDefinition>(PerunColumnType.DESCRIPTION));
			shortNameColumn.setColumnType(column);
			table.addColumn(shortNameColumn, "Description");
			if (widthInPixels > 0) {
				table.setColumnWidth(shortNameColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(shortNameColumn, "60%");
			}

		} else if (PerunColumnType.ATTR_DEF.equals(column)) {

			PerunColumn<AttributeDefinition, String> definitionColumn = createColumn(
					column,
					new GetValue<AttributeDefinition, String>() {
						@Override
						public String getValue(AttributeDefinition object) {
							return object.getDefinition();
						}
					},
					this.<String>getFieldUpdater(table)
			);

			definitionColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(definitionColumn, new PerunComparator<AttributeDefinition>(PerunColumnType.ATTR_DEF));
			definitionColumn.setColumnType(column);
			table.addColumn(definitionColumn, "Definition");
			if (widthInPixels > 0) {
				table.setColumnWidth(definitionColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(definitionColumn, "8%");
			}

		} else if (PerunColumnType.ATTR_ENTITY.equals(column)) {

			PerunColumn<AttributeDefinition, String> entityColumn = createColumn(
					column,
					new GetValue<AttributeDefinition, String>() {
						@Override
						public String getValue(AttributeDefinition object) {
							return object.getEntity();
						}
					},
					this.<String>getFieldUpdater(table)
			);

			entityColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(entityColumn, new PerunComparator<AttributeDefinition>(PerunColumnType.ATTR_ENTITY));
			entityColumn.setColumnType(column);
			table.addColumn(entityColumn, "Entity");
			if (widthInPixels > 0) {
				table.setColumnWidth(entityColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(entityColumn, "12%");
			}

		} else if (PerunColumnType.ATTR_URN.equals(column)) {

			PerunColumn<AttributeDefinition, String> entityColumn = createColumn(
					column,
					new GetValue<AttributeDefinition, String>() {
						@Override
						public String getValue(AttributeDefinition object) {
							return object.getURN();
						}
					},
					this.<String>getFieldUpdater(table)
			);

			entityColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(entityColumn, new PerunComparator<AttributeDefinition>(PerunColumnType.ATTR_URN));
			entityColumn.setColumnType(column);
			table.addColumn(entityColumn, "URN");
			if (widthInPixels > 0) {
				table.setColumnWidth(entityColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(entityColumn, "40%");
			}

		} else if (PerunColumnType.ATTR_FRIENDLY_NAME.equals(column)) {

			PerunColumn<AttributeDefinition, PerunLinkCell.PerunLinkCellHandler<AttributeDefinition>> nameColumn = createColumn(
					new PerunLinkCell<PerunLinkCell.PerunLinkCellHandler<AttributeDefinition>>(),
					column,
					new GetValue<AttributeDefinition, PerunLinkCell.PerunLinkCellHandler<AttributeDefinition>>() {
						@Override
						public PerunLinkCell.PerunLinkCellHandler<AttributeDefinition> getValue(final AttributeDefinition object) {
							return new PerunLinkCell.PerunLinkCellHandler<AttributeDefinition>() {
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
									return object.getFriendlyName();
								}

								@Override
								public AttributeDefinition getObject() {
									return object;
								}

							};
						}
					},
					this.<PerunLinkCell.PerunLinkCellHandler<AttributeDefinition>>getFieldUpdater(table)
			);

			nameColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(nameColumn, new PerunComparator<AttributeDefinition>(PerunColumnType.ATTR_FRIENDLY_NAME));
			nameColumn.setColumnType(column);
			table.addColumn(nameColumn, "Friendly name");
			if (widthInPixels > 0) {
				table.setColumnWidth(nameColumn, widthInPixels + "px");
			} else {
				// by default not with fixed width
				table.setColumnWidth(nameColumn, "20%");
			}

		} else if (PerunColumnType.ATTR_TYPE.equals(column)) {

			PerunColumn<AttributeDefinition, String> typeColumn = createColumn(
					column,
					new GetValue<AttributeDefinition, String>() {
						@Override
						public String getValue(AttributeDefinition object) {
							return object.getType().substring(object.getType().lastIndexOf(".")+1, object.getType().length());
						}
					},
					this.<String>getFieldUpdater(table)
			);

			typeColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(typeColumn, new PerunComparator<AttributeDefinition>(PerunColumnType.ATTR_TYPE));
			typeColumn.setColumnType(column);
			table.addColumn(typeColumn, "Type");
			if (widthInPixels > 0) {
				table.setColumnWidth(typeColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(typeColumn, "11%");
			}

		}

	}

}