package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import com.sun.javafx.beans.IDProperty;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

import java.util.Map;

/**
 * Overlay type for ExtSource object from Perun
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ExtSource extends GeneralObject {

	public enum ExtSourceType {

		X509("cz.metacentrum.perun.core.impl.ExtSourceX509"),
		IDP("cz.metacentrum.perun.core.impl.ExtSourceIdp"),
		KERBEROS("cz.metacentrum.perun.core.impl.ExtSourceKerberos"),
		SQL("cz.metacentrum.perun.core.impl.ExtSourceSql"),
		ISMU("cz.metacentrum.perun.core.impl.ExtSourceISMU"),
		LDAP("cz.metacentrum.perun.core.impl.ExtSourceLdap"),
		PERUN("cz.metacentrum.perun.core.impl.ExtSourcePerun"),
		INTERNAL("cz.metacentrum.perun.core.impl.ExtSourceInternal"),
		XML("cz.metacentrum.perun.core.impl.ExtSourceXML");

		private final String type;

		ExtSourceType(String type) {
			this.type= type;
		}

		public String getType() {
			return type;
		}

	}

	protected ExtSource() {}

	/**
	 * Return new instance of ExtSource with basic properties set.
	 *
	 * @param id   ID of ExtSource
	 * @param name Name of ExtSource
	 * @param type Type of ExtSource definition
	 * @return ExtSource object
	 */
	public static final ExtSource createNew(int id, String name, String type) {
		ExtSource extSource = new JSONObject().getJavaScriptObject().cast();
		extSource.setId(id);
		extSource.setName(name);
		extSource.setType(type);
		extSource.setObjectType("ExtSource");
		return extSource;
	}

	/**
	 * Sets ID of ExtSource definition
	 *
	 * @param id ID to set
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Sets name of ExtSource definition
	 *
	 * @param name name to set to ExtSource
	 */
	public final native void setName(String name) /*-{
        this.name = name;
    }-*/;

	/**
	 * Get type of ExtSource definition. It's class representing ExtSource connector
	 * implementation e.g.: "cz.metacentrum.perun.core.impl.ExtSourceKerberos".
	 *
	 * @return type of ExtSource definition
	 */
	public final String getType() {
		return JsUtils.getNativePropertyString(this, "type");
	}

	/**
	 * Sets type of ExtSource definition. It must be class representation of ExtSource connector
	 * implementation e.g.: "cz.metacentrum.perun.core.impl.ExtSourceKerberos".
	 *
	 * @param type Type of ExtSource definition
	 */
	public final native void setType(String type) /*-{
        this.type = type;
    }-*/;

	/**
	 * Get map of attributes associated with ExtSource definition.
	 * They provide mapping from ExtSource attributes to Perun attributes.
	 *
	 * IMPORTANT: value is present only when ExtSource is retrieved inside Candidate (UserExtSource) object.
	 *
	 * @return attributes map
	 */
	public final native Map<String, String> getAttributes() /*-{
        return this.attributes;
    }-*/;

	/**
	 * Compares to another object
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(ExtSource o) {
		return o.getId() == this.getId();
	}

}