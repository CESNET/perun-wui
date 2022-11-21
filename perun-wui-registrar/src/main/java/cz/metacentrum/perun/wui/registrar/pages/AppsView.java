package cz.metacentrum.perun.wui.registrar.pages;

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
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.model.ApplicationColumnProvider;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * Page to display application form for VO or Group.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AppsView extends ViewImpl implements AppsPresenter.MyView {

	interface AppsViewUiBinder extends UiBinder<Widget, AppsView> {
	}

	@UiField(provided = true)
	PerunDataGrid<Application> grid = new PerunDataGrid<Application>(false, new ApplicationColumnProvider());

	@UiField
	Text text;

	@UiField
	PerunButton refresh;

	@UiField
	AnchorListItem openAppsOnly;

	@UiField
	AnchorListItem allApps;

	@UiField
	PerunButton filterButton;

	private PerunRegistrarTranslation translation = GWT.create(PerunRegistrarTranslation.class);

	@Inject
	public AppsView(AppsViewUiBinder binder) {

		initWidget(binder.createAndBindUi(this));
		text.setText(translation.openAppsOnly());
		refresh.setTooltipText(translation.refresh());
		openAppsOnly.setText(translation.openAppsOnly());
		allApps.setText(translation.allApps());
		allApps.setTitle(translation.slowOperation());
		filterButton.setText(translation.filter());
		grid.setHeight("100%");

	}

	@UiHandler(value = "refresh")
	public void refresh(ClickEvent event) {
		draw();
	}

	@UiHandler(value = "filterButton")
	public void filterButton(ClickEvent event) {
		draw();
	}

	@UiHandler(value = "allApps")
	public void allApps(ClickEvent event) {
		allApps.setActive(true);
		openAppsOnly.setActive(false);
		text.setText(translation.allApps());
		draw();
	}

	@UiHandler(value = "openAppsOnly")
	public void openAppsOnly(ClickEvent event) {
		openAppsOnly.setActive(true);
		allApps.setActive(false);
		text.setText(translation.openAppsOnly());
		draw();
	}

	public void draw() {


		if (openAppsOnly.isActive()) {
			// make sure we search by identity and user session info
			RegistrarManager.getOpenApplicationsForUser(0, new JsonEvents() {

				JsonEvents retry = this;

				@Override
				public void onFinished(JavaScriptObject jso) {
					grid.setList(JsUtils.<Application>jsoAsList(jso));
					refresh.setEnabled(true);
				}

				@Override
				public void onError(PerunException error) {
					grid.getLoaderWidget().onError(error, new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							RegistrarManager.getOpenApplicationsForUser(0, retry);
						}
					});
					refresh.setEnabled(true);
				}

				@Override
				public void onLoadingStart() {
					refresh.setEnabled(false);
					grid.clearTable();
					grid.getLoaderWidget().onLoading(translation.loadingApplications());
				}
			});
		} else {
			// make sure we search by identity and user session info
			RegistrarManager.getApplicationsForUser(0, new JsonEvents() {

				JsonEvents retry = this;

				@Override
				public void onFinished(JavaScriptObject jso) {
					grid.setList(JsUtils.<Application>jsoAsList(jso));
					refresh.setEnabled(true);
				}

				@Override
				public void onError(PerunException error) {
					grid.getLoaderWidget().onError(error, new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							RegistrarManager.getApplicationsForUser(0, retry);
						}
					});
					refresh.setEnabled(true);
				}

				@Override
				public void onLoadingStart() {
					refresh.setEnabled(false);
					grid.clearTable();
					grid.getLoaderWidget().onLoading(translation.loadingApplications());
				}
			});
		}

	}

}
