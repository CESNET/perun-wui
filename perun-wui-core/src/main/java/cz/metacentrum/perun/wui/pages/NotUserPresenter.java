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
 * Presenter responsible for showing generic error "you are not user of Perun".
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class NotUserPresenter extends Presenter<NotUserPresenter.MyView, NotUserPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@NameToken(PlaceTokens.NOT_USER)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<NotUserPresenter> {
	}

	@Inject
	public NotUserPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
	}

	@Override
	protected void onReveal() {
		super.onReveal();


	}
}
