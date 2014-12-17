package cz.metacentrum.perun.wui.json;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PerunWebConstants;
import cz.metacentrum.perun.wui.json.managers.UtilsManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.PerunRequest;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.shared.event.ModalHiddenEvent;
import org.gwtbootstrap3.client.shared.event.ModalHiddenHandler;
import org.gwtbootstrap3.client.shared.event.ModalShownEvent;
import org.gwtbootstrap3.client.shared.event.ModalShownHandler;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.html.Paragraph;

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

	private Map<String, PerunRequest> runningRequests = new HashMap<>();
	private static Paragraph layout = new Paragraph();
	private static int counter = 0;
	private static Modal modal;
	private static boolean shown = false;

	/**
	 * New JsonClient using GET method
	 */
	public JsonClient() {

		// init modal dialog
		if (modal == null) {

			modal = new Modal();

			modal.setClosable(true);
			modal.addShownHandler(new ModalShownHandler() {
				@Override
				public void onShown(ModalShownEvent modalShownEvent) {
					shown = true;
				}
			});
			modal.addHiddenHandler(new ModalHiddenHandler() {
				@Override
				public void onHidden(ModalHiddenEvent modalHiddenEvent) {
					shown = false;
				}
			});
			ModalHeader header = new ModalHeader();
			header.setTitle("Processing long request");
			modal.add(header);

			ModalBody body = new ModalBody();
			body.add(layout);
			modal.add(body);

			ModalFooter footer = new ModalFooter();
			PerunButton close = new PerunButton("Hide");
			close.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent clickEvent) {
					modal.hide();
				}
			});
			footer.add(close);
			modal.add(footer);

		}

	}

	/**
	 * New JsonClient
	 *
	 * @param method specify request method (GET or POST)
	 */
	public JsonClient(RequestBuilder.Method method) {
		this();
		if (method != null) this.method = method;
	}

	/**
	 * New JsonClient
	 *
	 * @param method specify request method (GET or POST)
	 * @param events events, which handles retrieved response
	 */
	public JsonClient(RequestBuilder.Method method, JsonEvents events) {
		this();
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

		final PerunRequest perunRequest = new JSONObject().getJavaScriptObject().cast();
		perunRequest.setStartTime();
		final String callbackName = perunRequest.getStartTime()+"";

		runningRequests.put(callbackName, perunRequest);

		// build request URL
		this.requestUrl = URL.encode(urlPrefix + url + "?callback=" + callbackName + parametersString);

		RequestBuilder builder = new RequestBuilder(method, requestUrl);

		try {

			events.onLoadingStart();

			final String data = (json != null && json.isObject() != null) ? json.toString() : "";

			Request request = builder.sendRequest(data, new RequestCallback() {
				@Override
				public void onResponseReceived(Request req, Response resp) {

					// make JSO from textual JSON response
					JavaScriptObject jso = parseResponse(callbackName, resp.getText());

					// HTTP status code is OK
					if (resp.getStatusCode() == 200) {

						// check JSO, if not PerunException
						if (jso != null) {

							PerunException error = (PerunException)jso;

							if (error.getErrorId() != null && error.getMessage() != null) {
								error.setRequestURL(requestUrl);
								error.setPostData((json != null) ? json.toString() : "");
								runningRequests.remove(callbackName);
								events.onError(error);
								return;
							}

						}

						// Response is OK (object or null)
						runningRequests.remove(callbackName);
						events.onFinished(jso);

					} else {

						// HTTP status code != OK (200)

						PerunException error = new JSONObject().getJavaScriptObject().cast();
						error.setErrorId("" + resp.getStatusCode());
						error.setName(resp.getStatusText());
						error.setMessage("Server responded with HTTP error: " + resp.getStatusCode() + " - " + resp.getStatusText());
						error.setPostData(data);
						error.setRequestURL(requestUrl);

						if (resp.getStatusCode() == 401 || resp.getStatusCode() == 403) {

							error.setName("Not Authorized");
							error.setMessage("You are not authorized to server. Your session might have expired. Please refresh the browser window to re-login.");

						} else if (resp.getStatusCode() == 500) {

							if (runningRequests.get(callbackName) != null) {

								// 5 minute timeout for POST callbacks
								if ((runningRequests.get(callbackName).getDuration() / (1000)) >= 5 && method.equals(RequestBuilder.POST)) {

									counter++;
									layout.setHTML("Processing of your request(s) is taking longer than usual, but it's actively processed by the server.<p>Please do not close opened window/tab nor repeat your action. You will be notified once operation completes.<p>Remaining requests: " + counter);

									if (!shown && counter > 0) modal.show();

									Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
										boolean again = true;

										@Override
										public boolean execute() {
											if (again) {
												UtilsManager.getPendingRequest(callbackName, new JsonEvents() {
													@Override
													public void onFinished(JavaScriptObject jso) {

														final PerunRequest req = jso.cast();
														if ((req.getCallbackName().equals(perunRequest.getStartTime() + "")) && req.getEndTime() > 0) {

															if (again) {

																again = false;
																counter--;
																layout.setHTML("Processing of your request(s) is taking longer than usual, but it's actively processed by the server.<p>Please do not close opened window/tab nor repeat your action. You will be notified once operation completes.<p>Remaining requests: " + counter);

																// hide notification
																if (shown && counter <= 0) modal.hide();

																JavaScriptObject result = req.getResult();

																// check JSO, if not PerunException
																if (result != null) {

																	PerunException error = (PerunException) result;

																	if (error.getErrorId() != null && error.getMessage() != null) {
																		error.setRequestURL(requestUrl);
																		error.setPostData((json != null) ? json.toString() : "");
																		runningRequests.remove(callbackName);
																		events.onError(error);
																		return;
																	}

																}

																// Response is OK (object or null)
																runningRequests.remove(callbackName);
																events.onFinished(result);

															}
														}
													}

													@Override
													public void onError(PerunException error) {

													}

													@Override
													public void onLoadingStart() {

													}
												});
											}
											return again;
										}
									}, ((PerunWebConstants) GWT.create(PerunWebConstants.class)).pendingRequestsRefreshInterval());

									return;

								} else {

									error.setName("ServerInternalError");
									error.setMessage("Server encounter internal error while processing your request. Please report this error and retry.");

								}

							}

						} else if (resp.getStatusCode() == 503) {

							error.setName("Server Temporarily Unavailable");
							error.setMessage("Server is temporarily unavailable. Please try again later.");

						} else if (resp.getStatusCode() == 404) {

							error.setName("Not found");
							error.setMessage("Server is probably being restarted at the moment. Please try again later.");

						} else if (resp.getStatusCode() == 0) {

							error.setName("Aborted");
							error.setMessage("Can't contact remote server, connection was lost.");

						}

						runningRequests.remove(requestUrl);
						handleErrors(error);

					}

				}

				@Override
				public void onError(Request req, Throwable exc) {
					// request not sent
					handleErrors(parseResponse(callbackName, exc.toString()));
				}
			});

			return request;

		} catch (RequestException exc) {
			// usually couldn't connect to server
			handleErrors(parseResponse(callbackName, exc.toString()));
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
	 * @param callbackName unique name associated with this callback
	 * @param resp server response
	 * @return returned data as JavaScriptObject
	 */
	private JavaScriptObject parseResponse(String callbackName, String resp) {

		if (resp == null || resp.isEmpty()) return null;

		// trims the whitespace
		resp = resp.trim();

		// short comparing
		if ((callbackName + "(null);").equalsIgnoreCase(resp)) {
			return null;
		}

		// if starts with 'callbackPost(' and ends with ')'or ');' == wrapped => must be unwrapped
		RegExp re = RegExp.compile("^" + callbackName + "\\((.*)\\)|\\);$");
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