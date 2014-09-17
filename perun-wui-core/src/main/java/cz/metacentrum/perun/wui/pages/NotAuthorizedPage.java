package cz.metacentrum.perun.wui.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Warning page for 403 error.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class NotAuthorizedPage extends Page {

	interface NotAuthorizedPageUiBinder extends UiBinder<Widget, NotAuthorizedPage> {
	}

	private static NotAuthorizedPageUiBinder ourUiBinder = GWT.create(NotAuthorizedPageUiBinder.class);

	private Widget rootElement;

	public NotAuthorizedPage() {

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
	public void open() {

	}

	@Override
	public String getUrl() {
		return "notauthorized";
	}

	@Override
	public void toggleHelp() {

	}


}