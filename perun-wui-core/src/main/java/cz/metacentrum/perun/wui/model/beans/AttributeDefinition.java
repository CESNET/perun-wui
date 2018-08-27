package cz.metacentrum.perun.wui.model.beans;

import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * OverlayType for Attribute Definition object
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AttributeDefinition extends GeneralObject {

	protected AttributeDefinition() {
	}

	/**
	 * Set's ID of an attribute
	 *
	 * @param id
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Set new description of an attribute
	 *
	 * @param desc new description of attribute
	 */
	public final native void setDescription(String desc) /*-{
        this.description = desc;
    }-*/;

	/**
	 * Gets friendly name of an attribute (last part of URN)
	 *
	 * @return friendly name of an attribute
	 */
	public final String getFriendlyName() {
		return JsUtils.getNativePropertyString(this, "friendlyName");
	}

	/**
	 * Gets display name of an attribute
	 *
	 * @return display name
	 */
	public final String getDisplayName() {
		return JsUtils.getNativePropertyString(this, "displayName");
	}

	/**
	 * Gets namespace of an attribute (first part of URN)
	 *
	 * @return namespace of an attribute
	 */
	public final String getNamespace() {
		return JsUtils.getNativePropertyString(this, "namespace");
	}

	/**
	 * Get whole (unique) name of an attribute (URN)
	 *
	 * @return whole name of an attribute
	 */
	public final String getURN() {
		return getNamespace() + ":" + getFriendlyName();
	}

	/**
	 * Set new display name of an attribute. You can get value back by {@link #getName() getName()}.
	 *
	 * @param displayName new display name of attribute definition
	 */
	public final native void setDisplayName(String displayName) /*-{
        this.displayName = displayName;
    }-*/;

	/**
	 * Get base friendly name of an attribute
	 * <p/>
	 * e.g.: urn:perun:user:attribute-def:def:login-namespace:meta
	 * return "login-namespace"
	 * <p/>
	 * if no parameter present, return whole friendlyName
	 *
	 * @return base friendly name of attribute
	 */
	public final String getBaseFriendlyName() {
		return JsUtils.getNativePropertyString(this, "baseFriendlyName");
	}

	/**
	 * Get friendly name parameter of attribute
	 * <p/>
	 * e.g.: urn:perun:user:attribute-def:def:login-namespace:meta
	 * return "meta"
	 * <p/>
	 * If no parameter present, return ":";
	 *
	 * @return friendly name parameter of attribute
	 */
	public final String getFriendlyNameParameter() {
		return JsUtils.getNativePropertyString(this, "friendlyNameParameter");
	}

	/**
	 * Get entity attribute is meant for (is part of URN).
	 * Values like: user, member, user_facility, ...
	 *
	 * @return entity attributes is mean for (is part of URN)
	 */
	public final String getEntity() {
		return JsUtils.getNativePropertyString(this, "entity");
	}

	/**
	 * Return definition type of an attribute.
	 * Possible values are: 'core', 'def', 'opt', 'virt' or null if not present.
	 *
	 * @return definition type
	 */
	public final native String getDefinition() /*-{

        var namespace = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "namespace");
        if (namespace !== null) {
            var temp = new Array();
            temp = namespace.split(":");
            if (temp.length >= 5) return temp[4];
            return null
        }
        return null;

    }-*/;

	/**
	 * Gets type of an attribute. Type is expected to be java's class of attributes value.
	 * <p/>
	 * Possible values are: 'java.lang.String', 'java.lang.Integer', 'java.util.ArrayList', 'java.util.LinkedHashMap'.
	 *
	 * @return type of attribute
	 */
	public final String getType() {
		return JsUtils.getNativePropertyString(this, "type");
	}

	/**
	 * Return TRUE if attribute value is checked on uniqueness, FALSE otherwise.
	 * For multivalued types like java.util.ArrayList, each value
	 * in the list for a given object must be unique among all values for all objects.
	 * Entityless and virtual attributes cannot be unique.
	 *
	 * @return TRUE if unique
	 */
	public final boolean isUnique() {
		return JsUtils.getNativePropertyBoolean(this, "unique");
	}

	/**
	 * Set TRUE if attribute value should be checked on uniqueness, FALSE otherwise.
	 *
	 * @param unique TRUE = should be unique / FALSE not unique
	 */
	public final native void setUnique(boolean unique) /*-{
		this.unique = unique;
	}-*/;

	/**
	 * Check if attribute value is writable for user or not (authz is provided and determined by Perun's server side)
	 *
	 * @return TRUE if writable / FALSE otherwise
	 */
	public final native boolean isWritable() /*-{

        // if no authz provided = writable (Perun core can handle it by itself)
        if (!this.writable) return true;
        if (typeof this.writable === 'undefined') return true;
        if (this.writable === null) return true;
        return this.writable;

    }-*/;

	/**
	 * Set if attribute value is writable by user.
	 *
	 * @param write TRUE = writable / FALSE not writable
	 */
	public final native void setWritable(boolean write) /*-{
        this.writable = write;
    }-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(AttributeDefinition o) {
		return o.getId() == this.getId();
	}

}
