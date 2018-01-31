package cz.metacentrum.perun.wui.profile.pages.settings;


import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

public class SettingsPresenter extends Presenter<SettingsPresenter.MyView, SettingsPresenter.MyProxy> implements SettingsUiHandlers {

	public interface MyView extends View, HasUiHandlers<SettingsUiHandlers> {

	}

	@NameToken(PerunProfilePlaceTokens.SETTINGS)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<SettingsPresenter> {

	}

	@Inject
	public SettingsPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}
}
