package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UtilsManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.pages.Page;
import cz.metacentrum.perun.wui.registrar.client.PerunRegistrar;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * Logout page
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class LogoutPage extends Page {

	interface LogoutPageUiBinder extends UiBinder<Widget, LogoutPage> {
	}

	private static LogoutPageUiBinder ourUiBinder = GWT.create(LogoutPageUiBinder.class);

	private Widget rootElement;

	private RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

	public LogoutPage() {
		rootElement = ourUiBinder.createAndBindUi(this);
		text.setText(translation.logoutPageTitle());
		subText.setText(translation.logoutPageSubTitle());
		button.setText(translation.logoutPageButton());
	}

	@UiField
	Button button;

	@UiField
	Text text;

	@UiField
	Small subText;

	@UiField
	Div content;

	@UiField
	Div loader;

	@UiHandler("button")
	public void handleClick(ClickEvent event) {
		History.back();
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

		UtilsManager.logout(new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject jso) {
				content.setVisible(true);
				loader.setVisible(false);
				Utils.clearFederationCookies();
				PerunRegistrar.perunLoaded = false;
			}

			@Override
			public void onError(PerunException error) {
				// TODO error state
			}

			@Override
			public void onLoadingStart() {
				content.setVisible(false);
				loader.setVisible(true);
			}
		});

	}

	@Override
	public String getUrl() {
		return "logout";
	}

	@Override
	public void toggleHelp() {

	}

}