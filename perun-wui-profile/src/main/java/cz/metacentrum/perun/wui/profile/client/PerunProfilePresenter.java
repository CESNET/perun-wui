package cz.metacentrum.perun.wui.profile.client;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

/**
 * Main presenter for User profile app.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunProfilePresenter extends PerunPresenter<PerunProfilePresenter.MyView, PerunProfilePresenter.MyProxy> {

    @ProxyStandard
    public interface MyProxy extends Proxy<PerunProfilePresenter> {
    }

    public interface MyView extends View {
	    public void setActiveMenuItem(String anchor);
    }

	private PlaceManager placeManager = PerunSession.getPlaceManager();

    @Inject
    PerunProfilePresenter(EventBus eventBus, MyView view, MyProxy proxy) {
        super(eventBus, view, proxy);
    }

	@Override
	protected void onReset() {
		String token = placeManager.getCurrentPlaceRequest().getNameToken();
		if (token == null || token.isEmpty()) token = PerunProfilePlaceTokens.getPersonal();
		getView().setActiveMenuItem(token);
	}

}
