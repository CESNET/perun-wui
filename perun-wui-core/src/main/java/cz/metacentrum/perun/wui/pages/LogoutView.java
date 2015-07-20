package cz.metacentrum.perun.wui.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * Logout page
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class LogoutView extends ViewImpl implements LogoutPresenter.MyView {

	interface LogoutViewUiBinder extends UiBinder<Widget, LogoutView> {
	}

	private PerunTranslation translation = GWT.create(PerunTranslation.class);

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
		PerunSession.getPlaceManager().navigateBack();
	}

	@Inject
	public LogoutView(LogoutViewUiBinder uiBinder) {

		initWidget(uiBinder.createAndBindUi(this));
		text.setText(translation.logoutPageTitle());
		subText.setText(translation.logoutPageSubTitle());
		button.setText(translation.logoutPageButton());

	}

	@Override
	public void loading(boolean loading) {
		content.setVisible(!loading);
		loader.setVisible(loading);
	}

}