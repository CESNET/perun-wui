package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.json.client.JSONNumber;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (GroupsManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class GroupsManager {

	private static final String GROUPS_MANAGER = "groupsManager/";

	/**
	 * Add member into Group. Member and group must be from the same VO.
	 *
	 * @param groupId ID of group to add member into
	 * @param memberId ID of member to add to group
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request addMember(int groupId, int memberId, JsonEvents events) {

		JsonClient client = new JsonClient(true, events);
		client.put("group", groupId);
		client.put("member", memberId);
		return client.call(GROUPS_MANAGER + "addMember");

	}

	/**
	 * Remove member from Group. Member and group must be from the same VO.
	 * Only members with DIRECT membership in group can be removed.
	 *
	 * @param groupId ID of group to remove member from
	 * @param memberId ID of member to be removed from group
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request removeMember(int groupId, int memberId, JsonEvents events) {

		JsonClient client = new JsonClient(true, events);
		client.put("group", groupId);
		client.put("member", memberId);
		return client.call(GROUPS_MANAGER + "removeMember");

	}

	/**
	 * Returns all groups of specific member including group "members".
	 *
	 * @param memberId id of member
	 * @param events Events done on callback
	 * @return Request unique request
	 */
	public static Request getAllMemberGroups(int memberId, JsonEvents events) {

		JsonClient client = new JsonClient(true, events);
		client.put("member", memberId);
		return client.call(GROUPS_MANAGER + "getAllMemberGroups");
	}

	/**
	 * Returns all groups of specific member.
	 *
	 * @param memberId id of member.
	 * @param events Events done on callback
	 * @return Request unique request
	 */
	public static Request getMemberGroups(int memberId, JsonEvents events) {

		JsonClient client = new JsonClient(true, events);
		client.put("member", memberId);
		return client.call(GROUPS_MANAGER + "getMemberGroups");
	}
}