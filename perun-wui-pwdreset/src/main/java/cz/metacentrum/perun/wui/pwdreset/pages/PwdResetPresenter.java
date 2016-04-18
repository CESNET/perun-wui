package cz.metacentrum.perun.wui.pwdreset.pages;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.pwdreset.client.resources.PerunPwdResetPlaceTokens;

/**
 * Presenter for list of submitted applications
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PwdResetPresenter extends Presenter<PwdResetPresenter.MyView, PwdResetPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@NameToken(PerunPwdResetPlaceTokens.RESET)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<PwdResetPresenter> {
	}

	@Inject
	public PwdResetPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
	}

}