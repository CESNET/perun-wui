package cz.metacentrum.perun.wui.profile.pages.settings.altPasswords;


import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dev.json.JsonValue;
import com.google.gwt.json.client.JSONValue;
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
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.profile.client.PerunProfileUtils;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

import java.util.Map;


public class AltPasswordsPresenter extends Presenter<AltPasswordsPresenter.MyView, AltPasswordsPresenter.MyProxy> implements AltPasswordsUiHandlers {

	public interface MyView extends View, HasUiHandlers<AltPasswordsUiHandlers> {
		void onError(PerunException e);

		void setData(Map<String, JSONValue> data);

		void onLoadingStart();

		void showGeneratedPassword(String password);
	}

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	private Integer userId;

	private static final String ALT_PASSWORD_ATTRIBUTE_URN = "urn:perun:user:attribute-def:def:altPasswords:einfra";

	private Attribute attribute;

	@NameToken(PerunProfilePlaceTokens.SETTINGS_ALTPASSWORDS)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<AltPasswordsPresenter> {

	}

	@Inject
	public AltPasswordsPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);

		PlaceManager placeManager = PerunSession.getPlaceManager();
		userId = PerunProfileUtils.getUserId(placeManager);
		final PlaceRequest request = placeManager.getCurrentPlaceRequest();
		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
		}
	}

	@Override
	protected void onReveal(){
		loadAltPasswords();
	}

	@Override
	public void loadAltPasswords(){
		AttributesManager.getUserAttribute(userId, ALT_PASSWORD_ATTRIBUTE_URN, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				attribute = (Attribute)result;
				getView().setData(attribute.getValueAsMap());
			}

			@Override
			public void onError(PerunException error) {
				getView().onError(error);
			}

			@Override
			public void onLoadingStart() { getView().onLoadingStart(); }
		});
	}

	@Override
	public void newAltPassoword(String description, String password) {
		UsersManager.createAltPassword(userId, description, "einfra",password, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				loadAltPasswords();
				getView().showGeneratedPassword(password);
			}

			@Override
			public void onError(PerunException error) {
				getView().onError(error);
			}

			@Override
			public void onLoadingStart() {
				getView().onLoadingStart();
			}
		});
	}

	@Override
	public void deleteAltPassword(String passwordId) {
		String id = passwordId.replaceAll("\"","");
		UsersManager.deleteAltPassword(userId, "einfra", id, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				loadAltPasswords();
			}

			@Override
			public void onError(PerunException error) {
				getView().onError(error);
			}

			@Override
			public void onLoadingStart() {
				getView().onLoadingStart();
			}
		});
	}

	@Override
	public void navigateBack() {
		placeManager.navigateBack();
	}

	@Override
	public boolean descriptionIsUsed(String description){
		return attribute.getValueAsMap().containsKey(description);
	}
}
