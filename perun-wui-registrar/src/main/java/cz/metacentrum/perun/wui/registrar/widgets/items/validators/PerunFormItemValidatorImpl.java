package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.core.client.GWT;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;

/**
 * Created by ondrej on 9.10.15.
 */
public abstract class PerunFormItemValidatorImpl<PerunFormItem> implements PerunFormItemValidator<PerunFormItem> {

	private Result result = Result.NOT_CHECKED;

	private RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

	@Override
	public void validate(PerunFormItem item, Events<Boolean> events) {
		events.onLoadingStart();
		boolean valid = validateLocal(item);
		events.onFinished(valid);
	}

	@Override
	public Result getLastResult() {
		return result;
	}

	protected RegistrarTranslation getTransl() {
		return translation;
	}

	protected void setResult(Result result) {
		this.result = result;
	}
}
