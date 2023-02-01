package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * Overlay type for Application from Perun-Registrar.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Application extends JavaScriptObject {

	/**
	 * Define range of application types
	 */
	public enum ApplicationType {
		INITIAL,
		EXTENSION,
		EMBEDDED
	}

	/**
	 * Define range of application states
	 */
	public enum ApplicationState {
		NEW,
		VERIFIED,
		APPROVED,
		REJECTED
	}

	protected Application() {}

	static public String translateState(String state) {
		Application app = new JSONObject().getJavaScriptObject().cast();

		app.setState(state);
		return app.getTranslatedState();
	}

	/**
	 * Creates new application object to submit it to the Perun.
	 *
	 * @param vo Vo this application belongs to
	 * @param group Group this application belongs to
	 * @param type type of application
	 * @param fedInfo backup info from federation
	 * @param actor login of user submitting the application
	 * @param extSourceName name of authz backend of user submitting the application
	 * @param extSourceType type of authz backend of user submitting the application
	 * @param extSourceLoa level of assurance of user in external source
	 * @param user User who submitted this application
	 * @return Application object to submit to Perun.
	 */
	static public Application createNew(Vo vo, Group group, ApplicationType type, String fedInfo, String actor, String extSourceName, String extSourceType, int extSourceLoa, User user) {

		Application app = new JSONObject().getJavaScriptObject().cast();

		// reconstruct vo to be safe when loading it by attrs in registrar
		Vo newVo = new JSONObject().getJavaScriptObject().cast();
		newVo.setId(vo.getId());
		newVo.setName(vo.getName());
		newVo.setShortName(vo.getShortName());
		app.setVo(newVo);
		app.setGroup(group);
		// set rest
		app.setType(type);
		app.setState(ApplicationState.NEW);
		app.setFederationInfo(fedInfo);
		app.setCreatedBy(actor);
		app.setExtSourceName(extSourceName);
		app.setExtSourceType(extSourceType);
		app.setExtSourceLoa(extSourceLoa);
		app.setObjectType("Application");
		app.setUser(user);
		app.setAutoApproveError(null);
		return app;
	}

	/**
	 * Get ID of application
	 *
	 * @return ID of application
	 */
	public final int getId() {
		return JsUtils.getNativePropertyInt(this, "id");
	}

	/**
	 * Set ID of application
	 *
	 * @param id ID of application to set
	 */
	public final native void setId(int id) /*-{
		this.id = id;
	}-*/;

	/**
	 * Get VO this application belongs to.
	 *
	 * @return VO this application belongs to.
	 */
	public final Vo getVo() {
		if (JsUtils.getNativePropertyObject(this, "vo") != null) {
			return JsUtils.getNativePropertyObject(this, "vo").cast();
		}
		return null;
	}

	/**
	 * Set VO this application belongs to.
	 *
	 * @param vo VO this application belongs to.
	 */
	public final native void setVo(Vo vo) /*-{
		this.vo = vo;
	}-*/;

	/**
	 * Get group this application belongs to.
	 *
	 * @return Group this application belongs to.
	 */
	public final Group getGroup() {
		if (JsUtils.getNativePropertyObject(this, "group") != null) {
			return JsUtils.getNativePropertyObject(this, "group").cast();
		}
		return null;
	}

	/**
	 * Set group this application belongs to.
	 *
	 * @param group Group this application belongs to.
	 */
	public final native void setGroup(Group group) /*-{
		this.group = group;
	}-*/;

	/**
	 * Get type of application.
	 *
	 * @return Type of this application.
	 */
	public final ApplicationType getType(){
		return ApplicationType.valueOf(JsUtils.getNativePropertyString(this, "type"));
	}

	/**
	 * Set type of application.
	 *
	 * @param type Type of this application.
	 */
	public final void setType(ApplicationType type){
		setType(String.valueOf(type));
	}

	private final native void setType(String type) /*-{
		this.type = type;
	}-*/;

	/**
	 * Get state of application.
	 *
	 * @return State of this application.
	 */
	public final ApplicationState getState() {
		return ApplicationState.valueOf(JsUtils.getNativePropertyString(this, "state"));
	}

	/**
	 * Set state of application.
	 *
	 * @param state State of this application.
	 */
	public final void setState(ApplicationState state){
		setState(String.valueOf(state));
	}

	private final native void setState(String state) /*-{
		this.state = state;
	}-*/;

	/**
	 * Get base info from Federation attributes, if provided by IDP.
	 * Data are stored asi string in JSON format.
	 *
	 * @return Data from federation, if provided by IDP.
	 */
	public final String getFederationInfo() {
		return JsUtils.getNativePropertyString(this, "fedInfo");
	}

	/**
	 * Set base information from Federation attributes, if provided by IDP.
	 *
	 * @param fedInfo Data from federation as String in JSON format.
	 */
	public final native void setFederationInfo(String fedInfo) /*-{
		this.fedInfo = fedInfo;
	}-*/;

	/**
	 * Get error that occurred during the automatic approval of the application.
	 *
	 * @return error
	 */
	public final String getAutoApproveError() {
		return JsUtils.getNativePropertyString(this, "autoApproveError");
	}

	/**
	 * Set error that occurred during the automatic approval of the application.
	 *
	 * @param autoApproveError error
	 */
	public final native void setAutoApproveError(String autoApproveError) /*-{
		this.autoApproveError = autoApproveError;
	}-*/;

	/**
	 * Get name of used external authz system.
	 *
	 * @return Name of used external authz system.
	 */
	public final String getExtSourceName() {
		return JsUtils.getNativePropertyString(this, "extSourceName");
	}

	/**
	 * Set name of used external authz system.
	 *
	 * @param extSourceName Name of used external authz system.
	 */
	public final native void setExtSourceName(String extSourceName) /*-{
		this.extSourceName = extSourceName;
	}-*/;

	/**
	 * Get type of used external authz system.
	 *
	 * @return Type of used external authz system.
	 */
	public final String getExtSourceType() {
		return JsUtils.getNativePropertyString(this, "extSourceType");
	}

	/**
	 * Set type of used external authz system.
	 *
	 * @param extSourceType Type of used external authz system.
	 */
	public final native void setExtSourceType(String extSourceType) /*-{
		this.extSourceType = extSourceType;
	}-*/;

	/**
	 * Get user's LoA in used external authz system.
	 *
	 * @return LoA in used external authz system.
	 */
	public final int getExtSourceLoa() {
		return JsUtils.getNativePropertyInt(this, "extSourceLoa");
	}

	/**
	 * Set user's LoA in used external authz system.
	 *
	 * @param loa LoA in used external authz system.
	 */
	public final native void setExtSourceLoa(int loa) /*-{
		this.extSourceLoa = loa;
	}-*/;

	/**
	 * Get user who submitted this application. If null, user is not yet in Perun and
	 * data are available in params:
	 *
	 * @see #getCreatedBy()
	 * @see #getExtSourceName()
	 * @see #getExtSourceType()
	 * @see #getExtSourceLoa()
	 *
	 * @return User who submitted this application.
	 */
	public final User getUser() {
		return JsUtils.getNativePropertyObject(this, "user").cast();
	}

	/**
	 * Set user who submitted this application.
	 *
	 * @param user User to associate with application.
	 */
	public final native void setUser(User user) /*-{
		this.user = user;
	}-*/;

	/**
	 * Get login/unique id of user from external system.
	 *
	 * @return Login/unique id of user who submitted application.
	 */
	public final String getCreatedBy() {
		return JsUtils.getNativePropertyString(this, "createdBy");
	}

	/**
	 * Set login/unique id of user from external system.
	 *
	 * @param created Login/unique id of user who submitted application.
	 */
	public final native void setCreatedBy(String created) /*-{
		this.createdBy = created;
	}-*/;

	/**
	 * Get login/unique id of user from external system who modified application the last time.
	 *
	 * @return Login/unique id of user who modified application the last time.
	 */
	public final String getModifiedBy() {
		return JsUtils.getNativePropertyString(this, "modifiedBy");
	}

	/**
	 * Set login/unique id of user from external system.
	 *
	 * @param modified Login/unique id of user who modified application the last time.
	 */
	public final native void setModifiedBy(String modified) /*-{
		this.modifiedBy = modified;
	}-*/;

	/**
	 * Get timestamp of application creation as String.
	 *
	 * @return Timestamp of application creation.
	 */
	public final String getCreatedAt() {
		return JsUtils.getNativePropertyString(this, "createdAt");
	}

	/**
	 * Set timestamp of application creation as String.
	 *
	 * @param date Timestamp of application creation.
	 */
	public final native void setCreatedAt(String date) /*-{
		this.createdAt = date;
	}-*/;

	/**
	 * Get timestamp of application last change as String.
	 *
	 * @return Timestamp of application last change.
	 */
	public final String getModifiedAt() {
		return JsUtils.getNativePropertyString(this, "modifiedAt");
	}

	/**
	 * Set timestamp of application last change as String.
	 *
	 * @param date Timestamp of application last change.
	 */
	public final native void setModifiedAt(String date) /*-{
		this.modifiedAt = date;
	}-*/;

	public final String getTranslatedState() {

		PerunTranslation translation = GWT.create(PerunTranslation.class);

		if (ApplicationState.NEW.equals(getState())) {
			return translation.applicationNew();
		} else if (ApplicationState.VERIFIED.equals(getState())) {
			return translation.applicationVerified();
		} else if (ApplicationState.APPROVED.equals(getState())) {
			return translation.applicationApproved();
		} else if (ApplicationState.REJECTED.equals(getState())) {
			return translation.applicationRejected();
		}

		return getState().toString();

	}

	public final String getTranslatedType() {

		PerunTranslation translation = GWT.create(PerunTranslation.class);

		if (ApplicationType.INITIAL.equals(getType())) {
			return translation.applicationInitial();
		} else if (ApplicationType.EXTENSION.equals(getType())) {
			return translation.applicationExtension();
		} else if (ApplicationType.EMBEDDED.equals(getType())) {
			return translation.applicationEmbedded();
		}

		return getType().toString();

	}

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
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Application o) {
		return o.getId() == this.getId();
	}

}
