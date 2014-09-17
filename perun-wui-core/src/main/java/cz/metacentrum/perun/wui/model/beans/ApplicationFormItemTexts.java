package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * Overlay type for ApplicationFormItemTexts from Perun-Registrar.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ApplicationFormItemTexts extends JavaScriptObject {

	protected ApplicationFormItemTexts() {}

	// TODO constructor

	/**
	 * Get Locale (en,cs,...) this form item texts are in.
	 *
	 * @return Locale of stored text.
	 */
	public final String getLocale() {
		return JsUtils.getNativePropertyString(this, "locale");
	}

	/**
	 * Set Locale (en,cs,...) this form item texts are in.
	 *
	 * @param locale Locale of stored text.
	 */
	public final native void setLocale(String locale) /*-{
		this.locale = locale;
	}-*/;

	/**
	 * Get label of parent form item.
	 *
	 * @return Label of form item.
	 */
	public final String getLabel() {
		return JsUtils.getNativePropertyString(this, "label");
	}

	/**
	 * Set label of parent form item.
	 *
	 * @param label Label of parent form item
	 */
	public final native void setLabel(String label) /*-{
		this.label = label;
	}-*/;

	/**
	 * Get help text of parent form item.
	 *
	 * @return Text displayed as help of form item.
	 */
	public final String getHelp() {
		return JsUtils.getNativePropertyString(this, "help");
	}

	/**
	 * Set help text of parent form item.
	 *
	 * @param help Text displayed as help of parent form item
	 */
	public final native void setHelp(String help) /*-{
		this.help = help;
	}-*/;

	/**
	 * Get string with possible options for selection-like form items.
	 *
	 * // TODO - javadoc of data format
	 *
	 * @return String representation of options
	 */
	public final String getOptions() {
		return JsUtils.getNativePropertyString(this, "options");
	}

	/**
	 * Set string with possible options for selection-like form items.
	 *
	 * // TODO - javadoc of data format
	 *
	 * @param options String representation of options
	 */
	public final native void setOptions(String options) /*-{
		this.options = options;
	}-*/;

	/**
	 * Get error text of parent form item. Displayed when user insert wrong value to form item.
	 *
	 * @return Text displayed as help of form item.
	 */
	public final String getErrorText() {
		return JsUtils.getNativePropertyString(this, "errorMessage");
	}

	/**
	 * Set error text of parent form item. Displayed when user insert wrong value to form item.
	 *
	 * @param text Text displayed as help of parent form item
	 */
	public final native void setErrorText(String text) /*-{
		this.errorMessage = text;
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

}