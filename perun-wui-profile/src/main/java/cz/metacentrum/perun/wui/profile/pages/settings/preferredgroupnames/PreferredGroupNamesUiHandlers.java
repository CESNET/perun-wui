package cz.metacentrum.perun.wui.profile.pages.settings.preferredgroupnames;

import com.gwtplatform.mvp.client.UiHandlers;
import cz.metacentrum.perun.wui.model.beans.Attribute;

public interface PreferredGroupNamesUiHandlers extends UiHandlers {

	void loadPreferredGroupNames();

	void changePreferredName(Attribute attribute, String newValue, int index);

	void newPreferredName(Attribute namespaceAttribute, String value);

	void navigateBack();
}
