package cz.metacentrum.perun.wui.registrar.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.List;

/**
 * Main View for Perun WUI Registrar.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunRegistrarView extends ViewImpl implements PerunRegistrarPresenter.MyView {

	interface PerunRegistrarViewUiBinder extends UiBinder<Widget, PerunRegistrarView> {}

	@UiField
	Div pageContent;

	private RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

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
	AnchorListItem help;

	@UiField
	AnchorListItem logout;

	@UiField static Span footerSupport;
	@UiField static Span footerCredits;
	@UiField static Span footerVersion;

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

	@UiHandler(value="help")
	public void helpClick(ClickEvent event) {
		History.newItem("help");
	}

	@UiHandler(value="logout")
	public void logoutClick(ClickEvent event) {
		History.newItem("logout");
	}


	@Override
	public void onLoadingStartFooter() {
		footerSupport.setHTML(translation.supportAt(translation.loading()));
		footerCredits.setHTML(translation.credits(JsUtils.getCurrentYear()));
		footerVersion.setHTML(translation.version(PerunWebConstants.INSTANCE.guiVersion()));
	}

	@Override
	public void onFinishedFooter(List<String> contactEmail) {
		if (contactEmail == null || contactEmail.isEmpty()) {
			footerSupport.setHTML(translation.supportAt("perun@cesnet.cz"));
			return;
		}
		String mails = contactEmail.toString();
		footerSupport.setHTML(translation.supportAt(mails.substring(1, mails.length()-1)));
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
		logo.setPull(Pull.LEFT);
		navbarHeader.insert(logo, 0);

		UiUtils.addLanguageSwitcher(topMenu);

		// FIXME - temporary disabled
		help.setVisible(false);

		// init buttons
		application.setText(translation.application());
		myApplications.setText(translation.myApplications());
		help.setText(translation.help());
		logout.setText(translation.logout());

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
