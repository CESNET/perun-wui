package cz.metacentrum.perun.wui.admin.pages.perunManagement;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.UiUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.FacilitiesManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Facility;
import cz.metacentrum.perun.wui.model.columnProviders.FacilityColumnProvider;
import cz.metacentrum.perun.wui.pages.FocusableView;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedSuggestBox;
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;
import cz.metacentrum.perun.wui.widgets.resources.UnaccentMultiWordSuggestOracle;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

import java.util.HashMap;

/**
 * PERUN ADMIN - FACILITIES MANAGEMENT VIEW
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FacilitiesManagementView extends ViewImpl implements FacilitiesManagementPresenter.MyView, FocusableView {

	private UnaccentMultiWordSuggestOracle oracle = new UnaccentMultiWordSuggestOracle(" .-");

	@UiField(provided = true)
	PerunDataGrid<Facility> grid;

	@UiField (provided = true)
	PerunButton remove;

	@UiField(provided = true)
	ExtendedSuggestBox textBox = new ExtendedSuggestBox(oracle);

	@UiField ButtonToolBar menu;
	@UiField PerunButton filterButton;
	@UiField PerunButton button;
	@UiField AnchorListItem idDropdown;
	@UiField AnchorListItem nameDropdown;
	@UiField AnchorListItem descriptionDropdown;
	@UiField Button dropdown;

	private HashMap<AnchorListItem, PerunColumnType> anchorColumnMap;

	FacilitiesManagementView view = this;

	interface FacilitiesManagementViewUiBinder extends UiBinder<Widget, FacilitiesManagementView> {
	}

	@Inject
	FacilitiesManagementView(final FacilitiesManagementViewUiBinder uiBinder) {

		grid = new PerunDataGrid<Facility>(new FacilityColumnProvider());
		remove = PerunButton.getButton(PerunButtonType.REMOVE, ButtonType.DANGER, "Remove selected Facilities");
		initWidget(uiBinder.createAndBindUi(this));

		anchorColumnMap = new HashMap<AnchorListItem, PerunColumnType>();
		anchorColumnMap.put(idDropdown, PerunColumnType.ID);
		anchorColumnMap.put(nameDropdown, PerunColumnType.NAME);
		anchorColumnMap.put(descriptionDropdown, PerunColumnType.DESCRIPTION);

		UiUtils.bindFilterBox(grid, textBox, filterButton);
		UiUtils.bindDropdown(anchorColumnMap, grid);
		UiUtils.bindTableLoading(grid, filterButton, true);
		UiUtils.bindTableLoading(grid, textBox, true);
		UiUtils.bindTableLoading(grid, dropdown, true);
		UiUtils.bindTableSelection(grid, remove);

		draw();
	}


	@UiHandler(value = "remove")
	public void removeOnClick(ClickEvent event) {

	}

	@UiHandler(value = "button")
	public void onClick(ClickEvent event) {
		// FIXME - for testing of table reload only
		draw();
	}

	public void draw() {

		FacilitiesManager.getFacilities(true, new JsonEvents() {

			JsonEvents loadAgain = this;

			@Override
			public void onFinished(JavaScriptObject jso) {
				grid.setList(JsUtils.<Facility>jsoAsList(jso));
				for (Facility fac : grid.getList()) {
					oracle.add(fac.getName());
				}
			}

			@Override
			public void onError(PerunException error) {
				grid.getLoaderWidget().onError(error, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						FacilitiesManager.getFacilities(true, loadAgain);
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