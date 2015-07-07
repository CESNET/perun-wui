package cz.metacentrum.perun.wui.admin.pages.vosManagement;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.pages.FocusableView;
import cz.metacentrum.perun.wui.pages.ResizableView;

import java.util.ArrayList;

/**
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class VoSelectPresenter extends PresenterWidget<VoSelectPresenter.MyView> implements VoSelectUiHandler {

	public interface MyView extends View {
		void show();
		void setVos(ArrayList<Vo> vos);
	}

	@Inject
	public VoSelectPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onReset() {

		super.onReset();
		// resize when any of presenters is attached/detached
		if (getView() instanceof ResizableView) {
			((ResizableView)getView()).onResize();
		}
		// focus on display
		if (getView() instanceof FocusableView) {
			((FocusableView)getView()).focus();
		}
	}

	@Override
	public void onClick() {

	}

}
