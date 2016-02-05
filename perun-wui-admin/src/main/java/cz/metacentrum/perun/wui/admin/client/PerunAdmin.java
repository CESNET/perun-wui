package cz.metacentrum.perun.wui.admin.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import cz.metacentrum.perun.wui.admin.client.resources.PerunAdminResources;
import cz.metacentrum.perun.wui.admin.pages.perunManagement.*;
import cz.metacentrum.perun.wui.admin.pages.servicesManagement.ServiceDetailPresenter;
import cz.metacentrum.perun.wui.admin.pages.servicesManagement.ServiceDetailView;
import cz.metacentrum.perun.wui.admin.pages.perunManagement.ownersManagement.OwnerCreatePresenter;
import cz.metacentrum.perun.wui.admin.pages.perunManagement.ownersManagement.OwnerCreateView;
import cz.metacentrum.perun.wui.admin.pages.vosManagement.VoDetailPresenter;
import cz.metacentrum.perun.wui.admin.pages.vosManagement.VoDetailView;
import cz.metacentrum.perun.wui.admin.pages.vosManagement.VoSelectPresenter;
import cz.metacentrum.perun.wui.admin.pages.vosManagement.VoSelectView;
import cz.metacentrum.perun.wui.client.PerunRootPresenter;
import cz.metacentrum.perun.wui.client.resources.ExceptionLogger;
import cz.metacentrum.perun.wui.client.resources.PerunResources;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.pages.*;

/**
 * Entry point class of Perun admin Wui and also GWTP module configuration.
 *
 * All pages (presenters) used in this app must be bind() here.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunAdmin extends AbstractPresenterModule implements EntryPoint {

	//private static PerunLoader loader = new PerunLoader();

	@Override
	protected void configure() {

		install(new DefaultModule.Builder().placeManager(PerunAdminPlaceManager.class).build());

		// make sure app is embedded in a correct DIV
		bind(RootPresenter.class).to(PerunRootPresenter.class).asEagerSingleton();

		// Main Application must bind generic Presenter and custom View !!
		bindPresenter(PerunAdminPresenter.class, PerunAdminPresenter.MyView.class, PerunAdminView.class, PerunAdminPresenter.MyProxy.class);

		// pre-defined places
		bindConstant().annotatedWith(DefaultPlace.class).to(PlaceTokens.VOS);
		bindConstant().annotatedWith(ErrorPlace.class).to(PlaceTokens.NOT_FOUND);
		bindConstant().annotatedWith(UnauthorizedPlace.class).to(PlaceTokens.UNAUTHORIZED);

		// generic pages
		bindPresenter(NotAuthorizedPresenter.class, NotAuthorizedPresenter.MyView.class, NotAuthorizedView.class, NotAuthorizedPresenter.MyProxy.class);
		bindPresenter(NotFoundPresenter.class, NotFoundPresenter.MyView.class, NotFoundView.class, NotFoundPresenter.MyProxy.class);
		bindPresenter(LogoutPresenter.class, LogoutPresenter.MyView.class, LogoutView.class, LogoutPresenter.MyProxy.class);

		// Perun Admin WUI specific pages
		bindPresenter(VosManagementPresenter.class, VosManagementPresenter.MyView.class, VosManagementView.class, VosManagementPresenter.MyProxy.class);
		bindPresenter(VoDetailPresenter.class, VoDetailPresenter.MyView.class, VoDetailView.class, VoDetailPresenter.MyProxy.class);

		bindPresenter(ServicesManagementPresenter.class, ServicesManagementPresenter.MyView.class, ServicesManagementView.class, ServicesManagementPresenter.MyProxy.class);
		bindPresenter(ServiceDetailPresenter.class, ServiceDetailPresenter.MyView.class, ServiceDetailView.class, ServiceDetailPresenter.MyProxy.class);

		bindPresenter(UsersManagementPresenter.class, UsersManagementPresenter.MyView.class, UsersManagementView.class, UsersManagementPresenter.MyProxy.class);
		bindPresenter(FacilitiesManagementPresenter.class, FacilitiesManagementPresenter.MyView.class, FacilitiesManagementView.class, FacilitiesManagementPresenter.MyProxy.class);
		bindPresenter(AttributesManagementPresenter.class, AttributesManagementPresenter.MyView.class, AttributesManagementView.class, AttributesManagementPresenter.MyProxy.class);
		bindPresenter(ExtSourcesManagementPresenter.class, ExtSourcesManagementPresenter.MyView.class, ExtSourcesManagementView.class, ExtSourcesManagementPresenter.MyProxy.class);
		bindPresenter(OwnersManagementPresenter.class, OwnersManagementPresenter.MyView.class, OwnersManagementView.class, OwnersManagementPresenter.MyProxy.class);

		// Bind Presenter-Widgets
		bindSingletonPresenterWidget(LeftMenuPresenter.class, LeftMenuPresenter.MyView.class, LeftMenuView.class);
		bindSingletonPresenterWidget(VoSelectPresenter.class, VoSelectPresenter.MyView.class, VoSelectView.class);
		bindSingletonPresenterWidget(OwnerCreatePresenter.class, OwnerCreatePresenter.MyView.class, OwnerCreateView.class);

	}

	@Override
	public void onModuleLoad() {

		GWT.UncaughtExceptionHandler handler = new ExceptionLogger();
		GWT.setUncaughtExceptionHandler(handler);

		try {
			// ensure injecting custom CSS styles of PerunWui
			PerunResources.INSTANCE.gss().ensureInjected();

			PerunAdminResources.INSTANCE.gss().ensureInjected();

			// set default for Growl plugin
			Utils.getDefaultNotifyOptions().makeDefault();

		} catch (RuntimeException ex) {
			handler.onUncaughtException(ex);
		}

	}

}