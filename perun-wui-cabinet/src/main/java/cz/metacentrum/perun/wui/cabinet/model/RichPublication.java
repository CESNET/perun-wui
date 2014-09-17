package cz.metacentrum.perun.wui.cabinet.model;

import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Overlay type for Cabinet API: PublicationForGUI
 * <p/>
 * Represents Publication User reported to resource Owners in Perun so he could
 * benefit from it by means of priority access to resources.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class RichPublication extends Publication {

	protected RichPublication() {
	}

	/**
	 * Get list of Thanks (acknowledgements) associated with this Publication
	 *
	 * @return Thanks associated with this Publication
	 */
	public final ArrayList<Thanks> getThanks() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "thanks"));
	}

	/**
	 * Get name of Category this Publication belongs to
	 *
	 * @return name of Category
	 */
	public final String getCategoryName() {
		return JsUtils.getNativePropertyString(this, "categoryName");
	}

	/**
	 * Sets name of Category this Publication belongs to
	 *
	 * @param name name of Category
	 */
	public final native String setCategoryName(String name) /*-{
        this.categoryName = name;
    }-*/;

	/**
	 * Get name of PublicationSystem this Publication belongs to
	 *
	 * @return publication system name
	 */
	public final String getPublicationSystemName() {
		return JsUtils.getNativePropertyString(this, "pubSystemName");
	}

	/**
	 * Get list of Authors of this Publication (Users associated by Authorship)
	 *
	 * @return list of Authors
	 */
	public final ArrayList<Author> getAuthors() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "authors"));
	}

	/**
	 * Get list of Publication Authors as formatted string. Authors are ordered alphabetically.
	 * String look like: "SURENAME name, SURENAME name"
	 *
	 * @return list of Authors as formatted string
	 */
	public final String getAuthorsFormatted() {
		ArrayList<String> list = new ArrayList<>();
		for (Author author : getAuthors()) list.add(author.getFormattedName());
		Collections.sort(list, PerunComparator.getNativeComparator());
		return Utils.join(list, ", ");
	}

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(RichPublication o) {
		return o.getId() == this.getId();
	}

}