package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.EinfraPasswordValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.MuAdmPasswordValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PasswordValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.AdminMetaPasswordValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedPasswordTextBox;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.Objects;

/**
 * Represents TextField for password and his check (repeat it).
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Password extends PerunFormItemEditable {

	public final static int MIN_LENGTH = 8;
	public final static int MAX_LENGTH = 1024;
	private final PasswordValidator validator;

	private InputGroup widget;

	public Password(PerunForm form, ApplicationFormItemData item, String lang) {
		super(form, item, lang);
		// FIXME - specific per-namespace validation
		if (item.getFormItem() != null && Objects.equals("urn:perun:user:attribute-def:def:login-namespace:einfra", item.getFormItem().getPerunDestinationAttribute())) {
			this.validator = new EinfraPasswordValidator();
		} else if (item.getFormItem() != null && Objects.equals("urn:perun:user:attribute-def:def:login-namespace:mu-adm", item.getFormItem().getPerunDestinationAttribute())) {
			this.validator = new MuAdmPasswordValidator();
		} else if (item.getFormItem() != null && Objects.equals("urn:perun:user:attribute-def:def:login-namespace:admin-meta", item.getFormItem().getPerunDestinationAttribute())) {
			this.validator = new AdminMetaPasswordValidator();
		} else {
			this.validator = new PasswordValidator();
		}
	}

	protected Widget initWidget() {

		widget = new InputGroup();

		InputGroupAddon addon = new InputGroupAddon();
		addon.setIcon(IconType.KEY);

		ExtendedPasswordTextBox box = new ExtendedPasswordTextBox();
		box.setMaxLength(MAX_LENGTH);
		box.addStyleName("passwordFormItemFirst");
		box.setPlaceholder(getTranslation().enterPassword());

		ExtendedPasswordTextBox box2 = new ExtendedPasswordTextBox();
		box2.setMaxLength(MAX_LENGTH);
		box2.addStyleName("passwordFormItemLast");
		box2.setPlaceholder(getTranslation().confirmPassword());

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

		Events<Boolean> nothingEvent = new Events<Boolean>() {
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

		getPassword().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				validate(nothingEvent);
			}
		});

		getPasswordSecond().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				validate(nothingEvent);
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

	public PasswordValidator getValidator() {
		return this.validator;
	}

	@Override
	public boolean isOnlyPreview() {
		return super.isOnlyPreview() || PerunForm.FormState.EDIT.equals(getForm().getFormState());
	}

	@Override
	public boolean isUpdatable() {
		// password can't' be updated once submitted
		return false;
	}

}
