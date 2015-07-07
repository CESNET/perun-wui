package cz.metacentrum.perun.wui.registrar.pages;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.registrar.client.RegistrarPlaceTokens;

/**
 * Presenter for displaying registration form of VO / Group
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FormPresenter extends Presenter<FormPresenter.MyView, FormPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@NameToken(RegistrarPlaceTokens.FORM)
	@ProxyCodeSplit
	public interface MyProxy extends ProxyPlace<FormPresenter> {
	}

	@Inject
	public FormPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SET_MAIN_CONTENT);
	}

}