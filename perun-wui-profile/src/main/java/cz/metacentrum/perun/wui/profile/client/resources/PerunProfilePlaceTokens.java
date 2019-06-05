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
	public static final String GROUPS = "groups";
	public static final String COMPLETE_INFO = "completeInfo";
	public static final String RESOURCES = "resources";
	public static final String PRIVACY = "privacy";
	public static final String SETTINGS = "settings";
	public static final String SETTINGS_SSH = SETTINGS + "_sshkeys";
	public static final String SETTINGS_SSH_NEWKEY = SETTINGS_SSH + "_newkey";
	public static final String SETTINGS_SSH_NEWADMINKEY = SETTINGS_SSH + "_newadminkey";
	public static final String SETTINGS_PREFERREDSHELLS = SETTINGS + "_preferredshells";
	public static final String SETTINGS_PREFERREDGROUPNAMES = SETTINGS + "_preferredgroupnames";
	public static final String SETTINGS_DATAQUOTAS = SETTINGS + "_dataquotas";
	public static final String SETTINGS_ALTPASSWORDS = SETTINGS + "_altpasswords";
	public static final String SETTINGS_MAILING = SETTINGS + "_mailing";
	public static final String SETTINGS_SAMBA = SETTINGS + "_samba";

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

	public static String getSettingsSshKeys() {
		return SETTINGS_SSH;
	}

	public static String getResources() {
		return RESOURCES;
	}

	public static String getPrivacy() {
		return PRIVACY;
	}

	public static String getSettings() {
		return SETTINGS;
	}

	public static String getSettingsSshNewkey() {
		return SETTINGS_SSH_NEWKEY;
	}

	public static String getSettingsSshNewadminkey() {
		return SETTINGS_SSH_NEWADMINKEY;
	}

	public static String getSettingsPreferredShells() {
		return SETTINGS_PREFERREDSHELLS;
	}

	public static String getSettingsPreferredgroupnames() {
		return SETTINGS_PREFERREDGROUPNAMES;
	}

	public static String getSettingsDataquotas() {
		return SETTINGS_DATAQUOTAS;
	}

	public static String getSettingsAltPasswords() {
		return SETTINGS_ALTPASSWORDS;
	}

	public static String getSettingsMailing() {
		return SETTINGS_MAILING;
	}

	public static String getSettingsSamba() {
		return SETTINGS_SAMBA;
	}

}
