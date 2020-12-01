package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager with standard callbacks to Perun's API (RegistrarManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class RegistrarManager {

	private static final String REGISTRAR_MANAGER = "registrarManager/";

	/**
	 * Retrieve list of VO attributes by its name. If group is not null or empty retrieve also group attributes.
	 * Attributes can be distinguished by its entity field. "vo" or "group".
	 *
	 * @param voName Name of VO to get data for
	 * @param groupName Full name of Group to get data for (optional)
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getVoAndGroupAttributes(String voName, String groupName, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		client.put("vo", voName);
		if (groupName != null && !groupName.isEmpty()) client.put("group", groupName);

		return client.call(REGISTRAR_MANAGER + "initialize");

	}

	/**
	 * Retrieve init data for Registrar GUI in one big call.
	 * Retrieves all possible forms, objects and relevant exceptions
	 * if thrown in order to determine actual users state in registration process.
	 *
	 * @param voName Name of VO to get data for
	 * @param groupName Full name of Group to get data for (optional)
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request initializeRegistrar(String voName, String groupName, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		client.put("vo", voName);
		if (groupName != null && !groupName.isEmpty()) client.put("group", groupName);

		return client.call(REGISTRAR_MANAGER + "initializeRegistrar");

	}

	/**
	 * Retrieve applications for User.
	 *
	 * @param userId ID of user to get applications for or 0 if user unknown (search by authorization)
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getApplicationsForUser(int userId, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (userId > 0) {
			client.put("id", userId);
		}
		return client.call(REGISTRAR_MANAGER + "getApplicationsForUser");

	}

	/**
	 * Retrieve applications for Member.
	 *
	 * @param memberId ID of user to get applications for
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getApplicationsForMember(int memberId, JsonEvents events) {
		return getApplicationsForMember(memberId, 0, events);
	}

	/**
	 * Retrieve applications for Member.
	 *
	 * @param memberId ID of Member to get applications for
	 * @param groupId ID of Group to filter retrieved applications by
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getApplicationsForMember(int memberId, int groupId, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (memberId > 0) {
			client.put("member", memberId);
		}
		if (groupId > 0) {
			client.put("group", memberId);
		}
		return client.call(REGISTRAR_MANAGER + "getApplicationsForUser");

	}

	/**
	 * Retrieve applications for VO.
	 *
	 * @param voId ID of VO to get applications for
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getApplicationsForVo(int voId, JsonEvents events) {
		return getApplicationsForVo(voId, null, events);
	}

	/**
	 * Retrieve applications for VO.
	 *
	 * @param voId ID of VO to get applications for
	 * @param states List of states we want applications to filter
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getApplicationsForVo(int voId, ArrayList<Application.ApplicationState> states, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (voId > 0) {
			client.put("vo", voId);
		}
		if (states != null && !states.isEmpty()) {
			client.put("state", states);
		}
		return client.call(REGISTRAR_MANAGER + "getApplicationsForVo");

	}

	/**
	 * Retrieve applications for Group.
	 *
	 * @param groupId ID of Group to get applications for
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getApplicationsForGroup(int groupId, JsonEvents events) {
		return getApplicationsForGroup(groupId, null, events);
	}

	/**
	 * Retrieve applications for Group.
	 *
	 * @param groupId ID of Group to get applications for
	 * @param states List of states we want applications to filter
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getApplicationsForGroup(int groupId, ArrayList<Application.ApplicationState> states, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (groupId > 0) {
			client.put("group", groupId);
		}
		if (states != null && !states.isEmpty()) {
			client.put("state", states);
		}
		return client.call(REGISTRAR_MANAGER + "getApplicationsForGroup");

	}

	/**
	 * Get application by it's ID.
	 *
	 * @param applicationId ID of application to get
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getApplicationById(int applicationId, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (applicationId > 0) {
			client.put("id", applicationId);
		}
		return client.call(REGISTRAR_MANAGER + "getApplicationById");

	}

	/**
	 * Get application data by application ID.
	 *
	 * @param applicationId ID of application to get data for
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getApplicationDataById(int applicationId, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (applicationId > 0) {
			client.put("id", applicationId);
		}
		return client.call(REGISTRAR_MANAGER + "getApplicationDataById");

	}

	/**
	 * Get application data by application ID.
	 *
	 * @param application Application to create
	 * @param data Data from application form items to save
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request createApplication(Application application, List<ApplicationFormItemData> data, JsonEvents events) {

		JsonClient client = new JsonClient(true, events);
		if (application != null) {
			client.put("app", application);
		}
		if (data != null) {
			client.put("data", data);
		}
		return client.call(REGISTRAR_MANAGER + "submitApplication");

	}

	/**
	 * Get token for identity consolidation. This token saves current session
	 * and once user log-in with different credentials and calls opposite method,
	 * it will try to join both identities.
	 *
	 * @param events Events done on callback
	 * @see #consolidateIdentityUsingToken(String, JsonEvents)
	 *
	 * @return Request unique request
	 */
	public static Request getConsolidatorToken(JsonEvents events) {

		JsonClient client = new JsonClient(true, events);
		return client.call(REGISTRAR_MANAGER + "getConsolidatorToken");

	}

	/**
	 * Joins users current identity to the one represented by the token.
	 *
	 * @param token Token used to identify some identity
	 * @param events Events done on callback
	 * @see #getConsolidatorToken(JsonEvents)
	 *
	 * @return Request unique request
	 */
	public static Request consolidateIdentityUsingToken(String token, JsonEvents events) {

		JsonClient client = new JsonClient(true, events);
		client.put("token", token);
		return client.call(REGISTRAR_MANAGER + "consolidateIdentityUsingToken");

	}

	/**
	 * Verify email address provided on registration form.
	 *
	 * @param i Token used to identify email to verify
	 * @param m Token used to identify email to verify
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request validateEmail(String i, String m, JsonEvents events) {

		JsonClient client = new JsonClient(true, events);
		if (i != null) client.put("i", i);
		if (m != null) client.put("m", m);
		return client.call(REGISTRAR_MANAGER + "validateEmail");

	}

	/**
	 * Verify Google ReCaptcha challenge-response
	 *
	 * @param response Users response to ReCaptcha challenge
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request verifyCaptcha(String response, JsonEvents events) {

		JsonClient client = new JsonClient(true, events);
		if (response != null) client.put("response", response);
		return client.call(REGISTRAR_MANAGER + "verifyCaptcha");

	}

	/**
	 * Send registrar notification message. Type and related
	 *
	 * @param applicationId Id of application send message to
	 * @param mailType Type of mail notification
	 * @param reason Optional reason of action provided by VO/Group manager
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	// FIXME - String MailType to ENUM
	public static Request sendMessage(int applicationId, String mailType, String reason, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (applicationId > 0) client.put("appId", applicationId);
		if (mailType != null) client.put("mailType", mailType);
		if (reason != null) client.put("reason", reason);
		return client.call(REGISTRAR_MANAGER + "sendMessage");

	}

	/**
	 * Check for similar users based on filled application form.
	 *
	 * @param formItems Form items to check similar users for
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request checkForSimilarUsers(List<ApplicationFormItemData> formItems, JsonEvents events) {

		JsonClient client = new JsonClient(true, events);
		if (formItems != null) client.put("formItems", formItems);
		return client.call(REGISTRAR_MANAGER + "checkForSimilarUsers");

	}

	/**
	 * Update form items data of existing submitted application.
	 * This might trigger verification and approval of application!
	 *
	 * @param applicationId ID of application
	 * @param formItems Form items to be updated
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request updateFormItemsData(int applicationId, List<ApplicationFormItemData> formItems, JsonEvents events) {

		JsonClient client = new JsonClient(true, events);
		if (applicationId > 0) client.put("appId", applicationId);
		if (formItems != null) client.put("data", formItems);
		return client.call(REGISTRAR_MANAGER + "updateFormItemsData");

	}

}
