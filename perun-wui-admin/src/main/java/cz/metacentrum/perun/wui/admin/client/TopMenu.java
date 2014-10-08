package cz.metacentrum.perun.wui.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Image;
import cz.metacentrum.perun.wui.client.resources.PerunContextListener;
import org.gwtbootstrap3.client.ui.*;

/**
 * Perun Wui's top menu navigation with logo
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class TopMenu implements PerunContextListener {

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

	public TopMenu() {

		rootElement = ourUiBinder.createAndBindUi(this);

		Image logo = new Image("PerunWuiAdmin/image/perun.png");
		logo.setWidth("235px");
		logo.setHeight("50px");
		navbarHeader.insert(logo, 0);

		settings.setTargetHistoryToken("settings");
		signout.setTargetHistoryToken("logout");

	}

	public Navbar getWidget() {
		return rootElement;
	}

	@Override
	public void setContext(String context) {

		settings.setActive(false);
		signout.setActive(false);
		help.setActive(false);
		refresh.setActive(false);

		if ("settings".equals(context)) {
			settings.setActive(true);
		} else if ("logout".equals(context)) {
			signout.setActive(true);
		}

	}

	@UiHandler(value="help")
	public void clickHelp(ClickEvent event) {
		settings.setActive(false);
		signout.setActive(false);
		refresh.setActive(false);
		help.setActive(true);
		//PerunWui.getContentManager().getDisplayedPage().switchHelp();
	}

	@UiHandler(value="refresh")
	public void clickRefresh(ClickEvent event) {
		settings.setActive(false);
		signout.setActive(false);
		help.setActive(false);
		refresh.setActive(true);
		if (PerunWui.getContentManager().getDisplayedPage() != null) {
			PerunWui.getContentManager().getDisplayedPage().draw();
			refresh.setIconSpin(true);
			Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
				@Override
				public boolean execute() {
					refresh.setIconSpin(false);
					refresh.setActive(false);
					refresh.setFocus(false);
					return false;
				}
			}, 2000);
		}
	}

}