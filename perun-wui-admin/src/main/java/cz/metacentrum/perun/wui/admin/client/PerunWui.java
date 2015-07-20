package cz.metacentrum.perun.wui.admin.client;

import com.google.gwt.core.client.EntryPoint;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import cz.metacentrum.perun.wui.admin.pages.perunManagement.VosManagementPresenter;
import cz.metacentrum.perun.wui.admin.pages.perunManagement.VosManagementView;
import cz.metacentrum.perun.wui.admin.pages.vosManagement.VoDetailPresenter;
import cz.metacentrum.perun.wui.admin.pages.vosManagement.VoDetailView;
import cz.metacentrum.perun.wui.admin.pages.vosManagement.VoSelectPresenter;
import cz.metacentrum.perun.wui.admin.pages.vosManagement.VoSelectView;
import cz.metacentrum.perun.wui.client.PerunPresenter;
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
public class PerunWui extends AbstractPresenterModule implements EntryPoint {

	//private static PerunLoader loader = new PerunLoader();

	@Override
	protected void configure() {

		install(new DefaultModule(PerunWuiPlaceManager.class));

		// Main Application must bind generic Presenter and custom View !!
		bindPresenter(PerunPresenter.class, PerunPresenter.MyView.class, PerunWuiView.class, PerunPresenter.MyProxy.class);

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

		// Bind Presenter-Widgets
		bindSingletonPresenterWidget(LeftMenuPresenter.class, LeftMenuPresenter.MyView.class, LeftMenuView.class);
		bindSingletonPresenterWidget(VoSelectPresenter.class, VoSelectPresenter.MyView.class, VoSelectView.class);

	}

	@Override
	public void onModuleLoad() {

		// ensure injecting custom CSS styles of PerunWui
		PerunResources.INSTANCE.gss().ensureInjected();

		// set default for Growl plugin
		Utils.getDefaultGrowlOptions().makeDefault();

	}

}