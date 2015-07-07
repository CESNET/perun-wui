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
 * Warning page for 404 error.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class NotFoundView extends ViewImpl implements NotFoundPresenter.MyView {

	interface NotFoundViewUiBinder extends UiBinder<Widget, NotFoundView> {
	}

	private PerunTranslation translation = GWT.create(PerunTranslation.class);

	@UiField
	Text title;
	@UiField
	Alert message;

	@Inject
	public NotFoundView(NotFoundViewUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		title.setText(translation.notFoundPageTitle());
		message.setText(translation.notFoundPageText());
	}

}