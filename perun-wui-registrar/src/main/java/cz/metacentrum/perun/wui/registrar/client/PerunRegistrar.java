package cz.metacentrum.perun.wui.registrar.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunContextListener;
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
import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Entry point class of Registrar wui. Handles URL changes, sets context and open pages based on that change.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunRegistrar implements EntryPoint, ValueChangeHandler<String>, PerunContextListener {

	interface PerunRegistrarUiBinder extends UiBinder<Widget, PerunRegistrar> {}

	private static PerunRegistrarUiBinder uiBinder = GWT.create(PerunRegistrarUiBinder.class);

	@UiField(provided = true)
	RegistrarContentManager content;

	public static boolean perunLoaded = false;
	private static boolean perunLoading = false;
	private static PerunLoader loader = new PerunLoader();
	private PerunRegistrar gui = this;

	private RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

	@UiField
	AnchorButton language;

	@UiField
	AnchorListItem czech;

	@UiField
	AnchorListItem english;

	@UiField
	AnchorListItem application;

	@UiField
	AnchorListItem myApplications;

	@UiField
	AnchorListItem help;

	@UiField
	AnchorListItem logout;

	@UiField
	static NavbarHeader navbarHeader;

	@UiHandler(value="czech")
	public void czechClick(ClickEvent event) {
		setLocale(Utils.getNativeLanguage().get(0));
	}

	@UiHandler(value="english")
	public void englishClick(ClickEvent event) {
		setLocale("en");
	}

	@UiHandler(value="application")
	public void applicationClick(ClickEvent event) {
		History.newItem("form");
	}

	@UiHandler(value="myApplications")
	public void myApplicationsClick(ClickEvent event) {
		History.newItem("submitted");
	}

	@UiHandler(value="help")
	public void helpClick(ClickEvent event) {
		History.newItem("help");
	}

	@UiHandler(value="logout")
	public void logoutClick(ClickEvent event) {
		History.newItem("logout");
	}

	/**
	 * Update localization of whole GUI (reset the app)
	 *
	 * @param locale Locale code to set
	 */
	public void setLocale(String locale) {
		UrlBuilder builder = Window.Location.createUrlBuilder().setParameter("locale", locale);
		Window.Location.replace(builder.buildString());
	}

	@Override
	public void setContext(String context) {

		if (context != null && !context.isEmpty()) {

			if ("form".equals(context)) {
				application.setActive(true);
				myApplications.setActive(false);
				help.setActive(false);
				logout.setActive(false);
			} else if ("submitted".equals(context)) {
				application.setActive(false);
				myApplications.setActive(true);
				help.setActive(false);
				logout.setActive(false);
			} else if ("help".equals(context)) {
				application.setActive(false);
				myApplications.setActive(false);
				help.setActive(true);
				logout.setActive(false);
			} else if ("logout".equals(context)) {
				application.setEnabled(false);
				myApplications.setEnabled(false);
				help.setEnabled(false);
				application.setActive(false);
				myApplications.setActive(false);
				help.setActive(false);
				logout.setActive(true);
			} else {
				application.setActive(false);
				myApplications.setActive(false);
				help.setActive(false);
				logout.setActive(false);
			}

		} else {
			setContext("form");
		}

		disableLinks();

	}

	@Override
	public void onModuleLoad() {

		// set default for Growl plugin
		Utils.getDefaultGrowlOptions().makeDefault();

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

						// CLEAR PAGE
						RootPanel.get().clear();

						content = new RegistrarContentManager(gui);
						PerunSession.getInstance().setContentManager(content);

						// BIND LAYOUT
						RootPanel.get().add(uiBinder.createAndBindUi(gui));

						// put logo
						Image logo = new Image(PerunResources.INSTANCE.getPerunLogo());
						logo.setWidth("auto");
						logo.setHeight("50px");
						navbarHeader.insert(logo, 0);

						// init buttons
						application.setText(translation.application());
						myApplications.setText(translation.myApplications());
						help.setText(translation.help());
						language.setText(translation.language());
						logout.setText(translation.logout());

						if ("default".equals(LocaleInfo.getCurrentLocale().getLocaleName()) ||
								"en".equalsIgnoreCase(LocaleInfo.getCurrentLocale().getLocaleName())) {
							// use english name of native language
							czech.setText(Utils.getNativeLanguage().get(2));
							english.setIcon(IconType.CHECK);
							english.setIconPosition(IconPosition.RIGHT);
							czech.setIcon(null);
						} else {
							// use native name of native language
							czech.setText(Utils.getNativeLanguage().get(1));
							czech.setIcon(IconType.CHECK);
							czech.setIconPosition(IconPosition.RIGHT);
							english.setIcon(null);
						}

						english.setText(translation.english());

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

				Scheduler.get().scheduleDeferred(new Command() {
					@Override
					public void execute() {
						loader.getWidget().getElement().getFirstChildElement().setAttribute("style", "height: "+Window.getClientHeight()+"px;");
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
		if (!perunLoaded && !History.getToken().equals("logout")) {
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

	/**
	 * Prevent default click action on links in menu. Href is present but ignored, only
	 * GWT's click handler is activated.
	 */
	private final native void disableLinks() /*-{
		$wnd.jQuery('ul.navbar-nav li a').click(function (e) {
			e.preventDefault();
		});
	}-*/;

	/*
	public static void setLogo(String url) {
		((Image)navbarHeader.getWidget(0)).setUrl(url);
	}
	*/

}