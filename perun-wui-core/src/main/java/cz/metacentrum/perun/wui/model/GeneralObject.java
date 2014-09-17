package cz.metacentrum.perun.wui.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * General object is a wrapper around all PerunBeans objects used in GUI.
 * It provides basic functionality, especially methods like {@link #getId() getId()} or {@link #getName() getName()}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class GeneralObject extends JavaScriptObject {

	protected GeneralObject() {
	}

	/**
	 * Return new instance of GeneralObject
	 * WITHOUT ANY PROPERTIES SET IN OBJECT !!
	 *
	 * @return empty GeneralObject
	 */
	public static final GeneralObject createNew() {
		return ((GeneralObject) new JSONObject().getJavaScriptObject().cast());
	}

	/**
	 * Get object's ID. If not present in object, 0 is returned.
	 *
	 * @return ID of object
	 */
	public final int getId() {
		return JsUtils.getNativePropertyInt(this, "id");
	}

	/**
	 * Return name parameter or it's equivalent for any kind of PerunBean object.
	 * If getting of name is not possible or name is null, then null is returned.
	 *
	 * @return name of object
	 */
	public final native String getName() /*-{

        if (this.beanName === null || typeof this.beanName === 'undefined' || !this.beanName) {

       		// NOT PerunBean - safely return name parameter
			return @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "name");

        } else {

            // IF PerunBean safely return name parameter or it's equivalent

            if (this.beanName === "RichMember") {

                var user = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyObject(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "user");
				if (user !== null) {

                    var lastName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(user, "lastName");
                    var firstName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(user, "firstName");

                    var result = "";

                    if (lastName !== null && lastName.length > 0) result += lastName;
                    if (firstName !== null && firstName.length > 0) result += " "+firstName;

                    if (result.length > 0) return result;
                    return null;

                }
	            return null;

            } else if (this.beanName === "User") {

                var lastName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "lastName");
                var firstName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "firstName");

                var result = "";

                if (lastName !== null && lastName.length > 0) result += lastName;
                if (firstName !== null && firstName.length > 0) result += " "+firstName;

                if (result.length > 0) return result;
                return null;

            } else if (this.beanName === "RichUser") {

                var lastName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "lastName");
                var firstName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "firstName");

                var result = "";

                if (lastName !== null && lastName.length > 0) result += lastName;
                if (firstName !== null && firstName.length > 0) result += " "+firstName;

                if (result.length > 0) return result;
                return null;

            } else if (this.beanName === "Author") {

                var lastName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "lastName");
                var firstName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "firstName");

                var result = "";

                if (lastName !== null && lastName.length > 0) result += lastName;
                if (firstName !== null && firstName.length > 0) result += " "+firstName;

                if (result.length > 0) return result;
                return null;

            } else if (this.beanName === "ExecService") {

                var service = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyObject(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "service");
	            if (service !== null) {
                    var serviceName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(service, "name");
                    var execType = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "execServiceType");
		            return serviceName + "("+execType+")";
                } else {
		            return null;
	            }

            } else if (this.beanName === "AttributeDefinition") {

                return @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "displayName");

            } else if (this.beanName === "Attribute") {

                return @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "displayName");

            } else if (this.beanName === "ApplicationMail") {

                return @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "mailType");

            } else if (this.beanName === "Publication") {

                return @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "title");

            } else if (this.beanName === "PublicationSystem") {

                return @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "friendlyName");

            } else if (this.beanName === "UserExtSource") {

                var login = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "login");
	            var extSource = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyObject(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "extSource");
	            var extSourceName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(extSource, "name");

	            return login + " / " + extSourceName;

            } else if (this.beanName === "Host") {

                return @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "hostname");

            } else if (this.beanName === "RichDestination") {

                return @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "destination");

            } else {

	            // PerunBean with default name parameter
                return @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "name");

            }

        }

    }-*/;

	/**
	 * Return description parameter for any kind of PerunBean object.
	 * If not present in object, null is returned.
	 *
	 * @return description of Object
	 */
	public final String getDescription() {
		return JsUtils.getNativePropertyString(this, "description");
	}

	/**
	 * Return true if object is checked (selected) in table.
	 * <p/>
	 * IMPORTANT: This is helper method used only in GUI.
	 * No Perun object has this property in base system.
	 *
	 * @return TRUE = is checked / FALSE = not checked
	 */
	public final boolean isChecked() {
		return JsUtils.getNativePropertyBoolean(this, "isChecked");
	}

	/**
	 * Set object as checked (selected) in table. This stores
	 * information in object only, doesn't update any table or
	 * it's selection model.
	 * <p/>
	 * IMPORTANT: This is helper method used only in GUI.
	 * No Perun object has this property in base system.
	 *
	 * @param value TRUE = checked / FALSE = not checked
	 */
	public final native void setChecked(boolean value) /*-{
        this.isChecked = value;
    }-*/;

	/**
	 * Get object's type, equals to Class.getSimpleName().
	 * Value is stored to object on server side and only
	 * for PerunBeans object.
	 * <p/>
	 * If value not present in object, "JavaScriptObject"
	 * is returned instead.
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
	 * This property is not defined for non-PerunBeans
	 * objects in base system (server side).
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
	public final boolean equals(GeneralObject o) {
		return o.getId() == this.getId();
	}

}