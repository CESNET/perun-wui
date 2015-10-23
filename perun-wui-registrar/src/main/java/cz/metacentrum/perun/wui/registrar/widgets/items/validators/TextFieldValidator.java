package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.TextField;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class TextFieldValidator extends PerunFormItemValidatorImpl<TextField> {

	@Override
	public boolean validateLocal(TextField textField) {

		if (textField.isRequired() && textField.getValue().isEmpty()) {
			setResult(Result.EMPTY);
			textField.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		if (!textField.getBox().isValid()) {
			setResult(Result.INVALID_FORMAT);
			textField.setStatus(getErrorMsgOrDefault(textField), ValidationState.ERROR);
			return false;
		}

		if (textField.getValue().length() > textField.MAX_LENGTH) {
			setResult(Result.TOO_LONG);
			textField.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		textField.setStatus(ValidationState.SUCCESS);
		return true;
	}

}