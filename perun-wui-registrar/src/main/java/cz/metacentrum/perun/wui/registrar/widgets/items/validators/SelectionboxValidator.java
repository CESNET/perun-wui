package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.Selectionbox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * Created by ondrej on 6.10.15.
 */
public class SelectionboxValidator extends PerunFormItemValidatorImpl<Selectionbox> {

	@Override
	public boolean validateLocal(Selectionbox selectionbox) {

		if (selectionbox.isRequired() && selectionbox.getValue().isEmpty()) {
			setResult(Result.EMPTY);
			selectionbox.setStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		selectionbox.setSuccess();
		return true;
	}

}