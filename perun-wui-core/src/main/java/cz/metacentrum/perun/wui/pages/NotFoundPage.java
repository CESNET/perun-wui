package cz.metacentrum.perun.wui.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Warning page for 404 error.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class NotFoundPage extends Page {

	interface NotFoundPageUiBinder extends UiBinder<Widget, NotFoundPage> {
	}

	private static NotFoundPageUiBinder ourUiBinder = GWT.create(NotFoundPageUiBinder.class);

	private Widget rootElement;

	public NotFoundPage() {

		rootElement = ourUiBinder.createAndBindUi(this);

	}

	@Override
	public boolean isPrepared() {
		return true;
	}

	@Override
	public boolean isAuthorized() {
		return true;
	}

	@Override
	public void onResize() {

	}

	@Override
	public Widget draw() {

		return rootElement;

	}

	@Override
	public Widget getWidget() {
		return rootElement;
	}

	@Override
	public void open() {

	}

	@Override
	public String getUrl() {
		return "notfound";
	}

	@Override
	public void toggleHelp() {

	}

}