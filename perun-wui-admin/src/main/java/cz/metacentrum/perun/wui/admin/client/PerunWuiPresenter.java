package cz.metacentrum.perun.wui.admin.client;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import cz.metacentrum.perun.wui.client.PerunPresenter;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunWuiPresenter extends PerunPresenter<PerunWuiPresenter.MyView, PerunWuiPresenter.MyProxy> {

    @ProxyStandard
    public interface MyProxy extends Proxy<PerunWuiPresenter> {
    }

    public interface MyView extends View {
    }

    @Inject
    PerunWuiPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
        super(eventBus, view, proxy);
    }

}
