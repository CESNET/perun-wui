package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.registrar.widgets.items.Username;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class UsernameValidator extends PerunFormItemValidatorImpl<Username> {

	private static final int PERUN_ATTRIBUTE_LOGIN_NAMESPACE_POSITION = 49;

	@Override
	public boolean validateLocal(Username username) {

		if (username.isRequired() && isNullOrEmpty(username.getValue())) {
			setResult(Result.EMPTY);
			username.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		if (!username.getTextBox().isValid()) {
			setResult(Result.INVALID_FORMAT);
			username.setStatus(getErrorMsgOrDefault(username), ValidationState.ERROR);
			return false;
		}

		if (username.getValue() != null && username.getValue().length() > username.MAX_LENGTH) {
			setResult(Result.TOO_LONG);
			username.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		username.setStatus(ValidationState.SUCCESS);
		return true;
	}

	@Override
	public void validate(final Username username, final Events<Boolean> events) {

		events.onLoadingStart();

		if (!validateLocal(username)) {
			events.onFinished(false);
			return;
		}
		if (username.getValue() == null) {
			events.onFinished(true);
			return;
		}

		String loginNamespace = username.getItemData().getFormItem().getPerunAttribute().substring(PERUN_ATTRIBUTE_LOGIN_NAMESPACE_POSITION);

		UsersManager.isLoginAvailable(loginNamespace, username.getValue(), new JsonEvents() {

			@Override
			public void onFinished(JavaScriptObject jso) {
				BasicOverlayObject obj = jso.cast();
				boolean available = obj.getBoolean();

				if (!available) {
					setResult(Result.LOGIN_NOT_AVAILABLE);
					username.setStatus(getTransl().loginNotAvailable(), ValidationState.ERROR);
					events.onFinished(false);
					return;
				}
				username.setStatus(ValidationState.SUCCESS);
				events.onFinished(true);
			}

			@Override
			public void onError (PerunException error){
				setResult(Result.CANT_CHECK_LOGIN);
				username.setStatus(getTransl().checkingLoginFailed(), ValidationState.ERROR);
				events.onFinished(false);
			}

			@Override
			public void onLoadingStart () {
				setResult(Result.CHECKING_LOGIN);
				username.unsetStatus();
			}
		});

	}

}