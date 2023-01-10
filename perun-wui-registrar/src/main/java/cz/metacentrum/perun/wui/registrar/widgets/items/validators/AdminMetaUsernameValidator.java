package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.regexp.shared.RegExp;
import cz.metacentrum.perun.wui.registrar.widgets.items.Password;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;
import cz.metacentrum.perun.wui.registrar.widgets.items.Username;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.List;

/**
 * Specific validation for admin-meta namespace
 */
public class AdminMetaUsernameValidator extends UsernameValidator {

	@Override
	public boolean validateLocal(Username username) {

		if (username.isRequired() && isNullOrEmpty(username.getValue())) {
			setResult(Result.EMPTY);
			username.setRawStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		// must start with lower-case char
		RegExp regExp = RegExp.compile("^([a-z])(.*)$");
		if(regExp.exec(username.getValue()) == null){
			setResult(Result.INVALID_FORMAT);
			username.setRawStatus(getTransl().adminMetaLoginStart(), ValidationState.ERROR);
			return false;
		}

		// must contain only valid chars
		RegExp regExp2 = RegExp.compile("^[a-z0-9_-]+$");
		if(regExp2.exec(username.getValue()) == null){
			setResult(Result.INVALID_FORMAT);
			username.setRawStatus(getTransl().adminMetaLoginFormat(), ValidationState.ERROR);
			return false;
		}

		// must have valid length
		if (username.getValue().length() < 2 || username.getValue().length() > 15) {
			setResult(Result.INVALID_FORMAT);
			username.setRawStatus(getTransl().adminMetaLoginLength(), ValidationState.ERROR);
			return false;
		}

		// re-validate admin-meta password if login changes, but only if password was already entered
		List<PerunFormItem> allItems = username.getForm().getPerunFormItems();
		for (PerunFormItem item : allItems) {
			if (item instanceof Password) {
				if (((Password)item).getValidator() instanceof AdminMetaPasswordValidator) {
					if (!ValidationState.NONE.equals(item.getValidationState())) {
						item.validateLocal();
						break;
					}
				}
			}
		}

		username.setStatus(ValidationState.SUCCESS);
		return true;
	}

}
