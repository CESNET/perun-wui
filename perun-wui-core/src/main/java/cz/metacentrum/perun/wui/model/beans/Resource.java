package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for Resource object from Perun
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Resource extends GeneralObject {

	protected Resource() {
	}

	/**
	 * Return new instance of Resource with basic properties set.
	 *
	 * @param id          ID of Resource
	 * @param name        name of Resource
	 * @param description description of Resource
	 * @param facilityId  ID of Facility this Resource is associated with
	 * @param voId        ID of VO this Resource is associated with
	 * @return Resource object
	 */
	public static final Resource createNew(int id, String name, String description, int facilityId, int voId) {
		Resource resource = new JSONObject().getJavaScriptObject().cast();
		resource.setId(id);
		resource.setName(name);
		resource.setDescription(description);
		resource.setVoId(voId);
		resource.setFacilityId(facilityId);
		resource.setObjectType("Resource");
		return resource;
	}

	/**
	 * Sets ID of Resource
	 *
	 * @param id of Resource to set
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Sets name of Resource
	 *
	 * @param name of Resource to set
	 */
	public final native void setName(String name) /*-{
        this.name = name;
    }-*/;

	/**
	 * Sets description of Resource
	 *
	 * @param description of Resource to set
	 */
	public final native void setDescription(String description) /*-{
        this.description = description;
    }-*/;

	/**
	 * Get ID of Facility this Resource is associated with
	 *
	 * @return ID of Facility this Resource is associated with
	 */
	public final int getFacilityId() {
		return JsUtils.getNativePropertyInt(this, "facilityId");
	}

	/**
	 * Sets ID of Facility this Resource is associated with
	 *
	 * @param facilityId ID of Facility this Resource is associated with
	 */
	public final native void setFacilityId(int facilityId) /*-{
        this.facilityId = facilityId;
    }-*/;

	/**
	 * Get ID of VO this Resource is associated with
	 *
	 * @return ID of VO this Resource is associated with
	 */
	public final int getVoId() {
		return JsUtils.getNativePropertyInt(this, "voId");
	}

	/**
	 * Sets ID of VO this Resource is associated with
	 *
	 * @param voId ID of VO this Resource is associated with
	 */
	public final native void setVoId(int voId) /*-{
        this.voId = voId;
    }-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Resource o) {
		return o.getId() == this.getId();
	}

}