package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import java.util.ArrayList;

/**
 * Overlay type for EnrichedIdentity object from Perun consolidator API.
 */
public class EnrichedIdentity extends JavaScriptObject {

	protected EnrichedIdentity() {}

	public final int getId() {
		return JsUtils.getNativePropertyInt(this, "id");
	}

	public final String getName() {
		return JsUtils.getNativePropertyString(this, "name");
	}

	public final String getOrganization() {
		return JsUtils.getNativePropertyString(this, "organization");
	}

	public final String getEmail() {
		return JsUtils.getNativePropertyString(this, "email");
	}

	public final ArrayList<EnrichedExtSource> getIdentities() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyObject(this, "identities"));
	}

}
