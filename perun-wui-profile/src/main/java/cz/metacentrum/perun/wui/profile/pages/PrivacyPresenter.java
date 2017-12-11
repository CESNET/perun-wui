package cz.metacentrum.perun.wui.profile.pages;


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

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PrivacyPresenter extends Presenter<PrivacyPresenter.MyView, PrivacyPresenter.MyProxy> implements PrivacyUiHandlers {

	public interface MyView extends View, HasUiHandlers<PrivacyUiHandlers> {

	}

	@NameToken(PerunProfilePlaceTokens.PRIVACY)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<PrivacyPresenter> {

	}

	@Inject
	public PrivacyPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}
}