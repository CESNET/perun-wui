package cz.metacentrum.perun.wui.cabinet.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.cabinet.client.resources.PerunCabinetTranslation;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.UiUtils;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.Navbar;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.Objects;

/**
 * Main View for Perun Cabinet.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PerunCabinetView extends ViewImpl implements PerunCabinetPresenter.MyView {

	interface PerunCabinetViewUiBinder extends UiBinder<Widget, PerunCabinetView> {}

	@UiField static NavbarHeader navbarHeader;
	@UiField NavbarCollapse collapse;
	@UiField FocusPanel collapseClickHandler;
//	@UiField AnchorListItem topMenuMyPublications;
	@UiField NavbarNav topMenu;
	@UiField NavPills menuPills;
	@UiField Div pageContent;
	@UiField Div logoWrapper;
	@UiField Span brand;
	@UiField Navbar menuWrapper;
	@UiField AnchorListItem logout;
//	@UiField AnchorListItem personalXS;

	private PerunCabinetTranslation translations = GWT.create(PerunCabinetTranslation.class);

	@Inject
	PerunCabinetView(final PerunCabinetViewUiBinder binder) {

		initWidget(binder.createAndBindUi(this));

		init();
	}

	@UiHandler(value="logout")
	public void logoutClick(ClickEvent event) {
		History.newItem("logout");
	}

	@Override
	public void setActiveMenuItem(String anchor) {

		int count = menuPills.getWidgetCount();
		for (int i=0; i < count; i++) {
			if (menuPills.getWidget(i) instanceof AnchorListItem) {
				AnchorListItem item = (AnchorListItem)menuPills.getWidget(i);
				if (Objects.equals(anchor, item.getTargetHistoryToken())) {
					item.setActive(true);
				} else {
					item.setActive(false);
				}
			}
		}

	}


	private void init() {
		if (PerunConfiguration.isHeaderDisabled()) {
			menuWrapper.setVisible(false);
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
			elem.setInnerHTML(translations.supportAt(PerunConfiguration.getBrandSupportMail()));
		}
		Element elem2 = DOM.getElementById("perun-credits");
		if (elem2 != null) {
			elem2.setInnerHTML(translations.credits(JsUtils.getCurrentYear()));
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