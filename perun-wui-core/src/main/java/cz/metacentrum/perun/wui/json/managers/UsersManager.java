package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

import java.util.ArrayList;

/**
 * Manager with standard callbacks to Perun's API (UsersManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class UsersManager {

	private static final String USERS_MANAGER = "usersManager/";

	/**
	 * Check if provided login in selected namespace is available.
	 *
	 * @param namespace namespace to check in
	 * @param login login to check for
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request isLoginAvailable(String namespace, String login, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		client.put("login", login);
		client.put("loginNamespace", namespace);
		return client.call(USERS_MANAGER + "isLoginAvailable");

	}

	/**
	 * Search for RichUsers with attributes who matches the searchString by name, login or email.
	 *
	 * When list of attributes names is specified, only such attributes are returned. If null or empty,
	 * all user attributes with non-NULL value are returned.
	 *
	 * @param searchString searched string
	 * @param attrNames list of attributes name
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request findRichUsersWithAttributes(String searchString, ArrayList<String> attrNames, JsonEvents events){

		if(searchString == null || searchString.trim().isEmpty()){
			return null;
		}

		JsonClient client = new JsonClient(events);
		client.put("searchString", searchString);
		client.put("attrNames", attrNames);
		return client.call(USERS_MANAGER + "findRichUsersWithAttributes");
	}

	/**
	 * Search RichUsers without Vo.
	 *
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getRichUsersWithoutVoAssigned(JsonEvents events){

		JsonClient client = new JsonClient(events);
		return client.call(USERS_MANAGER + "getRichUsersWithoutVoAssigned");
	}

}
