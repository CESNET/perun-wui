package cz.metacentrum.perun.wui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickHandler;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Placement;

/**
 * Custom Button class, which enchances standard bootstrap button with different states
 * and relation to PerunDataGrid widget and it's events.
 *
 * Class also provides methods to get standardized button types like: "ADD, CREATE, REMOVE, DELETE,..."
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunButton extends Button {

	Tooltip tooltip = new Tooltip();
	IconType iconStore;
	boolean tableManaged = false;
	private int processingCounter = 0;
	private PerunDataGrid<? extends JavaScriptObject> table;

	public PerunButton() {
	}

	public PerunButton(String caption) {
		this();
		this.setText(caption);
	}

	public PerunButton(String caption, String tooltip) {
		this(caption);
		setTooltipText(tooltip);
	}

	public PerunButton(String caption, ClickHandler handler) {
		this(caption);
		addClickHandler(handler);
	}

	public PerunButton(String caption, String tooltip, ClickHandler handler) {
		this(caption, handler);
		setTooltipText(tooltip);
	}

	public PerunButton(String caption, IconType icon) {
		this(caption);
		this.setIcon(icon);
		this.iconStore = icon;
	}

	public PerunButton(String caption, String tooltip, IconType icon) {
		this(caption, icon);
		setTooltipText(tooltip);
		this.iconStore = icon;
	}

	public PerunButton(String caption, IconType icon, ClickHandler handler) {
		this(caption, icon);
		addClickHandler(handler);
		this.iconStore = icon;
	}

	public PerunButton(String caption, String tooltip, IconType icon, ClickHandler handler) {
		super(caption, icon, handler);
		setTooltipText(tooltip);
		this.iconStore = icon;
	}

	public static PerunButton getButton(PerunButtonType type, String tooltip) {
		PerunButton button = getButton(type);
		if (tooltip != null) button.setTooltipText(tooltip);
		return button;
	}

	public static PerunButton getButton(PerunButtonType type, ClickHandler handler) {
		PerunButton button = getButton(type);
		if (handler != null) button.addClickHandler(handler);
		return button;
	}

	public static PerunButton getButton(PerunButtonType type, String tooltip, ClickHandler handler) {
		PerunButton button = getButton(type, handler);
		if (tooltip != null) button.setTooltipText(tooltip);
		return button;
	}

	public static PerunButton getButton(PerunButtonType buttonClass, ButtonType type) {
		PerunButton button = getButton(buttonClass);
		button.setType(type);
		return button;
	}

	public static PerunButton getButton(PerunButtonType buttonClass, ButtonType type, String tooltip) {
		PerunButton button = getButton(buttonClass, type);
		if (tooltip != null) button.setTooltipText(tooltip);
		return button;
	}

	public static PerunButton getButton(PerunButtonType buttonClass, ButtonType type, ClickHandler handler) {
		PerunButton button = getButton(buttonClass, type);
		button.addClickHandler(handler);
		return button;
	}

	public static PerunButton getButton(PerunButtonType buttonClass, ButtonType type, String tooltip, ClickHandler handler) {
		PerunButton button = getButton(buttonClass, type, tooltip);
		if (handler != null) button.addClickHandler(handler);
		return button;
	}

	/**
	 * Get default button
	 *
	 * @param type
	 * @return
	 */
	public static PerunButton getButton(PerunButtonType type) {

		PerunTranslation translation = GWT.create(PerunTranslation.class);

		PerunButton button = new PerunButton();

		if (PerunButtonType.ADD.equals(type)) {
			button.setIcon(IconType.PLUS);
			button.setText(translation.add());
			button.setType(ButtonType.SUCCESS);
		} else if (PerunButtonType.REMOVE.equals(type)) {
			button.setText(translation.remove());
			button.setIcon(IconType.TRASH_O);
			button.setType(ButtonType.DANGER);
		} else if (PerunButtonType.CREATE.equals(type)) {
			button.setText(translation.create());
			button.setIcon(IconType.PLUS);
			button.setType(ButtonType.SUCCESS);
		} else if (PerunButtonType.DELETE.equals(type)) {
			button.setText(translation.delete());
			button.setIcon(IconType.TRASH_O);
			button.setType(ButtonType.DANGER);
		} else if (PerunButtonType.OK.equals(type)) {
			button.setText(translation.ok());
		} else if (PerunButtonType.CANCEL.equals(type)) {
			button.setText(translation.cancel());
		} else if (PerunButtonType.YES.equals(type)) {
			button.setText(translation.yes());
		} else if (PerunButtonType.NO.equals(type)) {
			button.setText(translation.yes());
		} else if (PerunButtonType.FILTER.equals(type)) {
			button.setText(translation.filter());
			button.setIcon(IconType.FILTER);
		} else if (PerunButtonType.SEARCH.equals(type)) {
			button.setText(translation.search());
			button.setIcon(IconType.SEARCH);
		} else if (PerunButtonType.SAVE.equals(type)) {
			button.setText(translation.save());
			button.setIcon(IconType.SAVE);
		} else if (PerunButtonType.EDIT.equals(type)) {
			button.setText(translation.edit());
			button.setIcon(IconType.PENCIL);
		} else if (PerunButtonType.ASSIGN.equals(type)) {
			button.setText(translation.assign());
			button.setIcon(IconType.PLUS);
		} else if (PerunButtonType.REFRESH.equals(type)) {
			button.setText(translation.refresh());
			button.setIcon(IconType.REFRESH);
		} else if (PerunButtonType.REPORT_ERROR.equals(type)) {
			button.setText(translation.reportError());
			button.setIcon(IconType.BUG);
		} else if (PerunButtonType.COPY.equals(type)) {
			button.setText(translation.copy());
			button.setIcon(IconType.COPY);
		} else if (PerunButtonType.BACK.equals(type)) {
			button.setText(translation.back());
			button.setIcon(IconType.ARROW_LEFT);
		} else if (PerunButtonType.NEXT.equals(type)) {
			button.setText(translation.next());
			button.setIcon(IconType.ARROW_RIGHT);
			button.setIconPosition(IconPosition.RIGHT);
		} else if (PerunButtonType.EXIT.equals(type)) {
			button.setText(translation.exit());
			button.setIcon(IconType.SIGN_OUT);
		} else if (PerunButtonType.APPROVE.equals(type)) {
			button.setText(translation.approve());
			button.setIcon(IconType.CHECK);
		} else if (PerunButtonType.REJECT.equals(type)) {
			button.setText(translation.reject());
			button.setIcon(IconType.MINUS);
		} else if (PerunButtonType.VERIFY.equals(type)) {
			button.setText(translation.verify());
			button.setIcon(IconType.CERTIFICATE);
		} else if (PerunButtonType.CONTINUE.equals(type)) {
			button.setText(translation.continue_());
			button.setIcon(IconType.CHEVRON_RIGHT);
			button.setIconPosition(IconPosition.RIGHT);
		}

		return button;

	}

	/**
	 * Sets button's tooltip text. If null or empty, then tooltip widget is temporarily removed from button to prevent
	 * displaying 'null' message.
	 *
	 * @param message tooltip text to display.
	 */
	public void setTooltipText(String message) {

		if (message == null || message.trim().isEmpty()) {
			// on empty remove tooltip
			if (this.tooltip.getWidget() != null) this.tooltip.remove(this);
			this.tooltip.reconfigure();
		} else {
			// set tooltip text
			this.tooltip.setTitle(message);
			this.tooltip.setWidget(this);
			this.tooltip.setShowDelayMs(500);
			this.tooltip.setIsAnimated(true);
			this.tooltip.setPlacement(Placement.BOTTOM);
			this.tooltip.reconfigure();
		}

	}

	@Override
	public void setIcon(IconType type) {
		iconStore = type;
		super.setIcon(type);
	}

	/**
	 * Temporary replace Icon (keep original in iconStore).
	 * Used for setting button to processing state.
	 *
	 * @param type Icon to set
	 */
	private void replaceIcon(IconType type) {
		super.setIcon(type);
	}

	/**
	 * Get tooltip content (message).
	 *
	 * @return tooltip text
	 */
	public String getTooltipText() {
		return this.tooltip.getTitle();
	}

	/**
	 * Get Tooltip widget associated with this button. Can be used to dynamically change properties, like
	 * tooltip's location (under, left, right, above) etc.
	 *
	 * @return tooltip widget
	 */
	public Tooltip getTooltip() {
		return this.tooltip;
	}

	/**
	 * Sets processing state on button. If set to TRUE, then button is disabled
	 * and icon is replaced by loading spinner. If set to FALSE, button is set back to normal.
	 *
	 * If call results in not-processing state, then button is enabled only, if it's not "table managed"
	 * or associated table has some items selected and is not in loading state.
	 *
	 * Every setProcessing() call is counted, so in order to change state to not-processing
	 * you must call setProcessing(false) for every setProcessing(true).
	 *
	 * @param processing TRUE = set button to processing state / FALSE = set not processing
	 */
	public void setProcessing(boolean processing) {

		// set processing for each callback
		if (processing) processingCounter++;
		if (!processing) processingCounter--;
		if (processingCounter < 0) processingCounter = 0;

		if (processingCounter > 0) {
			this.setEnabled(false);
			this.replaceIcon(IconType.SPINNER);
			this.setIconSpin(true);
		} else {
			this.setIcon(iconStore);
			this.setIconSpin(false);
			if ((!tableManaged) || (!table.getSelectedList().isEmpty() && table.isRowCountExact())) {
				// enable only if standard button or items in table are selected and is table is not loading
				this.setEnabled(true);
			}
		}

	}

	/**
	 * Determine, if button click action is currently processing.
	 *
	 * @return TRUE = button is processing / FALSE otherwise
	 */
	public boolean isProcessing() {
		return (processingCounter > 0);
	}

	/**
	 * Return TRUE if button enable/disable state is managed by table.
	 *
	 * @return TRUE = is table managed button / FALSE = otherwise
	 */
	public boolean isTableManaged() {
		return tableManaged;
	}

	/**
	 * Set button as table managed. So even when processing is over,
	 * button is not enabled, if table is loading or no item is selected
	 *
	 * @param table Table which manages this button
	 */
	public void setTableManaged(PerunDataGrid<?> table) {
		this.tableManaged = true;
		this.table = table;
	}

}
