package cz.metacentrum.perun.wui.model.common;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for RTMessage object from Perun.
 *
 * @author Vaclav Mach <374430@mail.muni.cz>
 * @author Pavel Zlamal <zlamal@cesnet.cz>
 */
public class RTMessage extends GeneralObject {

	protected RTMessage() {
	}

	public static final RTMessage createNew(int number, String mail) {
		RTMessage message = new JSONObject().getJavaScriptObject().cast();
		message.setTicketNumber(number);
		message.setMemberPreferredEmail(mail);
		return message;
	}

	public final int getTicketNumber() {
		return JsUtils.getNativePropertyInt(this, "ticketNumber");
	}

	/**
	 * Set number (ID) of Ticket
	 *
	 * @param ticketId ID of created ticket
	 */
	public final native void setTicketNumber(int ticketId) /*-{
		this.ticketNumber = ticketId;
	}-*/;

	/**
	 * Get user's mail associated with posted RT request
	 *
	 * @return user's mail associated with RT request
	 */
	public final String getMemberPreferredEmail() {
		return JsUtils.getNativePropertyString(this, "memberPreferredEmail");
	}

	/**
	 * Set member preferred mail
	 *
	 * @param mail mail of user we sent RT message to
	 */
	public final native void setMemberPreferredEmail(String mail) /*-{
		this.memberPreferredEmail = mail;
	}-*/;

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
