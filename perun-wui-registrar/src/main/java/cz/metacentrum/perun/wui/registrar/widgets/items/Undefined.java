package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import org.gwtbootstrap3.client.ui.html.Paragraph;

/**
 * Created by ondrej on 3.10.15.
 */
public class Undefined extends PerunFormItemStatic {

	private static final String VALUE = "UNDEFINED";
	private Paragraph widget;

	public Undefined(ApplicationFormItemData item, String lang) {
		super(item, lang);
	}

	@Override
	protected Widget initWidget() {
		widget = new Paragraph(VALUE);
		return widget;
	}

	@Override
	protected Widget getWidget() {
		return widget;
	}

}
