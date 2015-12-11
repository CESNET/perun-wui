package cz.metacentrum.perun.wui.pages;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;

/**
 * Presenter responsible for showing generic NOT AUTHORIZED page.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class NotAuthorizedPresenter extends Presenter<NotAuthorizedPresenter.MyView, NotAuthorizedPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@NameToken(PlaceTokens.UNAUTHORIZED)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<NotAuthorizedPresenter> {
	}

	@Inject
	public NotAuthorizedPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
	}

}
