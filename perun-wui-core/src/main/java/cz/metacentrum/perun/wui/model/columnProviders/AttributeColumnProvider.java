package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONValue;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.cells.PerunLinkCell;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link ColumnProvider ColumnProvider}
 * for {@link Attribute Attribute} to display user information.
 *
 * @author Vojtěch Sassmann <vojtech.sassmann@gmail.com>
 */
public class AttributeColumnProvider extends ColumnProvider<Attribute> {
	private static ArrayList<PerunColumnType> defaultColumns = new ArrayList<>();

	private boolean nameAsLink = false;

	private PerunTranslation translation = GWT.create(PerunTranslation.class);

	public AttributeColumnProvider(boolean nameAsLink) {
		this.nameAsLink = nameAsLink;
	}

	static {
		defaultColumns.add(PerunColumnType.ID);
		defaultColumns.add(PerunColumnType.NAME);
		defaultColumns.add(PerunColumnType.ATTR_VALUE);
		defaultColumns.add(PerunColumnType.DESCRIPTION);
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
	public PerunDataGrid.PerunSelectionEvent<Attribute> getDefaultSelectionEvent() {

		// by default all VOs can be selected
		return object -> (object != null);

	}

	@Override
	public PerunDataGrid.PerunFilterEvent<Attribute> getDefaultFilterEvent() {
		return (columnTypeSet, text, object) -> {
			if (object == null || text == null) return false;

			if (columnTypeSet == null || columnTypeSet.isEmpty()) {
				// use default filter - ID,displayName,description
				columnTypeSet = new HashSet<>(Arrays.asList(PerunColumnType.NAME, PerunColumnType.DESCRIPTION));
			}
			for (PerunColumnType columnType : columnTypeSet) {
				if (columnType.equals(PerunColumnType.ID) && Integer.toString(object.getId()).contains(text)) {
					return true;
				} else if (columnType.equals(PerunColumnType.NAME) && object.getName() != null &&
								   object.getName().toLowerCase().contains(text.toLowerCase())) {
					return true;
				} else if (columnType.equals(PerunColumnType.DESCRIPTION) && object.getDescription() != null &&
								   object.getDescription().toLowerCase().contains(text.toLowerCase())) {
					return true;
				}
			}
			return false;
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

		switch (column) {
			case ID:
				PerunColumn<Attribute, String> idColumn = createColumn(column,
						object -> String.valueOf(object.getId()), this.getFieldUpdater(table)
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
				break;
			case NAME:
				PerunColumn nameColumn;

				if (nameAsLink) {
					nameColumn = createColumn(
							new PerunLinkCell<>(),
							column,
							object -> new PerunLinkCell.PerunLinkCellHandler<Attribute>() {
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

							},
							this.<PerunLinkCell.PerunLinkCellHandler<Attribute>>getFieldUpdater(table)
					);
				} else {
					nameColumn = createColumn(
							column,
							Attribute::getName,
							this.getFieldUpdater(table)
					);
				}

				nameColumn.setSortable(true);
				table.getColumnSortHandler().setComparator(nameColumn, new PerunComparator<Attribute>(PerunColumnType.NAME));
				nameColumn.setColumnType(column);
				table.addColumn(nameColumn, translation.name());
				if (widthInPixels > 0) {
					table.setColumnWidth(nameColumn, widthInPixels + "px");
				} else {
					// by default not with fixed width
					table.setColumnWidth(nameColumn, "30%");
				}
				break;

			case ATTR_VALUE:
				PerunColumn<Attribute, String> attrValueColumn = createColumn(
						column,
						attr -> {
							if (attr.getType().equals("java.util.LinkedHashMap")) {
								return generateMap(attr.getValueAsMap());
							} else if (attr.getType().equals("java.lang.Integer")) {
								return generateNumberBox(attr.getValue());
							} else if (attr.getType().equals("java.lang.Boolean")) {
								return generateCheckBox(attr.getValue());
							} else if (attr.getType().equals("java.lang.LargeString")) {
								return generateString(attr.getValue());
							} else if (attr.getType().equals("java.util.LargeArrayList")) {
								return generateList(attr.getValueAsJsArray());
							} else {
								return generateList(attr.getValueAsJsArray());
							}
						},
						this.getFieldUpdater(table)
				);

				attrValueColumn.setSortable(true);
				table.getColumnSortHandler().setComparator(attrValueColumn, new PerunComparator<Attribute>(PerunColumnType.DESCRIPTION));
				attrValueColumn.setColumnType(column);
				table.addColumn(attrValueColumn, translation.value());
				if (widthInPixels > 0) {
					table.setColumnWidth(attrValueColumn, widthInPixels + "px");
				} else {
					table.setColumnWidth(attrValueColumn, "40%");
				}
				break;

			case DESCRIPTION:
				PerunColumn<Attribute, String> shortNameColumn = createColumn(
					column,
						GeneralObject::getDescription,
					this.getFieldUpdater(table)
				);

				shortNameColumn.setSortable(true);
				table.getColumnSortHandler().setComparator(shortNameColumn, new PerunComparator<Attribute>(PerunColumnType.DESCRIPTION));
				shortNameColumn.setColumnType(column);
				table.addColumn(shortNameColumn, translation.description());
				if (widthInPixels > 0) {
					table.setColumnWidth(shortNameColumn, widthInPixels + "px");
				} else {
					table.setColumnWidth(shortNameColumn, "30%");
				}
				break;
		}
	}

	private String generateList(JsArrayString valueAsJsArray) {
		return valueAsJsArray.toString();
	}

	private String generateString(String value) {
		return value;
	}

	private String generateCheckBox(String value) {
		return value;
	}

	private String generateNumberBox(String value) {
		return value;
	}

	private String generateMap(Map<String, JSONValue> valueAsMap) {
		StringBuilder str = new StringBuilder();
		List<String> keys = new ArrayList<>(valueAsMap.keySet());

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			str.append(key);
			str.append(": ");
			str.append(valueAsMap.get(key).toString());
			if (i != (keys.size() - 1)) {
				str.append(", ");
			}
		}
		return str.toString();
	}
}
