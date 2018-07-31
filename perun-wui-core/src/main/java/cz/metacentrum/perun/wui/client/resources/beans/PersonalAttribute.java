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
	private Map<String, String> localizedDescriptions = new HashMap<>();

	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public void addDescription(String language, String description) {
		this.localizedDescriptions.put(language, description);
	}

	public Map<String, String> getLocalizedDescriptions() {
		return localizedDescriptions;
	}

	public String getLocalizedDescription(String locale) {
		for (String descriptionLocale : localizedDescriptions.keySet()) {
			if (locale.contains(descriptionLocale)) {
				return localizedDescriptions.get(descriptionLocale);
			}
		}

		return "";
	}
}
