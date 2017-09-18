package cz.metacentrum.perun.wui.cabinet.client.resources;

import cz.metacentrum.perun.wui.client.resources.PerunTranslation;

/**
 * Perun Cabinet translations.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface PerunCabinetTranslation extends PerunTranslation {

	@DefaultMessage("Publications")
	String appName();

	@DefaultMessage("Publications")
	String publications();

	@DefaultMessage("Title")
	String title();

	@DefaultMessage("Authors")
	String authors();

	@DefaultMessage("Year")
	String year();

	@DefaultMessage("Thanks")
	String thanks();

	@DefaultMessage("Cite")
	String cite();

	@DefaultMessage("New publication")
	String newPublication();

	@DefaultMessage("Loading publications")
	String loadingPublications();

	@DefaultMessage("Search by title")
	String filterText();

	@DefaultMessage("List all")
	String listAll();
}