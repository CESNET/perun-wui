package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.regexp.shared.RegExp;
import cz.metacentrum.perun.wui.registrar.widgets.items.Username;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * Specific validation for EINFRA namespace
 *
 * @author Pavel Zlamal <zlamal@cesnet.cz>
 */
public class EinfraUsernameValidator extends UsernameValidator {

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
			username.setRawStatus(getTransl().einfraLoginStart(), ValidationState.ERROR);
			return false;
		}

		// must contain only valid chars
		RegExp regExp2 = RegExp.compile("^[a-z0-9_-]+$");
		if(regExp2.exec(username.getValue()) == null){
			setResult(Result.INVALID_FORMAT);
			username.setRawStatus(getTransl().einfraLoginFormat(), ValidationState.ERROR);
			return false;
		}

		// must have valid length
		if (username.getValue().length() < 2 || username.getValue().length() > 15) {
			setResult(Result.INVALID_FORMAT);
			username.setRawStatus(getTransl().einfraLoginLength(), ValidationState.ERROR);
			return false;
		}

		username.setStatus(ValidationState.SUCCESS);
		return true;
	}

}
