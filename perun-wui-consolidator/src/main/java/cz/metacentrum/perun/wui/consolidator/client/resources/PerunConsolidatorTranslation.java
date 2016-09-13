package cz.metacentrum.perun.wui.consolidator.client.resources;

import cz.metacentrum.perun.wui.client.resources.PerunTranslation;

/**
 * Class providing translations to Consolidator GUI.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunConsolidatorTranslation extends PerunTranslation {

	/* --  GENERIC -- */

	@DefaultMessage("Identity consolidator")
	public String appName();

	/* --  SELECT PAGE -- */

	@DefaultMessage("Select your organization")
	public String selectIdP();

	@DefaultMessage("Search your organization name...")
	public String searchYouOrganization();

	@DefaultMessage("Your current identity is not registered. Now please select already registered identity (previously used to access Perun).")
	public String notRegistered();

	@DefaultMessage("Your are signed in with")
	public String currentIdentityIs();

	@DefaultMessage("link new account from")
	public String joinWith();

	@DefaultMessage("at")
	public String at();

	@DefaultMessage("Loading organizations")
	public String loadingOrganizations();

	@DefaultMessage("No organization found")
	public String noOrganizationFound();

	@DefaultMessage("Your authorization token will expire in {0}s. Please make your choice before that.")
	public String authorizationTokenWillExpire(int count);

	@DefaultMessage("Your authorization token has expired. Please reload the page to retry.")
	public String authorizationTokenHasExpired();

	/* --  JOIN PAGE -- */

	@DefaultMessage("Continue")
	public String finishButtonContinue();

	@DefaultMessage("Leave")
	public String finishButtonLeave();

	@DefaultMessage("Link another account")
	public String backButton();

	@DefaultMessage("Your identities were successfully linked.")
	public String joinedMessage();

	@DefaultMessage("You have following registered identities")
	public String myIdents();

	/* -- EXCEPTIONS -- */

	@DefaultMessage("Your token for joining identities is no longer valid. Please retry from the start.")
	public String invalidTokenException();

	@DefaultMessage("Neither original or current identity is registered. Please use registered identity at least in a one step.")
	public String identityUnknownException();

	@DefaultMessage("You tried to join identity ({0}) with itself. Please go back and select different identity to join with.")
	public String identityIsSameException(String identity);

	@DefaultMessage("You already had both identities joined.")
	public String identitiesAlreadyJoinedException();

	@DefaultMessage("Your identities are split between two user accounts. If you wish to merge them, contact user support at: {0}")
	public String identityAlreadyInUseException(String userSupportMail);

}
