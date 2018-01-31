package cz.metacentrum.perun.wui.profile.pages.settings.sshkeys;

import com.gwtplatform.mvp.client.UiHandlers;
import cz.metacentrum.perun.wui.model.beans.Attribute;

public interface SshKeysUiHandlers extends UiHandlers {

	void removeSshKey(Attribute attribute, int n);

	void removeAdminSshKey(Attribute attribute, int n);

	void loadSshKeys();

	void loadAdminSshKeys();
}
