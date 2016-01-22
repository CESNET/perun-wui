package cz.metacentrum.perun.wui.consolidator.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.model.common.WayfGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelFooter;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.html.Paragraph;

/**
 * WAYF Group button widget. Displays one option for wayf (type of authentication).
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class WayfGroupButton extends Panel implements HasClickHandlers {

	private Panel widget;
	private Image image;

	@UiField PanelBody panelBody;
	@UiField PanelFooter panelFooter;
	@UiField Paragraph description;

	interface WayfGroupButtonUiBinder extends UiBinder<Panel, WayfGroupButton> {
	}

	private static WayfGroupButtonUiBinder ourUiBinder = GWT.create(WayfGroupButtonUiBinder.class);

	public WayfGroupButton(WayfGroup group) {

		widget = ourUiBinder.createAndBindUi(this);

		String locale = PerunConfiguration.getCurrentLocaleName();

		if (group.getIconUrl() != null) {
			image = new Image(group.getIconUrl());
			panelBody.add(image);
		} else {
			panelBody.add(new Heading(HeadingSize.H4, group.getName(locale)));
		}

		description.setText(group.getDescription(locale));

	}

	/**
	 * Add ClickHandler to the whole Panel widget
	 *
	 * @param handler handler to add
	 * @return handler registration
	 */
	public HandlerRegistration addClickHandler(ClickHandler handler) {

		return widget.addDomHandler(handler, ClickEvent.getType());

	}

	/**
	 * Return WayfGroupButton widget itself
	 *
	 * @return
	 */
	public Panel getWidget() {
		return widget;
	}

}
