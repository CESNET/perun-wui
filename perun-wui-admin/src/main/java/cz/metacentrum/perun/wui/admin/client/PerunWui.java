package cz.metacentrum.perun.wui.admin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
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
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AuthzManager;
import cz.metacentrum.perun.wui.json.managers.UtilsManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.html.Div;

/**
 * Entry point class of Wui. Handles URL changes, sets context and open pages based on that change.
 * <p/>
 * Provides access to all generic parts of Wui, like header, menu, content etc.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunWui implements EntryPoint, ValueChangeHandler<String> {

	interface PerunWuiUiBinder extends UiBinder<Widget, PerunWui> {}

	private static PerunWuiUiBinder uiBinder = GWT.create(PerunWuiUiBinder.class);

	@UiField(provided = true)
	Navbar navbar;

	@UiField(provided = true)
	Div menu;

	@UiField(provided = true)
	ContentManager content;

	private static boolean perunLoaded = false;
	private static boolean perunLoading = false;
	private static PerunLoader loader = new PerunLoader();
	private PerunWui gui = this;

	private static TopMenu topMenu;
	private static LeftMenu leftMenu;

	@Override
	public void onModuleLoad() {

		// ensure injecting custom CSS styles of PerunWui
		PerunResources.INSTANCE.gss().ensureInjected();

		// set default for Growl plugin
		Utils.getDefaultGrowlOptions().makeDefault();

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

						// CLEAR PAGE
						RootPanel.get().clear();

						// CREATE LAYOUT
						topMenu = new TopMenu();
						leftMenu = new LeftMenu();
						content = new ContentManager(topMenu, leftMenu);
						navbar = topMenu.getWidget();
						menu = leftMenu.getWidget();

						// store content manager
						PerunSession.getInstance().setContentManager(content);

						// BIND LAYOUT
						RootPanel.get().add(uiBinder.createAndBindUi(gui));

						// CONTENT IS RESIZABLE
						Scheduler.get().scheduleDeferred(new Command() {
							@Override
							public void execute() {

								if (Window.getClientWidth() <= 1200) {
									if (Window.getClientWidth() - 260 < 930) {
										content.setWidth("940px");
									}
								} else {
									content.setWidth(Window.getClientWidth() - 260 + "px");
								}

								// pass resizing to tab content
								content.onResize();

								Window.addResizeHandler(new ResizeHandler() {
									@Override
									public void onResize(ResizeEvent event) {

										if (Window.getClientWidth() <= 1200) {
											if (Window.getClientWidth() - 260 < 930) {
												content.setWidth("940px");
											}
										} else {
											content.setWidth(Window.getClientWidth() - 260 + "px");
										}

										// pass resizing to tab content
										content.onResize();

									}
								});

							}
						});

						perunLoaded = true;
						perunLoading = false;

						// OPEN PAGE BASED ON URL
						content.openPage(History.getToken());

					}

					@Override
					public void onError(PerunException error) {
						perunLoaded = false;
						perunLoading = false;
						loader.onError(error, null);
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
				loader.onError(error, null);
			}

			@Override
			public void onLoadingStart() {
				RootPanel.get().clear();
				RootPanel.get().add(loader);
				loader.onLoading();

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
				content.openPage(newToken);
			} else {
				// token is now correct - load it
				content.openPage(newToken);
			}

		} else {
			content.openPage(History.getToken());
		}

	}

}