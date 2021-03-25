package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * Overlay type for ApplicationFormItemData from Perun-Registrar.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ApplicationFormItemData extends JavaScriptObject {

	protected ApplicationFormItemData() {}

	/**
	 * Creates the new object
	 *
	 * @param formItem
	 * @param shortname
	 * @param value
	 * @param prefilled
	 * @param assuranceLevel
	 * @return
	 */
	static public ApplicationFormItemData construct(ApplicationFormItem formItem, String shortname, String value, String prefilled, String assuranceLevel){

		ApplicationFormItemData obj = new JSONObject().getJavaScriptObject().cast();
		obj.setFormItem(formItem);
		obj.setShortname(shortname);
		obj.setValue(value);
		obj.setPrefilledValue(prefilled);
		obj.setAssuranceLevel(assuranceLevel);

		return obj;
	}


	/**
	 * Get ID
	 * @return id
	 */
	public final int getId(){
		return JsUtils.getNativePropertyInt(this, "id");
	}


	/**
	 * Set id
	 * @param id
	 */
	public final native void setId(int id) /*-{
		this.id = id;
	}-*/;

	/**
	 * Get formItem
	 * @return formItem
	 */
	public final native ApplicationFormItem getFormItem() /*-{
		return this.formItem;
	}-*/;

	/**
	 * Set formItem
	 */
	public final native void setFormItem(ApplicationFormItem formItem) /*-{
		this.formItem = formItem;
	}-*/;

	/**
	 * Get shorname
	 * @return shortname
	 */
	public final native String getShortname() /*-{
		if(typeof this.shortname == "undefined") return "";
		return this.shortname;
	}-*/;

	/**
	 * Set shortname
	 */
	public final native void setShortname(String shortname) /*-{
		this.shortname = shortname;
	}-*/;

	/**
	 * Get value
	 * @return value
	 */
	public final native String getValue() /*-{
		if(typeof this.value == "undefined") return "";
		return this.value;
	}-*/;

	/**
	 * Set value
	 */
	public final native void setValue(String value) /*-{
		this.value = value;
	}-*/;

	/**
	 * Get prefilled value
	 * @return value
	 */
	public final native String getPrefilledValue() /*-{
		if(typeof this.prefilledValue == "undefined") return "";
		return this.prefilledValue;
	}-*/;

	/**
	 * Set prefilled value
	 */
	public final native void setPrefilledValue(String value) /*-{
		this.prefilledValue = value;
	}-*/;

	/**
	 * Get assuranceLevel
	 * @return assuranceLevel
	 */
	public final native String getAssuranceLevel() /*-{
		if(typeof this.assuranceLevel == "undefined") return "";
		return this.assuranceLevel;
	}-*/;

	public final int getAssuranceLevelAsInt() {

		String value = getAssuranceLevel();
		if (value == null) return 0;
		if ("null".equals(value)) return 0;
		if (value.isEmpty()) return 0;

		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			return 0;
		}

	}

	/**
	 * Set assuranceLevel
	 */
	public final native void setAssuranceLevel(String assuranceLevel) /*-{
		this.assuranceLevel = assuranceLevel;
	}-*/;

	/**
	 * Is generated
	 * @return TRUE if pre-filled value was generated
	 */
	public final native boolean isGenerated() /*-{
		if(typeof this.generated == "undefined") return false;
		return this.generated;
	}-*/;

	/**
	 * Set generated
	 */
	public final native void setGenerated(boolean generated) /*-{
		this.generated = generated;
	}-*/;

	/**
	 * Returns Perun specific type of object
	 *
	 * @return type of object
	 */
	public final native String getObjectType() /*-{
		if (!this.beanName) {
			return "JavaScriptObject"
		}
		return this.beanName;
	}-*/;

	/**
	 * Sets Perun specific type of object
	 *
	 * @param type type of object
	 */
	public final native void setObjectType(String type) /*-{
		this.beanName = type;
	}-*/;

	/**
	 * Returns the status of this item in Perun system as String
	 * VALID, INVALID, SUSPENDED, EXPIRED, DISABLED
	 *
	 * @return string which defines item status
	 */
	public final native String getStatus() /*-{
		return this.status;
	}-*/;

	/**
	 * Compares to another object
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(ApplicationFormItemData o) {
		return o.getShortname().equals(this.getShortname());
	}


}
