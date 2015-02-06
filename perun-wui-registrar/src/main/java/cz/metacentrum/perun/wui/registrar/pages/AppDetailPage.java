package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.pages.Page;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Page to display application form for VO or Group.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AppDetailPage extends Page {

	interface AppDetailPageUiBinder extends UiBinder<Widget, AppDetailPage> {
	}

	private static AppDetailPageUiBinder ourUiBinder = GWT.create(AppDetailPageUiBinder.class);

	@UiField
	Text text;

	@UiField
	PerunButton backButton;

	@UiField
	PerunForm form;

	@UiField
	Heading formTitle;

	@UiField
	DescriptionTitle subTitle;

	@UiField
	DescriptionData subData;

	@UiField
	DescriptionData stateText;

	@UiField
	DescriptionTitle stateTitle;

	@UiField
	Alert state;

	private Widget rootElement;
	private Application app;
	private int appId = 0;
	private boolean loadingFinished = false;

	private RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

	public AppDetailPage(int applicationId) {

		rootElement = ourUiBinder.createAndBindUi(this);

		RegistrarManager.getApplicationById(applicationId, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject jso) {
				app = jso.cast();
				appId = app.getId();
				form.setOnlyPreview(true);
				form.setSeeHiddenItems(canSeeHiddenFields());
				loadForm();
			}

			@Override
			public void onError(PerunException error) {
				loadingFinished = true;
			}

			@Override
			public void onLoadingStart() {

			}
		});

	}

	public AppDetailPage(Application app) {
		this.app = app;
		appId = app.getId();
		form.setOnlyPreview(true);
		form.setSeeHiddenItems(canSeeHiddenFields());
		rootElement = ourUiBinder.createAndBindUi(this);
		loadForm();
	}

	@UiHandler(value = "backButton")
	public void back(ClickEvent event) {
		History.newItem("submitted");
	}

	@Override
	public boolean isPrepared() {
		return loadingFinished;
	}

	@Override
	public boolean isAuthorized() {
		return (app != null && loadingFinished);
	}

	@Override
	public void onResize() {
	}

	@Override
	public Widget draw() {

		formTitle.setText(translation.formDataTitle());

		if (Application.ApplicationType.INITIAL.equals(app.getType())) {
			text.setText(translation.initialDetail(app.getVo().getName()));
		} else {
			text.setText(translation.extensionDetail(app.getVo().getName()));
		}

		if (app.getGroup() != null) {
			if (Application.ApplicationType.INITIAL.equals(app.getType())) {
				text.setText(translation.initialDetail(app.getGroup().getShortName() + " / " + app.getVo().getName()));
			} else {
				text.setText(translation.extensionDetail(app.getGroup().getShortName() + " / " + app.getVo().getName()));
			}
		}

		subTitle.setText(translation.submittedOn() + " ");
		subData.setText(app.getCreatedAt().split("\\.")[0]);

		if (Application.ApplicationState.NEW.equals(app.getState())) {
			state.setType(AlertType.INFO);
		} else if (Application.ApplicationState.VERIFIED.equals(app.getState())) {
			state.setType(AlertType.WARNING);
		} else if (Application.ApplicationState.APPROVED.equals(app.getState())) {
			state.setType(AlertType.SUCCESS);
		} else if (Application.ApplicationState.REJECTED.equals(app.getState())) {
			state.setType(AlertType.DANGER);
		}

		stateTitle.setText(app.getTranslatedState());
		stateText.setText(app.getModifiedAt().split("\\.")[0]);

		return rootElement;

	}

	@Override
	public Widget getWidget() {
		return rootElement;
	}

	@Override
	public void open() {

	}

	@Override
	public String getUrl() {
		return "detail";
	}

	@Override
	public void toggleHelp() {

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AppDetailPage that = (AppDetailPage) o;

		if (appId != that.appId) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return appId;
	}

	private boolean canSeeHiddenFields() {

		// admins can see hidden fields - users not
		PerunSession sess = PerunSession.getInstance();
		return (sess.isVoAdmin(app.getVo().getId()) || sess.isVoObserver(app.getVo().getId()) || (app.getGroup() != null && sess.isGroupAdmin(app.getGroup().getId())));

	}

	/**
	 * Load form items
	 */
	private void loadForm() {

		RegistrarManager.getApplicationDataById(app.getId(), new JsonEvents() {

			@Override
			public void onFinished(JavaScriptObject jso) {
				ArrayList<ApplicationFormItemData> list = JsUtils.<ApplicationFormItemData>jsoAsList(jso);
				Collections.sort(list, new Comparator<ApplicationFormItemData>() {
					public int compare(ApplicationFormItemData arg0, ApplicationFormItemData arg1) {
						if (arg0.getFormItem() != null && arg1.getFormItem() != null) {
							return arg0.getFormItem().getOrdnum() - arg1.getFormItem().getOrdnum();
						} else {
							// for old data with deleted form items
							return arg0.getShortname().compareTo(arg1.getShortname());
						}
					}
				});
				form.setFormItems(list);
				loadingFinished = true;
			}

			@Override
			public void onError(PerunException error) {

				if (error.getName().equalsIgnoreCase("PrivilegeException")) {
					History.newItem("notauthorized");
				}

			}

			@Override
			public void onLoadingStart() {
				//
			}

		});

	}

}