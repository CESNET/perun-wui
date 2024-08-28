package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.registrar.widgets.items.ValidatedEmail;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class ValidatedEmailValidator extends PerunFormItemValidatorImpl<ValidatedEmail> {

	@Override
	public boolean validateLocal(ValidatedEmail mail) {

		if (mail.isRequired() && isNullOrEmpty(mail.getValue())) {
			setResult(Result.EMPTY);
			mail.setRawStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		if (!Utils.isValidEmail(mail.getValue()) && !isNullOrEmpty(mail.getValue())) {
			setResult(Result.INVALID_FORMAT_EMAIL);
			mail.setStatus(getTransl().incorrectEmail(), ValidationState.ERROR);
			return false;
		}

		if (!mail.getTextBox().isValid()) {
			setResult(Result.INVALID_FORMAT);
			mail.setStatus(getErrorMsgOrDefault(mail), ValidationState.ERROR);
			return false;
		}

		if (mail.getValue() != null && mail.getValue().length() > mail.MAX_LENGTH) {
			setResult(Result.TOO_LONG);
			mail.setStatus(getTransl().tooLong(), ValidationState.ERROR);
			return false;
		}

		// check which mails can be really trusted
		ArrayList<String> validMails = new ArrayList<>();
		List<String> prefilledMails = new ArrayList<>();

		if (mail.getItemData().getPrefilledValue() != null) {
			prefilledMails.addAll(Arrays.asList(mail.getItemData().getPrefilledValue().split(";")));
		}

		// we do this, since we can't tell, if mail was pre-filled from federation or perun
		// by removing all fed values we can be sure, that value from perun will remain as trusted
		// PS: if value from perun and fed matches, it is doubled in prefilledMails list and only single one is removed.
		List<String> mailsFromFed = new ArrayList<>();
		String mailFromFed = PerunSession.getInstance().getPerunPrincipal().getAdditionInformation("mail");
		if (mailFromFed != null) {
			mailsFromFed.addAll(Arrays.asList(mailFromFed.split(";")));
		}

		// if LOA is present in form item, check it
		if (mail.getItemData().getAssuranceLevel() != null) {

			if (Integer.parseInt(mail.getItemData().getAssuranceLevel()) < 1) {
				// ext source is not trusted - remove fed values
				if (mailFromFed != null) {
					for (String str : mailsFromFed) {
						prefilledMails.remove(str);
					}
					validMails.addAll(prefilledMails);
				}
			} else {
				// trust all input, LOA > 0
				validMails.addAll(prefilledMails);
			}
		} else {
			// trust all input, no loa known
			validMails.addAll(prefilledMails);
		}

		if (!validMails.contains(mail.getValue()) && !isNullOrEmpty(mail.getValue()) &&
		    !mail.getValue().equals(mail.getInvitationMail())) {

			setResult(Result.MUST_VALIDATE_EMAIL);
			mail.setStatus(getTransl().mustValidateEmail(), ValidationState.WARNING);
			return true;
		}

		mail.setStatus(ValidationState.SUCCESS);
		return true;
	}

}
