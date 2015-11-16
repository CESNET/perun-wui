package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PasswordValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedPasswordTextBox;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Represents TextField for password and his check (repeat it).
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Password extends PerunFormItemEditable {

	public final static int MAX_LENGTH = 1024;
	private final PasswordValidator validator;

	private InputGroup widget;

	public Password(ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(item, lang, onlyPreview);
		this.validator = new PasswordValidator();
	}

	protected Widget initWidget() {

		widget = new InputGroup();

		InputGroupAddon addon = new InputGroupAddon();
		addon.setIcon(IconType.KEY);

		ExtendedPasswordTextBox box = new ExtendedPasswordTextBox();
		box.setMaxLength(MAX_LENGTH);
		box.addStyleName("passwordFormItemFirst");

		ExtendedPasswordTextBox box2 = new ExtendedPasswordTextBox();
		box2.setMaxLength(MAX_LENGTH);
		box2.addStyleName("passwordFormItemLast");

		if (getItemData().getFormItem().getRegex() != null) {
			box.setRegex(getItemData().getFormItem().getRegex());
		}

		widget.add(addon);
		widget.add(box);
		widget.add(box2);

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
		getPassword().setFocus(true);
		return true;
	}

	@Override
	protected Widget initWidgetOnlyPreview() {
		initWidget();
		getPassword().setEnabled(false);
		getPasswordSecond().setEnabled(false);
		return widget;
	}


	@Override
	public void setValidationTriggers() {
		if (isOnlyPreview()) {
			return;
		}
		getPassword().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				validateLocal();
			}
		});

		getPasswordSecond().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				validateLocal();
			}
		});
	}

	@Override
	public String getValue() {
		return getPassword().getValue();
	}

	@Override
	public InputGroup getWidget() {
		return widget;
	}

	@Override
	protected void setValueImpl(String value) {
		getPassword().setValue(value);
		getPasswordSecond().setValue(value);
	}

	public ExtendedPasswordTextBox getPassword() {
		for (Widget box : getWidget()) {
			if (box instanceof ExtendedPasswordTextBox) {
				return (ExtendedPasswordTextBox) box;
			}
		}
		return null;
	}

	public ExtendedPasswordTextBox getPasswordSecond() {
		boolean second = false;
		for (Widget box : getWidget()) {
			if (box instanceof ExtendedPasswordTextBox) {
				if (!second) {
					second = true;
				} else {
					return (ExtendedPasswordTextBox) box;
				}
			}
		}
		return null;
	}

}
