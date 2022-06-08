package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.ListBox;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * Validator for ListBox
 *
 * @author Jakub Hejda <Jakub.Hejda@cesnet.cz>
 */
public class ListBoxValidator extends PerunFormItemValidatorImpl<ListBox>{
	@Override
	public boolean validateLocal(ListBox listBox) {
		if (listBox.isRequired() && isNullOrEmpty(listBox.getValue())) {
			setResult(Result.EMPTY);
			listBox.setRawStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		String wrongValues = "";
		int index = 1;
		for (ExtendedTextBox lBox : listBox.getListValue()) {
			if (!lBox.isValid()) {
				String value = lBox.getValue().isEmpty() ? "\"\"" : lBox.getValue();
				if (lBox.getValue().length() > 25) {
					wrongValues += "<br>" + index + ". " + value.substring(0, 23) + "...";
				} else {
					wrongValues += "<br>" + index + ". " + value;
				}
			}
			index++;
		}
		if (!wrongValues.isEmpty()) {
			setResult(Result.INVALID_FORMAT);
			listBox.setRawStatus(getTransl().incorrectFormatItemList() + " <b>" + wrongValues + "</b>" , ValidationState.ERROR);
			return false;
		}

		listBox.setStatus(ValidationState.SUCCESS);
		return true;
	}
}
