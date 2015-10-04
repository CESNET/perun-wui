package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.widgets.Select;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.SelectionboxValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ondrej on 3.10.15.
 */
public class Selectionbox extends PerunFormItemEditable {

	private final SelectionboxValidator validator;

	private Select widget;

	public Selectionbox(ApplicationFormItemData item, String lang) {
		super(item, lang);
		this.validator = new SelectionboxValidator();
	}

	protected Widget initWidget() {

		RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

		widget = new Select();
		widget.setWidth("100%");
		widget.setShowTick(true);

		widget.clear();

		if (!isRequired()) {
			widget.addItem(translation.notSelected(), "");
		}

		Map<String, String> opts = parseSelectionBox(getItemData().getFormItem().getItemTexts(getLang()).getOptions());

		for (Map.Entry<String, String> entry : opts.entrySet()) {
			widget.addItem(entry.getValue(), entry.getKey());
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
		getWidget().setFocus(true);
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
