package cz.metacentrum.perun.wui.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.History;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AuthzManager;
import cz.metacentrum.perun.wui.json.managers.UtilsManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;

/**
 * Generic Bootstrapper for Perun Wui apps which is used to load user
 * session info and perun config. Only after that is requested
 * page (Presenter) revealed.
 *
 * TODO: load error page when loading fails or specific behavior on
 * TODO  values from user session
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunBootstrapper implements Bootstrapper {

	private final PlaceManager placeManager;

	@Inject
	public PerunBootstrapper(PlaceManager placeManager) {
		this.placeManager = placeManager;
	}

	@Override
	public void onBootstrap() {

		AuthzManager.getPerunPrincipal(new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject jso) {

				PerunPrincipal pp = ((PerunPrincipal) jso);

				PerunSession.getInstance().setPerunPrincipal(pp);
				PerunSession.getInstance().setRoles(pp.getRoles());

				// TODO - later load this setting from local storage too
				PerunSession.getInstance().setExtendedInfoVisible(PerunSession.getInstance().isPerunAdmin());

				UtilsManager.getGuiConfiguration(new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject jso) {

						// store configuration
						PerunSession.getInstance().setConfiguration((BasicOverlayObject) jso.cast());

						PerunSession.setPerunLoading(false);
						PerunSession.setPerunLoaded(true);

						// OPEN PAGE BASED ON URL
						placeManager.revealCurrentPlace();
						PerunSession.setPlaceManager(placeManager);
						History.fireCurrentHistoryState();

					}

					@Override
					public void onError(PerunException error) {
						PerunSession.setPerunLoading(false);
						PerunSession.setPerunLoaded(false);
						//loader.onError(error, null);
					}

					@Override
					public void onLoadingStart() {

					}
				});

			}

			@Override
			public void onError(PerunException error) {
				PerunSession.setPerunLoading(false);
				PerunSession.setPerunLoaded(false);
				//loader.onError(error, null);
			}

			@Override
			public void onLoadingStart() {
				/*
				RootPanel.get().clear();
				RootPanel.get().add(loader);
				loader.onLoading();
				*/
				PerunSession.setPerunLoading(true);
				PerunSession.setPerunLoaded(false);
			}
		});


	}

}