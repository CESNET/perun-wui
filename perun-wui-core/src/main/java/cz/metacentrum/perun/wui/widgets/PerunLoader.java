package cz.metacentrum.perun.wui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.PerunException;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.ProgressBar;
import org.gwtbootstrap3.client.ui.constants.ProgressBarType;

//TODO fix width of widget
/**
 * Loading widget with progress bar and custom states (loading, finished, error, filter).
 * It can be used as part of PerunDataGrid or simple page. When loader is set to error
 * state, error message is displayed with buttons to re-try and to report error.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunLoader extends Composite {

	public enum PerunLoaderState {
		loading,
		finished,
		error,
		filter
	}

	private PerunLoaderState state = PerunLoaderState.loading;

	interface PerunLoaderUiBinder extends UiBinder<Widget, PerunLoader> {
	}

	private static PerunLoaderUiBinder ourUiBinder = GWT.create(PerunLoaderUiBinder.class);

	Widget rootElement;
	@UiField ProgressBar bar;
	@UiField Alert alert;
	@UiField Alert message;
	@UiField ButtonToolBar tool;
	@UiField PerunButton retry;
	@UiField PerunButton report;

	public PerunLoader() {

		rootElement = ourUiBinder.createAndBindUi(this);
		initWidget(rootElement);
		bar.setPercent(0);
		alert.setVisible(false);

	}

	public Widget getWidget() {
		return rootElement;
	}

	public ProgressBar getProgressBar() {
		return bar;
	}

	public PerunLoaderState getLoaderState() {
		return this.state;
	}

	public void onFilter(String text) {

		state = PerunLoaderState.finished;
		// TODO

	}

	public void onLoading() {

		state = PerunLoaderState.loading;
		bar.setPercent(100);
		bar.setType(ProgressBarType.DEFAULT);
		alert.setVisible(false);

		/*Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand() {
			@Override
			public boolean execute() {
				if (state.equals(PerunLoaderState.loading)) {
					if (bar.getPercent() < 100) {
						bar.setPercent(bar.getPercent()+1);
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

	public void onEmpty(){

		state = PerunLoaderState.finished;

		/*
		bar.setVisible(false);
		alert.setVisible(false);

		// TODO ALERT
		message.setText("No items found.");
		message.setVisible(true);
		*/

	}

	public void onFinished(){

		state = PerunLoaderState.finished;
		bar.setVisible(false);

		// TODO ALERT
		message.setText("Loading finished.");
		message.setVisible(true);

	}

	public void onError(PerunException error, final ClickHandler handler) {

		state = PerunLoaderState.error;
		bar.setVisible(true);
		bar.setType(ProgressBarType.DANGER);
		alert.setText(error.getName()+": "+error.getMessage());
		alert.setVisible(true);

		tool.setVisible(true);
		retry.addClickHandler(handler);

	}

}