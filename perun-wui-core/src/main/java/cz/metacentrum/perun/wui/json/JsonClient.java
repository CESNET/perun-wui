package cz.metacentrum.perun.wui.json;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.URL;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PerunWebConstants;
import cz.metacentrum.perun.wui.model.PerunException;

import java.util.*;

/**
 * Client class performing GET requests to Perun's API.
 * For each call, new instance must be created.
 *
 * @author Vaclav Mach <374430@mail.muni.cz>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class JsonClient {

	// Request number counter
	private static int jsonRequestId = 0;

	// map with active requests info
	// KEY = requestId, VALUE = JsonClientRequest
	static private Map<Integer, JsonRequest> activeRequestsMap = new HashMap<Integer, JsonRequest>();

	// event to handle result of callback
	private JsonEvents events = new JsonEvents() {
		@Override
		public void onFinished(JavaScriptObject jso) {
		}

		@Override
		public void onError(PerunException error) {
		}

		@Override
		public void onLoadingStart() {
		}
	};

	// base URL of Perun's API
	private String urlPrefix = PerunSession.getInstance().getRpcUrl();
	// timeout
	int timeout = 30000;
	// storage for parameters to send to Perun's API
	Map<String, Object> parameters = new HashMap<>();

	/**
	 * Create client to make GET call with custom timeout.
	 *
	 * @param timeout time in milliseconds to wait for server response
	 */
	public JsonClient(int timeout) {
		this.timeout = (timeout <= 0) ? PerunWebConstants.INSTANCE.jsonTimeout() : timeout;
	}

	/**
	 * Default instance of the client.
	 */
	public JsonClient() {
		this(PerunWebConstants.INSTANCE.jsonTimeout());
	}

	/**
	 * Put param=value pair into request params
	 *
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value) {
		parameters.put(key, value);
	}

	/**
	 * Makes a call to Perun's API with specific URL.
	 * URL parameters (if any), must be passed to JsonClient by put() method before making a call.
	 * Passed events are used to handle response.
	 *
	 * @param url    URL specifying manager/method to call
	 * @param events events which handles callback response
	 * @return int unique ID of callback
	 */
	public int getData(String url, JsonEvents events) {
		return this.getData(url, parameters, events);
	}

	/**
	 * Makes a call to Perun's API with specific URL and specific parameters (passed as map object).
	 * Passed events are used to handle response.
	 *
	 * @param url    URL specifying manager/method to call
	 * @param parameters URL parameters used in call
	 * @param events events which handles callback response
	 * @return int unique ID of callback
	 */
	public int getData(String url, Map<String, Object> parameters, JsonEvents events) {

		String parametersString = "";
		if (parameters != null && !parameters.isEmpty()) {
			for (String key : parameters.keySet()) {
				// if value is list
				if (parameters.get(key) instanceof List) {
					for (Object value : (List<Object>)parameters.get(key)) {
						// pass value as part of list
						parametersString += "&"+key+"[]=" + value.toString();
					}
				} else {
					parametersString += "&"+key+"=" + parameters.get(key).toString();
				}
			}
		}
		return getData(url, parametersString, events);

	}

	/**
	 * Makes a call to Perun's API with specific URL and parameters.
	 * Passed events are used to handle response.
	 *
	 * @param url        URL specifying manager/method to call
	 * @param parameters string representation of URL parameters to send with call (is expected to start with '&')
	 * @param events     events which handles callback response
	 * @return int unique ID of callback
	 */
	public int getData(String url, String parameters, JsonEvents events) {

		if (events != null) this.events = events;

		this.events.onLoadingStart();

		// encode URL to allow passing special characters as param values
		String rpcUrl = URL.encode(urlPrefix + url);

		// process parameters and check, if correctly starts with '&'
		if (parameters != null && !parameters.isEmpty()) {
			if (parameters.startsWith("?")) {
				// replace ? for &
				parameters.replaceFirst("\\?", "&");
			}
			if (!parameters.startsWith("&")) {
				// add missing &
				parameters += "&"+parameters;
			}
			parameters = URL.encode(parameters);
		}

		// new request ID
		jsonRequestId++;
		get(jsonRequestId, rpcUrl, parameters, this, timeout);

		return jsonRequestId;

	}

	/**
	 * Makes a native call to the remote server (Appends script to the DOM).
	 *
	 * @param requestId Request unique identification
	 * @param url       URL specifying manager/method to call
	 * @param params    string representation of URL parameters to send with call (is expected to start with '&')
	 * @param handler   instance of this class (JsonClient)
	 * @param timeout   time in milliseconds to wait for server response
	 */
	private native static void get(int requestId, String url, String params, JsonClient handler, int timeout) /*-{

        var callback = "callback" + requestId;

        // [2] Define the callback function on the window object.
        window[callback] = function (jso) {

            // if not already done - expired?
            if (window[callback + "done"]) {
                return;
            }
            // [3] If response is not JSON object, try to create BasicOverlayType from it.
            try {
                if ((typeof jso) != "object") {
                    jso = {value: jso};
                }
            } catch (err) {

            }
            window[callback + "done"] = true;
            setTimeout(function () {
                handler.@cz.metacentrum.perun.wui.json.JsonClient::handleResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(requestId, jso);
            }, 1500);

        };

        // [1] Create a script element.
        var script = document.createElement("script");
        script.setAttribute("src", url + "?callback=" + callback + params);
        script.setAttribute("type", "text/javascript");

        // [4] JSON download has a timeout.
        setTimeout(
            function () {
                if (!window[callback + "done"]) {
                    handler.@cz.metacentrum.perun.wui.json.JsonClient::handleResponse(ILcom/google/gwt/core/client/JavaScriptObject;)(requestId, null);
                }
                // [5] Cleanup. Remove script and callback elements.
                document.body.removeChild(script);
                delete window[callback];
                delete window[callback + "done"];
            }, timeout);

        // [6] Attach the script element to the document body.
        document.body.appendChild(script);

    }-*/;

	/**
	 * Handle the response from server. Based on result either onFinished()
	 * or onError() is called.
	 *
	 * @param requestId ID of the called request
	 * @param jso       JavaScriptObject to be processed. If null, the error is called.
	 */
	private void handleResponse(int requestId, final JavaScriptObject jso) {

		activeRequestsMap.remove(requestId);

		if (jso == null) {
			// timeout
			events.onError(null);
		} else {

			PerunException error = (PerunException) jso.cast();
			if (error.getErrorId() != null && !error.getErrorId().isEmpty()) {
				// retrieved data is error object
				events.onError(error);
			} else {
				// retrieved data is correct
				events.onFinished(jso);
			}

		}

	}

	/**
	 * Removes all running requests
	 */
	public static void removeRunningRequests() {

		Set<Integer> requestsToRemove = new HashSet<Integer>();

		for (Map.Entry<Integer, JsonRequest> entry : activeRequestsMap.entrySet()) {
			JsonRequest request = entry.getValue();
			requestsToRemove.add(request.getId());
		}

		for (int request : requestsToRemove) {
			removeRunningRequest(request);
		}

	}

	/**
	 * Removes one running request
	 *
	 * @param requestId ID of request to remove
	 */
	public static boolean removeRunningRequest(int requestId) {

		// if the request doesn't exist, return false
		if (!activeRequestsMap.containsKey(requestId)) {
			return false;
		}

		// remove request
		removeRunningRequestNative(requestId);
		activeRequestsMap.remove(requestId);
		return true;
	}

	/**
	 * Removes the request from DOM.
	 *
	 * @param requestId ID of request to remove
	 */
	private static native void removeRunningRequestNative(int requestId) /*-{
        var callback = "callback" + requestId;
        window[callback + "done"] = true;
    }-*/;

}