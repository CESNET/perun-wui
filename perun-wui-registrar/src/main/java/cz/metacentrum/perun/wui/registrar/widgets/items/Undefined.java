package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import org.gwtbootstrap3.client.ui.constants.Emphasis;
import org.gwtbootstrap3.client.ui.html.Paragraph;

/**
 * Represents undefined form item. Means item which GUI do not support.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Undefined extends PerunFormItemStatic {

	private Paragraph widget;

	public Undefined(PerunForm form, ApplicationFormItemData item, String lang) {
		super(form, item, lang);
	}

	@Override
	protected Widget initFormItem() {

		widget = new Paragraph(getTranslation().undefinedFormItem());
		widget.setEmphasis(Emphasis.MUTED);
		return widget;

	}

}
