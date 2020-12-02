package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.Select;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.TimezoneValidator;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Span;

/**
 * Represents SelectionBox for selecting timezones.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Timezone extends PerunFormItemEditable {

	private final TimezoneValidator validator;

	private Widget widget;

	public Timezone(PerunForm form, ApplicationFormItemData item, String lang) {
		super(form, item, lang);
		this.validator = new TimezoneValidator();
	}

	@Override
	protected Widget initWidget() {

		widget = new Select();
		getSelect().setWidth("100%");
		getSelect().setShowTick(true);

		if (!isRequired()) {
			getSelect().addItem(getTranslation().notSelected(), (String) null);
		}

		for (String timezone : Utils.getTimezones()) {
			getSelect().addItem(timezone, timezone);
		}

		getSelect().setLiveSearch(true);

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
			getPreview().add(new Span(value));
			return;
		}

		for (int i = 0; i < getSelect().getItemCount(); i++) {
			if (getSelect().getValue(i).equals(value)) {
				getSelect().setSelectedIndex(i);
				break;
			}
		}
		getSelect().refresh();
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
