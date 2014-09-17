package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for ResourceTag object from Perun
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ResourceTag extends GeneralObject {

	protected ResourceTag() {
	}

	/**
	 * Return new instance of ResourceTag with basic properties set.
	 *
	 * @param id   ID of ResourceTag
	 * @param name name of ResourceTag
	 * @param voId ID of VO this ResourceTag belong to
	 * @return ResourceTag object
	 */
	public static final ResourceTag createNew(int id, String name, int voId) {
		ResourceTag resource = new JSONObject().getJavaScriptObject().cast();
		resource.setId(id);
		resource.setName(name);
		resource.setVoId(voId);
		resource.setObjectType("ResourceTag");
		return resource;
	}

	/**
	 * Sets ID of ResourceTag
	 *
	 * @param id of ResourceTag to set
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Sets name of ResourceTag
	 *
	 * @param name of ResourceTag to set
	 */
	public final native void setName(String name) /*-{
        this.tagName = name;
    }-*/;

	/**
	 * Get ID of VO this ResourceTag belong to
	 *
	 * @return ID of VO this ResourceTag belong to
	 */
	public final int getVoId() {
		return JsUtils.getNativePropertyInt(this, "voId");
	}

	/**
	 * Sets ID of VO this ResourceTag belong to
	 *
	 * @param voId ID of VO this ResourceTag belong to
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
	public final boolean equals(ResourceTag o) {
		return o.getId() == this.getId();
	}

}