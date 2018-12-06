package cz.metacentrum.perun.wui.pwdreset.client.resources;

import cz.metacentrum.perun.wui.client.resources.PerunTranslation;

/**
 * Class providing translations to Pwd Reset GUI.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunPwdResetTranslation extends PerunTranslation {

	// -------------- APP NAME ------------------------ //

	@DefaultMessage("Password reset")
	public String pwdresetAppName();

	@DefaultMessage("Please perform the confirmation below to continue")
	public String pleaseVerifyCaptcha();

	@DefaultMessage("Verification failed. Please perform the confirmation again.")
	String captchaFailed();


	// -------------- PWD-RESET PAGE ------------------------ //

	@DefaultMessage("Password reset for {0}")
	public String passwordResetFor(String login);

	@DefaultMessage("Continue")
	public String continueButton();

	@DefaultMessage("Reset password")
	public String submitPwdResetButton();

	@DefaultMessage("Passwords doesn`t match!")
	public String passwordsDoesnMatch();

	@DefaultMessage("Password can`t be empty!")
	public String passwordCantBeEmpty();

	@DefaultMessage("Enter new password twice")
	public String pwdresetLabel();

	@DefaultMessage("Password changed successfully. It might take 5-10 minutes for a change to take place across whole infrastructure.")
	public String resetSuccess();

	@DefaultMessage("Password reset in a namespace <i>{0}</i> is not supported.")
	public String namespaceNotSupported(String namespace);

	@DefaultMessage("You can`t reset password. You don`t have login in namespace <i>{0}</i>.")
	public String dontHaveLogin(String namespace);

	@DefaultMessage("Please <b>avoid using accented characters</b>. It might not be supported by all backend components and services.")
	public String dontUseAccents();

	@DefaultMessage("Password must be from 8 up to 20 characters long and contain only printable (not accented) characters. It must contain min. 3 uppercase or/and lowercase letters and min. 1 number or symbol.")
	public String metaHelp();

	@DefaultMessage("Password must be from 8 to 20 characters long.")
	public String metaLength();

	@DefaultMessage("Password can contain only printable (not accented) characters. It must contain min. 3 uppercase or/and lowercase letters and min. 1 number or symbol.")
	public String metaStrength();

	// -------------- ACTIVATE ACCOUNT TRANSLATION ------------------------ //

	@DefaultMessage("Activate account")
	public String activateAppName();

	@DefaultMessage("Activate account for {0}")
	public String activateFor(String login);

	@DefaultMessage("Activate account")
	public String submitActivateButton();

	@DefaultMessage("Account is active.")
	public String activateSuccess();

	@DefaultMessage("Account activation in a namespace <i>{0}</i> is not supported.")
	public String namespaceNotSupportedActive(String namespace);

	@DefaultMessage("Can`t activate account. You don`t have login in namespace <i>{0}</i>.")
	public String dontHaveLoginActive(String namespace);

}
