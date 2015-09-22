package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (ExtSourcesManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Kristyna Kysela
 */
public class ExtSourcesManager {

	private static final String EXT_SOURCES_MANAGER = "extSourcesManager/";

	/**
	 * Returns the list of all external sources.
	 *
	 * @param events  events done on callback
	 * @return Request unique request
	 */
	public static Request getExtSources(JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call(EXT_SOURCES_MANAGER + "getExtSources");

	}

	/**
	 * Loads ext source definitions from the configuration file and updates entries stored in the DB.
	 *
	 * @param events  events done on callback
	 * @return Request unique request
	 */
	public static Request loadExtSourcesDefinitions(JsonEvents events) {

		JsonClient client = new JsonClient(events);
		return client.call(EXT_SOURCES_MANAGER + "loadExtSourcesDefinitions");

	}

}
