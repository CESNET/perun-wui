package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarResources;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.extras.animate.client.ui.Animate;
import org.gwtbootstrap3.extras.animate.client.ui.constants.Animation;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents editable form items. E.g. TextField, Selectbox, ...
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public abstract class PerunFormItemEditable extends PerunFormItem {

	private FormLabel label;
	private HelpBlock status;
	private HelpBlock help;
	private final boolean onlyPreview;
	public PerunRegistrarTranslation translation = GWT.create(PerunRegistrarTranslation.class);

	public PerunFormItemEditable(ApplicationFormItemData item, String lang, boolean onlyPreview) {
		super(item, lang);
		this.onlyPreview = onlyPreview;
		add(initFormItem());
		if (!isOnlyPreview()) {
			setValidationTriggers();
		}
	}

	@Override
	protected Widget initFormItem() {

		Row item = new Row();
		this.label = new FormLabel();
		FlowPanel widgetWithTexts = new FlowPanel();
		Row widgetWithStatus = new Row();
		this.help = new HelpBlock();
		Column widget = new Column(PerunForm.WIDGET_SIZE);
		this.status = new HelpBlock();
		Column statusWrap = new Column(PerunForm.STATUS_SIZE);

		// We assure that * is at the same line
		if (getItemData().getFormItem().isRequired()) {
			label.setHTML(SafeHtmlUtils.htmlEscape(getLabelOrShortName())+"&#8203;<span style=\"color:#b94a48\">*</span>");
		} else {
			label.setText(getLabelOrShortName());
		}
		// original implementation
		//label.setHTML(SafeHtmlUtils.htmlEscape(getLabelOrShortName()));
		//label.setShowRequiredIndicator(getItemData().getFormItem().isRequired());

		label.addStyleName(PerunForm.LABEL_SIZE.getCssName());
		widgetWithTexts.addStyleName(PerunForm.WIDGET_WITH_TEXT_SIZE.getCssName());

		status.addStyleName(PerunRegistrarResources.INSTANCE.gss().status());


		widgetWithStatus.add(widget);
		widgetWithTexts.add(widgetWithStatus);

		statusWrap.add(status);
		widgetWithStatus.add(statusWrap);

		item.add(label);
		item.add(widgetWithTexts);


		if (isOnlyPreview()) {

			statusWrap.setVisible(false);

			Widget w = initWidgetOnlyPreview();
			w.addStyleName(PerunRegistrarResources.INSTANCE.gss().preview());
			widget.add(w);

			setValue(getItemData().getValue());

		} else {

			widget.add(initWidget());
			setValue(getItemData().getPrefilledValue());

			String helpText = getItemData().getFormItem().getItemTexts(getLang()).getHelp();
			if (helpText == null) {
				// fallback to english
				helpText = getItemData().getFormItem().getItemTexts("en").getHelp();
			}
			if (helpText != null) {
				help.setHTML(SafeHtmlUtils.fromString(helpText).asString());
			}
			help.setMarginTop(5);
			// enforce gray color on help text instead of taking validation color.
			help.addStyleName(PerunRegistrarResources.INSTANCE.gss().help());

			// add generic help for passwords, except for einfra
			if (this instanceof Password &&
					this.getItemData().getFormItem() != null &&
					!"urn:perun:user:attribute-def:def:login-namespace:einfra".equals(this.getItemData().getFormItem().getPerunDestinationAttribute())) {
				help.setHTML(help.getHTML() + "<p>" + translation.dontUseAccents());
			}

			// replace EINFRA help texts to support HTML formatting
			if (this.getItemData().getFormItem() != null &&
					"urn:perun:user:attribute-def:def:login-namespace:einfra".equals(this.getItemData().getFormItem().getPerunDestinationAttribute())) {
				if (this instanceof Password) {
					help.setHTML(translation.einfraPasswordHelp());
				} else if (this instanceof Username) {
					help.setHTML(translation.einfraLoginHelp());
				}
			}

			widgetWithTexts.add(help);

		}

		return item;
	}

	/**
	 * Generate form item widget. Widget means part of item which is editable.
	 * without label, helper and error texts.
	 *
	 * @return generated form item widget
	 */
	protected abstract Widget initWidget();

	/**
	 * Generate form item widget for preview.
	 */
	protected abstract Widget initWidgetOnlyPreview();

	/**
	 * @return the same widget as initWidget() or initWidgetOnlyPreview() method returns. But it does not generate it.
	 */
	protected abstract Widget getWidget();

	/**
	 * Set default validation triggers (handlers) on form item widget.
	 */
	protected abstract void setValidationTriggers();

	@Override
	public void setValue(String value) {
		if (value == null) {
			return;
		}
		if (value.equals("")) {
			return;
		}
		if (value.equals("null")) {
			return;
		}

		setValueImpl(value);
	}

	protected abstract void setValueImpl(String value);


	/**
	 * Item is considered required only when "required" by definition TODO and user can modify value.
	 *
	 * @return TRUE if required / FALSE = not required
	 */
	public boolean isRequired() {

		return getItemData().getFormItem().isRequired();

	}

	public boolean isOnlyPreview() {
		return onlyPreview;
	}


	/**
	 * Status of item means validationState and text.
	 * This method display to (inform) user visually status of validation.
	 *
	 * @param text
	 * @param state
	 */
	public void setStatus(String text, ValidationState state) {
		if (text != null) {
			setRawStatus(SafeHtmlUtils.fromString(text).asString(), state);
		} else {
			setStatus(state);
		}
	}

	public void setStatus(ValidationState state) {
		setRawStatus("", state);
	}

	/**
	 * Status of item means validationState and text.
	 * Only sanitized input is allowed
	 * This method display to (inform) user visually status of validation.
	 *
	 * @param text
	 * @param state
	 */
	public void setRawStatus(String text, ValidationState state) {
		if (text.isEmpty()) {
			this.status.getParent().setVisible(false);
		} else {
			this.status.getParent().setVisible(true);
		}
		if (state.equals(ValidationState.ERROR)
				|| state.equals(ValidationState.WARNING)) {
			Animate.animate(status, Animation.PULSE, 1, 400);
		}
		this.status.setHTML(text);
		this.setValidationState(state);
	}

	public void unsetStatus() {
		setStatus(ValidationState.NONE);
	}



	/**
	 * Parses the "options" into MAP
	 *
	 * Standard HTML options (e.g. for selection box) are in for each locale in ItemTexts.label separated by | with values separated by #.
	 * Example:
	 * Thus a language selection box would have for English locale the label <code>cs#Czech|en#English</code>.
	 *
	 * @return Map with key/value pairs of options
	 */
	public Map<String, String> parseItemOptions(){

		String options = getItemData().getFormItem().getItemTexts(getLang()).getOptions();
		if (options == null || options.isEmpty()) {
			options = getItemData().getFormItem().getItemTexts("en").getOptions();
		}

		Map<String, String> map = new HashMap<String, String>();

		if (options == null) {
			return map;
		}
		if (options.equals("")) {
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
