package cz.metacentrum.perun.wui.client.resources;

import com.google.gwt.i18n.client.Messages;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

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

	@DefaultMessage("You have been logged out.")
	public String logoutPageTitle();

	@DefaultMessage("Please close the browser window.")
	public String logoutPageSubTitle();

	@DefaultMessage("Log me back")
	public String logoutPageButton();

	@DefaultMessage("Form has no form items.")
	public String formHasNoFormItems();

	@DefaultMessage("An unexpected error occurred. Please help us improve our service and report the bug.")
	public String errorOccured();

	/* ========= OBJECT TRANSLATION ============ */

	@DefaultMessage("Approved")
	public String applicationApproved();

	@DefaultMessage("New")
	public String applicationNew();

	@DefaultMessage("Verified")
	public String applicationVerified();

	@DefaultMessage("Rejected")
	public String applicationRejected();

	@DefaultMessage("Registration")
	public String applicationInitial();

	@DefaultMessage("Extension")
	public String applicationExtension();



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

	@DefaultMessage("Report error")
	public String reportError();

	@DefaultMessage("Try again")
	public String tryAgain();

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

	@DefaultMessage("Send")
	public String send();

	@DefaultMessage("Close")
	public String close();

	@DefaultMessage("Continue")
	public String continue_();  //continue is a keyword

	@DefaultMessage("Logout")
	public String logout();



	@DefaultMessage("Subject")
	public String subject();

	@DefaultMessage("Message")
	public String message();

	/* ------------ LANGUAGE SWITCHING ---------------- */

	@DefaultMessage("Language")
	public String language();

	@DefaultMessage("English")
	public String english();

}