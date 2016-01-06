package cz.metacentrum.perun.wui.userprofile.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunWebConstants;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.NavbarBrand;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.Map;
import java.util.Objects;

/**
 * Main View for Perun WUI User profile.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class UserProfileView extends ViewImpl implements UserProfilePresenter.MyView{

	interface PerunUserProfileViewUiBinder extends UiBinder<Widget, UserProfileView> {}

	@UiField Div pageContent;

	private UserProfileTranslation translation = GWT.create(UserProfileTranslation.class);

	@UiField NavbarCollapse collapse;

	@UiField FocusPanel collapseClickHandler;

	@UiField Span brand;
	@UiField static NavbarHeader navbarHeader;

	@UiField AnchorListItem topMenuMyProfile;
	@UiField AnchorListItem personal;
	@UiField AnchorListItem organizations;
	@UiField AnchorListItem identities;
	@UiField AnchorListItem logins;
	@UiField AnchorListItem settings;
	@UiField AnchorButton language;
	@UiField AnchorListItem czech;
	@UiField AnchorListItem english;

	@UiField AnchorListItem personalXS;
	@UiField AnchorListItem organizationsXS;
	@UiField AnchorListItem identitiesXS;
	@UiField AnchorListItem loginsXS;
	@UiField AnchorListItem settingsXS;

	@UiField AnchorListItem logout;

	@UiField NavPills menuPills;

	@UiField Span footerSupport;
	@UiField Span footerCredits;
	@UiField Span footerVersion;

	@UiHandler(value="logout")
	public void logoutClick(ClickEvent event) {
		History.newItem("logout");
	}

	@UiHandler(value="czech")
	public void czechClick(ClickEvent event) {
		setLocale(Utils.getNativeLanguage());
	}

	@UiHandler(value="english")
	public void englishClick(ClickEvent event) {
		setLocale(Utils.ENGLISH_LANGUAGE);
	}

	/**
	 * Update localization of whole GUI (reset the app)
	 *
	 * @param locale Locale to set. Structure is "code":"val", "nativeName":"val", "englishName":"val"
	 */
	public void setLocale(Map<String, String> locale) {
		if (locale == null) {
			GWT.log("WARN: Locale is null");
			return;
		}
		UrlBuilder builder = Window.Location.createUrlBuilder().setParameter("locale", locale.get("code"));
		Window.Location.replace(builder.buildString());
	}

	@Override
	public void setActiveMenuItem(String anchor) {

		int count = menuPills.getWidgetCount();
		for (int i=0; i < count; i++) {
			AnchorListItem item = (AnchorListItem)menuPills.getWidget(i);
			if (Objects.equals(anchor, item.getTargetHistoryToken())) {
				item.setActive(true);
			} else {
				item.setActive(false);
			}
		}

	}

	@Inject
	UserProfileView(final PerunUserProfileViewUiBinder binder) {


		initWidget(binder.createAndBindUi(this));
		brand.setText(translation.appMyProfile());

		// put logo
		Image logo = Utils.perunInstanceLogo();
		logo.setWidth("auto");
		logo.setHeight("50px");
		logo.setPull(Pull.LEFT);
		navbarHeader.insert(logo, 0);

		topMenuMyProfile.setText(translation.menuMyProfile());

		personal.setText(translation.menuMyProfile());
		personalXS.setText(translation.menuMyProfile());
		organizations.setText(translation.menuOrganizations());
		organizationsXS.setText(translation.menuOrganizations());
		identities.setText(translation.menuMyIdentities());
		identitiesXS.setText(translation.menuMyIdentities());
		logins.setText(translation.menuLoginsAndPasswords());
		loginsXS.setText(translation.menuLoginsAndPasswords());
		settings.setText(translation.menuSettings());
		settingsXS.setText(translation.menuSettings());
		language.setText(translation.language());
		logout.setText(translation.logout());

		// We must set data toggle when text is changed otherwise its wrongly ordered
		topMenuMyProfile.setDataToggle(Toggle.DROPDOWN);
		language.setDataToggle(Toggle.DROPDOWN);

		if (Utils.getNativeLanguage() != null) {

			if ("default".equals(LocaleInfo.getCurrentLocale().getLocaleName()) ||
					"en".equalsIgnoreCase(LocaleInfo.getCurrentLocale().getLocaleName())) {
				// use english name of native language
				czech.setText(Utils.getNativeLanguage().get("englishName"));
				english.setIcon(IconType.CHECK);
				english.setIconPosition(IconPosition.RIGHT);
				czech.setIcon(null);
			} else {
				// use native name of native language
				czech.setText(Utils.getNativeLanguage().get("nativeName"));
				czech.setIcon(IconType.CHECK);
				czech.setIconPosition(IconPosition.RIGHT);
				english.setIcon(null);
			}
			english.setText(translation.english());

		} else {
			// no language switching
			language.setVisible(false);
		}

		english.setText(translation.english());

		footerSupport.setHTML(translation.supportAt(Utils.perunReportEmailAddress()));
		footerCredits.setHTML(translation.credits(JsUtils.getCurrentYear()));
		footerVersion.setHTML(translation.version(PerunWebConstants.INSTANCE.guiVersion()));

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
