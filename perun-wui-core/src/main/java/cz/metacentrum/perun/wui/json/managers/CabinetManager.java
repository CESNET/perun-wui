package cz.metacentrum.perun.wui.json.managers;


import com.google.gwt.http.client.Request;
import cz.metacentrum.perun.wui.json.JsonClient;
import cz.metacentrum.perun.wui.json.JsonEvents;


/**
 * Manager with standard callbacks to Perun's API (CabinetManager).
 * <p/>
 * Each callback returns unique Request used to make call. Such call can be removed
 * while processing to prevent any further actions based on it's {@link JsonEvents JsonEvents}.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class CabinetManager {

	private static final String CABINET_MANAGER = "cabinetManager/";

	/**
	 * Finds rich publications in Cabinet by GUI filter:
	 *
	 * @param id exact match (used when search for publication of authors)
	 * @param title if "like" this substring
	 * @param isbn if "like" this substring
	 * @param year exact match
	 * @param category exact match
	 * @param doi DOI
	 * @param yearSince if year >= yearSince
	 * @param yearTill if year <= yearTill
	 * @param userId exact match or 0
	 * @param events Events done on callback
	 *
	 * @return Request unique request
	 */
	public static Request findPublicationsByGUIFilter(int id, String title, String isbn, int year, int category, String doi, int yearSince, int yearTill, int userId, JsonEvents events) {

		JsonClient client = new JsonClient(events);
		if (id > 0) client.put("id", id);
		if (title != null && title.replaceAll("\\s*", "").length() > 0) client.put("title", title);
		if (isbn != null && isbn.replaceAll("\\s*", "").length() > 0) client.put("isbn", isbn);
		if (year > 0) client.put("year", year);
		if (category > 0) client.put("category", category);
		if (doi != null && doi.replaceAll("\\s*", "").length() > 0) client.put("doi", doi);
		if (yearSince > 0) client.put("yearSince", yearSince);
		if (yearTill > 0) client.put("yearTill", yearTill);
		if (userId > 0) client.put("userId", userId);

		return client.call(CABINET_MANAGER + "findPublicationsByGUIFilter");
	}
}
