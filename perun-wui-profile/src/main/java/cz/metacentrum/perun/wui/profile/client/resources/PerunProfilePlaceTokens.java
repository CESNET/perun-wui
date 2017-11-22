package cz.metacentrum.perun.wui.profile.client.resources;

import cz.metacentrum.perun.wui.client.resources.PlaceTokens;

/**
 * Place tokens used in Perun WUI Profile app.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunProfilePlaceTokens extends PlaceTokens {

	// General Pages
	public static final String PERSONAL = "personal";
	public static final String IDENTITIES = "identities";
	public static final String ORGANIZATIONS = "organizations";
	public static final String LOGINS = "logins";
	public static final String SETTINGS = "settings";
	public static final String GROUPS = "groups";
	public static final String COMPLETE_INFO = "completeInfo";

	public static String getCompleteInfo() {
		return COMPLETE_INFO;
	}

	public static String getPersonal() {
		return PERSONAL;
	}

	public static String getIdentities() {
		return IDENTITIES;
	}

	public static String getOrganizations() {
		return ORGANIZATIONS;
	}

	public static String getGroups() {
		return GROUPS;
	}

	public static String getLogins() {
		return LOGINS;
	}

	public static String getSettings() {
		return SETTINGS;
	}

}
