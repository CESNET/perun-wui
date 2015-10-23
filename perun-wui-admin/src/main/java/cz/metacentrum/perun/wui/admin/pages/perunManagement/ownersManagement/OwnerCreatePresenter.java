package cz.metacentrum.perun.wui.admin.pages.perunManagement.ownersManagement;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import cz.metacentrum.perun.wui.pages.FocusableView;


/**
 * OWNERS MANAGEMENT - CREATE OWNER PRESENTER
 *
 * @author Kristyna Kysela
 */

public class OwnerCreatePresenter extends PresenterWidget<OwnerCreatePresenter.MyView> implements OwnerCreateUiHandlers {

	public interface MyView extends View {
		void show();
	}

	@Inject
	public OwnerCreatePresenter(final EventBus eventBus, final MyView view) {
		super(true, eventBus, view);
	}

	@Override
	protected void onReset() {
		super.onReset();
		// focus on display
		if (getView() instanceof FocusableView) {
			((FocusableView)getView()).focus();
		}
	}

	@Override
	public void onClick() {

	}

}