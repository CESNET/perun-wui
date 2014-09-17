package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for Owner object
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Owner extends GeneralObject {

	protected Owner() {
	}

	/**
	 * Return new instance of Owner with basic properties set.
	 *
	 * @param id      ID of owner
	 * @param name    name of Owner
	 * @param contact contact of Owner
	 * @param type    type of Owner
	 * @return Owner object
	 */
	public static final Owner createNew(int id, String name, String contact, String type) {
		Owner owner = new JSONObject().getJavaScriptObject().cast();
		owner.setId(id);
		owner.setName(name);
		owner.setContact(contact);
		owner.setType(type);
		owner.setObjectType("Owner");
		return owner;
	}

	/**
	 * Set Owner's ID
	 *
	 * @param id ID to set
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Set Owner's name
	 *
	 * @param name name to set
	 */
	public final native void setName(String name) /*-{
        this.name = name;
    }-*/;

	/**
	 * Get contact info of owner
	 *
	 * @return contact of owner
	 */
	public final String getContact() {
		return JsUtils.getNativePropertyString(this, "contact");
	}

	/**
	 * Set Owner's contact information (email, phone,...)
	 *
	 * @param contact contact to set
	 */
	public final native void setContact(String contact) /*-{
        this.contact = contact;
    }-*/;

	/**
	 * Get type of owner
	 *
	 * @return type of owner (ADMINISTRATIVE, TECHNICAL)
	 */
	public final String getType() {
		return JsUtils.getNativePropertyString(this, "type");
	}

	/**
	 * Set Owner's type (ADMINISTRATIVE, TECHNICAL)
	 *
	 * @param type type to set
	 */
	public final native void setType(String type) /*-{
        this.type = type;
    }-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Owner o) {
		return o.getId() == this.getId();
	}

}
