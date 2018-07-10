package cz.metacentrum.perun.wui.setAffiliation.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.UiUtils;
import cz.metacentrum.perun.wui.setAffiliation.client.resources.PerunSetAffiliationTranslation;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.Navbar;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Span;

/**
 * Main View for Perun WUI Set Affiliation.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public class PerunSetAffiliationView extends ViewImpl implements PerunSetAffiliationPresenter.MyView{

	interface PerunSetAffiliationViewUiBinder extends UiBinder<Widget, PerunSetAffiliationView> {}

	@UiField Div pageContent;

	private PerunSetAffiliationTranslation translation = GWT.create(PerunSetAffiliationTranslation.class);

	@UiField Navbar menuWrapper;
	@UiField Span brand;
	@UiField Div logoWrapper;
	@UiField static NavbarHeader navbarHeader;

	@UiField NavbarNav topMenu;

	@UiField AnchorListItem logout;

	@UiHandler(value="logout")
	public void logoutClick(ClickEvent event) {
		History.newItem("logout");
	}

	@Inject
    PerunSetAffiliationView(final PerunSetAffiliationViewUiBinder binder) {

		initWidget(binder.createAndBindUi(this));

		if (PerunConfiguration.isHeaderDisabled()) {
			menuWrapper.setVisible(false);
		}

		// set Title from property if any
		if (PerunConfiguration.getBrandProfileTitle() != null) {
			brand.setText(PerunConfiguration.getBrandProfileTitle());
		}

		// put logo
		Image logo = PerunConfiguration.getBrandLogo();
		logo.setWidth("auto");
		logo.setHeight("50px");
		//logo.setPull(Pull.LEFT);
		String logoUrl = PerunConfiguration.getBrandLogoUrl();
		if (logoUrl == null) {
			logoWrapper.add(logo);
		} else {
			Anchor a = new Anchor(logoUrl);
			a.add(logo);
			logoWrapper.add(a);
		}

		if (!PerunConfiguration.isLangSwitchingDisabled()) {
			UiUtils.addLanguageSwitcher(topMenu);
		}


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
