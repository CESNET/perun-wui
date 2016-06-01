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
		client.put("attrsNames", attrNames);
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

	/**
	 * Get RichUser by ID with all filled attributes
	 *
	 * @param id Users ID
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getRichUserWithAttributes(int id, JsonEvents events){

		JsonClient client = new JsonClient(events);
		if (id > 0) client.put("user", id);
		return client.call(USERS_MANAGER + "getRichUserWithAttributes");

	}

	/**
	 * Remove UserExtSource from User
	 *
	 * @param userId Users ID
	 * @param userExtSourceId ID of UserExtSource to remove
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request removeUserExtSource(int userId, int userExtSourceId, JsonEvents events){

		JsonClient client = new JsonClient(events);
		if (userId > 0) client.put("user", userId);
		if (userExtSourceId > 0) client.put("userExtSource", userExtSourceId);
		return client.call(USERS_MANAGER + "removeUserExtSource");

	}

	/**
	 * Gets list of all external sources of the user.
	 *
	 * @param userId Users ID
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getUserExtSources(int userId, JsonEvents events){

		JsonClient client = new JsonClient(events);
		if (userId > 0) client.put("user", userId);
		return client.call(USERS_MANAGER + "getUserExtSources");

	}

	/**
	 * Return list of VOs where user is a member (independent of membership status)
	 *
	 * @param userId Users ID
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getVosWhereUserIsMember(int userId, JsonEvents events){

		JsonClient client = new JsonClient(events);
		if (userId > 0) client.put("user", userId);
		return client.call(USERS_MANAGER + "getVosWhereUserIsMember");

	}

	/**
	 * Request to change preferred email address of user. Validation mail is sent on new address.
	 * Change is not saved until user validate new email address by calling validatePreferredEmailChange()
	 * method with proper set of parameters (sent in validation mail).
	 *
	 * @param userId Users ID
	 * @param email  New email address to set
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request requestPreferredEmailChange(int userId, String email, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (userId > 0) client.put("user", userId);
		client.put("email", email);
		return client.call(USERS_MANAGER + "requestPreferredEmailChange");

	}

	/**
	 * Request to change preferred email address of user. Validation mail is sent on new address.
	 * Change is not saved until user validate new email address by calling validatePreferredEmailChange()
	 * method with proper set of parameters (sent in validation mail).
	 *
	 * @param userId Users ID
	 * @param email  New email address to set
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getPendingPreferredEmailChanges(int userId, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (userId > 0) client.put("user", userId);
		return client.call(USERS_MANAGER + "getPendingPreferredEmailChanges");

	}

	/**
	 * Change password in selected namespace
	 *
	 * @param userId Users ID
	 * @param loginNamespace Login namespace in a which user wants to change password
	 * @param oldPass Old password used by the user
	 * @param newPass New password set for the user
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request changePassword(int userId, String loginNamespace, String oldPass, String newPass, JsonEvents events){

		JsonClient client = new JsonClient(events);
		if (userId > 0) client.put("user", userId);
		if (loginNamespace != null && !loginNamespace.isEmpty()) client.put("loginNamespace", loginNamespace);
		if (oldPass != null && !oldPass.isEmpty()) client.put("oldPassword", oldPass);
		if (newPass!= null && !newPass.isEmpty()) client.put("newPassword", newPass);
		client.put("checkOldPassword", 1);
		return client.call(USERS_MANAGER + "changePassword");

	}

	/**
	 * Reset users password in selected namespace
	 *
	 * @param userId Users ID
	 * @param loginNamespace Login namespace in a which user wants to reset password
	 * @param newPass New password set for the user
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request resetPassword(int userId, String loginNamespace, String newPass, JsonEvents events){

		JsonClient client = new JsonClient(events);
		client.put("oldPassword", "");
		client.put("checkOldPassword", 0);

		if (userId > 0) client.put("user", userId);
		if (loginNamespace != null && !loginNamespace.isEmpty()) client.put("loginNamespace", loginNamespace);
		if (newPass!= null && !newPass.isEmpty()) client.put("newPassword", newPass);
		return client.call(USERS_MANAGER + "changePassword");

	}

	/**
	 * Reset users password in selected namespace by non-authz call using secret token
	 *
	 * @param i Token param i
	 * @param m Token param m
	 * @param newPass New password set for the user
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request resetNonAuthzPassword(String i, String m, String newPass, JsonEvents events){

		JsonClient client = new JsonClient(events);
		if (i != null && !i.isEmpty()) client.put("i", i);
		if (m != null && !m.isEmpty()) client.put("m", m);
		if (newPass!= null && !newPass.isEmpty()) client.put("password", newPass);
		return client.call(USERS_MANAGER + "changeNonAuthzPassword");

	}

}
