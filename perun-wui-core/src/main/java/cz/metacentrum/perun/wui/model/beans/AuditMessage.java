package cz.metacentrum.perun.wui.model.beans;

import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Object definition for audit messages from Perun
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AuditMessage extends GeneralObject {

	protected AuditMessage() {
	}

	/**
	 * Get full audit message text
	 *
	 * @return full message text
	 */
	public final String getFullMessage() {
		return JsUtils.getNativePropertyString(this, "fullMessage");
	}

	/**
	 * Get standard audit message text
	 *
	 * @return standard message text
	 */
	public final String getMessage() {
		return JsUtils.getNativePropertyString(this, "msg");
	}

	/**
	 * Get name of Actor who performed action audited by this message
	 *
	 * @return actor who performed action
	 */
	public final String getActor() {
		return JsUtils.getNativePropertyString(this, "actor");
	}

	/**
	 * Get date and time, when message was audited (action performed)
	 *
	 * @return date & time as string
	 */
	public final String getCreatedAt() {
		return JsUtils.getNativePropertyString(this, "createdAt");
	}

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(AuditMessage o) {
		return (o.getId() == this.getId());
	}

}
