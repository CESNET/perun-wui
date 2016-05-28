package cz.metacentrum.perun.wui.profile.client.resources;

import cz.metacentrum.perun.wui.client.resources.PerunTranslation;

/**
 * Perun Profile test translations
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunProfileTranslation extends PerunTranslation {

	@DefaultMessage("User profile")
	public String appName();

	@DefaultMessage("My profile")
	public String menuMyProfile();

	@DefaultMessage("Virtual organizations")
	public String menuOrganizations();

	@DefaultMessage("My identities")
	public String menuMyIdentities();

	@DefaultMessage("Logins & Passwords")
	public String menuLoginsAndPasswords();

	@DefaultMessage("Settings")
	public String menuSettings();

	@DefaultMessage("Title before")
	String title();

	@DefaultMessage("Name")
	String name();

	@DefaultMessage("Surname")
	String surname();

	@DefaultMessage("Title after")
	String titleAfter();

	@DefaultMessage("Preferred email")
	String preferredMail();

	@DefaultMessage("Preferred language")
	String preferredLang();

	@DefaultMessage("Timezone")
	String timezone();

	@DefaultMessage("Organization")
	String organization();

	@DefaultMessage("Phone")
	String phone();



	@DefaultMessage("Federated identity")
	String federatedIdp();

	@DefaultMessage("Login")
	String federatedLogin();

	@DefaultMessage("Certificate issuer")
	String x509Issuer();

	@DefaultMessage("Identity")
	String x509Identity();

	@DefaultMessage("Removing identity")
	String removingIdentity();

	@DefaultMessage("Loading identities")
	String loadingIdentities();

	@DefaultMessage("Add new federated identity")
	String addFed();

	@DefaultMessage("Add new certificate")
	String addCert();
}
