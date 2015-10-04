package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PasswordValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedPasswordTextBox;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Created by ondrej on 3.10.15.
 */
public class Password extends PerunFormItemEditable {

	public final static int MAX_LENGTH = 1024;
	private final PasswordValidator validator;

	private InputGroup widget;

	public Password(ApplicationFormItemData item, String lang) {
		super(item, lang);
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
	public void focus() {
		getPassword().setFocus(true);
	}


	@Override
	public void setValidationTriggers() {
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

	public ExtendedPasswordTextBox getPassword() {
		return ((ExtendedPasswordTextBox) widget.getWidget(1));
	}

	public ExtendedPasswordTextBox getPasswordSecond() {
		return ((ExtendedPasswordTextBox) widget.getWidget(2));
	}

}
