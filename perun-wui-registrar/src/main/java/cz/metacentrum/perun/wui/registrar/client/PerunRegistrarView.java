package cz.metacentrum.perun.wui.registrar.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
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
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.UiUtils;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.NavbarNav;
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

		Element elem = DOM.getElementById("perun-help");
		if (elem != null) {
			elem.setInnerHTML(" "+translation.supportAt(SafeHtmlUtils.fromString(PerunConfiguration.getBrandSupportMail()).asString()));
		}
		Element elem2 = DOM.getElementById("perun-credits");
		if (elem2 != null) {
			elem2.setInnerHTML(translation.credits(JsUtils.getCurrentYear()));
		}

		//translation.version(PerunWebConstants.INSTANCE.guiVersion();

	}

	@Override
	public void onFinishedFooter(List<String> contactEmail) {
		if (contactEmail == null || contactEmail.isEmpty()) {

			Element elem = DOM.getElementById("perun-help");
			if (elem != null) {
				elem.setInnerHTML(" "+translation.supportAt(SafeHtmlUtils.fromString(PerunConfiguration.getBrandSupportMail()).asString()));
			}
			Element elem2 = DOM.getElementById("perun-credits");
			if (elem2 != null) {
				elem2.setInnerHTML(translation.credits(JsUtils.getCurrentYear()));
			}

		} else {

			Element elem = DOM.getElementById("perun-help");
			if (elem != null) {
				elem.setInnerHTML(translation.supportAtMails());
				for (String mail : contactEmail) {
					Anchor mailto = new Anchor();
					mailto.setText(SafeHtmlUtils.fromString(mail+" ").asString());
					mailto.setHref("mailto:"+mail);
					elem.appendChild(mailto.getElement());
				}
			}

			Element elem2 = DOM.getElementById("perun-credits");
			if (elem2 != null) {
				elem2.setInnerHTML(translation.credits(JsUtils.getCurrentYear()));
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

		// set Title from property if any
		if (PerunConfiguration.getBrandRegistrarTitle() != null) {
			brand.setText(PerunConfiguration.getBrandRegistrarTitle());
		} else {
			brand.setText(translation.registrarAppName());
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

		// init buttons
		application.setText(translation.application());
		myApplications.setText(translation.myApplications());
		logout.setText(translation.logout());

		// hide if not signed-in
		if (PerunSession.getInstance().getRpcServer().equals("non")) {
			logout.setVisible(false);
		}

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
				// DEBUG
				//GWT.log("set in slot: " + content.getClass().getSimpleName());
			}
		} else {
			super.setInSlot(slot, content);
		}
	}

}
