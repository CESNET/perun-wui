package cz.metacentrum.perun.wui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.model.PerunException;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.base.HasType;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.html.Div;

/**
 * Generic alert widget with retry and report buttons.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AlertErrorReporter extends Div implements HasHTML, HasType<AlertType> {

	interface AlertErrorReporterUiBinder extends UiBinder<Alert, AlertErrorReporter> {
	}

	private static AlertErrorReporterUiBinder ourUiBinder = GWT.create(AlertErrorReporterUiBinder.class);

	private static PerunTranslation translation = GWT.create(PerunTranslation.class);

	@UiField Alert alert;
	@UiField HTML html;
	@UiField PerunButton retry;
	@UiField PerunButton report;
	@UiField ButtonToolBar toolbar;
	private HandlerRegistration lastRetryHandler;

	private PerunException exception;

	/**
	 * Create new default AlertErrorReporter instance
	 */
	public AlertErrorReporter() {

		add(ourUiBinder.createAndBindUi(this));

		retry.setText(translation.retry());
		report.setText(translation.reportError());

		// common error reporting
		report.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (exception != null) {
					ErrorReporter reportBox = new ErrorReporter(exception, report);
					reportBox.getWidget().show();
				}
			}
		});

	}

	/**
	 * Sets action for "Try again" button. If action is null, no button is displayed.
	 *
	 * @param handler Click handler for button
	 */
	public void setRetryHandler(ClickHandler handler) {
		if (handler == null) {
			return;
		}
		retry.setVisible(true);
		retry.getParent().setVisible(true);
		retry.setVisible(true);
		if (lastRetryHandler != null) {
			lastRetryHandler.removeHandler();
		}
		lastRetryHandler = retry.addClickHandler(handler);
	}

	/**
	 * Set PerunException to report (representing current error).
	 * If null, not "Report" button is shown.
	 *
	 * @param exception PerunException to report.
	 */
	public void setReportInfo(PerunException exception) {
		if (exception == null) {
			report.setVisible(false);
			report.getParent().setVisible(false);
			this.exception = null;
		} else {
			if ("0".equals(exception.getErrorId())) {
				// our fake errors - hide submit button
				report.setVisible(false);
				report.getParent().setVisible(false);
				this.exception = null;
			} else {
				// real errors
				report.setVisible(true);
				report.setEnabled(true); // because previous error report might disable it
				report.getParent().setVisible(true);
				this.exception = exception;
			}
		}
	}

	/**
	 * Get button toolbar for adding more action buttons
	 *
	 * @return ButtonToolbar
	 */
	public ButtonToolBar getToolbar() {
		return this.toolbar;
	}

	@Override
	public String getText() {
		return html.getText();
	}

	@Override
	public void setText(String text) {
		html.setText(text);
	}

	@Override
	public String getHTML() {
		return html.getHTML();
	}

	@Override
	public void setHTML(String html) {
		this.html.setHTML(html);
	}

	@Override
	public void setType(AlertType alertType) {
		alert.setType(alertType);
		if      (alertType.equals(AlertType.DANGER)) { report.setType(ButtonType.DANGER); }
		else if (alertType.equals(AlertType.WARNING)) { report.setType(ButtonType.WARNING); }
		else if (alertType.equals(AlertType.INFO)) { report.setType(ButtonType.INFO); }
		else if (alertType.equals(AlertType.SUCCESS)) { report.setType(ButtonType.SUCCESS); }
		else { report.setType(ButtonType.DEFAULT); }
	}

	@Override
	public AlertType getType() {
		return alert.getType();
	}

}
