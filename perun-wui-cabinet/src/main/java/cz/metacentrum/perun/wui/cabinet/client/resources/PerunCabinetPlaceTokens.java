package cz.metacentrum.perun.wui.cabinet.client.resources;

import cz.metacentrum.perun.wui.client.resources.PlaceTokens;

/**
 * Place tokens used in Perun Cabinet app.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PerunCabinetPlaceTokens extends PlaceTokens {

	//General pages
	public static final String PUBLICATIONS = "publications";
	public static final String NEW_PUBLICATION = "new-publication";

	public static String getPublications() {
		return PUBLICATIONS;
	}

	public static String getNewPublication() {
		return NEW_PUBLICATION;
	}
}