package cz.metacentrum.perun.wui.pages;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UtilsManager;
import cz.metacentrum.perun.wui.model.PerunException;

/**
 * Presenter responsible for showing generic LOGOUT page.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class LogoutPresenter extends Presenter<LogoutPresenter.MyView, LogoutPresenter.MyProxy> {

	public interface MyView extends View {
		/**
		 * Show processing widgets or hide them on finished.
		 *
		 * @param loading TRUE = loading
		 */
		void loading(boolean loading);
	}

	@NameToken(PlaceTokens.LOGOUT)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<LogoutPresenter> {
	}

	@Inject
	public LogoutPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, RevealType.Root);
	}

	@Override
	protected void onReveal() {
		super.onReveal();

		UtilsManager.logout(new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject jso) {
				getView().loading(false);
				Utils.clearFederationCookies();
				PerunSession.setPerunLoaded(false);
			}

			@Override
			public void onError(PerunException error) {
				// TODO error state
			}

			@Override
			public void onLoadingStart() {
				getView().loading(true);
			}
		});

	}
}
