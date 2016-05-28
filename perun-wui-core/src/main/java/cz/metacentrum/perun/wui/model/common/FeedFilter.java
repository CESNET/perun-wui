package cz.metacentrum.perun.wui.model.common;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FeedFilter extends JavaScriptObject {

	protected FeedFilter() {
	}

	public final List<String> getAllowedIdPs() {
		return JsUtils.listFromJsArrayString(JsUtils.getNativePropertyArrayString(this, "allowIdPs"));
	}

	public final boolean isIdPAllowed(String idpID) {

		List<String> allowed = JsUtils.listFromJsArrayString(JsUtils.getNativePropertyArrayString(this, "allowIdPs"));
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

	public final List<String> getAllowedFeeds() {
		return JsUtils.listFromJsArrayString(JsUtils.getNativePropertyArrayString(this, "allowFeeds"));
	}

}
