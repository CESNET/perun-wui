package cz.metacentrum.perun.wui.json.managers;

import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (RegistrarManager).
 * <p/>
 * Each callback returns unique ID used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class RegistrarManager {

	private static final String REGISTRAR_MANAGER = "registrarManager/";

	/**
	 * Retrieve application form items with values pre-filled from Perun or IdP/certificate.
	 *
	 * What form (initial/extension) is retrieved is based on current user requesting the form
	 * and it's status in VO.
	 *
	 * @param voId ID of VO to get form for
	 * @param locale Required localization of form texts
	 * @param events Events done on callback
	 *
	 * @return int unique ID of callback
	 */
	public static int getFormItemsWithValue(int voId, String locale, JsonEvents events) {
		return getFormItemsWithValue(voId, 0, locale, events);
	}

	/**
	 * Retrieve application form items with values pre-filled from Perun or IdP/certificate.
	 *
	 * What form (initial/extension) is retrieved is based on current user requesting the form.
	 *
	 * @param voId ID of VO to get form for
	 * @param locale Required localization of form texts
	 * @param events Events done on callback
	 *
	 * @return int unique ID of callback
	 */
	public static int getFormItemsWithValue(int voId, int groupId, String locale, JsonEvents events) {

		JsonClient client = new JsonClient(12000);
		client.put("vo", voId);
		if (groupId != 0) client.put("group", groupId);
		client.put("locale", locale);

		// FIXME - switch to new implementation
		client.put("type", "INITIAL");
		return client.getData(REGISTRAR_MANAGER + "getFormItemsWithPrefilledValues", events);
		//return client.getData(REGISTRAR_MANAGER + "getFormItemsWithValue", events);

	}

	/**
	 * Retrieve init data for Registrar GUI.
	 *
	 * @param voName Name of VO to get data for
	 * @param groupName Optional name of Group to get data for
	 * @param events Events done on callback
	 *
	 * @return int unique ID of callback
	 */
	public static int initialize(String voName, String groupName, JsonEvents events) {

		JsonClient client = new JsonClient(12000);
		client.put("vo", voName);
		if (groupName != null && !groupName.isEmpty()) client.put("group", groupName);

		return client.getData(REGISTRAR_MANAGER + "initialize", events);

	}

	/**
	 * Retrieve applications for User.
	 *
	 * @param userId ID of user to get applications for or 0 if user unknown (search by authorization)
	 * @param events Events done on callback
	 *
	 * @return int unique ID of callback
	 */
	public static int getApplicationsForUser(int userId, JsonEvents events) {

		JsonClient client = new JsonClient(12000);
		if (userId > 0) {
			client.put("id", userId);
		}
		return client.getData(REGISTRAR_MANAGER + "getApplicationsForUser", events);

	}

}
