package cz.metacentrum.perun.wui.client.resources;

import com.google.gwt.i18n.client.Messages;

/**
 * Global UI translations
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunTranslation extends Messages {

	@DefaultMessage("Not found")
	public String notFoundPageTitle();

	@DefaultMessage("Requested page was not found. Please check your URL and try again.")
	public String notFoundPageText();

	@DefaultMessage("Not authorized")
	public String notAuthorizedPageTitle();

	@DefaultMessage("You are not authorized to view requested content. Please check your URL.")
	public String notAuthorizedPageText();

	@DefaultMessage("You have been signed out.")
	public String logoutPageTitle();

	@DefaultMessage("Please close the browser window.")
	public String logoutPageSubTitle();

	@DefaultMessage("Sign me back")
	public String logoutPageButton();

	@DefaultMessage("Form has no form items.")
	public String formHasNoFormItems();

	@DefaultMessage("You are not registered user")
	public String notUserPageTitle();

	@DefaultMessage("In order to use this application you need to be registered user, but you were not recognized by identity you used to sign-in: {0} at {1}.")
	public String notUserText(String login, String from);

	@DefaultMessage("In order to use this application you need to be registered user, but you used anonymous access without any secret token to prove your identity.")
	public String notUserTextNon();

	/* ========= OBJECT TRANSLATION ============ */

	@DefaultMessage("Approved")
	public String applicationApproved();

	@DefaultMessage("Waiting for verification")
	public String applicationNew();

	@DefaultMessage("Waiting for approval")
	public String applicationVerified();

	@DefaultMessage("Rejected")
	public String applicationRejected();

	@DefaultMessage("Registration")
	public String applicationInitial();

	@DefaultMessage("Extension")
	public String applicationExtension();

	@DefaultMessage("Embedded")
	public String applicationEmbedded();



	/* ------------ LOADER MESSAGES ---------------- */

	@DefaultMessage("Loading user")
	public String loadingUser();

	@DefaultMessage("Preparing interface")
	public String preparingInterface();

	@DefaultMessage("Preparing form")
	public String preparingForm();



	/* ------------ BUTTONS ---------------- */

	@DefaultMessage("Add")
	public String add();

	@DefaultMessage("Remove")
	public String remove();

	@DefaultMessage("Create")
	public String create();

	@DefaultMessage("Delete")
	public String delete();

	@DefaultMessage("OK")
	public String ok();

	@DefaultMessage("Cancel")
	public String cancel();

	@DefaultMessage("Yes")
	public String yes();

	@DefaultMessage("No")
	public String no();

	@DefaultMessage("Filter")
	public String filter();

	@DefaultMessage("Search")
	public String search();

	@DefaultMessage("Save")
	public String save();

	@DefaultMessage("Edit")
	public String edit();

	@DefaultMessage("Assign")
	public String assign();

	@DefaultMessage("Refresh")
	public String refresh();

	@DefaultMessage("Try again")
	public String retry();

	@DefaultMessage("Report error")
	public String reportError();

	@DefaultMessage("Copy")
	public String copy();

	@DefaultMessage("Back")
	public String back();

	@DefaultMessage("Next")
	public String next();

	@DefaultMessage("Exit")
	public String exit();

	@DefaultMessage("Approve")
	public String approve();

	@DefaultMessage("Reject")
	public String reject();

	@DefaultMessage("Verify")
	public String verify();

	@DefaultMessage("Continue")
	public String continue_();  //continue is a keyword

	@DefaultMessage("Sign out")
	public String logout();

	@DefaultMessage("Close")
	public String close();

	/* ------------ LANGUAGE SWITCHING ---------------- */

	@DefaultMessage("Language")
	public String language();

	@DefaultMessage("English")
	public String english();

	/* ------------ FOOTER ---------------------------- */

	@DefaultMessage("<i class=\"fa fa-support\">&nbsp;</i>Support: <a href=\"mailto:{0}\">{0}</a>")
	public String supportAt(String email);

	@DefaultMessage("<i class=\"fa fa-support\">&nbsp;</i>Support: &nbsp;")
	public String supportAtMails();

	@DefaultMessage("Powered by <a href=\'https://perun.cesnet.cz/web/\'>Perun</a> &copy; {0} <a href=\'https://www.cesnet.cz/\'>CESNET</a> &amp; <a href=\'https://www.cerit-sc.cz/\'>CERIT-SC</a>, License: <a href=\'https://github.com/CESNET/perun/blob/master/LICENSE\'>BSD-2</a>")
	public String credits(int year);

	@DefaultMessage("Version: {0}")
	public String version(String version);


	/* ------------ ERROR REPORT ---------------------------- */

	@DefaultMessage("Report error")
	public String reportErrorHeading();

	@DefaultMessage("From")
	public String reportErrorFromLabel();

	@DefaultMessage("Please insert your email address.")
	public String reportErrorFromPlaceholder();

	@DefaultMessage("Subject")
	public String reportErrorSubjectLabel();

	@DefaultMessage("Message")
	public String reportErrorMessageLabel();

	@DefaultMessage("You can attach custom message describing what you tried to do.")
	public String reportErrorMessagePlaceholder();

	@DefaultMessage("Error report was successfully sent with ID: <b>{0}</b>.")
	public String reportErrorSuccessNoMail(int ticketNumber);

	@DefaultMessage("Error report was successfully sent with ID: <b>{0}</b>. You should receive a notification on your mail: <b>{1}</b>")
	public String reportErrorSuccess(int ticketNumber, String reporterMail);

	@DefaultMessage("Automatic error reporting is not working at the moment. Please send following message to <b>{0}</b> by mail. Thank you.")
	public String reportErrorFail(String supportMail);


	/* -----------   TABLE HEADER ------------------------*/
	@DefaultMessage("Title")
	String title();

	@DefaultMessage("Authors")
	String authors();

	@DefaultMessage("Year")
	String year();

	@DefaultMessage("Thanks")
	String thanks();

	@DefaultMessage("Cite")
	String cite();

	@DefaultMessage("Show")
	String show();

	@DefaultMessage("Description")
	String description();

	@DefaultMessage("Name")
	String name();

	@DefaultMessage("Value")
	String value();

	@DefaultMessage("Facility name")
	String facilityName();

	@DefaultMessage("Federated identity")
	String federatedIdp();

	@DefaultMessage("Login")
	String federatedLogin();

	@DefaultMessage("Email")
	String email();

	@DefaultMessage("Login")
	String login();

	@DefaultMessage("Virtual organization")
	String vo();

	@DefaultMessage("Loading")
	String loading();

	/* ---------- EINFRA LOGIN/PASSWORD ----------- */

	@DefaultMessage("Login must<ul><li>start with a lower-case letter<li>be 2-15 characters long<li>consist only of<ul><li>lower-case non-accented letters<li>digits<li>hyphens and underscores</ul></ul>")
	public String einfraLoginHelp();

	@DefaultMessage("Login must<ul><li>start with a lower-case letter<li>be 2-15 characters long<li>consist only of<ul><li>lower-case non-accented letters<li>digits<li>hyphens and underscores</ul></ul>")
	public String adminMetaLoginHelp();

	@DefaultMessage("Login must <b>be 2-15 characters long!</b>")
	public String einfraLoginLength();

	@DefaultMessage("Login must <b>be 2-15 characters long!</b>")
	public String adminMetaLoginLength();

	@DefaultMessage("Login must <b>start with lower-case letter!</b>")
	public String einfraLoginStart();

	@DefaultMessage("Login must <b>start with lower-case letter!</b>")
	public String adminMetaLoginStart();

	@DefaultMessage("Login can contain only<ul><li>lower-cased non-accented letters<li>digits<li>hyphens and underscores</ul>")
	public String einfraLoginFormat();

	@DefaultMessage("Login can contain only<ul><li>lower-cased non-accented letters<li>digits<li>hyphens and underscores</ul>")
	public String adminMetaLoginFormat();

	@DefaultMessage("Password must <ul><li>contain only printing (non-accented) characters<li>be at least 10 characters long<li>consist of at least 3 of 4 character groups<ul><li>lower-case letters<li>upper-case letters<li>digits<li>special characters</ul></ul>")
	public String einfraPasswordHelp();

	@DefaultMessage("Password must <ul><li>contain only printing (non-accented) characters<li>be at least 14 characters long<li>consist of at least 3 of 4 character groups<ul><li>lower-case letters<li>upper-case letters<li>digits<li>special characters</ul></ul>")
	public String muAdmPasswordHelp();

	@DefaultMessage("Password must <ul><li>contain only printing (non-accented) characters<li>be at least 10 characters long<li>consist of at least 3 of 4 character groups<ul><li>lower-case letters<li>upper-case letters<li>digits<li>special characters</ul></ul>")
	public String adminMetaPasswordHelp();

	@DefaultMessage("Password must be <b>at least 10 characters</b> long!")
	public String einfraPasswordLength();

	@DefaultMessage("Password must be <b>at least 14 characters</b> long!")
	public String muAdmPasswordLength();

	@DefaultMessage("Password must be <b>at least 10 characters</b> long!")
	public String adminMetaPasswordLength();

	@DefaultMessage("Password <b>can`t contain accented characters (diacritics)</b> or non-printing and control characters!")
	public String einfraPasswordFormat();

	@DefaultMessage("Password <b>can`t contain accented characters (diacritics)</b> or non-printing and control characters!")
	public String muAdmPasswordFormat();

	@DefaultMessage("Password <b>can`t contain accented characters (diacritics)</b> or non-printing and control characters!")
	public String adminMetaPasswordFormat();

	@DefaultMessage("Password must consist of <b>at least 3 of 4</b> character groups<ul><li>lower-case letters</li><li>upper-case letters</li><li>digits</li><li>special characters</li></ul>")
	public String einfraPasswordStrength();

	@DefaultMessage("Password must consist of <b>at least 3 of 4</b> character groups<ul><li>lower-case letters</li><li>upper-case letters</li><li>digits</li><li>special characters</li></ul>")
	public String muAdmPasswordStrength();

	@DefaultMessage("Password must consist of <b>at least 3 of 4</b> character groups<ul><li>lower-case letters</li><li>upper-case letters</li><li>digits</li><li>special characters</li></ul>")
	public String adminMetaPasswordStrength();

	@DefaultMessage("Password <b>can`t contain login, name or surname</b>, not even backwards!")
	public String einfraPasswordStrengthForNameLogin();

	@DefaultMessage("Password <b>can`t contain login</b>, not even backwards!")
	public String muAdmPasswordStrengthForNameLogin();

	@DefaultMessage("Password <b>can`t contain login, name or surname</b>, not even backwards!")
	public String adminMetaPasswordStrengthForNameLogin();

	@DefaultMessage("New password can`t be empty!")
	public String passwordCantBeEmpty();

	@DefaultMessage("Passwords dont`t match!")
	public String passwordMismatch();

	@DefaultMessage("Confirm your new password!")
	public String secondPasswordIsEmpty();

	@DefaultMessage("Old password:")
	public String oldPassLabel();
	@DefaultMessage("Enter your old password")
	public String enterOldPassPlaceholder();

	@DefaultMessage("New password:")
	public String newPassLabel();

	@DefaultMessage("Enter your new password")
	public String enterNewPassPlaceholder();

	@DefaultMessage("Confirm your new password")
	public String repeatNewPassPlaceholder();

	@DefaultMessage("Change password")
	public String changePasswordButton();

	@DefaultMessage("<b>Your password for login <i>{0}</i> has been changed!</b><br/>It might take a few minutes to reflect this change across our infrastructure.")
	public String passwordHasBeenChanged(String login);

	@DefaultMessage("Incorrect password!")
	public String oldPasswordIncorrect();

	@DefaultMessage("Password must <ul><li>be at least 12 characters long<li>consist of at least 3 of 4 character groups<ul><li>lower-case letters<li>upper-case letters<li>digits<li>special characters</ul></ul>")
	public String muPasswordHelp();

	@DefaultMessage("Password must be <b>at least 12 characters</b> long!")
	public String muPasswordLength();

	@DefaultMessage("Password must consist of <b>at least 3 of 4</b> character groups<ul><li>lower-case letters</li><li>upper-case letters</li><li>digits</li><li>special characters</li></ul>")
	public String muPasswordStrength();

	@DefaultMessage("Checking password strength...")
	public String checkingPasswordStrength();

	@DefaultMessage("Weak password!")
	public String weakPassword();

	@DefaultMessage("Unable to check password strength! Please check your internet connection and try again.")
	public String cantCheckPasswordStrength();

}
