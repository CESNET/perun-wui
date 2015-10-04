package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.Select;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.ValidatedEmailValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ondrej on 3.10.15.
 */
public class ValidatedEmail extends PerunFormItemEditable {

	public final static int MAX_LENGTH = 1024;
	public final static String CUSTOM_ID = "custom";
	private final ValidatedEmailValidator validator;

	private InputGroup widget;
	private List<String> validMails;

	public ValidatedEmail(ApplicationFormItemData item, String lang) {
		super(item, lang);
		this.validator = new ValidatedEmailValidator();
	}

	protected Widget initWidget() {

		getItemData().setPrefilledValue("my.mail@muni.cz;mysecond@gmail.com");

		RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

		widget = new InputGroup();

		InputGroupAddon addon = new InputGroupAddon();
		addon.setIcon(IconType.ENVELOPE);

		final ExtendedTextBox box = new ExtendedTextBox();
		box.setMaxLength(MAX_LENGTH);

		widget.add(addon);
		widget.add(box);

		if (getItemData().getFormItem().getRegex() != null) {
			box.setRegex(getItemData().getFormItem().getRegex());
		}

		if ((getItemData().getPrefilledValue() == null)
		|| (getItemData().getPrefilledValue().isEmpty())) {
			return widget;
		}



		validMails = Arrays.asList(getItemData().getPrefilledValue().split(";"));

		box.setValue(validMails.get(0));

		final Select emailSelect = new Select();
		emailSelect.addStyleName("emailFormItem");
		emailSelect.setWidth("38px");

		for (String val : validMails) {
			emailSelect.addItem(val, val);
		}
		emailSelect.addItem(translation.customValueEmail(), CUSTOM_ID);

		emailSelect.addChangeHandler(new ChangeHandler() {

			private String customValueStore = "";
			private boolean customSelected = false;

			@Override
			public void onChange(ChangeEvent event) {

				if (emailSelect.getValue(emailSelect.getSelectedIndex()).equals(CUSTOM_ID)) {
					box.setValue(customValueStore);
					box.setFocus(true);
					customSelected = true;
				} else {
					if (customSelected) {
						customValueStore = box.getValue();
					}
					customSelected = false;
					box.setValue(emailSelect.getSelectedValue());
				}
				ValueChangeEvent.fire(box, box.getValue());

			}
		});

		InputGroupButton dropdown = new InputGroupButton();
		dropdown.add(emailSelect);

		widget.add(dropdown);

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
		getTextBox().setFocus(true);
	}


	@Override
	public void setValidationTriggers() {

		getTextBox().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				validateLocal();
			}
		});

		getTextBox().addValueChangeHandler(new ValueChangeHandler() {
			@Override
			public void onValueChange(ValueChangeEvent event) {
				validateLocal();
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

	public ExtendedTextBox getTextBox() {
		return ((ExtendedTextBox) getWidget().getWidget(1));
	}

	public List<String> getValidMails() {
		return validMails;
	}

}
