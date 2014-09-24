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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AuthzManager;
import cz.metacentrum.perun.wui.json.managers.PerunManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.pages.Page;
import cz.metacentrum.perun.wui.registrar.pages.FormPage;
import cz.metacentrum.perun.wui.registrar.pages.TranslatablePage;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Entry point class of Registrar wui. Handles URL changes, sets context and open pages based on that change.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunRegistrar implements EntryPoint, ValueChangeHandler<String> {

	interface PerunRegistrarUiBinder extends UiBinder<Widget, PerunRegistrar> {}

	private static PerunRegistrarUiBinder uiBinder = GWT.create(PerunRegistrarUiBinder.class);

	@UiField
	static Column content;

	private static boolean perunLoaded = false;
	private static PerunLoader loader = new PerunLoader();
	private PerunRegistrar gui = this;
	public static String LOCALE = getLang();

	private Page activePage = null;

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

	public void setLocale() {

		if (LOCALE.equals("cs")) {
			if (activePage != null) {
				((TranslatablePage) activePage).setLanguage("cs");
			}
			application.setText("Refistrační formulář");
			myApplications.setText("Moje registrace");
			help.setText("Pomoc");
			logout.setText("Odhlásit");
			english.setText("Anglicky");
			english.setIcon(null);
			czech.setText("Česky");
			czech.setIcon(IconType.CHECK);
			language.setText("Jazyk");
		} else {
			if (activePage != null) {
				((TranslatablePage) activePage).setLanguage("en");
			}
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

	}

	@Override
	public void onModuleLoad() {

		// ensure injecting custom CSS styles of PerunWui
		//PerunWuiCss.INSTANCE.css().ensureInjected();

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

						// BIND LAYOUT
						RootPanel.get().add(uiBinder.createAndBindUi(gui));

						perunLoaded = true;

						// OPEN PAGE BASED ON URL
						//content.openPage(History.getToken());
						activePage = new FormPage();
						content.clear();
						content.add(activePage.draw());

						setLocale();

					}

					@Override
					public void onError(PerunException error) {
						perunLoaded = false;
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
				//content.openPage(newToken);
				activePage = new FormPage();
				content.clear();
				content.add(activePage.draw());
			} else {
				// token is now correct - load it
				//content.openPage(newToken);
				activePage = new FormPage();
				content.clear();
				content.add(activePage.draw());
			}

		} else {
			//content.openPage(History.getToken());
			activePage = new FormPage();
			content.clear();
			content.add(activePage.draw());
		}

	}

	private static final native String getLang() /*-{

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

}