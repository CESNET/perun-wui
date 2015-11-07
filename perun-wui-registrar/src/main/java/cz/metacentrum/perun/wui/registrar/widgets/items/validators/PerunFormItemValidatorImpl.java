package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.core.client.GWT;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public abstract class PerunFormItemValidatorImpl<T extends PerunFormItem> implements PerunFormItemValidator<T> {

	private Result result = Result.NOT_CHECKED;

	private RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

	@Override
	public void validate(T item, Events<Boolean> events) {
		events.onLoadingStart();
		boolean valid = validateLocal(item);
		events.onFinished(valid);
	}

	@Override
	public Result getLastResult() {
		return result;
	}

	public RegistrarTranslation getTransl() {
		return translation;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getErrorMsgOrDefault(T item) {
		String errorText = item.getItemData().getFormItem().getItemTexts(item.getLang()).getErrorText();

		if (errorText == null || errorText.isEmpty()) {
			return translation.incorrectFormat();
		} else {
			return errorText;
		}
	}

	protected boolean isNullOrEmpty(String value) {
		return (value == null || value.isEmpty());
	}

}
