package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Overlay type for Facility object from Perun
 *
 * @author Vaclav Mach <374430@mail.muni.cz>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Facility extends GeneralObject {

	protected Facility() {
	}

	/**
	 * Return new instance of Facility with basic properties set.
	 *
	 * @param id   ID of Facility
	 * @param name name of Facility
	 * @param description description of Facility
	 * @return Facility object
	 */
	public static final Facility createNew(int id, String name, String description) {
		Facility fac = new JSONObject().getJavaScriptObject().cast();
		fac.setId(id);
		fac.setName(name);
		fac.setDescription(description);
		fac.setObjectType("Facility");
		return fac;
	}

	/**
	 * Sets Facility's ID.
	 *
	 * @param id ID to set
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Sets Facility's name.
	 *
	 * @param name name to set
	 */
	public final native void setName(String name) /*-{
        this.name = name;
    }-*/;

	/**
	 * Sets Facility's description.
	 *
	 * @param description description to set
	 */
	public final native void setDescription(String description) /*-{
		this.description = description;
	}-*/;

	/**
	 * Get Owners of Facility. This property is present only in RichFacility object.
	 * For standard facility empty list is returned.
	 *
	 * @return owners of Facility
	 */
	public final ArrayList<Owner> getOwners() {

		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "facilityOwners"));

	}

	/**
	 * Return string with technical owners names used to be displayed in a table
	 * @return sorted technical owners names
	 */
	public final String getTechnicalOwnersString() {
		if (!JsUtils.hasOwnProperty(this, "$localTechnicalOwners")) {
			ArrayList<String> result = new ArrayList<>();
			for (Owner o : this.getOwners()) {
				if (o.getType().equals("technical")) result.add(o.getName());
			}
			Collections.sort(result, PerunComparator.getNativeComparator());
			setTechnicalOwnersString(Utils.join(result, ", "));
		}
		return JsUtils.getNativePropertyString(this, "$localTechnicalOwners");
	}

	/**
	 * Used to fill string value displayed in a table
	 * @param techString
	 */
	private final native void setTechnicalOwnersString(String techString) /*-{
		this["$localTechnicalOwners"] = techString;
	}-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Facility o) {
		return o.getId() == this.getId();
	}

}