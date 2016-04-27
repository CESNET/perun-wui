package cz.metacentrum.perun.wui.pwdreset.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.UiUtils;
import cz.metacentrum.perun.wui.pwdreset.client.resources.PerunPwdResetTranslation;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Span;

/**
 * Main View for Perun WUI Registrar.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunPwdResetView extends ViewImpl implements PerunPwdResetPresenter.MyView {

	interface PerunPwdResetViewUiBinder extends UiBinder<Widget, PerunPwdResetView> {}

	@UiField
	Div pageContent;

	private PerunPwdResetTranslation translation = GWT.create(PerunPwdResetTranslation.class);

	@UiField
	NavbarNav topMenu;

	@UiField
	NavbarCollapse collapse;

	@UiField
	FocusPanel collapseClickHandler;

	@UiField
	AnchorListItem logout;

	@UiField Div logoWrapper;

	@UiField static NavbarHeader navbarHeader;
	@UiField Span brand;

	private boolean isAccountActivation = Window.Location.getParameterMap().containsKey("activation");

	@UiHandler(value="logout")
	public void logoutClick(ClickEvent event) {
		History.newItem("logout");
	}


	@Override
	public void hideNavbar() {
		collapse.hide();
	}

	@Inject
	PerunPwdResetView(final PerunPwdResetViewUiBinder binder) {

		initWidget(binder.createAndBindUi(this));

		brand.setText((isAccountActivation) ? translation.activateAppName() : translation.pwdresetAppName());

		// put logo
		Image logo = PerunConfiguration.getBrandLogo();
		logo.setWidth("auto");
		logo.setHeight("50px");
		//logo.setPull(Pull.LEFT);
		logoWrapper.add(logo);

		if (!PerunConfiguration.isLangSwitchingDisabled()) {
			UiUtils.addLanguageSwitcher(topMenu);
		}

		logout.setText(translation.logout());

		// init buttons
		if (PerunSession.getInstance().getRpcServer().equals("non")) {
			logout.setVisible(false);
		}

		Element elem = DOM.getElementById("perun-help");
		if (elem != null) {
			elem.setInnerHTML(translation.supportAt(PerunConfiguration.getBrandSupportMail()));
		}
		Element elem2 = DOM.getElementById("perun-credits");
		if (elem2 != null) {
			elem2.setInnerHTML(translation.credits(JsUtils.getCurrentYear()));
		}

	}

	@Override
	public void setInSlot(final Object slot, final IsWidget content) {
		if (slot == PerunPresenter.SLOT_MAIN_CONTENT) {
			pageContent.clear();
			if (content != null) {
				pageContent.add(content);
			}
		} else {
			super.setInSlot(slot, content);
		}
	}

}
