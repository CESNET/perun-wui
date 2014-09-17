package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * Overlay type for ApplicationForm from Perun-Registrar.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ApplicationForm extends JavaScriptObject {

	protected ApplicationForm() {}

	/**
	 * Get ID of application form
	 *
	 * @return ID of application form
	 */
	public final int getId() {
		return JsUtils.getNativePropertyInt(this, "id");
	}

	/**
	 * Set ID of application form
	 *
	 * @param id ID of application form to set
	 */
	public final native void setId(int id) /*-{
		this.id = id;
	}-*/;

	/**
	 * Get VO this application form belongs to.
	 *
	 * @return VO this application form belongs to.
	 */
	public final Vo getVo() {
		return JsUtils.getNativePropertyObject(this, "vo").cast();
	}

	/**
	 * Set VO this application form belongs to.
	 *
	 * @param vo VO this application form belongs to.
	 */
	public final native void setVo(Vo vo) /*-{
		this.vo = vo;
	}-*/;

	/**
	 * Get group this application form belongs to.
	 *
	 * @return Group this application form belongs to.
	 */
	public final Group getGroup() {
		return JsUtils.getNativePropertyObject(this, "group").cast();
	}

	/**
	 * Set group this application form belongs to.
	 *
	 * @param group Group this application form belongs to.
	 */
	public final native void setGroup(Group group) /*-{
		this.group = group;
	}-*/;

	/**
	 * Return TRUE if submitted INITIAL applications of this form are automatically approved. FALSE otherwise.
	 *
	 * @return TRUE = automatic approval / FALSE = manual approval
	 */
	public final boolean isAutomaticApproval() {
		return JsUtils.getNativePropertyBoolean(this, "automaticApproval");
	}

	/**
	 * Set automatic approval for INITIAL applications of this form.
	 *
	 * @param automatic TRUE = automatic / FALSE = manual
	 */
	public final native void setAutomaticApproval(boolean automatic) /*-{
		this.automaticApproval = automatic;
	}-*/;

	/**
	 * Return TRUE if submitted EXTENSION applications of this form are automatically approved. FALSE otherwise.
	 *
	 * @return TRUE = automatic approval / FALSE = manual approval
	 */
	public final boolean isAutomaticApprovalExtension() {
		return JsUtils.getNativePropertyBoolean(this, "automaticApprovalExtension");
	}

	/**
	 * Set automatic approval for EXTENSION applications of this form.
	 *
	 * @param automatic TRUE = automatic / FALSE = manual
	 */
	public final native void setAutomaticApprovalExtension(boolean automatic) /*-{
		this.automaticApprovalExtension = automatic;
	}-*/;

	/**
	 * Get java class name of programmatic module used when processing application.
	 *
	 * @return Java class name of module.
	 */
	public final String getModuleClassName() {
		return JsUtils.getNativePropertyString(this, "moduleClassName");
	}

	/**
	 * Set java class name of programmatic module used when processing application.
	 *
	 * @param name Java class name of module.
	 */
	public final native void setModuleClassName(String name) /*-{
		this.moduleClassName = name;
	}-*/;

	/**
	 * Get object's type, equals to Class.getSimpleName().
	 * Value is stored to object on server side and only for PerunBeans object.
	 * <p/>
	 * If value not present in object, "JavaScriptObject" is returned instead.
	 *
	 * @return type of object
	 */
	public final String getObjectType() {

		if (JsUtils.getNativePropertyString(this, "beanName") == null) return "JavaScriptObject";
		return JsUtils.getNativePropertyString(this, "beanName");

	}

	/**
	 * Set object's type. It should be specific name
	 * of PerunBean equals to Class.getSimpleName().
	 * <p/>
	 * This property is not defined for non-PerunBeans objects in Perun system (server side).
	 *
	 * @param type type of object
	 */
	public final native void setObjectType(String type) /*-{
		this.beanName = type;
	}-*/;

	/**
	 * Compares to another object
	 *
	 * @param o  Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(ApplicationForm o) {
		return o.getId() == this.getId();
	}

}