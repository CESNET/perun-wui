package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemTexts;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import org.gwtbootstrap3.client.ui.CheckBox;

/**
 * Represents checkboxes for Group selection.
 * Value is a list of group names and ids, separated by "#". Groups are separated by pipe '|'.
 * It does not contain unchecked values.
 *
 * Example of a value:
 * "Group A#124|Group B#1212|Group C#1212"
 *
 * @author Vojtech Sassmann
 */
public class GroupCheckBox extends Checkbox {

	public GroupCheckBox(PerunForm form, ApplicationFormItemData item, String lang) {
		super(form, item, lang);
	}

	private boolean preview = false;

	@Override
	protected Widget initWidgetOnlyPreview() {
		setSelectOptionsFromValue();

		Widget widget = super.initWidgetOnlyPreview();

		checkAll();

		preview = true;

		return widget;
	}

	@Override
	protected void setValueImpl(String value) {
		if (preview) {
			return;
		}
		super.setValueImpl(value);
	}

	/**
	 * Check all checkboxes.
	 */
	private void checkAll() {
		for (Widget widget : getWidget()) {
			if (widget instanceof CheckBox) {
				CheckBox checkBox = (CheckBox) widget;
				checkBox.setValue(true);
			}
		}
	}

	/**
	 * As available checkboxes, sets the checkboxes, that were selected by the user.
	 */
	private void setSelectOptionsFromValue() {
		if (getItemData().getValue().isEmpty()) {
			return;
		}

		String options = "";

		String[] groups = getItemData().getValue().split("\\|");
		for (String group : groups) {
			String[] groupSplit = group.split("#");
			options += groupSplit[1] + "#" + groupSplit[0];
			options += "|";
		}
		options = options.substring(0, options.length() - 1);

		ApplicationFormItemTexts cs = getItemData().getFormItem().getItemTexts("cs");
		ApplicationFormItemTexts en = getItemData().getFormItem().getItemTexts("en");

		cs.setOptions(options);
		en.setOptions(options);
	}

	@Override
	public void setEnabled(boolean enabled) {
		// FIXME - Hack, for now, we expect that the item was disabled only on the application detail.
		if (!enabled) {
			checkAll();
		}
		super.setEnabled(enabled);
	}

	@Override
	public String getValue() {

		// rebuild value
		String value = "";
		for (Widget widget : getWidget()) {
			CheckBox checkBox = (CheckBox) widget;
			if (checkBox.getValue()) {
				// put in selected values
				value += checkBox.getText() + "#" + checkBox.getName() + "|";
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

}
