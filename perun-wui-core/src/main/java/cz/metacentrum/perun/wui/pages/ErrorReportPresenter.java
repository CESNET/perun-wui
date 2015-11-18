package cz.metacentrum.perun.wui.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.UmbrellaException;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;

/**
 * Presenter responsible for showing generic exception
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class ErrorReportPresenter extends Presenter<ErrorReportPresenter.MyView, ErrorReportPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@NameToken(PlaceTokens.ERROR)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<ErrorReportPresenter> {
	}

	@Inject
	public ErrorReportPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SET_MAIN_CONTENT);
	}
}
