package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.JavaScriptObject;
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
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarPlaceTokens;

/**
 * Presenter for displaying registration detail.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AppDetailPresenter extends Presenter<AppDetailPresenter.MyView, AppDetailPresenter.MyProxy> {

	private PlaceManager placeManager = PerunSession.getPlaceManager();
	private AppDetailPresenter presenter = this;

	public interface MyView extends View {
		void setApplication(Application application);
		void onLoadingStartApplication();
		void onErrorApplication(PerunException error, JsonEvents retry);
	}

	@NameToken(PerunRegistrarPlaceTokens.APP_DETAIL)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<AppDetailPresenter> {
	}

	@Inject
	public AppDetailPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
	}

	@Override
	public void prepareFromRequest(final PlaceRequest request) {
		super.prepareFromRequest(request);

		try {
			final int id = Integer.valueOf(request.getParameter("id", null));
			if (id < 1) {
				placeManager.revealErrorPlace(request.getNameToken());
			}

			RegistrarManager.getApplicationById(id, new JsonEvents() {

				final JsonEvents retry = this;

				@Override
				public void onFinished(JavaScriptObject jso) {
					Application app  = jso.cast();
					getView().setApplication(app);
					getProxy().manualReveal(presenter);
				}

				@Override
				public void onError(PerunException error) {
					if (error.getName().equals("PrivilegeException")) {
						getProxy().manualRevealFailed();
						placeManager.revealUnauthorizedPlace(request.getNameToken());
					} else {
						getProxy().manualRevealFailed();
						placeManager.revealErrorPlace(request.getNameToken());

						getView().onErrorApplication(error, retry);
					}
				}

				@Override
				public void onLoadingStart() {
					getView().onLoadingStartApplication();
				}
			});
		} catch( NumberFormatException e ) {
			getProxy().manualRevealFailed();
			placeManager.revealErrorPlace(request.getNameToken());
		}

	}

}
