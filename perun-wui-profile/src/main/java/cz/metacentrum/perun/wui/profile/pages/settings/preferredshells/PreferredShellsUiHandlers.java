package cz.metacentrum.perun.wui.profile.pages.settings.preferredshells;

import com.gwtplatform.mvp.client.UiHandlers;
import cz.metacentrum.perun.wui.model.beans.Attribute;

public interface PreferredShellsUiHandlers extends UiHandlers {

	void loadPreferredShells();

	void changePreferredShell(Attribute preferredShellsAttribute, String newValue, int index);

	void removePreferredShell(Attribute preferredShellsAttribute, int index);

	void addPreferredShell(Attribute preferredShellsAttribute);

	void navigateBack();
}
