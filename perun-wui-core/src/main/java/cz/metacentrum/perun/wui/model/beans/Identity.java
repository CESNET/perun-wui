package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

import java.util.ArrayList;

/**
 * OverlayType for Identity object
 *
 * @author Pavel Zlamal <zlamal@cesnet.cz>
 */
public class Identity extends JavaScriptObject {

	protected Identity() {}

	/**
	 * Gets ID of user
	 *
	 * @return ID of user
	 */
	public final int getId() {
		return JsUtils.getNativePropertyInt(this, "id");
	}

	/**
	 * Gets name of user
	 *
	 * @return name of user
	 */
	public final String getName() {
		return JsUtils.getNativePropertyString(this, "name");
	}

	/**
	 * Gets users organization
	 *
	 * @return users organization
	 */
	public final String getOrganization() {
		return JsUtils.getNativePropertyString(this, "organization");
	}

	/**
	 * Get obfuscated email address
	 *
	 * @return obfuscated email address
	 */
	public final String getEmail() {
		return JsUtils.getNativePropertyString(this, "email");
	}

	/**
	 * Get list of users external identities
	 *
	 * @return external identities
	 */
	public final ArrayList<ExtSource> getExternalIdentities() {

		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this,"identities"));

	}

	/**
	 * Returns Perun specific type of object
	 *
	 * @return type of object
	 */
	public final String getObjectType() {
		return JsUtils.getNativePropertyString(this, "beanName");
	}

	/**
	 * Compares to another object
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Identity o) {
		return o.getId() == this.getId();
	}

}