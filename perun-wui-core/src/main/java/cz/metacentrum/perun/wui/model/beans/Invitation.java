package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for Invitation object from Perun.
 *
 * @author Rastislav Krutak <492918@mail.muni.cz>
 */
public class Invitation extends GeneralObject {

	protected Invitation() {
	}

	/**
	 * Return new instance of Invitation with basic properties set.
	 *
	 * @param id          ID of Invitation
	 * @param redirectUrl the url to redirect the user to after completing the form
	 * @return Invitation object
	 */
	public static final Invitation createNew(int id, String redirectUrl, String token, String receiverEmail) {
		Invitation invitation = new JSONObject().getJavaScriptObject().cast();
		invitation.setId(id);
		invitation.setRedirectUrl(redirectUrl);
		invitation.setToken(token);
		invitation.setReceiverEmail(receiverEmail);
		return invitation;
	}

	/**
	 * Set ID of the invitation
	 *
	 * @param id ID to set
	 */
	public final native void setId(int id) /*-{
		this.id = id;
	}-*/;

	/**
	 * Get redirectUrl of the invitation
	 *
	 * @return redirectUrl of the invitation
	 */
	public final String getRedirectUrl() {
		return JsUtils.getNativePropertyString(this, "redirectUrl");
	}

	/**
	 * Set redirectUrl of the invitation
	 *
	 * @param redirectUrl redirectUrl to set
	 */
	public final native void setRedirectUrl(String redirectUrl) /*-{
		this.redirectUrl = redirectUrl;
	}-*/;

	/**
	 * Get token of the invitation
	 *
	 * @return token of the invitation
	 */
	public final String getToken() {
		return JsUtils.getNativePropertyString(this, "token");
	}

	/**
	 * Set token of the invitation
	 *
	 * @param token token to set
	 */
	public final native void setToken(String token) /*-{
		this.token = token;
	}-*/;

	/**
	 * Get receiverEmail of the invitation
	 *
	 * @return receiverEmail of the invitation
	 */
	public final String getReceiverEmail() {
		return JsUtils.getNativePropertyString(this, "receiverEmail");
	}

	/**
	 * Set receiverEmail of the invitation
	 *
	 * @param receiverEmail receiverEmail to set
	 */
	public final native void setReceiverEmail(String receiverEmail) /*-{
		this.receiverEmail = receiverEmail;
	}-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Invitation o) {
		return o.getId() == this.getId();
	}

}
