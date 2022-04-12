package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.VerticalPanel;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.MapBoxValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.ui.constants.Alignment;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents MapBox form item.
 *
 * @author Jakub Hejda <Jakub.Hejda@cesnet.cz>
 */
public class MapBox extends WidgetBox {

	private final MapBoxValidator validator;
	List<ExtendedTextBox> keys;
	List<ExtendedTextBox> values;

	public MapBox(PerunForm form, ApplicationFormItemData item, String lang) {
		super(form, item, lang);
		this.validator = new MapBoxValidator();
	}

	@Override
	public String getValue() {
		StringBuilder value = new StringBuilder();
		if(isOnlyPreview()) {
			for(int i = 0; i < getPreview().getWidgetCount(); i++) {
				HorizontalPanel hp = (HorizontalPanel) getPreview().getWidget(i);
				Paragraph p = (Paragraph) hp.getWidget(0);
				if (p.getText() != null && !p.getText().isEmpty()) {
					value.append(p.getText().replace(",", "\\,").replace(":", "\\:"))
						.append(":");
					p = (Paragraph) hp.getWidget(2);
					value.append(p.getText().replace(",", "\\,").replace(":", "\\:"))
						.append(",");
				}
			}
		} else {
			for (ExtendedTextBox key : keys) {
				ExtendedTextBox val = values.get(keys.indexOf(key));
				if (key.getValue() != null && !key.getValue().isEmpty()) {
					value.append(key.getValue().replace(",", "\\,").replace(":", "\\:"))
						.append(":")
						.append(val.getValue().replace(",", "\\,").replace(":", "\\:"))
						.append(",");
				}
			}
		}

		return value.toString();
	}

	public List<ExtendedTextBox> getKeys() {
		return keys;
	}

	public List<ExtendedTextBox> getValues() {
		return values;
	}

	public Map<ExtendedTextBox, ExtendedTextBox> getKeysAndValues() {
		Map<ExtendedTextBox, ExtendedTextBox> map = new HashMap<>();
		for (ExtendedTextBox key : keys) {
			map.put(key, values.get(keys.indexOf(key)));
		}
		return map;
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
	protected Widget initWidget() {
		keys = new ArrayList<>();
		values = new ArrayList<>();
		return super.initWidget();
	}

	@Override
	protected Widget initWidgetOnlyPreview() {
		widget = new Paragraph();

		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setWidth("100%");
		Paragraph key = new Paragraph();
		key.addStyleName("form-control");
		Paragraph equals = new Paragraph("=");
		Paragraph val = new Paragraph();
		val.addStyleName("form-control");
		addItemToPreview(hp, key, equals, val);

		return widget;
	}

	@Override
	protected void setValidationTriggers() {
		if (isOnlyPreview()) {
			return;
		}
		for (ExtendedTextBox input : keys) {
			input.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					validateLocal();
				}
			});
		}

		for (ExtendedTextBox input : values) {
			input.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					validateLocal();
				}
			});
		}
	}

	@Override
	protected void setValueImpl(String value) {
		int counter = 0;
		if (isOnlyPreview()) {
			// delete default empty widget if there is some value
			getPreview().getWidget(0).removeFromParent();
		}
		for (String keyAndValue : value.split("(?<!\\\\),+?")) {
			String[] keyVal = keyAndValue.split("(?<!\\\\):+?");
			if (isOnlyPreview()) {
				HorizontalPanel hp = new HorizontalPanel();
				hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
				hp.setWidth("100%");
				Paragraph key = new Paragraph();
				Paragraph equals = new Paragraph("=");
				Paragraph val = new Paragraph();
				key.setText(keyVal[0].trim().replace("\\,", ",").replace("\\:", ":"));
				key.addStyleName("form-control");
				key.setHeight("auto");
				val.setText(keyVal[1].trim().replace("\\,", ",").replace("\\:", ":"));
				val.addStyleName("form-control");
				val.setHeight("auto");

				addItemToPreview(hp, key, equals, val);
			} else {
				if (counter != 0) {
					generateItemWithRemoveButton((VerticalPanel) widget);
				}
				keys.get(counter).setValue(keyVal[0].trim().replace("\\,", ",").replace("\\:", ":"));
				values.get(counter).setValue(keyVal[1].trim().replace("\\,", ",").replace("\\:", ":"));
			}
			counter++;
		}
	}

	@Override
	protected PerunButton generateAddButton(VerticalPanel vp) {
		return new PerunButton("", new ClickHandler() {
			public void onClick(ClickEvent event) {
				generateItemWithRemoveButton(vp);
			}
		});
	}

	@Override
	protected void generateItemWithRemoveButton(VerticalPanel vp) {
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setWidth("100%");
		ExtendedTextBox key = new ExtendedTextBox();
		key.setPlaceholder(getTranslation().enterKey());
		Label equals = new Label(" = ");
		ExtendedTextBox value = new ExtendedTextBox();
		value.setPlaceholder(getTranslation().enterValue());

		if (getItemData().getFormItem().getRegex() != null) {
			key.setRegex(getItemData().getFormItem().getRegex());
			value.setRegex(getItemData().getFormItem().getRegex());
		}
		keys.add(key);
		values.add(value);
		PerunButton removeButton = new PerunButton("", new ClickHandler() {
			public void onClick(ClickEvent event) {
				keys.remove(key);
				values.remove(value);
				vp.remove(hp);
				validateLocal();
			}
		});
		setupRemoveButton(removeButton);
		hp.add(key);
		hp.add(equals);
		hp.add(value);
		hp.add(removeButton);
		hp.setCellWidth(equals, "15px");
		hp.setCellHorizontalAlignment(equals, HasHorizontalAlignment.ALIGN_CENTER);
		setValidationTriggers();
		setupHPandAddToVP(hp, vp, removeButton);
	}

	private void addItemToPreview(HorizontalPanel hp, Paragraph key, Paragraph equals, Paragraph val) {
		hp.add(key);
		hp.add(equals);
		hp.add(val);
		hp.setCellWidth(key, "50%");
		equals.setWidth("15px");
		equals.setAlignment(Alignment.CENTER);
		hp.setCellWidth(val, "50%");
		getPreview().add(hp);
	}

}
