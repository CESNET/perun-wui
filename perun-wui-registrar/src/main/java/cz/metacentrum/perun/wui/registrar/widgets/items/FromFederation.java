package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Span;

/**
 * Represents TextField with value prefilled from federation. It can be visible or hidden and it Should be disable.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class FromFederation extends PerunFormItemEditable {

	private Widget widget;

	public FromFederation(ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(item, lang, onlyPreview);
	}

	@Override
	protected Widget initWidget() {

		widget = new ExtendedTextBox();
		getBox().setEnabled(false);
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
	protected Widget initWidgetOnlyPreview() {
		widget = new Paragraph();
		getPreview().addStyleName("form-control");
		setRawStatus(getTranslation().federation(), ValidationState.NONE);
		return widget;
	}


	@Override
	public void setValidationTriggers() {
		// no validation
	}

	@Override
	public String getValue() {
		if (isOnlyPreview()) {
			return getPreview().getText();
		}
		return getBox().getValue();
	}

	@Override
	public Widget getWidget() {
		return widget;
	}

	@Override
	protected void setValueImpl(String value) {

		// FIXME - We should implement value select for users

		// if mail or organization, use only first value of multivalue attribute
		if (getItemData() != null && getItemData().getFormItem() != null &&
				("mail".equals(getItemData().getFormItem().getFederationAttribute()) ||
						"o".equals(getItemData().getFormItem().getFederationAttribute()))) {

			if (isOnlyPreview()) {

				Span span = new Span();
				span.setText(value.split(";")[0]);
				getPreview().add(span);

			} else {
				getBox().setValue(value.split(";")[0]);
			}

		} else {

			if (isOnlyPreview()) {
				Span span = new Span();
				span.setText(value);
				getPreview().add(span);
			} else {
				getBox().setValue(value);
			}

		}

	}


	public ExtendedTextBox getBox() {
		if (widget instanceof ExtendedTextBox) {
			return (ExtendedTextBox) widget;
		}
		return null;
	}
	public Paragraph getPreview() {
		if (widget instanceof Paragraph) {
			return (Paragraph) widget;
		}
		return null;
	}

}
