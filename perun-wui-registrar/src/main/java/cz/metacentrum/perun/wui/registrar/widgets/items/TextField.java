package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.TextFieldValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Span;

/**
 * Represents text field form item.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class TextField extends PerunFormItemEditable {

	public final static int MAX_LENGTH = 1024;
	private final TextFieldValidator validator;

	private Widget widget;

	public TextField(PerunForm form, ApplicationFormItemData item, String lang) {
		super(form, item, lang);
		this.validator = new TextFieldValidator();
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (getBox() != null) {
			getBox().setEnabled(enabled);
		}
	}

	@Override
	protected Widget initWidget() {

		widget = new ExtendedTextBox();
		getBox().setMaxLength(MAX_LENGTH);
		getBox().addStyleName(ColumnSize.MD_6.getCssName());

		if (getItemData().getFormItem().getRegex() != null) {
			getBox().setRegex(getItemData().getFormItem().getRegex());
		}

		return widget;
	}

	@Override
	protected Widget initWidgetOnlyPreview() {

		widget = new Paragraph();
		getPreview().addStyleName("form-control");
		if (getItemData() != null &&
				getItemData().getFormItem() != null &&
				getItemData().getFormItem().getFederationAttribute() != null) {
			setRawStatus(getTranslation().federation(), ValidationState.NONE);
		}
		return widget;
	}

	@Override
	protected Widget getWidget() {
		return widget;
	}

	@Override
	public void validate(Events<Boolean> events) {
		validator.validate(this, events);
	}

	@Override
	public boolean validateLocal() {
		return validator.validateLocal(this);
	}

	@Override
	public PerunFormItemValidator.Result getLastValidationResult() {
		return validator.getLastResult();
	}

	@Override
	public boolean focus() {
		if (getBox() == null) {
			return false;
		}
		getBox().setFocus(true);
		return true;
	}

	@Override
	public void setValidationTriggers() {
		if (isOnlyPreview()) {
			return;
		}
		getBox().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				validateLocal();
			}
		});
	}

	@Override
	public String getValue() {
		if (isOnlyPreview()) {
			return getPreview().getText();
		}
		return getBox().getValue();
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
				getPreview().setText(value);
				return;
			}
			getBox().setValue(value);
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

	@Override
	public boolean isUpdatable() {
		return getItemData().getFormItem() != null && getItemData().getFormItem().isUpdatable();
	}
}
