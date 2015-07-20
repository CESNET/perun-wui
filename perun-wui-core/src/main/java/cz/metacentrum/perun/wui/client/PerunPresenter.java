package cz.metacentrum.perun.wui.client;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import cz.metacentrum.perun.wui.pages.FocusableView;
import cz.metacentrum.perun.wui.pages.ResizableView;

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
public class PerunPresenter extends Presenter<PerunPresenter.MyView, PerunPresenter.MyProxy> {

	@ProxyStandard
	public interface MyProxy extends Proxy<PerunPresenter> {
	}

	public interface MyView extends View {
	}

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final GwtEvent.Type<RevealContentHandler<?>> SET_MAIN_CONTENT = new GwtEvent.Type<RevealContentHandler<?>>();

	@Inject
	PerunPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, RevealType.Root);

		// Resize view on every page refresh
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				if (getView() instanceof ResizableView) {
					((ResizableView)getView()).onResize();
				}
			}
		});

	}

	@Override
	protected void onReset() {
		super.onReset();
		// resize when any of presenters is attached/detached
		if (getView() instanceof ResizableView) {
			((ResizableView)getView()).onResize();
		}
		// focus when any of presenters is attached/detached
		if (getView() instanceof FocusableView) {
			((FocusableView)getView()).focus();
		}
	}

}