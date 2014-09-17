package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for Group object from Perun
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Group extends GeneralObject {

	protected Group() {
	}

	/**
	 * Return new instance of Group with basic properties set.
	 *
	 * @param id            ID of Group
	 * @param name          name of Group
	 * @param description   description of Group
	 * @param voId          ID of VO this group belongs to
	 * @param parentGroupId ID of parent group (0 if group is top-level)
	 * @return Group object
	 */
	public static final Group createNew(int id, String name, String description, int voId, int parentGroupId) {
		Group group = new JSONObject().getJavaScriptObject().cast();
		group.setId(id);
		group.setName(name);
		if (name != null && !name.isEmpty()) {
			String[] shortName = name.split(":");
			// store last part of name as shortname
			group.setShortName(shortName[shortName.length - 1]);
		}
		group.setDescription(description);
		group.setVoId(voId);
		group.setParentGroupId(parentGroupId);
		group.setObjectType("Group");
		return group;
	}

	/**
	 * Sets ID to new group
	 *
	 * @param id Id to process
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Sets of new name of group
	 *
	 * @param name names to process
	 */
	public final native void setName(String name) /*-{
        this.name = name;
    }-*/;

	/**
	 * Get Groups shortname (name without group's parent hierarchy).
	 *
	 * @return shortname of group
	 */
	public final String getShortName() {
		return JsUtils.getNativePropertyString(this, "shortName");
	}

	/**
	 * Sets short name of Group
	 *
	 * @param shortName to set
	 */
	public final native void setShortName(String shortName) /*-{
        this.shortName = shortName;
    }-*/;

	/**
	 * Sets description of Group
	 *
	 * @param text text to set as description
	 */
	public final native void setDescription(String text) /*-{
        this.description = text;
    }-*/;

	/**
	 * Get groupId of parent group.
	 *
	 * @return groupId of parent
	 */
	public final int getParentGroupId() {
		return JsUtils.getNativePropertyInt(this, "parentGroupId");
	}

	/**
	 * Sets ID of parent group for this group.
	 *
	 * @param id ID to set as parent
	 */
	public final native void setParentGroupId(int id) /*-{
        this.parentGroupId = id;
    }-*/;

	/**
	 * Return ID of VO, to which this group belongs
	 * This property might not be always set !!
	 *
	 * @return voId
	 */
	public final int getVoId() {
		return JsUtils.getNativePropertyInt(this, "voId");
	}

	/**
	 * Sets ID of VO, to which this group belongs
	 *
	 * @param id
	 */
	public final native void setVoId(int id) /*-{
        this.voId = id;
    }-*/;

	/**
	 * Return TRUE if group is core group (members, administrators)
	 *
	 * @return TRUE if core group
	 */
	public final boolean isCoreGroup() {
		return ("members".equalsIgnoreCase(getName()));
	}

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Group o) {
		return o.getId() == this.getId();
	}

}
