package cz.metacentrum.perun.wui.profile.pages.settings.samba;

import com.gwtplatform.mvp.client.UiHandlers;
import cz.metacentrum.perun.wui.json.JsonEvents;

/**
 * UI Handlers for SAMBA password page
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface SambaPasswordUiHandlers extends UiHandlers {

	/**
	 * Loads info whether user has SAMBA password set
	 */
	void loadSambaPassword();

	/**
	 * Changes SAMBA password for the user
	 *
	 * @param password password to set
	 * @param events events passed to the callback updating UI
	 */
	void changeSambaPassword(String password, JsonEvents events);

	void navigateBack();

}
