package cz.metacentrum.perun.wui.registrar.client;

import cz.metacentrum.perun.wui.client.resources.PerunTranslation;

/**
 * Class providing translations to Registrar GUI.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface RegistrarTranslation extends PerunTranslation {

	// -------------- MAIN PAGE - MENU------------------------ //

	@DefaultMessage("Form")
	public String application();

	@DefaultMessage("Submitted registrations")
	public String myApplications();

	@DefaultMessage("Help")
	public String help();

	@DefaultMessage("Logout")
	public String logout();

	@DefaultMessage("Language")
	public String language();

	@DefaultMessage("English")
	public String english();

	// -------------- REGISTRATION FORM WIDGETS ------------------------ //

	@DefaultMessage("Text is too long!")
	public String tooLong();

	@DefaultMessage("Incorrect input format!")
	public String incorrectFormat();

	@DefaultMessage("Value can`t be empty!")
	public String cantBeEmpty();

	@DefaultMessage("You must select at least one option!")
	public String cantBeEmptySelect();

	@DefaultMessage("Incorrect email address format!")
	public String incorrectEmail();

	@DefaultMessage("Check & submit the form")
	public String checkAndSubmit();

	@DefaultMessage("Password can`t be empty!")
	public String passEmpty();

	@DefaultMessage("Passwords doesn`t match!")
	public String passMismatch();

	@DefaultMessage("Checking...")
	public String checkingLogin();

	@DefaultMessage("Login not available!")
	public String loginNotAvailable();

	@DefaultMessage("Unable to check login availability!")
	public String checkingLoginFailed();

	@DefaultMessage("--- Custom ---")
	public String customValueEmail();

	@DefaultMessage("Email with verification link will be sent to provided email address.")
	public String mustValidateEmail();

	@DefaultMessage("Similar user(s) are already registered in system. If it`s you, prove your identity by logging-in using one of the registered identities.")
	public String similarUsersFound();

	@DefaultMessage("Similar user found")
	public String similarUserFoundTitle();

	@DefaultMessage("Similar users found")
	public String similarUsersFoundTitle();

	@DefaultMessage("It`s not me")
	public String noThanks();

	@DefaultMessage("By certificate")
	public String byCertificate();

	@DefaultMessage("By identity provider")
	public String byIdp();

	@DefaultMessage("--- Not selected ---")
	public String notSelected();

	@DefaultMessage("--- Custom value ---")
	public String customValue();

	@DefaultMessage("<i>( HIDDEN )</i>")
	public String isHidden();

	// -------------- SUBMITTED APPS PAGE ------------------------ //

	@DefaultMessage("Submitted registrations")
	public String submittedTitle();

	@DefaultMessage("Submitted on")
	public String submittedOn();

	@DefaultMessage("State")
	public String state();

	@DefaultMessage("Type")
	public String type();

	@DefaultMessage("Virtual organization")
	public String virtualOrganization();

	@DefaultMessage("Group")
	public String group();

	@DefaultMessage("Show")
	public String showDetail();


	// -------------- APP DETAIL PAGE ------------------------ //

	@DefaultMessage("Registration to {0}")
	public String initialDetail(String registerTo);

	@DefaultMessage("Membership extension in {0}")
	public String extensionDetail(String registerTo);

	@DefaultMessage("Form content")
	public String formDataTitle();

	// --------------- EXCEPTIONS -------------------------------- //

	@DefaultMessage("Continue")
	public String continueButton();

	@DefaultMessage("You are already registered in {0}")
	public String alreadyRegistered(String voOrGroupName);

	@DefaultMessage("You already have submitted registration to {0}")
	public String alreadySubmitted(String voOrGroupName);

	@DefaultMessage("You can check state of your application in <a href=\"{0}#submitted\">{1}</a>.")
	public String visitSubmitted(String url, String title);

	@DefaultMessage("You can`t extend membership right now")
	public String cantExtendMembership();

	@DefaultMessage("<b>Your membership is valid until {0}.</b> Membership can be extended only in a short time before membership expiration or after. ")
	public String cantExtendMembershipOutside(String expirationDate);

	@DefaultMessage("You don`t have required Level of Assurance (LoA) to extend membership. Contact your IDP ({0}) and prove your identity to him by official ID card (passport, driving license). If you have another identity with higher Level of Assurance, try to use it instead.")
	public String cantExtendMembershipInsufficientLoa(String idpName);

	@DefaultMessage("You can`t register to {0}")
	public String cantBecomeMember(String voOrGroupName);

	@DefaultMessage("The Identity Provider you used to log-in ({0}) doesn`t provide information about your Level of Assurance (LoA). Please ask your IDP to publish such information or use different IDP.")
	public String cantBecomeMemberLoa(String idpName);

	@DefaultMessage("You don`t have required Level of Assurance (LoA) to display registration form. Contact your IDP ({0}) and prove your identity to him by official ID card (passport, driving license). If you have another identity with higher Level of Assurance, try to use it instead.")
	public String cantBecomeMemberInsufficientLoa(String idpName);

	@DefaultMessage("<h4>You can`t submit the registration</h4><p>The Identity Provider you used to log-in ({0}) doesn`t provide data required by the registration form. Please ask your IDP to publish following attributes or use different IDP.")
	public String missingRequiredData(String idpName);

	@DefaultMessage("Missing attribute: {0}")
	public String missingAttribute(String attributeName);

	@DefaultMessage("Organization / project with name <i>{0}</i> doesn`t exist. Please check the address used in a browser.")
	public String voNotExistsException(String voName);

	@DefaultMessage("Group with name <i>{0}</i> doesn`t exist. Please check the address used in a browser.")
	public String groupNotExistsException(String groupName);

	@DefaultMessage("Organization / project you wish to register into is missing in a browser`s address bar. Please specify it by: <i>?vo=[name]</i>.")
	public String missingVoInURL();

}