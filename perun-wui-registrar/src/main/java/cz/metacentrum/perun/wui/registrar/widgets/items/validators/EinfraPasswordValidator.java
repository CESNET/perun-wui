package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.regexp.shared.RegExp;
import cz.metacentrum.perun.wui.registrar.widgets.items.Password;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;
import cz.metacentrum.perun.wui.registrar.widgets.items.Username;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.List;
import java.util.Objects;

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

		if (!checkOnNamesAndLogins(getLoginValue(password), password)) return false;
		if (!checkOnNamesAndLogins(getName(password), password)) return false;
		if (!checkOnNamesAndLogins(getFirstName(password), password)) return false;
		if (!checkOnNamesAndLogins(getSurname(password), password)) return false;

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

	private String getLoginValue(Password password) {

		List<PerunFormItem> items = password.getForm().getPerunFormItems();
		for (PerunFormItem item : items) {
			if (item instanceof Username) {
				if (item.getItemData().getFormItem() != null && Objects.equals("urn:perun:user:attribute-def:def:login-namespace:einfra", item.getItemData().getFormItem().getPerunDestinationAttribute())) {
					return item.getValue();
				}
			}
		}
		return null;

	}

	private String getName(Password password) {

		List<PerunFormItem> items = password.getForm().getPerunFormItems();
		for (PerunFormItem item : items) {
			if (item.getItemData().getFormItem() != null && Objects.equals("urn:perun:user:attribute-def:core:displayName", item.getItemData().getFormItem().getPerunDestinationAttribute())) {
				return item.getValue();
			}
		}
		return null;

	}

	private String getFirstName(Password password) {

		List<PerunFormItem> items = password.getForm().getPerunFormItems();
		for (PerunFormItem item : items) {
			if (item.getItemData().getFormItem() != null && Objects.equals("urn:perun:user:attribute-def:core:firstName", item.getItemData().getFormItem().getPerunDestinationAttribute())) {
				return item.getValue();
			}
		}
		return null;

	}

	private String getSurname(Password password) {

		List<PerunFormItem> items = password.getForm().getPerunFormItems();
		for (PerunFormItem item : items) {
			if (item.getItemData().getFormItem() != null && Objects.equals("urn:perun:user:attribute-def:core:lastName", item.getItemData().getFormItem().getPerunDestinationAttribute())) {
				return item.getValue();
			}
		}
		return null;

	}

	public static String reverse(String string) {
		if (string == null || string.isEmpty() || string.length() == 1) return string;
		return string.charAt(string.length()-1)+reverse(string.substring(1, string.length()-1))+string.charAt(0);
	}

	/**
	 * Return FALSE if password contains "login"
	 *
	 * @param string
	 * @param password
	 * @return
	 */
	public boolean checkOnNamesAndLogins(String string, Password password) {

		// do not check too small string parts !!
		if (string == null || string.length() < 3) return true;

		if (string.split("\\s").length > 1) {

			// consist of more pieces - check them individually - each piece must be relevant
			String[] splitedString = string.split("\\s");
			for (String s : splitedString) {

				// too small part, skip
				if (s == null || s.length() < 3) continue;

				// check part
				if (password.getValue().toLowerCase().contains(s.toLowerCase()) ||
						password.getValue().toLowerCase().contains(reverse(s.toLowerCase())) ||
						normalizeString(password.getValue()).contains(normalizeString(s)) ||
						normalizeString(password.getValue()).contains(normalizeString(reverse(s)))) {
					setResult(Result.INVALID_FORMAT);
					password.setRawStatus(getTransl().einfraPasswordStrengthForNameLogin(), ValidationState.ERROR);
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
			password.setRawStatus(getTransl().einfraPasswordStrengthForNameLogin(), ValidationState.ERROR);
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
