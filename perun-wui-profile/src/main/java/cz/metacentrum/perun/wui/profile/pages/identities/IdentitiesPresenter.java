package cz.metacentrum.perun.wui.profile.pages.identities;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AttributesManager;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.profile.model.beans.RichUserExtSource;
import cz.metacentrum.perun.wui.model.beans.UserExtSource;
import cz.metacentrum.perun.wui.profile.client.PerunProfileUtils;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for displaying registration detail.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class IdentitiesPresenter extends Presenter<IdentitiesPresenter.MyView, IdentitiesPresenter.MyProxy> implements IdentitiesUiHandlers {

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	public interface MyView extends View, HasUiHandlers<IdentitiesUiHandlers> {
		void loadingUserExtSourcesStart();
		void loadingUserExtSourcesError(PerunException ex);

		void addUserExtSource(RichUserExtSource extSource);
		void clearUserExtSources();

		void removingUserExtSourceStart(RichUserExtSource userExtSource);
		void removingUserExtSourceError(PerunException ex, RichUserExtSource userExtSource);
	}

	@NameToken(PerunProfilePlaceTokens.IDENTITIES)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<IdentitiesPresenter> {
	}

	@Inject
	public IdentitiesPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}


	@Override
	public void onReveal() {
		getView().clearUserExtSources();
		loadUserExtSources();
	}



	@Override
	public void loadUserExtSources() {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null) {
			placeManager.revealErrorPlace(request.getNameToken());
		} else {
			getView().clearUserExtSources();

			UsersManager.getUserExtSources(userId, new JsonEvents() {

				@Override
				public void onFinished(JavaScriptObject result) {
					List<UserExtSource> userExtSources = JsUtils.jsoAsList(result);

					for (UserExtSource ues : userExtSources) {
						loadAttributes(ues);
					}
				}

				@Override
				public void onError(PerunException error) {
					getView().loadingUserExtSourcesError(error);
				}

				@Override
				public void onLoadingStart() {
					getView().loadingUserExtSourcesStart();
				}

			});
		}
	}

	private void loadAttributes(UserExtSource ues) {
		AttributesManager.getUesAttributes(ues.getId(), new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				ArrayList<Attribute> attributes = JsUtils.jsoAsList(result);
				RichUserExtSource rues = new RichUserExtSource(ues, attributes);
				getView().addUserExtSource(rues);
			}

			@Override
			public void onError(PerunException error) {
				getView().loadingUserExtSourcesError(error);
			}

			@Override
			public void onLoadingStart() {
				getView().loadingUserExtSourcesStart();
			}
		});
	}

	@Override
	public void removeUserExtSource(final RichUserExtSource userExtSource) {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null) {
			placeManager.revealErrorPlace(request.getNameToken());
		} else {
			UsersManager.removeUserExtSource(userId, userExtSource.getUes().getId(), new JsonEvents() {

				@Override
				public void onFinished(JavaScriptObject result) {
					loadUserExtSources();
				}

				@Override
				public void onError(PerunException error) {
					getView().removingUserExtSourceError(error, userExtSource);
				}

				@Override
				public void onLoadingStart() {
					getView().removingUserExtSourceStart(userExtSource);
				}
			});
		}
	}

	@Override
	public void addUserExtSource() {
		Window.Location.assign(Utils.getIdentityConsolidatorLink(true));
	}
}
