package cz.metacentrum.perun.wui.model.beans;

import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for UserExtSource object from Perun
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class UserExtSource extends GeneralObject {

	protected UserExtSource() { }

	// TODO - add createNew() method

	/**
	 * Sets ID of ExtSource definition
	 *
	 * @param id ID to set
	 */
	public final native int setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Get User's login in concrete ExtSource
	 *
	 * @return User's login in concrete ExtSource
	 */
	public final String getLogin() {
		return JsUtils.getNativePropertyString(this, "login");
	}

	/**
	 * Set User's login in concrete ExtSource
	 *
	 * @param login User's login in concrete ExtSource
	 */
	public final native void setLogin(String login) /*-{
		this.login = login;
	}-*/;

	/**
	 * Get ExtSource definition
	 *
	 * @return ExtSource definition
	 */
	public final ExtSource getExtSource() {
		return (ExtSource)JsUtils.getNativePropertyObject(this, "extSource");
	}

	/**
	 * Get Level of Assurance this UserExtSource (User's identity) provides.
	 *
	 * 0 = not verified
	 * 1 = verified email
	 * 2 = verified identity by official ID (and academic status)
	 * 3 = verified identity by official ID + strong password requirement
	 *
	 * @return level of assurance
	 */
	public final int getLoa() {
		return JsUtils.getNativePropertyInt(this, "loa");
	}

	/**
	 * Set Level of Assurance this UserExtSource (User's identity) provides.
	 *
	 * 0 = not verified
	 * 1 = verified email
	 * 2 = verified identity by official ID (and academic status)
	 * 3 = verified identity by official ID + strong password requirement
	 *
	 * @param loa level of assurance
	 */
	public final native void setLoa(int loa) /*-{
		this.loa = loa;
	}-*/;

	/**
	 * Get ID of User this UserExtSource belongs.
	 * *
	 * @return ID of User this UES belongs to
	 */
	public final int getUserId() {
		return JsUtils.getNativePropertyInt(this, "userId");
	}

	/**
	 * Compares to another object
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(UserExtSource o) {
		return o.getId() == this.getId();
	}

}