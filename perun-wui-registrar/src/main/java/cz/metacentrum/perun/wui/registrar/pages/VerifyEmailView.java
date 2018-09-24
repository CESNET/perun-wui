package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ColumnOffset;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * View for displaying VO/Group registration detail.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class VerifyEmailView extends ViewImpl implements VerifyEmailPresenter.MyView {

	private String i;
	private String m;
	private String target;

	@UiField
	Alert result;

	@UiField
	PerunLoader loader;

	@UiField
	Text text;

	@UiField
	PerunButton continueButton;

	@UiField Column content;

	interface VerifyEmailViewUiBinder extends UiBinder<Widget, VerifyEmailView> {
	}

	private PerunRegistrarTranslation translation = GWT.create(PerunRegistrarTranslation.class);

	@Inject
	public VerifyEmailView(VerifyEmailViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
		text.setText(translation.emailVerification());

		i = Window.Location.getParameter("i");
		m = Window.Location.getParameter("m");
		target = Window.Location.getParameter("target");

	}

	@Override
	public void setResult(boolean verified) {

		loader.setVisible(false);
		loader.onFinished();

		if (verified) {

			result.setType(AlertType.SUCCESS);
			result.setText(translation.emailWasVerified());

			if (target != null && !target.isEmpty()) {

				continueButton.setText(translation.continueButton());
				continueButton.setVisible(true);
				continueButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {

						Heading head = new Heading(HeadingSize.H4, translation.redirectingBackToService());
						Icon spin = new Icon(IconType.SPINNER);
						spin.setSpin(true);
						spin.setSize(IconSize.LARGE);
						spin.setPull(Pull.LEFT);
						spin.setMarginTop(10);

						Column column = new Column(ColumnSize.MD_8, ColumnSize.LG_6, ColumnSize.SM_10, ColumnSize.XS_12);
						column.setOffset(ColumnOffset.MD_2,ColumnOffset.LG_3,ColumnOffset.SM_1,ColumnOffset.XS_0);

						column.add(spin);
						column.add(head);
						column.setMarginTop(30);

						content.add(column);
						continueButton.setVisible(false);

						// WAIT 4 SEC BEFORE REDIRECT
						Timer timer = new Timer() {
							@Override
							public void run() {
								Window.Location.assign(target);
							}
						};
						timer.schedule(7000);
					}
				});

			}

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
