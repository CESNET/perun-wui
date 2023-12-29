package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Paragraph;


/**
 * WidgetBox represent shared logic for ListBox and MapBox.
 *
 * @author Jakub Hejda <Jakub.Hejda@cesnet.cz>
 */
public class WidgetBox extends PerunFormItemEditable {

	protected Widget widget;
	protected PerunButton addButton;


	public WidgetBox(PerunForm form, ApplicationFormItemData item, String lang) {
		super(form, item, lang);
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public void validate(Events<Boolean> events) {}

	@Override
	public boolean validateLocal() { return false; }

	@Override
	public PerunFormItemValidator.Result getLastValidationResult() {
		return null;
	}

	@Override
	public boolean focus() {
		return false;
	}

	@Override
	protected Widget initWidget() {
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		addButton = generateAddButton(vp);
		addButton.setIcon(IconType.PLUS);
		addButton.setType(ButtonType.SUCCESS);
		addButton.setMarginRight(40);
		addButton.setTooltipText(getTranslation().addNewValue());

		vp.add(addButton);
		generateItemWithRemoveButton(vp);

		widget = vp;
		return vp;
	}

	@Override
	protected Widget initWidgetOnlyPreview() {
		widget = new Paragraph();
		return widget;
	}

	@Override
	protected Widget getWidget() {
		return this.widget;
	}

	@Override
	protected void setValidationTriggers() {}

	@Override
	protected void setValueImpl(String value) {}

	public Paragraph getPreview() {
		if (widget instanceof Paragraph) {
			return (Paragraph) widget;
		}
		return null;
	}

	/**
	 * Generates new customized button with no default behavior
	 * @param vp VerticalPanel to which will be added new widgets by this button
	 * @return PerunButton customized addButton
	 */
	protected PerunButton generateAddButton(VerticalPanel vp) {
		return new PerunButton(getTranslation().addNewValue(), new ClickHandler() {
			public void onClick(ClickEvent event) {
				// behavior need to be implemented in the child class
			}
		});
	}

	/**
	 * Generates new input with remove button
	 * @param vp VerticalPanel to which will be added new item with remove button
	 */
	protected void generateItemWithRemoveButton(VerticalPanel vp) {
		// behavior need to be implemented in the child class
	}

	protected void setupRemoveButton (PerunButton removeButton) {
		removeButton.setIcon(IconType.MINUS);
		removeButton.setType(ButtonType.DANGER);
		removeButton.setTooltipText(getTranslation().removeValue());
	}

	protected void setupHPandAddToVP (HorizontalPanel hp, VerticalPanel vp, PerunButton removeButton) {
		hp.setCellWidth(removeButton, "40px");
		hp.setCellHorizontalAlignment(removeButton, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setHeight("40px");
		vp.insert(hp, vp.getWidgetCount() - 1);
	}
}
