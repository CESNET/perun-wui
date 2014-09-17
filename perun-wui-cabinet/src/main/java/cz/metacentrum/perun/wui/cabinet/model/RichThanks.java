package cz.metacentrum.perun.wui.cabinet.model;

import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * Overlay type for Cabinet API: ThanksForGUI
 * <p/>
 * Thanks represents acknowledgement user (Author) presents to resource owners in Publication itself.
 * This object represents connection between Publication and resource owner.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class RichThanks extends Thanks {

	protected RichThanks() {
	}

	/**
	 * Get name of Owner this Thanks belongs to
	 *
	 * @return name of Owner
	 */
	public final String getOwnerName() {
		return JsUtils.getNativePropertyString(this, "ownerName");
	}

	/**
	 * Set name of Owner this Thanks belongs to
	 *
	 * @param name name of Owner
	 */
	public final native void setId(String name) /*-{
        this.ownerName = name;
    }-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(RichThanks o) {
		return o.getId() == this.getId();
	}

}