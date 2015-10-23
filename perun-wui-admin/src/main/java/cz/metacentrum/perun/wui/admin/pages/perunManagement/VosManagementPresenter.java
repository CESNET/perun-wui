package cz.metacentrum.perun.wui.admin.pages.perunManagement;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.pages.FocusableView;

/**
 * Presenter for PERUN ADMIN - VOS MANAGEMENT
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class VosManagementPresenter extends Presenter<VosManagementPresenter.MyView, VosManagementPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@NameToken({PlaceTokens.VOS, PlaceTokens.PERUN_VOS})
	@ProxyCodeSplit
	public interface MyProxy extends ProxyPlace<VosManagementPresenter> {
	}

	@Inject
	public VosManagementPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SET_MAIN_CONTENT);
	}

	@Override
	protected void onReset() {
		super.onReset();
		// focus on display
		if (getView() instanceof FocusableView) {
			((FocusableView)getView()).focus();
		}

	}

}
