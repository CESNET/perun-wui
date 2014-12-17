package cz.metacentrum.perun.wui.cabinet.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import cz.metacentrum.perun.wui.client.resources.PerunResources;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AuthzManager;
import cz.metacentrum.perun.wui.json.managers.UtilsManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Navbar;
import org.gwtbootstrap3.client.ui.NavbarHeader;

/**
 * Entry point for Cabinet application
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunCabinet implements EntryPoint, ValueChangeHandler<String> {

	interface PerunCabinetUiBinder extends UiBinder<Widget, PerunCabinet>{}

	private static PerunCabinetUiBinder uiBinder = GWT.create(PerunCabinetUiBinder.class);

	private static boolean perunLoaded = false;
	private static PerunLoader guiLoader = new PerunLoader();
	private PerunCabinet gui = this;

	@UiField Navbar navbar;
	@UiField NavbarHeader navbarHeader;

	@Override
	public void onModuleLoad() {

		// ensure injecting custom CSS styles of PerunWui
		PerunResources.INSTANCE.css().ensureInjected();

		AuthzManager.getPerunPrincipal(new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject jso) {

				PerunPrincipal pp = ((PerunPrincipal) jso);

				PerunSession.getInstance().setPerunPrincipal(pp);
				PerunSession.getInstance().setRoles(pp.getRoles());

				// TODO - later load this setting from local storage too
				PerunSession.getInstance().setExtendedInfoVisible(PerunSession.getInstance().isPerunAdmin());

				History.addValueChangeHandler(gui);

				UtilsManager.getGuiConfiguration(new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject jso) {

						// store configuration
						PerunSession.getInstance().setConfiguration((BasicOverlayObject) jso.cast());

						RootLayoutPanel.get().clear();
						RootLayoutPanel.get().add(uiBinder.createAndBindUi(gui));

						Image logo = new Image(PerunResources.INSTANCE.getPerunLogo());
						logo.setWidth("auto");
						logo.setHeight("65px");
						navbarHeader.insert(logo, 0);

						// TRIGGER LOADING DEFAULT TABS
						//PerunCabinet.getContent().openTab(History.getToken());

						perunLoaded = true;

					}

					@Override
					public void onError(PerunException error) {
						perunLoaded = false;
						//guiLoader.onError(error);
					}

					@Override
					public void onLoadingStart() {

					}
				});

			}

			@Override
			public void onError(PerunException error) {
				perunLoaded = false;
				//guiLoader.onError(error);
			}

			@Override
			public void onLoadingStart() {
				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(guiLoader.getWidget());

				Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand() {
					@Override
					public boolean execute() {
						if (guiLoader.getProgressBar().getPercent() <= 100) {
							guiLoader.getProgressBar().setPercent(guiLoader.getProgressBar().getPercent() + 1);
							return true;
						}
						return false;
					}
				}, 200);

				perunLoaded = false;
			}
		});

	}

	@Override
	public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {

		// if GUI not loaded, change should force module loading
		if (!perunLoaded) {
			onModuleLoad();
			return;
		}

		// when there is no token, default tabs are loaded
		// this is useful if user has bookmarked a site other than the homepage.
		if (History.getToken().isEmpty()) {

			// get whole URL
			String url = Window.Location.getHref();
			String newToken = "";

			int index = -1;

			if (url.contains("?gwt.codesvr=127.0.0.1:9997")) {
				// if local devel build

				if (url.contains("?locale=")) {
					// with locale
					index = url.indexOf("?", url.indexOf("?", url.indexOf("?")) + 1);
				} else {
					// without locale
					index = url.indexOf("?", url.indexOf("?") + 1);
				}
			} else {
				// if production build
				if (url.contains("?locale=")) {
					// with locale
					index = url.indexOf("?", url.indexOf("?") + 1);
				} else {
					// without locale
					index = url.indexOf("?");
				}
			}

			if (index != -1) {
				newToken = url.substring(index + 1);
			}

			// will sort of break URL, but will work without refreshing whole GUI
			if (newToken.isEmpty()) {
				// token was empty anyway - load default
				//PerunCabinet.getContent().openTab(newToken);

			} else {
				// token is now correct - load it
				History.newItem(newToken);
			}

		} else {
			//PerunCabinet.getContent().openTab(History.getToken());
		}

	}

}