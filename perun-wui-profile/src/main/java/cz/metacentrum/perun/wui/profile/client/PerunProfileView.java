package cz.metacentrum.perun.wui.profile.client;

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
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.UiUtils;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.Navbar;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.List;

/**
 * Main View for Perun WUI User profile.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunProfileView extends ViewImpl implements PerunProfilePresenter.MyView{

	interface PerunUserProfileViewUiBinder extends UiBinder<Widget, PerunProfileView> {}

	@UiField Div pageContent;

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField NavbarCollapse collapse;

	@UiField FocusPanel collapseClickHandler;

	@UiField Navbar menuWrapper;
	@UiField Span brand;
	@UiField Div logoWrapper;
	@UiField static NavbarHeader navbarHeader;

	@UiField AnchorListItem topMenuMyProfile;
	@UiField AnchorListItem personal;
	@UiField AnchorListItem organizations;
	@UiField AnchorListItem identities;
	@UiField AnchorListItem groups;
	@UiField AnchorListItem resources;
	@UiField AnchorListItem privacy;
//	@UiField AnchorListItem logins;
	@UiField AnchorListItem settings;
	@UiField NavbarNav topMenu;

	@UiField AnchorListItem personalXS;
	@UiField AnchorListItem organizationsXS;
	@UiField AnchorListItem groupsXS;
	@UiField AnchorListItem identitiesXS;
	@UiField AnchorListItem resourcesXS;
	@UiField AnchorListItem privacyXS;
//	@UiField AnchorListItem loginsXS;
	@UiField AnchorListItem settingsXS;

	@UiField AnchorListItem logout;

	@UiField NavPills menuPills;

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
				if (anchor.startsWith(item.getTargetHistoryToken())) {
					item.setActive(true);
				} else {
					item.setActive(false);
				}
			}
		}

	}

	@Inject
	PerunProfileView(final PerunUserProfileViewUiBinder binder) {

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

		// We must set data toggle when text is changed otherwise its wrongly ordered
		topMenuMyProfile.setDataToggle(Toggle.DROPDOWN);

		Element elem = DOM.getElementById("perun-help");
		if (elem != null) {
			elem.setInnerHTML(translation.supportAt(SafeHtmlUtils.fromString(PerunConfiguration.getBrandSupportMail()).asString()));
		}
		Element elem2 = DOM.getElementById("perun-credits");
		if (elem2 != null) {
			elem2.setInnerHTML(translation.credits(JsUtils.getCurrentYear()));
		}

		applyHideConfiguration();
	}

	private void applyHideConfiguration() {
		setPageVisibility(PerunProfilePlaceTokens.ORGANIZATIONS, organizations, organizationsXS);
		setPageVisibility(PerunProfilePlaceTokens.GROUPS, groups, groupsXS);
		setPageVisibility(PerunProfilePlaceTokens.RESOURCES, resources, resourcesXS);
		setPageVisibility(PerunProfilePlaceTokens.IDENTITIES, identities, identitiesXS);
		setPageVisibility(PerunProfilePlaceTokens.PRIVACY, privacy, privacyXS);
		setPageVisibility(PerunProfilePlaceTokens.SETTINGS, settings, settingsXS);
	}

	private void setPageVisibility(String name, Widget mainMenuItem, Widget topMenuItem) {
		List<String> pagesToHide = PerunConfiguration.getProfilePagesToHide();
		if (pagesToHide.contains(name)) {
			topMenuItem.removeFromParent(); // hiding doesn't work since bootstraps "visibleOn" takes place -> remove from parent
			mainMenuItem.setVisible(false);
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
