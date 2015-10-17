package cz.metacentrum.perun.wui.registrar.client;

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
import cz.metacentrum.perun.wui.client.resources.PerunResources;
import cz.metacentrum.perun.wui.client.utils.Utils;
import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Div;

/**
 * Main View for Perun WUI Registrar.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunRegistrarView extends ViewImpl implements PerunPresenter.MyView{

	interface PerunRegistrarViewUiBinder extends UiBinder<Widget, PerunRegistrarView> {}

	@UiField
	Div pageContent;

	private RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

	@UiField
	NavbarCollapse collapse;

	@UiField
	FocusPanel collapseClickHandler;

	@UiField
	AnchorButton language;

	@UiField
	AnchorListItem czech;

	@UiField
	AnchorListItem english;

	@UiField
	AnchorListItem application;

	@UiField
	AnchorListItem myApplications;

	@UiField
	AnchorListItem help;

	@UiField
	AnchorListItem logout;

	@UiField
	static NavbarHeader navbarHeader;

	@UiHandler(value="czech")
	public void czechClick(ClickEvent event) {
		setLocale(Utils.getNativeLanguage().get(0));
	}

	@UiHandler(value="english")
	public void englishClick(ClickEvent event) {
		setLocale("en");
	}

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

	/**
	 * Update localization of whole GUI (reset the app)
	 *
	 * @param locale Locale code to set
	 */
	public void setLocale(String locale) {
		UrlBuilder builder = Window.Location.createUrlBuilder().setParameter("locale", locale);
		Window.Location.replace(builder.buildString());
	}

	@Inject
	PerunRegistrarView(final PerunRegistrarViewUiBinder binder) {

		initWidget(binder.createAndBindUi(this));

		// put logo
		Image logo = new Image(PerunResources.INSTANCE.getPerunLogo());
		logo.setWidth("auto");
		logo.setHeight("50px");
		navbarHeader.insert(logo, 0);

		// init buttons
		application.setText(translation.application());
		myApplications.setText(translation.myApplications());
		help.setText(translation.help());
		language.setText(translation.language());
		logout.setText(translation.logout());

		if ("default".equals(LocaleInfo.getCurrentLocale().getLocaleName()) ||
				"en".equalsIgnoreCase(LocaleInfo.getCurrentLocale().getLocaleName())) {
			// use english name of native language
			czech.setText(Utils.getNativeLanguage().get(2));
			english.setIcon(IconType.CHECK);
			english.setIconPosition(IconPosition.RIGHT);
			czech.setIcon(null);
		} else {
			// use native name of native language
			czech.setText(Utils.getNativeLanguage().get(1));
			czech.setIcon(IconType.CHECK);
			czech.setIconPosition(IconPosition.RIGHT);
			english.setIcon(null);
		}

		english.setText(translation.english());

	}

	@Override
	public void setInSlot(final Object slot, final IsWidget content) {
		if (slot == PerunPresenter.SET_MAIN_CONTENT) {
			pageContent.clear();
			if (content != null) {
				pageContent.add(content);
			}
		} else {
			super.setInSlot(slot, content);
		}
	}

}
