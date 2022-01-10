package cz.metacentrum.perun.wui.registrar.client.resources;

import cz.metacentrum.perun.wui.client.resources.PerunTranslation;

/**
 * Class providing translations to Registrar GUI.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunRegistrarTranslation extends PerunTranslation {

	// -------------- APP NAME ------------------------ //

	@DefaultMessage("Registrar")
	public String registrarAppName();

	@DefaultMessage("Please perform the confirmation below to continue")
	public String pleaseVerifyCaptcha();

	@DefaultMessage("Verification failed. Please perform the confirmation again.")
	String captchaFailed();

	// -------------- MAIN PAGE - MENU ------------------------ //

	@DefaultMessage("Form")
	public String application();

	@DefaultMessage("Submitted registrations")
	public String myApplications();

	@DefaultMessage("Help")
	public String help();

	// -------------- REGISTRATION FORM ------------------------ //

	// TITLES

	@DefaultMessage("We are sorry but we can do nothing for you now.")
	public String canDoNothing();

	@DefaultMessage("You have successfully applied for membership")
	public String initTitle();

	@DefaultMessage("You have successfully applied for extension")
	public String extendTitle();

	@DefaultMessage("You have been successfully registered")
	public String initTitleAutoApproval();

	@DefaultMessage("Email verification needed")
	public String emailVerificationNeededTitle();

	@DefaultMessage("You have successfully extend your membership")
	public String extendTitleAutoApproval();

	// MESSAGES

	@DefaultMessage("Please check your mailbox {0} and click the link to verify your email address. " +
			"Without verification it is not possible to approve your application.")
	public String verifyMail(String mail);

	@DefaultMessage("Please wait until your application has been approved. You will be notified by email.")
	public String waitForAcceptation();

	@DefaultMessage("<p><p>You can <b>see or edit your application <a href=\"{0}#appdetail;id={1}\">here</b></a>.")
	public String seeOrEditApplicationHere(String url, int id);

	@DefaultMessage("Please wait until your application for membership extension has been approved. You will be notified by email.")
	public String waitForExtAcceptation();

	@DefaultMessage("After acceptation you will become member of \"{0}\" automatically.")
	String waitForVoAcceptation(String groupName);

	@DefaultMessage("After acceptation you will become member of \"{0}\" automatically.")
	String waitForVoExtension(String groupName);

	@DefaultMessage("You are now a member of {0}.")
	public String registered(String voOrGroupName);

	@DefaultMessage("Your membership in {0} is valid now.")
	public String extended(String voOrGroupName);

	@DefaultMessage("It seems you want to submit application for membership in group {0}. However you have already applied.")
	public String groupFailedAlreadyApplied(String groupName);

	@DefaultMessage("It seems you want to submit application for membership in group {0}. However you are already registered.")
	public String groupFailedAlreadyRegistered(String groupName);

	@DefaultMessage("Please <b>avoid using accented characters</b>. It might not be supported by all backend components and services.")
	public String dontUseAccents();

	// OTHERS

	@DefaultMessage("Do you want to extend a membership?")
	public String offerMembershipExtensionTitle();

	@DefaultMessage("After a while your membership in <b>{0}</b> will expire. We recommend you to extend the membership right now.")
	public String offerMembershipExtensionMessage(String vo);

	@DefaultMessage("No thanks")
	public String offerMembershipExtensionNoThanks();

	@DefaultMessage("Extend")
	public String offerMembershipExtensionExtend();

	@DefaultMessage("<i>User don`t have identity suitable for automatic joining. If it`s you, <b>please contact support at: <a href=\"mailto:{0}\">{0}</a></b>.</i>")
	public String noIdentityForJoining(String mailAddress);

	@DefaultMessage("Please wait, redirecting you back to the service ...")
	public String redirectingBackToService();


	// -------------- REGISTRATION FORM WIDGETS ------------------------ //

	@DefaultMessage("Text is too long!")
	public String tooLong();

	@DefaultMessage("Incorrect input format!")
	public String incorrectFormat();

	@DefaultMessage("Item <b>can`t be empty!</b>")
	public String cantBeEmpty();

	@DefaultMessage("You must select at least one option!")
	public String cantBeEmptySelect();

	@DefaultMessage("Incorrect email address format!")
	public String incorrectEmail();

	@DefaultMessage("Check & submit the form")
	public String checkAndSubmit();

	@DefaultMessage("Password <b>can`t be empty!</b>")
	public String passEmpty();

	@DefaultMessage("Passwords doesn`t match!")
	public String passMismatch();

	@DefaultMessage("Confirm password!")
	public String secondPassEmpty();

	@DefaultMessage("Enter password")
	public String enterPassword();

	@DefaultMessage("Confirm password")
	public String confirmPassword();

	@DefaultMessage("Checking...")
	public String checkingLogin();

	@DefaultMessage("Login <b>is not available!</b> Please choose another.")
	public String loginNotAvailable();

	@DefaultMessage("Unable to check login availability! Please check your internet connection and try again later.")
	public String checkingLoginFailed();

	@DefaultMessage("Login contains invalid character(s) or is not allowed!")
	public String loginNotAllowed();

	@DefaultMessage("--- Custom ---")
	public String customValueEmail();

	@DefaultMessage("Email with verification link will be sent to provided email address.")
	public String mustValidateEmail();

	@DefaultMessage("Enter custom value...")
	public String enterCustomValue();

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

	@DefaultMessage("By login/password")
	public String byLoginPassword();

	@DefaultMessage("--- Not selected ---")
	public String notSelected();

	@DefaultMessage("--- Custom value ---")
	public String customValue();

	@DefaultMessage("<i>HIDDEN</i>")
	public String isHidden();

	@DefaultMessage("<i>value verified by Identity provider</i>")
	String federation();

	@DefaultMessage("UNDEFINED")
	public String undefinedFormItem();

	@DefaultMessage("none of the above")
	String clearRadiobox();

	@DefaultMessage("Type to search...")
	String typeToSearch();

	@DefaultMessage("Key \"<i>{0}</i>\" does not have the correct format.")
	String sshKeyFormat(String key);

	@DefaultMessage("Do not mix commas and new-lines as SSH keys separators.")
	String mixingNewlinesWithCommas();

	@DefaultMessage("Multiple consecutive commas are not allowed, use a single comma or newline as a separator between SSH keys.")
	String tooMuchCommas();

	@DefaultMessage("Multiple consecutive commas are not allowed, use a single comma as a separator between SSH keys.")
	String tooMuchCommasTextField();

	@DefaultMessage("Multiple consecutive newlines are not allowed, use one newline or comma as a separator between SSH keys.")
	String tooMuchNewlines();

	@DefaultMessage("Missing separator between the SSH keys (comma or newline).")
	String sshKeyMissingDelimiter();

	@DefaultMessage("Missing comma as a separator between the SSH keys.")
	String sshKeyMissingCommaDelimiterTextField();

	@DefaultMessage("No spaces are allowed around the SSH key separator (comma or newline).")
	String sshKeyNoSpaceAroundKeySeparator();

	@DefaultMessage("No spaces are allowed around the SSH key separator (comma).")
	String sshKeyNoSpaceAroundCommasTextField();

	// -------------- SUBMITTED APPS PAGE ------------------------ //

	@DefaultMessage("Submitted registrations")
	public String submittedTitle();

	@DefaultMessage("Submitted")
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

	@DefaultMessage("Please check inbox of <b>{0}</b> for mail verification message. If you didn`t receive any, check your SPAM folder or use button below to re-send.")
	public String mailVerificationText(String mailAddress);

	@DefaultMessage("Re-send mail verification message")
	public String reSendMailVerificationButton();

	@DefaultMessage("Mail verification message sent to <b>{0}</b>")
	public String mailVerificationRequestSent(String mailAddress);

	// -------------- APP DETAIL PAGE ------------------------ //

	@DefaultMessage("Registration detail")
	public String detailDefaultTitle();

	@DefaultMessage("Registration to {0}")
	public String initialDetail(String registerTo);

	@DefaultMessage("Membership extension in {0}")
	public String extensionDetail(String registerTo);

	@DefaultMessage("This registration was automatically submitted after approved registration to {0}.")
	public String embeddedInfo(String vo);

	// -------------- APP DETAIL PAGE - EDIT ------------------------ //

	@DefaultMessage("Edit")
	public String edit();

	@DefaultMessage("Save")
	public String save();

	@DefaultMessage("Cancel")
	public String cancel();

	@DefaultMessage("There are no changes to be saved!")
	public String noChange();

	@DefaultMessage("All changes to the form will be discarded. Do you wish to continue?")
	public String cancelAsk();

	// --------------- EXCEPTIONS -------------------------------- //

	@DefaultMessage("Continue")
	public String continueButton();

	@DefaultMessage("You are already registered in {0}")
	public String alreadyRegistered(String voOrGroupName);

	@DefaultMessage("You already have submitted registration to {0}")
	public String alreadySubmitted(String voOrGroupName);

	@DefaultMessage("You have already submitted extension application to {0}")
	public String alreadySubmittedExtension(String voName);

	@DefaultMessage("<p>You can <b>see or edit your application <a href=\"{0}#appdetail;id={1}\">here</b></a>. You can see all your applications in section <a href=\"{0}#submitted\">{2}</a>.")
	public String visitSubmitted(String url, int appId, String title);

	@DefaultMessage("You are already registered")
	public String cantExtendMembership();

	@DefaultMessage("<br/>Your membership in <i>{1}<i> is valid until <b>{0}</b>.")
	public String cantExtendMembershipOutside(String expirationDate, String name);

	@DefaultMessage("You don`t have required Level of Assurance (LoA) to extend membership. Contact your IDP ({0}) and prove your identity to him by official ID card (passport, driving license). If you have another identity with higher Level of Assurance, try to use it instead.")
	public String cantExtendMembershipInsufficientLoa(String idpName);

	@DefaultMessage("You can`t register to {0}")
	public String cantBecomeMember(String voOrGroupName);

	@DefaultMessage("<br/>The Identity Provider you used to log-in ({0}) doesn`t provide information about your Level of Assurance (LoA). Please ask your IDP to publish such information or use different IDP.")
	public String cantBecomeMemberLoa(String idpName);

	@DefaultMessage("<br/>You don`t have required Level of Assurance (LoA) to display registration form. Contact your IDP ({0}) and prove your identity to him by official ID card (passport, driving license). If you have another identity with higher Level of Assurance, try to use it instead.")
	public String cantBecomeMemberInsufficientLoa(String idpName);

	@DefaultMessage("<h4>You can`t submit the registration</h4><p><br/>The Identity Provider you used to log-in ({0}) doesn`t provide data required by the registration form. Please ask your IDP to publish following attributes or use different IDP.")
	public String missingRequiredData(String idpName);

	@DefaultMessage("Missing attribute: {0}")
	public String missingAttribute(String attributeName);

	@DefaultMessage("Organization / project with name <i>{0}</i> doesn`t exist. Please check the address used in a browser.")
	public String voNotExistsException(String voName);

	@DefaultMessage("Group with name <i>{0}</i> doesn`t exist. Please check the address used in a browser.")
	public String groupNotExistsException(String groupName);

	@DefaultMessage("Organization / project you wish to register into is missing in a browser`s address bar. Please specify it by: <i>?vo=[name]</i>.")
	public String missingVoInURL();

	@DefaultMessage("Registration form is not created. Please contact administrator.")
	public String formNotExist();

	@DefaultMessage("Registration form is wrongly formed. Probably you won`t be able to submit it. Please contact administrator.")
	public String formWrongFormed();

	@DefaultMessage("Application couldn`t been created. Probably your login can`t be reserved.")
	public String applicationNotCreatedBecauseLogin();

	@DefaultMessage("Application couldn`t been created.")
	public String applicationNotCreated();

	@DefaultMessage("Your application to {0} has been submitted but there were an unexpected error. Your administrator has been informed and he will deal with it.")
	public String registrarException(String voOrGroupName);

	@DefaultMessage("Application to {0} has been submitted, but not approved automatically, due to unexpected error. Your administrator has been informed and he will deal with it.")
	public String cantBeApproved(String voOrGroupName);

	@DefaultMessage("Unable to submit the form")
	public String unableToSubmit();

	@DefaultMessage("Can`t submit the form")
	public String cantSubmitLoA();

	@DefaultMessage("Based on information provided by <i>{0}</i> you are <b>not active academia member</b>. Please sing out and use your home institution (University you are active member) when signing in.")
	public String notAcademicLoA(String idp);

	@DefaultMessage("In order to access CESNET services you must log-in using verified academic identity (at least once a year). Please use such identity to access this form.")
	public String notEligibleCESNET();

	@DefaultMessage("<p>To renew your membership, <b>verification of academic status</b> is required for your e-Infrastructure account. <p><ul><li>I have a verified account</li><ul><li>The verification may have expired. In this case, please sign in to your account with a verified academic identity, which will extend the validity of the verification.</li></ul></ul><ul><li>I don`t have a verified account</li><ul><li>Please <a href=\"{0}\">add verified identity</a> to your account from some academic institution (involved in eduId).</li><li><a href=\"{1}\">Ask for manual verification</a> of your academic background (if your institution is not involved in the eduId federation).</li></ul>")
	public String notEligibleEINFRAextension(String consolidatorUrl, String einfraVerifyUrl);

	@DefaultMessage("<p>You are signed in with an account from Metacentrum. You <b>cannot register or renew your membership</b> using this identity.<p>Please <b>sign-out / close all browser windows</b> and sign-in again using an external identity provider - an account from your academic organization or a social identity provider if your institution doesn`t have one.")
	public String notEligibleMETAIDP();

	@DefaultMessage("<p>You are signed in with an e-INFRA CZ account. You <b>cannot register or renew your membership</b> using this identity.<p>Please <b>sign-out / close all browser windows</b> and sign-in again using an external identity provider - an account from your academic organization or a social identity provider if your institution doesn`t have one.")
	public String notEligibleEINFRAIDP();

	@DefaultMessage("<p>Your application still awaits for mail address verification. If you continue now, it is most probable, that service will redirect you back to the registration form.<p>Please check your mailbox for verification mail. Once your application is verified and approved, you will be able to access the service.")
	public String redirectWaitForVerification();

	@DefaultMessage("<p>Your application still awaits administrators approval. If you continue now, it is most probable, that service will redirect you back to the registration form.<p>Once your application is approved by administrator, you will be notified and able to access the service.")
	public String redirectWaitForApproval();

	@DefaultMessage("I understand")
	public String understand();

	@DefaultMessage("Continue anyway")
	public String continueAnyway();

	/* ------------ LOADER MESSAGES ---------------- */

	@DefaultMessage("Loading ...")
	public String loading();

	@DefaultMessage("Loading application")
	public String loadingApplication();

	@DefaultMessage("Loading applications")
	public String loadingApplications();

	/* ------------ MAIL VERIFICATION --------------- */

	@DefaultMessage("Email verification")
	public String emailVerification();

	@DefaultMessage("Your email address was verified.")
	public String emailWasVerified();

	@DefaultMessage("We couldn`t verify your email address.")
	public String emailWasNotVerified();

}
