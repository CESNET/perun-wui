package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (utils).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class UtilsManager {

	private static final String UTILS_MANAGER = "utils/";

	private static final String RT_MESSAGES_MANAGER = "rtMessagesManager/";

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
		return client.call(UTILS_MANAGER+"logout");

	}

	/**
	 * Get GUIs configuration defined by Perun instance.
	 *
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getGuiConfiguration(JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call(UTILS_MANAGER+"getGuiConfiguration");

	}

	/**
	 * Get list of pending requests, which are currently processed by the server side of Perun.
	 *
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getPendingRequests(JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call("getPendingRequests");

	}

	/**
	 * Get pending request by it's unique name. Response is NULL if no such request is present.
	 *
	 * @param callbackName unique name of callback
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getPendingRequest(String callbackName, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		client.put("callbackId", callbackName);
		return client.call("getPendingRequests");

	}

	/**
	 * Get list of string with status information about Perun instance, like Java version, DB driver and type etc.
	 *
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getPerunStatus(JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call(UTILS_MANAGER+"getPerunStatus");

	}

	/**
	 * Send message to RT (request tracking system) of CESNET.
	 *
	 * @param subject Subject of RT report message
	 * @param message Body of RT report message
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request sendMessageToRT(String subject, String message, JsonEvents events) {

		// appended space after each new line ("\n" to "\n ")
		// required by RT system to be parsed as multi-line text
		message = message.replace("\n", "\n ");

		JsonClient client = new JsonClient(events);
		client.put("queue", "perun");
		client.put("subject", subject);
		client.put("text", message);
		return client.call(RT_MESSAGES_MANAGER+"sentMessageToRT");

	}

}