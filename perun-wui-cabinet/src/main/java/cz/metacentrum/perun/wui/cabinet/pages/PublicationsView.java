package cz.metacentrum.perun.wui.cabinet.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Text;


public class HomeView extends ViewImpl implements HomePresenter.MyView {
	interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {
	}

	@Inject
	public HomeView(HomeViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
	}

	@UiField
	Div page;

	@UiField
	Text nameData;

	@Override
	public void init() {
		nameData.setText("Hello world");
		GWT.log("sdfsdf");
	}
}