package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.Selectionbox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class SelectionboxValidator extends PerunFormItemValidatorImpl<Selectionbox> {

	@Override
	public boolean validateLocal(Selectionbox selectionbox) {

		if (selectionbox.isRequired() && isNullOrEmpty(selectionbox.getValue())) {
			setResult(Result.EMPTY_SELECT);
			selectionbox.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		selectionbox.setStatus(ValidationState.SUCCESS);
		return true;
	}

}