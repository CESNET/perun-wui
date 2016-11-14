package cz.metacentrum.perun.wui.profile.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
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
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Presenter for displaying personal info about user
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PersonalPresenter extends Presenter<PersonalPresenter.MyView, PersonalPresenter.MyProxy> implements PersonalUiHandlers {

	private RichUser user;

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	public interface MyView extends View, HasUiHandlers<PersonalUiHandlers> {
		void setUser(RichUser user);
		void loadingUserStart();
		void loadingUserError(PerunException ex);

		void checkingEmailUpdatesStart();
		void checkingEmailUpdatesError(PerunException ex);
		void setEmailUpdateRequests(List<String> pendingEmails);

		void requestingEmailUpdateStart();
		void requestingEmailUptadeError(PerunException ex, String email);
	}

	@NameToken(PerunProfilePlaceTokens.PERSONAL)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<PersonalPresenter> {
	}

	@Inject
	public PersonalPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		loadUser();
		checkEmailRequestPending();
	}

	@Override
	public void loadUser() {

		final int id = getUserId();

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (id < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
		}

		if (PerunSession.getInstance().isSelf(id)) {

			UsersManager.getRichUserWithAttributes(id, new JsonEvents() {

				@Override
				public void onFinished(JavaScriptObject jso) {
					user = jso.cast();
					getView().setUser(user);
				}

				@Override
				public void onError(PerunException error) {
					if (error.getName().equals("PrivilegeException")) {
						placeManager.revealUnauthorizedPlace(request.getNameToken());
					} else {
						getView().loadingUserError(error);
					}
				}

				@Override
				public void onLoadingStart() {
					getView().loadingUserStart();
				}
			});

		} else {
			placeManager.revealUnauthorizedPlace(request.getNameToken());
		}

	}

	@Override
	public void checkEmailRequestPending() {

		final int id = getUserId();

		UsersManager.getPendingPreferredEmailChanges(id, new JsonEvents() {

			@Override
			public void onFinished(JavaScriptObject jso) {
				List<String> pendingEmails = JsUtils.listFromJsArrayString((JsArrayString) jso.cast());
				getView().setEmailUpdateRequests(pendingEmails);
			}

			@Override
			public void onError(PerunException error) {
				getView().checkingEmailUpdatesError(error);
			}

			@Override
			public void onLoadingStart() {
				getView().checkingEmailUpdatesStart();
			}
		});

	}


	@Override
	public void updateEmail(final String email) {

		if (!Utils.isValidEmail(email)) {
			getView().requestingEmailUptadeError(null, email);
			return;
		}

		UsersManager.requestPreferredEmailChange(user.getId(), email, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				List<String> pendingEmails = JsUtils.listFromJsArrayString((JsArrayString) result.cast());
				getView().setEmailUpdateRequests(pendingEmails);
			}

			@Override
			public void onError(PerunException error) {
				getView().requestingEmailUptadeError(error, email);
			}

			@Override
			public void onLoadingStart() {
				getView().requestingEmailUpdateStart();
			}
		});

	}

	private Integer getUserId() {

		try {

			String userId = placeManager.getCurrentPlaceRequest().getParameter("id", null);
			if (userId == null) {
				userId = String.valueOf(PerunSession.getInstance().getUserId());
			}

			final int id = Integer.valueOf(userId);

			if (id < 1) {
				if (PerunConfiguration.getBrandProfileUnknownUrl() != null) {
					Window.Location.assign(PerunConfiguration.getBrandProfileUnknownUrl());
				} else {
					placeManager.revealErrorPlace(placeManager.getCurrentPlaceRequest().getNameToken());
				}
			}

			return id;
		} catch (NumberFormatException e) {
			placeManager.revealErrorPlace(placeManager.getCurrentPlaceRequest().getNameToken());
		}
		return null;
	}


}
