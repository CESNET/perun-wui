package cz.metacentrum.perun.wui.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import cz.metacentrum.perun.wui.pages.FocusableView;

/**
 * Generic Presenter for main app window can be used by any app.
 *
 * It defines slot (SET_MAIN_CONTENT) used by other for generic pages
 * but you can use own View implementation in your app.
 *
 * It support automatic passing resize event to displayed view as well
 * as custom focusing on opening page.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public abstract class PerunPresenter<V extends View, P extends Proxy<? extends PerunPresenter>> extends Presenter<V, P> {

	public static final NestedSlot SLOT_MAIN_CONTENT = new NestedSlot();
	public static final NestedSlot SLOT_LEFT_MENU = new NestedSlot();
	public static final NestedSlot SLOT_HEADER = new NestedSlot();
	public static final NestedSlot SLOT_FOOTER = new NestedSlot();

	protected PerunPresenter(final EventBus eventBus, final V view, final P proxy) {
		super(eventBus, view, proxy, RevealType.Root);
	}

	@Override
	protected void onReset() {
		super.onReset();
		// focus when any of presenters is attached/detached
		if (getView() instanceof FocusableView) {
			((FocusableView)getView()).focus();
		}
	}

}