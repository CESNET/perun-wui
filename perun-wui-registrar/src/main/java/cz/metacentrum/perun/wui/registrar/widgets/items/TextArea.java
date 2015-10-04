package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.TextAreaValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextArea;

/**
 * Represents text area form item.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class TextArea extends PerunFormItemEditable {

	public final static int MAX_LENGTH = 3999;
	private final TextAreaValidator validator;

	private ExtendedTextArea widget;

	public TextArea(ApplicationFormItemData item, String lang) {
		super(item, lang);
		this.validator = new TextAreaValidator();
	}

	protected Widget initWidget() {

		widget = new ExtendedTextArea();
		widget.setMaxLength(MAX_LENGTH);
		widget.setSize("100%", "100px");

		if (getItemData().getFormItem().getRegex() != null) {
			widget.setRegex(getItemData().getFormItem().getRegex());
		}

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
		getWidget().setFocus(true);
		return true;
	}


	@Override
	public void setValidationTriggers() {
		getWidget().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				validateLocal();
			}
		});
	}

	@Override
	public String getValue() {
		return getWidget().getValue();
	}

	@Override
	public ExtendedTextArea getWidget() {
		return widget;
	}

	@Override
	public void setValue(String value) {
		widget.setValue(value);
	}

}
