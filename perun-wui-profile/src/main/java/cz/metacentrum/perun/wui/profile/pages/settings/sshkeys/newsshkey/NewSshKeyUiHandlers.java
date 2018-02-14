package cz.metacentrum.perun.wui.profile.pages.settings.sshkeys.newsshkey;

import com.gwtplatform.mvp.client.UiHandlers;

public interface NewSshKeyUiHandlers extends UiHandlers {

	void addSshKey(String value);

	void navigateBack();
}
