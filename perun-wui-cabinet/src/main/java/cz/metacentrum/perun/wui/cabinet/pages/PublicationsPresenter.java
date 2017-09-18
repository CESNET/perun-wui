package cz.metacentrum.perun.wui.cabinet.pages;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import cz.metacentrum.perun.wui.cabinet.client.resources.PerunCabinetPlaceTokens;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;


public class PublicationsPresenter extends Presenter<PublicationsPresenter.MyView, PublicationsPresenter.MyProxy> {

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	public interface MyView extends View{
		void init();
	}

	@NameToken(PerunCabinetPlaceTokens.HOME)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<PublicationsPresenter> {
	}

	@Inject
	public PublicationsPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
	}

	@Override
	protected void onReveal() {
		getView().init();
	}
}