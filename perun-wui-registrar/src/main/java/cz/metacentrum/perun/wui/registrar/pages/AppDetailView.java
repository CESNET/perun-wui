package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.ErrorTranslator;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItem;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.widgets.AlertErrorReporter;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Text;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

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
		embeddedInfo.setVisible(false);

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
	DescriptionTitle subTitle;

	@UiField
	DescriptionData subData;

	@UiField
	DescriptionData stateText;

	@UiField
	DescriptionTitle stateTitle;

	@UiField
	Alert state;

	@UiField PerunButton editButton;
	@UiField PerunButton saveButton;
	@UiField PerunButton cancelButton;

	@UiField
	PerunLoader loader;

	@UiField
	PerunButton resendNotification;
	HandlerRegistration resendNotificationHandler = null;

	@UiField
	Paragraph mailVerificationText;

	@UiField Alert mailVerificationAlert;

	@UiField
	AlertErrorReporter alertErrorReporter;

	@UiField
	Alert embeddedInfo;

	@UiField
	DescriptionData embeddedInfoText;

	private Application app;

	private PerunRegistrarTranslation translation = GWT.create(PerunRegistrarTranslation.class);

	@Inject
	public AppDetailView(AppDetailViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
		editButton.setText(translation.edit());
		saveButton.setText(translation.save());
		cancelButton.setText(translation.cancel());
		form.setFormState(PerunForm.FormState.PREVIEW);
	}

	@UiHandler(value = "backButton")
	public void back(ClickEvent event) {
		if (PerunForm.FormState.EDIT.equals(form.getFormState())) {
			//
			boolean ok = Window.confirm(translation.cancelAsk());
			if (ok) {
				updateState(false);
				placeManager.navigateBack();
			}
		} else {
			placeManager.navigateBack();
		}
	}

	@UiHandler(value = "editButton")
	public void edit(ClickEvent event) {
		updateState(true);
	}

	@UiHandler(value = "cancelButton")
	public void cancel(ClickEvent event) {
		updateState(false);
	}

	@UiHandler(value = "saveButton")
	public void save(ClickEvent event) {
		form.submitEditedForm(app, new Events<Boolean>() {
			@Override
			public void onFinished(Boolean result) {
				alertErrorReporter.setVisible(false);
				saveButton.setProcessing(false);
				cancelButton.setEnabled(true);
				updateState(false);
				// reload whole application state/form from API
				placeManager.revealCurrentPlace();
			}

			@Override
			public void onError(PerunException error) {
				cancelButton.setEnabled(true);
				saveButton.setProcessing(false);
				if (error != null) {
					alertErrorReporter.setVisible(true);
					alertErrorReporter.setHTML(ErrorTranslator.getTranslatedMessage(error));
					alertErrorReporter.setReportInfo(error);
				} else {
					Window.alert(translation.noChange());
				}
			}

			@Override
			public void onLoadingStart() {
				cancelButton.setEnabled(false);
				saveButton.setProcessing(true);
				alertErrorReporter.setVisible(false);
			}
		});
	}

	private void updateState(boolean edit) {
		form.setFormState(edit ? PerunForm.FormState.EDIT : PerunForm.FormState.PREVIEW);
		form.setFormItems(form.getSourceFormItems());
		editButton.setVisible(!edit);
		saveButton.setVisible(edit);
		cancelButton.setVisible(edit);
	}


	public void draw() {

		form.setSeeHiddenItems(canSeeHiddenFields());

		// hide notice until loaded
		mailVerificationAlert.setVisible(false);

		if (Application.ApplicationType.INITIAL.equals(app.getType())) {
			text.setText(translation.initialDetail(app.getVo().getName()));
		} else {
			text.setText(translation.extensionDetail(app.getVo().getName()));
		}

		if (app.getGroup() != null) {
			if (Application.ApplicationType.INITIAL.equals(app.getType()) || Application.ApplicationType.EMBEDDED.equals(app.getType())) {
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

		if (Application.ApplicationType.EMBEDDED.equals(app.getType())) {
			embeddedInfo.setVisible(true);
			embeddedInfoText.setText(translation.embeddedInfo(app.getVo().getName()));
		}

		// only new and verified application are editable
		editButton.setVisible(Application.ApplicationState.NEW.equals(app.getState()) ||
				Application.ApplicationState.VERIFIED.equals(app.getState()));

	}

	private boolean canSeeHiddenFields() {
		// TODO - authorization shouldn't be in gui.
		// admins can see hidden fields - users not
		PerunSession sess = PerunSession.getInstance();
		return (sess.isPerunAdmin() || sess.isVoAdmin(app.getVo().getId()) || sess.isVoObserver(app.getVo().getId()) || (app.getGroup() != null && sess.isGroupAdmin(app.getGroup().getId())));

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

				// find unverified mail
				boolean found = false;
				for (final ApplicationFormItemData item : list) {
					if (item.getFormItem() != null &&
							item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.VALIDATED_EMAIL) &&
							item.getAssuranceLevelAsInt() < 1) {
						found = true;

						String val = SafeHtmlUtils.fromString((item.getValue()!=null) ? item.getValue() : "").asString();
						mailVerificationText.setHTML(translation.mailVerificationText(val));
						resendNotification.setText(translation.reSendMailVerificationButton());
						// clear previous handler registration on same button
						if (resendNotificationHandler != null) {
							resendNotificationHandler.removeHandler();
						}
						resendNotificationHandler = resendNotification.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {

								RegistrarManager.sendMessage(app.getId(), "MAIL_VALIDATION", null, new JsonEvents() {
									@Override
									public void onFinished(JavaScriptObject result) {
										resendNotification.setProcessing(false);
										Notify.notify(translation.mailVerificationRequestSent(item.getValue()), NotifyType.SUCCESS);
									}

									@Override
									public void onError(PerunException error) {
										if ("ApplicationNotNewException".equalsIgnoreCase(error.getName())) {
											mailVerificationAlert.setVisible(false);
											resendNotification.setVisible(false);
											alertErrorReporter.setType(AlertType.SUCCESS);
											alertErrorReporter.setVisible(true);
											alertErrorReporter.setReportInfo(null);
											alertErrorReporter.setHTML(translation.resendMailAlreadyApproved(Application.translateState(error.getState())));

										} else {
											alertErrorReporter.setVisible(true);
											alertErrorReporter.setHTML(ErrorTranslator.getTranslatedMessage(error));
											alertErrorReporter.setReportInfo(error);
											resendNotification.setProcessing(false);
										}
									}

									@Override
									public void onLoadingStart() {
										resendNotification.setProcessing(true);
										alertErrorReporter.setVisible(false);
									}
								});

							}
						});
					}
				}

				// mail verification is relevant only for new applications, already
				// approved/rejected or verified apps doesn't need another verification.
				if (Application.ApplicationState.NEW.equals(app.getState())) {
					mailVerificationAlert.setVisible(found);
				}

				form.setFormItems(list);

				if (form.hasNoVisibleItems() || form.hasNoUpdatableItems()) {
					editButton.setVisible(false);
				}

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
