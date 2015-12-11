package cz.metacentrum.perun.wui.pages;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;

/**
 * Presenter responsible for showing generic NOT FOUND page.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class NotFoundPresenter extends Presenter<NotFoundPresenter.MyView, NotFoundPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@NameToken(PlaceTokens.NOT_FOUND)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<NotFoundPresenter> {
	}

	@Inject
	public NotFoundPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
	}

}
