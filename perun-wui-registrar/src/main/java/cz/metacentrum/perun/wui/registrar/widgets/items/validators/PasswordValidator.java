package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.Password;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PasswordValidator extends PerunFormItemValidatorImpl<Password> {

	@Override
	public boolean validateLocal(Password password) {

		if (password.isRequired() && password.getValue().isEmpty()) {
			setResult(Result.EMPTY_PASSWORD);
			password.setStatus(getTransl().passEmpty(), ValidationState.ERROR);
			return false;
		}

		if (!password.getPassword().isValid()) {
			setResult(Result.INVALID_FORMAT);
			password.setStatus(getErrorMsgOrDefault(password), ValidationState.ERROR);
			return false;
		}

		if (password.getValue().length() > password.MAX_LENGTH) {
			setResult(Result.TOO_LONG);
			password.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		if (!password.getPassword().getValue().equals(password.getPasswordSecond().getValue())) {
			setResult(Result.PASSWORD_MISSMATCH);
			password.setStatus(getTransl().passMismatch(), ValidationState.ERROR);
			return false;
		}

		password.setStatus(ValidationState.SUCCESS);
		return true;
	}

}