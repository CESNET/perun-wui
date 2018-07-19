package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for User object
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class User extends GeneralObject {

	protected User() {
	}

	/**
	 * Return new instance of User with basic properties set.
	 *
	 * @param id            ID of User
	 * @param firstName     firstName of User
	 * @param middleName    middleName of User
	 * @param lastName      lastName of User
	 * @param titleBefore   titleBefore of User
	 * @param titleAfter    titleAfter of User
	 * @param isServiceUser TRUE = is service user / FALSE = is normal user
	 * @param sponsoredUser TRUE = is sponsored user / FALSE = is normal user
	 * @return User object
	 */
	public static final User createNew(int id, String firstName, String middleName, String lastName, String titleBefore,
	                                   String titleAfter, boolean isServiceUser, boolean sponsoredUser) {

		User user = new JSONObject().getJavaScriptObject().cast();
		user.setId(id);
		user.setFirstName(firstName);
		user.setMiddleName(middleName);
		user.setLastName(lastName);
		user.setTitleBefore(titleBefore);
		user.setTitleAfter(titleAfter);
		user.setServiceUser(isServiceUser);
		user.setSponsoredUser(sponsoredUser);
		user.setObjectType("User");
		return user;

	}

	/**
	 * Set ID of User
	 *
	 * @param userId of User
	 */
	public final native void setId(int userId) /*-{
        this.id = userId;
    }-*/;

	/**
	 * Get first name of user
	 *
	 * @return first name of user
	 */
	public final String getFirstName() {
		return JsUtils.getNativePropertyString(this, "firstName");
	}

	/**
	 * Set first name
	 *
	 * @param name first name of user
	 */
	public final native String setFirstName(String name) /*-{
        this.firstName = name;
    }-*/;

	/**
	 * Get last name of user
	 *
	 * @return last name of user
	 */
	public final String getLastName() {
		return JsUtils.getNativePropertyString(this, "lastName");
	}

	/**
	 * Set last name
	 *
	 * @param name last name of user
	 */
	public final native String setLastName(String name) /*-{
        this.lastName = name;
    }-*/;

	/**
	 * Get ID of user
	 *
	 * @return id of user
	 */
	public final String getMiddleName() {
		return JsUtils.getNativePropertyString(this, "middleName");
	}

	/**
	 * Set middle name
	 *
	 * @param name middle name of user
	 */
	public final native String setMiddleName(String name) /*-{
        this.middleName = name;
    }-*/;

	/**
	 * Get title before name of user
	 *
	 * @return title before name of user
	 */
	public final String getTitleBefore() {
		return JsUtils.getNativePropertyString(this, "titleBefore");
	}

	/**
	 * Set title before name
	 *
	 * @param title title of user before name
	 */
	public final native void setTitleBefore(String title) /*-{
        this.titleBefore = title;
    }-*/;

	/**
	 * Get title after name of user
	 *
	 * @return title after name of user
	 */
	public final String getTitleAfter() {
		return JsUtils.getNativePropertyString(this, "titleAfter");
	}

	/**
	 * Set title after name
	 *
	 * @param title title of user after name
	 */
	public final native void setTitleAfter(String title) /*-{
        this.titleAfter = title;
    }-*/;

	/**
	 * Get full name with academic titles of user. If no part of name is present, null is returned.
	 *
	 * @return full name with academic titles of user
	 */
	public final native String getFullName() /*-{

        var fullName = "";

        var firstName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "firstName");
        var lastName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "lastName");
        var middleName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "middleName");
        var titleBefore = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "titleBefore");
        var titleAfter = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "titleAfter");

        if (titleBefore !== null) fullName = titleBefore;
        if (firstName !== null) fullName += " " + firstName;
        if (middleName !== null) fullName += " " + middleName;
        if (lastName !== null) fullName += " " + lastName;
        if (titleAfter !== null) fullName += ", " + titleAfter;

        if (fullName.length > 0) return fullName;
        return null;

    }-*/;

	/**
	 * Get common name of the user, without academic titles
	 *
	 * @return firs + middle + last name
	 */
	public final native String getCommonName() /*-{

        var commonName = "";

        var firstName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "firstName");
        var lastName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "lastName");
        var middleName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "middleName");

        if (firstName !== null) commonName = firstName;
        if (middleName !== null) commonName += " " + middleName;
        if (lastName !== null) commonName += " " + lastName;

        if (commonName.length > 0) return commonName;
        return null;

    }-*/;

	/**
	 * Return TRUE if user is "service user".
	 *
	 * @return TRUE = service user / FALSE = standard user
	 */
	public final boolean isServiceUser() {
		return JsUtils.getNativePropertyBoolean(this, "serviceUser");
	}

	/**
	 * Mark user as service account
	 *
	 * @param service TRUE = service user / FALSE = normal user
	 */
	public final native void setServiceUser(boolean service) /*-{
        return this.serviceUser = service;
    }-*/;

	/**
	 * Return TRUE if user is "sponsored user".
	 *
	 * @return TRUE = sponsored user / FALSE = standard user
	 */
	public final boolean isSponsoredUser(){
		return JsUtils.getNativePropertyBoolean(this, "sponsoredUser");
	}

	/**
	 * Mark user as sponsored account
	 *
	 * @param sponsored
	 */
	public final native void setSponsoredUser(boolean sponsored) /*-{
		this.sponsoredUser = sponsored;
	}-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(User o) {
		return o.getId() == this.getId();
	}

}
