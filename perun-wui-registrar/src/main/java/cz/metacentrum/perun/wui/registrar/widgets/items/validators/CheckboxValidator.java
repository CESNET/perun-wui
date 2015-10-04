package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.Checkbox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class CheckboxValidator extends PerunFormItemValidatorImpl<Checkbox> {

	@Override
	public boolean validateLocal(Checkbox checkbox) {

		if (checkbox.isRequired() && checkbox.getValue().isEmpty()) {
			setResult(Result.EMPTY);
			checkbox.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		checkbox.setStatus(ValidationState.SUCCESS);
		return true;
	}

}