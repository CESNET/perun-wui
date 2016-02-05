package cz.metacentrum.perun.wui.model.common;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * Overlay type for Feed object which represents category of IdPs
 * including all available IdPs in such category.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Feed extends JavaScriptObject {

	protected Feed() {
	}

	/**
	 * Get ID of IdP feed (string identifier)
	 *
	 * @return ID of IdP feed
	 */
	public final String getId() {
		return JsUtils.getNativePropertyString(this, "id");
	}

	/**
	 * Get Label (display name) of IdP feed
	 *
	 * @return Label (display name) of IdP feed
	 */
	public final String getLabel() {
		return JsUtils.getNativePropertyString(this, "label");
	}

	/**
	 * Get map of available IdPs in this feed
	 *
	 * @return Map of available IdPs in this feed
	 */
	public final FeedEntities getEntities() {
		return JsUtils.getNativePropertyObject(this, "entities").cast();
	}

}
