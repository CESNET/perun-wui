package cz.metacentrum.perun.wui.admin.pages.perunManagement;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.UiUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.OwnersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Owner;
import cz.metacentrum.perun.wui.model.columnProviders.OwnerColumnProvider;
import cz.metacentrum.perun.wui.pages.FocusableView;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedSuggestBox;
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;
import cz.metacentrum.perun.wui.widgets.resources.UnaccentMultiWordSuggestOracle;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.extras.growl.client.ui.Growl;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlType;

/**
 * PERUN ADMIN - OWNERS MANAGEMENT VIEW
 *
 * @author Kristyna Kysela
 */
public class OwnersManagementView extends ViewWithUiHandlers<PerunManagementUiHandlers> implements OwnersManagementPresenter.MyView, FocusableView {

	private UnaccentMultiWordSuggestOracle oracle = new UnaccentMultiWordSuggestOracle();

	@UiField(provided = true)
	static
	PerunDataGrid<Owner> grid;

	@UiField (provided = true)
	PerunButton remove;

	@UiField(provided = true)
	ExtendedSuggestBox textBox = new ExtendedSuggestBox(oracle);

	@UiField ButtonToolBar menu;
	@UiField PerunButton filterButton;
	@UiField PerunButton createButton;

	OwnersManagementView view = this;

	interface OwnersManagementViewUiBinder extends UiBinder<Widget, OwnersManagementView> {
	}

	@Inject
	OwnersManagementView(final OwnersManagementViewUiBinder uiBinder) {

		grid = new PerunDataGrid<Owner>(new OwnerColumnProvider());
		remove = PerunButton.getButton(PerunButtonType.REMOVE, ButtonType.DANGER, "Remove selected Owner(s)");

		initWidget(uiBinder.createAndBindUi(this));
		UiUtils.bindFilterBox(grid, textBox, filterButton);
		UiUtils.bindTableLoading(grid, filterButton, true);
		UiUtils.bindTableLoading(grid, textBox, true);
		UiUtils.bindTableLoading(grid, createButton, true);
		UiUtils.bindTableSelection(grid, remove);
		createButton.setEnabled(false);

		draw();
	}

	@UiHandler(value = "createButton")
	public void onClick(ClickEvent event) {
		getUiHandlers().createOwner();
	}

	public static void update(Owner createdOwner){
		grid.addToTable(createdOwner);
		grid.sortTable();
		grid.refresh();

	}

	@UiHandler(value = "remove")
	public void onRemove(ClickEvent event) {

		for (final Owner owner : grid.getSelectedList()) {

			OwnersManager.deleteOwner(owner.getId(), new JsonEvents() {

				JsonEvents loadAgain = this;

				@Override
				public void onFinished(JavaScriptObject jso) {
					remove.setProcessing(false);
					Growl.growl("Owner " + owner.getName() + " was deleted.", GrowlType.SUCCESS);
					grid.removeFromTable(owner);
					grid.refresh();
				}

				@Override
				public void onError(PerunException error) {
					remove.setProcessing(false);
					Growl.growl("Owner " + owner.getName() + " was not deleted. " + error.getMessage(), GrowlType.DANGER);
							grid.getLoaderWidget().onError(error, new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									OwnersManager.deleteOwner(owner.getId(), loadAgain);
								}
							});
				}

				@Override
				public void onLoadingStart() {
					remove.setProcessing(true);
				}
			});
		}


	}

	public void draw() {

		OwnersManager.getOwners(new JsonEvents() {

			JsonEvents loadAgain = this;

			@Override
			public void onFinished(JavaScriptObject jso) {
				grid.setList(JsUtils.<Owner>jsoAsList(jso));
				for (Owner owner : grid.getList()) {
					oracle.add(owner.getName());
					oracle.add(owner.getType());
				}
			}

			@Override
			public void onError(PerunException error) {
				grid.getLoaderWidget().onError(error, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						OwnersManager.getOwners(loadAgain);
					}
				});
			}

			@Override
			public void onLoadingStart() {
				grid.clearTable();
				grid.getLoaderWidget().onLoading();
				oracle.clear();
			}
		});

	}

	@Override
	public void focus() {
		textBox.setFocus(true);
	}

}