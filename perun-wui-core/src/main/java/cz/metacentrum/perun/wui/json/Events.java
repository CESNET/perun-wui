package cz.metacentrum.perun.wui.json;

import cz.metacentrum.perun.wui.model.PerunException;

/**
 * Interface for anonymous classes, which are used as event handlers for
 * callbacks. Each callback can have events processed when
 * callback starts and when finishes (ok or error).
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface Events<T extends Object> {

	/**
	 * Called when callback finishes with success.
	 *
	 * @param result retrieved data
	 */
	void onFinished(T result);

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
