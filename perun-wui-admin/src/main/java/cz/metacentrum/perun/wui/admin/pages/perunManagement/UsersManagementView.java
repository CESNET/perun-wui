package cz.metacentrum.perun.wui.admin.pages.perunManagement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.UiUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.model.columnProviders.RichUserColumnProvider;
import cz.metacentrum.perun.wui.pages.FocusableView;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedSuggestBox;
import cz.metacentrum.perun.wui.widgets.resources.UnaccentMultiWordSuggestOracle;
import org.gwtbootstrap3.client.ui.*;

/**
 * PERUN ADMIN - USERS MANAGEMENT VIEW
 *
 * @author Kristyna Kysela
 */
public class UsersManagementView extends ViewImpl implements UsersManagementPresenter.MyView, FocusableView {

	private UnaccentMultiWordSuggestOracle oracle = new UnaccentMultiWordSuggestOracle();

	@UiField(provided = true)
	PerunDataGrid<RichUser> grid;

	@UiField(provided = true)
	ExtendedSuggestBox textBox = new ExtendedSuggestBox(oracle);

	@UiField ButtonToolBar menu;
	@UiField PerunButton searchButton;
	@UiField PerunButton listButton;

	UsersManagementView view = this;

	interface UsersManagementViewUiBinder extends UiBinder<Widget, UsersManagementView> {
	}

	@Inject
	UsersManagementView(final UsersManagementViewUiBinder uiBinder) {

		grid = new PerunDataGrid<RichUser>(new RichUserColumnProvider());
		initWidget(uiBinder.createAndBindUi(this));

		UiUtils.bindSearchBox(textBox, searchButton);
		UiUtils.bindTableLoading(grid, textBox, true);
		UiUtils.bindTableLoading(grid, searchButton, true);
		UiUtils.bindTableLoading(grid, listButton, true);
		searchButton.setEnabled(false);

		grid.getLoaderWidget().setEmptyMessage("Type name, mail or login to search by...");
		grid.getLoaderWidget().onEmpty();


	}

	@UiHandler(value = "searchButton")
	public void search(ClickEvent event) {

			UsersManager.findRichUsersWithAttributes(textBox.getText(), PerunSession.getInstance().getConfiguration().getListOfStrings("getAttributesListForUserTables"), new JsonEvents() {

				JsonEvents loadAgain = this;

				@Override
				public void onFinished(JavaScriptObject jso) {
					searchButton.setProcessing(false);
					grid.setList(JsUtils.<RichUser>jsoAsList(jso));
				}

				@Override
				public void onError(PerunException error) {
					grid.getLoaderWidget().onError(error, new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							UsersManager.findRichUsersWithAttributes(textBox.getText(), PerunSession.getInstance().getConfiguration().getListOfStrings("getAttributesListForUserTables"), loadAgain);
						}
					});
					searchButton.setProcessing(false);
				}

				@Override
				public void onLoadingStart() {
					grid.clearTable();
					grid.getLoaderWidget().onLoading();
					searchButton.setProcessing(true);
				}
			});
	}

	@UiHandler(value = "listButton")
	public void onClick(ClickEvent event) {

		UsersManager.getRichUsersWithoutVoAssigned(new JsonEvents() {

			JsonEvents loadAgain = this;

			@Override
			public void onFinished(JavaScriptObject jso) {
				listButton.setProcessing(false);
				grid.setList(JsUtils.<RichUser>jsoAsList(jso));
			}

			@Override
			public void onError(PerunException error) {
				listButton.setProcessing(false);
				grid.getLoaderWidget().onError(error, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						UsersManager.getRichUsersWithoutVoAssigned(loadAgain);
					}
				});
			}

			@Override
			public void onLoadingStart() {
				listButton.setProcessing(true);
				grid.clearTable();
				grid.getLoaderWidget().onLoading();
			}
		});
	}

	@Override
	public void focus() {
		GWT.log("called");
		textBox.setFocus(true);
	}



}
