package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;

/**
 * Created by ondrej on 3.10.15.
 */
public class HtmlComment extends PerunFormItemStatic {

	private HTML widget;

	public HtmlComment(ApplicationFormItemData item, String lang) {
		super(item, lang);
	}

	@Override
	protected Widget initWidget() {
		widget = new HTML(getLabelOrShortName());
		return widget;
	}

	@Override
	protected HTML getWidget() {
		return widget;
	}

}
