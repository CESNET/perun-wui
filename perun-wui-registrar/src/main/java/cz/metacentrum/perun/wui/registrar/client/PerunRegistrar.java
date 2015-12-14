package cz.metacentrum.perun.wui.registrar.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import cz.metacentrum.perun.wui.client.PerunPlaceManager;
import cz.metacentrum.perun.wui.client.resources.ExceptionLogger;
import cz.metacentrum.perun.wui.client.resources.PerunResources;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.pages.*;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunWuiRegistrarResources;
import cz.metacentrum.perun.wui.registrar.pages.*;

/**
 * Entry point class and GWTP module for Perun WUI Registrar.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunRegistrar extends AbstractPresenterModule implements EntryPoint{

	@Override
	protected void configure() {

		install(new DefaultModule.Builder().placeManager(PerunPlaceManager.class).build());

		// Main Application must bind generic Presenter or its subclass and custom View !!
		bindPresenter(PerunRegistrarPresenter.class, PerunRegistrarPresenter.MyView.class, PerunRegistrarView.class, PerunRegistrarPresenter.MyProxy.class);

		// bind app-specific pages
		bindPresenter(FormPresenter.class, FormPresenter.MyView.class, FormView.class, FormPresenter.MyProxy.class);
		bindPresenter(AppsPresenter.class, AppsPresenter.MyView.class, AppsView.class, AppsPresenter.MyProxy.class);
		bindPresenter(AppDetailPresenter.class, AppDetailPresenter.MyView.class, AppDetailView.class, AppDetailPresenter.MyProxy.class);
		bindPresenter(VerifyEmailPresenter.class, VerifyEmailPresenter.MyView.class, VerifyEmailView.class, VerifyEmailPresenter.MyProxy.class);

		// pre-defined places
		bindConstant().annotatedWith(DefaultPlace.class).to(RegistrarPlaceTokens.FORM);
		bindConstant().annotatedWith(ErrorPlace.class).to(RegistrarPlaceTokens.NOT_FOUND);
		bindConstant().annotatedWith(UnauthorizedPlace.class).to(RegistrarPlaceTokens.UNAUTHORIZED);

		// generic pages
		bindPresenter(NotAuthorizedPresenter.class, NotAuthorizedPresenter.MyView.class, NotAuthorizedView.class, NotAuthorizedPresenter.MyProxy.class);
		bindPresenter(NotFoundPresenter.class, NotFoundPresenter.MyView.class, NotFoundView.class, NotFoundPresenter.MyProxy.class);
		bindPresenter(LogoutPresenter.class, LogoutPresenter.MyView.class, LogoutView.class, LogoutPresenter.MyProxy.class);

	}

	@Override
	public void onModuleLoad() {

		ExceptionLogger exceptionHandler = new ExceptionLogger();
		GWT.setUncaughtExceptionHandler(exceptionHandler);

		try {

			// set default for Growl plugin
			Utils.getDefaultNotifyOptions().makeDefault();

			// ensure injecting custom CSS styles of PerunWui
			PerunResources.INSTANCE.gss().ensureInjected();

			PerunWuiRegistrarResources.INSTANCE.gss().ensureInjected();

		} catch (Exception ex) {
			exceptionHandler.onUncaughtException(ex);
		}

	}

}