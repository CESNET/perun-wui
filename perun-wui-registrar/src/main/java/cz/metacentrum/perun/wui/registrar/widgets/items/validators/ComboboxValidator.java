package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.Combobox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * Created by ondrej on 6.10.15.
 */
public class ComboboxValidator extends PerunFormItemValidatorImpl<Combobox> {

	@Override
	public boolean validateLocal(Combobox combobox) {

		if (combobox.getValue().length() > combobox.MAX_LENGTH && combobox.isCustomSelected()) {
			setResult(Result.TOO_LONG);
			combobox.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		if (combobox.isRequired() && combobox.getValue().isEmpty()) {
			setResult(Result.EMPTY);
			combobox.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		if (!combobox.getTextBox().isValid() && combobox.isCustomSelected()) {
			setResult(Result.INVALID_FORMAT);
			combobox.setStatus(getTransl().incorrectFormat(), ValidationState.ERROR);
			return false;
		}

		if (combobox.getValue().length() > combobox.MAX_LENGTH && combobox.isCustomSelected()) {
			setResult(Result.TOO_LONG);
			combobox.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		combobox.setSuccess();
		return true;
	}

}