package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.Select;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.ComboboxValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Span;

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

	private Widget widget;

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
			select.addItem(getTranslation().notSelected(), (String) null);
		}
		select.addItem(getTranslation().customValue(), CUSTOM_ID);

		Map<String, String> opts = parseItemOptions();

		for (Map.Entry<String, String> entry : opts.entrySet()) {
			select.addItem(entry.getValue(), entry.getKey());
		}


		final ExtendedTextBox box = new ExtendedTextBox();


		getWidgetPanel().add(select);
		getWidgetPanel().add(box);

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
		if (isOnlyPreview()) {
			return false;
		}
		if (isCustomSelected()) {
			getTextBox().setFocus(true);
		} else {
			getSelect().setFocus(true);
		}
		return true;
	}

	@Override
	protected Widget initWidgetOnlyPreview() {

		widget = new Paragraph();
		Icon caret = new Icon(IconType.CARET_DOWN);
		caret.setPull(Pull.RIGHT);
		getPreview().add(caret);
		getPreview().addStyleName("form-control");
		return widget;
	}


	@Override
	public void setValidationTriggers() {
		if (getSelect() != null) {
			getSelect().addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					validateLocal();
				}
			});
		}

		if (getTextBox() != null) {
			getTextBox().addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					validateLocal();
				}
			});
		}

	}

	@Override
	public String getValue() {
		if (isOnlyPreview()) {
			return getPreview().getText();
		}
		if (isCustomSelected()) {
			return getTextBox().getValue();
		}
		return getSelect().getValue();
	}

	@Override
	public Widget getWidget() {
		return widget;
	}

	@Override
	protected void setValueImpl(String value) {

		if (isOnlyPreview()) {
			setValueOnlyPreview(value);
		} else {
			setValueReal(value);
		}


	}

	private void setValueOnlyPreview(String value) {

		if (value.isEmpty()) {
			return;
		}

		Map<String, String> opts = parseItemOptions();

		for (Map.Entry<String, String> entry : opts.entrySet()) {
			if (entry.getKey().equals(value)) {
				getPreview().add(new Span(entry.getValue() + " (" + value + ")"));
				return;
			}
		}

		getPreview().setText(value + " (custom value)");
	}
	private void setValueReal(String value) {

		// Value is already in select
		for (int i = 0; i < getSelect().getItemCount(); i++) {
			if (getSelect().getValue(i).equals(value)) {

				getSelect().setSelectedIndex(i);
				checkCustomSelected();
				return;
			}
		}

		// Value is different => put it as custom value;
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
		for (Widget select : getWidgetPanel()) {
			if (select instanceof Select) {
				return (Select) select;
			}
		}
		return null;
	}

	public ExtendedTextBox getTextBox() {
		for (Widget box : getWidgetPanel()) {
			if (box instanceof ExtendedTextBox) {
				return (ExtendedTextBox) box;
			}
		}
		return null;
	}

	public boolean isCustomSelected() {
		if (getSelect() == null) {
			return false;
		}
		return getSelect().getValue().equals(CUSTOM_ID);
	}

	private void checkCustomSelected() {
		if (isOnlyPreview()) {
			return;
		}
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


	public Panel getWidgetPanel() {
		if (widget instanceof Panel) {
			return (Panel) widget;
		}
		return null;
	}

	public Paragraph getPreview() {
		if (widget instanceof Paragraph) {
			return (Paragraph) widget;
		}
		return null;
	}

}
