package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;

/**
 * Created by ondrej on 5.10.15.
 */
public abstract class PerunFormItemStatic extends PerunFormItem {

	public PerunFormItemStatic(ApplicationFormItemData item, String lang) {
		super(item, lang);
	}

	@Override
	protected Widget initFormItem() {
		return initWidget();
	}

	protected abstract Widget initWidget();

	protected abstract Widget getWidget();

	@Override
	public void validate(Events<Boolean> events) {
		events.onLoadingStart();
		events.onFinished(true);
	}

	@Override
	public boolean validateLocal() {
		return true;
	}

	@Override
	public PerunFormItemValidator.Result getLastValidationResult() {
		return PerunFormItemValidator.Result.OK;
	}

	@Override
	public void focus() {
		// do nothing
	}

	@Override
	public String getValue() {
		return "";
	}
}
