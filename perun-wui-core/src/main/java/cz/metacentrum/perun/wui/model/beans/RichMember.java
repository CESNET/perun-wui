package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Overlay type for RichMember object
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class RichMember extends Member {

	protected RichMember() {
	}

	/**
	 * Return new instance of RichMember with basic properties set.
	 *
	 * @param id               ID of Member
	 * @param voId             ID of VO member is associated with
	 * @param membershipType   type of Membership
	 * @param membershipStatus status of Membership
	 * @param user             User this RichMember is associated with
	 * @return RichMember object
	 */
	public static final RichMember createNew(int id, int voId, String membershipType, String membershipStatus, User user) {
		RichMember member = new JSONObject().getJavaScriptObject().cast();
		member.setId(id);
		member.setUser(user);
		member.setUserId(user.getId());
		member.setVoId(voId);
		member.setMembershipType(membershipType);
		member.setMembershipStatus(membershipStatus);
		member.setObjectType("RichMember");
		return member;
	}

	/**
	 * Get User associated with this RichMember
	 *
	 * @return User this RichMember is associated with
	 */
	public final User getUser() {
		return (User) JsUtils.getNativePropertyObject(this, "user");
	}

	/**
	 * Set User associated with this RichMember
	 *
	 * @param user User this RichMember is associated with
	 */
	public final native void setUser(User user) /*-{
        this.user = user;
    }-*/;

	/**
	 * Get User attributes stored in RichMember. If none present, return empty list.
	 * <p/>
	 * Included attributes can be specified on RichMember retrieval (see RPC API).
	 * Attributes are also filtered based on callers READ right.
	 *
	 * @return User attributes of RichMember
	 */
	public final ArrayList<Attribute> getUserAttributes() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "userAttributes"));
	}

	/**
	 * Get Member attributes stored in RichMember. If none present, return empty list.
	 * <p/>
	 * Included attributes can be specified on RichMember retrieval (see RPC API).
	 * Attributes are also filtered based on callers READ right.
	 *
	 * @return Member attributes of RichMember
	 */
	public final ArrayList<Attribute> getMemberAttributes() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "memberAttributes"));
	}

	/**
	 * Return User or Member attribute from RichMember or NULL if attribute is not present.
	 *
	 * @param urn Whole URN of an attribute to get (must start with "unr:perun:[user/member]"
	 * @return User / Member attribute stored in RichMember or NULL
	 */
	public final Attribute getAttribute(String urn) {

		if (urn.startsWith("urn:perun:user")) {
			for (Attribute a : getUserAttributes()) {
				if (a.getURN().equals(urn)) return a;
			}
		} else if (urn.startsWith("urn:perun:member")) {
			for (Attribute a : getMemberAttributes()) {
				if (a.getURN().equals(urn)) return a;
			}
		}
		return null;

	}

	/**
	 * Set attribute to RichMember object.
	 * <p/>
	 * WORK ONLY FOR USER AND MEMBER ATTRIBUTES
	 * <p/>
	 * If attribute present, update it's value
	 * If attribute not present, add to attr list
	 *
	 * @param attribute to set to RichMember object
	 */
	public final native void setAttribute(Attribute attribute) /*-{
        if (attribute == null) return;
        // init fields if empty
        if (this.userAttributes == null) {
            this.userAttributes = [];
        }
        if (this.memberAttributes == null) {
            this.memberAttributes = [];
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
        } else if (attribute.namespace.indexOf("urn:perun:member:") !== -1) {
            // set member attribute
            for (var i in this.memberAttributes) {
                if (this.memberAttributes[i].namespace == attribute.namespace && this.memberAttributes[i].friendlyName == attribute.friendlyName) {
                    this.memberAttributes[i].value = attribute.value;
                    found = true;
                }
            }
            if (!found) {
                // put whole attribute
                this.memberAttributes[this.memberAttributes.length] = attribute;
            }
        }
    }-*/;

	/**
	 * Get all logins stored in User attributes of RichMember. Return empty string if none found.
	 * <p/>
	 * IMPORTANT: this method doesn't ensure to return all actual Users logins, since
	 * list of available attributes to search through can be filtered on RichMember retrieval
	 * and by callers READ rights.
	 *
	 * @return Users logins as string or empty string if none found.
	 */
	public final String getUserLogins() {

		ArrayList<String> list = new ArrayList<>();
		for (Attribute a : JsUtils.<Attribute>jsoAsList(JsUtils.getNativePropertyArray(this, "userAttributes"))) {
			if (a.getBaseFriendlyName().equals("login-namespace")) {
				if (a.getValue() != null) list.add(a.getFriendlyNameParameter() + ": " + a.getValue());
			}
		}
		Collections.sort(list, PerunComparator.getNativeComparator());
		return Utils.join(list, ", ");

	}

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(RichMember o) {
		return (o.getId() == this.getId()) && (o.getUser().getId() == this.getUser().getId());
	}

}