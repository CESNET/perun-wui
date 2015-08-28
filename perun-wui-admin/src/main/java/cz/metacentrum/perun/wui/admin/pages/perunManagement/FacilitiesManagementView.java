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
import cz.metacentrum.perun.wui.widgets.resources.UnaccentMultiWordSuggestOracle;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

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

	FacilitiesManagementView view = this;

	interface FacilitiesManagementViewUiBinder extends UiBinder<Widget, FacilitiesManagementView> {
	}

	@Inject
	FacilitiesManagementView(final FacilitiesManagementViewUiBinder uiBinder) {

		grid = new PerunDataGrid<Facility>(new FacilityColumnProvider());
		grid.setHeight("100%");
		remove = PerunButton.getButton(PerunButtonType.REMOVE, ButtonType.DANGER, "Remove selected Facilities");
		initWidget(uiBinder.createAndBindUi(this));
		grid.addTableManagedFilterBox(textBox, filterButton);
		grid.addTableManagedWidget(remove);
		//remove.setTableManaged(grid);
		//grid.addTableLoadingManagedWidget(remove, false);
		// FIXME - is this right place to draw ?
		draw();
	}

	@UiHandler(value = "filterButton")
	public void filter(ClickEvent event) {
		grid.filterTable(textBox.getValue());
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