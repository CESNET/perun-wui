package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;

/**
 * Created by ondrej on 3.10.15.
 */
public class FromFederation extends PerunFormItemEditable {

	private ExtendedTextBox widget;

	public FromFederation(ApplicationFormItemData item, String lang, boolean visible) {
		super(item, lang);
		setVisible(visible);
	}

	protected Widget initWidget() {

		widget = new ExtendedTextBox();
		widget.setValue(getItemData().getPrefilledValue());
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
	public void focus() {
		// do nothing
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

}
