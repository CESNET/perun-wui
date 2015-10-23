package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.TextAreaValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextArea;
import org.gwtbootstrap3.client.ui.html.Paragraph;

/**
 * Represents text area form item.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class TextArea extends PerunFormItemEditable {

	public final static int MAX_LENGTH = 3999;
	private final TextAreaValidator validator;

	private Widget widget;

	public TextArea(ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(item, lang, onlyPreview);
		this.validator = new TextAreaValidator();
	}

	protected Widget initWidget() {

		widget = new ExtendedTextArea();
		getBox().setMaxLength(MAX_LENGTH);
		getBox().setSize("100%", "100px");

		if (getItemData().getFormItem().getRegex() != null) {
			getBox().setRegex(getItemData().getFormItem().getRegex());
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
		if (isOnlyPreview()) {
			return false;
		}
		getBox().setFocus(true);
		return true;
	}

	@Override
	protected Widget initWidgetOnlyPreview() {
		widget = new Paragraph();
		getPreview().addStyleName("form-control");
		getPreview().setSize("auto", "102px");
		return widget;
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
	public Widget getWidget() {
		return widget;
	}

	@Override
	public void setValue(String value) {
		if (isOnlyPreview()) {
			getPreview().setText(value);
		} else {
			getBox().setValue(value);
		}
	}

	public ExtendedTextArea getBox() {
		if (widget instanceof ExtendedTextArea) {
			return (ExtendedTextArea) widget;
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
