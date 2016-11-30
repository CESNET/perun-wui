package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
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
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarPlaceTokens;

/**
 * Presenter for displaying registration form of VO / Group
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FormPresenter extends Presenter<FormPresenter.MyView, FormPresenter.MyProxy> {

	private PlaceManager placeManager = PerunSession.getPlaceManager();
	private boolean verifyOnceVisited = false;

	public interface MyView extends View {
		/*Actually load form */
		void draw();
	}

	@NameToken(PerunRegistrarPlaceTokens.FORM)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<FormPresenter> {
	}

	@Inject
	public FormPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
	}

	/* Following makes sure default (form) page is not loaded if it's validation request */

	@Override
	public boolean useManualReveal() {
		return true;
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		String i = Window.Location.getParameter("i");
		String m = Window.Location.getParameter("m");

		if (i != null && m != null && !verifyOnceVisited) {

			PlaceRequest placeRequest = new PlaceRequest.Builder()
					.nameToken(PerunRegistrarPlaceTokens.VERIFY).build();
			// form load failed
			verifyOnceVisited = true;
			getProxy().manualRevealFailed();
			// reveal verify
			placeManager.revealPlace(placeRequest, false);

		} else {
			getProxy().manualReveal(FormPresenter.this);
			getView().draw();
		}

	}

}
