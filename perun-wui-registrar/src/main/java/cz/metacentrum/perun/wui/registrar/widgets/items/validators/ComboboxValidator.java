package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.Combobox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class ComboboxValidator extends PerunFormItemValidatorImpl<Combobox> {

	@Override
	public boolean validateLocal(Combobox combobox) {

		if (combobox.isRequired() && isNullOrEmpty(combobox.getValue())) {
			setResult(Result.EMPTY);
			combobox.setRawStatus(getTransl().cantBeEmptySelect(), ValidationState.ERROR);
			return false;
		}

		if (combobox.isCustomSelected()
				&& combobox.getValue() != null
				&& combobox.getValue().length() > combobox.MAX_LENGTH) {
			setResult(Result.TOO_LONG);
			combobox.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		if (!combobox.getTextBox().isValid() && combobox.isCustomSelected()) {
			setResult(Result.INVALID_FORMAT);
			combobox.setStatus(getErrorMsgOrDefault(combobox), ValidationState.ERROR);
			return false;
		}

		combobox.setStatus(ValidationState.SUCCESS);
		return true;
	}

}
