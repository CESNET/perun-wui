package cz.metacentrum.perun.wui.profile.client;

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
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileResources;
import cz.metacentrum.perun.wui.profile.pages.CompleteInfoPresenter;
import cz.metacentrum.perun.wui.profile.pages.CompleteInfoView;
import cz.metacentrum.perun.wui.profile.pages.GroupsPresenter;
import cz.metacentrum.perun.wui.profile.pages.GroupsView;
import cz.metacentrum.perun.wui.profile.pages.IdentitiesPresenter;
import cz.metacentrum.perun.wui.profile.pages.IdentitiesView;
import cz.metacentrum.perun.wui.profile.pages.OrganizationsPresenter;
import cz.metacentrum.perun.wui.profile.pages.OrganizationsView;
import cz.metacentrum.perun.wui.profile.pages.PersonalPresenter;
import cz.metacentrum.perun.wui.profile.pages.PersonalView;

/**
 * Entry point class and GWTP module for Perun WUI User profile.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunProfile extends AbstractPresenterModule implements EntryPoint {

	@Override
	protected void configure() {

		install(new DefaultModule.Builder().placeManager(PerunPlaceManager.class).build());

		// make sure app is embedded in a correct DIV
		bind(RootPresenter.class).to(PerunRootPresenter.class).asEagerSingleton();

		// Main Application must bind generic Presenter and custom View !!
		bindPresenter(PerunProfilePresenter.class, PerunProfilePresenter.MyView.class, PerunProfileView.class, PerunProfilePresenter.MyProxy.class);

		// bind app-specific pages
		// TODO - implement pages
		bindPresenter(PersonalPresenter.class, PersonalPresenter.MyView.class, PersonalView.class, PersonalPresenter.MyProxy.class);
		bindPresenter(OrganizationsPresenter.class, OrganizationsPresenter.MyView.class, OrganizationsView.class, OrganizationsPresenter.MyProxy.class);
		bindPresenter(IdentitiesPresenter.class, IdentitiesPresenter.MyView.class, IdentitiesView.class, IdentitiesPresenter.MyProxy.class);
		bindPresenter(GroupsPresenter.class, GroupsPresenter.MyView.class, GroupsView.class, GroupsPresenter.MyProxy.class);
		bindPresenter(CompleteInfoPresenter.class, CompleteInfoPresenter.MyView.class, CompleteInfoView.class, CompleteInfoPresenter.MyProxy.class);

		// pre-defined places
		bindConstant().annotatedWith(DefaultPlace.class).to(PerunProfilePlaceTokens.PERSONAL);
		bindConstant().annotatedWith(ErrorPlace.class).to(PerunProfilePlaceTokens.NOT_FOUND);
		bindConstant().annotatedWith(UnauthorizedPlace.class).to(PerunProfilePlaceTokens.UNAUTHORIZED);

		// generic pages
		bindPresenter(NotAuthorizedPresenter.class, NotAuthorizedPresenter.MyView.class, NotAuthorizedView.class, NotAuthorizedPresenter.MyProxy.class);
		bindPresenter(NotFoundPresenter.class, NotFoundPresenter.MyView.class, NotFoundView.class, NotFoundPresenter.MyProxy.class);
		bindPresenter(LogoutPresenter.class, LogoutPresenter.MyView.class, LogoutView.class, LogoutPresenter.MyProxy.class);
		bindPresenter(NotUserPresenter.class, NotUserPresenter.MyView.class, NotUserView.class, NotUserPresenter.MyProxy.class);

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

			PerunProfileResources.INSTANCE.gss().ensureInjected();

		} catch (Exception ex) {
			exceptionHandler.onUncaughtException(ex);
		}

	}

}