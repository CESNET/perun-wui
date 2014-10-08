package cz.metacentrum.perun.wui.registrar.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunContextListener;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AuthzManager;
import cz.metacentrum.perun.wui.json.managers.PerunManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.pages.LogoutPage;
import cz.metacentrum.perun.wui.pages.Page;
import cz.metacentrum.perun.wui.client.resources.Translatable;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
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
	static RegistrarContentManager content;

	public static boolean perunLoaded = false;
	private static boolean perunLoading = false;
	private static PerunLoader loader = new PerunLoader();
	private PerunRegistrar gui = this;
	public static String LOCALE = getLang();

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
	NavbarHeader navbarHeader;

	@UiHandler(value="czech")
	public void czechClick(ClickEvent event) {
		LOCALE = "cs";
		setLocale();
	}

	@UiHandler(value="english")
	public void englishClick(ClickEvent event) {
		LOCALE = "en";
		setLocale();
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
	 * Update localization of whole GUI
	 */
	public void setLocale() {

		if (LOCALE.equals("cs")) {
			application.setText("Registrační formulář");
			myApplications.setText("Moje registrace");
			help.setText("Pomoc");
			logout.setText("Odhlásit");
			english.setText("Anglicky");
			english.setIcon(null);
			czech.setText("Česky");
			czech.setIcon(IconType.CHECK);
			language.setText("Jazyk");
		} else {
			application.setText("Registration form");
			myApplications.setText("My registrations");
			help.setText("Help");
			logout.setText("Logout");
			english.setText("English");
			english.setIcon(IconType.CHECK);
			english.setIconPosition(IconPosition.RIGHT);
			czech.setText("Czech");
			czech.setIcon(null);
			language.setText("Language");
		}

		if (content.getDisplayedPage() != null && content.getDisplayedPage() instanceof Translatable) {
			((Translatable)content.getDisplayedPage()).changeLanguage();
		}

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

		AuthzManager.getPerunPrincipal(new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject jso) {

				PerunPrincipal pp = ((PerunPrincipal) jso);

				PerunSession.getInstance().setPerunPrincipal(pp);
				PerunSession.getInstance().setRoles(pp.getRoles());

				// TODO - later load this setting from local storage too
				PerunSession.getInstance().setExtendedInfoVisible(PerunSession.getInstance().isPerunAdmin());

				History.addValueChangeHandler(gui);

				PerunManager.getGuiConfiguration(new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject jso) {

						// store configuration
						PerunSession.getInstance().setConfiguration((BasicOverlayObject) jso.cast());

						// CLEAR PAGE
						RootPanel.get().clear();

						content = new RegistrarContentManager(gui);

						// BIND LAYOUT
						RootPanel.get().add(uiBinder.createAndBindUi(gui));

						// put logo
						Image logo = new Image("PerunRegistrar/image/perun.png");
						logo.setWidth("235px");
						logo.setHeight("50px");
						navbarHeader.insert(logo, 0);

						perunLoaded = true;
						perunLoading = false;

						// OPEN PAGE BASED ON URL
						content.openPage(History.getToken());

						setLocale();

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
	 * Get default language from browser
	 *
	 * @return language string
	 */
	public static final native String getLang() /*-{

		var l_lang = "en";
		if (navigator.userLanguage) // Explorer
			l_lang = navigator.userLanguage;
		else if (navigator.language) // FF
			l_lang = navigator.language;
		else
			l_lang = "en";

		return l_lang;

		//$wnd.jQuery("meta[name='gwt:property']").attr('content', 'locale='+l_lang);

	}-*/;

	/**
	 * Prevent default click action on links in menu. Href is present but ignored, only
	 * GWT's click handler is activated.
	 */
	private final native void disableLinks() /*-{
		$wnd.jQuery('ul.navbar-nav li a').click(function (e) {
			e.preventDefault();
		});
	}-*/;

}