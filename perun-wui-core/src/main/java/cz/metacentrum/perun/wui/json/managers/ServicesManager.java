package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.model.beans.Service;

/**
 * Manager with standard callbacks to Perun's API (ServicesManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Kristyna Kysela
 *
 */
public class ServicesManager {

	private static final String SERVICES_MANAGER = "servicesManager/";


	/**
	 * Returns all services.
	 *
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getServices(JsonEvents events){

		JsonClient client = new JsonClient(events);
		return client.call(SERVICES_MANAGER + "getServices");
	}

	/**
	 * Deletes a service.
	 *
	 * @param service service
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request deleteService(Service service, JsonEvents events){

		JsonClient client = new JsonClient(events);
		client.put("service", service.getId());
		return client.call(SERVICES_MANAGER + "deleteService");
	}

	/**
	 * Returns a service by its id.
	 *
	 * @param id services id
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request getServiceById(int id, JsonEvents events){

		JsonClient client = new JsonClient(events);
		client.put("id", id);
		return client.call(SERVICES_MANAGER + "getServiceById");
	}

}
