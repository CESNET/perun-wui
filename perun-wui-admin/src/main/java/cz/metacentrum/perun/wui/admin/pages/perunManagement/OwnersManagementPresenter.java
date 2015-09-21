package cz.metacentrum.perun.wui.admin.pages.perunManagement;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import cz.metacentrum.perun.wui.admin.pages.perunManagement.ownersManagement.OwnerCreatePresenter;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.pages.FocusableView;

/**
 * Presenter for PERUN ADMIN - OWNERS MANAGEMENT
 *
 * @author Kristyna Kysela
 */
public class OwnersManagementPresenter extends Presenter<OwnersManagementPresenter.MyView, OwnersManagementPresenter.MyProxy> implements PerunManagementUiHandlers {

	private OwnerCreatePresenter ownerCreatePresenter;

	@Override
	public void createOwner() {
		ownerCreatePresenter.getView().show();
	}

	public interface MyView extends View, HasUiHandlers<PerunManagementUiHandlers> {
	}

	@NameToken({PlaceTokens.OWNERS, PlaceTokens.PERUN_OWNERS})
	@ProxyCodeSplit
	public interface MyProxy extends ProxyPlace<OwnersManagementPresenter> {
	}

	@Inject
	public OwnersManagementPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, OwnerCreatePresenter ownerCreatePresenter) {
		super(eventBus, view, proxy, PerunPresenter.SET_MAIN_CONTENT);
		this.ownerCreatePresenter = ownerCreatePresenter;
		getView().setUiHandlers(this);
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