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

	@DefaultMessage("You have been signed out.")
	public String logoutPageTitle();

	@DefaultMessage("Please close the browser window.")
	public String logoutPageSubTitle();

	@DefaultMessage("Sign me back")
	public String logoutPageButton();

	@DefaultMessage("Form has no form items.")
	public String formHasNoFormItems();

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

	/* ------------ LANGUAGE SWITCHING ---------------- */

	@DefaultMessage("Language")
	public String language();

	@DefaultMessage("English")
	public String english();

	/* ------------ FOOTER ---------------------------- */

	@DefaultMessage("<strong>Support at <a href=\"mailto:{0}\">{0}</a></strong>")
	public String supportAt(String email);

	@DefaultMessage("Powered by <a href=\'https://perun.cesnet.cz/web/\'>Perun</a> &copy; {0} <a href=\'https://www.cesnet.cz/\'>CESNET</a> &amp; <a href=\'https://www.cerit-sc.cz/\'>CERIT-SC</a>")
	public String credits(int year);

	@DefaultMessage("Version: {0}")
	public String version(String version);


}