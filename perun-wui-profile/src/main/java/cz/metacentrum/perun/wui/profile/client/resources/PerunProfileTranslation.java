package cz.metacentrum.perun.wui.profile.client.resources;

import cz.metacentrum.perun.wui.client.resources.PerunTranslation;

/**
 * Perun Profile test translations
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunProfileTranslation extends PerunTranslation {

	@DefaultMessage("Member ID")
	String memberId();

	@DefaultMessage("Member status")
	String memberStatus();

	@DefaultMessage("Member details")
	String memberDetails();

	@DefaultMessage("Select VO")
	String selectVo();

	@DefaultMessage("User profile")
	public String appName();

	@DefaultMessage("My profile")
	public String menuMyProfile();

	@DefaultMessage("My groups")
	String menuMyGroups();

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

	@DefaultMessage("E-mail")
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

	@DefaultMessage("Loading user data")
	String loadingUserData();

	@DefaultMessage("Change")
	String updateEmail();

	@DefaultMessage("New e-mail")
	String newPreferredEmail();

	@DefaultMessage("Request e-mail change (you will receive validation e-mail)")
	String sendValidationEmail();

	@DefaultMessage("Sending validation e-mail")
	String requestingEmailUpdate();

	@DefaultMessage("Invalid e-mail format")
	String wrongEmailFormat();

	@DefaultMessage("Validation e-mail has been send to {0}. Check your inbox and complete validation by clicking on link in received e-mail.")
	String haveRequestedEmailUpdate(String emails);

	@DefaultMessage("E-mail change")
	String updateEmailModalTitle();

	@DefaultMessage("Member groups")
	String memberGroups();

	@DefaultMessage("You are not a member of any group in selected VO.")
	String noGroups();
}
