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

	@DefaultMessage("Choose")
	String select();

	@DefaultMessage("User profile")
	public String appName();

	@DefaultMessage("My profile")
	public String menuMyProfile();

	@DefaultMessage("My groups")
	String menuMyGroups();

	@DefaultMessage("Virtual Organizations")
	public String menuOrganizations();

	@DefaultMessage("My linked accounts")
	public String menuMyIdentities();

	@DefaultMessage("My services")
	String menuMyResources();

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


	@DefaultMessage("These are the accounts you use to access your services.")
	public String myIdentsText();

	@DefaultMessage("Certificate issuer")
	String x509Issuer();

	@DefaultMessage("Identity")
	String x509Identity();

	@DefaultMessage("Removing identity")
	String removingIdentity();

	@DefaultMessage("Loading identities")
	String loadingIdentities();

	@DefaultMessage("Link a new account")
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

	@DefaultMessage("Groups in which you are a member")
	String memberGroups();

	@DefaultMessage("Groups in which you are an admin")
	String adminGroups();

	@DefaultMessage("You are not a member of any group in selected OrganizationsView.")
	String noMemberGroups();

	@DefaultMessage("You are not admin of any group in selected OrganizationsView.")
	String noAdminGroups();

	@DefaultMessage("User information")
	String userInfo();

	@DefaultMessage("Information from virtual OrganizationsView")
	String voInfo();

	@DefaultMessage("Display all information")
	String completeInfo();

	@DefaultMessage("here")
	String here();

	@DefaultMessage("No resources found")
	String noResources();

	@DefaultMessage("Email")
	String uesEmail();

	@DefaultMessage("Show other identities")
	String otherIdentities();

	@DefaultMessage("External source name")
	String uesName();

	@DefaultMessage("No groups found.")
	String noGroups();

	@DefaultMessage("Groups")
	String groups();

	@DefaultMessage("Membership expiration")
	String membershipExpiration();

	@DefaultMessage("Extend")
	String extendMembership();

	@DefaultMessage("Privacy")
	String menuPrivacy();

	@DefaultMessage("To view all information that we keep about you, click")
	String showAllInfoText();

	@DefaultMessage("Here is all information that we keep about you. We keep this information for the purpose of providing services.")
	String allInfoDescriptionText();

	@DefaultMessage("Groups with access")
	String resourceGroups();

	@DefaultMessage("Data used for")
	String dataUsedFor();

	@DefaultMessage("Terms of use")
	String aupHeader();

	@DefaultMessage("No identities found")
	String noIdentities();

	@DefaultMessage("You have no SSH keys")
	String noSshKey();

	@DefaultMessage("You have no admin SSH keys")
	String noAdminSshKey();

	@DefaultMessage("SSH keys")
	String sshKeys();

	@DefaultMessage("Admin SSH keys")
	String adminSshKeysHeading();

	@DefaultMessage("Here is a list of your SSH keys used for root access.")
	String adminSshKeysNote();

	@DefaultMessage("Here is a list of your SSH keys.")
	String sshKeysNote();

	@DefaultMessage("New key")
	String newPublicKey();

	@DefaultMessage("New admin key")
	String newPublicAdminKey();

	@DefaultMessage("SSH Keys")
	String menuSettingsSshKeys();

	@DefaultMessage("Key")
	String key();

	@DefaultMessage("Add SSH key")
	String addPublicKey();

	@DefaultMessage("Add admin SSH key")
	String addAdminPublicKey();

	@DefaultMessage("Begins with ''ssh-rsa'', ''ssh-ed25519'', ''ecdsa-sha2-nistp256'', ''ecdsa-sha2-nistp384'' or ''ecdsa-sha2-nistp521''.")
	String newSshKeyInfoText();

	@DefaultMessage("Invalid value given. Your key should begin with ''ssh-rsa'', ''ssh-ed25519'', ''ecdsa-sha2-nistp256'', ''ecdsa-sha2-nistp384'' or ''ecdsa-sha2-nistp521''.")
	String sshInvalidPrefixText();

	@DefaultMessage("Cancel")
	String cancel();

	@DefaultMessage("Key value cannot contain multiple lines.")
	String sshInvalidNewLinesText();

	@DefaultMessage("Public part of key")
	String publicKey();

	@DefaultMessage("Preferred shells")
    String preferredShells();

	@DefaultMessage("Bona fide status")
	String bonaFideStatus();

	@DefaultMessage("List of preferred shells ordered from the most preferred to least is used to determine your shell on provided resources. If none of preferred shells is available on resource (or no preferred shell is set), resource''s default is used.")
	String preferredShellsInfo();

	@DefaultMessage("Custom value")
	String customValue();

	@DefaultMessage("Enter custom shell")
	String customShellValueHeader();

	@DefaultMessage("Update value")
	String updateValue();

	@DefaultMessage("Change")
	String change();

	@DefaultMessage("Add preferred shell")
	String addPreferredShell();

	@DefaultMessage("Invalid shell value. Example: ''/bin/bash''.")
	String invalidShellValue();

	@DefaultMessage("Shell value can not be empty.")
	String emptyShellValue();

	@DefaultMessage("Preferred Unix group names")
	String unixGroupNames();

	@DefaultMessage("Group names in namespaces ''")
	String preferredGroupNameHeaderText();

	@DefaultMessage("Add preferred group name")
	String addPreferredGroupName();

	@DefaultMessage("Edit preferred group name")
	String changePreferredGroupName();

	@DefaultMessage("Enter new preferred group name")
	String newPreferredGroupName();

	@DefaultMessage("Data quotas")
	String unixDataQuotas();

	@DefaultMessage("Data quota")
	String dataQuota();

	@DefaultMessage("Files quota")
	String filesQuota();

	@DefaultMessage("Default")
	String defaultValue();

	@DefaultMessage("Using default")
	String usingDefault();

	@DefaultMessage("Request change")
	String requestChange();

	@DefaultMessage("Data quota change request")
	String dataQuotaRequestChange();

	@DefaultMessage("Files quota change request")
	String filesQuotaRequestChange();

	@DefaultMessage("Resource")
	String resourceHeading();

	@DefaultMessage("Current quota")
	String currentQuota();

	@DefaultMessage("Value can not be empty!")
	String cannotBeEmpty();

	@DefaultMessage("Value must be number!")
	String mustBeNumber();

	@DefaultMessage("No resources where you can request quota change were found.")
	String noQuotaResources();

	@DefaultMessage("RT ticket QUOTA: Change request sent. Responses will be sent to the e-mail address: ")
	String rtMessageSuccess();

	@DefaultMessage("Sending request")
	String sendingRequest();

	@DefaultMessage("Requested quota")
	String requestedQuota();

	@DefaultMessage("Reason")
	String reason();

	@DefaultMessage("Not set")
	String notSet();

}
