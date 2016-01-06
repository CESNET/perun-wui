package cz.metacentrum.perun.wui.userprofile.client;

import cz.metacentrum.perun.wui.client.resources.PerunTranslation;

/**
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface UserProfileTranslation extends PerunTranslation {

	@DefaultMessage("User profile")
	public String appMyProfile();

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

}
