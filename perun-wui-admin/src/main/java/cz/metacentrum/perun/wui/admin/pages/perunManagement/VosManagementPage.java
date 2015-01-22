package cz.metacentrum.perun.wui.admin.pages.perunManagement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.VosManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.model.columnProviders.VoColumnProvider;
import cz.metacentrum.perun.wui.pages.Page;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.growl.client.ui.Growl;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlOptions;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlType;

/**
 * PERUN ADMIN - VOS MANAGEMENT
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class VosManagementPage extends Page {

	interface VosManagementPageUiBinder extends UiBinder<Widget, VosManagementPage> {
	}

	private static VosManagementPageUiBinder ourUiBinder = GWT.create(VosManagementPageUiBinder.class);

	private Widget rootElement;

	@UiField(provided = true)
	PerunDataGrid<Vo> grid;

	@UiField
	ButtonToolBar menu;

	@UiField
	PerunButton button;

	@UiField (provided = true)
	PerunButton remove;

	@UiField
	AnchorListItem growl1;
	@UiField
	AnchorListItem growl2;
	@UiField
	AnchorListItem growl3;
	@UiField
	AnchorListItem growl4;

	/*
	@UiField
	PerunButton addMember;

	@UiField
	PerunButton removeMember;
	*/

	@UiField
	TextBox textBox;

	public VosManagementPage() {

		grid = new PerunDataGrid<Vo>(new VoColumnProvider());

		remove = PerunButton.getButton(PerunButtonType.REMOVE, ButtonType.DANGER, "Remove selected VO(s)");

		rootElement = ourUiBinder.createAndBindUi(this);

	}

	@Override
	public boolean isPrepared() {
		return true;
	}

	@Override
	public boolean isAuthorized() {
		return PerunSession.getInstance().isPerunAdmin();
	}

	@Override
	public void onResize() {

		Scheduler.get().scheduleDeferred(new Command() {
			@Override
			public void execute() {
				int height = DOM.getElementById("web-content").getAbsoluteBottom();
				if (DOM.getElementById("web-content").getAbsoluteBottom() < Window.getClientHeight()) {
					height = Window.getClientHeight();
					if (Window.getClientHeight() < 700) {
						height = 700;
					}
				}
				grid.setHeight(height - grid.getAbsoluteTop() -10 + "px");
				grid.onResize();
			}
		});

	}

	/*
	@UiHandler(value = "addMember")
	public void addMember(ClickEvent event) {
		GroupsManager.addMember(7842, 5723, null);
		GroupsManager.addMember(7842, 3973, null);
	}

	@UiHandler(value = "removeMember")
	public void removeMember(ClickEvent event) {
		GroupsManager.removeMember(7842, 5723, null);
		GroupsManager.removeMember(7842, 3973, null);
	}
	*/

	@UiHandler(value = "remove")
	public void removeOnClick(ClickEvent event) {
		for (Vo vo : grid.getSelectedList()) {
			if (vo.getName().length() > 10) {
				GrowlOptions opts = new GrowlOptions();
				opts.setAllowDismiss(true);
				opts.setType(GrowlType.DANGER);
				opts.setDelay(0);
				Growl.growl("VO "+vo.getName()+" was not deleted.", opts);
				grid.getSelectionModel().setSelected(vo, true);
			} else {
				Growl.growl("VO "+vo.getName()+" was deleted.", GrowlType.SUCCESS);
				grid.getSelectionModel().setSelected(vo, false);
			}
		}
	}

	@UiHandler(value = "button")
	public void onClick(ClickEvent event) {
		ButtonGroup group = new ButtonGroup();
		Button button = new Button("More buttons, jajks....");
		button.addStyleName("GIM-RRTCAJ");
		group.add(button);
		menu.add(group);
		onResize();
	}

	@UiHandler(value = "growl1")
	public void onClick2(ClickEvent event) {

		final GrowlOptions opts = new GrowlOptions();
		Growl.growl("Clicked on Growl", opts);

	}

	@UiHandler(value = "growl2")
	public void onClick3(ClickEvent event) {
		Growl.growl("Title", "Message", IconType.AMBULANCE);
	}

	@UiHandler(value = "growl3")
	public void onClick4(ClickEvent event) {
		GrowlOptions opt = new GrowlOptions();
		opt.setType(GrowlType.SUCCESS);
		opt.setDelay(0);
		final Growl g = Growl.growl("Title", "Message", IconType.AMBULANCE, "http://perun.cesnet.cz/web/", opt);
		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
			@Override
			public boolean execute() {
				g.updateType(GrowlType.DANGER);
				return false;
			}
		}, 2000);
	}

	@UiHandler(value = "growl4")
	public void onClick5(ClickEvent event) {
		Growl.growl("", "Message", IconType.AMBULANCE, "http://perun.cesnet.cz/web/");
	}

	@Override
	public Widget draw() {

		remove.setTableManaged(grid);
		grid.addTableLoadingManagedWidget(remove, false);

		VosManager.getVos(false, new JsonEvents() {

			JsonEvents loadAgain = this;

			@Override
			public void onFinished(JavaScriptObject jso) {
				grid.setList(JsUtils.<Vo>jsoAsList(jso));
			}

			@Override
			public void onError(PerunException error) {
				grid.getLoaderWidget().onError(error, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						VosManager.getVos(false, loadAgain);
					}
				});
			}

			@Override
			public void onLoadingStart() {
				grid.clearTable();
				grid.getLoaderWidget().onLoading();
			}
		});

		return rootElement;

	}

	@Override
	public Widget getWidget() {
		return rootElement;
	}

	@Override
	public void open() {

		textBox.setFocus(true);

	}

	@Override
	public String getUrl() {
		return "perun/vos";
	}

	@Override
	public void toggleHelp() {

	}

	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 1;
		result = prime * result;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

}