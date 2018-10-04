package cz.metacentrum.perun.wui.client.resources.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing attribute on Personal page
 *
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public class PersonalAttribute {

	private String urn;
	private Map<Locale, String> localizedDescriptions = new HashMap<>();
	private Map<Locale, String> localizedNames = new HashMap<>();

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public void addDescription(Locale locale, String description) {
		this.localizedDescriptions.put(locale, description);
	}

	public Map<Locale, String> getLocalizedDescriptions() {
		return localizedDescriptions;
	}

	public void addName(Locale locale, String description) {
		this.localizedNames.put(locale, description);
	}

	public String getLocalizedName(Locale locale) {
		for (Locale nameLocale : localizedNames.keySet()) {
			if (locale == nameLocale) {
				return localizedNames.get(nameLocale);
			}
		}

		return null;
	}

	public String getLocalizedDescription(Locale locale) {
		for (Locale descriptionLocale : localizedDescriptions.keySet()) {
			if (locale == descriptionLocale) {
				return localizedDescriptions.get(descriptionLocale);
			}
		}

		return null;
	}
}
