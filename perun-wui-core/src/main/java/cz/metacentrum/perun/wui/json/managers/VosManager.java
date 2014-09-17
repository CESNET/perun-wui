package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.JsonPostClient;
import cz.metacentrum.perun.wui.json.JsonUtils;
import cz.metacentrum.perun.wui.model.beans.Vo;

/**
 * Manager with standard callbacks to Perun's API (VosManager).
 * <p/>
 * Each callback returns unique ID used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class VosManager {

	private static final String VOS_MANAGER = "vosManager/";

	/**
	 * Return list of VO, where user is manager or
	 * all VOs in Perun, if listAll = TRUE.
	 *
	 * @param listAll TRUE = to get all VOs in Perun / FALSE = get Vos where user is manager
	 * @param events  events done on callback
	 * @return int unique ID of callback
	 */
	public static int getVos(boolean listAll, JsonEvents events) {

		JsonClient client = new JsonClient();
		if (listAll) {
			return client.getData(VOS_MANAGER + "getAllVos", events);
		} else {
			return client.getData(VOS_MANAGER + "getVos", events);
		}

	}

	/**
	 * Get VO by its ID
	 *
	 * @param id     ID of VO to get
	 * @param events events done on callback
	 * @return int unique ID of callback
	 */
	public static int getVoById(int id, JsonEvents events) {

		if (id <= 0) return 0;

		JsonClient client = new JsonClient();
		client.put("id", id);
		return client.getData(VOS_MANAGER + "getVoById", events);

	}

	/**
	 * Create new VO in Perun. Creator is automatically set as manager of VO.
	 * Vo object with new ID set is returned by server to onFinished() method.
	 *
	 * @param vo Vo to create with all necessary properties set.
	 * @param events events done on callback
	 */
	public static void createVo(Vo vo, JsonEvents events) {

		if (vo == null) return;

		JsonPostClient client = new JsonPostClient(events);
		client.put("vo", new JSONObject(vo));
		client.sendData(VOS_MANAGER + "createVo");

	}

	/**
	 * Delete VO from Perun. VO must not contain any members, groups (excluding 'members')
	 * or resources. You can delete such VO by using 'force == TRUE'
	 *
	 * @param vo VO to delete
	 * @param force TRUE = force delete / FALSE = standard delete (default)
	 * @param events events done on callback
	 */
	public static void deleteVo(Vo vo, boolean force, JsonEvents events) {

		if (vo == null) return;

		JsonPostClient client = new JsonPostClient(events);
		client.put("vo", new JSONNumber(vo.getId()));
		if (force) client.put("force", null);
		client.sendData(VOS_MANAGER + "deleteVo");

	}

	/**
	 * Update VO definition in Perun. VO to update is decided by it's ID.
	 * Only VO's name can be updated.
	 *
	 * @param vo VO to update
	 * @param events events done on callback
	 */
	public static void updateVo(Vo vo, JsonEvents events) {

		if (vo == null) return;

		JsonPostClient client = new JsonPostClient(events);
		client.put("vo", JsonUtils.convertToJSON(vo));
		client.sendData(VOS_MANAGER + "updateVo");

	}

	/**
	 * Search all VO's external sources of new members by searchString (which can't be empty).
	 * Candidates, who are already members of VO are not returned by server.
	 *
	 * @param vo VO to search external sources for
	 * @param searchString string to search by
	 * @param events events done on callback
	 * @return List of Candidates to be VO members
	 */
	public static int findCandidates(Vo vo, String searchString, JsonEvents events) {

		if (vo == null || searchString == null || searchString.trim().isEmpty()) return 0;

		JsonClient client = new JsonClient(120000);
		client.put("vo", vo.getId());
		client.put("searchString", searchString);
		return client.getData(VOS_MANAGER + "findCandidates", events);

	}

}