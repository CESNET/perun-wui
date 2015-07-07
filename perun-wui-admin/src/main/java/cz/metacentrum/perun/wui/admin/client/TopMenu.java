package cz.metacentrum.perun.wui.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Image;
import cz.metacentrum.perun.wui.client.resources.PerunResources;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import org.gwtbootstrap3.client.ui.*;

/**
 * Perun Wui's top menu navigation with logo
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class TopMenu {

	interface TopMenuUiBinder extends UiBinder<Navbar, TopMenu> {
	}

	private static TopMenuUiBinder ourUiBinder = GWT.create(TopMenuUiBinder.class);

	private Navbar rootElement;

	@UiField
	NavbarHeader navbarHeader;

	@UiField
	AnchorListItem help;

	@UiField
	AnchorListItem refresh;

	@UiField
	AnchorListItem settings;

	@UiField
	AnchorListItem signout;

	@UiField
	AnchorListItem user;

	public TopMenu() {

		rootElement = ourUiBinder.createAndBindUi(this);

		Image logo = new Image(PerunResources.INSTANCE.getPerunLogo());
		logo.setWidth("auto");
		logo.setHeight("50px");
		navbarHeader.insert(logo, 0);

		user.setText(PerunSession.getInstance().getUser().getFullName());

		settings.setTargetHistoryToken("settings");
		signout.setTargetHistoryToken("logout");

	}

	public Navbar getWidget() {
		return rootElement;
	}

	@UiHandler(value="help")
	public void clickHelp(ClickEvent event) {
		settings.setActive(false);
		signout.setActive(false);
		refresh.setActive(false);
		help.setActive(true);
	}

	@UiHandler(value="refresh")
	public void clickRefresh(ClickEvent event) {
		settings.setActive(false);
		signout.setActive(false);
		help.setActive(false);
		refresh.setActive(true);
	}

}