package cz.metacentrum.perun.wui.model.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * Represents one group of wayf from local config file
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class WayfGroup extends JavaScriptObject {


	protected WayfGroup() {
	}

	/**
	 * Get base (English) name of a Wayf Group
	 *
	 * @return Name of Wayf Group
	 */
	public final String getName() {
		JavaScriptObject jso = JsUtils.getNativePropertyObject(this, "name");
		return JsUtils.getNativePropertyString(jso, "en");
	}

	/**
	 * Get custom lang name of a Wayf Group or NULL if not present
	 *
	 * @param lang Language code to get name in
	 * @return Name of Wayf Group in specified lang
	 */
	public final String getName(String lang) {
		return JsUtils.getNativePropertyString(JsUtils.getNativePropertyObject(this, "name"), lang);
	}

	/**
	 * Get type of authentication method used for this wayf group
	 *
	 * @return type of authentication used by this group
	 */
	public final String getAuthzType() {
		return JsUtils.getNativePropertyString(this, "type");
	}

	public final String getIconUrl() {
		return JsUtils.getNativePropertyString(this, "icon");
	}

	public final String getUrl() {
		return JsUtils.getNativePropertyString(this, "url");
	}

	public final String getFeeds() {
		return JsUtils.getNativePropertyString(this, "feed");
	}

	public final FeedEntities getFeedData() {
		return JsUtils.getNativePropertyObject(this, "data").cast();
	}

}
