package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.TextArea;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * Created by ondrej on 6.10.15.
 */
public class TextAreaValidator extends PerunFormItemValidatorImpl<TextArea> {

	@Override
	public boolean validateLocal(TextArea textArea) {

		if (textArea.isRequired() && textArea.getValue().isEmpty()) {
			setResult(Result.EMPTY);
			textArea.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		if (!textArea.getWidget().isValid()) {
			setResult(Result.INVALID_FORMAT);
			textArea.setStatus(getTransl().incorrectFormat(), ValidationState.ERROR);
			return false;
		}

		if (textArea.getValue().length() > textArea.MAX_LENGTH) {
			setResult(Result.TOO_LONG);
			textArea.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		textArea.setSuccess();
		return true;
	}

}