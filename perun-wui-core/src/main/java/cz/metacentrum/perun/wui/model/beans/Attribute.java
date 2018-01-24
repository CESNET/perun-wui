package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONValue;
import cz.metacentrum.perun.wui.json.JsonUtils;

import java.util.Map;

/**
 * OverlayType for Attribute object, which is extension of {@link AttributeDefinition AttributeDefinition} with actual value.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Attribute extends AttributeDefinition {

	public static int counter = 0;

	protected Attribute() {
	}

	/**
	 * Gets any value of an attribute as a string.
	 * Return null if value is not defined / is null.
	 *
	 * @return value of attribute
	 */
	public final native String getValue() /*-{
        if (!this.value) return null;
        if (typeof this.value === 'undefined') return null;
        if (this.value === null) return null;
        return this.value.toString();
    }-*/;

	/**
	 * Sets a new value to attribute. String input is checked
	 * before setting for respecting value type
	 *
	 * @param newValue new value to be set to attribute
	 * @return true if success (correct value)
	 */
	public final native boolean setValue(String newValue) /*-{
        // add trim function
        String.prototype.trim = function () {
            return this.replace(/(^\s*)|(\s*$)/g, "")
        };
        if (this.type == "java.lang.Integer") {
            // true on any number format, false otherwise
            if (!isNaN(parseFloat(newValue)) && isFinite(newValue)) {
                this.value = parseInt(newValue);
                return true;
            } else {
                return false;
            }
        }
        if (this.type == "java.util.ArrayList") {
            this.value = [];
            var count = 0;
            var input = "";
            for (var i = 0; i < newValue.length; i++) {
                // escaped ","
                if (newValue[i] == "\\" && i + 1 < newValue.length && newValue[i + 1] == ",") {
                    i++;                         // skip escape char
                    input = input + newValue[i]  // add next
                    continue;                    // and continue
                    // normal ","
                } else {
                    if (newValue[i] == ",") {
                        input = input.trim();          // trim whitespace on sides
                        if (input == "") {
                            continue;
                        } // skip empty values
                        this.value[count] = input;     // save previous value
                        count++;                       // update field counter
                        input = "";                    // clear input string
                        continue;                      // skip "," and continue
                    }
                    input = input + newValue[i]  	   // append letter
                }
            }
            input = input.trim();               // at end - trim value
            if (input == "") {
                return false;
            }  // at end - do not save empty strings
            this.value[count] = input;          // at end - save value
            return true;
        }
        if (newValue == "") {
            return false;
        }   // do not save empty strings
        this.value = newValue;
        return true;
    }-*/;

	public native final JsArrayString getValueAsJsArray() /*-{
        return this.value;
    }-*/;

	public native final void setValueAsJsArray(JsArrayString arr) /*-{
        this.value = arr;
    }-*/;

	public native final JavaScriptObject getValueAsJso() /*-{
        return this.value;
    }-*/;

	public native final void setValueAsJso(JavaScriptObject valueAsJso) /*-{
        this.value = valueAsJso;
    }-*/;

	public native final Object getValueAsObject() /*-{
        return this.value;
    }-*/;

	public native final void setValueAsString(String str) /*-{
        this.value = str;
    }-*/;

	public native final void setValueAsNumber(int i) /*-{
        this.value = i;
    }-*/;

	public native final boolean isAttributeValid() /*-{
        return this.attributeValid;
    }-*/;

	public native final void setAttributeValid(boolean valid) /*-{
        this.attributeValid = valid;
    }-*/;

	/**
	 * Returns unique ID for GUI
	 *
	 * @return
	 */
	public native final String getGuiUniqueId() /*-{
        if (typeof $wnd.perunAttributeCounter == "undefined") {
            $wnd.perunAttributeCounter = 0;
        }
        if (typeof this.guiUniqueId == "undefined") {
            this.guiUniqueId = "attr-" + $wnd.perunAttributeCounter;
            $wnd.perunAttributeCounter++;
        }
        return this.guiUniqueId;
    }-*/;

	public final Map<String, JSONValue> getValueAsMap() {
		return JsonUtils.parseJsonToMap(getValueAsJso());
	}

	/**
	 * Returns true, if attribute value is empty
	 *
	 * @return true if empty, false otherwise
	 */
	public final boolean isEmpty() {
		if (getValue() == null) {
			return true;
		}
		switch (getType()) {
			case "java.util.LinkedHashMap":
				return getValueAsMap().isEmpty();
			case "java.lang.Integer":
				return getValue().isEmpty();
			case "java.lang.Boolean":
				return getValue().isEmpty();
			case "java.lang.LargeString":
				return getValue().isEmpty();
			case "java.util.LargeArrayList":
				return getValue().isEmpty();
			default:
				return getValueAsJsArray().length() == 0;
		}
	}

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Attribute o) {
		return o.getId() == this.getId();
	}

}