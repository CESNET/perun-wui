package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for Service object
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Service extends GeneralObject {

	protected Service() {
	}

	/**
	 * Return new instance of Services with basic properties set.
	 *
	 * @param id   ID of Services
	 * @param name name of Services
	 * @return ServicesPackage object
	 */
	public static final Service createNew(int id, String name) {
		Service sp = new JSONObject().getJavaScriptObject().cast();
		sp.setId(id);
		sp.setName(name);
		sp.setObjectType("Service");
		return sp;
	}

	/**
	 * Sets ID of service
	 *
	 * @param id id of service
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Sets name of service
	 *
	 * @param name of service
	 */
	public final native String setName(String name)  /*-{
        this.name = name;
    }-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Service o) {
		return o.getId() == this.getId();
	}

}
