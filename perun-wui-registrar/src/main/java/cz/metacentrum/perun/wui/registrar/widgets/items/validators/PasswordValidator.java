package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.registrar.widgets.items.Password;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PasswordValidator extends PerunFormItemValidatorImpl<Password> {

	private static final int PERUN_ATTRIBUTE_LOGIN_NAMESPACE_POSITION = 49;

	@Override
	public boolean validateLocal(Password password) {

		if (password.isRequired() && isNullOrEmpty(password.getValue())) {
			setResult(Result.EMPTY_PASSWORD);
			password.setRawStatus(getTransl().passEmpty(), ValidationState.ERROR);
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
}
