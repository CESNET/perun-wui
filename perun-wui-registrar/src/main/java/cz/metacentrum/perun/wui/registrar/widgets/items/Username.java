package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.EinfraUsernameValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.UsernameValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.Objects;

/**
 * Represents text field for username/login.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Username extends PerunFormItemEditable {

	public final static int MAX_LENGTH = 1024;
	private final UsernameValidator validator;

	private InputGroup widget;

	public Username(PerunForm form, ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(form, item, lang, onlyPreview);
		if (item.getFormItem() != null && Objects.equals("urn:perun:user:attribute-def:def:login-namespace:einfra", item.getFormItem().getPerunDestinationAttribute())) {
			this.validator = new EinfraUsernameValidator();
		} else {
			this.validator = new UsernameValidator();
		}
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
	public Widget initWidgetOnlyPreview() {

		InputGroupAddon addon = new InputGroupAddon();
		addon.setIcon(IconType.USER);

		Paragraph box = new Paragraph();
		box.addStyleName("form-control");

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
		if (isOnlyPreview()) {
			return false;
		}
		if (getTextBox().isEnabled()) {
			getTextBox().setFocus(true);
			return true;
		}
		return false;
	}


	@Override
	public void setValidationTriggers() {
		if (isOnlyPreview()) {
			return;
		}

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
		if (isOnlyPreview()) {
			return getPreview().getText();
		}
		return getTextBox().getValue();
	}

	@Override
	public InputGroup getWidget() {
		return widget;
	}

	@Override
	protected void setValueImpl(String value) {
		if (isOnlyPreview()) {
			getPreview().setText(value);
			return;
		}
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
	public Paragraph getPreview() {
		for (Widget box : getWidget()) {
			if (box instanceof Paragraph) {
				return (Paragraph) box;
			}
		}
		return null;
	}

	public void setEnable(boolean enable) {
		getTextBox().setEnabled(enable);
	}
}
