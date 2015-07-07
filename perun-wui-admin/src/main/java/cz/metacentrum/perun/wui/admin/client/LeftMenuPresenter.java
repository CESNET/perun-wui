package cz.metacentrum.perun.wui.admin.client;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import cz.metacentrum.perun.wui.admin.pages.vosManagement.VoSelectPresenter;

/**
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class LeftMenuPresenter extends PresenterWidget<LeftMenuPresenter.MyView> implements LeftMenuUiHandlers {

	private VoSelectPresenter voSelectPresenter;

	public interface MyView extends View, HasUiHandlers<LeftMenuUiHandlers> {
	}

	@Inject
	LeftMenuPresenter(EventBus eventBus, MyView view, VoSelectPresenter voSelectPresenter) {
		super(eventBus, view);
		this.voSelectPresenter = voSelectPresenter;
		getView().setUiHandlers(this);
	}

	@Override
	public void selectVo() {
		voSelectPresenter.getView().show();
	}

}
