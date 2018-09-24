package cz.metacentrum.perun.wui.registrar.model;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.*;

import java.util.ArrayList;

/**
 * Main Registrar object containing all necessary data to resolve
 * user status in VO/Group.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class RegistrarObject extends JavaScriptObject {

	protected RegistrarObject(){}

	/**
	 * Get VO
	 *
	 * @return VO
	 */
	public final Vo getVo() {
		if (JsUtils.getNativePropertyObject(this, "vo") != null) {
			return JsUtils.getNativePropertyObject(this, "vo").cast();
		}
		return null;
	}

	/**
	 * Get VO attributes
	 *
	 * @return VO attributes
	 */
	public final ArrayList<Attribute> getVoAttributes() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "voAttributes"));
	}

	/**
	 * Check if VO has automatic approval of initial application
	 *
	 * @return true if automatic approval is activated
	 */
	public final boolean hasVoFormAutoApproval() {
		return JsUtils.getNativePropertyBoolean(JsUtils.getNativePropertyObject(this, "voForm"), "automaticApproval");
	}

	/**
	 * Check if VO has automatic approval of extension application
	 *
	 * @return true if automatic approval is activated
	 */
	public final boolean hasVoFormAutoApprovalExtension() {
		return JsUtils.getNativePropertyBoolean(JsUtils.getNativePropertyObject(this, "voForm"), "automaticApprovalExtension");
	}

	/**
	 * Check if group has automatic approval of application
	 *
	 * @return true if automatic approval is activated
	 */
	public final boolean hasGroupFormAutoApproval() {
		return JsUtils.getNativePropertyBoolean(JsUtils.getNativePropertyObject(this, "groupForm"), "automaticApproval");
	}

	/**
	 * Check if group has automatic approval of extension application
	 *
	 * @return true if automatic approval is activated
	 */
	public final boolean hasGroupFormAutoApprovalExtension() {
		return JsUtils.getNativePropertyBoolean(JsUtils.getNativePropertyObject(this, "groupForm"), "automaticApprovalExtension");
	}

	/**
	 * Get group
	 *
	 * @return Group
	 */
	public final Group getGroup() {
		if (JsUtils.getNativePropertyObject(this, "group") != null) {
			return JsUtils.getNativePropertyObject(this, "group").cast();
		}
		return null;
	}

	/**
	 * Get VOs initial form
	 *
	 * @return VOs initial form
	 */
	public final ArrayList<ApplicationFormItemData> getVoFormInitial() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "voFormInitial"));
	}

	/**
	 * Get VOs initial form exception if any
	 *
	 * @return VOs initial form exception if any
	 */
	public final PerunException getVoFormInitialException() {
		if (JsUtils.getNativePropertyObject(this, "voFormInitialException") != null) {
			return JsUtils.getNativePropertyObject(this, "voFormInitialException").cast();
		}
		return null;
	}

	/**
	 * Get VOs extension form
	 *
	 * @return VOs extension form
	 */
	public final ArrayList<ApplicationFormItemData> getVoFormExtension() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "voFormExtension"));
	}

	/**
	 * Get VOs extension form exception if any
	 *
	 * @return VOs extension form exception if any
	 */
	public final PerunException getVoFormExtensionException() {
		if (JsUtils.getNativePropertyObject(this, "voFormExtensionException") != null) {
			return JsUtils.getNativePropertyObject(this, "voFormExtensionException").cast();
		}
		return null;
	}

	/**
	 * Get groups initial form
	 *
	 * @return groups initial form
	 */
	public final ArrayList<ApplicationFormItemData> getGroupFormInitial() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "groupFormInitial"));
	}

	/**
	 * Get group initial form exception if any
	 *
	 * @return Group initial form exception if any
	 */
	public final PerunException getGroupFormInitialException() {
		if (JsUtils.getNativePropertyObject(this, "groupFormInitialException") != null) {
			return JsUtils.getNativePropertyObject(this, "groupFormInitialException").cast();
		}
		return null;
	}

	/**
	 * Get groups extension form
	 *
	 * @return groups extension form
	 */
	public final ArrayList<ApplicationFormItemData> getGroupFormExtension() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "groupFormExtension"));
	}

	/**
	 * Get group extension form exception if any
	 *
	 * @return Group extension form exception if any
	 */
	public final PerunException getGroupFormExtensionException() {
		if (JsUtils.getNativePropertyObject(this, "groupFormExtensionException") != null) {
			return JsUtils.getNativePropertyObject(this, "groupFormExtensionException").cast();
		}
		return null;
	}

	/**
	 * Get Exception thrown on data retrieval. If not empty, it's fatal internal
	 * exception and some relevant data are probably missing inside RegistrarObject.
	 *
	 * @return PerunException
	 */
	public final PerunException getException() {
		if (JsUtils.getNativePropertyObject(this, "exception") != null) {
			return JsUtils.getNativePropertyObject(this, "exception").cast();
		}
		return null;
	}

	/**
	 * Return list of similar identities (users) in Perun. It's used to offer
	 * new user joining of his identities.
	 *
	 * @return List of similar identities
	 */
	public final ArrayList<Identity> getSimilarUsers() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "similarUsers"));
	}

}
