package cz.metacentrum.perun.wui.client.resources.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public enum Locale {
	CS,
	EN;

	private static Map<String, Locale> values;

	static {
		values = new HashMap<>();
		for (Locale locale : Locale.values()) {
			values.put(locale.toString().toLowerCase(), locale);
		}
	}

	public static Locale fromString(String string) {
		return values.get(string.trim().toLowerCase());
	}
}
