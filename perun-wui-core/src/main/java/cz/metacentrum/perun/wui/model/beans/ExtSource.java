package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

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
		XML("cz.metacentrum.perun.core.impl.ExtSourceXML"),
		CSV("cz.metacentrum.perun.core.impl.ExtSourceCSV"),
		GOOGLE("cz.metacentrum.perun.core.impl.ExtSourceGoogle");

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
	 * Compares to another object
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(ExtSource o) {
		return o.getId() == this.getId();
	}

}