package cz.metacentrum.perun.wui.admin.pages.perunManagement;

import com.google.gwt.core.client.JavaScriptObject;
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
import cz.metacentrum.perun.wui.json.managers.ExtSourcesManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ExtSource;
import cz.metacentrum.perun.wui.model.columnProviders.ExtSourceColumnProvider;
import cz.metacentrum.perun.wui.pages.FocusableView;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedSuggestBox;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;
import cz.metacentrum.perun.wui.widgets.resources.UnaccentMultiWordSuggestOracle;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.extras.growl.client.ui.Growl;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlType;

import java.util.HashMap;

/**
 * PERUN ADMIN - EXT SOURCES MANAGEMENT VIEW
 *
 * @author Kristyna Kysela
 */
public class ExtSourcesManagementView extends ViewImpl implements ExtSourcesManagementPresenter.MyView, FocusableView {

	private UnaccentMultiWordSuggestOracle oracle = new UnaccentMultiWordSuggestOracle(" ./-=");

	@UiField(provided = true)
	PerunDataGrid<ExtSource> grid;

	@UiField(provided = true)
	ExtendedSuggestBox textBox = new ExtendedSuggestBox(oracle);

	@UiField ButtonToolBar menu;
	@UiField PerunButton filterButton;
	@UiField(provided = true) PerunButton loadButton;
	@UiField PerunButton createButton;
	@UiField PerunButton removeButton;
	@UiField AnchorListItem idDropdown;
	@UiField AnchorListItem nameDropdown;
	@UiField AnchorListItem typeDropdown;
	@UiField Button dropdown;

	private HashMap<AnchorListItem, PerunColumnType> anchorColumnMap;

	ExtSourcesManagementView view = this;

	interface ExtSourcesManagementViewUiBinder extends UiBinder<Widget, ExtSourcesManagementView> {
	}

	@Inject
	ExtSourcesManagementView(final ExtSourcesManagementViewUiBinder uiBinder) {

		grid = new PerunDataGrid<ExtSource>(new ExtSourceColumnProvider());
		loadButton = new PerunButton("Load ext sources", "Load ext sources definitions from xml file", IconType.GLOBE);

		initWidget(uiBinder.createAndBindUi(this));
		anchorColumnMap = new HashMap<AnchorListItem, PerunColumnType>();
		anchorColumnMap.put(idDropdown, PerunColumnType.ID);
		anchorColumnMap.put(nameDropdown, PerunColumnType.NAME);
		anchorColumnMap.put(typeDropdown, PerunColumnType.EXT_SOURCE_TYPE);

		UiUtils.bindFilterBox(grid, textBox, filterButton);
		UiUtils.bindFilteringDropDown(anchorColumnMap, grid);
		UiUtils.bindTableLoading(grid, filterButton, true);
		UiUtils.bindTableLoading(grid, textBox, true);
		UiUtils.bindTableLoading(grid, loadButton, true);
		UiUtils.bindTableLoading(grid, createButton, true);
		UiUtils.bindTableLoading(grid, dropdown, true);
		UiUtils.bindTableSelection(grid, removeButton);

		draw();
	}

	@UiHandler(value = "removeButton")
	public void deleteExtSource(ClickEvent event) {

		for (final ExtSource extSource: grid.getSelectedList()) {

			ExtSourcesManager.deleteExtSource(extSource, new JsonEvents() {

				@Override
				public void onFinished(JavaScriptObject jso) {
					removeButton.setProcessing(false);
					Growl.growl("ExtSource " + extSource.getName() + " was deleted.", GrowlType.SUCCESS);
					grid.removeFromTable(extSource);
				}

				@Override
				public void onError(PerunException error) {
					removeButton.setProcessing(false);
					Growl.growl("ExtSource " + extSource.getName() + " was not deleted. " + error.getMessage(), GrowlType.DANGER);
				}

				@Override
				public void onLoadingStart() {
					removeButton.setProcessing(true);
				}

			});
		}

	}

	@UiHandler(value = "loadButton")
	public void onClick(ClickEvent event) {

		ExtSourcesManager.loadExtSourcesDefinitions(new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject jso) {
				loadButton.setProcessing(false);
				draw();
			}

			@Override
			public void onError(PerunException error) {
				loadButton.setProcessing(false);
			}

			@Override
			public void onLoadingStart() {
				loadButton.setProcessing(true);
			}
		});

	}

	public void draw() {

		ExtSourcesManager.getExtSources(new JsonEvents() {

			JsonEvents loadAgain = this;

			@Override
			public void onFinished(JavaScriptObject jso) {
				grid.setList(JsUtils.<ExtSource>jsoAsList(jso));
				for (ExtSource source : grid.getList()) {
					oracle.add(source.getName());
					oracle.add(source.getType().substring(40).toUpperCase());
				}
			}

			@Override
			public void onError(PerunException error) {
				grid.getLoaderWidget().onError(error, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						ExtSourcesManager.getExtSources(loadAgain);
					}
				});
			}

			@Override
			public void onLoadingStart() {
				oracle.clear();
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