package cz.metacentrum.perun.wui.registrar.model;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.Identity;
import cz.metacentrum.perun.wui.model.beans.Vo;

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
		if (JsUtils.getNativePropertyArray(this, "voFormInitial") != null) {
			return JsUtils.jsoAsList(JsUtils.getNativePropertyObject(this, "voFormInitial"));
		}
		return null;
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
		if (JsUtils.getNativePropertyArray(this, "voFormExtension") != null) {
			return JsUtils.jsoAsList(JsUtils.getNativePropertyObject(this, "voFormExtension"));
		}
		return null;
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
		if (JsUtils.getNativePropertyArray(this, "groupFormInitial") != null) {
			return JsUtils.jsoAsList(JsUtils.getNativePropertyObject(this, "groupFormInitial"));
		}
		return null;
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
		if (JsUtils.getNativePropertyArray(this, "similarUsers") != null) {
			return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "similarUsers"));
		}
		return null;
	}

}