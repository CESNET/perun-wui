package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.Select;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.SelectionboxValidator;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.Map;

/**
 * Represents SelectionBox item form.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Selectionbox extends PerunFormItemEditable {

	private final SelectionboxValidator validator;

	private Widget widget;

	public Selectionbox(PerunForm form, ApplicationFormItemData item, String lang) {
		super(form, item, lang);
		this.validator = new SelectionboxValidator();
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (getSelect() != null) {
			getSelect().setEnabled(enabled);
		}
	}

	@Override
	protected Widget initWidget() {

		widget = new Select();
		getSelect().setWidth("100%");
		getSelect().setShowTick(true);

		getSelect().clear();
		getSelect().addItem(translation.notSelected(), "");

		Map<String, String> opts = parseItemOptions();

		for (Map.Entry<String, String> entry : opts.entrySet()) {
			getSelect().addItem(entry.getValue(), entry.getKey());
		}

		// allow live-search if more than 10 entries
		if (getSelect().getItemCount() > 10) {
			getSelect().setLiveSearch(true);
			getSelect().setLiveSearchPlaceholder(translation.typeToSearch());
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
		getSelect().setFocus(true);
		return true;
	}

	@Override
	protected Widget initWidgetOnlyPreview() {

		widget = new Paragraph();
		Icon caret = new Icon(IconType.CARET_DOWN);
		caret.setPull(Pull.RIGHT);
		caret.setColor("#ccc");
		getPreview().add(caret);
		getPreview().addStyleName("form-control");
		return widget;
	}


	@Override
	public void setValidationTriggers() {
		if (isOnlyPreview()) {
			return;
		}
		getSelect().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				validateLocal();
			}
		});
	}

	@Override
	public String getValue() {
		if (isOnlyPreview()) {
			return getPreview().getText();
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
			Map<String, String> opts = parseItemOptions();

			for (Map.Entry<String, String> entry : opts.entrySet()) {
				// value from param is the key in this map (but we want to display the value from map)
				if (entry.getKey().equals(value)) {
					getPreview().add(new Span(entry.getValue()));
					return;
				}
			}
			// if this option was deleted by admin from this form item, then it won't be parsed, and we have to display raw value
			getPreview().add(new Span(value));
			return;
		}
		// Value is already in select
		for (int i = 0; i < getSelect().getItemCount(); i++) {
			if (getSelect().getValue(i).equals(value)) {
				getSelect().setSelectedIndex(i);
				return;
			}
		}
		// set the value which doesn't match any option (this option was probably deleted after the form had been saved)
		getSelect().addItem(value, value);
		getSelect().setSelectedIndex(getSelect().getItemCount() - 1);
	}

	public Select getSelect() {
		if (widget instanceof Select) {
			return (Select) widget;
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
