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

	@DefaultMessage("Password must be at least {0} characters long.")
	public String passwordLength(int length);

	@DefaultMessage("<ul><li>the minimum password length is 8 characters</li><li>must contain characters of at least three sets of characters</li><li>character sets are: [a-z], [A-Z], [0-9], [!#%&()[]*+,./:;<=>?@^_&#96;&#123;&#125;&#124;~-]</li><li>letters must not contain diacritics, the space is allowed</li><li>may not include your name, surname, personal number or username</li></ul>")
	public String vsupHelp();

	@DefaultMessage("Password must contain characters from at least 3 of 4 character sets: {0}")
	public String passwordStrength4(String characterSets);

	@DefaultMessage("Password may not include your name, surname, personal number or username!")
	public String passwordStrength2();

	@DefaultMessage("Password can`t contain accented characters (diacritics)!")
	public String passwordStrength3();

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
