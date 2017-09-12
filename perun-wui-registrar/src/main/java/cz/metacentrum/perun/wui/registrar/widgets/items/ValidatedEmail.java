package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.Select;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.ValidatedEmailValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Represents field for mails with prefilled options. Value have to be correct e-Mail format.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class ValidatedEmail extends PerunFormItemEditable {

	public final static int MAX_LENGTH = 1024;
	public final static String CUSTOM_ID = "custom";
	private final ValidatedEmailValidator validator;

	private InputGroup widget;
	private List<String> validMails;

	public ValidatedEmail(ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(item, lang, onlyPreview);
		this.validator = new ValidatedEmailValidator();
	}

	protected Widget initWidget() {

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
		getTextBox().setFocus(true);
		return true;
	}

	@Override
	public Widget initWidgetOnlyPreview() {

		InputGroupAddon addon = new InputGroupAddon();
		addon.setIcon(IconType.ENVELOPE);

		Paragraph box = new Paragraph();
		box.addStyleName("form-control");

		widget = new InputGroup();
		widget.add(addon);
		widget.add(box);

		return widget;
	}


	@Override
	public void setValidationTriggers() {
		if (isOnlyPreview()) {
			return;
		}
		getTextBox().addValueChangeHandler(new ValueChangeHandler() {
			private boolean first = true;
			@Override
			public void onValueChange(ValueChangeEvent event) {
				if (first && isCustomSelected() && getValue().isEmpty()) {
					first = false;
					return;
				}
				validateLocal();
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

		if (value.isEmpty()) {
			return;
		}

		if (isOnlyPreview()) {
			getPreview().setText(value.split(";")[0]);
			return;
		}

		validMails = Arrays.asList(value.split(";"));

		// remove duplicates
		validMails = new ArrayList<>(new LinkedHashSet<>(validMails));

		getTextBox().setValue(validMails.get(0));

		if (isOnlyPreview()) {
			// should contain only one value;
			return;
		}

		final Select emailSelect = new Select();
		emailSelect.addStyleName("emailFormItem");
		emailSelect.setWidth("38px");

		for (String val : validMails) {
			emailSelect.addItem(val, val);
		}
		emailSelect.addItem(getTranslation().customValueEmail(), CUSTOM_ID);

		emailSelect.addChangeHandler(new ChangeHandler() {

			private String customValueStore = "";
			private boolean customSelected = false;

			@Override
			public void onChange(ChangeEvent event) {

				if (emailSelect.getValue(emailSelect.getSelectedIndex()).equals(CUSTOM_ID)) {
					getTextBox().setValue(customValueStore);
					getTextBox().setFocus(true);
					customSelected = true;
				} else {
					if (customSelected) {
						customValueStore = getTextBox().getValue();
					}
					customSelected = false;
					getTextBox().setValue(emailSelect.getSelectedValue());
				}
				ValueChangeEvent.fire(getTextBox(), getTextBox().getValue());

			}
		});

		InputGroupButton dropdown = new InputGroupButton();
		dropdown.add(emailSelect);

		emailSelect.refresh();

		widget.add(dropdown);

	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if (!isOnlyPreview()) {
			// Selectpicker widget has to have proper form class too, make sure it's not null
			if(getSelect() != null &&
					getSelect().getElement().getPreviousSiblingElement() != null &&
					getSelect().getElement().getPreviousSiblingElement().getPreviousSiblingElement() != null) {
				getSelect().getElement().getPreviousSiblingElement().getPreviousSiblingElement().addClassName("form-control");
			}
		}
	}

	public ExtendedTextBox getTextBox() {
		for (Widget box : getWidget()) {
			if (box instanceof ExtendedTextBox) {
				return (ExtendedTextBox) box;
			}
		}
		return null;
	}

	public Select getSelect() {

		for (Widget box : getWidget()) {
			if (box instanceof InputGroupButton) {
				for (Widget select : (InputGroupButton) box) {
					if (select instanceof Select) {
						return (Select) select;
					}
				}
			}
		}
		return null;

	}

	public List<String> getValidMails() {
		if (validMails == null) {
			return new ArrayList<>();
		}
		return validMails;
	}

	public Paragraph getPreview() {
		for (Widget par : getWidget()) {
			if (par instanceof Paragraph) {
				return (Paragraph) par;
			}
		}
		return null;
	}

	private boolean isCustomSelected() {
		if (getSelect() != null) return getSelect().getSelectedValue().equals(CUSTOM_ID);
		return false;
	}

}
