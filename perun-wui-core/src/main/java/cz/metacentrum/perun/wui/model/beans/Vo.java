package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for VirtualOrganization object from Perun.
 *
 * @author Vaclav Mach <374430@mail.muni.cz>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Vo extends GeneralObject {

	protected Vo() {
	}

	/**
	 * Return new instance of VO with basic properties set.
	 *
	 * @param id        ID of VO
	 * @param name      name of VO
	 * @param shortName shortName of VO
	 * @return Vo object
	 */
	public static final Vo createNew(int id, String name, String shortName) {
		Vo vo = new JSONObject().getJavaScriptObject().cast();
		vo.setId(id);
		vo.setName(name);
		vo.setShortName(shortName);
		vo.setObjectType("Vo");
		return vo;
	}

	/**
	 * Set VO's ID
	 *
	 * @param id ID to set
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Set VO's name
	 *
	 * @param name name to set
	 */
	public final native void setName(String name) /*-{
        this.name = name;
    }-*/;

	/**
	 * Get shortName of VO
	 *
	 * @return shortName of VO
	 */
	public final String getShortName() {
		return JsUtils.getNativePropertyString(this, "shortName");
	}

	/**
	 * Set VO's shortName
	 *
	 * @param shortName shortName to set
	 */
	public final native void setShortName(String shortName) /*-{
        this.shortName = shortName;
    }-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Vo o) {
		return o.getId() == this.getId();
	}

}