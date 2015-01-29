package cz.metacentrum.perun.wui.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Text;

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

	private PerunTranslation translation = GWT.create(PerunTranslation.class);

	@UiField
	Text title;
	@UiField
	Alert message;

	public NotAuthorizedPage() {

		rootElement = ourUiBinder.createAndBindUi(this);
		title.setText(translation.notFoundPageTitle());
		message.setText(translation.notFoundPageText());

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
		return "notauthorized";
	}

	@Override
	public void toggleHelp() {

	}


}