package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.RadioboxValidator;
import org.gwtbootstrap3.client.ui.Radio;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.html.Div;

import java.util.Map;

/**
 * Represents radio button group form item.
 * Value is a options key of checked radio button. Be careful it can be confusing. ;)
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Radiobox extends PerunFormItemEditable {

	public static final String NONE_ID = "none";
	public static final String KEY_ATTR = "data-key";

	private final RadioboxValidator validator;

	private FlowPanel widget;

	public Radiobox(ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(item, lang, onlyPreview);
		this.validator = new RadioboxValidator();
	}

	@Override
	protected Widget initWidget() {

		widget = new FlowPanel();

		FlowPanel wrapper = new FlowPanel();

		Map<String, String> opts = parseItemOptions();

		for (Map.Entry<String, String> entry : opts.entrySet()) {

			Radio radio = new Radio("radio-" + getItemData().getFormItem().getId(), entry.getValue());
			radio.getElement().setAttribute(KEY_ATTR, entry.getKey());
			widget.add(radio);

		}

		if (!isRequired()) {

			Radio radio = new Radio("radio-" + getItemData().getFormItem().getId(), getTranslation().clearRadiobox());
			radio.getElement().setAttribute(KEY_ATTR, NONE_ID);
			Div divider = new Div();
			divider.addStyleName("radioboxDivider");
			widget.add(divider);
			widget.add(radio);

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
		for (Widget widget : getWidget()) {
			if (widget instanceof Radio) {
				Radio radio = (Radio) widget;
				radio.setFocus(true);
				return true;
			}
		}
		return false;
	}

	@Override
	protected Widget initWidgetOnlyPreview() {

		initWidget();

		for (Widget widget : getWidget()) {
			if (widget instanceof Radio) {
				Radio radio = (Radio) widget;

				radio.setEnabled(false);
			}
		}

		return widget;
	}


	@Override
	public void setValidationTriggers() {

		for (Widget widget : getWidget()) {
			if (widget instanceof Radio) {
				Radio radio = (Radio) widget;
				radio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						validateLocal();
					}
				});
			}
		}

	}

	@Override
	public String getValue() {

		String value = "";
		for (Widget widget : getWidget()) {
			if (widget instanceof Radio) {
				Radio radio = (Radio) widget;
				if (radio.getValue()) {
					value = radio.getElement().getAttribute(KEY_ATTR);
					break;
				}
			}
		}

		if (value.equals(NONE_ID)) {
			return "";
		}
		return value;
	}

	@Override
	public FlowPanel getWidget() {
		return widget;
	}

	@Override
	public void setValue(String value) {

		for (Widget widget : getWidget()) {
			if (widget instanceof Radio) {
				Radio radio = (Radio) widget;
				if (radio.getElement().getAttribute(KEY_ATTR).equals(value)) {
					radio.setValue(true);
					break;
				}
			}
		}

	}
}