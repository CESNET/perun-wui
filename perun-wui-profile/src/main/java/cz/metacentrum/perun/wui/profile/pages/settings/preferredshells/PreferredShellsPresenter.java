package cz.metacentrum.perun.wui.profile.pages.settings.preferredshells;


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

public class PreferredShellsPresenter extends Presenter<PreferredShellsPresenter.MyView, PreferredShellsPresenter.MyProxy> implements PreferredShellsUiHandlers {

	public interface MyView extends View, HasUiHandlers<PreferredShellsUiHandlers> {

		void setPreferredShells(Attribute shells);

		void setUpdatePreferredShellsStart();

		void setRemovePreferredShellError(PerunException error, int index);

		void setAddPreferredShellError(PerunException error);

		void setChangePreferredShellError(PerunException error, String newValue, int index);

		void setLoadPreferredShellsError(PerunException error);
	}

	private static final String A_U_D_PREFERRED_SHELLS = "urn:perun:user:attribute-def:def:preferredShells";

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	@NameToken(PerunProfilePlaceTokens.SETTINGS_PREFERREDSHELLS)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<PreferredShellsPresenter> {

	}

	@Inject
	public PreferredShellsPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		loadPreferredShells();
	}

	@Override
	public void loadPreferredShells() {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			AttributesManager.getUserAttribute(userId, A_U_D_PREFERRED_SHELLS, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					getView().setPreferredShells((Attribute) result);
				}

				@Override
				public void onError(PerunException error) {
					getView().setLoadPreferredShellsError(error);
				}

				@Override
				public void onLoadingStart() {
					getView().setUpdatePreferredShellsStart();
				}
			});
		}
	}

	@Override
	public void changePreferredShell(Attribute attribute, String newValue, int index) {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			JsArrayString values = attribute.getValueAsJsArray();
			JsArrayString newValues = (JsArrayString) JsArrayString.createArray();
			for (int i = 0; i < values.length(); ++i) {
				String value = values.get(i);
				if (i != index) {
					newValues.push(value);
				} else {
					newValues.push(newValue);
				}
			}
			attribute.setValueAsJsArray(newValues);

			AttributesManager.setUserAttribute(userId, attribute, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					loadPreferredShells();
				}

				@Override
				public void onError(PerunException error) {
					getView().setChangePreferredShellError(error, newValue, index);
				}

				@Override
				public void onLoadingStart() {
					getView().setUpdatePreferredShellsStart();
				}
			});
		}
	}

	@Override
	public void removePreferredShell(Attribute attribute, int index) {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			JsArrayString values = attribute.getValueAsJsArray();
			JsArrayString newValues = (JsArrayString) JsArrayString.createArray();
			for (int i = 0; i < values.length(); ++i) {
				String value = values.get(i);
				if (i != index) {
					newValues.push(value);
				}
			}
			attribute.setValueAsJsArray(newValues);

			AttributesManager.setUserAttribute(userId, attribute, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					loadPreferredShells();
				}

				@Override
				public void onError(PerunException error) {
					getView().setRemovePreferredShellError(error, index);
				}

				@Override
				public void onLoadingStart() {
					getView().setUpdatePreferredShellsStart();
				}
			});
		}
	}

	@Override
	public void addPreferredShell(Attribute attribute) {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			JsArrayString values = attribute.getValueAsJsArray();
			if (values == null) {
				values = (JsArrayString)JsArrayString.createArray();
			}
			values.push(PerunProfileUtils.SHELL_OPTIONS.get(0));
			attribute.setValueAsJsArray(values);

			AttributesManager.setUserAttribute(userId, attribute, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					loadPreferredShells();
				}

				@Override
				public void onError(PerunException error) {
					getView().setAddPreferredShellError(error);
				}

				@Override
				public void onLoadingStart() {
					getView().setUpdatePreferredShellsStart();
				}
			});
		}
	}

	@Override
	public void navigateBack() {
		placeManager.navigateBack();
	}
}
