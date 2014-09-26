package cz.metacentrum.perun.wui.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

/**
 * Logout page
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class LogoutPage extends Page {

	interface LogoutPagePageUiBinder extends UiBinder<Widget, LogoutPage> {
	}

	private static LogoutPagePageUiBinder ourUiBinder = GWT.create(LogoutPagePageUiBinder.class);

	private Widget rootElement;

	public LogoutPage() {

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
		return "logout";
	}

	@Override
	public void toggleHelp() {

	}


}