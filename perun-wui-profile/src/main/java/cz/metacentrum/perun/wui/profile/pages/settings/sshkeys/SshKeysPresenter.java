package cz.metacentrum.perun.wui.profile.pages.settings.sshkeys;


import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
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

public class SshKeysPresenter extends Presenter<SshKeysPresenter.MyView, SshKeysPresenter.MyProxy> implements SshKeysUiHandlers {

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	public interface MyView extends View, HasUiHandlers<SshKeysUiHandlers> {

		void setSshKeys(Attribute attribute);

		void setAdminSshKeys(Attribute attribute);

		void setSshKeysError(PerunException error);

		void setAdminSshKeysError(PerunException error);

		void setSshKeysLoading();

		void setAdminSshKeysLoading();

		void setRemoveSshKeyError(PerunException error, int n);

		void setRemoveAdminSshKeyError(PerunException error, int n);
	}

	@NameToken(PerunProfilePlaceTokens.SETTINGS_SSH)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<SshKeysPresenter> {

	}

	@Inject
	public SshKeysPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		loadSshKeys();
		loadAdminSshKeys();
	}

	@Override
	public void removeSshKey(Attribute attribute, int n) {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			JsArrayString values = attribute.getValueAsJsArray();
			JsArrayString newValues = (JsArrayString)JsArrayString.createArray();
			for(int i = 0; i < values.length(); ++i) {
				String value = values.get(i);
				if (i != n) {
					newValues.push(value);
				}
			}
			attribute.setValueAsJsArray(newValues);

			AttributesManager.setUserAttribute(userId, attribute, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					loadSshKeys();
				}

				@Override
				public void onError(PerunException error) {
					getView().setRemoveSshKeyError(error, n);
				}

				@Override
				public void onLoadingStart() {
					// do nothing
				}
			});
		}
	}

	@Override
	public void removeAdminSshKey(Attribute attribute, int n) {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			JsArrayString values = attribute.getValueAsJsArray();
			JsArrayString newValues = (JsArrayString)JsArrayString.createArray();
			for(int i = 0; i < values.length(); ++i) {
				String value = values.get(i);
				if (i != n) {
					newValues.push(value);
				}
			}
			attribute.setValueAsJsArray(newValues);

			AttributesManager.setUserAttribute(userId, attribute, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					loadAdminSshKeys();
				}

				@Override
				public void onError(PerunException error) {
					getView().setRemoveAdminSshKeyError(error, n);
				}

				@Override
				public void onLoadingStart() {
					// do nothing
				}
			});
		}
	}

	@Override
	public void loadSshKeys() {

		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			AttributesManager.getUserAttribute(userId, PerunProfileUtils.A_U_DEF_SSH_KEYS, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					getView().setSshKeys((Attribute)result);
				}

				@Override
				public void onError(PerunException error) {
					getView().setSshKeysError(error);
				}

				@Override
				public void onLoadingStart() {
					getView().setSshKeysLoading();
				}
			});
		}
	}

	@Override
	public void loadAdminSshKeys() {

		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			AttributesManager.getUserAttribute(userId, PerunProfileUtils.A_U_DEF_ADMIN_SSH_KEYS, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					getView().setAdminSshKeys((Attribute)result);
				}

				@Override
				public void onError(PerunException error) {
					getView().setAdminSshKeysError(error);
				}

				@Override
				public void onLoadingStart() {
					getView().setAdminSshKeysLoading();
				}
			});
		}
	}
}
