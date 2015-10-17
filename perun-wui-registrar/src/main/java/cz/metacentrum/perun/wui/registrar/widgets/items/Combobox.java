package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.Select;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.ComboboxValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import java.util.Map;

/**
 * Represents combobox form item. Resp. select with optional value.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Combobox extends PerunFormItemEditable {

	public static final String CUSTOM_ID = "custom";
	public static final int MAX_LENGTH = 1024;

	private final ComboboxValidator validator;

	private FlowPanel widget;

	public Combobox(ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(item, lang, onlyPreview);
		this.validator = new ComboboxValidator();
	}

	@Override
	protected Widget initWidget() {

		widget = new FlowPanel();

		final Select select = new Select();
		select.setWidth("100%");
		select.setShowTick(true);

		select.clear();

		if (!isRequired()) {
			select.addItem(getTranslation().notSelected(), "");
		}
		select.addItem(getTranslation().customValue(), CUSTOM_ID);

		Map<String, String> opts = parseItemOptions();

		for (Map.Entry<String, String> entry : opts.entrySet()) {
			select.addItem(entry.getValue(), entry.getKey());
		}


		final ExtendedTextBox box = new ExtendedTextBox();



		widget.add(select);
		widget.add(box);

		checkCustomSelected();

		select.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				checkCustomSelected();
			}
		});

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
		if (isCustomSelected()) {
			getTextBox().setFocus(true);
		} else {
			getSelect().setFocus(true);
		}
		return true;
	}

	@Override
	protected void makeOnlyPreviewWidget() {

		getSelect().setEnabled(false);
		getTextBox().setEnabled(false);

		if (isCustomSelected()) {

			getSelect().removeFromParent();
			getTextBox().removeStyleName("comboboxFormItemFirst");
			getTextBox().removeStyleName("comboboxFormItemLast");

		} else if (!getValue().isEmpty()) {

			int i = getSelect().getSelectedIndex();
			getSelect().setItemText(i, getSelect().getSelectedItemText() + " (" + getSelect().getSelectedValue() + ")");
			getSelect().refresh();

		}
	}


	@Override
	public void setValidationTriggers() {

		getSelect().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				validateLocal();
			}
		});

		getTextBox().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				validateLocal();
			}
		});

	}

	@Override
	public String getValue() {
		if (isCustomSelected()) {
			return getTextBox().getValue();
		}
		return getSelect().getValue();
	}

	@Override
	public FlowPanel getWidget() {
		return widget;
	}

	@Override
	public void setValue(String value) {

		for (int i = 0; i < getSelect().getItemCount(); i++) {
			if (getSelect().getValue(i).equals(value)) {

				getSelect().setSelectedIndex(i);
				checkCustomSelected();
				return;
			}
		}

		for (int i = 0; i < getSelect().getItemCount(); i++) {
			if (getSelect().getValue(i).equals(CUSTOM_ID)) {

				getSelect().setSelectedIndex(i);
				getTextBox().setValue(value);
				checkCustomSelected();
				return;
			}
		}

		getSelect().refresh();

	}

	public Select getSelect() {
		for (Widget select : getWidget()) {
			if (select instanceof Select) {
				return (Select) select;
			}
		}
		return null;
	}

	public ExtendedTextBox getTextBox() {
		for (Widget box : getWidget()) {
			if (box instanceof ExtendedTextBox) {
				return (ExtendedTextBox) box;
			}
		}
		return null;
	}

	public boolean isCustomSelected() {
		return getSelect().getValue().equals(CUSTOM_ID);
	}

	public void checkCustomSelected() {
		if (isCustomSelected()) {
			getTextBox().setVisible(true);
			getTextBox().setFocus(true);
			getTextBox().addStyleName("comboboxFormItemLast");
			getSelect().addStyleName("comboboxFormItemFirst");
			// FIXME - hack bug in BootstrapSelect
			getSelect().getElement().getNextSiblingElement().addClassName("comboboxFormItemFirst");
		} else {
			getTextBox().setVisible(false);
			getTextBox().removeStyleName("comboboxFormItemLast");
			getSelect().removeStyleName("comboboxFormItemFirst");
			// FIXME - hack bug in BootstrapSelect
			getSelect().getElement().getNextSiblingElement().removeClassName("comboboxFormItemFirst");
		}
	}

}
