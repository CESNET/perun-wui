package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.VerticalPanel;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarResources;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.ListBoxValidator;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.SshKeysListBoxValidator;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents ListBox form item.
 *
 * @author Jakub Hejda <Jakub.Hejda@cesnet.cz>
 */
public class ListBox extends WidgetBox {

	private final ListBoxValidator validator;
	List<ExtendedTextBox> inputList;
	Map<ExtendedTextBox, BlurHandler> handlers;

	public ListBox(PerunForm form, ApplicationFormItemData item, String lang) {
		super(form, item, lang);
		if ("urn:perun:user:attribute-def:def:sshPublicKey".equals(item.getFormItem().getPerunDestinationAttribute())) {
			this.validator = new SshKeysListBoxValidator();
		} else {
			this.validator = new ListBoxValidator();
		}
	}

	@Override
	public String getValue() {
		StringBuilder value = new StringBuilder();
		if (isOnlyPreview()) {
			for(int i = 0; i < getPreview().getWidgetCount(); i++) {
				Paragraph p = (Paragraph) getPreview().getWidget(i);
				value.append((p.getText() == null || p.getText().isEmpty())
					? ""
					: p.getText().replace(",", "\\,") + ",");
			}
		} else {
			for (ExtendedTextBox input : inputList) {
				value.append((input.getValue() == null || input.getValue().isEmpty())
					? ""
					: input.getValue().replace(",", "\\,") + ",");
			}
		}

		return value.toString();
	}

	public List<ExtendedTextBox> getListValue() {
		return inputList;
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
		inputList = new ArrayList<>();
		handlers = new HashMap<>();
		return super.initWidget();
	}

	@Override
	protected Widget initWidgetOnlyPreview() {
		widget = new Paragraph();
		Paragraph p = new Paragraph();
		p.addStyleName("form-control");
		getPreview().add(p);
		return widget;
	}

	@Override
	protected void setValidationTriggers() {
		if (isOnlyPreview()) {
			return;
		}
		if ("urn:perun:user:attribute-def:def:sshPublicKey".equals(this.getItemData().getFormItem().getPerunDestinationAttribute())) {
			final Events<Boolean> nothingEvent = new Events<Boolean>() {
				@Override
				public void onFinished(Boolean result) {

				}

				@Override
				public void onError(PerunException error) {

				}

				@Override
				public void onLoadingStart() {

				}
			};

			for (ExtendedTextBox input : inputList) {

				if (!handlers.containsKey(input)) {
					BlurHandler handler = new BlurHandler() {
						@Override
						public void onBlur(BlurEvent event) {
							validate(nothingEvent);
						}
					};
					input.addBlurHandler(handler);
					handlers.put(input, handler);
				}

			}

		} else {
			for (ExtendedTextBox input : inputList) {
				input.addBlurHandler(new BlurHandler() {
					@Override
					public void onBlur(BlurEvent event) {
						validateLocal();
					}
				});
			}
		}
	}

	@Override
	protected void setValueImpl(String value) {
		int counter = 0;
		if (isOnlyPreview()) {
			// delete default empty widget if there is some value
			getPreview().getWidget(0).removeFromParent();
		}
		for (String val : value.split("(?<!\\\\),+?")) {
			if (isOnlyPreview()) {
				Paragraph p = new Paragraph();
				p.setText(val.trim().replace("\\,", ","));
				p.addStyleName("form-control");
				p.addStyleName(PerunRegistrarResources.INSTANCE.gss().overflow());
				p.setHeight("auto");
				getPreview().add(p);
			} else {
				if (counter != 0) {
					generateItemWithRemoveButton((VerticalPanel) widget);
				}
				inputList.get(counter).setValue(val.trim().replace("\\,", ","));
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
		ExtendedTextBox input = new ExtendedTextBox();

		if (getItemData().getFormItem().getRegex() != null) {
			input.setRegex(getItemData().getFormItem().getRegex());
		}

		inputList.add(input);
		PerunButton removeButton = new PerunButton("", new ClickHandler() {
			public void onClick(ClickEvent event) {
				inputList.remove(input);
				handlers.remove(input);
				vp.remove(hp);
				validate(new Events<Boolean>() {
					@Override
					public void onFinished(Boolean result) {
					}
					@Override
					public void onError(PerunException error) {
					}
					@Override
					public void onLoadingStart() {
					}
				});
			}
		});
		setupRemoveButton(removeButton);
		hp.add(input);
		hp.add(removeButton);
		setValidationTriggers();
		setupHPandAddToVP(hp, vp, removeButton);
	}

}
