package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.UsernameValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Represents text field for user.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Username extends PerunFormItemEditable {

	public final static int MAX_LENGTH = 1024;
	private final UsernameValidator validator;

	private InputGroup widget;

	public Username(ApplicationFormItemData item, String lang, boolean enable) {
		super(item, lang);
		this.validator = new UsernameValidator();
		getTextBox().setEnabled(enable);
	}

	protected Widget initWidget() {

		InputGroupAddon addon = new InputGroupAddon();
		addon.setIcon(IconType.USER);
		ExtendedTextBox box = new ExtendedTextBox();
		box.setMaxLength(MAX_LENGTH);

		if (getItemData().getFormItem().getRegex() != null) {
			box.setRegex(getItemData().getFormItem().getRegex());
		}

		widget = new InputGroup();
		widget.add(addon);
		widget.add(box);

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
		getTextBox().setFocus(true);
		return true;
	}


	@Override
	public void setValidationTriggers() {

		final Events<Boolean> nothingEvents = new Events<Boolean>() {
			@Override
			public void onFinished(Boolean result) {

			}

			@Override
			public void onError(PerunException error) {

			}

			@Override
			public void onLoadingStart() {

			}
		};

		getTextBox().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				validate(nothingEvents);
			}
		});
	}

	@Override
	public String getValue() {
		return getTextBox().getValue();
	}

	@Override
	public InputGroup getWidget() {
		return widget;
	}

	@Override
	public void setValue(String value) {
		getTextBox().setValue(value);
	}

	public ExtendedTextBox getTextBox() {
		return ((ExtendedTextBox) getWidget().getWidget(1));
	}

}
