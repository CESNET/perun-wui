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

	@DefaultMessage("My profile")
	String menuMyProfile();
}