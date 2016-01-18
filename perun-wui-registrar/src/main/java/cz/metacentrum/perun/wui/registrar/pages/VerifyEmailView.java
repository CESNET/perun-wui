package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * View for displaying VO/Group registration detail.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class VerifyEmailView extends ViewImpl implements VerifyEmailPresenter.MyView {

	private String i;
	private String m;

	@UiField
	Alert result;

	@UiField
	PerunLoader loader;

	@UiField
	Text text;

	interface VerifyEmailViewUiBinder extends UiBinder<Widget, VerifyEmailView> {
	}

	private PerunRegistrarTranslation translation = GWT.create(PerunRegistrarTranslation.class);

	@Inject
	public VerifyEmailView(VerifyEmailViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
		text.setText(translation.emailVerification());

		i = Window.Location.getParameter("i");
		m = Window.Location.getParameter("m");

	}

	@Override
	public void setResult(boolean verified) {

		loader.setVisible(false);
		loader.onFinished();

		if (verified) {
			result.setType(AlertType.SUCCESS);
			result.setText(translation.emailWasVerified());
		} else {
			result.setType(AlertType.DANGER);
			result.setText(translation.emailWasNotVerified());
		}

	}

	@Override
	public void onLoadingStart() {

		loader.setVisible(true);
		loader.onLoading();

	}

	@Override
	public void onError(PerunException error, final JsonEvents retry) {
		loader.onError(error, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RegistrarManager.validateEmail(i, m, retry);
			}
		});
	}

}