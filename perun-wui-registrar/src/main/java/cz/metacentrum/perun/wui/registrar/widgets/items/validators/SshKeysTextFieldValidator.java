package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import cz.metacentrum.perun.wui.registrar.widgets.items.TextField;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class SshKeysTextFieldValidator extends TextFieldValidator {

	RegExp regExp = RegExp.compile("^(" +
			"(ssh-(rsa|dss|ed25519)(-cert-v01@openssh.com)?)|" +
			"(sk-(ssh-ed25519|ecdsa-sha2-nistp256)(-cert-v01)?@openssh.com)|" +
			"(ecdsa-sha2-nistp(256|384|521)(-cert-v01@openssh.com)?))" +
			" (([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?)( [^,\n]+)?$");

	@Override
	public boolean validateLocal(TextField textField) {

		if (textField.isRequired() && isNullOrEmpty(textField.getValue())) {
			setResult(Result.EMPTY);
			textField.setRawStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		if (!textField.getBox().isValid()) {
			setResult(Result.INVALID_FORMAT);
			textField.setStatus(getErrorMsgOrDefault(textField), ValidationState.ERROR);
			return false;
		}

		if (textField.getValue() != null && textField.getValue().length() > textField.MAX_LENGTH) {
			setResult(Result.TOO_LONG);
			textField.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		if (textField.getValue() != null && !textField.getValue().isEmpty()) {

			String sshKeys = textField.getValue();

			if (sshKeys.contains(",,")) {
				setResult(Result.INVALID_FORMAT);
				textField.setStatus(getTransl().tooMuchCommas(), ValidationState.ERROR);
				return false;
			}

			if (sshKeys.contains(", ") || sshKeys.contains(" ,")) {
				setResult(Result.INVALID_FORMAT);
				textField.setStatus(getTransl().sshKeyNoSpaceAroundKeySeparator(), ValidationState.ERROR);
				return false;
			}

			// FIXME - this doesn't make sense anymore, as we have multiple different SSH keys prefixes, which needs to be checked.
			/*
			if (sshKeys.indexOf("ssh-") != sshKeys.lastIndexOf("ssh-")) {
				// there are at least two keys
				if (!sshKeys.contains(",ssh-")) {
					setResult(Result.INVALID_FORMAT);
					textField.setStatus(getTransl().sshKeyMissingCommaDelimiterTextField(), ValidationState.ERROR);
					return false;
				}
			}
			*/

			// normalize value just in case
			sshKeys = sshKeys.replaceAll("(,)+", ",");
			List<String> keys = Arrays.stream(sshKeys.split(",")).collect(Collectors.toList());

			for (String key : keys) {
				MatchResult matcher = regExp.exec(key);
				if (matcher == null) {
					int length = Math.min(key.length(), 30);
					textField.setRawStatus(getTransl().sshKeyFormat(key.substring(0, length)+((length == 30) ? "..." : "")), ValidationState.ERROR);
					setResult(Result.INVALID_FORMAT);
					return false;
				}
			}

		}

		textField.setStatus(ValidationState.SUCCESS);
		return true;

	}

}
