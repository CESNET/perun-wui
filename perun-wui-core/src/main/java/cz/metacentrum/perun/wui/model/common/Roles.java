package cz.metacentrum.perun.wui.model.common;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;

/**
 * Overlay type for Roles object. It's part of {@link PerunPrincipal PerunPrincipal}
 * and is used to determine user's authz.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Roles extends JavaScriptObject {

	protected Roles() {
	}

	/**
	 * Check if user has specific role (passed as string)
	 * Possible values are: PERUNADMIN, VOADMIN, GROUPADMIN, FACILITYADMIN, SELF, VOOBSERVER
	 *
	 * @return TRUE = user has role / FALSE = user doesn't have role
	 */
	public final native boolean hasRole(String role) /*-{
        if (this.hasOwnProperty(role)) {
            return true;
        }
        return false;
    }-*/;

	/**
	 * Return TRUE if user has any role. User's without role shouldn't be allowed to access GUI.
	 *
	 * @return TRUE = user has any role / FALSE = user has no role
	 */
	public final native boolean hasAnyRole() /*-{
        for (var key in this) {
            if (this.hasOwnProperty(key)) {
                return true;
            }
        }
        return false;
    }-*/;

	/**
	 * Return IDS of all editable entities contained in session. Entities are taken from all roles.
	 * This means, that you will get e.g. ID of VO, where user is only Group admin.
	 *
	 * @param entityType "Vo, Facility, User, Group"
	 */
	public final native JsArrayInteger getEditableEntities(String entityType) /*-{
        var entities = new Array();
        for (var key in this) {
            if (this.hasOwnProperty(key)) {
                if (typeof this[key][entityType] !== "undefined") {
                    var ids = this[key][entityType];
                    entities = entities.concat(ids);
                }
            }
        }
        return entities;
    }-*/;


	/**
	 * Return IDS of all editable entities contained in session. Entities are taken base on role.
	 * This means, that you will get only IDS of such entities, where user IS their admin.
	 *
	 * @param role       VOADMIN, SELF, PERUNADMIN, GROUPADMIN, FACILITYADMIN, VOOBSERVER.
	 * @param entityType "Vo, Facility, User, Group"
	 */
	public final native JsArrayInteger getEditableEntities(String role, String entityType) /*-{
        var entities = new Array();
        if (this.hasOwnProperty(role)) {
            if (typeof this[role][entityType] != "undefined") {
                var ids = this[role][entityType];
                entities = entities.concat(ids);
            }
        }
        return entities;
    }-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Roles o) {
		// FIXME - find proper way to compare
		return true;
	}

}
