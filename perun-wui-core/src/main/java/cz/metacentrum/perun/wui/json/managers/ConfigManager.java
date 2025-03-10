package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (ConfigManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ConfigManager {

	private static final String CONFIG_MANAGER = "configManager/";

	/**
	 * Retrieve Perun's configuration for email/name change.
	 *
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getPersonalDataChangeConfig(JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call(CONFIG_MANAGER + "getPersonalDataChangeConfig");

	}

}
