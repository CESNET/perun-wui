package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (calls without any manager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunManager {

	/**
	 * Get GUIs configuration defined by Perun instance.
	 *
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getGuiConfiguration(JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call("utils/getGuiConfiguration");

	}

	/**
	 * Logout from Perun. Destroys session on server side.
	 *
	 * In order to logout locally in browser, Utils.clearFederationCookies() must be called.
	 * If using Kerberos authz, user must close browser in order to logout.
	 *
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request logout(JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call("utils/logout");

	}

	/**
	 * Get list of all user's requests, which are still being processed by server.
	 *
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getPendingRequests(JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call("getPendingRequests");

	}

}