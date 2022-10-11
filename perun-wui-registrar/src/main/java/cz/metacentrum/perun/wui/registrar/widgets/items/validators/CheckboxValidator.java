package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.Checkbox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.Map;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class CheckboxValidator extends PerunFormItemValidatorImpl<Checkbox> {

	@Override
	public boolean validateLocal(Checkbox checkbox) {

		if (checkbox.isRequired() && isNullOrEmpty(checkbox.getValue())) {

			Map<String, String> opts = checkbox.parseItemOptions();

			if (opts.size() > 1) {
				// multiple checkboxes - prefer generic message
				setResult(Result.EMPTY);
				checkbox.setRawStatus(getTransl().cantBeEmptyCheckBox(), ValidationState.ERROR);
			} else {
				// single checkbox - prefer own error message
				setResult(Result.EMPTY);
				checkbox.setRawStatus(getErrorMsgOrDefault(checkbox), ValidationState.ERROR);
			}
			return false;
		}

		checkbox.setStatus(ValidationState.SUCCESS);
		return true;
	}

}
