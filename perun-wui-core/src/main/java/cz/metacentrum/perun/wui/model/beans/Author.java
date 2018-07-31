package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Overlay type for Cabinet API: Author
 * <p/>
 * Object is a wrapper around User object from Perun and all non-User properties are optional.
 * This means, that their values are dependant on context, under which they were retrieved from Perun.
 * <p/>
 * e.g. Author taken from Publication object doesn't contain all logins and authorships, but
 * Authors retrieved by getAllAuthors() API method contain them.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */

public class Author extends User {

	protected Author() {
	}

	/**
	 * Return new instance of Author with basic properties set.
	 *
	 * @param id            ID of User
	 * @param firstName     firstName of User
	 * @param middleName    middleName of User
	 * @param lastName      lastName of User
	 * @param titleBefore   titleBefore of User
	 * @param titleAfter    titleAfter of User
	 * @param attributes	attributes related to Author
	 * @param authorships   all Author's authorships
	 * @return new instance of Author with basic properties set
	 */
	static public Author createNew(int id, String firstName, String lastName, String middleName, String titleBefore, String titleAfter,
								   List<Attribute> attributes, List<Authorship> authorships){
		Author author = new JSONObject().getJavaScriptObject().cast();
		author.setId(id);
		author.setFirstName(firstName);
		author.setLastName(lastName);
		author.setMiddleName(middleName);
		author.setTitleBefore(titleBefore);
		author.setTitleAfter(titleAfter);
		author.setAttributes(attributes);
		author.setAuthorships(authorships);
		return author;
	}

	/**
	 * Get Author's name is format expected in citations, like: "LAST_NAME First_name Middle_name"
	 * Any academic titles are omitted. If no name is present, return NULL. By User/Author definition,
	 * in Perun, at least "lastName" should be present
	 *
	 * @return Author's name like "LAST_NAME First_name Middle_name"
	 */
	public final native String getFormattedName() /*-{

        var fullName = "";

        var firstName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "firstName");
        var lastName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "lastName");
        var middleName = @cz.metacentrum.perun.wui.client.utils.JsUtils::getNativePropertyString(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(this, "middleName");

        if (lastName !== null) fullName += lastName.toUpperCase();
        if (firstName !== null) fullName += " " + firstName;
        if (middleName !== null) fullName += " " + middleName;

        if (fullName.length > 0) return fullName;
        return null;

    }-*/;

	/**
	 * Return count of Author's / User's publications based on count of his unique Authorships.
	 *
	 * @return count of reported Publications of Author
	 */
	public final int getPublicationsCount() {
		return JsUtils.getNativePropertyArray(this, "authorships").length();
	}

	/**
	 * Authorships related to this author.
	 *
	 * @param authorships List<Authorship> related to this author
	 */
	public final void setAuthorships(List<Authorship> authorships){
		setAuthorships(JsUtils.listToJsArray(authorships));
	}

	private final native void setAuthorships(JsArray<Authorship> authorships)/*-{
		this.authorships = authorships;
	}-*/;


	/**
	 * Get Authorship entries associated with Author.
	 * <p/>
	 * IMPORTATNT: This property is filled only when Author was retrieved by Publication ID
	 *
	 * @return all Author's authorships
	 */
	public final ArrayList<Authorship> getAuthorships() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "authorships"));
	}

	/**
	 * Get Authorship for specific Publication of Author. Return NULL if not found.
	 * <p/>
	 * IMPORTATNT: This property is filled only when Author was retrieved by Publication ID
	 *
	 * @param publicationId ID of Publication to get Authorship for
	 * @return Authorship associated with Author and Publication
	 */
	public final Authorship getAuthorshipByPublicationId(int publicationId) {

		for (Authorship auth : getAuthorships()) {
			if (auth.getPublicationId() == publicationId) {
				return auth;
			}
		}
		return null;

	}

	/**
	 * Gets all logins for this Author which are stored inside his UserExtSources
	 *
	 * @return logins stored for this user
	 */
	public final String getLogins() {
		ArrayList<String> list = new ArrayList<>();
		for (UserExtSource ues : getUserExtSources()) {
			list.add(ues.getLogin());
		}
		Collections.sort(list, PerunComparator.getNativeComparator());
		return Utils.join(list, ", ");
	}

	/**
	 * Return all UserExtSources stored inside the Author
	 *
	 * @return UserExtSources stored inside the Author
	 */
	private final ArrayList<UserExtSource> getUserExtSources() {
		return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "logins"));
	}

	/**
	 * Set attributes related to Author
	 * @param attributes
	 */
	public final void setAttributes(List<Attribute> attributes){
    	setAttributes(JsUtils.listToJsArray(attributes));
	}

	private native final void setAttributes(JsArray<Attribute> attributes)/*-{
		this.attributes = attributes;
	}-*/;

	/**
	 * Get all attributes related to Author
	 * @return All attributes related to Author
	 */
	public final List<Attribute> getAttributes(){
        return JsUtils.jsoAsList(JsUtils.getNativePropertyArray(this, "attributes"));
	}

	/**
	 * Compares two Authors
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Author o) {
		return o.getId() == this.getId();
	}

}