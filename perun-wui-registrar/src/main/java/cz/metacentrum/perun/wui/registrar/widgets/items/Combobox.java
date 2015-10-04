package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.Select;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.ComboboxValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ondrej on 3.10.15.
 */
public class Combobox extends PerunFormItemEditable {

	public static final String CUSTOM_ID = "custom";
	public static final int MAX_LENGTH = 1024;

	private final ComboboxValidator validator;

	private FlowPanel widget;

	public Combobox(ApplicationFormItemData item, String lang) {
		super(item, lang);
		this.validator = new ComboboxValidator();
	}

	protected Widget initWidget() {

		RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

		widget = new FlowPanel();

		final Select select = new Select();
		select.setWidth("100%");
		select.setShowTick(true);

		select.clear();

		if (!isRequired()) {
			select.addItem(translation.notSelected(), "");
		}
		select.addItem(translation.customValue(), CUSTOM_ID);

		Map<String, String> opts = parseSelectionBox(getItemData().getFormItem().getItemTexts(getLang()).getOptions());

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
	public void focus() {
		if (isCustomSelected()) {
			getTextBox().setFocus(true);
		} else {
			getSelect().setFocus(true);
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
