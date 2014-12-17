package cz.metacentrum.perun.wui.model.common;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * Overlay type for PerunRequest object. It holds information about pending HTTP request.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunRequest extends JavaScriptObject {

	protected PerunRequest() { }

	/**
	 * Get unique identification of this callback
	 *
	 * This value is present only when PerunRequest is returned from server.
	 * Value should be equal to timestamp of callback start.
	 *
	 * @return unique identifier
	 */
	public final String getCallbackName() {
		return JsUtils.getNativePropertyString(this, "callbackId");
	}

	/**
	 * Get "manager" part of path of request.
	 *
	 * @return "Manager" part of path of request.
	 */
	public final String getManager() {
		return JsUtils.getNativePropertyString(this, "manager");
	}

	/**
	 * Set "manager" part of path of request.
	 *
	 * @param manager part of path to set.
	 */
	public final native void setManager(String manager) /*-{
		this.manager = manager;
	}-*/;

	/**
	 * Get "method" part of path of request.
	 *
	 * @return "Method" part of path of request.
	 */
	public final String getMethod() {
		return JsUtils.getNativePropertyString(this, "method");
	}

	/**
	 * Set "method" part of path of request.
	 *
	 * @param method part of path to set.
	 */
	public final native void setMethod(String method) /*-{
		this.method = method;
	}-*/;

	/**
	 * Get query string part of request.
	 *
	 * @return Query string part of request.
	 */
	public final String getQueryString() {
		return JsUtils.getNativePropertyString(this, "params");
	}

	/**
	 * Set query string part of request.
	 *
	 * @param params query part of request to set.
	 */
	public final native void setQueryString(String params) /*-{
		this.params = params;
	}-*/;

	/**
	 * Return start time of callback in milliseconds from start of epoch.
	 *
	 * @return Start time in ms.
	 */
	public final double getStartTime() {
		return JsUtils.getNativePropertyDouble(this, "startTime");
	}

	/**
	 * Sets start time to this callback from current time
	 */
	public final native void setStartTime() /*-{
		this.startTime = new Date().getTime();
	}-*/;

	/**
	 * Get end time of request (when processing finished).
	 * If callback is still processing, value equals -1
	 *
	 * @return End time in ms or -1 when not finished.
	 */
	public final double getEndTime() {
		return JsUtils.getNativePropertyDouble(this, "endTime");
	}

	/**
	 * Calculates duration of callback by distraction of start time from current time
	 *
	 * @return duration of callback in milliseconds
	 */
	public final native double getDuration() /*-{
		return new Date().getTime() - this.startTime;
	}-*/;

	/**
	 * Return result of this callback. Initial value is NULL. Result can be considered as "valid" only,
	 * when end time of callback is set by server.
	 *
	 * @return Result of operation (server response) or NULL.
	 */
	public final JavaScriptObject getResult() {
		return JsUtils.getNativePropertyObject(this, "result");
	}

}