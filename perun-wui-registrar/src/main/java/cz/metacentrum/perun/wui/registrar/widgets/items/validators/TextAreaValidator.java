package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.TextArea;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class TextAreaValidator extends PerunFormItemValidatorImpl<TextArea> {

	@Override
	public boolean validateLocal(TextArea textArea) {

		if (textArea.isRequired() && isNullOrEmpty(textArea.getValue())) {
			setResult(Result.EMPTY);
			textArea.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		if (!textArea.getBox().isValid()) {
			setResult(Result.INVALID_FORMAT);
			textArea.setStatus(getErrorMsgOrDefault(textArea), ValidationState.ERROR);
			return false;
		}

		if (textArea.getValue() != null && textArea.getValue().length() > textArea.MAX_LENGTH) {
			setResult(Result.TOO_LONG);
			textArea.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		textArea.setStatus(ValidationState.SUCCESS);
		return true;
	}

}