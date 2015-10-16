package cz.metacentrum.perun.wui.registrar.widgets.items;

import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;

/**
 * Represents non editable (static) form items. E.g. Comment, Header, submit button...
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public abstract class PerunFormItemStatic extends PerunFormItem {

	public PerunFormItemStatic(ApplicationFormItemData item, String lang) {
		super(item, lang);
		add(initFormItem());
	}

	@Override
	public boolean validateLocal() {
		return true;
	}

	@Override
	public void validate(Events<Boolean> events) {
		events.onLoadingStart();
		events.onFinished(true);
	}

	@Override
	public PerunFormItemValidator.Result getLastValidationResult() {
		return PerunFormItemValidator.Result.OK;
	}

	@Override
	public boolean focus() {
		return false;
	}

	@Override
	public String getValue() {
		return "";
	}

	@Override
	public void setValue(String value) {
		// do nothing.
	}

}
