package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * Overlay type for EnrichedExtSource object from Perun consolidator API.
 */
public class EnrichedExtSource extends JavaScriptObject {

	protected EnrichedExtSource() {}

	public final ExtSource getExtSource() {
		return JsUtils.getNativePropertyObject(this, "extSource").cast();
	}

	public final String getAttribute(String name) {
		JavaScriptObject attributes = JsUtils.getNativePropertyObject(this, "attributes");
		if (attributes == null) return null;
		return JsUtils.getNativePropertyString(attributes, name);
	}

}
