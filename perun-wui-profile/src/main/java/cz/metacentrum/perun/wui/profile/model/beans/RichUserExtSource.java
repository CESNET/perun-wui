package cz.metacentrum.perun.wui.profile.model.beans;

import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.UserExtSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Extended UserExtSource entity containing the original ues and its attributes
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class RichUserExtSource {

	UserExtSource ues;
	Map<String,Attribute> attributes = new HashMap<>();

	public RichUserExtSource(UserExtSource ues) {
		this.ues = ues;
	}

	public RichUserExtSource(UserExtSource ues, ArrayList<Attribute> attrs) {
		this(ues);
		setAttributes(attrs);
	}

	public UserExtSource getUes() {
		return ues;
	}

	public void setUes(UserExtSource ues) {
		this.ues = ues;
	}

	/**
	 * Set attributes to RichUserExtSource object.
	 *
	 * If attribute present, update it's value
	 * If attribute not present, add to attr list
	 *
	 * @param attributes to set to RichUserExtSource object
	 */
	public final void setAttributes(ArrayList<Attribute> attributes) {
		if (attributes != null) {
			for (Attribute a : attributes) {
				if (a != null) {
					this.attributes.put(a.getURN(), a);
				}
			}
		}
	}

	/**
	 * Get specified user ext source attribute stored in rich user ext source
	 *
	 * @param urn URN of attribute to get
	 * @return user ext source attribute or null if not present
	 */
	public final Attribute getAttribute(String urn) {
		return this.attributes.get(urn);
	}

}
