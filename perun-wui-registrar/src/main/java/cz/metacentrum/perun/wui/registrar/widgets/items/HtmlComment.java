package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;

/**
 * Represents text comment in form.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class HtmlComment extends PerunFormItemStatic {

	private HTML widget;

	public HtmlComment(PerunForm form, ApplicationFormItemData item, String lang) {
		super(form ,item, lang);
	}

	@Override
	protected Widget initFormItem() {
		widget = new HTML(getLabelOrShortName());
		return widget;
	}

}
