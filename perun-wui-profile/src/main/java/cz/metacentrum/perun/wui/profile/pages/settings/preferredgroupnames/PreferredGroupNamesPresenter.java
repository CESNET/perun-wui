package cz.metacentrum.perun.wui.profile.pages.settings.preferredgroupnames;


import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.ClickHandler;
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
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AttributesManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.profile.client.PerunProfileUtils;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

import java.util.List;

public class PreferredGroupNamesPresenter extends Presenter<PreferredGroupNamesPresenter.MyView, PreferredGroupNamesPresenter.MyProxy> implements PreferredGroupNamesUiHandlers {

	public interface MyView extends View, HasUiHandlers<PreferredGroupNamesUiHandlers> {

		void setPreferredGroupNamesAttributes(List<Attribute> attributes);

		void setUpdating();

		void setError(PerunException error, ClickHandler retryAction);
	}

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	@NameToken(PerunProfilePlaceTokens.SETTINGS_PREFERREDGROUPNAMES)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<PreferredGroupNamesPresenter> {

	}

	@Inject
	public PreferredGroupNamesPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		loadPreferredGroupNames();
	}

	@Override
	public void loadPreferredGroupNames() {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			List<String> namespaces = PerunProfileUtils.getPreferredGroupNamesAttrNames();
			AttributesManager.getUserAttributes(userId, namespaces, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					List<Attribute> attributes = JsUtils.jsoAsList(result);
					getView().setPreferredGroupNamesAttributes(attributes);
				}

				@Override
				public void onError(PerunException error) {
					getView().setError(error, clickEvent -> loadPreferredGroupNames());
				}

				@Override
				public void onLoadingStart() {
					getView().setUpdating();
				}
			});
		}
	}

	@Override
	public void changePreferredName(Attribute attribute, String newValue, int index) {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			JsArrayString values = attribute.getValueAsJsArray();
			JsArrayString newValues = JsArrayString.createArray().cast();
			for (int i = 0; i < values.length(); i++) {
				if (index == i) {
					if (newValue != null && !newValue.isEmpty()) {
						newValues.push(newValue);
					}
				} else {
					newValues.push(values.get(i));
				}
			}
			attribute.setValueAsJsArray(newValues);
			AttributesManager.setUserAttribute(userId, attribute, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					loadPreferredGroupNames();
				}

				@Override
				public void onError(PerunException error) {
					getView().setError(error, clickEvent -> changePreferredName(attribute, newValue, index));
				}

				@Override
				public void onLoadingStart() {
					getView().setUpdating();
				}
			});
		}
	}

	@Override
	public void newPreferredName(Attribute namespaceAttribute, String value) {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			JsArrayString values = namespaceAttribute.getValueAsJsArray();
			if (values == null) {
				values = JsArrayString.createArray().cast();
			}
			values.push(value);
			namespaceAttribute.setValueAsJsArray(values);

			AttributesManager.setUserAttribute(userId, namespaceAttribute, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					loadPreferredGroupNames();
				}

				@Override
				public void onError(PerunException error) {
					getView().setError(error, clickEvent -> newPreferredName(namespaceAttribute, value));
				}

				@Override
				public void onLoadingStart() {
					getView().setUpdating();
				}
			});
		}
	}

	@Override
	public void navigateBack() {
		placeManager.navigateBack();
	}
}
