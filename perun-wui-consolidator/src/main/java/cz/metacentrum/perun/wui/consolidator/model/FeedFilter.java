package cz.metacentrum.perun.wui.consolidator.model;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

import java.util.ArrayList;

/**
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FeedFilter extends JavaScriptObject {

	protected FeedFilter() {
	}

	public final ArrayList<String> getAllowedIdPs() {
		return JsUtils.listFromJsArrayString(JsUtils.getNativePropertyArrayString(this, "allowIdPs"));
	}

	public final boolean isIdPAllowed(String idpID) {

		ArrayList<String> allowed = JsUtils.listFromJsArrayString(JsUtils.getNativePropertyArrayString(this, "allowIdPs"));
		if (allowed == null || allowed.isEmpty()) {
			return true;
		}

		for (String idp : allowed) {
			if (idp.startsWith(idpID)) {
				return true;
			}
		}

		return false;

	}

	public final ArrayList<String> getAllowedFeeds() {
		return JsUtils.listFromJsArrayString(JsUtils.getNativePropertyArrayString(this, "allowFeeds"));
	}

}
