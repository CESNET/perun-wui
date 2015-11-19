package cz.metacentrum.perun.wui.userprofile.pages;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.userprofile.client.UserProfilePlaceTokens;

/**
 * Presenter for displaying registration detail.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PersonalPresenter extends Presenter<PersonalPresenter.MyView, PersonalPresenter.MyProxy> {

	private PlaceManager placeManager = PerunSession.getPlaceManager();
	private PersonalPresenter presenter = this;

	public interface MyView extends View {
	}

	@NameToken(UserProfilePlaceTokens.PERSONAL)
	@ProxyCodeSplit
	public interface MyProxy extends ProxyPlace<PersonalPresenter> {
	}

	@Inject
	public PersonalPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SET_MAIN_CONTENT);
	}

	/* TODO - we will probably want to fake authz for perun admin to see profiles of others

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

	*/

}
