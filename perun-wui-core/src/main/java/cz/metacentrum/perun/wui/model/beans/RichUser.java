package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Overlay type for RichUser object
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class RichUser extends User {

	protected RichUser() {
	}

	/**
	 * Return new instance of RichUser with basic properties set.
	 *
	 * @param id             ID of User
	 * @param firstName      firstName of User
	 * @param middleName     middleName of User
	 * @param lastName       lastName of User
	 * @param titleBefore    titleBefore of User
	 * @param titleAfter     titleAfter of User
	 * @param isServiceUser  TRUE = is service user / FALSE = is normal user
	 * @param userAttributes list of user attributes (can be null)
	 * @return RichUser object
	 */
	public static final RichUser createNew(int id, String firstName, String middleName, String lastName, String titleBefore,
	                                       String titleAfter, boolean isServiceUser, ArrayList<Attribute> userAttributes) {

		RichUser user = new JSONObject().getJavaScriptObject().cast();
		user.setId(id);
		user.setFirstName(firstName);
		user.setMiddleName(middleName);
		user.setLastName(lastName);
		user.setTitleBefore(titleBefore);
		user.setTitleAfter(titleAfter);
		user.setServiceUser(isServiceUser);
		if (userAttributes != null) user.setUserAttributes(userAttributes);
		user.setObjectType("RichUser");
		return user;

	}

	/**
	 * Get User attributes stored in RichUser. If none present, return empty list.
	 * <p/>
	 * Included attributes can be specified on RichUser retrieval (see RPC API).
	 * Attributes are also filtered based on callers READ right.
	 *
	 * @return User attributes of RichUser
	 */
	public final ArrayList<Attribute> getUserAttributes() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "userAttributes"));
	}

	/**
	 * Set attributes to RichUser object.
	 * <p/>
	 * WORK ONLY FOR USER ATTRIBUTES
	 * <p/>
	 * If attribute present, update it's value
	 * If attribute not present, add to attr list
	 *
	 * @param attributes to set to RichUser object
	 */
	public final void setUserAttributes(ArrayList<Attribute> attributes) {
		if (attributes != null) {
			for (Attribute a : attributes) setAttribute(a);
		}
	}

	/**
	 * Get specified user attribute stored in rich user
	 * <p/>
	 * USE ONLY FOR RICH USER
	 *
	 * @param urn URN of attribute to get
	 * @return user attribute or null if not present
	 */
	public final Attribute getAttribute(String urn) {
		for (Attribute a : JsUtils.<Attribute>jsoAsList(JsUtils.getNativePropertyArray(this, "userAttributes"))) {
			if (a.getName().equals(urn)) return a;
		}
		return null;
	}

	/**
	 * Set attribute to RichUser object.
	 * <p/>
	 * WORK ONLY FOR USER ATTRIBUTES
	 * <p/>
	 * If attribute present, update it's value
	 * If attribute not present, add to attr list
	 *
	 * @param attribute to set to RichUser object
	 */
	public final native void setAttribute(Attribute attribute) /*-{
        if (attribute == null) return;
        // init fields if empty
        if (this.userAttributes == null) {
            this.userAttributes = [];
        }
        var found = false;
        if (attribute.namespace.indexOf("urn:perun:user:") !== -1) {
            // set user attribute
            for (var i in this.userAttributes) {
                if (this.userAttributes[i].namespace == attribute.namespace && this.userAttributes[i].friendlyName == attribute.friendlyName) {
                    this.userAttributes[i].value = attribute.value;
                    found = true;
                }
            }
            if (!found) {
                // put whole attribute
                this.userAttributes[this.userAttributes.length] = attribute;
            }
        }
    }-*/;

	/**
	 * Get all logins stored in User attributes of RichUser. Return empty string if none found.
	 * <p/>
	 * IMPORTANT: this method doesn't ensure to return all actual Users logins, since
	 * list of available attributes to search through can be filtered on RichUser retrieval
	 * and by callers READ rights.
	 *
	 * @return Users logins as string or empty string if none found.
	 */
	public final String getLogins() {

		ArrayList<String> list = new ArrayList<>();
		for (Attribute a : JsUtils.<Attribute>jsoAsList(JsUtils.getNativePropertyArray(this, "userAttributes"))) {
			if (a.getBaseFriendlyName().equals("login-namespace")) {
				list.add(a.getFriendlyNameParameter() + ": " + a.getValue());
			}
		}
		Collections.sort(list, PerunComparator.getNativeComparator());
		return Utils.join(list, ", ");

	}

	/**
	 * Get preferredMail stored in User attributes of RichUser.
	 *
	 * @return users mail
	 */
	public final String getPreferredEmail() {
		for (Attribute a : JsUtils.<Attribute>jsoAsList(JsUtils.getNativePropertyArray(this, "userAttributes"))) {
			if (a.getFriendlyName().equals("preferredMail")) {
				return a.getValue();
			}
		}
		return null;
	}

	/**
	 * Get organization stored in User attributes of RichUser.
	 *
	 * @return users organization
	 */
	public final String getOrganization() {
		for (Attribute a : JsUtils.<Attribute>jsoAsList(JsUtils.getNativePropertyArray(this, "userAttributes"))) {
			if (a.getFriendlyName().equals("organization")) {
				return a.getValue();
			}
		}
		return null;
	}

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(RichUser o) {
		return o.getId() == this.getId();
	}

}
