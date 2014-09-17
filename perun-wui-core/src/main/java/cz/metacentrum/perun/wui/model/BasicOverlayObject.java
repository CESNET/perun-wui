package cz.metacentrum.perun.wui.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayBoolean;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayString;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

import java.util.ArrayList;

/**
 * Object definition for primitive types and lists of them
 * used to wrap non-JSON server responses.
 * <p/>
 * Can return int, String, boolean, float from itself.
 *
 * @author Vaclav Mach  <374430@mail.muni.cz>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class BasicOverlayObject extends JavaScriptObject {

	protected BasicOverlayObject() {
	}

	/**
	 * Return Sting value representation of itself.
	 *
	 * @return string value
	 */
	public final String getString() {
		return JsUtils.getNativePropertyString(this, "value");
	}

	/**
	 * Return boolean value representation of itself.
	 *
	 * @return boolean value
	 */
	public final boolean getBoolean() {
		return JsUtils.getNativePropertyBoolean(this, "value");
	}

	/**
	 * Return int value representation of itself.
	 *
	 * @return int value
	 */
	public final int getInt() {
		return JsUtils.getNativePropertyInt(this, "value");
	}

	/**
	 * Return float value representation of itself.
	 *
	 * @return float value
	 */
	public final float getFloat() {
		return JsUtils.getNativePropertyFloat(this, "value");
	}

	/**
	 * Return Sting value representation of itself as list.
	 *
	 * @return list of string values
	 */
	public final ArrayList<String> getListOfStrings() {

		JsArrayString array = this.cast();
		ArrayList<String> list = new ArrayList<String>();
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				list.add(array.get(i));
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * Return int value representation of itself as list.
	 *
	 * @return list of int values
	 */
	public final ArrayList<Integer> getListOfIntegers() {

		JsArrayInteger array = this.cast();
		ArrayList<Integer> list = new ArrayList<Integer>();
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				list.add(array.get(i));
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * Return boolean value representation of itself as list.
	 *
	 * @return list of boolean values
	 */
	public final ArrayList<Boolean> getListOfBooleans() {

		JsArrayBoolean array = this.cast();
		ArrayList<Boolean> list = new ArrayList<Boolean>();
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				list.add(array.get(i));
			}
			return list;
		} else {
			return null;
		}
	}

}