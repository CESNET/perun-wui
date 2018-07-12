package cz.metacentrum.perun.wui.setAffiliation.client.resources;

import cz.metacentrum.perun.wui.client.resources.PerunTranslation;

/**
 * Perun Set Affiliation translations
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public interface PerunSetAffiliationTranslation extends PerunTranslation {

	@DefaultMessage("Set affiliations for user")
    String setAffiliationTitle();

	@DefaultMessage("Find users")
	String userSearchbarLabel();

	@DefaultMessage("Enter the name or email address of user...")
	String userSearchbarPlaceholder();

	@DefaultMessage("Search")
	String searchUserBtn();

	@DefaultMessage("Assign user affiliations")
	String affiliationTitleLabel();

	@DefaultMessage("User has a formal affiliation with organisation")
	String affiliationMember();

	@DefaultMessage("User is a researcher or teacher")
	String affiliationFaculty();

	@DefaultMessage("User has an informal affiliation with organisation")
	String affiliationAffiliate();

	@DefaultMessage("Organization")
	String organizationLabel();

	@DefaultMessage("Confirm")
	String submit();

	@DefaultMessage("Select user")
    String userSelectLabel();

	@DefaultMessage("Changes were successfully saved")
	String success();

	@DefaultMessage("Request is being processed, please wait")
	String loadingData();

	@DefaultMessage("Select user from list")
	String userSelectPlaceholder();

	@DefaultMessage("You are not authorized to assign affiliations")
	String unauthorizedMessage();

	@DefaultMessage("No user has been found")
	String noUsersFound();

	@DefaultMessage("Select VO or group to search in")
	String voGroupSelectLabel();
}
