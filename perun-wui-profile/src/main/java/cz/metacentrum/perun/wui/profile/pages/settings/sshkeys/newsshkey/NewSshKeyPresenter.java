package cz.metacentrum.perun.wui.profile.pages.settings.sshkeys.newsshkey;


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

public class NewSshKeyPresenter extends Presenter<NewSshKeyPresenter.MyView, NewSshKeyPresenter.MyProxy> implements NewSshKeyUiHandlers {

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	public interface MyView extends View, HasUiHandlers<NewSshKeyUiHandlers> {

		void setAddSshKeyFinish();

		void setAddSshKeyError(PerunException error);

		void resetView();
	}

	@NameToken(PerunProfilePlaceTokens.SETTINGS_SSH_NEWKEY)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<NewSshKeyPresenter> {

	}

	@Inject
	public NewSshKeyPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		getView().resetView();
	}

	@Override
	public void addSshKey(String value) {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {

			if (PerunSession.getInstance().isSelf(userId)) {

				AttributesManager.getUserAttribute(userId, PerunProfileUtils.A_U_DEF_SSH_KEYS, new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject result) {
						Attribute attribute = (Attribute)result;
						updateKeyAttribute(userId, attribute, value);
					}

					@Override
					public void onError(PerunException error) {
						getView().setAddSshKeyError(error);
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
				getView().setAddSshKeyFinish();
				Window.Location.assign("#" + PerunProfilePlaceTokens.getSettingsSshKeys());
			}

			@Override
			public void onError(PerunException error) {
				getView().setAddSshKeyError(error);
			}

			@Override
			public void onLoadingStart() {
				// do nothing
			}
		});
	}
}
