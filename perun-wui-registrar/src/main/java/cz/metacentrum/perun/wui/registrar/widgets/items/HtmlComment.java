package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;

/**
 * Represents text comment in form.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class HtmlComment extends PerunFormItemStatic {

	private HTML widget;

	public HtmlComment(ApplicationFormItemData item, String lang) {
		super(item, lang);
	}

	@Override
	protected Widget initFormItem() {
		widget = new HTML(getLabelOrShortName());
		return widget;
	}

}
