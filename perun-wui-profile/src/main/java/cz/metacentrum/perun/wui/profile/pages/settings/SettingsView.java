package cz.metacentrum.perun.wui.profile.pages.settings;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;


public class SettingsView extends ViewWithUiHandlers<SettingsUiHandlers> implements SettingsPresenter.MyView {

	interface SettingsViewUiBinder extends UiBinder<Widget, SettingsView> {}

	@Inject
	public SettingsView(SettingsViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
	}
}
