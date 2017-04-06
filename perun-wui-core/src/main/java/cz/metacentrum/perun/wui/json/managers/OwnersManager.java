package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;


/**
 * Manager with standard callbacks to Perun's API (OwnersManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Kristyna Kysela
 */
public class OwnersManager {

	private static final String OWNERS_MANAGER = "ownersManager/";

	/**
	 * Returns all owners.
	 *
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getOwners(JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call(OWNERS_MANAGER + "getOwners");

	}

	/**
	 * Deletes an owner.
	 *
	 * @param owner Id of owner
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request deleteOwner(int owner, JsonEvents events) {

		JsonClient client = new JsonClient(true, events);
		client.put("owner", owner);
		return client.call(OWNERS_MANAGER + "deleteOwner");

	}

	/**
	 * Creates a new owner.
	 *
	 * @param ownerContact Contact of owner
	 * @param ownerType Type of owner
	 * @param ownerName Name of owner
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request createOwner(String ownerName, String ownerContact, String ownerType, JsonEvents events) {


		JSONObject owner = new JSONObject();
		owner.put("id", new JSONNumber(0));
		owner.put("name", new JSONString(ownerName));
		owner.put("type", new JSONString(ownerType));
		owner.put("contact", new JSONString(ownerContact));

		JsonClient client = new JsonClient(true, events);
		client.put("owner", owner.getJavaScriptObject());
		return client.call(OWNERS_MANAGER + "createOwner");

	}
}
