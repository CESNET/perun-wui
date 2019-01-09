package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import cz.metacentrum.perun.wui.registrar.widgets.items.Password;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * Specific password validato for EINFRA namespace
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class EinfraPasswordValidator extends PasswordValidator {

	@Override
	public boolean validateLocal(Password password) {

		if (password.isRequired() && isNullOrEmpty(password.getValue())) {
			setResult(Result.EMPTY_PASSWORD);
			password.setStatus(getTransl().passEmpty(), ValidationState.ERROR);
			return false;
		}

		/*
		if (!password.getPassword().isValid()) {
			setResult(Result.INVALID_FORMAT);
			password.setStatus(getErrorMsgOrDefault(password), ValidationState.ERROR);
			return false;
		}
		*/

		// check length
		if (password.getValue().length() < 8) {
			setResult(Result.INVALID_FORMAT);
			password.setStatus(getTransl().metaLength(), ValidationState.ERROR);
			return false;
		}

		// Check format with three chars and at least one non-char in some place
		RegExp regExp = RegExp.compile("^((([^a-zA-Z].*[a-zA-Z].*[a-zA-Z].*[a-zA-Z].*)|([a-zA-Z].*[^a-zA-Z].*[a-zA-Z].*[a-zA-Z].*)|([a-zA-Z].*[a-zA-Z].*[^a-zA-Z].*[a-zA-Z].*)|([a-zA-Z].*[a-zA-Z].*[a-zA-Z].*[^a-zA-Z].*)))$");
		MatchResult matcher = regExp.exec(password.getValue());
		boolean matchFound = (matcher != null);
		if(!matchFound){
			setResult(Result.INVALID_FORMAT);
			password.setStatus(getTransl().metaStrength(), ValidationState.ERROR);
			return false;
		}

		// limit only to ASCII
		RegExp regExp2 = RegExp.compile("^[\\x20-\\x7E]{8,}$");
		MatchResult matcher2 = regExp2.exec(password.getValue());
		boolean matchFound2 = (matcher2 != null);
		if(!matchFound2){
			setResult(Result.INVALID_FORMAT);
			password.setStatus(getTransl().metaStrength(), ValidationState.ERROR);
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
