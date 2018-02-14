package cz.metacentrum.perun.wui.json.managers;

import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * Manager with standard callbacks to Perun's API (RTMessagesManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link cz.metacentrum.perun.wui.json.JsonEvents JsonEvents}.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class RTMessagesManager {

	private static final String RTMessagesManager = "rtMessagesManager/";

	/**
	 * Sends a message to RT. VO id is sent.
	 *
	 * @param voId vo id
	 * @param queue queue
	 * @param subject subject
	 * @param text text
	 * @param events events done on Callback
	 * @return Request unique request
	 */
	public static Request sentMessageToRT(int voId, String queue, String subject, String text, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (voId > 0) client.put("voId", voId);
		client.put("queue", queue);
		client.put("subject", subject);
		client.put("text", text);

		return client.call(RTMessagesManager + "sentMessageToRT");
	}
}
