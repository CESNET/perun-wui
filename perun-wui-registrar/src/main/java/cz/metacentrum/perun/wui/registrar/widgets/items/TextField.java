package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.TextFieldValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;

/**
 * Represents text field form item.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class TextField extends PerunFormItemEditable {

	public final static int MAX_LENGTH = 1024;
	private final TextFieldValidator validator;

	private ExtendedTextBox widget;

	public TextField(ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(item, lang, onlyPreview);
		this.validator = new TextFieldValidator();
	}

	protected Widget initWidget() {

		widget = new ExtendedTextBox();
		widget.setMaxLength(MAX_LENGTH);
		widget.addStyleName(ColumnSize.MD_6.getCssName());

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
	protected void makeOnlyPreviewWidget() {

		getWidget().setEnabled(false);

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
	public ExtendedTextBox getWidget() {
		return widget;
	}

	@Override
	public void setValue(String value) {
		widget.setValue(value);
	}

}
