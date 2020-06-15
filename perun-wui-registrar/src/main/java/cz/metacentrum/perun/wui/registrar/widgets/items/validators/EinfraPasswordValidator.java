package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.regexp.shared.RegExp;
import cz.metacentrum.perun.wui.registrar.widgets.items.Password;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * Specific password validator for EINFRA namespace
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class EinfraPasswordValidator extends PasswordValidator {

	@Override
	public boolean validateLocal(Password password) {

		if (password.isRequired() && isNullOrEmpty(password.getValue())) {
			setResult(Result.EMPTY_PASSWORD);
			password.setRawStatus(getTransl().passEmpty(), ValidationState.ERROR);
			return false;
		}

		// limit only to ASCII printable chars
		RegExp regExp2 = RegExp.compile("^[\\x20-\\x7E]{1,}$");
		if(regExp2.exec(password.getValue()) == null){
			setResult(Result.INVALID_FORMAT);
			password.setRawStatus(getTransl().einfraPasswordFormat(), ValidationState.ERROR);
			return false;
		}

		// Check that password contains at least 3 of 4 character groups

		RegExp regExpDigit = RegExp.compile("^.*[0-9].*$");
		RegExp regExpLower = RegExp.compile("^.*[a-z].*$");
		RegExp regExpUpper = RegExp.compile("^.*[A-Z].*$");
		RegExp regExpSpec = RegExp.compile("^.*[\\x20-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7E].*$"); // FIXME - are those correct printable specific chars?

		int matchCounter = 0;
		if (regExpDigit.exec(password.getValue()) != null) matchCounter++;
		if (regExpLower.exec(password.getValue()) != null) matchCounter++;
		if (regExpUpper.exec(password.getValue()) != null) matchCounter++;
		if (regExpSpec.exec(password.getValue()) != null) matchCounter++;

		if(matchCounter < 3){
			setResult(Result.INVALID_FORMAT);
			password.setRawStatus(getTransl().einfraPasswordStrength(), ValidationState.ERROR);
			return false;
		}

		// check length
		if (password.getValue().length() < 10) {
			setResult(Result.INVALID_FORMAT);
			password.setRawStatus(getTransl().einfraPasswordLength(), ValidationState.ERROR);
			return false;
		}

		// check typos
		if (!password.getPassword().getValue().equals(password.getPasswordSecond().getValue())) {
			setResult(Result.PASSWORD_MISSMATCH);
			password.setStatus(getTransl().passMismatch(), ValidationState.ERROR);
			return false;
		}

		password.setStatus(ValidationState.SUCCESS);
		return true;

	}

}
