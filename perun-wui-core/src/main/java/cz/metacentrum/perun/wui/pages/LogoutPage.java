package cz.metacentrum.perun.wui.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Text;

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

	private PerunTranslation translation = GWT.create(PerunTranslation.class);

	@UiField
	Text title;

	@UiField
	Small title2;

	@UiField
	Button button;

	public LogoutPage() {

		rootElement = ourUiBinder.createAndBindUi(this);
		title.setText(translation.logoutPageTitle());
		title2.setText(translation.logoutPageSubTitle());
		button.setText(translation.logoutPageButton());

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