package cz.metacentrum.perun.wui.cabinet.model;

import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for Cabinet API: Publication
 * <p/>
 * Represents Publication User reported to resource Owners in Perun so he could
 * benefit from it by means of priority access to resources.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Publication extends GeneralObject {

	protected Publication() {
	}

	/**
	 * Sets ID of Publication in Perun
	 *
	 * @param id ID of Publication in Perun
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Set name of Publication (title)
	 *
	 * @param title Name of Publication
	 */
	public final native void setName(String title) /*-{
        this.title = title;
    }-*/;

	/**
	 * Get ID of Publication in external pub. system
	 *
	 * @return ID of Publication in external pub. system
	 */
	public final int getExternalId() {
		return JsUtils.getNativePropertyInt(this, "externalId");
	}

	/**
	 * Set ID of Publication in external pub. system
	 *
	 * @param id ID of Publication in external pub. system
	 */
	public final native void setExternalId(int id) /*-{
        this.externalId = id;
    }-*/;

	/**
	 * Get ID of external Publication system
	 *
	 * @return ID of external Publication system
	 */
	public final int getPublicationSystemId() {
		return JsUtils.getNativePropertyInt(this, "publicationSystemId");
	}

	/**
	 * Set ID of external Publication system
	 *
	 * @param id ID of external Publication system
	 */
	public final native void setPublicationSystemId(int id) /*-{
        this.publicationSystemId = id;
    }-*/;

	/**
	 * Get ID of Category this Publication belongs to.
	 *
	 * @return ID of Category this Publication belongs to.
	 */
	public final int getCategoryId() {
		return JsUtils.getNativePropertyInt(this, "categoryId");
	}

	/**
	 * Set ID of Category this Publication belongs to.
	 *
	 * @param categoryId ID of Category this Publication belongs to
	 */
	public final native void setCategoryId(int categoryId) /*-{
        this.categoryId = categoryId;
    }-*/;

	/**
	 * Get year when Publication was published
	 *
	 * @return year when Publication was published
	 */
	public final int getYear() {
		return JsUtils.getNativePropertyInt(this, "year");
	}

	/**
	 * Set year when Publication was published
	 *
	 * @param year when Publication was published
	 */
	public final native void setYear(int year) /*-{
        this.year = year;
    }-*/;

	/**
	 * Get full citation of Publication.
	 * <p/>
	 * IMPORTANT: User's are not technically restricted to provide citation in a specific format, but
	 * they are advised to respect ČSN ISO 690 or ČSN ISO 690-2.
	 *
	 * @return full citation of Publication
	 */
	public final String getCitation() {
		return JsUtils.getNativePropertyString(this, "main");
	}

	/**
	 * Sets full citation to Publication.
	 * <p/>
	 * IMPORTANT: User's are not technically restricted to provide citation in a specific format, but
	 * they are advised to respect ČSN ISO 690 or ČSN ISO 690-2.
	 *
	 * @param citation
	 */
	public final native void setCitation(String citation) /*-{
        this.main = citation;
    }-*/;

	/**
	 * Get ISBN or ISSN of Publication.
	 * <p/>
	 * IMPORTANT: User's are not technically restricted to provide valid ISBN / ISSN value.
	 *
	 * @return ISBN or ISSN of Publication
	 */
	public final String getIsbn() {
		return JsUtils.getNativePropertyString(this, "isbn");
	}

	/**
	 * Set ISBN of ISSN of Publication
	 * <p/>
	 * IMPORTANT: User's are not technically restricted to provide valid ISBN / ISSN value.
	 *
	 * @param isbn ISBN or ISSN of Publication
	 */
	public final native void setIsbn(String isbn) /*-{
        this.isbn = isbn;
    }-*/;

	/**
	 * Get Publication's DOI (digital object identifier)
	 *
	 * @return DOI of Publication
	 */
	public final String getDoi() {
		return JsUtils.getNativePropertyString(this, "doi");
	}

	/**
	 * Set Publication's DOI (digital object identifier)
	 *
	 * @param doi DOI to set to Publication
	 */
	public final native void setDoi(String doi) /*-{
        return this.doi = doi;
    }-*/;

	/**
	 * Get custom rank of Publication (rating coefficient).
	 *
	 * @return rank of Publication
	 */
	public final double getRank() {
		return JsUtils.getNativePropertyDouble(this, "rank");
	}

	/**
	 * Sets custom rank of Publication (rating coefficient).
	 * Default value should be 0.0
	 * <p/>
	 * IMPORTANT: this shouldn't be set by normal users, rather only by administrator.
	 *
	 * @param rank rating coefficient to set
	 */
	public final native void setRank(double rank) /*-{
        this.rank = rank;
    }-*/;

	/**
	 * Return TRUE if Publication is locked for changes, FALSE otherwise.
	 *
	 * @return TRUE = locked / FALSE = not locked
	 */
	public final boolean isLocked() {
		return JsUtils.getNativePropertyBoolean(this, "locked");
	}

	/**
	 * Set Publication as locked for changes
	 *
	 * @param locked TRUE = locked / FALSE = unlocked
	 */
	public final native void setLocked(boolean locked) /*-{
        this.locked = locked;
    }-*/;

	/**
	 * Get CreatedBy (login of user who created this Publication)
	 *
	 * @return createdBy
	 */
	public final String getCreatedBy() {
		return JsUtils.getNativePropertyString(this, "createdBy");
	}

	/**
	 * Get CreatedByUid (ID of user, who created this Publication)
	 * <p/>
	 * IMPORTANT: this property might not be set even if getCreatedBy() returns non-NULL value.
	 *
	 * @return ID of user, who created this Publication
	 */
	public final int getCreatedByUid() {
		return JsUtils.getNativePropertyInt(this, "createdByUid");
	}

	/**
	 * Sets CreatedByUid (ID of user, who created this Publication)
	 *
	 * @param uid ID of User who created this Publication
	 */
	public final native void setCreatedByUid(int uid) /*-{
        this.createdByUid = uid;
    }-*/;

	/**
	 * Get Date of Publication creation as milliseconds from 1.1.1970
	 *
	 * @return Date of Publication creation
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
	public final boolean equals(Publication o) {
		return o.getId() == this.getId();
	}

}