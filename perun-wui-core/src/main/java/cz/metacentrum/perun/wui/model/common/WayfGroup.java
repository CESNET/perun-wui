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
		if (jso != null) return JsUtils.getNativePropertyString(jso, "en");
		return null;
	}

	/**
	 * Get custom lang name of a Wayf Group or NULL if not present
	 *
	 * @param lang Language code to get name in
	 * @return Name of Wayf Group in specified lang
	 */
	public final String getName(String lang) {
		JavaScriptObject jso = JsUtils.getNativePropertyObject(this, "name");
		if (jso != null) return JsUtils.getNativePropertyString(jso, lang);
		return null;
	}

	/**
	 * Get base (English) description of a Wayf Group
	 *
	 * @return Description of Wayf Group
	 */
	public final String getDescription() {
		JavaScriptObject jso = JsUtils.getNativePropertyObject(this, "description");
		if (jso != null) return JsUtils.getNativePropertyString(jso, "en");
		return null;
	}

	/**
	 * Get custom lang description of a Wayf Group or NULL if not present
	 *
	 * @param lang Language code to get name in
	 * @return Description of Wayf Group in specified lang
	 */
	public final String getDescription(String lang) {
		JavaScriptObject jso = JsUtils.getNativePropertyObject(this, "description");
		if (jso != null) return JsUtils.getNativePropertyString(jso, lang);
		return null;
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

	public final String getEFilter() {
		return JsUtils.getNativePropertyString(this, "efilter");
	}

	public final String getIdpEntityID() {
		return JsUtils.getNativePropertyString(this, "idpentityid");
	}

	@Deprecated
	public final String getFeeds() {
		return JsUtils.getNativePropertyString(this, "feed");
	}

	@Deprecated
	public final FeedEntities getFeedData() {
		return JsUtils.getNativePropertyObject(this, "data").cast();
	}

	/**
	 * Get target entity IdP for SP to use (if not configured by SP based on URL)
	 *
	 * @return Target entity ID
	 */
	@Deprecated
	public final String getTargetEntity() {
		return JsUtils.getNativePropertyString(this, "target");
	}

}
