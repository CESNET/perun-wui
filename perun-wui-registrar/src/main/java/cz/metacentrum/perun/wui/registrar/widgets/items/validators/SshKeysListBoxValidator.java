package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import cz.metacentrum.perun.wui.registrar.widgets.items.ListBox;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * Validator for ListBox
 *
 * @author Jakub Hejda <Jakub.Hejda@cesnet.cz>
 */
public class SshKeysListBoxValidator extends ListBoxValidator {

	RegExp regExp = RegExp.compile("^(" +
		"(ssh-(rsa|dss|ed25519)(-cert-v01@openssh.com)?)|" +
		"(sk-(ssh-ed25519|ecdsa-sha2-nistp256)(-cert-v01)?@openssh.com)|" +
		"(ecdsa-sha2-nistp(256|384|521)(-cert-v01@openssh.com)?))" +
		" (([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?)( [^,\n]+)?$");

	@Override
	public boolean validateLocal(ListBox listBox) {
		if (listBox.isRequired() && isNullOrEmpty(listBox.getValue())) {
			setResult(Result.EMPTY);
			listBox.setRawStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		if (listBox.getValue() != null && !listBox.getValue().isEmpty()) {

			String wrongValues = "";
			int index = 1;
			for (ExtendedTextBox extendedTextBox : listBox.getListValue()) {
				String sshKey = extendedTextBox.getValue();

				if (sshKey.contains(",")) {
					setResult(Result.INVALID_FORMAT);
					listBox.setStatus(getTransl().sshKeySeparatorNotAllowed(), ValidationState.ERROR);
					return false;
				}

				MatchResult matcher = regExp.exec(sshKey);
				if (matcher == null) {
					wrongValues += "<br>" + index + ". " + (sshKey.length() > 25 ? sshKey.substring(0, 23) + "..." : sshKey);
				}
				index++;
			}
			if (!wrongValues.isEmpty()) {
				setResult(Result.INVALID_FORMAT);
				listBox.setRawStatus(getTransl().incorrectFormatItemList() + " <b>" + wrongValues + "</b>", ValidationState.ERROR);
				return false;
			}
		}

		listBox.setStatus(ValidationState.SUCCESS);
		return true;
	}
}
