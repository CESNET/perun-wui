package cz.metacentrum.perun.wui.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;

import java.util.ArrayList;

/**
 * Overlay type for PerunException object from Perun.
 *
 * @author Vaclav Mach <374430@mail.muni.cz>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunException extends JavaScriptObject {

	protected PerunException() {
	}

	/**
	 * Return new instance of PerunException with basic properties set.
	 *
	 * @param errorId String representation of error ID
	 * @param name    name of exception should be equal to getClass().getSimpleName() called above Java exception object.
	 * @param message exception message
	 * @return PerunException object
	 */
	public static final PerunException createNew(String errorId, String name, String message) {
		PerunException exception = new JSONObject().getJavaScriptObject().cast();
		exception.setErrorId(errorId);
		exception.setName(name);
		exception.setMessage(message);
		return exception;
	}

	/**
	 * Return string representation of ID of an error object.
	 * <p/>
	 * If property is not defined, null is returned. In such case,
	 * underlaying object can't be considered as representation of PerunException.
	 *
	 * @return String representation of ID of an error
	 */
	public final String getErrorId() {
		return JsUtils.getNativePropertyString(this, "errorId");
	}

	/**
	 * Sets String representation of ID of PerunException
	 *
	 * @param id ID to set
	 */
	public final native void setErrorId(String id) /*-{
        this.errorId = id;
    }-*/;

	/**
	 * Gets Exception name (is equal to java's getClass().getSimpleName())
	 *
	 * @return name of exception
	 */
	public final String getName() {
		return JsUtils.getNativePropertyString(this, "name");
	}

	/**
	 * Sets name of PerunException
	 *
	 * @param name name of exception to set
	 */
	public final native void setName(String name) /*-{
        this.name = name;
    }-*/;

	/**
	 * Return PerunException's message.
	 *
	 * @return message
	 */
	public final String getMessage() {
		return JsUtils.getNativePropertyString(this, "message");
	}

	/**
	 * Sets PerunException's message.
	 *
	 * @param message message to set
	 */
	public final native void setMessage(String message) /*-{
        this.message = message;
    }-*/;

	/**
	 * Return TYPE of an PerunException (e.g. for CabinetException).
	 * It's not standard part of all exceptions !!
	 *
	 * @return type of exception or null if not set
	 */
	public final String getType() {
		return JsUtils.getNativePropertyString(this, "type");
	}

	/**
	 * Sets type of PerunException (e.g. for CabinetException).
	 * It's not standard part of all exceptions !!
	 *
	 * @param type
	 */
	public final native void setType(String type) /*-{
        this.type = type;
    }-*/;

	/**
	 * Return reason for "ExtendMembershipException"
	 *
	 * @return reason why user can't extend membership
	 */
	public final String getReason() {
		return JsUtils.getNativePropertyString(this, "reason");
	}

	/**
	 * Return reason for "ExtendMembershipException"
	 *
	 * @return reason why user can't extend membership
	 */
	public final String getExpirationDate() {
		return JsUtils.getNativePropertyString(this, "expirationDate");
	}

	public final ArrayList<ApplicationFormItemData> getFormItems() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "formItems"));
	}

	/**
	 * If callback causing this error was POST type,
	 * this will retrieve data sent with request.
	 * <p/>
	 * If callback was GET, return null.
	 *
	 * @return string representation of posted data
	 */
	public final String getPostData() {
		return JsUtils.getNativePropertyString(this, "postData");
	}

	;

	/**
	 * Set content of original request if it was POST.
	 * <p/>
	 * Should be set only inside JsonPostClient !!
	 *
	 * @param postData String containing posted data (json)
	 */
	public final native void setPostData(String postData) /*-{
        this.postData = postData;
    }-*/;

	/**
	 * Get URL of original request which caused this error.
	 *
	 * @return URL of original request or null
	 */
	public final String getRequestURL() {
		return JsUtils.getNativePropertyString(this, "requestURL");
	}

	/**
	 * Set URL of original request, which caused this error.
	 * <p/>
	 * Should be set only inside JsonPostClient !!
	 *
	 * @param requestURL URL of original request
	 */
	public final native void setRequestURL(String requestURL) /*-{
        this.requestURL = requestURL;
    }-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(PerunException o) {
		return o.getErrorId().equals(this.getErrorId());
	}

}