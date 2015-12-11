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
 * Presenter for PERUN ADMIN - EXT SOURCES MANAGEMENT
 *
 * @author Kristyna Kysela
 */
public class ExtSourcesManagementPresenter extends Presenter<ExtSourcesManagementPresenter.MyView, ExtSourcesManagementPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@NameToken({PlaceTokens.EXT_SOURCES, PlaceTokens.PERUN_EXT_SOURCES})
	@ProxyCodeSplit
	public interface MyProxy extends ProxyPlace<ExtSourcesManagementPresenter> {
	}

	@Inject
	public ExtSourcesManagementPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);
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