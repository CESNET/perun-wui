package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Overlay type for ApplicationFormItem from Perun-Registrar.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ApplicationFormItem extends JavaScriptObject {

	/**
	 * Enumeration of possible form items types. Type define widget used
	 * when displaying form item and also editable and visible state.
	 */
	public enum ApplicationFormItemType {
		HTML_COMMENT,
		SUBMIT_BUTTON,
		AUTO_SUBMIT_BUTTON,
		FROM_FEDERATION_SHOW,
		FROM_FEDERATION_HIDDEN,
		PASSWORD,
		VALIDATED_EMAIL,
		TEXTFIELD,
		TEXTAREA,
		CHECKBOX,
		RADIO,
		SELECTIONBOX,
		COMBOBOX,
		USERNAME,
		HEADING,
		TIMEZONE
	}

	protected ApplicationFormItem() {}

	/**
	 * Return new instance of ApplicationFormItem with basic properties set.
	 *
	 * @param id
	 * @param shortname shortName of form item
	 * @param required TRUE = item is required / FALSE = item is optional
	 * @param type ApplicationFormItemType of form item
	 * @param federationAttribute Name of IdP federation attribute
	 * @param perunSourceAttribute URN of linked attribute definition
	 * @param perunDestinationAttribute URN of linked attribute definition
	 * @param regex Regular expression to set
	 * @param applicationTypes List<Application.ApplicationType> List of application types this form item is used on
	 * @param ordnum Ordering number
	 * @param forDelete TRUE = marked for delete / FALSE = not marked for delete
	 * @return
	 */
	static public ApplicationFormItem createNew(int id, String shortname, boolean required, ApplicationFormItemType type,
												String federationAttribute, String perunSourceAttribute, String perunDestinationAttribute, String regex,
												List<Application.ApplicationType> applicationTypes, Integer ordnum, boolean forDelete)	{
		ApplicationFormItem afi = new JSONObject().getJavaScriptObject().cast();
		afi.setId(id);
		afi.setShortname(shortname);
		afi.setRequired(required);
		afi.setType(type);
		afi.setFederationAttribute(federationAttribute);
		afi.setPerunSourceAttribute(perunSourceAttribute);
		afi.setPerunDestinationAttribute(perunDestinationAttribute);
		afi.setRegex(regex);
		afi.setApplicationTypes(applicationTypes);
		afi.setOrdnum(ordnum);
		afi.setForDelete(forDelete);
		return afi;}

	/**
	 * Get form item's ID.
	 *
	 * @return ID of object
	 */
	public final int getId() {
		return JsUtils.getNativePropertyInt(this, "id");
	}

	/**
	 * Set form item's ID
	 *
	 * @param id ID to set
	 */
	public final native void setId(int id) /*-{
		this.id = id;
	}-*/;

	/**
	 * Get shortName of form item.
	 *
	 * @return shortName of form item.
	 */
	public final String getShortname() {
		return JsUtils.getNativePropertyString(this, "shortname");
	}

	/**
	 * Set form item's shortName
	 *
	 * @param shortName shortName to set
	 */
	public final native void setShortname(String shortName) /*-{
		this.shortname = shortName;
	}-*/;

	/**
	 * Return TRUE if item is required, FALSE otherwise.
	 *
	 * User CAN'T submit form item with empty value if is required with exception of non-editable or invisible items.
	 *
	 * @return TRUE = item is required / FALSE = item is optional
	 */
	public final boolean isRequired() {
		return JsUtils.getNativePropertyBoolean(this, "required");
	}

	/**
	 * Set TRUE if item is required, FALSE otherwise.
	 *
	 * User CAN'T submit form item with empty value if is required with exception of non-editable or invisible items.
	 *
	 * @param required TRUE = item is required / FALSE = item is optional
	 */
	public final native void setRequired(boolean required) /*-{
		this.required = required;
	}-*/;

	/**
	 * Get type of form item. Type defines used widget when drawing the item and editable and visible state.
	 * @see ApplicationFormItemType
	 *
	 * @return Type of form item.
	 */
	public final ApplicationFormItemType getType(){
		return ApplicationFormItemType.valueOf(JsUtils.getNativePropertyString(this, "type"));
	}

	/**
	 * Set type of form item. Type defines used widget when drawing the item and editable and visible state.
	 * @see ApplicationFormItemType
	 *
	 * @param type Type of form item.
	 */
	public final void setType(ApplicationFormItemType type){
		setType(String.valueOf(type));
	}

	private final native void setType(String type) /*-{
		this.type = type;
	}-*/;

	/**
	 * Get URN of Attribute definition this form item is pre-filled from.
	 *
	 * Value from attribute is pushed to form item on item retrieval.
	 *
	 * @see AttributeDefinition#getURN()
	 *
	 * @return URN of linked attribute definition
	 */
	public final String getPerunSourceAttribute() {
		return JsUtils.getNativePropertyString(this, "perunSourceAttribute");
	}

	/**
	 * Set URN of Attribute definition this form item is pre-filled from.
	 *
	 * Value from attribute is pushed to form item on item retrieval.
	 *
	 * @see AttributeDefinition#getURN()
	 *
	 * @param attribute URN of linked attribute definition
	 */
	public final native void setPerunSourceAttribute(String attribute) /*-{
		this.perunSourceAttribute = attribute;
	}-*/;

	/**
	 * Get URN of Attribute definition this form item is stored into.
	 *
	 * Vale from form item is stored to attribute on application approval.
	 *
	 * @see AttributeDefinition#getURN()
	 *
	 * @return URN of linked attribute definition
	 */
	public final String getPerunDestinationAttribute() {
		return JsUtils.getNativePropertyString(this, "perunDestinationAttribute");
	}

	/**
	 * Set URN of Attribute definition this form item is stored into.
	 *
	 * Vale from form item is stored to attribute on application approval.
	 *
	 * @see AttributeDefinition#getURN()
	 *
	 * @param attribute URN of linked attribute definition
	 */
	public final native void setPerunDestinationAttribute(String attribute) /*-{
		this.perunDestinationAttribute = attribute;
	}-*/;

	/**
	 * Get name of IdP federation attribute this form item is linked to.
	 *
	 * Value from attribute (if provided by IDP) is pushed to form item on item retrieval.
	 *
	 * @return Name of IdP federation attribute.
	 */
	public final String getFederationAttribute() {
		return JsUtils.getNativePropertyString(this, "federationAttribute");
	}

	/**
	 * Set name of IdP federation attribute this form item is linked to.
	 *
	 * Value from attribute (if provided by IDP) is pushed to form item on item retrieval.
	 *
	 * @param attribute Name of IdP federation attribute.
	 */
	public final native void setFederationAttribute(String attribute) /*-{
		this.federationAttribute = attribute;
	}-*/;

	/**
	 * Get regular expression used to validate user input for this form item.
	 *
	 * IMPORTANT: Validation is performed in Javascript, so regex must conform it.
	 *
	 * @return Regular expression used to validate input.
	 */
	public final String getRegex() {
		return JsUtils.getNativePropertyString(this, "regex");
	}

	/**
	 * Set regular expression used to validate user input for this form item.
	 *
	 * IMPORTANT: Validation is performed in Javascript, so regex must conform it.
	 *
	 * @param regex Regular expression to set.
	 */
	public final native void setRegex(String regex) /*-{
		this.regex = regex;
	}-*/;

	/**
	 * Get ordering number used when sorting form items on form.
	 *
	 * @return Ordering number.
	 */
	public final int getOrdnum() {
		return JsUtils.getNativePropertyInt(this, "ordnum");
	}

	/**
	 * Set ordering number used when sorting form items on form.
	 *
	 * @param ordnum Ordering number.
	 */
	public final native void setOrdnum(int ordnum) /*-{
		this.ordnum = ordnum;
	}-*/;

	/**
	 * Get list of application types this form item is used on.
	 * @see cz.metacentrum.perun.wui.model.beans.Application.ApplicationType
	 *
	 * @return List of application types.
	 */
	public final ArrayList<Application.ApplicationType> getApplicationTypes() {

		// TODO - check if it works
		List<String> list = JsUtils.listFromJsArrayString(JsUtils.getNativePropertyArrayString(this, "applicationTypes"));
		ArrayList<Application.ApplicationType> result = new ArrayList<Application.ApplicationType>();
		for (String s : list) {
			result.add(Application.ApplicationType.valueOf(s));
		}
		return result;

	}

	/**
	 * Set list of application types this form item is used on.
	 *
	 * @see cz.metacentrum.perun.wui.model.beans.Application.ApplicationType
	 *
	 * @param applicationTypes List of application types this form item is used on.
	 */
	public final void setApplicationTypes(List<Application.ApplicationType> applicationTypes){
		JSONArray array = new JSONArray();
		for(int i = 0; i < applicationTypes.size(); i++){
			array.set(i, new JSONString(applicationTypes.get(i).toString().toUpperCase()));
		}
		this.setApplicationTypes(array.getJavaScriptObject());
	}

	public final native void setApplicationTypes(JsArrayString applicationTypes) /*-{
		this.applicationTypes = applicationTypes;
	}-*/;

	private final native void setApplicationTypes(JavaScriptObject object)/*-{
        this.applicationTypes = object;
	}-*/;

	// TODO - item texts

	/**
	 * Get ItemTexts
	 * @return
	 */
	public final native ApplicationFormItemTexts getItemTexts(String locale) /*-{
		if(!(locale in this.i18n)){
			this.i18n[locale] = {locale: locale, errorMessage : "", help : "", label : "", options : ""};
		}
		return this.i18n[locale];
	}-*/;

	/**
	 * Set item texts
	 */
	public final native void setItemTexts(String locale, ApplicationFormItemTexts itemTexts) /*-{
		this.i18n[locale] = itemTexts;
	}-*/;

	public final native String[] getItemTextLocales() /*-{
		console.log(Object.keys(this.i18n));
		return Object.keys(this.i18n);
	}-*/;

	/**
	 * Return TRUE if item was marked for deletion. FALSE otherwise.
	 *
	 * @return TRUE = marked for delete / FALSE = not marked for delete.
	 */
	public final boolean isForDelete() {
		return JsUtils.getNativePropertyBoolean(this, "forDelete");
	}

	/**
	 * Set TRUE if item is marked for deletion. FALSE otherwise.
	 *
	 * @param del TRUE = marked for delete / FALSE = not marked for delete.
	 */
	public final native void setForDelete(boolean del) /*-{
		this.forDelete = del;
	}-*/;

	/**
	 * Return TRUE if item was edited (configuration change). FALSE otherwise.
	 *
	 * IMPORTANT: This property is used only in GUI. Do not send it
	 * to the Perun server as part of object.
	 *
	 * @return TRUE = edited / FALSE = not edited
	 */
	public final boolean isEdited(){
		return JsUtils.getNativePropertyBoolean(this, "edited");
	}

	/**
	 * Set item to edited state (configuration change).
	 *
	 * IMPORTANT: This property is used only in GUI. Do not send it
	 * to the Perun server as part of object.
	 *
	 * @param edited TRUE = edited / FALSE = not edited (default)
	 */
	public final native void setEdited(boolean edited) /*-{
		this.edited = edited;
	}-*/;

	/**
	 * Get object's type, equals to Class.getSimpleName().
	 * Value is stored to object on server side and only for PerunBeans object.
	 * <p/>
	 * If value not present in object, "JavaScriptObject" is returned instead.
	 *
	 * @return type of object
	 */
	public final String getObjectType() {

		if (JsUtils.getNativePropertyString(this, "beanName") == null) return "JavaScriptObject";
		return JsUtils.getNativePropertyString(this, "beanName");

	}

	/**
	 * Set object's type. It should be specific name
	 * of PerunBean equals to Class.getSimpleName().
	 * <p/>
	 * This property is not defined for non-PerunBeans objects in Perun system (server side).
	 *
	 * @param type type of object
	 */
	public final native void setObjectType(String type) /*-{
		this.beanName = type;
	}-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(ApplicationFormItem o){
		return o.getId() == this.getId();
	}

}
