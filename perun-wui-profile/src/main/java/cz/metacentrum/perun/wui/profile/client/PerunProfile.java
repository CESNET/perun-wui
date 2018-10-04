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
import cz.metacentrum.perun.wui.profile.pages.completeinfo.CompleteInfoPresenter;
import cz.metacentrum.perun.wui.profile.pages.completeinfo.CompleteInfoView;
import cz.metacentrum.perun.wui.profile.pages.groups.GroupsPresenter;
import cz.metacentrum.perun.wui.profile.pages.groups.GroupsView;
import cz.metacentrum.perun.wui.profile.pages.identities.IdentitiesPresenter;
import cz.metacentrum.perun.wui.profile.pages.identities.IdentitiesView;
import cz.metacentrum.perun.wui.profile.pages.organizations.OrganizationsPresenter;
import cz.metacentrum.perun.wui.profile.pages.organizations.OrganizationsView;
import cz.metacentrum.perun.wui.profile.pages.personal.PersonalPresenter;
import cz.metacentrum.perun.wui.profile.pages.personal.PersonalView;
import cz.metacentrum.perun.wui.profile.pages.privacy.PrivacyPresenter;
import cz.metacentrum.perun.wui.profile.pages.privacy.PrivacyView;
import cz.metacentrum.perun.wui.profile.pages.resources.ResourcesPresenter;
import cz.metacentrum.perun.wui.profile.pages.resources.ResourcesView;
import cz.metacentrum.perun.wui.profile.pages.settings.SettingsPresenter;
import cz.metacentrum.perun.wui.profile.pages.settings.SettingsView;
import cz.metacentrum.perun.wui.profile.pages.settings.altPasswords.AltPasswordsPresenter;
import cz.metacentrum.perun.wui.profile.pages.settings.altPasswords.AltPasswordsView;
import cz.metacentrum.perun.wui.profile.pages.settings.sshkeys.SshKeysPresenter;
import cz.metacentrum.perun.wui.profile.pages.settings.sshkeys.SshKeysView;
import cz.metacentrum.perun.wui.profile.pages.settings.sshkeys.newadminsshkey.NewAdminSshKeyPresenter;
import cz.metacentrum.perun.wui.profile.pages.settings.sshkeys.newadminsshkey.NewAdminSshKeyView;
import cz.metacentrum.perun.wui.profile.pages.settings.sshkeys.newsshkey.NewSshKeyPresenter;
import cz.metacentrum.perun.wui.profile.pages.settings.sshkeys.newsshkey.NewSshKeyView;
import cz.metacentrum.perun.wui.profile.pages.settings.datalimits.DataQuotasPresenter;
import cz.metacentrum.perun.wui.profile.pages.settings.datalimits.DataQuotasView;
import cz.metacentrum.perun.wui.profile.pages.settings.preferredgroupnames.PreferredGroupNamesPresenter;
import cz.metacentrum.perun.wui.profile.pages.settings.preferredgroupnames.PreferredGroupNamesView;
import cz.metacentrum.perun.wui.profile.pages.settings.preferredshells.PreferredShellsPresenter;
import cz.metacentrum.perun.wui.profile.pages.settings.preferredshells.PreferredShellsView;

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
		bindPresenter(PrivacyPresenter.class, PrivacyPresenter.MyView.class, PrivacyView.class, PrivacyPresenter.MyProxy.class);
		bindPresenter(ResourcesPresenter.class, ResourcesPresenter.MyView.class, ResourcesView.class, ResourcesPresenter.MyProxy.class);
		bindPresenter(PersonalPresenter.class, PersonalPresenter.MyView.class, PersonalView.class, PersonalPresenter.MyProxy.class);
		bindPresenter(OrganizationsPresenter.class, OrganizationsPresenter.MyView.class, OrganizationsView.class, OrganizationsPresenter.MyProxy.class);
		bindPresenter(IdentitiesPresenter.class, IdentitiesPresenter.MyView.class, IdentitiesView.class, IdentitiesPresenter.MyProxy.class);
		bindPresenter(GroupsPresenter.class, GroupsPresenter.MyView.class, GroupsView.class, GroupsPresenter.MyProxy.class);
		bindPresenter(CompleteInfoPresenter.class, CompleteInfoPresenter.MyView.class, CompleteInfoView.class, CompleteInfoPresenter.MyProxy.class);
		bindPresenter(SshKeysPresenter.class, SshKeysPresenter.MyView.class, SshKeysView.class, SshKeysPresenter.MyProxy.class);
		bindPresenter(SettingsPresenter.class, SettingsPresenter.MyView.class, SettingsView.class, SettingsPresenter.MyProxy.class);
		bindPresenter(NewSshKeyPresenter.class, NewSshKeyPresenter.MyView.class, NewSshKeyView.class, NewSshKeyPresenter.MyProxy.class);
		bindPresenter(NewAdminSshKeyPresenter.class, NewAdminSshKeyPresenter.MyView.class, NewAdminSshKeyView.class, NewAdminSshKeyPresenter.MyProxy.class);
		bindPresenter(PreferredGroupNamesPresenter.class, PreferredGroupNamesPresenter.MyView.class, PreferredGroupNamesView.class, PreferredGroupNamesPresenter.MyProxy.class);
		bindPresenter(PreferredShellsPresenter.class, PreferredShellsPresenter.MyView.class, PreferredShellsView.class, PreferredShellsPresenter.MyProxy.class);
		bindPresenter(DataQuotasPresenter.class, DataQuotasPresenter.MyView.class, DataQuotasView.class, DataQuotasPresenter.MyProxy.class);
		bindPresenter(AltPasswordsPresenter.class, AltPasswordsPresenter.MyView.class, AltPasswordsView.class, AltPasswordsPresenter.MyProxy.class);

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