package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

import java.util.ArrayList;
import java.util.Map;

/**
 * Manager with standard callbacks to Perun's API (AuthzResolver).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AttributesManager {

	private static final String ATTRIBUTES_MANAGER = "attributesManager/";

	/**
	 * Get all attributes definitions without authz info.
	 *
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getAttributesDefinitions(JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call(ATTRIBUTES_MANAGER + "getAttributesDefinition");

	}

	/**
	 * Get all attributes definitions with appropriate right to related entities.
	 * Type and ID of entity is passed as map (params) and only readable and writable attributes
	 * are retrieved.
	 *
	 * This method is used to get attributes "to set" by user.
	 *
	 * @param ids map of entities and their ids to get attributes for
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getAttributesDefinitionsWithRights(Map<String,Integer> ids, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (ids != null && !ids.isEmpty()) {
			for (String key : ids.keySet()) {
				client.put(key, ids.get(key));
			}
		}
		return client.call(ATTRIBUTES_MANAGER + "getAttributesDefinitionWithRights");

	}

	/**
	 * Get all member attributes specified by names
	 *
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getMemberAttributes(ArrayList<String> attrNames, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call(ATTRIBUTES_MANAGER + "getAttributesDefinition");

	}

	/**
	 * Get all user logins (from all namespaces) as attributes
	 *
	 * @param userId ID of User to get logins for
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getLogins(int userId, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (userId > 0) {
			client.put("user", userId);
		}
		return client.call(ATTRIBUTES_MANAGER + "getLogins");

	}

}
