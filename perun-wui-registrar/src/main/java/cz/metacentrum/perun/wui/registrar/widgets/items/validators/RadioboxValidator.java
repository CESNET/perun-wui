package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import cz.metacentrum.perun.wui.registrar.widgets.items.Radiobox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class RadioboxValidator extends PerunFormItemValidatorImpl<Radiobox> {

	@Override
	public boolean validateLocal(Radiobox radiobox) {

		if (radiobox.isRequired() && isNullOrEmpty(radiobox.getValue())) {
			setResult(Result.EMPTY);
			radiobox.setRawStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		String regex = radiobox.getItemData().getFormItem().getRegex();

		if (regex != null && !regex.equals("")) {

			RegExp regExp = RegExp.compile(regex);
			MatchResult matcher = regExp.exec(radiobox.getValue());
			boolean matchFound = (matcher != null); // equivalent to regExp.test(inputStr);
			if(!matchFound){

				setResult(Result.INVALID_FORMAT);
				radiobox.setStatus(getErrorMsgOrDefault(radiobox), ValidationState.ERROR);
				return false;
			}
		}

		radiobox.setStatus(ValidationState.SUCCESS);
		return true;
	}

}
