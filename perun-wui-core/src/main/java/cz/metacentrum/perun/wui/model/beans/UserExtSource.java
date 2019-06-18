package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for UserExtSource object from Perun
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class UserExtSource extends GeneralObject {

	protected UserExtSource() { }

	/**
	 *
	 * @param source     ExtSource definition
	 * @param login		 User's login in concrete ExtSource
	 * @param userId     ID of User this UES belongs to
	 * @param loa		 level of assurance
	 * @param persistent persistent flag of this UserExtSource
	 * @return
	 */
	public static final UserExtSource createNew(ExtSource source, String login, int userId, int loa, String lastAccess, boolean persistent){
		UserExtSource ues = new JSONObject().getJavaScriptObject().cast();
		ues.setExtSource(source);
		ues.setLogin(login);
		ues.setUserId(userId);
		ues.setLoa(loa);
		ues.setLastAccess(lastAccess);
		ues.setPersistent(persistent);
		return ues;
	}

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
	 * Set ExtSource definition
	 *
	 * @param source ExtSource definition
	 */
	public final native void setExtSource(ExtSource source)/*-{
    	this.extSource = source;
	}-*/;

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
	 * Get Persistent flag of this UserExtSource (User's identity).
	 *
	 * false = UserExtSource can be removed. It is truly external.
	 * true = UserExtSource can NOT be removed. It is somehow important and needed in the system.
	 *
	 * @return persistent flag
	 */
	public final boolean getPersistent() {
		return JsUtils.getNativePropertyBoolean(this, "persistent");
	}

	/**
	 * Set Persistent flag of this UserExtSource (User's identity).
	 *
	 * false = UserExtSource can be removed. It is truly external.
	 * true = UserExtSource can NOT be removed. It is somehow important and needed in the system.
	 *
	 * @param persistent flag
	 */
	public final native void setPersistent(boolean persistent) /*-{
		this.persistent = persistent;
	}-*/;

	/**
	 * Get timestamp of last users usage of UserExtSource in "YYYY-MM-DD HH:mm:ss.SSSSSS" format.
	 *
	 * It is either timestamp of UserExtSource creation or last users access to Perun using this UES.
	 *
	 * @return last access timestamp
	 */
	public final String getLastAccess() {
		return JsUtils.getNativePropertyString(this, "lastAccess");
	}

	/**
	 * Set timestamp of last users usage of UserExtSource in "YYYY-MM-DD HH:mm:ss.SSSSSS" format.
	 *
	 * It is either timestamp of UserExtSource creation or last users access to Perun using this UES.
	 *
	 * @param lastAccess last access timestamp
	 */
	public final native void setLastAccess(String lastAccess) /*-{
		this.lastAccess = lastAccess;
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
	 * Set ID of User this UserExtSource belongs.
	 * @param id ID of User this UES belongs to
	 */
	public final native void setUserId(int id)/*-{
    	this.userId = id;
	}-*/;

	/**
	 * Compares to another object
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(UserExtSource o) {
		return o.getId() == this.getId();
	}

}
