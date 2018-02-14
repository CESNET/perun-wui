package cz.metacentrum.perun.wui.profile.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlexTable;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.Resource;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileResources;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.profile.pages.settings.datalimits.DataQuotasUiHandlers;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.base.TextBoxBase;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.HTMLPanel;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Span;
import org.gwtbootstrap3.client.ui.html.Strong;
import org.gwtbootstrap3.client.ui.html.Text;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.Map;

import static cz.metacentrum.perun.wui.profile.client.PerunProfileUtils.A_D_R_DATA_LIMIT_NAME;
import static cz.metacentrum.perun.wui.profile.client.PerunProfileUtils.A_D_R_DEFAULT_DATA_LIMIT_NAME;
import static cz.metacentrum.perun.wui.profile.client.PerunProfileUtils.A_D_R_DEFAULT_FILES_LIMIT_NAME;
import static cz.metacentrum.perun.wui.profile.client.PerunProfileUtils.A_D_R_FILES_LIMIT_NAME;
import static cz.metacentrum.perun.wui.profile.client.PerunProfileUtils.A_D_R_USER_SETTINGS_NAME_NAME;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class QuotaChangeModal extends Modal {

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	private Resource resource;
	private Map<String, Attribute> indexedAttrs;
	private QuotaType quotaType;
	private DataQuotasUiHandlers handlers;
	private TextBox valueTextBox;
	private TextArea reasonTextArea;
	private Select valueSelect;

	private boolean drawn = false;

	public QuotaChangeModal(Resource resource, Map<String, Attribute> indexedAttrs, QuotaType quotaType,
							DataQuotasUiHandlers handlers) {
		super();

		this.resource = resource;
		this.indexedAttrs = indexedAttrs;
		this.quotaType = quotaType;
		this.handlers = handlers;
	}

	public void draw() {
		if (drawn) {
			this.show();
			return;
		}

		switch (quotaType) {
			case DATA:
				this.setTitle(translation.dataQuotaRequestChange());
				break;
			case FILES:
				this.setTitle(translation.filesQuotaRequestChange());
		}
		this.setWidth("450px");

		Text invalidValueLabel = new Text();
		Span requestedQuotaSpan = new Span();

		valueTextBox = initRequestDataQuotaValueSpan(requestedQuotaSpan, invalidValueLabel);

		Div requestedQuotaDiv = new Div();
		requestedQuotaDiv.addStyleName(PerunProfileResources.INSTANCE.gss().requestQuotaChangeDiv());
		requestedQuotaDiv.add(requestedQuotaSpan);
		requestedQuotaDiv.add(invalidValueLabel);

		Text invalidReasonLabel = new Text();
		Div descriptionDiv = new Div();
		TextArea descriptionTextArea = initDescriptionDiv(descriptionDiv, invalidReasonLabel);

		Span buttonsSpan = initRequestQuotaButtonSpan(valueTextBox, descriptionTextArea, invalidValueLabel, invalidReasonLabel);

		ModalBody body = new ModalBody();

		FlexTable table = new FlexTable();
		table.addStyleName(PerunProfileResources.INSTANCE.gss().requestQuotaChangeTable());
		body.add(table);
		table.setWidget(0, 0, new Strong(translation.resourceHeading() + ":"));
		table.setWidget(0, 1, new Text(indexedAttrs.get(A_D_R_USER_SETTINGS_NAME_NAME).getValue()));

		table.setWidget(1, 0, new Strong(translation.currentQuota() + ":"));
		Text currentValueText = new Text();
		String currentValue;

		Attribute attribute;
		Attribute defAttribute;

		if (quotaType == QuotaType.FILES) {
			attribute = indexedAttrs.get(A_D_R_FILES_LIMIT_NAME);
			defAttribute = indexedAttrs.get(A_D_R_DEFAULT_FILES_LIMIT_NAME);
		} else {
			attribute = indexedAttrs.get(A_D_R_DATA_LIMIT_NAME);
			defAttribute = indexedAttrs.get(A_D_R_DEFAULT_DATA_LIMIT_NAME);
		}

		if (attribute == null || attribute.getValue() == null) {
			currentValue = translation.usingDefault();
		} else {
			currentValue = attribute.getValue();
		}

		String defaultValue;

		if (defAttribute == null || defAttribute.isEmpty()) {
			defaultValue = translation.notSet();
		} else {
			defaultValue = defAttribute.getValue();
		}

		currentValueText.setText(currentValue + " (" + translation.defaultValue() + ": " + defaultValue + ")");

		table.setWidget(1, 1, currentValueText);
		table.setWidget(2, 0, new Strong(translation.requestedQuota() + ":"));
		table.setWidget(2, 1, requestedQuotaDiv);
		table.setWidget(3, 0, new Strong(translation.reason() + ":"));
		table.setWidget(3, 1, descriptionDiv);
		table.setWidget(4, 1, buttonsSpan);

		this.add(body);
		this.show();
		drawn = true;
	}

	private Span initRequestQuotaButtonSpan(TextBox valueTextBox, TextArea reasonTextArea, Text invalidValueLabel,
											Text invalidReasonLabel) {
		Span buttonsSpan = new Span();
		buttonsSpan.addStyleName("pull-right");
		Button sendButton = new Button();
		sendButton.setText("Send request");
		sendButton.setType(ButtonType.SUCCESS);
		sendButton.setMarginRight(5);
		sendButton.setIcon(IconType.ENVELOPE);

		sendButton.addClickHandler(clickEvent -> {
			boolean isValueCorrect = checkTextBoxValue(valueTextBox, invalidValueLabel, true);
			boolean isReasonCorrect = checkTextBoxValue(reasonTextArea, invalidReasonLabel, false);

			if (!isValueCorrect || !isReasonCorrect) {
				return;
			}

			String newValue = valueTextBox.getValue();
			if (valueSelect != null) {
				newValue += valueSelect.getValue();
			}
			handlers.requestChange(newValue, reasonTextArea.getValue(), resource, quotaType);

			this.hide();
		});

		Button cancelButton = new Button();
		cancelButton.setText("Cancel");
		cancelButton.setType(ButtonType.DANGER);
		cancelButton.addClickHandler(c -> this.hide());

		buttonsSpan.add(sendButton);
		buttonsSpan.add(cancelButton);

		return buttonsSpan;
	}

	private TextArea initDescriptionDiv(Div div, Text invalidReasonLabel) {

		div.addStyleName(PerunProfileResources.INSTANCE.gss().requestQuotaChangeDiv());

		reasonTextArea = new TextArea();

		reasonTextArea.addValidationChangedHandler(validationChangedEvent -> checkTextBoxValue(reasonTextArea,
				invalidReasonLabel, false));
		reasonTextArea.addValueChangeHandler(valueChangeEvent -> checkTextBoxValue(reasonTextArea,
				invalidReasonLabel, false));

		reasonTextArea.setStyleName(PerunProfileResources.INSTANCE.gss().requestQuotaChangeValueTextArea());

		div.add(reasonTextArea);
		div.add(invalidReasonLabel);
		return reasonTextArea;
	}

	private void addValueSelect(HTMLPanel htmlPanel) {
		valueSelect = new Select();
		valueSelect.setWidth("25%");
		Option MOption = new Option();
		Option GOption = new Option();
		Option TOption = new Option();
		MOption.setValue("M");
		MOption.setText("M");
		GOption.setText("G");
		GOption.setValue("G");
		TOption.setValue("T");
		TOption.setText("T");
		valueSelect.add(MOption);
		valueSelect.add(GOption);
		valueSelect.add(TOption);
		valueSelect.addStyleName(PerunProfileResources.INSTANCE.gss().requestQuotaChangeValueSelect());
		htmlPanel.add(valueSelect);
	}

	private TextBox initRequestDataQuotaValueSpan(Span span, Text invalidValueLabel) {
		span.getElement().getStyle().setDisplay(Style.Display.INLINE_FLEX);

		TextBox textBox = createChangeTextBox(invalidValueLabel);

		span.add(textBox);

		if (quotaType == QuotaType.DATA) {
			addValueSelect(span);
		}

		return textBox;
	}

	private TextBox createChangeTextBox(Text invalidValueLabel) {
		TextBox textBox = new TextBox();
		textBox.addStyleName(PerunProfileResources.INSTANCE.gss().requestQuotaChangeValueTextBox());
		textBox.addValidationChangedHandler(validationChangedEvent -> checkTextBoxValue(textBox, invalidValueLabel, true));
		textBox.addValueChangeHandler(valueChangeEvent -> checkTextBoxValue(textBox, invalidValueLabel, true));
		return textBox;
	}

	private boolean checkTextBoxValue(TextBoxBase textBox, Text invalidValueLabel, boolean numberOnly) {
		String newValue = textBox.getValue().trim();
		if (newValue.isEmpty()) {
			textBox.addStyleName(PerunProfileResources.INSTANCE.gss().invalid());
			invalidValueLabel.setText(translation.cannotBeEmpty());
			return false;
		}
		if (numberOnly && !newValue.matches("[0-9]*")) {
			textBox.addStyleName(PerunProfileResources.INSTANCE.gss().invalid());
			invalidValueLabel.setText(translation.mustBeNumber());
			return false;
		}

		textBox.removeStyleName(PerunProfileResources.INSTANCE.gss().invalid());
		invalidValueLabel.setText("");
		return true;
	}
}
