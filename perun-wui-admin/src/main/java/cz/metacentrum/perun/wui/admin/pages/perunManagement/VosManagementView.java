package cz.metacentrum.perun.wui.admin.pages.perunManagement;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.UiUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.VosManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.model.columnProviders.VoColumnProvider;
import cz.metacentrum.perun.wui.pages.FocusableView;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedSuggestBox;
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;
import cz.metacentrum.perun.wui.widgets.resources.UnaccentMultiWordSuggestOracle;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.growl.client.ui.Growl;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlOptions;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlType;

import java.util.HashMap;

/**
 * PERUN ADMIN - VOS MANAGEMENT VIEW
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class VosManagementView extends ViewImpl implements VosManagementPresenter.MyView, FocusableView {

	private UnaccentMultiWordSuggestOracle oracle = new UnaccentMultiWordSuggestOracle();

	@UiField(provided = true)
	PerunDataGrid<Vo> grid;

	@UiField (provided = true)
	PerunButton remove;

	@UiField(provided = true)
	ExtendedSuggestBox textBox = new ExtendedSuggestBox(oracle);

	@UiField ButtonToolBar menu;
	@UiField PerunButton filterButton;
	@UiField PerunButton button;
	@UiField AnchorListItem growl1;
	@UiField AnchorListItem growl2;
	@UiField AnchorListItem growl3;
	@UiField AnchorListItem growl4;

	@UiField AnchorListItem idDropdown;
	@UiField AnchorListItem nameDropdown;
	@UiField AnchorListItem shortNameDropdown;
	@UiField Button dropdown;


	private HashMap<AnchorListItem, PerunColumnType> anchorColumnMap;

	VosManagementView view = this;

	interface VosManagementViewUiBinder extends UiBinder<Widget, VosManagementView> {
	}

	@Inject
	VosManagementView(final VosManagementViewUiBinder uiBinder) {

		grid = new PerunDataGrid<Vo>(new VoColumnProvider());
		remove = PerunButton.getButton(PerunButtonType.REMOVE, ButtonType.DANGER, "Remove selected VO(s)");

		initWidget(uiBinder.createAndBindUi(this));

		anchorColumnMap = new HashMap<AnchorListItem, PerunColumnType>();
		anchorColumnMap.put(idDropdown, PerunColumnType.ID);
		anchorColumnMap.put(nameDropdown, PerunColumnType.NAME);
		anchorColumnMap.put(shortNameDropdown, PerunColumnType.VO_SHORT_NAME);

		UiUtils.bindFilterBox(grid, textBox, filterButton);
		UiUtils.bindFilteringDropDown(anchorColumnMap, grid);
		UiUtils.bindTableLoading(grid, filterButton, true);
		UiUtils.bindTableLoading(grid, textBox, true);
		UiUtils.bindTableLoading(grid, dropdown, true);
		UiUtils.bindTableSelection(grid, remove);

		draw();
	}

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

	public void draw() {

		VosManager.getVos(false, new JsonEvents() {

			JsonEvents loadAgain = this;

			@Override
			public void onFinished(JavaScriptObject jso) {
				grid.setList(JsUtils.<Vo>jsoAsList(jso));
				for (Vo vo : grid.getList()) {
					oracle.add(vo.getName());
					oracle.add(vo.getShortName());
				}
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

	}

	@Override
	public void focus() {
		textBox.setFocus(true);
	}



}