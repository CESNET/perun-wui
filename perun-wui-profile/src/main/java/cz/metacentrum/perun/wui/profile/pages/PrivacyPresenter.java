package cz.metacentrum.perun.wui.profile.pages;


import com.google.gwt.core.client.JavaScriptObject;
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
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PrivacyPresenter extends Presenter<PrivacyPresenter.MyView, PrivacyPresenter.MyProxy> implements PrivacyUiHandlers {

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	public static final String ATTRIBUTE_NAME = "urn:perun:vo:attribute-def:def:applicationURL";

	public interface MyView extends View, HasUiHandlers<PrivacyUiHandlers> {

		void setVosWithAttribute(Map<Vo, Attribute> vosWithAttribute);

		void setLoadingError(PerunException e);

		void setLoadingStart();
	}

	@NameToken(PerunProfilePlaceTokens.PRIVACY)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<PrivacyPresenter> {

	}

	@Inject
	public PrivacyPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		loadVosData();
	}

	@Override
	public void loadVosData() {
		final Integer userId = Utils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			loadVos(userId);
		}
	}

	private void loadVos(int userId) {

		UsersManager.getVosWhereUserIsMember(userId, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				List<Vo> vos = JsUtils.jsoAsList(result);
				loadAttributes(userId, vos);
			}

			@Override
			public void onError(PerunException error) {
				getView().setLoadingError(error);
			}

			@Override
			public void onLoadingStart() {
				getView().setLoadingStart();
			}
		});
	}

	private void loadAttributes(int userId, List<Vo> vos) {

		AbstractRepeatingJsonEvent attributesEvent = new AbstractRepeatingJsonEvent(vos.size()) {
			@Override
			public void finished(List<JavaScriptObject> results) {
				List<Attribute> attributes = JsUtils.jsListAsList(results);

				Map<Vo, Attribute> vosWithAttribute = new HashMap<>();
				for (int i = 0; i < vos.size(); i++) {
					vosWithAttribute.put(vos.get(i), attributes.get(i));
				}

				getView().setVosWithAttribute(vosWithAttribute);
			}

			@Override
			public void erred(PerunException exception) {
				getView().setLoadingError(exception);
			}

			@Override
			public void started() {}
		};

		for (Vo vo : vos) {
			AttributesManager.getVoAttribute(vo.getId(), ATTRIBUTE_NAME, attributesEvent);
		}
	}
}