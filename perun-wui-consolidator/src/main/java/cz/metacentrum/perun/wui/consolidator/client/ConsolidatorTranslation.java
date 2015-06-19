package cz.metacentrum.perun.wui.consolidator.client;

import cz.metacentrum.perun.wui.client.resources.PerunTranslation;

/**
 * Class providing translations to Consolidator GUI.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface ConsolidatorTranslation extends PerunTranslation {

	/* --  SELECT PAGE -- */

	@DefaultMessage("Institutional identity")
	public String idpButton();

	@DefaultMessage("Personal certificate")
	public String certButton();

	@DefaultMessage("Kerberos login")
	public String krbButton();

	@DefaultMessage("Social identity")
	public String socialButton();

	@DefaultMessage("Select your identity provider")
	public String selectIdP();

	@DefaultMessage("Type to search...")
	public String typeToSearch();

	@DefaultMessage("Your identity is not registered yet. In a next step please select registered identity.")
	public String notRegistered();

	@DefaultMessage("Your current identity is")
	public String currentIdentityIs();

	@DefaultMessage("Join with")
	public String joinWith();

	@DefaultMessage("Identity consolidation")
	public String topConsolidateButton();

	/* --  JOIN PAGE -- */

	@DefaultMessage("Continue")
	public String finishButtonContinue();

	@DefaultMessage("Leave")
	public String finishButtonLeave();

	@DefaultMessage("Join another identity")
	public String backButton();

	@DefaultMessage("Your identities were successfully joined.")
	public String joinedMessage();

	@DefaultMessage("Your have following registered identities")
	public String myIdents();

	/* -- EXCEPTIONS -- */

	@DefaultMessage("Your token for joining identities is no longer valid. Please retry from the start.")
	public String invalidTokenException();

	@DefaultMessage("Neither original or current identity is registered. Please use registered identity at least in a one step.")
	public String identityUnknownException();

	@DefaultMessage("You tried to join identity ({0}) with itself. Please go back and select different identity to join with.")
	public String identityIsSameException(String identity);

	@DefaultMessage("You already have both identities joined.")
	public String identitiesAlreadyJoinedException();

	@DefaultMessage("Your identities are split between two user accounts. If you wish to merge them, contact user support at: perun@cesnet.cz")
	public String identityAlreadyInUseException();

}
