package cz.metacentrum.perun.wui.json.managers;

import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (AuthzResolver).
 * <p/>
 * Each callback returns unique ID used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AuthzManager {

	private static final String AUTHZ_MANAGER = "authzResolver/";

	/**
	 * Retrieve Perun's internal session data about logged user.
	 * This should be the first call any client does in order to properly
	 * handle authz in application.
	 *
	 * @param events events done on callback
	 * @return int unique ID of callback
	 */
	public static int getPerunPrincipal(JsonEvents events) {

		JsonClient client = new JsonClient(12000);
		return client.getData(AUTHZ_MANAGER + "getPerunPrincipal", events);

	}

	/**
	 * Logout user from Perun server (destroy session on Server side)
	 *
	 * @param events events done on callback
	 * @return int unique ID of callback
	 */
	public static int logout(JsonEvents events) {

		JsonClient client = new JsonClient(12000);
		return client.getData("utils/logout", events);

	}

}
