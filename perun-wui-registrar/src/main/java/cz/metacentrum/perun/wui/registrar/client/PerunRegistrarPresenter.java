package cz.metacentrum.perun.wui.registrar.client;

import com.google.gwt.core.client.*;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunRegistrarPresenter extends PerunPresenter<PerunRegistrarPresenter.MyView, PerunRegistrarPresenter.MyProxy> {

	@ProxyStandard
	public interface MyProxy extends Proxy<PerunRegistrarPresenter> {
	}

	public interface MyView extends View {

		void onLoadingStartFooter();

		/**
		 * Show list of contact emails
		 * @param contactEmail null if there was an error meanwhile loading data List otherwise.
		 */
		void onFinishedFooter(List<String> contactEmail);

		void hideNavbar();
	}

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	@Inject
	PerunRegistrarPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void onBind() {
		super.onBind();

		loadVoAttributes(new Events<List<Attribute>>() {

			@Override
			public void onFinished(List<Attribute> result) {

				Attribute contactEmailAttr = null;
				for (Attribute attr : result) {
					if (attr.getFriendlyName().equals("contactEmail")) {
						contactEmailAttr = attr;
						break;
					}
				}
				List<String> contactEmails = new ArrayList<>();
				if (contactEmailAttr != null) {
					contactEmails = Arrays.asList(contactEmailAttr.getValue().split(","));
				}

				getView().onFinishedFooter(contactEmails);

				String i = Window.Location.getParameter("i");
				String m = Window.Location.getParameter("m");

				if (i != null && m != null) {

					PlaceRequest placeRequest = new PlaceRequest.Builder()
							.nameToken(RegistrarPlaceTokens.VERIFY)
							.with("i", i).with("m", m).build();
					placeManager.revealPlace(placeRequest, false);

				}

			}

			@Override
			public void onError(PerunException error) {
				getView().onFinishedFooter(null);
			}

			@Override
			public void onLoadingStart() {
				getView().onLoadingStartFooter();
			}
		});



	}

	private int resets = 0;
	@Override
	protected void onReset() {
		super.onReset();

		// Because NavbarCollapse.hide() doesn't work properly. It shows the NavbarCollapse on application startup .
		resets++;
		if (resets > 2) {
			getView().hideNavbar();
		}
	}

	private void loadVoAttributes(final Events<List<Attribute>> callback) {

		final String voName = Window.Location.getParameter("vo");

		if (voName == null || voName.isEmpty()) {
			PerunException exception = PerunException.createNew("0", "WrongURL", "Missing parameters in URL.");
			callback.onError(exception);
			return;
		}

		RegistrarManager.getVoAndGroupAttributes(voName, null, new JsonEvents() {

			@Override
			public void onFinished(JavaScriptObject jso) {
				List<Attribute> attributes = JsUtils.jsoAsList(jso);
				callback.onFinished(attributes);
			}

			@Override
			public void onError(PerunException error) {
				callback.onError(error);
			}

			@Override
			public void onLoadingStart() {
				callback.onLoadingStart();
			}
		});

	}

}
