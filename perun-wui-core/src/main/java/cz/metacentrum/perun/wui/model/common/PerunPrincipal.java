package cz.metacentrum.perun.wui.model.common;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.beans.User;

/**
 * Overlay type for PerunPrincipal object. This object represents session on Perun's server side.
 * It provides various info about the user and most importantly authz information stored in
 * {@link cz.metacentrum.perun.wui.model.common.Roles Roles}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunPrincipal extends JavaScriptObject {

	protected PerunPrincipal() {
	}

	/**
	 * Get user's login used to log into Perun's RPC server.
	 *
	 * @return login or null
	 */
	public final String getActor() {
		return JsUtils.getNativePropertyString(this, "actor");
	}

	/**
	 * Get name of external source (identity provider) user used to log into Perun's RPC server.
	 *
	 * @return external source name or null
	 */
	public final String getExtSource() {
		return JsUtils.getNativePropertyString(this, "extSourceName");
	}

	/**
	 * Get type of external source (identity provider) user used to log into Perun's RPC server.
	 * Returned value is java's class name of ExtSource type implementation.
	 *
	 * @return external source type
	 */
	public final String getExtSourceType() {
		return JsUtils.getNativePropertyString(this, "extSourceType");
	}

	/**
	 * Get LoA (level of assurance) of user in external source (identity provider) user used to log into Perun's RPC server.
	 *
	 * 0 - not verified
	 * 1 - verified email
	 * 2 - verified identity (by some official ID)
	 * 3 - verified identity and is verified member/employee of any academic institution (University)
	 *
	 * @return user's LoA in external source or 0 if not defined.
	 */
	public final int getExtSourceLoa() {
		return JsUtils.getNativePropertyInt(this, "extSourceLoa");
	}

	/**
	 * Get User object (for providing name etc.) associated with authz info user used
	 * to log into Perun's RPC server.
	 *
	 * @return logged user
	 */
	public final User getUser() {
		if (JsUtils.getNativePropertyObject(this, "user") != null) {
			return (User) JsUtils.getNativePropertyObject(this, "user");
		}
		return null;
	}

	/**
	 * Get User ID associated with authz info user used
	 * to log into Perun's RPC server. If user is unknown (null)
	 * then -1 is returned.
	 *
	 * @return ID of user logged to RPC or -1.
	 */
	public final int getUserId() {
		return JsUtils.getNativePropertyInt(this, "userId");
	}

	/**
	 * Get any additional information provided by IDP in session.
	 *
	 * @param shibAttrName valid shibboleth attribute name:
	 *                     mail - mail
	 *                     o - organization
	 *                     loa - level of assurance
	 *                     givenName - given name
	 *                     cn - common name
	 *                     displayName - display name
	 *                     sn - sure name
	 *                     eppn - edu person principal name
	 *                     md_entityCategory - IDP Category
	 *                     affiliation - Users affiliation in IDP
	 *
	 * @return attribute value or null if not present
	 */
	public final native String getAdditionInformation(String shibAttrName) /*-{
        if (!this.additionalInformations[shibAttrName]) return null;
        if (typeof this.additionalInformations[shibAttrName] === 'undefined') return null;
        if (this.additionalInformations[shibAttrName] === null) return null;
        return this.additionalInformations[shibAttrName];
    }-*/;

	/**
	 * Get user's roles containing all authz info regarding to entities management (e.g. in which VOs is manager).
	 *
	 * @return user's roles or null if not present
	 */
	public final Roles getRoles() {
		if (JsUtils.getNativePropertyObject(this, "roles") != null) {
			return (Roles) JsUtils.getNativePropertyObject(this, "roles");
		}
		return null;
	}

}
