package cz.metacentrum.perun.wui.userprofile.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import cz.metacentrum.perun.wui.client.PerunPlaceManager;
import cz.metacentrum.perun.wui.client.PerunRootPresenter;
import cz.metacentrum.perun.wui.client.resources.ExceptionLogger;
import cz.metacentrum.perun.wui.client.resources.PerunResources;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.pages.*;
import cz.metacentrum.perun.wui.userprofile.client.resources.UserProfileResources;
import cz.metacentrum.perun.wui.userprofile.pages.OrganizationsPresenter;
import cz.metacentrum.perun.wui.userprofile.pages.OrganizationsView;
import cz.metacentrum.perun.wui.userprofile.pages.PersonalPresenter;
import cz.metacentrum.perun.wui.userprofile.pages.PersonalView;

/**
 * Entry point class and GWTP module for Perun WUI User profile.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class UserProfile extends AbstractPresenterModule implements EntryPoint {

	@Override
	protected void configure() {

		install(new DefaultModule.Builder().placeManager(PerunPlaceManager.class).build());

		// make sure app is embedded in a correct DIV
		bind(RootPresenter.class).to(PerunRootPresenter.class).asEagerSingleton();

		// Main Application must bind generic Presenter and custom View !!
		bindPresenter(UserProfilePresenter.class, UserProfilePresenter.MyView.class, UserProfileView.class, UserProfilePresenter.MyProxy.class);

		// bind app-specific pages
		bindPresenter(PersonalPresenter.class, PersonalPresenter.MyView.class, PersonalView.class, PersonalPresenter.MyProxy.class);
		bindPresenter(OrganizationsPresenter.class, OrganizationsPresenter.MyView.class, OrganizationsView.class, OrganizationsPresenter.MyProxy.class);
		//bindPresenter(FormPresenter.class, FormPresenter.MyView.class, FormView.class, FormPresenter.MyProxy.class);
		//bindPresenter(AppsPresenter.class, AppsPresenter.MyView.class, AppsView.class, AppsPresenter.MyProxy.class);
		//bindPresenter(AppDetailPresenter.class, AppDetailPresenter.MyView.class, AppDetailView.class, AppDetailPresenter.MyProxy.class);

		// pre-defined places
		bindConstant().annotatedWith(DefaultPlace.class).to(UserProfilePlaceTokens.PERSONAL);
		bindConstant().annotatedWith(ErrorPlace.class).to(UserProfilePlaceTokens.NOT_FOUND);
		bindConstant().annotatedWith(UnauthorizedPlace.class).to(UserProfilePlaceTokens.UNAUTHORIZED);

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

			UserProfileResources.INSTANCE.gss().ensureInjected();

		} catch (Exception ex) {
			exceptionHandler.onUncaughtException(ex);
		}

	}

}