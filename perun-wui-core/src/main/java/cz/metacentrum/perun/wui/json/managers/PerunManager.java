package cz.metacentrum.perun.wui.json.managers;

import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (calls without any manager).
 * <p/>
 * Each callback returns unique ID used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunManager {

	/**
	 * Get GUIs configuration defined by Perun instance.
	 *
	 * @param events events done on callback
	 * @return int unique ID of callback
	 */
	public static int getGuiConfiguration(JsonEvents events) {

		JsonClient client = new JsonClient(120000);
		return client.getData("utils/getGuiConfiguration", events);

	}

	/**
	 * Logout from Perun. Destroys session on server side.
	 *
	 * In order to logout locally in browser, Utils.clearFederationCookies() must be called.
	 * If using Kerberos authz, user must close browser in order to logout.
	 *
	 * @param events events done on callback
	 * @return int unique ID of callback
	 */
	public static int logout(JsonEvents events) {

		JsonClient client = new JsonClient();
		return client.getData("utils/logout", events);

	}

	/**
	 * Get list of all user's requests, which are still being processed by server.
	 *
	 * @param events events done on callback
	 * @return int unique ID of callback
	 */
	public static int getPendingRequests(JsonEvents events) {

		JsonClient client = new JsonClient();
		return client.getData("getPendingRequests", events);

	}

}