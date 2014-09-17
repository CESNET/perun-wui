package cz.metacentrum.perun.wui.cabinet.model;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

/**
 * Overlay type for Cabinet API: Authorship
 * <p/>
 * It represents connection between Publication and User.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Authorship extends JavaScriptObject {

	protected Authorship() {
	}

	/**
	 * Get Authorship ID
	 *
	 * @return ID of Authorship
	 */
	public final int getId() {
		return JsUtils.getNativePropertyInt(this, "id");
	}

	/**
	 * Set Authorship ID
	 *
	 * @param id ID to set
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Get ID of Publication
	 *
	 * @return ID of Publication
	 */
	public final int getPublicationId() {
		return JsUtils.getNativePropertyInt(this, "publicationId");
	}

	/**
	 * Set Publication ID
	 *
	 * @param id ID to set
	 */
	public final native void setPublicationId(int id) /*-{
        this.publicationId = id;
    }-*/;

	/**
	 * Get User ID
	 *
	 * @return ID of User
	 */
	public final int getUserId() {
		return JsUtils.getNativePropertyInt(this, "userId");
	}

	/**
	 * Set User ID
	 *
	 * @param id ID to set
	 */
	public final native void setUserId(int id) /*-{
        this.userId = id;
    }-*/;

	/**
	 * Get CreatedBy (login of user who created this Authorship)
	 *
	 * @return createdBy
	 */
	public final String getCreatedBy() {
		return JsUtils.getNativePropertyString(this, "createdBy");
	}

	/**
	 * Get CreatedByUid (ID of user, who created this Authorship)
	 * <p/>
	 * IMPORTANT: this property might not be set even if getCreatedBy() returns non-NULL value.
	 *
	 * @return ID of user, who created this Authorship
	 */
	public final int getCreatedByUid() {
		return JsUtils.getNativePropertyInt(this, "createdByUid");
	}

	/**
	 * Sets CreatedByUid (ID of user, who created this Authorship)
	 *
	 * @param uid ID of User who created this Authorship
	 */
	public final native void setCreatedByUid(int uid) /*-{
        this.createdByUid = uid;
    }-*/;

	/**
	 * Get Date of Authorship creation as milliseconds from 1.1.1970
	 *
	 * @return Date of Authorship creation
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
	public final boolean equals(Authorship o) {
		return o.getId() == this.getId();
	}

}
