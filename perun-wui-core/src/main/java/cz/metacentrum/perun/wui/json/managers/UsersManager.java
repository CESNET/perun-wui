package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
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
	 * Get all RichUsers with specified attributes
	 *
	 * @param attrNames list of attributes name
	 * @param includedSpecificUsers if you want to or don't want to get specificUsers too
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getRichUsersWithAttributes(ArrayList<String> attrNames, boolean includedSpecificUsers, JsonEvents events){

		JsonClient client = new JsonClient(events);
		client.put("attrsNames", attrNames);
		client.put("includedSpecificUsers", includedSpecificUsers);
		return client.call(USERS_MANAGER + "getRichUsersWithAttributes");

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
	 * Returns user by its id.
	 *
	 * @param userId Users ID
	 * @param events Events done on callback
	 * @return Request unique request
	 */
	public static Request getUserById(int userId, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (userId > 0) client.put("id", userId);
		return client.call(USERS_MANAGER + "getUserById");
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
	 * @param lang Language to get confirmation mail in
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request requestPreferredEmailChange(int userId, String email, String lang, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (userId > 0) client.put("user", userId);
		client.put("email", email);
		client.put("lang", lang);
		client.put("linkPath","/"+PerunSession.getInstance().getRpcServer()+"/profile/");
		return client.call(USERS_MANAGER + "requestPreferredEmailChange");

	}

	/**
	 * Request to change preferred email address of user. Validation mail is sent on new address.
	 * Change is not saved until user validate new email address by calling validatePreferredEmailChange()
	 * method with proper set of parameters (sent in validation mail).
	 *
	 * @param userId Users ID
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
	 * Validates change of preferred email in Perun
	 *
	 * @param userId Users ID
	 * @param i Param "i" retrieved from URL
	 * @param m Param "m" retrieved from URL
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request validatePreferredEmailChange(int userId, String i, String m, JsonEvents events){

		JsonClient client = new JsonClient(events);
		if (userId > 0) client.put("u", userId);
		if (i != null && !i.isEmpty()) client.put("i", i);
		if (m != null && !m.isEmpty()) client.put("m", m);
		return client.call(USERS_MANAGER + "validatePreferredEmailChange");

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
	 * @param lang Language to get notification in
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request resetNonAuthzPassword(String i, String m, String newPass, String lang, JsonEvents events){

		JsonClient client = new JsonClient(events);
		if (i != null && !i.isEmpty()) client.put("i", i);
		if (m != null && !m.isEmpty()) client.put("m", m);
		if (newPass!= null && !newPass.isEmpty()) client.put("password", newPass);
		if (lang!= null && !lang.isEmpty()) client.put("lang", lang);
		return client.call(USERS_MANAGER + "changeNonAuthzPassword");

	}

	/**
	 * Returns all groups of specific user in given vo where the given user is admin.
	 *
	 * @param user user to be used.
	 * @param vo vo to be used.
	 * @param events events done on callback.
	 * @return Request unique request.
	 */
	public static Request getGroupsWhereUserIsAdmin(int user, int vo, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (user > 0) client.put("user", user);
		if (vo > 0) client.put("vo", vo);
		return client.call(USERS_MANAGER + "getGroupsWhereUserIsAdmin");
	}

	/**
	 * Returns all groups of specific user where the given user is admin.
	 *
	 * @param user user to be used.
	 * @param events events done on callback.
	 * @return Request unique request.
	 */
	public static Request getGroupsWhereUserIsAdmin(int user, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (user > 0) client.put("user", user);
		return client.call(USERS_MANAGER + "getGroupsWhereUserIsAdmin");
	}


	/**
	 * Returns all vos of specific user where the given user is admin.
	 *
	 * @param user user to be used.
	 * @param events events done on callback.
	 * @return Request unique request.
	 */
	public static Request getVosWhereUserIsAdmin(int user, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (user > 0) client.put("user", user);
		return client.call(USERS_MANAGER + "getVosWhereUserIsAdmin");
	}

	/**
	 * Creates alernative password for specified user.
	 *
	 * @param user password will be set for this user
	 * @param description description of the password
	 * @param loginNamespace
	 * @param password password itself
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request createAltPassword(int user, String description, String loginNamespace, String password, JsonEvents events){
		JsonClient client = new JsonClient(events);
		if (user > 0) client.put("user", user);
		if (description != null && !description.isEmpty()) client.put("description", description);
		if (loginNamespace != null && !loginNamespace.isEmpty()) client.put("loginNamespace", loginNamespace);
		if (password != null && !password.isEmpty()) client.put("password", password);
		return client.call(USERS_MANAGER + "createAlternativePassword");
	}

	/**
	 * Deletes alernative password for specified user.
	 *
	 * @param user password will be set for this user
	 * @param loginNamespace
	 * @param passwordId Id of the password to be deleted
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request deleteAltPassword(int user, String loginNamespace, String passwordId, JsonEvents events){
		JsonClient client = new JsonClient(events);
		if (user > 0) client.put("user", user);
		if (loginNamespace != null && !loginNamespace.isEmpty()) client.put("loginNamespace", loginNamespace);
		if (passwordId != null && !passwordId.isEmpty()) client.put("passwordId", passwordId);
		return client.call(USERS_MANAGER + "deleteAlternativePassword");
	}
}
