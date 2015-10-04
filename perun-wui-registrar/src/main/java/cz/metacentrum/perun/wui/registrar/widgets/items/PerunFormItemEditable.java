package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.extras.animate.client.ui.Animate;
import org.gwtbootstrap3.extras.animate.client.ui.constants.Animation;

/**
 * Created by ondrej on 5.10.15.
 */
public abstract class PerunFormItemEditable extends PerunFormItem {

	private FormLabel label;
	private HelpBlock status;
	private HelpBlock help;

	public PerunFormItemEditable(ApplicationFormItemData item, String lang) {
		super(item, lang);
		setValidationTriggers();
	}

	public void setStatus(String text, ValidationState state) {
		Animate.animate(status, Animation.SHAKE, 1, 700);
		this.status.setHTML(text);
		this.setValidationState(state);
	}
	public void unsetStatus() {
		this.status.setHTML("");
		this.setValidationState(ValidationState.NONE);
	}
	public void setSuccess() {
		this.status.setHTML("");
		this.setValidationState(ValidationState.SUCCESS);
	}

	/**
	 * Generate form item
	 */
	@Override
	protected Widget initFormItem() {

		Row item = new Row();

		this.label = new FormLabel();
		FlowPanel widgetWithTexts = new FlowPanel();

		Row widgetWithStatus = new Row();
		this.help = new HelpBlock();

		Column widget = new Column(PerunForm.WIDGET_SIZE);
		this.status = new HelpBlock();


		item.add(label);
		item.add(widgetWithTexts);

		widgetWithTexts.add(widgetWithStatus);
		widgetWithTexts.add(help);

		widgetWithStatus.add(widget);
		widgetWithStatus.add(status);

		widget.add(initWidget());
		

		label.setText(getLabelOrShortName());
		label.setShowRequiredIndicator(getItemData().getFormItem().isRequired());
		help.setText(getItemData().getFormItem().getItemTexts(getLang()).getHelp());

		status.addStyleName(PerunForm.STATUS_SIZE.getCssName());
		status.setMarginTop(0);
		help.setMarginTop(0);
		label.addStyleName(PerunForm.LABEL_SIZE.getCssName());
		widgetWithTexts.addStyleName(PerunForm.WIDGET_WITH_TEXT_SIZE.getCssName());

		return item;
	}

	protected abstract Widget initWidget();

	protected abstract Widget getWidget();

	/**
	 * Item is considered required only when "required" by definition and user can modify value.
	 *
	 * @return TRUE if required / FALSE = not required
	 */
	public boolean isRequired() {

		return getItemData().getFormItem().isRequired();

	}

	/**
	 * Set default validation triggers on form item widget.
	 */
	public abstract void setValidationTriggers();



}
