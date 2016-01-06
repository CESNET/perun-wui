package cz.metacentrum.perun.wui.userprofile.client;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;

/**
 * Main presenter for User profile app.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class UserProfilePresenter extends PerunPresenter<UserProfilePresenter.MyView, UserProfilePresenter.MyProxy> {

    @ProxyStandard
    public interface MyProxy extends Proxy<UserProfilePresenter> {
    }

    public interface MyView extends View {
	    public void setActiveMenuItem(String anchor);
    }

	private PlaceManager placeManager = PerunSession.getPlaceManager();

    @Inject
    UserProfilePresenter(EventBus eventBus, MyView view, MyProxy proxy) {
        super(eventBus, view, proxy);
    }

	@Override
	protected void onReset() {
		String token = placeManager.getCurrentPlaceRequest().getNameToken();
		if (token == null || token.isEmpty()) token = UserProfilePlaceTokens.getPersonal();
		getView().setActiveMenuItem(token);
	}

}
