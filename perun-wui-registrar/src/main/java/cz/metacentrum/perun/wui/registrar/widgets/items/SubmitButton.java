package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Placement;

/**
 * Created by ondrej on 7.10.15.
 */
public class SubmitButton extends PerunFormItemStatic {

	private PerunForm form;
	private Row widget;


	public SubmitButton(ApplicationFormItemData item, String lang, PerunForm form) {
		super(item, lang);
		this.form = form;
	}

	@Override
	protected Widget initWidget() {

		RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

		widget = new Row();

		final PerunButton button = new PerunButton();
		button.setIcon(IconType.CHEVRON_RIGHT);
		button.setIconFixedWidth(true);
		button.setTooltipText(translation.checkAndSubmit());
		button.getTooltip().setPlacement(Placement.TOP);
		button.setType(ButtonType.SUCCESS);
		button.setText(getLabelOrShortName());

		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (form != null) {
					form.submit(button);
				}
			}
		});

		widget.add(button);
		widget.addStyleName(PerunForm.LABEL_OFFSET.getCssName());
		widget.addStyleName(PerunForm.WIDGET_WITH_TEXT_SIZE.getCssName());
		return widget;
	}

	@Override
	protected Row getWidget() {
		return widget;
	}

	@Override
	public void focus() {
		super.focus();
		for (Widget button : getWidget()) {
			if (button instanceof PerunButton) {
				((PerunButton) button).setFocus(true);
			}
		}
	}
}
