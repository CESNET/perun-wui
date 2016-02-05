package cz.metacentrum.perun.wui.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;

/**
 * Main Perun Admin WUI app Presenter
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunAdminPresenter extends PerunPresenter<PerunAdminPresenter.MyView, PerunAdminPresenter.MyProxy> {

	@ProxyStandard
	public interface MyProxy extends Proxy<PerunAdminPresenter> {
	}

	public interface MyView extends View {
		void setActiveMenuItem(String token);
	}

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	@Inject
	PerunAdminPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
	}


	@Override
	protected void onReset() {
		GWT.log("called");
		String token = placeManager.getCurrentPlaceRequest().getNameToken();
		// FIXME - get default token by role from place manager)
		if (token == null || token.isEmpty()) token = PlaceTokens.getPerunVos();
		getView().setActiveMenuItem(token);
	}

}
