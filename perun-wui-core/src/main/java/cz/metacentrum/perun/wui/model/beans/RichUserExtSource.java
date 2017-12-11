package cz.metacentrum.perun.wui.model.beans;

import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * Extended UserExtSource entity containing the original ues and its email Attribute.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class RichUserExtSource extends UserExtSource {

	protected RichUserExtSource() {}

	/**
	 * Get User's email
	 *
	 * @return User's email
	 */
	public final String getEmail() {
		return JsUtils.getNativePropertyString(this, "email");
	}

	/**
	 * Set User's email in concrete ExtSource
	 *
	 * @param email User's email
	 */
	public final native void setEmail(String email) /*-{
        this.email = email;
    }-*/;

	public static RichUserExtSource mapUes(UserExtSource ues, Attribute mailAttribute) {
		RichUserExtSource richUserExtSource = (RichUserExtSource) ues;
		richUserExtSource.setEmail(mailAttribute.getValue());

		return richUserExtSource;
	}
}
