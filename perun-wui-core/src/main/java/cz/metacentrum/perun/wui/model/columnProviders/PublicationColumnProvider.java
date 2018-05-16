package cz.metacentrum.perun.wui.model.columnProviders;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.beans.RichPublication;
import cz.metacentrum.perun.wui.model.resources.IconCell;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.bootbox.client.Bootbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Implementation of {@link cz.metacentrum.perun.wui.model.ColumnProvider ColumnProvider}
 * for {@link cz.metacentrum.perun.wui.model.beans.Publication Publication}.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PublicationColumnProvider extends ColumnProvider<RichPublication> {

	private static ArrayList<PerunColumnType> defaultColumns = new ArrayList<>();

	static {
		defaultColumns.add(PerunColumnType.ID);
		defaultColumns.add(PerunColumnType.PUBLICATION_LOCK);
		defaultColumns.add(PerunColumnType.NAME);
		defaultColumns.add(PerunColumnType.PUBLICATION_AUTHORS);
		defaultColumns.add(PerunColumnType.PUBLICATION_YEAR);
		defaultColumns.add(PerunColumnType.PUBLICATION_THANKS);
		defaultColumns.add(PerunColumnType.PUBLICATION_CITE);
	}

	private PerunTranslation translation = GWT.create(PerunTranslation.class);

	@Override
	public ArrayList<PerunColumnType> getDefaultColumns() {
		return defaultColumns;
	}

	@Override
	public PerunColumnType getDefaultSortColumn() {
		return PerunColumnType.ID;
	}

	@Override
	public boolean isDefaultSortColumnDescending() {
		return false;
	}

	@Override
	public PerunDataGrid.PerunSelectionEvent<RichPublication> getDefaultSelectionEvent() {

		return object -> (object != null);
	}

	@Override
	public PerunDataGrid.PerunFilterEvent<RichPublication> getDefaultFilterEvent() {
		return (columnTypeSet, text, object) -> {
			if (object == null || text == null) return false;

			if (columnTypeSet == null || columnTypeSet.isEmpty()) {
				columnTypeSet = new HashSet<>(Collections.singletonList(PerunColumnType.NAME));
			}
			for (PerunColumnType columnType : columnTypeSet) {
				if (columnType.equals(PerunColumnType.ID) && Integer.toString(object.getId()).contains(text)) {
					return true;
				} else if (columnType.equals(PerunColumnType.NAME) && object.getName() != null &&
						object.getName().toLowerCase().contains(text.toLowerCase())) {
					return true;
				}
			}
			return false;
		};
	}

	@Override
	public void addColumnToTable(PerunDataGrid<RichPublication> table, PerunColumnType column) {
		addColumnToTable(table, column, null, 0);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<RichPublication> table, PerunColumnType column, FieldUpdater<RichPublication, C> updater) {
		addColumnToTable(table, column, updater, 0);
	}

	@Override
	public void addColumnToTable(PerunDataGrid<RichPublication> table, PerunColumnType column, int widthInPixels) {
		addColumnToTable(table, column, null, widthInPixels);
	}

	@Override
	public <C> void addColumnToTable(PerunDataGrid<RichPublication> table, PerunColumnType column, FieldUpdater<RichPublication, C> updater, int widthInPixels) {
		if (PerunColumnType.ID.equals(column)) {

			PerunColumn<RichPublication, String> idColumn = createColumn(column,
					richPublication -> String.valueOf(richPublication.getId()), this.getFieldUpdater(table)
			);
			idColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(idColumn, new PerunComparator<RichPublication>(PerunColumnType.ID));
			idColumn.setColumnType(column);
			table.addColumn(idColumn, "Id");
			if (widthInPixels > 0) {
				table.setColumnWidth(idColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(idColumn, "7%");
			}

		} else if (PerunColumnType.PUBLICATION_LOCK.equals(column)) {


			final Column<RichPublication, String> lockColumn = new Column<RichPublication, String>(new IconCell(IconType.LOCK, IconSize.LARGE)) {
				@Override
				public String getValue(RichPublication object) {

					IconCell iconCell = (IconCell)getCell();

					if(!object.isLocked()) {
						iconCell.setIconType(IconType.UNLOCK);
					} else {
						iconCell.setIconType(IconType.LOCK);
					}
					return "";
				}
			};

			table.addColumn(lockColumn, "");
			if (widthInPixels > 0) {
				table.setColumnWidth(lockColumn, widthInPixels + "px");
			} else {
				// by default not with fixed width
				table.setColumnWidth(lockColumn, "3%");
			}
		} else if (PerunColumnType.NAME.equals(column)) {

			PerunColumn<RichPublication, String> nameColumn = createColumn(column,
					richPublication -> {
						if (richPublication.getTitle() == null) {
							return "-";
						} else {
							return richPublication.getTitle();
						}
					}, this.getFieldUpdater(table)
			);

			nameColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(nameColumn, new PerunComparator<RichPublication>(PerunColumnType.NAME));
			nameColumn.setColumnType(column);
			table.addColumn(nameColumn, translation.title());
			if (widthInPixels > 0) {
				table.setColumnWidth(nameColumn, widthInPixels + "px");
			} else {
				// by default not with fixed width
				table.setColumnWidth(nameColumn, "40%");
			}
		} else if (PerunColumnType.PUBLICATION_AUTHORS.equals(column)) {

			PerunColumn<RichPublication, String> authorsColumn = createColumn(column,
					richPublication -> {
						if(richPublication.getAuthors().size() == 0) {
							return "-";
						}
						StringBuilder authors = new StringBuilder();
						for(int i = 0; i < richPublication.getAuthors().size(); i++) {
							authors.append(richPublication.getAuthors().get(i).getFormattedName());
							if(i < (richPublication.getAuthors().size() - 1)) {
								authors.append(", ");
							}
						}
						return authors.toString();
					},
					this.getFieldUpdater(table)
			);

			authorsColumn.setSortable(false);
			authorsColumn.setColumnType(column);
			table.addColumn(authorsColumn, translation.authors());
			if (widthInPixels > 0) {
				table.setColumnWidth(authorsColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(authorsColumn, "20%");
			}

		} else if (PerunColumnType.PUBLICATION_YEAR.equals(column)) {

			PerunColumn<RichPublication, String> yearColumn = createColumn(column,
					richPublication -> String.valueOf(richPublication.getYear()),
					this.getFieldUpdater(table)
			);

			yearColumn.setSortable(true);
			table.getColumnSortHandler().setComparator(yearColumn, new PerunComparator<RichPublication>(PerunColumnType.PUBLICATION_YEAR));
			yearColumn.setColumnType(column);
			table.addColumn(yearColumn, translation.year());
			if (widthInPixels > 0) {
				table.setColumnWidth(yearColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(yearColumn, "9%");
			}




		} else if (PerunColumnType.PUBLICATION_THANKS.equals(column)) {
			PerunColumn<RichPublication, String> thanksColumn = createColumn(
					column,
					richPublication -> {
						if(richPublication.getThanks().size() == 0) {
							return "-";
						}
						StringBuilder thanks = new StringBuilder();
						for(int i = 0; i < richPublication.getThanks().size(); i++) {
							thanks.append(richPublication.getThanks().get(i).getOwnerName());
							if(i < (richPublication.getThanks().size() - 1)) {
								thanks.append(", ");
							}
						}
						return thanks.toString();
					},
					this.getFieldUpdater(table)
			);

			thanksColumn.setColumnType(column);
			table.addColumn(thanksColumn, translation.thanks());
			if (widthInPixels > 0) {
				table.setColumnWidth(thanksColumn, widthInPixels + "px");
			} else {
				table.setColumnWidth(thanksColumn, "13%");
			}
		} else if (PerunColumnType.PUBLICATION_CITE.equals(column)) {

			PerunColumn<RichPublication, String> citeColumn = createColumn(
					column,
					richPublication -> translation.cite(),
					(index, object, value) -> Bootbox.alert(object.getCitation())
			);

			table.addColumn(citeColumn, translation.cite());
			if (widthInPixels > 0) {
				table.setColumnWidth(citeColumn, widthInPixels + "px");
			} else {
				// by default not with fixed width
				table.setColumnWidth(citeColumn, "8%");
			}
		}
	}
}
