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
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PerunWebConstants;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.Map;

/**
 * Main View for Perun WUI User profile.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class UserProfileView extends ViewImpl implements UserProfilePresenter.MyView{

	interface PerunUserProfileViewUiBinder extends UiBinder<Widget, UserProfileView> {}

	@UiField
	Div pageContent;

	private UserProfileTranslation translation = GWT.create(UserProfileTranslation.class);

	@UiField
	NavbarCollapse collapse;

	@UiField
	FocusPanel collapseClickHandler;

	@UiField
	AnchorListItem logout;

	@UiField
	static NavbarHeader navbarHeader;

	@UiField
	AnchorListItem user;

	@UiField
	AnchorButton language;

	@UiField
	AnchorListItem czech;

	@UiField
	AnchorListItem english;

	@UiField
	Span footerLeft;
	@UiField
	Span footerRight;

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

	@Inject
	UserProfileView(final PerunUserProfileViewUiBinder binder) {

		initWidget(binder.createAndBindUi(this));

		// put logo
		Image logo = Utils.perunInstanceLogo();
		logo.setWidth("auto");
		logo.setHeight("50px");
		navbarHeader.insert(logo, 0);

		language.setText(translation.language());
		logout.setText(translation.logout());

		user.setText(PerunSession.getInstance().getUser().getFullName());

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

		// TODO - more advanced footer
		footerLeft.setHTML(translation.supportAt(Utils.perunReportEmailAddress()));
		footerRight.setHTML(translation.credits(JsUtils.getCurrentYear()) + " | " +  translation.version(PerunWebConstants.INSTANCE.guiVersion()));

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
