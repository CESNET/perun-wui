package cz.metacentrum.perun.wui.profile.pages;

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
import cz.metacentrum.perun.wui.client.utils.UiUtils;
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

import java.util.List;

/**
 * Presenter for displaying registration detail.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class IdentitiesPresenter extends Presenter<IdentitiesPresenter.MyView, IdentitiesPresenter.MyProxy> implements IdentitiesUiHandlers {

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	private static final String EMAIL_ATTRIBUTE = "urn:perun:ues:attribute-def:def:mail";

	public interface MyView extends View, HasUiHandlers<IdentitiesUiHandlers> {
		void loadingUserExtSourcesStart();
		void loadingUserExtSourcesError(PerunException ex);

		void addUserExtSource(RichUserExtSource extSource);
		void clearUserExtSources();

		void removingUserExtSourceStart(UserExtSource userExtSource);
		void removingUserExtSourceError(PerunException ex, UserExtSource userExtSource);
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
						loadEmail(ues);
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

	private void loadEmail(UserExtSource ues) {
		AttributesManager.getUesAttribute(ues.getId(), EMAIL_ATTRIBUTE, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				Attribute emailAttr = (Attribute) result;
				RichUserExtSource rues = RichUserExtSource.mapUes(ues, emailAttr);
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
	public void removeUserExtSource(final UserExtSource userExtSource) {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null) {
			placeManager.revealErrorPlace(request.getNameToken());
		} else {
			UsersManager.removeUserExtSource(userId, userExtSource.getId(), new JsonEvents() {

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
