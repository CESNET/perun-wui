package cz.metacentrum.perun.wui.model.common;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FeedEntity extends JavaScriptObject {

	protected FeedEntity() {
	}

	/**
	 * Get URL to IdPs logo (institution).
	 *
	 * @return URL of IdP logo
	 */
	public final String getLogoUrl() {
		String url = JsUtils.getNativePropertyString(this, "logo");
		if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
			return PerunConfiguration.getWayfFeedImagesUrl()+"/"+url;
		}
		return url;
	}

	/**
	 * Return label of this IdP in specified language (should be name of institution).
	 * If not present, try to fallback to English name.
	 * If still not present, return null.
	 *
	 * @param lang Language to get label in (e.g. en, cs, ...)
	 * @return Label in specified language
	 */
	public final String getLabel(String lang) {
		String label = JsUtils.getNativePropertyString(JsUtils.getNativePropertyObject(this, "label"), lang);
		if (label == null || label.isEmpty()) {
			label = JsUtils.getNativePropertyString(JsUtils.getNativePropertyObject(this, "label"), "en");
		}
		if (label == null || label.isEmpty()) {
			label = JsUtils.getNativePropertyString(JsUtils.getNativePropertyObject(this, "label"), "fr");
		}
		if (label == null || label.isEmpty()) {
			label = getAnyLabel();
		}
		return label;
	}


	private final native String getAnyLabel() /*-{

		for (var property in this.label) {
			if (this.label.hasOwnProperty(property)) {
				return this.label[property];
			}
		}

	}-*/;

}
