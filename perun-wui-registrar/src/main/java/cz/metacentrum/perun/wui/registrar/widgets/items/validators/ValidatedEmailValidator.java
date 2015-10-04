package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.registrar.widgets.items.ValidatedEmail;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * Created by ondrej on 6.10.15.
 */
public class ValidatedEmailValidator extends PerunFormItemValidatorImpl<ValidatedEmail> {

	@Override
	public boolean validateLocal(ValidatedEmail mail) {

		if (mail.isRequired() && mail.getValue().isEmpty()) {
			setResult(Result.EMPTY);
			mail.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		if (!Utils.isValidEmail(mail.getValue()) && !mail.getValue().isEmpty()) {
			setResult(Result.INVALID_FORMAT_EMAIL);
			mail.setStatus(getTransl().incorrectEmail(), ValidationState.ERROR);
			return true;
		}

		if (!mail.getTextBox().isValid()) {
			setResult(Result.INVALID_FORMAT);
			mail.setStatus(getTransl().incorrectFormat(), ValidationState.ERROR);
			return false;
		}

		if (mail.getValue().length() > mail.MAX_LENGTH) {
			setResult(Result.TOO_LONG);
			mail.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		if (!mail.getValidMails().contains(mail.getValue()) && !mail.getValue().isEmpty()) {
			setResult(Result.MUST_VALIDATE_EMAIL);
			mail.setStatus(getTransl().mustValidateEmail(), ValidationState.WARNING);
			return true;
		}

		mail.setSuccess();
		return true;
	}

}