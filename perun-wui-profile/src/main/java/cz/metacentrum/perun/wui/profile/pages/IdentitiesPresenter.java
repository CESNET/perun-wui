package cz.metacentrum.perun.wui.profile.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.model.beans.User;
import cz.metacentrum.perun.wui.model.beans.UserExtSource;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;

import java.util.List;

/**
 * Presenter for displaying registration detail.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class IdentitiesPresenter extends Presenter<IdentitiesPresenter.MyView, IdentitiesPresenter.MyProxy> implements IdentitiesUiHandlers {

	private List<UserExtSource> userExtSources;

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	public interface MyView extends View, HasUiHandlers<IdentitiesUiHandlers> {
		void loadingUserExtSourcesStart();
		void loadingUserExtSourcesError(PerunException ex);
		void setUserExtSources(List<UserExtSource> extSources);

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
		UsersManager.getUserExtSources(getUserId(), new JsonEvents() {

			@Override
			public void onFinished(JavaScriptObject result) {
				userExtSources = JsUtils.jsoAsList(result.cast());
				getView().setUserExtSources(userExtSources);
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

		UsersManager.removeUserExtSource(getUserId(), userExtSource.getId(), new JsonEvents() {

			@Override
			public void onFinished(JavaScriptObject result) {
				userExtSources.remove(userExtSource);
				getView().setUserExtSources(userExtSources);
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

	@Override
	public void addUserExtSource() {
		Window.Location.assign(Utils.getIdentityConsolidatorLink(true));
	}


	private Integer getUserId() {

		try {

			String userId = placeManager.getCurrentPlaceRequest().getParameter("id", null);
			if (userId == null) {
				userId = String.valueOf(PerunSession.getInstance().getUserId());
			}

			final int id = Integer.valueOf(userId);

			if (id < 1) {
				placeManager.revealErrorPlace(placeManager.getCurrentPlaceRequest().getNameToken());
			}

			return id;
		} catch (NumberFormatException e) {
			placeManager.revealErrorPlace(placeManager.getCurrentPlaceRequest().getNameToken());
		}
		return null;
	}

}
