package cz.metacentrum.perun.wui.profile.pages.settings.samba;

import com.google.gwt.core.client.JavaScriptObject;
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

import java.util.Date;
import java.util.Map;

/**
 * Presenter for settings SAMBA password
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class SambaPasswordPresenter extends Presenter<SambaPasswordPresenter.MyView, SambaPasswordPresenter.MyProxy> implements SambaPasswordUiHandlers {

	public interface MyView extends View, HasUiHandlers<SambaPasswordUiHandlers> {
		void onError(PerunException e);

		void setData(Map<String, JSONValue> data);

		void onLoadingStart();
	}

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	private Integer userId;

	private static final String ALT_PASSWORD_ATTRIBUTE_URN = "urn:perun:user:attribute-def:def:altPasswords:samba-du";

	private Attribute attribute;

	@NameToken(PerunProfilePlaceTokens.SETTINGS_SAMBA)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<SambaPasswordPresenter> {

	}

	@Inject
	public SambaPasswordPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
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
		loadSambaPassword();
	}

	@Override
	public void loadSambaPassword(){
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
	public void changeSambaPassword(String password, JsonEvents events) {
		UsersManager.createAltPassword(userId, String.valueOf(new Date().getTime()), "samba-du",password, events);
	}

	@Override
	public void navigateBack() {
		placeManager.navigateBack();
	}

}
