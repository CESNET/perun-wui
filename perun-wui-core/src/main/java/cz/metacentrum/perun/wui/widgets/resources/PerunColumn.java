package cz.metacentrum.perun.wui.widgets.resources;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.cellview.client.Column;

/**
 * Column wrapper which provides additional functionality to base GWT Column implementation
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public abstract class PerunColumn<T, C> extends Column<T, C> {

	private PerunColumnType columnType;

	public PerunColumn(Cell<C> cell) {
		super(cell);
	}

	public PerunColumn(Cell<C> cell, PerunColumnType columnType) {
		super(cell);
		this.columnType = columnType;
	}

	public PerunColumnType getColumnType() {
		return columnType;
	}

	public void setColumnType(PerunColumnType columnType) {
		this.columnType = columnType;
	}

	@Override
	public String getCellStyleNames(Cell.Context context, T object) {

		return super.getCellStyleNames(context, object) + ((PerunColumnType.ID.equals(columnType)) ? " idColumn" : "");

	}

}
