package cz.metacentrum.perun.wui.cabinet.client;


import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import cz.metacentrum.perun.wui.cabinet.client.resources.PerunCabinetPlaceTokens;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.model.beans.Publication;

import java.util.List;

/**
 * Main presenter for Publications app.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PerunCabinetPresenter extends PerunPresenter<PerunCabinetPresenter.MyView, PerunCabinetPresenter.MyProxy> {

	private List<Publication> userPublications;

	@ProxyStandard
	public interface MyProxy extends Proxy<PerunCabinetPresenter> {

	}

	public interface MyView extends View {
		void setActiveMenuItem(String anchor);
	}

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	@Inject
	PerunCabinetPresenter(EventBus eventBus, MyView View, MyProxy proxy) {
		super(eventBus, View, proxy);
	}

	@Override
	protected void onReveal() {
		loadUserPublications();
	}

	private void loadUserPublications() {

	}

	@Override
	protected void onReset() {
		String token = placeManager.getCurrentPlaceRequest().getNameToken();
		if (token == null || token.isEmpty()) token = PerunCabinetPlaceTokens.getPublications();
		getView().setActiveMenuItem(token);
	}
}