package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.model.beans.Attribute;

import java.util.ArrayList;
import java.util.List;
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

	/**
	 * Returns an Attribute by its name.
	 *
	 * @param uesId UserExtSource id
	 * @param attrName Attribute name
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getUesAttribute(int uesId, String attrName, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (uesId > 0) client.put("userExtSource", uesId);
		if (attrName != null && !attrName.isEmpty()) client.put("attributeName", attrName);
		return client.call(ATTRIBUTES_MANAGER + "getAttribute");
	}

	/**
	 * Returns all non-empty UserExtSource attributes for selected UserExtSource.
	 *
	 * @param uesId UserExtSource id
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getUesAttributes(int uesId, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (uesId > 0) client.put("userExtSource", uesId);
		return client.call(ATTRIBUTES_MANAGER + "getAttributes");
	}

	/**
	 * Returns an Attribute by its name. Returns only non-empty attributes.
	 *
	 * @param vo Vo id
	 * @param attrName Attribute name
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getVoAttribute(int vo, String attrName, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (vo > 0) client.put("vo", vo);
		if (attrName != null && !attrName.isEmpty()) client.put("attributeName", attrName);
		return client.call(ATTRIBUTES_MANAGER + "getAttribute");
	}

	/**
	 * Returns an Attribute by its name. Returns only non-empty attributes.
	 *
	 * @param user user id
	 * @param attrName Attribute name
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getUserAttribute(int user, String attrName, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (user > 0) client.put("user", user);
		if (attrName != null && !attrName.isEmpty()) client.put("attributeName", attrName);
		return client.call(ATTRIBUTES_MANAGER + "getAttribute");
	}

	/**
	 * Sets an attribute.
	 *
	 * @param user user id
	 * @param attribute Attribute
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request setUserAttribute(int user, Attribute attribute, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (user > 0) client.put("user", user);
		if (attribute != null) client.put("attribute", attribute);
		return client.call(ATTRIBUTES_MANAGER + "setAttribute");
	}

	/**
	 * Returns all specified User attributes for selected User.
	 *
	 * @param user user id
	 * @param attrNames List of attribute names
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getUserAttributes(int user, List<String> attrNames, JsonEvents events) {
		JsonClient client = new JsonClient(events);
		if (user > 0) client.put("user", user);
		if (attrNames != null && !attrNames.isEmpty()) client.put("attrNames", attrNames);
		return client.call(ATTRIBUTES_MANAGER + "getAttributes");
	}

	/**
	 * Returns all non-empty Resource attributes for selected Resource.
	 *
	 * @param resource resource id
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getResourceAttributes(int resource, JsonEvents events) {
		JsonClient client = new JsonClient(events);
		if (resource > 0) client.put("resource", resource);
		return client.call(ATTRIBUTES_MANAGER + "getAttributes");
	}

	/**
	 * Returns required resource attributes.
	 *
	 * @param resource resource id
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getRequiredAttributes(int resource, JsonEvents events) {
		JsonClient client = new JsonClient(events);
		if (resource > 0) client.put("resource", resource);
		return client.call(ATTRIBUTES_MANAGER + "getRequiredAttributes");
	}

	/**
	 * Returns required resource attributes.
	 *
	 * @param resource
	 * @param member
	 * @param resourceToGetServicesFrom
	 * @param workWithUserAttributes
	 * @param events
	 * @return
	 */
	public static Request getRequiredAttributes(int resource, int member, int resourceToGetServicesFrom,
												boolean workWithUserAttributes, JsonEvents events) {
		JsonClient client = new JsonClient(events);
		if (resource > 0) client.put("resource", resource);
		if (member > 0) client.put("member", member);
		if (resourceToGetServicesFrom > 0) client.put("resourceToGetServicesFrom", resourceToGetServicesFrom);
		client.put("workWithUserAttributes", workWithUserAttributes ? 1 : 0);
		return client.call(ATTRIBUTES_MANAGER + "getRequiredAttributes");
	}
}
