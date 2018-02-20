package cz.metacentrum.perun.wui.model.beans;

import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for Cabinet API: Category
 * <p/>
 * Publications are categorized by some Category with custom rating coefficient.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Category extends GeneralObject {

	protected Category() {
	}

	/**
	 * Sets Category ID
	 *
	 * @param id ID of Category to set
	 */
	public final native void setId(int id) /*-{
		return this.id = id;
	}-*/;

	/**
	 * Sets Category name
	 *
	 * @param name Name of Category to set
	 */
	public final native void setName(String name) /*-{
		this.name = name;
	}-*/;

	/**
	 * Get Category rank (rating coefficient)
	 *
	 * @return rank
	 */
	public final double getRank() {
		return JsUtils.getNativePropertyDouble(this, "rank");
	}

	/**
	 * Set Category rank (rating coefficient)
	 *
	 * @param rank double value
	 */
	public final native void setRank(double rank) /*-{
		this.rank = rank;
	}-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Category o) {
		return o.getId() == this.getId();
	}

}
