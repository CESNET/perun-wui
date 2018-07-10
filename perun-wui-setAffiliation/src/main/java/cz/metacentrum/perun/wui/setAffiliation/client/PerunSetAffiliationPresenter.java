package cz.metacentrum.perun.wui.setAffiliation.client;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import cz.metacentrum.perun.wui.client.PerunPresenter;

/**
 * Main presenter for Set Affiliation app.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public class PerunSetAffiliationPresenter extends PerunPresenter<PerunSetAffiliationPresenter.MyView, PerunSetAffiliationPresenter.MyProxy> {

    @ProxyStandard
    public interface MyProxy extends Proxy<PerunSetAffiliationPresenter> {
    }

    public interface MyView extends View {
    }

    @Inject
    PerunSetAffiliationPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
        super(eventBus, view, proxy);
    }


}
