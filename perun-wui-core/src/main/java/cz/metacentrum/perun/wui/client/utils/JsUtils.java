package cz.metacentrum.perun.wui.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import cz.metacentrum.perun.wui.model.beans.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Class with JavaScript native utilities functions.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class JsUtils {

	/**
	 * Clone any JavaScriptObject
	 *
	 * @param obj object to clone
	 * @return clone of original object
	 */
	public static final native JavaScriptObject clone(JavaScriptObject obj)/*-{
        return $wnd.jQuery.extend(true, {}, obj);
    }-*/;

	/**
	 * Helping method which can be used to access any basic object property.
	 * Value is returned as String if exists, or as null if not present in object
	 * or it's value is really null.
	 *
	 * @param jso object to get property from
	 * @param propertyName object property to get
	 * @return property value
	 */
	public static final native String getNativePropertyString(JavaScriptObject jso, String propertyName) /*-{
        if (!jso[propertyName]) return null;
        if (typeof jso[propertyName] === 'undefined') return null;
        if (jso[propertyName] === null) return null;
        return jso[propertyName];
    }-*/;

	/**
	 * Helping method which can be used to access any basic object property.
	 * Value is returned as "int" if exists, or 0 if not present in object
	 * or it's value is really 0.
	 *
	 * @param jso object to get property from
	 * @param propertyName object property to get
	 * @return property value
	 */
	public static final native int getNativePropertyInt(JavaScriptObject jso, String propertyName) /*-{
        if (!jso[propertyName]) return 0;
        if (typeof jso[propertyName] === 'undefined') return 0;
        if (jso[propertyName] === null) return 0;
        return jso[propertyName];
    }-*/;

	/**
	 * Helping method which can be used to access any basic object property.
	 * Value is returned as boolean if exists, or as FALSE if not present in object
	 * or it's value is null.
	 *
	 * @param jso object to get property from
	 * @param propertyName object property to get
	 * @return property value
	 */
	public static final native boolean getNativePropertyBoolean(JavaScriptObject jso, String propertyName) /*-{
        if (!jso[propertyName]) return false;
        if (typeof jso[propertyName] === 'undefined') return false;
        if (jso[propertyName] === null) return false;
        return jso[propertyName];
    }-*/;

	/**
	 * Helping method which can be used to access any basic object property.
	 * Value is returned as float if exists, or as null if not present in object
	 * or it's value is null.
	 *
	 * @param jso object to get property from
	 * @param propertyName object property to get
	 * @return property value
	 */
	public static final native float getNativePropertyFloat(JavaScriptObject jso, String propertyName) /*-{
        if (!jso[propertyName]) return null;
        if (typeof jso[propertyName] === 'undefined') return null;
        if (jso[propertyName] === null) return null;
        return jso[propertyName];
    }-*/;

	/**
	 * Helping method which can be used to access any basic object property.
	 * Value is returned as double if exists, or as null if not present in object
	 * or it's value is null.
	 *
	 * @param jso object to get property from
	 * @param propertyName object property to get
	 * @return property value
	 */
	public static final native double getNativePropertyDouble(JavaScriptObject jso, String propertyName) /*-{
        if (!jso[propertyName]) return null;
        if (typeof jso[propertyName] === 'undefined') return null;
        if (jso[propertyName] === null) return null;
        return jso[propertyName];
    }-*/;

	/**
	 * Helping method which can be used to access any basic object property.
	 *
	 * Value is returned as JavaScriptObject if exists, or as null if not present in object
	 * or it's value is really null. If property is not (typeof === 'object') then null is returned.
	 *
	 * @param jso object to get property from
	 * @param propertyName object property to get
	 * @return property value
	 */
	public static final native JavaScriptObject getNativePropertyObject(JavaScriptObject jso, String propertyName) /*-{
        if (!jso[propertyName]) return null;
        if (typeof jso[propertyName] === 'undefined') return null;
        if (jso[propertyName] === null) return null;
        if (typeof jso[propertyName] !== 'object') return null;
        return jso[propertyName];
    }-*/;

	/**
	 * Helping method which can be used to access any basic object property.
	 *
	 * Value is returned as JsArray<T> if exists, or as null if not present in object
	 * or it's value is really null. If property is not (typeof === 'array') then null is returned.
	 *
	 * @param jso object to get property from
	 * @param propertyName object property to get
	 * @return property value
	 */
	public static final native <T extends JavaScriptObject> JsArray<T> getNativePropertyArray(JavaScriptObject jso, String propertyName) /*-{
        if (!jso[propertyName]) return null;
        if (typeof jso[propertyName] === 'undefined') return null;
        if (jso[propertyName] === null) return null;
        if (jso[propertyName].constructor.toString().indexOf("Array") == -1) return null;
        return jso[propertyName];
    }-*/;

	/**
	 * Helping method which can be used to access any basic object property.
	 *
	 * Value is returned as JsArrayString if exists, or as null if not present in object
	 * or it's value is really null. If property is not (typeof === 'array') then null is returned.
	 *
	 * @param jso object to get property from
	 * @param propertyName object property to get
	 * @return property value
	 */
	public static final native JsArrayString getNativePropertyArrayString(JavaScriptObject jso, String propertyName) /*-{
		if (!jso[propertyName]) return null;
		if (typeof jso[propertyName] === 'undefined') return null;
		if (jso[propertyName] === null) return null;
		if (jso[propertyName].constructor.toString().indexOf("Array") == -1) return null;
		return jso[propertyName];
	}-*/;

	/**
	 * Helping method which can be used to check if object has own property of required name.
	 *
	 * TRUE is returned only if passed JSO is not null, type of object and hasOwnProperty of expected name.
	 * FALSE is returned otherwise.
	 *
	 * @param jso object to get check property existence
	 * @param propertyName object property to check
	 * @return TRUE if property exists, FALSE otherwise
	 */
	public static final native boolean hasOwnProperty(JavaScriptObject jso, String propertyName) /*-{
		if (!jso) return null;
		if (typeof jso === 'undefined') return null;
		if (jso === null) return null;
		if (typeof jso !== 'object') return null;
		return (jso.hasOwnProperty(propertyName));
	}-*/;

	/**
	 * Check if String input can be parsed as Integer
	 *
	 * @param value String input from some text box
	 * @return true if input is number (integer), false otherwise
	 */
	public static final native boolean checkParseInt(String value)/*-{
        // true on any number format, false otherwise
        if (!isNaN(parseFloat(value)) && isFinite(value)) {
            return true;
        } else {
            return false;
        }
    }-*/;

	/**
	 * Returns true if the JS object is an array
	 *
	 * @param jso javscript object to check
	 * @return TRUE if JS object is an array / FALSE otherwise
	 */
	public static final native boolean isJsArray(JavaScriptObject jso) /*-{
        return !(jso.constructor.toString().indexOf("Array") == -1);
    }-*/;

	/**
	 * Return JS Array made from JavaScriptObject.
	 * Return empty array if JavaScriptObject is null.
	 *
	 * @param jso any javascript object
	 * @return JSArray<T> array of javascript objects
	 */
	public static final native <T extends JavaScriptObject> JsArray<T> jsoAsArray(JavaScriptObject jso) /*-{
		if (jso === null) return new Array();
		return jso;
    }-*/;

	/**
	 * Returns passed unknown javascript object as ArrayList<T>.
	 *
	 * If object is null, then empty list is returned (for code safety reasons)
	 *
	 * @param jso Unknown javascript object
	 * @return ArrayList<T> list of unknown objects
	 */
	public static final <T extends JavaScriptObject> ArrayList<T> jsoAsList(JavaScriptObject jso) {

		JsArray<T> arr = jsoAsArray(jso);
		ArrayList<T> list = new ArrayList<T>();

		// return empty list
		if (arr == null) return list;

		// fill list with data
		for (int i = 0; i < arr.length(); i++) {
			list.add(arr.get(i));
		}

		return list;

	}

	/**
	 * Returns passed single object as ArrayList<T> of required type.
	 *
	 * @param jso Unknown javascript object
	 * @return ArrayList<T> list of objects of specified type
	 */
	public static final <T extends JavaScriptObject> ArrayList<T> toList(JavaScriptObject jso) {

		ArrayList<T> l = new ArrayList<T>();
		T object = jso.cast();
		l.add(object);
		return l;

	}

	/**
	 * Returns a Java List from JsArrayString or empty list for null or empty array.
	 *
	 * @param jsa javascript array of strings
	 * @return ArrayList<String> list of strings
	 */
	public static final List<String> listFromJsArrayString(JsArrayString jsa) {
		List<String> arrayList = new ArrayList<String>();
		if (jsa != null) {
			for (int i = 0; i < jsa.length(); i++) {
				String str = jsa.get(i).toString();
				arrayList.add(str);
			}
		}
		return arrayList;
	}

	/**
	 * Decode any ASCII string encoded as base64
	 *
	 * @param encodedString String to decode
	 * @return Decoded string
	 */
	public static final native String decodeBase64(String encodedString)/*-{
		return atob(encodedString);
	}-*/;

	/**
	 * Encode any ASCII string to base64
	 *
	 * @param decodedString String to encode
	 * @return Encoded string
	 */
	public static final native String encodeBase64(String decodedString)/*-{
		return btoa(decodedString);
	}-*/;

	/**
	 * Return current year as number
	 * @return current year
	 */
	public static final native int getCurrentYear() /*-{
		return new Date().getFullYear()
	}-*/;

	public static <T extends JavaScriptObject> List<T> jsoListAsList(List<JavaScriptObject> results) {
		List<T> converted = new ArrayList<>();

		for (JavaScriptObject result : results) {
				converted.add((T)result);
		}

		return converted;
	}
}