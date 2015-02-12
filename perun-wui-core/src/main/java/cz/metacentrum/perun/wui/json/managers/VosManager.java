package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.json.*;
import cz.metacentrum.perun.wui.model.beans.Vo;

/**
 * Manager with standard callbacks to Perun's API (VosManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
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
	 * @return Request unique request
	 */
	public static Request getVos(boolean listAll, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (listAll) {
			return client.call(VOS_MANAGER + "getAllVos");
		} else {
			return client.call(VOS_MANAGER + "getVos");
		}

	}

	/**
	 * Get VO by its ID
	 *
	 * @param id     ID of VO to get
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getVoById(int id, JsonEvents events) {

		if (id <= 0) return null;

		JsonClient client = new JsonClient();
		client.put("id", id);
		return client.call(VOS_MANAGER + "getVoById");

	}

	/**
	 * Create new VO in Perun. Creator is automatically set as manager of VO.
	 * Vo object with new ID set is returned by server to onFinished() method.
	 *
	 * @param vo Vo to create with all necessary properties set.
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request createVo(Vo vo, JsonEvents events) {

		if (vo == null) return null;

		JsonClient client = new JsonClient(true, events);
		client.put("vo", vo);
		return client.call(VOS_MANAGER + "createVo");

	}

	/**
	 * Delete VO from Perun. VO must not contain any members, groups (excluding 'members')
	 * or resources. You can delete such VO by using 'force == TRUE'
	 *
	 * @param vo VO to delete
	 * @param force TRUE = force delete / FALSE = standard delete (default)
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request deleteVo(Vo vo, boolean force, JsonEvents events) {

		if (vo == null) return null;

		JsonClient client = new JsonClient(true, events);
		client.put("vo", vo.getId());
		client.put("force", force ? 1 : 0);
		return client.call(VOS_MANAGER + "deleteVo");

	}

	/**
	 * Update VO definition in Perun. VO to update is decided by it's ID.
	 * Only VO's name can be updated.
	 *
	 * @param vo VO to update
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request updateVo(Vo vo, JsonEvents events) {

		if (vo == null) return null;

		JsonClient client = new JsonClient(true, events);
		client.put("vo", vo);
		return client.call(VOS_MANAGER + "updateVo");

	}

	/**
	 * Search all VO's external sources of new members by searchString (which can't be empty).
	 * Candidates, who are already members of VO are not returned by server.
	 *
	 * @param vo VO to search external sources for
	 * @param searchString string to search by
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request findCandidates(Vo vo, String searchString, JsonEvents events) {

		if (vo == null || searchString == null || searchString.trim().isEmpty()) return null;

		JsonClient client = new JsonClient(events);
		client.put("vo", vo.getId());
		client.put("searchString", searchString);
		return client.call(VOS_MANAGER + "findCandidates");

	}

}