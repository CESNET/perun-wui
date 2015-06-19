package cz.metacentrum.perun.wui.consolidator.client;

import com.google.gwt.core.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
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
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.html.Div;

/**
 * Entry point for Identity Consolidator application
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunConsolidator implements EntryPoint, ValueChangeHandler<String> {

	interface PerunConsolidatorUiBinder extends UiBinder<Widget, PerunConsolidator>{}

	private static PerunConsolidatorUiBinder uiBinder = GWT.create(PerunConsolidatorUiBinder.class);

	private static boolean perunLoaded = false;
	public static boolean perunLoading = false;
	private static PerunLoader guiLoader = new PerunLoader();
	private PerunConsolidator gui = this;

	private ConsolidatorTranslation translation = GWT.create(ConsolidatorTranslation.class);

	SelectPage page = new SelectPage();
	JoinPage joinPage = new JoinPage();

	@UiField NavbarHeader navbarHeader;

	@UiField(provided = true)
	Div content;

	@UiField AnchorListItem consolidateButton;

	@Override
	public void onModuleLoad() {

		// ensure injecting custom CSS styles of PerunWui
		PerunResources.INSTANCE.gss().ensureInjected();

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

						String token = Window.Location.getParameter("token");
						if (token == null || token.isEmpty()) {
							content = (Div)page.draw();
						} else {
							content = (Div)joinPage.draw(token);
						}

						RootPanel.get().clear();
						RootPanel.get().add(uiBinder.createAndBindUi(gui));

						// put logo
						Image logo = new Image(PerunResources.INSTANCE.getPerunLogo());
						logo.setWidth("auto");
						logo.setHeight("50px");
						navbarHeader.insert(logo, 0);

						consolidateButton.setText(translation.topConsolidateButton());

						// TRIGGER LOADING DEFAULT TABS
						perunLoaded = true;
						perunLoading = false;

					}

					@Override
					public void onError(PerunException error) {
						perunLoaded = false;
						perunLoading = false;
						guiLoader.onError(error, null);
					}

					@Override
					public void onLoadingStart() {

					}
				});

			}

			@Override
			public void onError(PerunException error) {
				perunLoaded = false;
				perunLoading = false;
				guiLoader.onError(error, null);
			}

			@Override
			public void onLoadingStart() {

				RootPanel.get().clear();
				RootPanel.get().add(guiLoader);
				guiLoader.onLoading();

				Scheduler.get().scheduleDeferred(new Command() {
					@Override
					public void execute() {
						guiLoader.getWidget().getElement().getFirstChildElement().setAttribute("style", "height: "+Window.getClientHeight()+"px;");
					}
				});

				perunLoaded = false;
				perunLoading = true;

			}
		});

	}

	@Override
	public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {

		// if GUI not loaded, change should force module loading
		if (!perunLoaded) {
			if (!perunLoading) onModuleLoad();
			return;
		}

		// when there is no token, default tabs are loaded
		// this is useful if user has bookmarked a site other than the homepage.
		if (History.getToken().isEmpty()) {

			// get whole URL
			String url = Window.Location.getHref();
			String newToken = "";

			int index = -1;

			if (url.contains("?locale=")) {
				// with locale
				index = url.indexOf("?", url.indexOf("?") + 1);
			} else {
				// without locale
				index = url.indexOf("?");
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