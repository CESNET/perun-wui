package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (FacilitiesManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link JsonEvents JsonEvents}.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FacilitiesManager {

	private static final String FACILITIES_MANAGER = "facilitiesManager/";

	/**
	 * Return list of Facilities in Perun. If getRich == TRUE, then each facility
	 * also contains list of Owners of 'technical' type.
	 *
	 * @param getRich TRUE = get RichFacilities / FALSE = get Facilities (default)
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getFacilities(boolean getRich, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (getRich) {
			return client.call(FACILITIES_MANAGER + "getRichFacilities");
		} else {
			return client.call(FACILITIES_MANAGER + "getFacilities");
		}

	}

	/**
	 * Return Facility by it's ID.
	 *
	 * @param id ID of facility to get
	 * @param events events done on callback
	 * @return Request unique request
	 */
	public static Request getFacilityById(int id, JsonEvents events) {

		if (id <= 0) return null;

		JsonClient client = new JsonClient(events);
		client.put("id", id);
		return client.call(FACILITIES_MANAGER + "getFacilityById");

	}

}