package cz.metacentrum.perun.wui.cabinet.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import cz.metacentrum.perun.wui.cabinet.client.resources.PerunCabinetPlaceTokens;
import cz.metacentrum.perun.wui.cabinet.client.resources.PerunCabinetResources;
import cz.metacentrum.perun.wui.cabinet.pages.NewPublicationPresenter;
import cz.metacentrum.perun.wui.cabinet.pages.NewPublicationView;
import cz.metacentrum.perun.wui.cabinet.pages.PublicationsPresenter;
import cz.metacentrum.perun.wui.cabinet.pages.PublicationsView;
import cz.metacentrum.perun.wui.client.PerunPlaceManager;
import cz.metacentrum.perun.wui.client.PerunRootPresenter;
import cz.metacentrum.perun.wui.client.resources.ExceptionLogger;
import cz.metacentrum.perun.wui.client.resources.PerunResources;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.pages.LogoutPresenter;
import cz.metacentrum.perun.wui.pages.LogoutView;
import cz.metacentrum.perun.wui.pages.NotAuthorizedPresenter;
import cz.metacentrum.perun.wui.pages.NotAuthorizedView;
import cz.metacentrum.perun.wui.pages.NotFoundPresenter;
import cz.metacentrum.perun.wui.pages.NotFoundView;
import cz.metacentrum.perun.wui.pages.NotUserPresenter;
import cz.metacentrum.perun.wui.pages.NotUserView;

/**
 * Entry point for Cabinet application
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PerunCabinet extends AbstractPresenterModule implements EntryPoint {

	@Override
	public void onModuleLoad() {
		ExceptionLogger exceptionHandler = new ExceptionLogger();
		GWT.setUncaughtExceptionHandler(exceptionHandler);

		try {

			// set default for Growl plugin
			Utils.getDefaultNotifyOptions().makeDefault();

			// ensure injecting custom CSS styles of PerunWui
			PerunResources.INSTANCE.gss().ensureInjected();

			PerunCabinetResources.INSTANCE.gss().ensureInjected();

		} catch (Exception ex) {
			exceptionHandler.onUncaughtException(ex);
		}
	}

	@Override
	protected void configure() {
		install(new DefaultModule.Builder().placeManager(PerunPlaceManager.class).build());

		// make sure app is embedded in a correct DIV
		bind(RootPresenter.class).to(PerunRootPresenter.class).asEagerSingleton();

		// Main Application must bind generic Presenter and custom View !!
		bindPresenter(PerunCabinetPresenter.class, PerunCabinetPresenter.MyView.class, PerunCabinetView.class, PerunCabinetPresenter.MyProxy.class);

		// bind app-specific pages
		// TODO - implement pages
		bindPresenter(PublicationsPresenter.class, PublicationsPresenter.MyView.class, PublicationsView.class, PublicationsPresenter.MyProxy.class);
		bindPresenter(NewPublicationPresenter.class, NewPublicationPresenter.MyView.class, NewPublicationView.class, NewPublicationPresenter.MyProxy.class);

		// pre-defined places
		bindConstant().annotatedWith(DefaultPlace.class).to(PerunCabinetPlaceTokens.PUBLICATIONS);
		bindConstant().annotatedWith(ErrorPlace.class).to(PerunCabinetPlaceTokens.NOT_FOUND);
		bindConstant().annotatedWith(UnauthorizedPlace.class).to(PerunCabinetPlaceTokens.UNAUTHORIZED);

		// generic pages
		bindPresenter(NotAuthorizedPresenter.class, NotAuthorizedPresenter.MyView.class, NotAuthorizedView.class, NotAuthorizedPresenter.MyProxy.class);
		bindPresenter(NotFoundPresenter.class, NotFoundPresenter.MyView.class, NotFoundView.class, NotFoundPresenter.MyProxy.class);
		bindPresenter(LogoutPresenter.class, LogoutPresenter.MyView.class, LogoutView.class, LogoutPresenter.MyProxy.class);
		bindPresenter(NotUserPresenter.class, NotUserPresenter.MyView.class, NotUserView.class, NotUserPresenter.MyProxy.class);
	}
}