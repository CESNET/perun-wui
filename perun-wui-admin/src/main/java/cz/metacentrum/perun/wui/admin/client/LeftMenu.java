package cz.metacentrum.perun.wui.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import cz.metacentrum.perun.wui.client.resources.PerunContextListener;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.admin.pages.perunManagement.VosManagementPage;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.extras.growl.client.ui.Growl;

/**
 * Perun Wui's left menu.
 *
 * @author Pavel Zlámal
 */
public class LeftMenu implements PerunContextListener {

	interface LeftMenuUiBinder extends UiBinder<Div, LeftMenu> {
	}

	private static LeftMenuUiBinder ourUiBinder = GWT.create(LeftMenuUiBinder.class);

	private Div rootElement;

	@UiField
	PanelCollapse perunManagerMenu;

	@UiField
	PanelCollapse voManagerMenu;

	@UiField
	PanelCollapse groupManagerMenu;

	@UiField
	PanelCollapse facilityManagerMenu;

	@UiField
	Panel perunManager;

	@UiField
	Panel voManager;

	@UiField
	Panel groupManager;

	@UiField
	Panel facilityManager;

	@UiField
	AnchorListItem perunVos;
	@UiField
	AnchorListItem perunFacs;
	@UiField
	AnchorListItem perunUsr;
	@UiField
	AnchorListItem perunAttrs;
	@UiField
	AnchorListItem perunServ;
	@UiField
	AnchorListItem perunOwn;
	@UiField
	AnchorListItem perunExtsrc;
	@UiField
	AnchorListItem perunName;
	@UiField
	AnchorListItem perunSearch;

	public LeftMenu() {

		rootElement = ourUiBinder.createAndBindUi(this);

		// set visibility of menu per roles

		if (PerunSession.getInstance().isPerunAdmin()) {
			perunManager.setVisible(true);
		}
		if (PerunSession.getInstance().isVoAdmin() || PerunSession.getInstance().isVoObserver()) {
			voManager.setVisible(true);
		}
		if (PerunSession.getInstance().isGroupAdmin()) {
			groupManager.setVisible(true);
		}
		if (PerunSession.getInstance().isFacilityAdmin()) {
			facilityManager.setVisible(true);
		}

	}

	public Div getWidget() {
		return rootElement;
	}

	@Override
	public void setContext(String context) {

		if ("perun".equals(context.split("/")[0])) {

			perunManagerMenu.setIn(true);
			voManagerMenu.setIn(false);
			groupManagerMenu.setIn(false);
			facilityManagerMenu.setIn(false);

		} else if ("vo".equals(context.split("/")[0])) {

			perunManagerMenu.setIn(false);
			voManagerMenu.setIn(true);
			groupManagerMenu.setIn(false);
			facilityManagerMenu.setIn(false);

		} else if ("group".equals(context.split("/")[0])) {

			perunManagerMenu.setIn(false);
			voManagerMenu.setIn(false);
			groupManagerMenu.setIn(true);
			facilityManagerMenu.setIn(false);

		} else if ("facility".equals(context.split("/")[0])) {

			perunManagerMenu.setIn(false);
			voManagerMenu.setIn(false);
			groupManagerMenu.setIn(false);
			facilityManagerMenu.setIn(true);

		}

		// set menu items targets to be clickable links

		perunVos.setTargetHistoryToken("perun/vos");
		perunFacs.setTargetHistoryToken("perun/facs");
		perunUsr.setTargetHistoryToken("perun/usrs");
		perunAttrs.setTargetHistoryToken("perun/attrs");
		perunServ.setTargetHistoryToken("perun/serv");
		perunOwn.setTargetHistoryToken("perun/own");
		perunExtsrc.setTargetHistoryToken("perun/extsrc");
		perunName.setTargetHistoryToken("perun/namespace");
		perunSearch.setTargetHistoryToken("perun/search");

		// TODO - set targets based on Active entity in session

		// TODO - rest of menu items

		perunVos.setActive(false);
		perunFacs.setActive(false);
		perunUsr.setActive(false);
		perunAttrs.setActive(false);
		perunServ.setActive(false);
		perunOwn.setActive(false);
		perunExtsrc.setActive(false);
		perunName.setActive(false);
		perunSearch.setActive(false);

		if ("perun/vos".equals(context)) {
			perunVos.setActive(true);
		} else if ("perun/facs".equals(context)) {
			perunFacs.setActive(true);
		} else if ("perun/usrs".equals(context)) {
			perunUsr.setActive(true);
		} else if ("perun/attrs".equals(context)) {
			perunAttrs.setActive(true);
		} else if ("perun/serv".equals(context)) {
			perunServ.setActive(true);
		} else if ("perun/own".equals(context)) {
			perunOwn.setActive(true);
		} else if ("perun/extsrc".equals(context)) {
			perunExtsrc.setActive(true);
		} else if ("perun/namespace".equals(context)) {
			perunName.setActive(true);
		} else if ("perun/search".equals(context)) {
			perunSearch.setActive(true);
		}

		// disable link click in order to use click handlers
		Scheduler.get().scheduleDeferred(new Command() {
			@Override
			public void execute() {
				disableLinks();
			}
		});

	}

	// TODO - call content manager and create Pages as classes, don't use History.newItem() it will be called by Page itself

	@UiHandler("perunVos")
	public void click1(ClickEvent handler) {
		PerunWui.getContentManager().openPage(new VosManagementPage());
	}

	@UiHandler("perunFacs")
	public void click2(ClickEvent handler) {
		History.newItem("perun/facs");
		Growl.growl("Clicked on Facilities");
	}

	@UiHandler("perunUsr")
	public void click3(ClickEvent handler) {
		History.newItem("perun/usrs");
		Growl.growl("Clicked on Users");
	}

	@UiHandler("perunAttrs")
	public void click4(ClickEvent handler) {
		History.newItem("perun/attrs");
		Growl.growl("Clicked on Attributes");
	}

	@UiHandler("perunServ")
	public void click5(ClickEvent handler) {
		History.newItem("perun/serv");
		Growl.growl("Clicked on Services");
	}

	@UiHandler("perunOwn")
	public void click6(ClickEvent handler) {
		History.newItem("perun/own");
		Growl.growl("Clicked on Owners");
	}

	@UiHandler("perunExtsrc")
	public void click7(ClickEvent handler) {
		History.newItem("perun/extsrc");
		Growl.growl("Clicked on External sources");
	}

	@UiHandler("perunName")
	public void click8(ClickEvent handler) {
		History.newItem("perun/namespace");
		Growl.growl("Clicked on Namespaces");
	}

	@UiHandler("perunExtsrc")
	public void click9(ClickEvent handler) {
		History.newItem("perun/search");
		Growl.growl("Clicked on Searcher");
	}

	/**
	 * Prevent default click action on links in menu. Href is present but ignored, only
	 * GWT's click handler is activated.
	 */
	private final native void disableLinks() /*-{
		$wnd.jQuery('ul.leftMenu li a').click(function (e) {
			e.preventDefault();
		});
	}-*/;

}