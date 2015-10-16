package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Placement;

/**
 * Represents submit button and suto submit button form item.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class SubmitButton extends PerunFormItemStatic {

	private boolean autoSubmit;
	private PerunForm form;
	private Row widget;

	public SubmitButton(ApplicationFormItemData item, String lang, PerunForm form, boolean autoSubmit) {
		super(item, lang);
		this.form = form;
		this.autoSubmit = autoSubmit;
	}

	@Override
	protected Widget initFormItem() {

		widget = new Row();
		Column col = new Column(PerunForm.WIDGET_WITH_TEXT_SIZE);
		col.setOffset(PerunForm.LABEL_OFFSET);

		final PerunButton button = new PerunButton();
		button.setIcon(IconType.CHEVRON_RIGHT);
		button.setIconFixedWidth(true);
		button.setTooltipText(getTranslation().checkAndSubmit());
		button.getTooltip().setPlacement(Placement.TOP);
		button.setType(ButtonType.SUCCESS);
		button.setText(getLabelOrShortName());

		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (form != null) {
					form.submit(button);
				}
			}
		});

		col.add(button);
		widget.add(col);
		return widget;
	}

	@Override
	public boolean focus() {
		super.focus();
		for (Widget button : widget) {
			if (button instanceof PerunButton) {
				((PerunButton) button).setFocus(true);
				return true;
			}
		}
		return false;
	}

	public PerunButton getButton() {
		return (PerunButton) ((Column) widget.getWidget(0)).getWidget(0);
	}

	public boolean hasAutoSubmit() {
		return autoSubmit;
	}
}
