package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.CheckboxValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ondrej on 3.10.15.
 */
public class Checkbox extends PerunFormItemEditable {

	private final CheckboxValidator validator;

	private FlowPanel widget;

	public Checkbox(ApplicationFormItemData item, String lang) {
		super(item, lang);
		this.validator = new CheckboxValidator();
	}

	protected Widget initWidget() {

		RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

		widget = new FlowPanel();
		widget.addStyleName("checkboxFormItem");

		widget.clear();

		FlowPanel wrapper = new FlowPanel();
		wrapper.getElement().addClassName("checkboxFormItem");

		Map<String, String> opts = parseSelectionBox(getItemData().getFormItem().getItemTexts(getLang()).getOptions());

		for (Map.Entry<String, String> entry : opts.entrySet()) {

			CheckBox checkBox = new CheckBox(entry.getValue());
			checkBox.setName(entry.getKey());
			widget.add(checkBox);

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
	public void focus() {
		if (getWidget().getWidgetCount() > 0) {
			((CheckBox) getWidget().getWidget(0)).setFocus(true);
		}
	}


	@Override
	public void setValidationTriggers() {

		for (Widget widget : getWidget()) {

			CheckBox checkBox = (CheckBox) widget;
			checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					validateLocal();
				}
			});
		}

	}

	@Override
	public String getValue() {

		// rebuild value
		String value = "";
		for (Widget widget : getWidget()) {
			CheckBox checkBox = (CheckBox) widget;
			if (checkBox.getValue()) {
				// put in selected values
				value += checkBox.getName()+"|";
			}
		}
		if (value.length() > 1) {
			value = value.substring(0, value.length()-1);
		}

		return value;
	}

	@Override
	public FlowPanel getWidget() {
		return widget;
	}


	/**
	 * Parses the "options" into MAP
	 *
	 * Standard HTML selection box, options are in for each locale in ItemTexts.label separated by | with values separated by #.
	 * Thus a language selection box would have for English locale the label <code>cs#Czech|en#English</code>.
	 *
	 * @param options Source string
	 * @return Map with key/value pairs of options
	 */
	private Map<String, String> parseSelectionBox(String options){

		Map<String, String> map = new HashMap<String, String>();

		if(options == null || options.length() == 0){
			return map;
		}

		String[] keyValue = options.split("\\|");

		for(int i = 0; i < keyValue.length; i++){

			String kv = keyValue[i];

			String[] split = kv.split("#", 2);

			if(split.length != 2){
				continue;
			}

			String key = split[0];
			String value = split[1];
			map.put(key, value);
		}
		return map;
	}
}
