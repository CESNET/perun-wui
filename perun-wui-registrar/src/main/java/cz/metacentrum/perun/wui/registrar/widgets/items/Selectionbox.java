package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.Select;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.SelectionboxValidator;

import java.util.Map;

/**
 * Represents SelectionBox item form.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Selectionbox extends PerunFormItemEditable {

	private final SelectionboxValidator validator;

	private Select widget;

	public Selectionbox(ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(item, lang, onlyPreview);
		this.validator = new SelectionboxValidator();
	}

	@Override
	protected Widget initWidget() {

		widget = new Select();
		widget.setWidth("100%");
		widget.setShowTick(true);

		widget.clear();

		if (!isRequired()) {
			widget.addItem(translation.notSelected(), "");
		}

		Map<String, String> opts = parseItemOptions();

		for (Map.Entry<String, String> entry : opts.entrySet()) {
			widget.addItem(entry.getValue(), entry.getKey());
		}

		return widget;

	}

	@Override
	public void validate(Events<Boolean> events) {
		validator.validate(this, events);
	}

	@Override
	public boolean validateLocal() {
		return validator.validateLocal(this);
	}

	@Override
	public PerunFormItemValidator.Result getLastValidationResult() {
		return validator.getLastResult();
	}

	@Override
	public boolean focus() {
		getWidget().setFocus(true);
		return true;
	}

	@Override
	protected void makeOnlyPreviewWidget() {

		if (!getValue().isEmpty()) {
			int i = getWidget().getSelectedIndex();
			getWidget().setItemText(i, getWidget().getSelectedItemText() + " (" + getWidget().getSelectedValue() + ")");
		}

		getWidget().setEnabled(false);

	}


	@Override
	public void setValidationTriggers() {
		getWidget().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				validateLocal();
			}
		});
	}

	@Override
	public String getValue() {
		return getWidget().getValue();
	}

	@Override
	public Select getWidget() {
		return widget;
	}

	@Override
	public void setValue(String value) {
		for (int i = 0; i < getWidget().getItemCount(); i++) {
			if (getWidget().getValue(i).equals(value)) {
				getWidget().setSelectedIndex(i);
				break;
			}
		}
	}

}
