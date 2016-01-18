package cz.metacentrum.perun.wui.profile.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.model.beans.User;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

/**
 * Presenter for displaying registration detail.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class OrganizationsPresenter extends Presenter<OrganizationsPresenter.MyView, OrganizationsPresenter.MyProxy> {

	private PlaceManager placeManager = PerunSession.getPlaceManager();
	private OrganizationsPresenter presenter = this;

	public interface MyView extends View {
		void setUser(User user);
		void onLoadingStart();
		void onError(PerunException ex, JsonEvents retry);
	}

	@NameToken(PerunProfilePlaceTokens.ORGANIZATIONS)
	@ProxyCodeSplit
	public interface MyProxy extends ProxyPlace<OrganizationsPresenter> {
	}

	@Inject
	public OrganizationsPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
	}


	@Override
	public void prepareFromRequest(final PlaceRequest request) {

		super.prepareFromRequest(request);

		try {

			String userId = request.getParameter("id", null);
			if (userId == null) {
				userId = String.valueOf(PerunSession.getInstance().getUser().getId());
			}

			final int id = Integer.valueOf(userId);

			if (id < 1) {
				placeManager.revealErrorPlace(request.getNameToken());
			}

			if (PerunSession.getInstance().isSelf(id)) {

				UsersManager.getRichUserWithAttributes(id, new JsonEvents() {

					final JsonEvents retry = this;

					@Override
					public void onFinished(JavaScriptObject jso) {
						RichUser user = jso.cast();
						getView().setUser(user);
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
							getView().onError(error, retry);
						}
					}

					@Override
					public void onLoadingStart() {
						getView().onLoadingStart();
					}
				});

			} else {
				placeManager.revealUnauthorizedPlace(request.getNameToken());
			}

		} catch( NumberFormatException e ) {
			getProxy().manualRevealFailed();
			placeManager.revealErrorPlace(request.getNameToken());
		}

	}

	@Override
	public boolean useManualReveal() {
		return true;
	}
}
