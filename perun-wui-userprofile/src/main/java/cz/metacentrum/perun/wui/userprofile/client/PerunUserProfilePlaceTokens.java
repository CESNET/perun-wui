package cz.metacentrum.perun.wui.userprofile.client;

import cz.metacentrum.perun.wui.client.resources.PlaceTokens;

/**
 * Place tokens used in Perun WUI Registrar app.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunUserProfilePlaceTokens extends PlaceTokens {

	// General Pages
	public static final String PERSONAL = "personal";
	public static final String IDENTITIES = "identities";


	public static String getPersonal() {
		return PERSONAL;
	}

	public static String getIdentities() {
		return IDENTITIES;
	}

}
