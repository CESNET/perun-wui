package cz.metacentrum.perun.wui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.ErrorTranslator;
import cz.metacentrum.perun.wui.model.PerunException;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Progress;
import org.gwtbootstrap3.client.ui.ProgressBar;
import org.gwtbootstrap3.client.ui.constants.ProgressBarType;

/**
 * Loading widget with progress bar and custom states (loading, finished, error, filter).
 * It can be used as part of PerunDataGrid or simple page. When loader is set to error
 * state, error message is displayed with buttons to re-try and to report error.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunLoader extends Composite {

	/**
	 * Possible states of PerunLoader
	 */
	public enum PerunLoaderState {
		loading,
		finished,
		error,
		filter,
		empty
	}

	private PerunLoaderState state = PerunLoaderState.empty;

	interface PerunLoaderUiBinder extends UiBinder<Widget, PerunLoader> {
	}

	private static PerunLoaderUiBinder ourUiBinder = GWT.create(PerunLoaderUiBinder.class);

	Widget rootElement;
	@UiField Progress progress;
	@UiField ProgressBar bar;
	@UiField AlertErrorReporter alert;
	@UiField Alert message;
	private HandlerRegistration lastRetryHandler;

	String emptyMessage = "No items found.";

	private PerunException catchedException;

	/**
	 * Create new default PerunLoader instance
	 */
	public PerunLoader() {

		rootElement = ourUiBinder.createAndBindUi(this);
		initWidget(rootElement);
		bar.setPercent(0);
		alert.setVisible(false);

	}

	/**
	 * Get PerunLoader widget itself
	 *
	 * @return loader widget
	 */
	public Widget getWidget() {
		return rootElement;
	}

	/**
	 * Get progress bar
	 *
	 * @return progress bar
	 */
	public ProgressBar getProgressBar() {
		return bar;
	}

	/**
	 * Get current state of PerunLoader e.g. loading, finished,...
	 *
	 * @return loader state
	 */
	public PerunLoaderState getLoaderState() {
		return this.state;
	}

	/**
	 * Set custom message to display when table is empty.
	 *
	 * @param message Message to display
	 */
	public void setEmptyMessage(String message) {
		this.emptyMessage = message;
	}

	/**
	 * Called by table when filtering is done.
	 *
	 * @param text filtering string
	 */
	public void onFilter(String text) {

		state = PerunLoaderState.filter;
		progress.setVisible(false);

		// TODO ALERT
		message.setText("No results match the filter '" + text + "'.");
		message.setVisible(true);

	}

	/**
	 * Called by table when it's loading data
	 */
	public void onLoading() {

		setVisible(true);
		progress.setVisible(true);
		message.setVisible(false);
		alert.setVisible(false);

		state = PerunLoaderState.loading;
		progress.setActive(true);
		bar.setPercent(100);
		bar.setType(ProgressBarType.DEFAULT);
		bar.setVisible(true);

		/*Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand() {
			@Override
			public boolean execute() {
				if (state.equals(PerunLoaderState.loading)) {
					if (bar.getPercent() < 100) {
						bar.setPercent(bar.getPercent() + 1);
						return true;
					}
					return false;
				} else {
					return false;
				}
			}
		}, 100);*/

	}

	public void onLoading(String label) {

		onLoading();

		bar.setText(label);

	}

	/**
	 * Called by table when content is cleared.
	 */
	public void onEmpty(){

		state = PerunLoaderState.empty;
		progress.setVisible(false);
		message.setText(emptyMessage);
		message.setVisible(true);
	}

	/**
	 * Called by table when loading is finished and result is not empty.
	 */
	public void onFinished(){

		progress.setVisible(true);
		message.setVisible(true);
		alert.setVisible(false);

		state = PerunLoaderState.finished;
		bar.setVisible(false);

		// TODO ALERT
		message.setText("Loading finished.");

	}

	/**
	 * Called by table when loading is finished, but resulting list is empty.
	 */
	public void onFinishedEmpty(){

		state = PerunLoaderState.empty;
		progress.setVisible(false);

		message.setText("Loading finished. No results found.");
		message.setVisible(true);

	}

	/**
	 * Called by table when loading ends with error.
	 */
	public void onError(PerunException error, final ClickHandler handler) {

		progress.setVisible(true);
		bar.setVisible(true);
		message.setVisible(false);
		alert.setVisible(true);

		state = PerunLoaderState.error;
		progress.setActive(false);
		bar.setType(ProgressBarType.DANGER);
		alert.setHTML(ErrorTranslator.getTranslatedMessage(error));

		alert.setRetryHandler(handler);
		alert.setReportInfo(error);

	}

}