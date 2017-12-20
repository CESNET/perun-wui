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
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.AbstractRepeatingJsonEvent;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AttributesManager;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.RichUserExtSource;
import cz.metacentrum.perun.wui.model.beans.UserExtSource;
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
		void setUserExtSources(List<RichUserExtSource> extSources);

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
		loadUserExtSources();
	}



	@Override
	public void loadUserExtSources() {
		Integer userId = Utils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null) {
			placeManager.revealErrorPlace(request.getNameToken());
		} else {
			UsersManager.getUserExtSources(userId, new JsonEvents() {

				@Override
				public void onFinished(JavaScriptObject result) {
					List<UserExtSource> userExtSources = JsUtils.jsoAsList(result);

					loadMails(userExtSources);
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

	private void mapAttributesAndUes(List<Attribute> attributes, List<UserExtSource> ueses) {
		List<RichUserExtSource> richUserExtSources = new ArrayList<>();
		if (attributes.size() != ueses.size()) {
			// hopefully should not happen
			getView().loadingUserExtSourcesError(null);
			return;
		}

		for (int i = 0; i < ueses.size(); i++) {
			RichUserExtSource rues = RichUserExtSource.mapUes(ueses.get(i), attributes.get(i));
			richUserExtSources.add(rues);
		}

		getView().setUserExtSources(richUserExtSources);
	}

	private void loadMails(List<UserExtSource> userExtSources) {

		AbstractRepeatingJsonEvent attributesEvent = new AbstractRepeatingJsonEvent(userExtSources.size()) {
			@Override
			public void finished(List<JavaScriptObject> results) {
				List<Attribute> attributes = JsUtils.jsListAsList(results);

				mapAttributesAndUes(attributes, userExtSources);
			}

			@Override
			public void erred(PerunException exception) {
				getView().loadingUserExtSourcesError(exception);
			}

			@Override
			public void started() {
				// do nothing
			}
		};

		for (UserExtSource ues : userExtSources) {
			AttributesManager.getUesAttribute(ues.getId(), "urn:perun:ues:attribute-def:def:mail", attributesEvent);
		}
	}

	@Override
	public void removeUserExtSource(final UserExtSource userExtSource) {
		Integer userId = Utils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null) {
			placeManager.revealErrorPlace(request.getNameToken());
		} else {
			UsersManager.removeUserExtSource(userId, userExtSource.getId(), new JsonEvents() {

				@Override
				public void onFinished(JavaScriptObject result) {
					getView().setUserExtSources(JsUtils.jsoAsList(result));
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
