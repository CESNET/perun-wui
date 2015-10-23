package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

/**
 * Represents TextField with value prefilled from federation. It can be visible or hidden and it Should be disable.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class FromFederation extends PerunFormItemEditable {

	private ExtendedTextBox widget;

	public FromFederation(ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(item, lang, onlyPreview);
	}

	@Override
	protected Widget initWidget() {

		widget = new ExtendedTextBox();
		widget.setEnabled(false);
		return widget;
	}

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
	public boolean focus() {
		return false;
	}

	@Override
	protected void makeOnlyPreviewWidget() {
		// shouldnt be editable never.

		setStatus(getTranslation().federation(), ValidationState.NONE);

	}


	@Override
	public void setValidationTriggers() {
		// no validation
	}

	@Override
	public String getValue() {
		return getWidget().getValue();
	}

	@Override
	public ExtendedTextBox getWidget() {
		return widget;
	}

	@Override
	public void setValue(String value) {
		widget.setValue(value.split(";")[0]);
	}

}
