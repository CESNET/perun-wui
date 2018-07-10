package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

import java.util.List;

/**
 * Manager with standard callbacks to Perun's API (MembersManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class MembersManager {

	private static final String MEMBERS_MANAGER = "membersManager/";

	/**
	 * Get list of all Members of a user
	 *
	 * @param userId ID of user to get members for
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getMembersByUser(int userId, JsonEvents events){
		JsonClient client = new JsonClient(events);
		if (userId > 0) client.put("user", userId);
		return client.call(MEMBERS_MANAGER + "getMembersByUser");
	}

	/**
	 * Get Member of specific User in a specific Vo
	 *
	 * @param userId ID of User to get member from
	 * @param voId ID of Vo to get member from
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getMemberByUser(int userId, int voId, JsonEvents events){

		JsonClient client = new JsonClient(events);
		if (userId > 0) client.put("user", userId);
		if (voId > 0) client.put("vo", voId);
		return client.call(MEMBERS_MANAGER + "getMemberByUser");

	}

	/**
	 * Returns a RichMember with all non-empty user/member attributes by it's member id.
	 *
	 * @param memberId ID of member
	 * @param events Events done on callback
	 *
	 * @return Requset unique request
	 */
	public static Request getRichMemberWithAttributes(int memberId, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (memberId > 0) client.put("id", memberId);
		return client.call(MEMBERS_MANAGER + "getRichMemberWithAttributes");
	}

	/**
	 * Get complete Rich members from group and their attributes
	 * @param groupId id of group
	 * @param attrsNames names of attributes to be fetched
	 * @param lookingInParentGroup look into parent group
	 * @param events Events done on callback
	 * @return Request unique request
	 */
	public static Request getCompleteRichMembers(int groupId, List<String> attrsNames,
												 boolean lookingInParentGroup, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (groupId > 0) client.put("group", groupId);
		if (! attrsNames.isEmpty()) client.put("attrsNames", attrsNames);
		client.put("lookingInParentGroup", lookingInParentGroup);
		return client.call(MEMBERS_MANAGER + "getCompleteRichMembers");
	}

	/**
	 * Get complete Rich members from vo and their attributes
	 * @param voId id of vo
	 * @param attrsNames names of attributes to be fetched
	 * @param events Events done on callback
	 * @return Request unique request
	 */
	public static Request getCompleteRichMembers(int voId, List<String> attrsNames, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (voId > 0) client.put("vo", voId);
		if (! attrsNames.isEmpty()) client.put("attrsNames", attrsNames);
		return client.call(MEMBERS_MANAGER + "getCompleteRichMembers");
	}
}
