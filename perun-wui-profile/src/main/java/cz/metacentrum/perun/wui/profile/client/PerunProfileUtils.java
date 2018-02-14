package cz.metacentrum.perun.wui.profile.client;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import cz.metacentrum.perun.wui.client.resources.PerunSession;

import java.util.Arrays;
import java.util.List;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PerunProfileUtils {

	public static final String A_U_DEF_SSH_KEYS = "urn:perun:user:attribute-def:def:sshPublicKey";
	public static final String A_U_DEF_ADMIN_SSH_KEYS = "urn:perun:user:attribute-def:def:sshPublicAdminKey";
	public static final List<String> SSH_KEY_PREFIXES = Arrays.asList(
			"ssh-rsa",
			"ssh-ed25519",
			"ecdsa-sha2-nistp256",
			"ecdsa-sha2-nistp384",
			"ecdsa-sha2-nistp521"
	);

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
}
