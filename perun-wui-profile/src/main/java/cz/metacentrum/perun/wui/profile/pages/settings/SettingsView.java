package cz.metacentrum.perun.wui.profile.pages.settings;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;
import org.gwtbootstrap3.client.ui.AnchorListItem;

import java.util.List;


public class SettingsView extends ViewWithUiHandlers<SettingsUiHandlers> implements SettingsPresenter.MyView {

	interface SettingsViewUiBinder extends UiBinder<Widget, SettingsView> {}

	@Inject
	public SettingsView(SettingsViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));

		applyHideConfiguration();
	}

	@UiField AnchorListItem preferredGroupNames;
	@UiField AnchorListItem sshKeys;
	@UiField AnchorListItem preferredShells;
	@UiField AnchorListItem dataQuotas;
	@UiField AnchorListItem alternativePasswords;
	@UiField AnchorListItem sambaPassword;

	private void applyHideConfiguration() {
		setPageVisibility(PerunProfilePlaceTokens.SETTINGS_SSH, sshKeys);
		setPageVisibility(PerunProfilePlaceTokens.SETTINGS_PREFERREDGROUPNAMES, preferredGroupNames);
		setPageVisibility(PerunProfilePlaceTokens.SETTINGS_PREFERREDSHELLS, preferredShells);
		setPageVisibility(PerunProfilePlaceTokens.SETTINGS_DATAQUOTAS, dataQuotas);
		setPageVisibility(PerunProfilePlaceTokens.SETTINGS_ALTPASSWORDS, alternativePasswords);
		setPageVisibility(PerunProfilePlaceTokens.SETTINGS_SAMBA, sambaPassword);
	}

	private void setPageVisibility(String name, Widget menuWidget) {
		List<String> pagesToHide = PerunConfiguration.getProfileSettingsPagesToHide();
		if (pagesToHide.contains(name)) {
			menuWidget.setVisible(false);
		}
	}
}
