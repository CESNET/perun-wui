package cz.metacentrum.perun.wui.json;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.model.PerunException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Client class performing GET/POST requests to Perun's API.
 * For each call, new instance must be created.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class JsonClient {

	private static final String CALLBACK_NAME = "callback";

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

	private String urlPrefix = PerunSession.getInstance().getRpcUrl();
	private String requestUrl;
	private JSONObject json = null;
	Map<String, Object> parameters = new HashMap<>();
	private RequestBuilder.Method method = RequestBuilder.GET;

	/**
	 * New JsonClient using GET method
	 */
	public JsonClient() {
	}

	/**
	 * New JsonClient
	 *
	 * @param method specify request method (GET or POST)
	 */
	public JsonClient(RequestBuilder.Method method) {
		if (method != null) this.method = method;
	}

	/**
	 * New JsonClient
	 *
	 * @param method specify request method (GET or POST)
	 * @param events events, which handles retrieved response
	 */
	public JsonClient(RequestBuilder.Method method, JsonEvents events) {
		if (method != null) this.method = method;
		if (events != null) this.events = events;
	}

	/**
	 * New JsonClient2 using GET method
	 * @param events events, which handles retrieved response
	 */
	public JsonClient(JsonEvents events) {
		if (events != null) this.events = events;
	}

	/**
	 * Puts custom parameters into payload or URL (based on call type) in a request.
	 *
	 * @param key key (parameter name)
	 * @param data data to send (parameter value)
	 */
	public void put(String key, Object data) {

		if (method.equals(RequestBuilder.GET)) {

			//GET request
			parameters.put(key, data);

		} else {

			// POST request
			if (json == null) json = new JSONObject();
			if (data instanceof JSONValue) {
				json.put(key, (JSONValue)data);
			}

		}

	}

	/**
	 * Call specific URL with custom events.
	 *
	 * @param url URL to send data to
	 * @return Request unique handling Request
	 */
	public Request call(String url) {

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

		// build request URL
		this.requestUrl = URL.encode(urlPrefix + url + "?callback=" + CALLBACK_NAME + parametersString);

		RequestBuilder builder = new RequestBuilder(method, requestUrl);

		try {

			events.onLoadingStart();

			String data = (json != null && json.isObject() != null) ? json.toString() : "";

			Request request = builder.sendRequest(data, new RequestCallback() {
				@Override
				public void onResponseReceived(Request req, Response resp) {

					// make JSO from textual JSON response
					JavaScriptObject jso = parseResponse(resp.getText());

					// HTTP status code is OK
					if (resp.getStatusCode() == 200) {

						// check JSO, if not PerunException
						if (jso != null) {

							PerunException error = (PerunException)jso;

							if (error.getErrorId() != null && error.getMessage() != null) {
								error.setRequestURL(requestUrl);
								error.setPostData((json != null) ? json.toString() : "");
								events.onError(error);
								return;
							}

						}

						// Response is OK (object or null)
						events.onFinished(jso);

					} else {

						// HTTP status code != OK (200)
						handleErrors(jso);

					}

				}

				@Override
				public void onError(Request req, Throwable exc) {
					// request not sent
					handleErrors(parseResponse(exc.toString()));
				}
			});

			return request;

		} catch (RequestException exc) {
			// usually couldn't connect to server
			handleErrors(parseResponse(exc.toString()));
		}

		// request failed
		return null;

	}

	/**
	 * Handles callback errors before passing them to events handler.
	 *
	 * @param jso retrieved data (error response)
	 */
	private void handleErrors(JavaScriptObject jso) {

		if (jso != null) {
			events.onError((PerunException) jso);
		} else {
			PerunException error = PerunException.createNew("0", "Cross-site request", "Cross-site request was blocked by browser.");
			error.setRequestURL(requestUrl);
			error.setPostData(json.toString());
			events.onError(error);
		}
	}

	/**
	 * Parse server response so it can be evaluated as JSON.
	 * If primitive type is returned, it's wrapped as BasicOverlayObject
	 *
	 * @param resp server response
	 * @return returned data as JavaScriptObject
	 */
	private JavaScriptObject parseResponse(String resp) {

		if (resp == null || resp.isEmpty()) return null;

		// trims the whitespace
		resp = resp.trim();

		// short comparing
		if ((CALLBACK_NAME + "(null);").equalsIgnoreCase(resp)) {
			return null;
		}

		// if starts with 'callbackPost(' and ends with ')'or ');' == wrapped => must be unwrapped
		RegExp re = RegExp.compile("^" + CALLBACK_NAME + "\\((.*)\\)|\\);$");
		MatchResult result = re.exec(resp);
		if (result != null) {
			resp = result.getGroup(1);
		}

		// if response == null => return null
		if (resp.equalsIgnoreCase("null")) {
			return null;
		}

		// normal object
		JavaScriptObject jso = JsonUtils.parseJson(resp);
		return jso;

	}

}
