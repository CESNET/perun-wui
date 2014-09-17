package cz.metacentrum.perun.wui.json;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.model.PerunException;

/**
 * Interface for anonymous classes, which are used as event handlers for
 * callbacks to Perun's API. Each callback can have events processed when
 * callback starts and when finishes (ok or error).
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface JsonEvents {

	/**
	 * Called when callback finishes with success.
	 *
	 * @param jso retrieved data from server
	 */
	void onFinished(JavaScriptObject jso);

	/**
	 * Called, when callback finishes in error (or timeout).
	 * On timeout, PerunError is null.
	 *
	 * @param error PerunError object retrieved from server
	 */
	void onError(PerunException error);

	/**
	 * Called, when callback starts.
	 */
	void onLoadingStart();

}