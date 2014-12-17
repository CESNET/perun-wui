package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (AuthzResolver).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AuthzManager {

	private static final String AUTHZ_MANAGER = "authzResolver/";

	/**
	 * Retrieve Perun's internal session data about logged user.
	 * This should be the first call any client does in order to properly
	 * handle authz in application.
	 *
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getPerunPrincipal(JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call(AUTHZ_MANAGER + "getPerunPrincipal");

	}

}
