package cz.metacentrum.perun.wui.userprofile.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.UrlBuilder;
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
import cz.metacentrum.perun.wui.client.utils.Utils;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarHeader;
import org.gwtbootstrap3.client.ui.html.Div;

import java.util.Map;

/**
 * Main View for Perun WUI User profile.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunUserProfileView extends ViewImpl implements PerunPresenter.MyView{

	interface PerunUserProfileViewUiBinder extends UiBinder<Widget, PerunUserProfileView> {}

	@UiField
	Div pageContent;

	//private RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

	@UiField
	NavbarCollapse collapse;

	@UiField
	FocusPanel collapseClickHandler;

	@UiField
	AnchorListItem logout;

	@UiField
	static NavbarHeader navbarHeader;

	@UiHandler(value="logout")
	public void logoutClick(ClickEvent event) {
		History.newItem("logout");
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
	PerunUserProfileView(final PerunUserProfileViewUiBinder binder) {

		initWidget(binder.createAndBindUi(this));

		// put logo
		Image logo = Utils.perunInstanceLogo();
		logo.setWidth("auto");
		logo.setHeight("50px");
		navbarHeader.insert(logo, 0);

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
