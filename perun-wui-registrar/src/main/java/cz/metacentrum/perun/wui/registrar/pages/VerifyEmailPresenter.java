package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarPlaceTokens;

/**
 * Presenter for displaying mail verification result
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class VerifyEmailPresenter extends Presenter<VerifyEmailPresenter.MyView, VerifyEmailPresenter.MyProxy> {

	private PlaceManager placeManager = PerunSession.getPlaceManager();
	private VerifyEmailPresenter presenter = this;

	public interface MyView extends View {
		void setResult(boolean result);
		void onLoadingStart();
		void onError(PerunException error, JsonEvents retry);
	}

	@NameToken(PerunRegistrarPlaceTokens.VERIFY)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<VerifyEmailPresenter> {
	}

	@Inject
	public VerifyEmailPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
	}

	@Override
	public void prepareFromRequest(final PlaceRequest request) {
		super.prepareFromRequest(request);

		final String i = Window.Location.getParameter("i");
		final String m = Window.Location.getParameter("m");
		if (i == null || m == null) {
			placeManager.revealErrorPlace(request.getNameToken());
		}

		RegistrarManager.validateEmail(i, m, new JsonEvents() {

			final JsonEvents retry = this;

			@Override
			public void onFinished(JavaScriptObject jso) {
				BasicOverlayObject result = jso.cast();
				getView().setResult(result.getBoolean());
				getProxy().manualReveal(presenter);
			}

			@Override
			public void onError(PerunException error) {
				if (error.getName().equals("PrivilegeException")) {
					getProxy().manualRevealFailed();
					placeManager.revealUnauthorizedPlace(request.getNameToken());
				} else {
					// if verification fails because of an error, display it
					getView().onError(error, retry);
					getProxy().manualReveal(presenter);
				}
			}

			@Override
			public void onLoadingStart() {
				getView().onLoadingStart();
			}
		});

	}

	@Override
	public boolean useManualReveal() {
		return true;
	}
}
