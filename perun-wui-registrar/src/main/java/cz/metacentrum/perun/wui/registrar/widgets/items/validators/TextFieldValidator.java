package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.TextField;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * Created by ondrej on 6.10.15.
 */
public class TextFieldValidator extends PerunFormItemValidatorImpl<TextField> {

	@Override
	public boolean validateLocal(TextField textField) {

		if (textField.isRequired() && textField.getValue().isEmpty()) {
			setResult(Result.EMPTY);
			textField.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		if (!textField.getWidget().isValid()) {
			setResult(Result.INVALID_FORMAT);
			textField.setStatus(getTransl().incorrectFormat(), ValidationState.ERROR);
			return false;
		}

		if (textField.getValue().length() > textField.MAX_LENGTH) {
			setResult(Result.TOO_LONG);
			textField.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		textField.setSuccess();
		return true;
	}

}