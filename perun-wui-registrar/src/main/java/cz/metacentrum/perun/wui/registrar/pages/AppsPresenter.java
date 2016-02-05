package cz.metacentrum.perun.wui.registrar.pages;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarPlaceTokens;

/**
 * Presenter for list of submitted applications
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AppsPresenter extends Presenter<AppsPresenter.MyView, AppsPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@NameToken(PerunRegistrarPlaceTokens.MY_APPS)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<AppsPresenter> {
	}

	@Inject
	public AppsPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
	}

/*	@Override
	protected void onReset() {
		super.onReset();
		// resize when any of presenters is attached/detached
		if (getView() instanceof ResizableView) {
			((ResizableView)getView()).onResize();
		}

	}*/
}