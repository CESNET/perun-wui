package cz.metacentrum.perun.wui.consolidator.client;

import com.google.gwt.core.client.*;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import cz.metacentrum.perun.wui.client.resources.ExceptionLogger;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunResources;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.UiUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.consolidator.client.resources.PerunConsolidatorResources;
import cz.metacentrum.perun.wui.consolidator.client.resources.PerunConsolidatorTranslation;
import cz.metacentrum.perun.wui.consolidator.pages.JoinPage;
import cz.metacentrum.perun.wui.consolidator.pages.SelectPage;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AuthzManager;
import cz.metacentrum.perun.wui.json.managers.UtilsManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Span;

/**
 * Entry point for Identity Consolidator application
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunConsolidator implements EntryPoint{

	interface PerunConsolidatorUiBinder extends UiBinder<Widget, PerunConsolidator>{}

	private static PerunConsolidatorUiBinder uiBinder = GWT.create(PerunConsolidatorUiBinder.class);
	private static PerunConsolidatorTranslation translation = GWT.create(PerunConsolidatorTranslation.class);

	private static boolean perunLoaded = false;
	public static boolean perunLoading = false;
	private PerunConsolidator gui = this;
	@UiField Div menuWrapper;
	@UiField Span brand;
	@UiField Div logoWrapper;
	@UiField static NavbarHeader navbarHeader;
	@UiField NavbarNav topMenu;

	SelectPage page;
	JoinPage joinPage;

	@UiField Column content;

	@Override
	public void onModuleLoad() {

		ExceptionLogger exceptionHandler = new ExceptionLogger();
		GWT.setUncaughtExceptionHandler(exceptionHandler);

		try {

			// set default for Growl plugin
			Utils.getDefaultNotifyOptions().makeDefault();

			// ensure injecting custom CSS styles of PerunWui
			PerunResources.INSTANCE.gss().ensureInjected();

			PerunConsolidatorResources.INSTANCE.gss().ensureInjected();

			AuthzManager.getPerunPrincipal(new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject jso) {

					PerunPrincipal pp = ((PerunPrincipal) jso);
					PerunSession.getInstance().setPerunPrincipal(pp);
					PerunSession.getInstance().setRoles(pp.getRoles());

					PerunSession.getInstance().setExtendedInfoVisible(PerunSession.getInstance().isPerunAdmin());

					UtilsManager.getGuiConfiguration(new JsonEvents() {
						@Override
						public void onFinished(JavaScriptObject jso) {

							// store configuration
							PerunConfiguration.setPerunConfig(((BasicOverlayObject) jso.cast()));

							// TRIGGER LOADING DEFAULT TABS
							perunLoaded = true;
							perunLoading = false;

							page = new SelectPage();
							joinPage = new JoinPage();

							RootPanel.get("app-content").clear();
							RootPanel.get("app-content").add(uiBinder.createAndBindUi(gui));

							String token = Window.Location.getParameter("token");
							if (token == null || token.isEmpty()) {
								content.add(page.draw());
							} else {
								content.add(joinPage.draw(token));
							}

							// put logo
							Image logo = PerunConfiguration.getBrandLogo();
							logo.setWidth("auto");
							logo.setHeight("50px");
							//logo.setPull(Pull.LEFT);
							logoWrapper.add(logo);

							if (!PerunConfiguration.isLangSwitchingDisabled()) {
								UiUtils.addLanguageSwitcher(topMenu);
							}

							brand.setText(translation.appName());

							Element elem = DOM.getElementById("perun-help");
							if (elem != null) {
								elem.setInnerHTML(translation.supportAt(SafeHtmlUtils.fromString(PerunConfiguration.getBrandSupportMail()).asString()));
							}
							Element elem2 = DOM.getElementById("perun-credits");
							if (elem2 != null) {
								elem2.setInnerHTML(translation.credits(JsUtils.getCurrentYear()));
							}

						}

						@Override
						public void onError(PerunException error) {
							perunLoaded = false;
							perunLoading = false;
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
				}

				@Override
				public void onLoadingStart() {
					perunLoaded = false;
					perunLoading = true;

				}
			});

		} catch (Exception ex) {
			exceptionHandler.onUncaughtException(ex);
		}

	}

}
