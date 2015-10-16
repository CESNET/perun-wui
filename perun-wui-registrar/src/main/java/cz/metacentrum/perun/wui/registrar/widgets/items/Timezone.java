package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.Select;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.TimezoneValidator;

/**
 * Represents SelectionBox for selecting timezones.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Timezone extends PerunFormItemEditable {

	private final TimezoneValidator validator;

	private Select widget;

	public Timezone(ApplicationFormItemData item, String lang) {
		super(item, lang);
		this.validator = new TimezoneValidator();
	}

	@Override
	protected Widget initWidget() {

		widget = new Select();
		widget.setWidth("100%");
		widget.setShowTick(true);

		if (!isRequired()) {
			widget.addItem(getTranslation().notSelected(), "");
		}

		for (String timezone : Utils.getTimezones()) {
			widget.addItem(timezone, timezone);
		}

		widget.setLiveSearch(true);

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
		getWidget().setFocus(true);
		return true;
	}

	@Override
	public void setEnable(boolean enable) {
		getWidget().setEnabled(enable);
	}


	@Override
	public void setValidationTriggers() {
		getWidget().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				validateLocal();
			}
		});
	}

	@Override
	public String getValue() {
		return getWidget().getValue();
	}

	@Override
	public Select getWidget() {
		return widget;
	}

	@Override
	public void setValue(String value) {
		for (int i = 0; i < getWidget().getItemCount(); i++) {
			if (getWidget().getValue(i).equals(value)) {
				getWidget().setSelectedIndex(i);
				break;
			}
		}
	}

}
