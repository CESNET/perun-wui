package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
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
	 * @param id          ID of Services
	 * @param name 		  name of Services
	 * @param description description of Services
	 * @param delay		  delay
	 * @param enabled     enabled
	 * @param recurrence  recurrence
	 * @param script      script
	 * @return Service object
	 */
	public static final Service createNew(int id, String name, String description, int delay, int recurrence,
										  boolean enabled, String script) {
		Service sp = new JSONObject().getJavaScriptObject().cast();
		sp.setId(id);
		sp.setName(name);
		sp.setDescription(description);
		sp.setDelay(delay);
		sp.setRecurrence(recurrence);
		sp.setEnabled(enabled);
		sp.setScript(script);
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
	 * Set description of Service
	 *
	 * @param description
	 */
	public final native void setDescription(String description)/*-{
    	this.description = description;
	}-*/;

	/**
	 * Get Delay
	 *
	 * @return delay
	 */
	public final int getDelay(){
		return JsUtils.getNativePropertyInt(this, "sponsoredUser");
	}

	/**
	 * Set Delay
	 *
	 * @param delay
	 */
	public final native void setDelay(int delay)/*-{
    	this.delay = delay;
	}-*/;

	/**
	 * Get Recurrence
	 *
	 * @return Recurrence
	 */
	public final int getRecurrence(){
		return JsUtils.getNativePropertyInt(this, "recurrence");
	}

	/**
	 * Set Recurrence
	 *
	 * @param recurrence
	 */
	public final native void setRecurrence(int recurrence)/*-{
    	this.recurrence = recurrence;
	}-*/;

	/**
	 * Return TRUE if service is enabled.
	 *
	 * @return TRUE = enabled / FALSE = disabled
	 */
	public final boolean isEnabled(){
		return JsUtils.getNativePropertyBoolean(this, "enabled");
	}

	/**
	 * Set Enabled
	 *
	 * @param enabled
	 */
	public final native void setEnabled(boolean enabled)/*-{
    	this.enabled = enabled;
	}-*/;

	/**
	 * Get Script
	 *
	 * @return script
	 */
	public final String getScript(){
		return JsUtils.getNativePropertyString(this, "script");
	}

	/**
	 * Set Script
	 *
	 * @param script
	 */
	public final native void setScript(String script)/*-{
    	this.script = script;
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
