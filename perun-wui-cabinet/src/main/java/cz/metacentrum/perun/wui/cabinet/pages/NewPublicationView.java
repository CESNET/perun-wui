package cz.metacentrum.perun.wui.cabinet.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.cabinet.client.resources.PerunCabinetTranslation;


public class NewPublicationView extends ViewImpl implements NewPublicationPresenter.MyView {

	interface NewPublicationUiBinder extends UiBinder<Widget, NewPublicationView> {}

	private PerunCabinetTranslation translation = GWT.create(PerunCabinetTranslation.class);


	@Inject
	public NewPublicationView(NewPublicationUiBinder binder) {

		initWidget(binder.createAndBindUi(this));

		init();
	}


	private void init() {

	}
}