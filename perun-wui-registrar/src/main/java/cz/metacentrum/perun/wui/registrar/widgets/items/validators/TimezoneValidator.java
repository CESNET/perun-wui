package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.Timezone;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class TimezoneValidator extends PerunFormItemValidatorImpl<Timezone> {

	@Override
	public boolean validateLocal(Timezone timezone) {

		if (timezone.isRequired() && isNullOrEmpty(timezone.getValue())) {
			setResult(Result.EMPTY_SELECT);
			timezone.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		timezone.setStatus(ValidationState.SUCCESS);
		return true;
	}

}