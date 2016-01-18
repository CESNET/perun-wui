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
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * View for displaying VO/Group registration detail.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AppDetailView extends ViewImpl implements AppDetailPresenter.MyView {

	@Override
	public void setApplication(Application application) {
		this.app = application;

		formWrapper.setVisible(true);
		state.setVisible(true);
		loader.setVisible(false);

		draw();
		loadForm();
	}

	@Override
	public void onLoadingStartApplication() {
		loader.setVisible(true);
		loader.onLoading(translation.loadingApplication());
		text.setText(translation.detailDefaultTitle());
		formWrapper.setVisible(false);
		state.setVisible(false);
	}

	@Override
	public void onErrorApplication(PerunException error, final JsonEvents retry) {
		loader.onError(error, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RegistrarManager.getApplicationDataById(app.getId(), retry);
			}
		});
	}

	PlaceManager placeManager = PerunSession.getPlaceManager();

	interface AppDetailViewUiBinder extends UiBinder<Widget, AppDetailView> {
	}

	@UiField
	Text text;

	@UiField
	PerunButton backButton;

	@UiField
	PerunForm form;

	@UiField
	Form formWrapper;

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

	@UiField
	PerunLoader loader;

	private Application app;

	private PerunRegistrarTranslation translation = GWT.create(PerunRegistrarTranslation.class);

	@Inject
	public AppDetailView(AppDetailViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
		form.setOnlyPreview(true);
	}

	@UiHandler(value = "backButton")
	public void back(ClickEvent event) {
		placeManager.navigateBack();
	}

	public void draw() {

		form.setSeeHiddenItems(canSeeHiddenFields());

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

	}

	private boolean canSeeHiddenFields() {
		// TODO - authorization shouldnt be in gui.
		// admins can see hidden fields - users not
		PerunSession sess = PerunSession.getInstance();
		return (sess.isVoAdmin(app.getVo().getId()) || sess.isVoObserver(app.getVo().getId()) || (app.getGroup() != null && sess.isGroupAdmin(app.getGroup().getId())));

	}


	/**
	 * Load form items
	 */
	private void loadForm() {

		form.clear();

		RegistrarManager.getApplicationDataById(app.getId(), new JsonEvents() {

			final JsonEvents retry = this;

			@Override
			public void onFinished(JavaScriptObject jso) {
				loader.setVisible(false);
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
			}

			@Override
			public void onError(PerunException error) {

				if (error.getName().equalsIgnoreCase("PrivilegeException")) {
					placeManager.revealUnauthorizedPlace("");
				}

				loader.onError(error, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						RegistrarManager.getApplicationDataById(app.getId(), retry);
					}
				});

			}

			@Override
			public void onLoadingStart() {
				loader.setVisible(true);
				loader.onLoading(translation.preparingForm());
			}

		});

	}

}