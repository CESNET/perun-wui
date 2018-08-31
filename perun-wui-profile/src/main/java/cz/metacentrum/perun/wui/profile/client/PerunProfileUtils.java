package cz.metacentrum.perun.wui.profile.client;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PerunProfileUtils {

	public static final String A_U_DEF_SSH_KEYS = "urn:perun:user:attribute-def:def:sshPublicKey";
	public static final String A_U_DEF_ADMIN_SSH_KEYS = "urn:perun:user:attribute-def:def:sshPublicAdminKey";
	public static final String A_U_DEF_USER_PREFERRED_UNIX_GROUP_NAME_NAMESPACE = "urn:perun:user:attribute-def:def:preferredUnixGroupName-namespace";

	public static final String A_D_R_FILES_LIMIT_NAME = "filesLimit";
	public static final String A_D_R_DATA_LIMIT_NAME = "dataLimit";
	public static final String A_D_R_DEFAULT_FILES_LIMIT_NAME = "defaultFilesLimit";
	public static final String A_D_R_DEFAULT_DATA_LIMIT_NAME = "defaultDataLimit";
	public static final String A_D_R_USER_SETTINGS_NAME_NAME = "userSettingsName";
	public static final String A_D_R_USER_SETTINGS_DESCRIPTION_NAME = "userSettingsDescription";

	public static final List<String> SSH_KEY_PREFIXES = Arrays.asList(
			"ssh-rsa",
			"ssh-ed25519",
			"ecdsa-sha2-nistp256",
			"ecdsa-sha2-nistp384",
			"ecdsa-sha2-nistp521"
	);
	public static final List<String> SHELL_OPTIONS = Arrays.asList(
			"/bin/bash",
			"/bin/csh",
			"/bin/ksh",
			"/bin/sh",
			"/bin/zsh"
	);

	private static final String SHELL_REGEX = "^(/[-_a-zA-Z0-9]+)+$";

	/**
	 * Returns user's id
	 *
	 * @return user's id or null when error
	 */
	public static Integer getUserId(PlaceManager placeManager) {
		try {
			String userId = placeManager.getCurrentPlaceRequest().getParameter("id", null);
			if (userId == null) {
				userId = String.valueOf(PerunSession.getInstance().getUserId());
			}

			final int id = Integer.valueOf(userId);

			if (id < 1) {
				placeManager.revealErrorPlace(placeManager.getCurrentPlaceRequest().getNameToken());
			}

			return id;
		} catch (NumberFormatException e) {
			placeManager.revealErrorPlace(placeManager.getCurrentPlaceRequest().getNameToken());
		}
		return null;
	}

	/**
	 * Checks if given value is valid ssh key
	 *
	 * @param value value to check
	 * @return true if given value is correct ssh key, false otherwise
	 */
	public static boolean isValidSshKey(String value) {
		for (String prefix : PerunProfileUtils.SSH_KEY_PREFIXES) {
			if (value.startsWith(prefix)) {
				return true;
			}
		}

		return false;
	}


	/**
	 * Checks if given value is valid shell
	 *
	 * @param value value to check
	 * @return true if given value is correct shell, false otherwise
	 */
	public static boolean isShellValid(String value) {

		return value.matches(SHELL_REGEX);
	}

	/**
	 * Returns names of preferredGroupNames attributes for allowed namespaces
	 * @return list of attribute names
	 */
	public static List<String> getPreferredGroupNamesAttrNames() {
		List<String> attrNames = new ArrayList<>();
		List<String> allowedNamespaces = PerunConfiguration.getPreferredUnixGroupNamesNamespaces();

		for (String namespace : allowedNamespaces) {
			attrNames.add(A_U_DEF_USER_PREFERRED_UNIX_GROUP_NAME_NAMESPACE + ":" + namespace);
		}

		return attrNames;
	}
}
