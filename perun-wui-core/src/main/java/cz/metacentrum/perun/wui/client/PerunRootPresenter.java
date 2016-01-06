package cz.metacentrum.perun.wui.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;

/**
 * This presenter allows embedding of GWTP app to <div id="app-content"> in a specified HTML page.
 * Then in your GWTP module class call: bind(RootPresenter.class).to(PerunRootPresenter.class).asEagerSingleton();
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunRootPresenter extends RootPresenter {

	public static final class MyRootView extends RootView {
		@Override
		public void setInSlot(Object slot, IsWidget widget) {
			// set app to the specified DIV on a page
			RootPanel.get("app-content").clear();
			RootPanel.get("app-content").add(widget);
		}
	}

	@Inject
	PerunRootPresenter(EventBus eventBus, MyRootView myRootView) {
		super( eventBus, myRootView );
	}

}