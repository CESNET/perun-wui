package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (ResourcesManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class ResourcesManager {

	private static final String RESOURCES_MANAGER = "resourcesManager/";

	/**
	 * List all rich resources associated with a member's group.
	 *
	 * @param member Member id
	 *
	 * @return Request unique request
	 */
	public static Request getAssignedRichResources(int member, JsonEvents events) {
		JsonClient client = new JsonClient(events);
		if (member > 0) client.put("member", member);

		return client.call(RESOURCES_MANAGER + "getAssignedRichResources");
	}

	/**
	 * List all groups associated with the resource and member.
	 *
	 * @param resource resource id
	 * @param member member id
	 * @param events events to be done
	 * @return Request unique request
	 */
	public static Request getAssignedGroups(int resource, int member, JsonEvents events) {
		JsonClient client = new JsonClient(events);
		if (resource > 0) client.put("resource", resource);
		if (member > 0) client.put("member", member);

		return client.call(RESOURCES_MANAGER + "getAssignedGroups");
	}
}
