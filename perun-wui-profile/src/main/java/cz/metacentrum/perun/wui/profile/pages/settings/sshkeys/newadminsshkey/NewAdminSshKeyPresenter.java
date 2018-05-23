package cz.metacentrum.perun.wui.profile.pages.settings.sshkeys.newadminsshkey;

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
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AttributesManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.profile.client.PerunProfileUtils;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

public class NewAdminSshKeyPresenter extends Presenter<NewAdminSshKeyPresenter.MyView, NewAdminSshKeyPresenter.MyProxy> implements NewAdminSshKeyUiHandlers {

	private PlaceManager placeManager = PerunSession.getPlaceManager();


	public interface MyView extends View, HasUiHandlers<NewAdminSshKeyUiHandlers> {

		void setAddAdminSshKeyFinish();

		void setAddAdminSshKeyError(PerunException error);

		void resetView();
	}

	@NameToken(PerunProfilePlaceTokens.SETTINGS_SSH_NEWADMINKEY)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<NewAdminSshKeyPresenter> {

	}

	@Inject
	public NewAdminSshKeyPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		getView().resetView();
	}

	@Override
	public void addAdminSshKey(String value) {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {

			if (PerunSession.getInstance().isSelf(userId)) {
				AttributesManager.getUserAttribute(userId, PerunProfileUtils.A_U_DEF_ADMIN_SSH_KEYS, new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject result) {
						Attribute attribute = (Attribute)result;
						updateKeyAttribute(userId, attribute, value);
					}

					@Override
					public void onError(PerunException error) {
						getView().setAddAdminSshKeyError(error);
					}

					@Override
					public void onLoadingStart() {
						// do nothing
					}
				});
			}
		}
	}

	private void updateKeyAttribute(int userId, Attribute attribute, String value) {

		JsArrayString values = attribute.getValueAsJsArray();
		if (values == null) {
			values = (JsArrayString)JsArrayString.createArray();
			attribute.setValueAsJsArray(values);
		}
		values.push(value);
		AttributesManager.setUserAttribute(userId, attribute, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				getView().setAddAdminSshKeyFinish();
				Window.Location.assign("#" + PerunProfilePlaceTokens.getSettingsSshKeys());
			}

			@Override
			public void onError(PerunException error) {
				getView().setAddAdminSshKeyError(error);
			}

			@Override
			public void onLoadingStart() {
				// do nothing
			}
		});
	}

	@Override
	public void navigateBack() {
		placeManager.navigateBack();
	}
}
