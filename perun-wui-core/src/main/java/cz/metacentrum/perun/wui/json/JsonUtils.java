package cz.metacentrum.perun.wui.json;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility functions for handling all JSON strings and objects.
 *
 * @author Vaclav Mach <374430@mail.muni.cz>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class JsonUtils {

	/**
	 * Parses a string in JSON format (with trusted content) and cast it into JsArray<JavaScriptObject>.
	 * Standard eval() is used to parse string.
	 *
	 * @param json string to parse
	 * @return array of objects contained in input JSON string
	 */
	public static final native <T extends JavaScriptObject> JsArray<T> jsonAsArray(String json) /*-{
		return eval(json);
	}-*/;

	/**
	 * Parses a string in JSON format (with trusted content) and cast it into JavaScriptObject.
	 * jQuery.parseJSON() is used to parse string.
	 * If string is not in JSON format, the returned JavaScriptObject is {@link cz.metacentrum.perun.wui.model.BasicOverlayObject BasicOverlayObject}.
	 *
	 * @param json that you trust
	 * @return JavaScriptObject that you can cast to any Overlay Type
	 */
	public static final native JavaScriptObject parseJson(String json) /*-{
		try {
			var response = $wnd.jQuery.parseJSON(json);
			if (typeof response === 'object') {
				return response;
			}
			// if returned value is not json object
			return {"value": response};
		} catch (err) {
			// if parsing fails, return raw data wrapped in BasicOverlayObject
			return {"value": response};
		}
	}-*/;

	/**
	 * Parses a JavaScript map into a Java map.
	 *
	 * @param str
	 * @return
	 */
	public static final Map<String, JSONValue> parseJsonToMap(String str) {
		return parseJsonToMap(parseJson(str));
	}

	/**
	 * Parses a JavaScript map into a Java map.
	 *
	 * @param jso
	 * @return
	 */
	public static final Map<String, JSONValue> parseJsonToMap(JavaScriptObject jso) {

		if (jso == null) {
			return new HashMap<String, JSONValue>();   // when null, return empty map
		}

		JSONObject obj = new JSONObject(jso);

		HashMap<String, JSONValue> m = new HashMap<String, JSONValue>();
		for (String key : obj.keySet()) {
			m.put(key, obj.get(key));
		}
		return m;

	}

	/**
	 * Convert original JavaScriptObject into JSONObject to be sent to Perun's API.
	 *
	 * All common non-standard properties are removed from resulting object.
	 *
	 * @param object
	 * @param <T> Type of object to rebuild
	 * @return JSONObject representation of original JS object without non-standard properties
	 */
	public static final <T extends JavaScriptObject> JSONObject convertToJSON(T object) {

		JSONObject origin = new JSONObject(object);
		JSONObject result = new JSONObject();
		for (String key : origin.isObject().keySet()) {
			if (key.equalsIgnoreCase("isChecked") || key.equalsIgnoreCase("$H")) {
				continue;
			} else {
				result.put(key, origin.get(key));
			}
		}

		return result;

	}

}