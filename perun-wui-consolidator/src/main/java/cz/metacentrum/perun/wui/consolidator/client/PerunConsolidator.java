package cz.metacentrum.perun.wui.consolidator.client;

import com.google.gwt.core.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunResources;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.UiUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AuthzManager;
import cz.metacentrum.perun.wui.json.managers.UtilsManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.Pull;
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
	private static ConsolidatorTranslation translation = GWT.create(ConsolidatorTranslation.class);

	private static boolean perunLoaded = false;
	public static boolean perunLoading = false;
	private PerunConsolidator gui = this;
	@UiField static NavbarHeader navbarHeader;
	@UiField NavbarNav topMenu;

	SelectPage page;
	JoinPage joinPage;

	@UiField(provided = true)
	Div content;

	@UiField
	Span brand;

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

						String token = Window.Location.getParameter("token");
						if (token == null || token.isEmpty()) {
							content = (Div)page.draw();
						} else {
							content = (Div)joinPage.draw(token);
						}

						RootPanel.get("perun-consolidator").clear();
						RootPanel.get("perun-consolidator").add(uiBinder.createAndBindUi(gui));

						// put logo
						org.gwtbootstrap3.client.ui.Image logo = PerunConfiguration.getBrandLogo();
						logo.setWidth("auto");
						logo.setHeight("50px");
						logo.setPull(Pull.LEFT);
						navbarHeader.insert(logo, 0);

						brand.setText(translation.consolidatorAppName());

						UiUtils.addLanguageSwitcher(topMenu);

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

	}

}