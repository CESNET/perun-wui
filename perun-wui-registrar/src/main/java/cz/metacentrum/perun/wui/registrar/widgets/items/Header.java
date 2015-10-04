package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import org.gwtbootstrap3.client.ui.Legend;

/**
 * Represents Title (header) in form.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Header extends PerunFormItemStatic {

	private Legend widget;

	public Header(ApplicationFormItemData item, String lang) {
		super(item, lang);
	}

	@Override
	protected Widget initFormItem() {
		widget = new Legend();
		widget.setHTML(getLabelOrShortName());
		return widget;
	}

}
