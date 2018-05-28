package cz.metacentrum.perun.wui.widgets.boxes;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.constants.InputType;

/**
 * Extended PasswordTextBox with following functions:
 *
 *  - getValue() returns safe value (non-NULL trimmed string).
 *  - handles browsers ONPASTE action as change event.
 *  - bind keyboard shortcuts for cut,copy,paste to onKeyUp event.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ExtendedPasswordTextBox extends Input {

	// custom class counter
	private static int counter = 0;
	// local input check
	private String regex = "";
	private String okMessage = "";
	private String errorMessage = "";
	private String helpMessage = "";

	public ExtendedPasswordTextBox() {
		super(InputType.PASSWORD);
		sinkEvents(Event.ONPASTE);
		// bind custom events
		this.getElement().addClassName("password"+counter++);
		setCutCopyPasteHandler("password"+counter);
	}

	/**
	 * Get regex value associated with TextBox used to validate input.
	 *
	 * @return regex value
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * Set regex value used to validate input.
	 *
	 * @param regex regex value
	 */
	public void setRegex(String regex) {
		if (regex == null) this.regex = "";
		this.regex = regex;
	}

	/**
	 * Get message that should be displayed when input validation is OK.
	 *
	 * @return OK message
	 */
	public String getOkMessage() {
		return okMessage;
	}

	/**
	 * Set message that should be displayed when input validation is OK.
	 *
	 * @param okMessage OK message
	 */
	public void setOkMessage(String okMessage) {
		if (okMessage == null) this.okMessage = "";
		this.okMessage = okMessage;
	}

	/**
	 * Get message that should be displayed when input validation fails.
	 *
	 * @return Error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Set message that should be displayed when input validation fails.
	 *
	 * @param errorMessage Error message
	 */
	public void setErrorMessage(String errorMessage) {
		if (errorMessage == null) this.errorMessage = "";
		this.errorMessage = errorMessage;
	}

	/**
	 * Get message that should be displayed as help to users.
	 *
	 * @return Help message
	 */
	public String getHelpMessage() {
		return helpMessage;
	}

	/**
	 * Set message that should be displayed as help to users.
	 *
	 * @param helpMessage Help message
	 */
	public void setHelpMessage(String helpMessage) {
		if (helpMessage == null) this.helpMessage = "";
		this.helpMessage = helpMessage;
	}

	/**
	 * Return TRUE if input in TextBox is valid, according to the regex.
	 * If regex not set, then return TRUE. Return FALSE otherwise.
	 *
	 * @return TRUE = valid input / FALSE = not valid input
	 */
	public boolean isValid() {

		if (!regex.equals("")) {

			RegExp regExp = RegExp.compile(regex);
			MatchResult matcher = regExp.exec(getValue());
			boolean matchFound = (matcher != null); // equivalent to regExp.test(inputStr);
			if(!matchFound){
				return false;
			}
		}
		return true;

	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) {
			case Event.ONPASTE:
				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
					@Override
					public void execute() {
						ValueChangeEvent.fire(ExtendedPasswordTextBox.this, getValue());
					}
				});
				break;
		}
	}

	@Override
	public String getValue() {
		return (super.getValue() != null) ? super.getValue() : "";
	}

	/**
	 * Assign cup,copy,paste change events to onKeyUp() action.
	 *
	 * @param id unique ID of each widget
	 */
	private final native void setCutCopyPasteHandler(String id) /*-{
		$wnd.jQuery.ready(function() {
			$wnd.jQuery('#'+id).bind('cut', function(e) {
				$wnd.jQuery('#'+id).onkeyup()
			});
			$wnd.jQuery('#'+id).bind('copy', function(e) {
				$wnd.jQuery('#'+id).onkeyup()
			});
			$wnd.jQuery('#'+id).bind('paste', function(e) {
				$wnd.jQuery('#'+id).onkeyup()
			});
		});
	}-*/;

}
