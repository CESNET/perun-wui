package cz.metacentrum.perun.wui.widgets.boxes;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle;
import cz.metacentrum.perun.wui.widgets.SuggestBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.base.ValueBoxBase;
import org.gwtbootstrap3.client.ui.constants.Styles;

/**
 * Extended SuggestBox with support for onPaste events
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ExtendedSuggestBox extends SuggestBox {

	// custom class counter
	private static int counter = 0;

	/**
	 * Constructor for {@link SuggestBox}. Creates a {@link com.google.gwt.user.client.ui.MultiWordSuggestOracle} and {@link org.gwtbootstrap3.client.ui.TextBox} to use
	 * with this {@link SuggestBox}.
	 */
	public ExtendedSuggestBox() {
		this(new MultiWordSuggestOracle());
	}

	/**
	 * Constructor for {@link SuggestBox}. Creates a {@link org.gwtbootstrap3.client.ui.TextBox} to use with this {@link SuggestBox}.
	 *
	 * @param oracle the oracle for this <code>SuggestBox</code>
	 */
	public ExtendedSuggestBox(SuggestOracle oracle) {
		this(oracle, new TextBox());
	}

	/**
	 * Constructor for {@link SuggestBox}. The text box will be removed from it's current location and wrapped
	 * by the {@link SuggestBox}.
	 *
	 * @param oracle supplies suggestions based upon the current contents of the text widget
	 * @param box the text widget
	 */
	public ExtendedSuggestBox(SuggestOracle oracle, ValueBoxBase<String> box) {
		this(oracle, box, new DefaultSuggestionDisplay());
	}

	/**
	 * Constructor for {@link SuggestBox}. The text box will be removed from it's current location and wrapped
	 * by the {@link SuggestBox}.
	 *
	 * @param oracle supplies suggestions based upon the current contents of the text widget
	 * @param box the text widget
	 * @param suggestDisplay the class used to display suggestions
	 */
	public ExtendedSuggestBox(SuggestOracle oracle, ValueBoxBase<String> box, SuggestionDisplay suggestDisplay) {
		super(oracle, box, suggestDisplay);
		setStyleName(Styles.FORM_CONTROL);

		sinkEvents(Event.ONPASTE);
		// bind custom events
		this.getElement().addClassName("suggest" + counter++);
		setCutCopyPasteHandler("suggest" + counter);

	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (DOM.eventGetType(event)) {
			case Event.ONPASTE:
				Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
					@Override
					public void execute() {
						ValueChangeEvent.fire(ExtendedSuggestBox.this, getValue());
					}
				});
				break;
		}
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
