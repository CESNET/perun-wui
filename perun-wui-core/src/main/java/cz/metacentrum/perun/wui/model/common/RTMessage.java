package cz.metacentrum.perun.wui.model.common;

import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for RTMessage object from Perun.
 *
 * @author Vaclav Mach <374430@mail.muni.cz>
 */
public class RTMessage extends GeneralObject {

	protected RTMessage() {
	}

	public final int getTicketNumber() {
		return JsUtils.getNativePropertyInt(this, "ticketNumber");
	}

	/**
	 * Get user's mail associated with posted RT request
	 *
	 * @return user's mail associated with RT request
	 */
	public final String getMemberPreferredEmail() {
		return JsUtils.getNativePropertyString(this, "memberPreferredEmail");
	}

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(RTMessage o) {
		return o.getTicketNumber() == this.getTicketNumber();
	}
}
