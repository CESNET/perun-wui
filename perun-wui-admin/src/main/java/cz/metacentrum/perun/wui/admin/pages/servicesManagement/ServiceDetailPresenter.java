package cz.metacentrum.perun.wui.admin.pages.servicesManagement;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.ServicesManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Service;


/**
 * Presenter for PERUN ADMIN - SERVICE DETAIL
 *
 * @author Kristyna Kysela
 */
public class ServiceDetailPresenter extends Presenter<ServiceDetailPresenter.MyView, ServiceDetailPresenter.MyProxy> {

	private PlaceManager placeManager = PerunSession.getPlaceManager();
	private ServiceDetailPresenter presenter = this;

	public interface MyView extends View {
		void setService(Service service);
	}

	@NameToken(PlaceTokens.SERVICE_DETAIL)
	@ProxyCodeSplit
	public interface MyProxy extends ProxyPlace<ServiceDetailPresenter> {
	}

	@Inject
	public ServiceDetailPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SET_MAIN_CONTENT);
	}

	@Override
	public void prepareFromRequest(final PlaceRequest request) {

		super.prepareFromRequest(request);
		try {
			final int id = Integer.valueOf(request.getParameter("id", null));
			if (id < 1) {
				placeManager.revealErrorPlace(request.getNameToken());
			}
			if (canReveal()) {
				ServicesManager.getServiceById(id, new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject jso) {
						Service service = jso.cast();
						getView().setService(service);
						getProxy().manualReveal(presenter);
					}

					@Override
					public void onError(PerunException error) {
						getProxy().manualRevealFailed();
						placeManager.revealErrorPlace(request.getNameToken());
					}

					@Override
					public void onLoadingStart() {
						// TODO - show loader ??
					}
				});
			} else {
				getProxy().manualRevealFailed();
				placeManager.revealUnauthorizedPlace(request.getNameToken());
			}
		} catch( NumberFormatException e ) {
			getProxy().manualRevealFailed();
			placeManager.revealErrorPlace(request.getNameToken());
		}

	}

	@Override
	public boolean useManualReveal() {
		return true;
	}

	/**
	 * Return TRUE if user can view the page for specified service
	 *
	 * @return TRUE = can see / FALSE = can't see
	 */
	private boolean canReveal() {
		return PerunSession.getInstance().isPerunAdmin();
	}

}