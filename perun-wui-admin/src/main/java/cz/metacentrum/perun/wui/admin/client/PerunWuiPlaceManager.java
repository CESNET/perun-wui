package cz.metacentrum.perun.wui.admin.client;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;
import cz.metacentrum.perun.wui.client.PerunPlaceManager;

/**
 * PlaceManager specific for Perun Admin WUI
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunWuiPlaceManager extends PerunPlaceManager {

	private final PlaceRequest defaultPlaceRequest;

	@Inject
	PerunWuiPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter, @DefaultPlace String defaultPlaceNameToken) {
		super(eventBus, tokenFormatter, defaultPlaceNameToken);
		this.defaultPlaceRequest = new PlaceRequest.Builder().nameToken(defaultPlaceNameToken).build();
	}

	@Override
	public void revealDefaultPlace() {

		// TODO - here we must reveal specific parts of GUI based on user roles !!
		// TODO - perunadmin / vo admin / group admin / facility admin etc.
		revealPlace(defaultPlaceRequest, false);

	}

}
