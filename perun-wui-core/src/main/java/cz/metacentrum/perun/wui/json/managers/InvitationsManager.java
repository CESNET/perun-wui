package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (InvitationsManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Rastislav Kruták <492918@mail.muni.cz>
 */
public class InvitationsManager {

	private static final String INVITATIONS_MANAGER = "invitationsManager/";

	/**
	 * Check whether the invitation given by the UUID token exists and is in a pending state.
	 *
	 * @param invitationUUID UUID of an invitation to check
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request canInvitationBeAccepted(String invitationUUID, int groupId, JsonEvents events) {
		JsonClient client = new JsonClient(events);
		client.put("uuid", invitationUUID);
		client.put("group", groupId);
		return client.call(INVITATIONS_MANAGER + "canInvitationBeAccepted");

	}
}
