package cz.metacentrum.perun.wui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunErrorTranslation;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PerunWebConstants;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.JsonUtils;
import cz.metacentrum.perun.wui.json.managers.UtilsManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.RTMessage;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextArea;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.shared.event.ModalHiddenEvent;
import org.gwtbootstrap3.client.shared.event.ModalHiddenHandler;
import org.gwtbootstrap3.client.shared.event.ModalShownEvent;
import org.gwtbootstrap3.client.shared.event.ModalShownHandler;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.Objects;
import java.util.Set;

/**
 * Error reporting widget used by users which sends reports to RequestTracking system of CESNET.
 * Where the report is actually sent depends on configuration of Perun instance.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ErrorReporter {

	interface PerunErrorReportUiBinder extends UiBinder<Modal, ErrorReporter> {
	}

	private static PerunErrorReportUiBinder ourUiBinder = GWT.create(PerunErrorReportUiBinder.class);

	private static PerunErrorTranslation translation = GWT.create(PerunErrorTranslation.class);

	@UiField PerunButton sendButton;
	@UiField PerunButton cancelButton;
	@UiField ExtendedTextArea message;
	@UiField ExtendedTextBox subject;
	@UiField ModalBody modalBody;
	@UiField FormLabel subjectLabel;
	@UiField FormLabel messageLabel;
	@UiField Text heading;
	private Modal widget;

	public ErrorReporter(final PerunException ex) {
		this(ex, null);
	}

	public ErrorReporter(final PerunException ex, final PerunButton originalReportButton) {

		widget = ourUiBinder.createAndBindUi(this);

		subject.setText("Reported error: " + ex.getRequestURL() + " (" +ex.getErrorId() + ")");
		message.setHeight("100px");

		heading.setText(translation.reportErrorHeading());
		subjectLabel.setText(translation.reportErrorSubjectLabel());
		messageLabel.setText(translation.reportErrorMessageLabel());
		message.setPlaceholder(translation.reportErrorMessagePlaceholder());
		sendButton.setText(translation.reportError());
		cancelButton.setText(translation.cancel());

		widget.addShownHandler(new ModalShownHandler() {
			@Override
			public void onShown(ModalShownEvent evt) {
				message.setFocus(true);
			}
		});

		sendButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (subject.getText().isEmpty()) {
					subject.setText("Reported error: " + ex.getRequestURL() + " (" + ex.getErrorId() + ")");
				}

				final String text = getMessage(ex);
				UtilsManager.sendMessageToRT(subject.getText(), text, new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject jso) {
						sendButton.setProcessing(false);
						showSuccess((RTMessage) jso, originalReportButton);
					}

					@Override
					public void onError(PerunException error) {
						sendButton.setProcessing(false);
						showError(text);
					}

					@Override
					public void onLoadingStart() {
						sendButton.setProcessing(true);
					}
				});

			}
		});

		widget.addHiddenHandler(new ModalHiddenHandler() {
			@Override
			public void onHidden(ModalHiddenEvent modalHiddenEvent) {
				widget.removeFromParent();
			}
		});

	}

	@UiHandler(value = "cancelButton")
	public void cancel(ClickEvent event) {
		widget.hide();
	}

	public Modal getWidget() {
		return widget;
	}

	private String getMessage(PerunException ex) {

		// clear password fields if present
		final JSONObject postObject = new JSONObject(JsonUtils.parseJson(ex.getPostData()));

		clearPasswords(postObject);

		String text = "";
		if (!message.getText().trim().isEmpty()) {
			text += message.getText() + "\n\n";
			text += "-------------------------------------\n";
		}
		text += "Technical details: \n\n";
		text += ex.getErrorId() + " - " + ex.getName() + "\n";
		text += ex.getMessage() + "\n\n";
		text += "Perun instance: " + PerunConfiguration.perunInstanceName()+ "\n";
		text += "Request: " + ex.getRequestURL() + "\n";
		if (postObject != null) text += "Post data: " + postObject.toString() + "\n";
		text += "Application state: " + Window.Location.createUrlBuilder().buildString() + "\n\n";
		text += "Authz: " + PerunSession.getInstance().getRolesString() + "\n\n";

		if (PerunSession.getInstance().getUser() == null) {
			// post original authz if unknown user
			text += "Actor/ExtSource: " + PerunSession.getInstance().getPerunPrincipal().getActor() + " / " +
					PerunSession.getInstance().getPerunPrincipal().getExtSource() + " (" +
					PerunSession.getInstance().getPerunPrincipal().getExtSourceType() + ")" + "\n\n";
		}
		text += "GUI version: " + PerunWebConstants.INSTANCE.guiVersion();
		text += "\n\n-------------------------------------\nSent from Perun UI";
		return text;
	}

	private void showSuccess(RTMessage message, PerunButton originalReportButton) {

		modalBody.clear();
		Alert alert = new Alert();
		alert.setType(AlertType.SUCCESS);
		alert.setDismissable(false);

		if (message.getMemberPreferredEmail() != null) {
			alert.getElement().setInnerHTML(translation.reportErrorSuccess(message.getTicketNumber(), message.getMemberPreferredEmail()));
		} else {
			alert.getElement().setInnerHTML(translation.reportErrorSuccessNoMail(message.getTicketNumber()));
		}

		modalBody.add(alert);

		sendButton.setVisible(false);
		cancelButton.setText(translation.close());

		if (originalReportButton != null) {
			originalReportButton.setEnabled(false);
			originalReportButton.setText(translation.reportErrorEnd());
		}

	}

	private void showError(String text) {

		modalBody.clear();
		message.setHeight("250px");
		Alert alert = new Alert();
		alert.setType(AlertType.DANGER);
		alert.setDismissable(false);
		alert.getElement().setInnerHTML(translation.reportErrorFail(PerunConfiguration.getBrandSupportMail()));
		modalBody.add(alert);
		message.setText(text);
		modalBody.add(message);
		sendButton.setVisible(false);
		cancelButton.setText(translation.close());

	}

	/**
	 * Clear all password-like params from posted objects
	 *
	 * @param object object to clear
	 */
	private static void clearPasswords(JSONObject object) {

		for (String key : object.keySet()) {
			if (key.equals("oldPassword") || key.equals("newPassword") || key.equals("password")) {
				object.put(key, new JSONString(""));
			} else {
				JSONObject obj = object.get(key).isObject();
				if (obj != null) {
					clearPasswords(obj);
				}
			}
		}

		if (object.containsKey("data") && object.get("data") != null) {
			JSONArray formItemsData = object.get("data").isArray();
			if (formItemsData != null) {
				for (int i=0; i<formItemsData.size(); i++) {
					if (formItemsData.get(i) != null) {
						JSONObject formItemWithData = formItemsData.get(i).isObject();
						if (formItemWithData != null && formItemWithData.containsKey("formItem") && formItemWithData.get("formItem") != null) {
							JSONObject formItem = formItemWithData.get("formItem").isObject();
							if (formItem != null && formItem.containsKey("type") && formItem.get("type") != null) {
								JSONString type = formItem.get("type").isString();
								if (type != null && Objects.equals(type.stringValue(), "PASSWORD")) {
									// form item of type password
									if (formItemWithData.containsKey("value")) {
										// clear value property
										formItemWithData.put("value", new JSONString(""));
									}
								}
							}
						}
					}
				}
			}
		}
	}

}
