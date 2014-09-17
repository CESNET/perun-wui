package cz.metacentrum.perun.wui.cabinet.model;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * Overlay type for Cabinet API: Thanks
 * <p/>
 * Thanks represents acknowledgement user (Author) presents to resource owners in Publication itself.
 * This object represents connection between Publication and resource owner.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Thanks extends JavaScriptObject {

	protected Thanks() {
	}

	/**
	 * Get ID of Thanks
	 *
	 * @return ID of Thanks
	 */
	public final int getId() {
		return JsUtils.getNativePropertyInt(this, "id");
	}

	/**
	 * Set ID of Thanks
	 *
	 * @param id ID of Thanks
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Get ID of Publication this Thanks is associated with
	 *
	 * @return ID of Publication this Thanks is associated with
	 */
	public final int getPublicationId() {
		return JsUtils.getNativePropertyInt(this, "publicationId");
	}

	/**
	 * Set ID of Publication this Thanks is associated with
	 *
	 * @param id ID of Publication this Thanks is associated with
	 */
	public final native void setPublicationId(int id) /*-{
        this.publicationId = id;
    }-*/;

	/**
	 * Get ID of Owner this Thanks is associated with
	 *
	 * @return ID of Owner this Thanks is associated with
	 */
	public final int getOwnerId() {
		return JsUtils.getNativePropertyInt(this, "ownerId");
	}

	/**
	 * Set ID of Owner this Thanks is associated with
	 *
	 * @param id ID of Owner this Thanks is associated with
	 */
	public final native void setOwnerId(int id) /*-{
        this.ownerId = id;
    }-*/;

	/**
	 * Get CreatedBy (login of user who created this Thanks)
	 *
	 * @return createdBy
	 */
	public final String getCreatedBy() {
		return JsUtils.getNativePropertyString(this, "createdBy");
	}

	/**
	 * Get CreatedByUid (ID of user, who created this Thanks)
	 * <p/>
	 * IMPORTANT: this property might not be set even if getCreatedBy() returns non-NULL value.
	 *
	 * @return ID of user, who created this Thanks
	 */
	public final int getCreatedByUid() {
		return JsUtils.getNativePropertyInt(this, "createdByUid");
	}

	/**
	 * Sets CreatedByUid (ID of user, who created this Thanks)
	 *
	 * @param uid ID of User who created this Thanks
	 */
	public final native void setCreatedByUid(int uid) /*-{
        this.createdByUid = uid;
    }-*/;

	/**
	 * Get Date of Thanks creation as milliseconds from 1.1.1970
	 *
	 * @return Date of Thanks creation
	 */
	public final double getCreatedDate() {
		return JsUtils.getNativePropertyDouble(this, "createdDate");
	}

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Thanks o) {
		return o.getId() == this.getId();
	}

}