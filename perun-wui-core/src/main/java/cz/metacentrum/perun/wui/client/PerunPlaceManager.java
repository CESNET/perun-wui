package cz.metacentrum.perun.wui.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;

/**
 *  Generic PlaceManager for Perun WUI apps, which ensure, that access without valid
 *  session causes page to force reload (e.g. when user previously logged out).
 *
 *  It can be extended if your app requires specific behaviour.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunPlaceManager extends PlaceManagerImpl {

	private final PlaceRequest defaultPlaceRequest;

	@Inject
	public PerunPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter, @DefaultPlace String defaultPlaceNameToken) {
		super(eventBus, tokenFormatter, (PlaceHistoryHandler.Historian) GWT.create(PlaceHistoryHandler.Historian.class));
		this.defaultPlaceRequest = new PlaceRequest.Builder().nameToken(defaultPlaceNameToken).build();
	}

	@Override
	public void revealDefaultPlace() {
		revealPlace(defaultPlaceRequest, false);
	}

	@Override
	public void revealErrorPlace(String invalidHistoryToken) {
		revealPlace(new PlaceRequest.Builder().nameToken(PlaceTokens.NOT_FOUND).build(), false);
	}

	@Override
	public void revealUnauthorizedPlace(String unauthorizedHistoryToken) {
		revealPlace(new PlaceRequest.Builder().nameToken(PlaceTokens.UNAUTHORIZED).build(), false);
	}

	@Override
	protected void doRevealPlace(PlaceRequest request, boolean updateBrowserUrl) {

		if (!PerunSession.isPerunLoaded()) {
			// User logged out previously. Use force reload of current URL to reload whole app.
			Window.Location.reload();
		} else {
			super.doRevealPlace(request, updateBrowserUrl);
		}

	}

}
