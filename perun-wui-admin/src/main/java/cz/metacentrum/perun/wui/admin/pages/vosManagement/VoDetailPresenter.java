package cz.metacentrum.perun.wui.admin.pages.vosManagement;

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
import cz.metacentrum.perun.wui.json.managers.VosManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Vo;

/**
 * Presenter for PERUN ADMIN - VOS MANAGEMENT - VO DETAIL
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class VoDetailPresenter extends Presenter<VoDetailPresenter.MyView, VoDetailPresenter.MyProxy> {

	private PlaceManager placeManager = PerunSession.getPlaceManager();
	private VoDetailPresenter presenter = this;

	public interface MyView extends View {
		void setVo(Vo vo);
	}

	@NameToken(PlaceTokens.VOS_DETAIL)
	@ProxyCodeSplit
	public interface MyProxy extends ProxyPlace<VoDetailPresenter> {
	}

	@Inject
	public VoDetailPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
	}

	@Override
	public void prepareFromRequest(final PlaceRequest request) {

		super.prepareFromRequest(request);
		try {
			final int id = Integer.valueOf(request.getParameter("id", null));
			if (id < 1) {
				placeManager.revealErrorPlace(request.getNameToken());
			}
			if (canReveal(id)) {
				VosManager.getVoById(id, new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject jso) {
						Vo vo = jso.cast();
						getView().setVo(vo);
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
	 * Return TRUE if user can view the page for specified VO
	 *
	 * @param voId ID of VO to check authorization
	 * @return TRUE = can see / FALSE = can't see
	 */
	private boolean canReveal(int voId) {
		return PerunSession.getInstance().isVoAdmin(voId) || PerunSession.getInstance().isVoObserver(voId);
	}

}
