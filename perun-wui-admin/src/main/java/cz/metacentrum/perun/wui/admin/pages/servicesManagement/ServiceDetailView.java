package cz.metacentrum.perun.wui.admin.pages.servicesManagement;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.model.beans.Service;
import org.gwtbootstrap3.client.ui.Heading;

/**
 * PERUN/SERVICE ADMIN - SERVICE DETAIL
 *
 * @author Kristyna Kysela
 */
public class ServiceDetailView extends ViewImpl implements ServiceDetailPresenter.MyView {

	@UiField
	Heading pageTitle;

	interface ServiceDetailViewUiBinder extends UiBinder<Widget, ServiceDetailView> {
	}

	@Inject
	public ServiceDetailView(ServiceDetailViewUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setService(Service service) {
		pageTitle.setText("Service " + service.getName());
	}

}