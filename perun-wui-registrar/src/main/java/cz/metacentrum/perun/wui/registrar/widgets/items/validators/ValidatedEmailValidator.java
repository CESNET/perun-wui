package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.registrar.widgets.items.ValidatedEmail;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class ValidatedEmailValidator extends PerunFormItemValidatorImpl<ValidatedEmail> {

	@Override
	public boolean validateLocal(ValidatedEmail mail) {

		if (mail.isRequired() && isNullOrEmpty(mail.getValue())) {
			setResult(Result.EMPTY);
			mail.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		if (!Utils.isValidEmail(mail.getValue()) && !isNullOrEmpty(mail.getValue())) {
			setResult(Result.INVALID_FORMAT_EMAIL);
			mail.setStatus(getTransl().incorrectEmail(), ValidationState.ERROR);
			return false;
		}

		if (!mail.getTextBox().isValid()) {
			setResult(Result.INVALID_FORMAT);
			mail.setStatus(getErrorMsgOrDefault(mail), ValidationState.ERROR);
			return false;
		}

		if (mail.getValue() != null && mail.getValue().length() > mail.MAX_LENGTH) {
			setResult(Result.TOO_LONG);
			mail.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		if (!mail.getValidMails().contains(mail.getValue()) && !isNullOrEmpty(mail.getValue())) {
			setResult(Result.MUST_VALIDATE_EMAIL);
			mail.setStatus(getTransl().mustValidateEmail(), ValidationState.WARNING);
			return true;
		}

		mail.setStatus(ValidationState.SUCCESS);
		return true;
	}

}