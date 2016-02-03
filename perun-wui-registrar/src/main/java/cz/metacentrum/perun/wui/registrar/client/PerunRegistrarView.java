package cz.metacentrum.perun.wui.registrar.client;

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
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunWebConstants;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.UiUtils;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.Well;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.List;
import java.util.Objects;

/**
 * Main View for Perun WUI Registrar.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunRegistrarView extends ViewImpl implements PerunRegistrarPresenter.MyView {

	interface PerunRegistrarViewUiBinder extends UiBinder<Widget, PerunRegistrarView> {}

	@UiField
	Div pageContent;

	private PerunRegistrarTranslation translation = GWT.create(PerunRegistrarTranslation.class);

	@UiField
	NavbarNav topMenu;

	@UiField
	NavbarCollapse collapse;

	@UiField
	FocusPanel collapseClickHandler;

	@UiField
	AnchorListItem application;

	@UiField
	AnchorListItem myApplications;

	@UiField
	AnchorListItem logout;

	@UiField static Span footerSupport;
	@UiField static Span footerCredits;
	@UiField static Span footerVersion;
	@UiField Well perunFooter;
	@UiField Div logoWrapper;

	@UiField static NavbarHeader navbarHeader;
	@UiField Span brand;

	@UiHandler(value="application")
	public void applicationClick(ClickEvent event) {
		History.newItem("form");
	}

	@UiHandler(value="myApplications")
	public void myApplicationsClick(ClickEvent event) {
		History.newItem("submitted");
	}

	@UiHandler(value="logout")
	public void logoutClick(ClickEvent event) {
		History.newItem("logout");
	}

	@Override
	public void onLoadingStartFooter() {

		if (PerunConfiguration.isFooterDisabled()) {
			Element elem = DOM.getElementById("perun-copyright");
			if (elem != null) {
				elem.setInnerHTML(translation.supportAt(PerunConfiguration.getBrandSupportMail()) + "<br />" + translation.credits(JsUtils.getCurrentYear()));
			}
		} else {
			footerSupport.setHTML(translation.supportAt(PerunConfiguration.getBrandSupportMail()));
			footerCredits.setHTML(translation.credits(JsUtils.getCurrentYear()));
			footerVersion.setHTML(translation.version(PerunWebConstants.INSTANCE.guiVersion()));
		}

	}

	@Override
	public void onFinishedFooter(List<String> contactEmail) {
		if (contactEmail == null || contactEmail.isEmpty()) {
			if (!PerunConfiguration.isFooterDisabled()) {
				footerSupport.setHTML(translation.supportAt(PerunConfiguration.getBrandSupportMail()));
			} else {
				Element elem = DOM.getElementById("perun-copyright");
				if (elem != null) {
					elem.setInnerHTML(translation.supportAt(PerunConfiguration.getBrandSupportMail()) + "<br />" + translation.credits(JsUtils.getCurrentYear()));
				}
			}
		} else {
			String mails = contactEmail.toString();
			if (!PerunConfiguration.isFooterDisabled()) {
				footerSupport.setHTML(translation.supportAt(mails.substring(1, mails.length()-1)));
			} else {
				Element elem = DOM.getElementById("perun-copyright");
				if (elem != null) {
					elem.setInnerHTML(translation.supportAt(mails.substring(1, mails.length() - 1)) + "<br />" + translation.credits(JsUtils.getCurrentYear()));
				}
			}
		}
	}

	@Override
	public void hideNavbar() {
		collapse.hide();
	}

	@Inject
	PerunRegistrarView(final PerunRegistrarViewUiBinder binder) {

		initWidget(binder.createAndBindUi(this));
		brand.setText(translation.registrarAppName());

		// put logo
		Image logo = PerunConfiguration.getBrandLogo();
		logo.setWidth("auto");
		logo.setHeight("50px");
		//logo.setPull(Pull.LEFT);
		logoWrapper.add(logo);

		UiUtils.addLanguageSwitcher(topMenu);

		// init buttons
		application.setText(translation.application());
		myApplications.setText(translation.myApplications());
		logout.setText(translation.logout());

		// fill perun properties to predefined footer
		perunFooter.setVisible(!PerunConfiguration.isFooterDisabled());

	}

	@Override
	public void setActiveMenuItem(String anchor) {

		int count = topMenu.getWidgetCount();
		for (int i=0; i < count; i++) {
			if (topMenu.getWidget(i) instanceof AnchorListItem) {
				AnchorListItem item = (AnchorListItem)topMenu.getWidget(i);
				if (Objects.equals(anchor, item.getTargetHistoryToken())) {
					item.setActive(true);
				} else {
					item.setActive(false);
				}
			}
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
