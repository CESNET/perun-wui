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

	public Username(ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(item, lang, onlyPreview);
		this.validator = new UsernameValidator();
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
		if (getTextBox().isEnabled()) {
			validator.validate(this, events);
		} else {
			events.onLoadingStart();
			events.onFinished(true);
		}
	}

	@Override
	public boolean validateLocal() {
		if (getTextBox().isEnabled()) {
			return validator.validateLocal(this);
		}
		return true;
	}

	@Override
	public PerunFormItemValidator.Result getLastValidationResult() {
		if (getTextBox().isEnabled()) {
			return validator.getLastResult();
		}
		return PerunFormItemValidator.Result.OK;
	}

	@Override
	public boolean focus() {
		if (getTextBox().isEnabled()) {
			getTextBox().setFocus(true);
			return true;
		}
		return false;
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
		for (Widget box : getWidget()) {
			if (box instanceof ExtendedTextBox) {
				return (ExtendedTextBox) box;
			}
		}
		return null;
	}

	@Override
	public void makeOnlyPreviewWidget() {

		getTextBox().setEnabled(false);

	}

	public void setEnable(boolean enable) {
		getTextBox().setEnabled(enable);
	}
}
