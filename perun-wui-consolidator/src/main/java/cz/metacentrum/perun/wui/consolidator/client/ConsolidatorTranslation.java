package cz.metacentrum.perun.wui.consolidator.client;

import cz.metacentrum.perun.wui.client.resources.PerunTranslation;

/**
 * Class providing translations to Consolidator GUI.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface ConsolidatorTranslation extends PerunTranslation {

	@DefaultMessage("Institutional identity")
	public String idpButton();

	@DefaultMessage("Personal certificate")
	public String certButton();

	@DefaultMessage("Kerberos login")
	public String krbButton();

	@DefaultMessage("Social identity")
	public String socialButton();

	@DefaultMessage("Please select your identity provider")
	public String selectIdP();

	@DefaultMessage("Type to search...")
	public String typeToSearch();

	@DefaultMessage("Your current identity is not registered yet. As a next step please select identity, which is already registered.")
	public String notRegistered();

	@DefaultMessage("Your current identity is")
	public String currentIdentityIs();

	@DefaultMessage("Join with")
	public String joinWith();

}
