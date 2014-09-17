package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for ServicesPackage object
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ServicesPackage extends GeneralObject {

	protected ServicesPackage() {
	}

	/**
	 * Return new instance of ServicesPackage with basic properties set.
	 *
	 * @param id          ID of ServicesPackage
	 * @param name        name of ServicesPackage
	 * @param description description of ServicesPackage
	 * @return ServicesPackage object
	 */
	public static final ServicesPackage createNew(int id, String name, String description) {
		ServicesPackage sp = new JSONObject().getJavaScriptObject().cast();
		sp.setId(id);
		sp.setName(name);
		sp.setDescription(description);
		sp.setObjectType("ServicesPackage");
		return sp;
	}

	/**
	 * Set ID of service package
	 *
	 * @param id ID of service package
	 */
	public final native void setId(int id)  /*-{
        this.id = id;
    }-*/;

	/**
	 * Set name of service package
	 *
	 * @param name of service package
	 */
	public final native void setName(String name)  /*-{
        this.name = name;
    }-*/;

	/**
	 * Set description of service package
	 *
	 * @param description of service package
	 */
	public final native void setDescription(String description)  /*-{
        this.description = description;
    }-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(ServicesPackage o) {
		return o.getId() == this.getId();
	}

}
