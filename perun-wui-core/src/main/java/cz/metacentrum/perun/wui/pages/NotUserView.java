package cz.metacentrum.perun.wui.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.client.utils.Utils;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * "Not user of Perun" page
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class NotUserView extends ViewImpl implements NotUserPresenter.MyView {

	interface NotUserViewUiBinder extends UiBinder<Widget, NotUserView> {
	}

	private PerunTranslation translation = GWT.create(PerunTranslation.class);

	@UiField Text text;
	@UiField Alert message;
	@UiField Button button;

	@UiHandler("button")
	public void handleClick(ClickEvent event) {

		if (Window.Location.getParameterMap().containsKey("target_url")) {
			Window.Location.replace(Window.Location.getParameter("target_url"));
		}

	}

	@Inject
	public NotUserView(NotUserViewUiBinder uiBinder) {

		initWidget(uiBinder.createAndBindUi(this));

		text.setText(translation.notUserPageTitle());

		if (PerunSession.getInstance().getUser() == null && !PerunSession.getInstance().getRpcServer().equals("non")) {

			String actor = Utils.unescapeDN(PerunSession.getInstance().getPerunPrincipal().getActor());
			String extSource = Utils.translateIdp(Utils.unescapeDN(PerunSession.getInstance().getPerunPrincipal().getExtSource()));
			message.setText(translation.notUserText(actor, extSource));

		} else if (PerunSession.getInstance().getRpcServer().equals("non")){
			message.setText(translation.notUserTextNon());
		}

		button.setText(translation.continue_());
		if (Window.Location.getParameterMap().containsKey("target_url")) {
			button.setVisible(true);
		}

	}

}