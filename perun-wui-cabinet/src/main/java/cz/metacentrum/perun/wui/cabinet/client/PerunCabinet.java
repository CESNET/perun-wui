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
import cz.metacentrum.perun.wui.cabinet.pages.PublicationsPresenter;
import cz.metacentrum.perun.wui.cabinet.pages.PublicationsView;
import cz.metacentrum.perun.wui.client.PerunPlaceManager;
import cz.metacentrum.perun.wui.client.PerunRootPresenter;
import cz.metacentrum.perun.wui.client.resources.ExceptionLogger;
import cz.metacentrum.perun.wui.client.resources.PerunResources;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.pages.*;

/**
 * Entry point for Cabinet application
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
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

		// pre-defined places
		bindConstant().annotatedWith(DefaultPlace.class).to(PerunCabinetPlaceTokens.HOME);
		bindConstant().annotatedWith(ErrorPlace.class).to(PerunCabinetPlaceTokens.NOT_FOUND);
		bindConstant().annotatedWith(UnauthorizedPlace.class).to(PerunCabinetPlaceTokens.UNAUTHORIZED);

		// generic pages
		bindPresenter(NotAuthorizedPresenter.class, NotAuthorizedPresenter.MyView.class, NotAuthorizedView.class, NotAuthorizedPresenter.MyProxy.class);
		bindPresenter(NotFoundPresenter.class, NotFoundPresenter.MyView.class, NotFoundView.class, NotFoundPresenter.MyProxy.class);
		bindPresenter(LogoutPresenter.class, LogoutPresenter.MyView.class, LogoutView.class, LogoutPresenter.MyProxy.class);
		bindPresenter(NotUserPresenter.class, NotUserPresenter.MyView.class, NotUserView.class, NotUserPresenter.MyProxy.class);
	}

	/*
	interface PerunCabinetUiBinder extends UiBinder<Widget, PerunCabinet>{}

	private static PerunCabinetUiBinder uiBinder = GWT.create(PerunCabinetUiBinder.class);

	private static boolean perunLoaded = false;
	private static PerunLoader guiLoader = new PerunLoader();
	private PerunCabinet gui = this;

	@UiField Navbar navbar;
	@UiField NavbarHeader navbarHeader;

	@Override
	public void onModuleLoad() {

		// ensure injecting custom CSS styles of PerunWui
		PerunResources.INSTANCE.gss().ensureInjected();

		AuthzManager.getPerunPrincipal(new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject jso) {

				PerunPrincipal pp = ((PerunPrincipal) jso);

				PerunSession.getInstance().setPerunPrincipal(pp);
				PerunSession.getInstance().setRoles(pp.getRoles());

				// TODO - later load this setting from local storage too
				PerunSession.getInstance().setExtendedInfoVisible(PerunSession.getInstance().isPerunAdmin());

				History.addValueChangeHandler(gui);

				UtilsManager.getGuiConfiguration(new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject jso) {

						// store configuration
						PerunSession.getInstance().setConfiguration((BasicOverlayObject) jso.cast());

						RootLayoutPanel.get().clear();
						RootLayoutPanel.get().add(uiBinder.createAndBindUi(gui));

						Image logo = new Image(PerunResources.INSTANCE.getPerunLogo());
						logo.setWidth("auto");
						logo.setHeight("65px");
						navbarHeader.insert(logo, 0);

						// TRIGGER LOADING DEFAULT TABS
						//PerunCabinet.getContent().openTab(History.getToken());

						perunLoaded = true;

					}

					@Override
					public void onError(PerunException error) {
						perunLoaded = false;
						//guiLoader.onError(error);
					}

					@Override
					public void onLoadingStart() {

					}
				});

			}

			@Override
			public void onError(PerunException error) {
				perunLoaded = false;
				//guiLoader.onError(error);
			}

			@Override
			public void onLoadingStart() {
				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(guiLoader.getWidget());

				Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand() {
					@Override
					public boolean execute() {
						if (guiLoader.getProgressBar().getPercent() <= 100) {
							guiLoader.getProgressBar().setPercent(guiLoader.getProgressBar().getPercent() + 1);
							return true;
						}
						return false;
					}
				}, 200);

				perunLoaded = false;
			}
		});

	}

	@Override
	public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {

		// if GUI not loaded, change should force module loading
		if (!perunLoaded) {
			onModuleLoad();
			return;
		}

		// when there is no token, default tabs are loaded
		// this is useful if user has bookmarked a site other than the homepage.
		if (History.getToken().isEmpty()) {

			// get whole URL
			String url = Window.Location.getHref();
			String newToken = "";

			int index = -1;

			if (url.contains("?locale=")) {
				// with locale
				index = url.indexOf("?", url.indexOf("?") + 1);
			} else {
				// without locale
				index = url.indexOf("?");
			}

			if (index != -1) {
				newToken = url.substring(index + 1);
			}

			// will sort of break URL, but will work without refreshing whole GUI
			if (newToken.isEmpty()) {
				// token was empty anyway - load default
				//PerunCabinet.getContent().openTab(newToken);

			} else {
				// token is now correct - load it
				History.newItem(newToken);
			}

		} else {
			//PerunCabinet.getContent().openTab(History.getToken());
		}

	}
*/


}