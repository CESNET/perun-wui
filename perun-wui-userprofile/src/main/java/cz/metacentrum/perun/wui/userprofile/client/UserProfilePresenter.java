package cz.metacentrum.perun.wui.userprofile.client;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import cz.metacentrum.perun.wui.client.PerunPresenter;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class UserProfilePresenter extends PerunPresenter<UserProfilePresenter.MyView, UserProfilePresenter.MyProxy> {

    @ProxyStandard
    public interface MyProxy extends Proxy<UserProfilePresenter> {
    }

    public interface MyView extends View {
    }

    @Inject
    UserProfilePresenter(EventBus eventBus, MyView view, MyProxy proxy) {
        super(eventBus, view, proxy);
    }

}
