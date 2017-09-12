package cz.metacentrum.perun.wui.cabinet.client;


import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;

/**
 * Main presenter for Publications app.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PerunCabinetPresenter extends PerunPresenter<PerunCabinetPresenter.MyView, PerunCabinetPresenter.MyProxy> {

	@ProxyStandard
	public interface MyProxy extends Proxy<PerunCabinetPresenter> {

	}

	public interface MyView extends View {

	}

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	@Inject
	PerunCabinetPresenter(EventBus eventBus, MyView View, MyProxy proxy) {
		super(eventBus, View, proxy);
	}
}