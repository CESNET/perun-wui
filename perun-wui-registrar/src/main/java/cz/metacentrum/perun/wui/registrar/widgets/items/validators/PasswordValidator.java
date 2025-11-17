package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import java.util.List;
import java.util.Objects;
import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.registrar.widgets.items.Password;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;
import cz.metacentrum.perun.wui.registrar.widgets.items.Username;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PasswordValidator extends PerunFormItemValidatorImpl<Password> {

	private static final int PERUN_ATTRIBUTE_LOGIN_NAMESPACE_POSITION = 49;

	@Override
	public boolean validateLocal(Password password) {

		if (isNullOrEmpty(password.getValue())) {
			if (password.isRequired()) {
				// password should be checked for emptiness only if required
				setResult(Result.EMPTY_PASSWORD);
				password.setRawStatus(getTransl().passEmpty(), ValidationState.ERROR);
				return false;
			}
		} else if (password.getValue().length() < Password.MIN_LENGTH) {
			// if user wants to set his password, it must conform minimum length requirements
			setResult(Result.PASSWORD_TOO_SHORT);
			password.setRawStatus(getTransl().passwordLength(Password.MIN_LENGTH), ValidationState.ERROR);
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

		if (!password.getPassword().isValid()) {
			setResult(Result.INVALID_FORMAT);
			password.setStatus(getErrorMsgOrDefault(password), ValidationState.ERROR);
			return false;
		}

		if (password.getValue().length() > Password.MAX_LENGTH) {
			setResult(Result.TOO_LONG);
			password.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

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

	@Override
	public void validate(Password item, Events<Boolean> events) {

		events.onLoadingStart();

		if (!validateLocal(item)) {
			events.onFinished(false);
			return;
		}

		String loginNamespace = item.getItemData().getFormItem().getPerunDestinationAttribute().substring(PERUN_ATTRIBUTE_LOGIN_NAMESPACE_POSITION);

		UsersManager.checkPasswordStrength(loginNamespace, item.getValue(), new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				setResult(Result.OK);
				item.setStatus(ValidationState.SUCCESS);
				events.onFinished(true);
			}

			@Override
			public void onError(PerunException error) {
				if ("PasswordStrengthException".equalsIgnoreCase(error.getName())) {
					setResult(Result.WEAK_PASSWORD);
					item.setRawStatus(getTransl().weakPassword(), ValidationState.ERROR);
				} else {
					setResult(Result.CANT_CHECK_WEAK_PASSWORD);
					item.setRawStatus(getTransl().cantCheckPasswordStrength(), ValidationState.ERROR);
				}
				events.onFinished(false);
			}

			@Override
			public void onLoadingStart() {
				setResult(Result.CHECKING_WEAK_PASSWORD);
				item.setStatus(ValidationState.NONE);
			}
		});

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

	public static String getLoginValue(Password password) {
		String[] parts = password.getItemData().getFormItem().getPerunDestinationAttribute().split(":");
		String namespace = parts.length > 0 ? parts[parts.length - 1] : "";
		List<PerunFormItem> items = password.getForm().getPerunFormItems();
		for (PerunFormItem item : items) {
			if (item instanceof Username) {
				if (item.getItemData().getFormItem() != null && Objects.equals(
					"urn:perun:user:attribute-def:def:login-namespace:" + namespace,
					item.getItemData().getFormItem().getPerunDestinationAttribute())) {
					return item.getValue();
				}
			}
		}
		return null;

	}

	public static String getName(Password password) {

		List<PerunFormItem> items = password.getForm().getPerunFormItems();
		for (PerunFormItem item : items) {
			if (item.getItemData().getFormItem() != null && Objects.equals("urn:perun:user:attribute-def:core:displayName", item.getItemData().getFormItem().getPerunDestinationAttribute())) {
				return item.getValue();
			}
		}
		return null;

	}

	public static String getFirstName(Password password) {

		List<PerunFormItem> items = password.getForm().getPerunFormItems();
		for (PerunFormItem item : items) {
			if (item.getItemData().getFormItem() != null && Objects.equals("urn:perun:user:attribute-def:core:firstName", item.getItemData().getFormItem().getPerunDestinationAttribute())) {
				return item.getValue();
			}
		}
		return null;

	}

	public static String getSurname(Password password) {

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
}
