package cz.metacentrum.perun.wui.profile.pages.settings.altPasswords;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * @author Daniel Fecko <dano9500@gmail.com>
 */
public interface AltPasswordsUiHandlers extends UiHandlers {

	void loadAltPasswords();

	void newAltPassoword(String description, String password);

	void deleteAltPassword(String passwordId);

	void navigateBack();

	boolean descriptionIsUsed(String description);

}
