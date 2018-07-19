package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Overlay type for RichResource object from Perun
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class RichResource extends Resource {

	protected RichResource() {
	}

	/**
	 * Return new instance of RichResource with basic properties set.
	 *
	 * @param id          ID of Resource
	 * @param name        name of Resource
	 * @param description description of Resource
	 * @param facility    Facility this Resource is associated with
	 * @param vo          VO this Resource is associated with
	 * @return RichResource object
	 */
	public static final RichResource createNew(int id, String name, String description, Facility facility, Vo vo, List<ResourceTag> resourceTags) {
		RichResource resource = new JSONObject().getJavaScriptObject().cast();
		resource.setId(id);
		resource.setName(name);
		resource.setDescription(description);
		resource.setVoId(vo.getId());
		resource.setVo(vo);
		resource.setFacilityId(facility.getId());
		resource.setFacility(facility);
		resource.setObjectType("RichResource");
		resource.setResourcesTags(resourceTags);
		return resource;
	}

	/**
	 * Get Facility this RichResource is associated with
	 *
	 * @return Facility this RichResource is associated with
	 */
	public final Facility getFacility() {
		return (Facility) JsUtils.getNativePropertyObject(this, "facility");
	}

	/**
	 * Set Facility this RichResource is associated with
	 *
	 * @param facility Facility this RichResource is associated with
	 */
	public final native void setFacility(Facility facility) /*-{
        this.facility = facility;
    }-*/;

	/**
	 * Get VO this RichResource is associated with
	 *
	 * @return VO this RichResource is associated with
	 */
	public final Vo getVo() {
		return (Vo) JsUtils.getNativePropertyObject(this, "vo");
	}

	/**
	 * Set VO this RichResource is associated with
	 *
	 * @param vo VO this RichResource is associated with
	 */
	public final native void setVo(Vo vo) /*-{
        this.vo = vo;
    }-*/;

	/**
	 * Get RichResource's tags
	 *
	 * @return list of ResourceTags associated with this resource
	 */
	public final ArrayList<ResourceTag> getResourceTags() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "resourceTags"));
	}

	/**
	 * Set list of associated ResourceTags with this resource
	 *
	 * @param resourceTags ResourceTags associated with resource
	 */
	public final void setResourcesTags(List<ResourceTag> resourceTags){
		setResourcesTags(JsUtils.listToJsArray(resourceTags));
	}

	private native final void setResourcesTags(JsArray<ResourceTag> resourceTags)/*-{
		this.resourceTags = resourceTags;
	}-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(RichResource o) {
		return o.getId() == this.getId();
	}

}