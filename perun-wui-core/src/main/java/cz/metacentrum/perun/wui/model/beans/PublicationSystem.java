package cz.metacentrum.perun.wui.model.beans;

import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for Cabinet API: PublicationSystem
 * <p/>
 * Represents PublicationSystem user's can import publications from.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PublicationSystem extends GeneralObject {

	protected PublicationSystem() {
	}

	/**
	 * Sets ID of PublicationSystem
	 *
	 * @param id ID of PublicationSystem to set
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Set name (friendlyName) of PublicationSystem
	 *
	 * @return name of PublicationSystem to set
	 */
	public final native void setName(String name) /*-{
        this.friendlyName = name;
    }-*/;

	/**
	 * Get URL of PublicationSystem used to query for User's Publications.
	 *
	 * @return URL of PublicationSystem API
	 */
	public final String getUrl() {
		String url = JsUtils.getNativePropertyString(this, "url");
		// hack to hide INTERNAL PUB. SYSTEM value
		if (url != null && url.equals("empty")) return "";
		return url;
	}

	/**
	 * Set URL of PublicationSystem used to query for User's Publications.
	 *
	 * @param url URL of PublicationSystem API
	 */
	public final native void setUrl(String url) /*-{
        this.url = url;
    }-*/;

	/**
	 * Get login-namespace of PublicationSystem used for User identification.
	 *
	 * @return login-namespace of PublicationSystem
	 */
	public final String getLoginNamespace() {
		String namespace = JsUtils.getNativePropertyString(this, "loginNamespace");
		// hack to hide INTERNAL PUB. SYSTEM value
		if (namespace != null && namespace.equals("empty")) return "";
		return namespace;
	}

	/**
	 * Set login-namespace of PublicationSystem used for User identification.
	 *
	 * @param namespace login-namespace of PublicationSystem
	 */
	public final native void setLoginNamespace(String namespace) /*-{
        this.loginNamespace = namespace;
    }-*/;

	/**
	 * Get type of PublicationSystem (class of xml parser)
	 *
	 * @return full class name of xml parser
	 */
	public final String getType() {
		String type = JsUtils.getNativePropertyString(this, "type");
		// hack to hide INTERNAL PUB. SYSTEM value
		if (type != null && type.equals("empty")) return "";
		return type;
	}

	/**
	 * Set type of PublicationSystem (class of xml parser)
	 *
	 * @param type full class name of xml parser
	 */
	public final native String setType(String type) /*-{
        this.type = type;
    }-*/;

	/**
	 * Get username of PublicationSystem used to contact external PS if requires authentication.
	 *
	 * @return username associated with this PS
	 */
	public final String getUsername() {
		String type = JsUtils.getNativePropertyString(this, "username");
		// hack to hide INTERNAL PUB. SYSTEM value
		if (type != null && type.equals("empty")) return "";
		return type;
	}

	/**
	 * Set username of PublicationSystem used to contact external PS if requires authentication.
	 *
	 * @param username username associated with this PS
	 */
	public final native void setUsername(String username) /*-{
        this.username = username;
    }-*/;

	/**
	 * Get password of PublicationSystem used to contact external PS if requires authentication.
	 *
	 * @return password associated with this PS
	 */
	public final String getPassword() {
		return JsUtils.getNativePropertyString(this, "password");
	}

	/**
	 * Set password of PublicationSystem used to contact external PS if requires authentication.
	 *
	 * @param password password associated with this PS
	 */
	public final native void setPassword(String password) /*-{
        this.password = password;
    }-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(PublicationSystem o) {
		return o.getId() == this.getId();
	}

}