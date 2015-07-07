package cz.metacentrum.perun.wui.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * Warning page for 403 error.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class NotAuthorizedView extends ViewImpl implements NotAuthorizedPresenter.MyView {

	interface NotAuthorizedPageUiBinder extends UiBinder<Widget, NotAuthorizedView> {
	}

	private PerunTranslation translation = GWT.create(PerunTranslation.class);

	@UiField
	Text title;
	@UiField
	Alert message;

	@Inject
	NotAuthorizedView(final NotAuthorizedPageUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		title.setText(translation.notAuthorizedPageTitle());
		message.setText(translation.notAuthorizedPageText());
	}

}