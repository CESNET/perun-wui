package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.Checkbox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * Created by ondrej on 6.10.15.
 */
public class CheckboxValidator extends PerunFormItemValidatorImpl<Checkbox> {

	@Override
	public boolean validateLocal(Checkbox checkbox) {

		if (checkbox.isRequired() && checkbox.getValue().isEmpty()) {
			setResult(Result.EMPTY);
			checkbox.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		checkbox.setSuccess();
		return true;
	}

}