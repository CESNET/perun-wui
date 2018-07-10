package cz.metacentrum.perun.wui.setAffiliation.pages.affiliation;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * UI Handlers interface for setting affiliations
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public interface AffiliationUiHandlers extends UiHandlers {

	void loadUsers(String searchString);

	void loadUsersFromGroup(String searchString, int groupId);

	void loadUsersFromVo(String searchString, int voId);

	void loadAssignedAffiliations(Long uid);

	void storeAssignedAffiliations(Long uid, String organization, boolean member, boolean faculty, boolean affiliate);
}
