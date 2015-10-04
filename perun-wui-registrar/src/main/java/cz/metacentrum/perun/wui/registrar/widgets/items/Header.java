package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import org.gwtbootstrap3.client.ui.Legend;

/**
 * Created by ondrej on 3.10.15.
 */
public class Header extends PerunFormItemStatic {

	private Legend widget;

	public Header(ApplicationFormItemData item, String lang) {
		super(item, lang);
	}

	@Override
	protected Widget initWidget() {
		widget = new Legend();
		widget.setHTML(getLabelOrShortName());
		return widget;
	}

	@Override
	protected Legend getWidget() {
		return widget;
	}

}
