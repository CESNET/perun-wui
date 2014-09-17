package cz.metacentrum.perun.wui.json;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.model.PerunException;

/**
 * Client class performing POST requests to Perun's API.
 * For each call, new instance must be created.
 *
 * @author Vaclav Mach <374430@mail.muni.cz>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class JsonPostClient {

	private static final String CALLBACK_NAME = "callbackPost";

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
	private JSONObject json;

	/**
	 * New JsonPostClient
	 */
	public JsonPostClient() {
	}

	/**
	 * New JsonPostClient
	 *
	 * @param events events, which handles retrieved response
	 */
	public JsonPostClient(JsonEvents events) {
		this.events = events;
	}

	/**
	 * Sends JSON data to specific URL. If json is null or empty, no request is made.
	 *
	 * @param url URL to make call to
	 * @param json JSON data to post
	 */
	public void sendData(String url, JSONObject json) {

		this.requestUrl = URL.encode(urlPrefix + url + "?callback=" + CALLBACK_NAME);
		this.json = json;

		if (json != null && json.isObject() != null) {
			send();
		}

	}

	/**
	 * Sends JSON data to specific URL. Data must be passed to JsonPostClient before.
	 * If data object is null or empty, no request is sent.
	 *
	 * @param url URL to make call to
	 */
	public void sendData(String url) {

		this.requestUrl = URL.encode(urlPrefix + url + "?callback=" + CALLBACK_NAME);

		if (json != null && json.isObject() != null) {
			send();
		}

	}

	/**
	 * Puts data into object to be send to Perun's API in a request.
	 *
	 * @param key key (parameter name)
	 * @param data data to send (parameter value)
	 */
	public void put(String key, JSONValue data) {
		if (json == null) json = new JSONObject();
		json.put(key, data);
	}

	/**
	 * Sends JSON data to specific URL
	 */
	private void send() {

		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, requestUrl);

		try {

			events.onLoadingStart();

			builder.sendRequest(json.toString(), new RequestCallback() {
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
								error.setPostData(json.toString());
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
		} catch (RequestException exc) {
			// usually couldn't connect to server
			handleErrors(parseResponse(exc.toString()));
		}

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
