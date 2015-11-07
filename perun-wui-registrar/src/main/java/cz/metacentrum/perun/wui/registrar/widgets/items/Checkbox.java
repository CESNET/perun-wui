package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.CheckboxValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Represents checkboxes form item.
 * Value is a list of option names (keys) which is checked separated by pipe '|'. It does not contain unchecked values.
 * Example of value:
 * "Germany|CzechRepublic|Spain"
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Checkbox extends PerunFormItemEditable {

	private final CheckboxValidator validator;

	private FlowPanel widget;

	public Checkbox(ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(item, lang, onlyPreview);
		this.validator = new CheckboxValidator();
	}

	@Override
	protected Widget initWidget() {

		widget = new FlowPanel();
		widget.addStyleName("checkboxFormItem");

		FlowPanel wrapper = new FlowPanel();
		wrapper.getElement().addClassName("checkboxFormItem");

		Map<String, String> opts = parseItemOptions();

		for (Map.Entry<String, String> entry : opts.entrySet()) {

			CheckBox checkBox = new CheckBox(entry.getValue());
			checkBox.setName(entry.getKey());
			widget.add(checkBox);

		}

		return widget;

	}

	@Override
	protected Widget initWidgetOnlyPreview() {
		initWidget();

		for (Widget widget : getWidget()) {
			if (widget instanceof CheckBox) {
				CheckBox checkBox = (CheckBox) widget;
				checkBox.setText(checkBox.getText() + " (" + checkBox.getName() + ")");
				checkBox.setEnabled(false);
			}
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
		if (isOnlyPreview()) {
			return false;
		}
		if (getWidget().getWidgetCount() > 0) {
			((CheckBox) getWidget().getWidget(0)).setFocus(true);
			return true;
		}
		return false;
	}


	@Override
	public void setValidationTriggers() {

		for (Widget widget : getWidget()) {

			CheckBox checkBox = (CheckBox) widget;
			checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					validateLocal();
				}
			});
		}

	}

	@Override
	public String getValue() {

		// rebuild value
		String value = "";
		for (Widget widget : getWidget()) {
			CheckBox checkBox = (CheckBox) widget;
			if (checkBox.getValue()) {
				// put in selected values
				value += checkBox.getName() + "|";
			}
		}
		if (value.length() > 1) {
			value = value.substring(0, value.length() - 1);
		}

		if (value.equals("")) {
			return null;
		}
		return value;
	}

	@Override
	public FlowPanel getWidget() {
		return widget;
	}

	@Override
	protected void setValueImpl(String value) {

		List<String> values = Arrays.asList(value.split("\\|"));

		for (Widget widget : getWidget()) {
			CheckBox checkBox = (CheckBox) widget;
			checkBox.setValue(false);
			for (String val : values) {
				if (checkBox.getName().equals(val)) {
					checkBox.setValue(true);
					break;
				}
			}
		}
	}
}