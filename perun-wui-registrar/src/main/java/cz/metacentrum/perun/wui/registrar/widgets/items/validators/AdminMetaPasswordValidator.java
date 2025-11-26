package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.regexp.shared.RegExp;
import cz.metacentrum.perun.wui.registrar.widgets.items.Password;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;
import cz.metacentrum.perun.wui.registrar.widgets.items.Username;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.List;
import java.util.Objects;

/**
 * Specific password validator for admin-meta namespace
 *
 * TODO: We must revalidate this form item when person`s name changes after entering password value !!
 */
public class AdminMetaPasswordValidator extends PasswordValidator {

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
			password.setRawStatus(getTransl().adminMetaPasswordFormat(), ValidationState.ERROR);
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
			password.setRawStatus(getTransl().adminMetaPasswordStrength(), ValidationState.ERROR);
			return false;
		}

		if (!checkOnNamesAndLogins(getLoginValue(password), password, "[-_]")) {
			setResult(Result.INVALID_FORMAT);
			password.setStatus(getTransl().passwordLogin(), ValidationState.ERROR);
			return false;
		}
		if (!checkOnNamesAndLogins(getName(password), password, null)) {
			setResult(Result.INVALID_FORMAT);
			password.setStatus(getTransl().passwordName(), ValidationState.ERROR);
			return false;
		}
		if (!checkOnNamesAndLogins(getFirstName(password), password, null)) {
			setResult(Result.INVALID_FORMAT);
			password.setStatus(getTransl().passwordName(), ValidationState.ERROR);
			return false;
		}
		if (!checkOnNamesAndLogins(getSurname(password), password, null)) {
			setResult(Result.INVALID_FORMAT);
			password.setStatus(getTransl().passwordName(), ValidationState.ERROR);
			return false;
		}

		// check length
		if (password.getValue().length() < 12) {
			setResult(Result.INVALID_FORMAT);
			password.setRawStatus(getTransl().adminMetaPasswordLength(), ValidationState.ERROR);
			return false;
		}

		// check typos
		if (!password.getPassword().getValue().equals(password.getPasswordSecond().getValue())) {
			if (isNullOrEmpty(password.getPasswordSecond().getValue())) {
				setResult(Result.SECOND_PASSWORD_EMPTY);
				password.setStatus(getTransl().secondPassEmpty(), ValidationState.WARNING);
				return false;
			}
			setResult(Result.PASSWORD_MISSMATCH);
			password.setStatus(getTransl().passMismatch(), ValidationState.ERROR);
			return false;
		}

		password.setStatus(ValidationState.SUCCESS);
		return true;

	}

	public static String reverse(String string) {
		if (string == null || string.isEmpty() || string.length() == 1) return string;
		return string.charAt(string.length()-1)+reverse(string.substring(1, string.length()-1))+string.charAt(0);
	}

	/**
	 * Return FALSE if password contains "string", or some delimeter separated part of it.
	 *
	 * @param string
	 * @param password
	 * @param delimeter the delimeter, default whitespace regex
	 * @return
	 */
	public boolean checkOnNamesAndLogins(String string, Password password, String delimeter) {

		if (delimeter == null) {
			delimeter = "\\s";
		}
		// do not check too small string parts !!
		if (string == null || string.length() < 3) return true;

		if (string.split(delimeter).length > 1) {

			// consist of more pieces - check them individually - each piece must be relevant
			String[] splitedString = string.split(delimeter);
			for (String s : splitedString) {

				// too small part, skip
				if (s == null || s.length() < 3) continue;

				// check part
				if (password.getValue().toLowerCase().contains(s.toLowerCase()) ||
					password.getValue().toLowerCase().contains(reverse(s.toLowerCase())) ||
					normalizeString(password.getValue()).contains(normalizeString(s)) ||
					normalizeString(password.getValue()).contains(normalizeString(reverse(s)))) {
					setResult(Result.INVALID_FORMAT);
					password.setRawStatus(getTransl().passwordStrengthForNameLogin(), ValidationState.ERROR);
					return false;
				}

			}

		}

		// check also whole string
		if (password.getValue().toLowerCase().contains(string.toLowerCase()) ||
			password.getValue().toLowerCase().contains(reverse(string.toLowerCase())) ||
			normalizeString(password.getValue()).contains(normalizeString(string)) ||
			normalizeString(password.getValue()).contains(normalizeString(reverse(string)))) {
			setResult(Result.INVALID_FORMAT);
			password.setRawStatus(getTransl().passwordStrengthForNameLogin(), ValidationState.ERROR);
			return false;
		}

		return true;

	}

	private String normalizeString(String string) {
		String result = normalizeStringToNFD(string);
		result = result.replaceAll("\\s","");
		return result;
	}

	private final native String normalizeStringToNFD(String input) /*-{
		if (typeof input.normalize !== "undefined") {
			// convert to normal decomposed form and replace all combining-diacritics marks
			return input.normalize('NFD').replace(/[\u0300-\u036f]/g, "").toLowerCase();
		}
		// just lowercase
		return input.toLowerCase();
	}-*/;

}
