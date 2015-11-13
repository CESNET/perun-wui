package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

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
			radiobox.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		radiobox.setStatus(ValidationState.SUCCESS);
		return true;
	}

}