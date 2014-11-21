package cz.metacentrum.perun.wui.client.resources;

import com.google.gwt.core.client.JsArrayInteger;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.beans.Facility;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.User;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.model.common.Roles;

import java.util.ArrayList;

/**
 * Session class used as store for authz and any other static data.
 * Exists in only instance, which you can get like: PerunSession.getInstance();
 *
 * @author Vaclav Mach <374430@mail.muni.cz>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunSession {

	// Only instance
	static private PerunSession INSTANCE;

	static public String LOCALE = "en";

	// User roles constants
	static public final String PERUN_ADMIN_PRINCIPAL_ROLE = "PERUNADMIN";
	static public final String GROUP_ADMIN_PRINCIPAL_ROLE = "GROUPADMIN";
	static public final String VO_ADMIN_PRINCIPAL_ROLE = "VOADMIN";
	static public final String FACILITY_ADMIN_PRINCIPAL_ROLE = "FACILITYADMIN";
	static public final String USER_ROLE = "SELF";
	static public final String VO_OBSERVER_PRINCIPAL_ROLE = "VOOBSERVER";

	// User's authz data
	private PerunPrincipal perunPrincipal = null; // contain all users auth returned from RPC

	//Keepers of user's roles
	private boolean perunAdmin = false;
	private boolean groupAdmin = false;
	private boolean voAdmin = false;
	private boolean facilityAdmin = false;
	private boolean voObserver = false; // is not vo admin
	private boolean self = false; // is not admin

	// Entities which can the user edit
	private ArrayList<Integer> editableGroups = new ArrayList<Integer>();
	private ArrayList<Integer> editableVos = new ArrayList<Integer>();
	private ArrayList<Integer> editableFacilities = new ArrayList<Integer>();
	private ArrayList<Integer> editableUsers = new ArrayList<Integer>();

	// entities which user can view (Observer role)
	private ArrayList<Integer> viewableVos = new ArrayList<Integer>();

	// Currently active entities - user is editing them now
	private Vo activeVo;
	private Group activeGroup;
	private Facility activeFacility;
	private User activeUser;
	private BasicOverlayObject configuration;

	// RPC URL
	private String rpcUrl = "";

	private boolean isExtendedInfoVisible = false;

	/**
	 * Creates new instance of the Session
	 */
	private PerunSession() {

		LOCALE = getLOCALE();

	}

	/**
	 * Get default language from browser
	 *
	 * @return language string
	 */
	public static final native String getLOCALE() /*-{

		var l_lang = "en";
		if (navigator.userLanguage) // Explorer
			l_lang = navigator.userLanguage.split("-")[0];
		else if (navigator.language) // FF
			l_lang = navigator.language.split("-")[0];
		else
			l_lang = "en";

		return l_lang;

		//$wnd.jQuery("meta[name='gwt:property']").attr('content', 'locale='+l_lang);

	}-*/;

	/**
	 * Returns the instance of PerunWebSession
	 */
	static public PerunSession getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PerunSession();
		}
		return INSTANCE;
	}

	/**
	 * Returns the URL of the RPC
	 *
	 * @return URL
	 */
	public String getRpcUrl() {

		if (!rpcUrl.isEmpty()) {
			return rpcUrl;
		}

		String rpcType = getRpcServer();
		if (rpcType == null) {
			rpcUrl = PerunWebConstants.INSTANCE.perunRpcUrl();
			return rpcUrl;
		}

		if (rpcType.equals("krb")) {
			rpcUrl = PerunWebConstants.INSTANCE.perunRpcUrlKrb();
		} else if (rpcType.equals("fed")) {
			rpcUrl = PerunWebConstants.INSTANCE.perunRpcUrlFed();
		} else if (rpcType.equals("cert")) {
			rpcUrl = PerunWebConstants.INSTANCE.perunRpcUrlCert();
		} else if (rpcType.equals("forceAuthn-fed")) {
			rpcUrl = PerunWebConstants.INSTANCE.perunRpcUrlForceAuthFed();
		} else if (rpcType.equals("einfra")) {
			rpcUrl = PerunWebConstants.INSTANCE.perunRpcUrlKrbEinfra();
		} else {
			rpcUrl = PerunWebConstants.INSTANCE.perunRpcUrl();
		}
		return rpcUrl;
	}

	/**
	 * Returns RPC type: default, krb, cert, fed
	 * RPC type should be included in PerunWeb*.html
	 *
	 * @return RPC type
	 */
	public native String getRpcServer() /*-{
        return $wnd.RPC_SERVER;
    }-*/;

	/**
	 * Returns current PerunPrincipal of user logged to RPC
	 *
	 * @return PerunPrincipal
	 */
	public PerunPrincipal getPerunPrincipal() {
		return this.perunPrincipal;
	}

	/**
	 * Sets PerunPrincipal for this session
	 *
	 * @param pp PerunPrincipal received from RPC login
	 */
	public void setPerunPrincipal(PerunPrincipal pp) {
		this.perunPrincipal = pp;
	}

	/**
	 * Returns User object from PerunPrincipal
	 * of person currently logged to RPC.
	 *
	 * @return User which is logged to RPC
	 */
	public User getUser() {
		return perunPrincipal.getUser();
	}

	/**
	 * Safe return User ID from PerunPrincipal.
	 * If user == null then -1 is returned
	 *
	 * @return User ID or -1 if user == null
	 */
	public int getUserId() {
		return perunPrincipal.getUserId();
	}

	/**
	 * True if the user is perun admin
	 *
	 * @return true if perun admin
	 */
	public boolean isPerunAdmin() {
		return this.perunAdmin;
	}

	/**
	 * True if the user is VO admin.
	 * TRUE for PerunAdmin too.
	 *
	 * @return true if VO admin
	 */
	public boolean isVoAdmin() {

		if (this.perunAdmin) {
			return this.perunAdmin;
		}
		return this.voAdmin;
	}

	/**
	 * True if the user is vo admin of a specified VO.
	 * TRUE for PerunAdmin too.
	 *
	 * @param id ID of VO to check admin status for
	 * @return true if user is VO's admin
	 */
	public boolean isVoAdmin(int id) {
		if (this.perunAdmin) {
			return this.perunAdmin;
		} else if (this.voAdmin) {
			return editableVos.contains(id);
		}
		return false;
	}

	/**
	 * True if the user is VO observer.
	 * TRUE for PerunAdmin too.
	 *
	 * @return true if VO observer
	 */
	public boolean isVoObserver() {

		if (this.perunAdmin) {
			return this.perunAdmin;
		}
		return this.voObserver;
	}

	/**
	 * True if the user is vo observer of a specified VO.
	 * TRUE for PerunAdmin too.
	 *
	 * @param id ID of VO to check observer status for
	 * @return true if user is VO's observer
	 */
	public boolean isVoObserver(int id) {
		if (this.perunAdmin) {
			return this.perunAdmin;
		} else if (this.voObserver) {
			return viewableVos.contains(id);
		}
		return false;
	}

	/**
	 * True if the user is group admin.
	 * TRUE for PerunAdmin too.
	 *
	 * @return true if group admin
	 */
	public boolean isGroupAdmin() {
		if (this.perunAdmin) {
			return this.perunAdmin;
		}
		return this.groupAdmin;
	}

	/**
	 * True if the user is group admin of a specified group.
	 * TRUE for PerunAdmin too.
	 *
	 * @param id ID of group to check admin status for
	 * @return true if user is Group's admin
	 */
	public boolean isGroupAdmin(int id) {
		if (this.perunAdmin) {
			return this.perunAdmin;
		} else if (this.groupAdmin) {
			return editableGroups.contains(id);
		}
		return false;
	}

	/**
	 * True if the user is facility admin.
	 * TRUE for PerunAdmin too.
	 *
	 * @return true if facility admin
	 */
	public boolean isFacilityAdmin() {
		if (this.perunAdmin) {
			return this.perunAdmin;
		}
		return this.facilityAdmin;
	}

	/**
	 * True if the user is facility admin of a specified Facility.
	 * TRUE for PerunAdmin too.
	 *
	 * @param id ID of Facility to check admin status for
	 * @return true if user is Facility's admin
	 */
	public boolean isFacilityAdmin(int id) {
		if (this.perunAdmin) {
			return this.perunAdmin;
		} else if (this.facilityAdmin) {
			return editableFacilities.contains(id);
		}
		return false;
	}

	/**
	 * True if the user is also user.
	 * TRUE for PerunAdmin too.
	 *
	 * @return true if also self
	 */
	public boolean isSelf() {
		if (this.perunAdmin) {
			return this.perunAdmin;
		}
		return this.self;
	}

	/**
	 * True if the passed userID is same
	 * as userId stored in SELF role.
	 * TRUE for PerunAdmin too.
	 *
	 * @return true if self / false otherwise
	 */
	public boolean isSelf(int id) {
		if (this.perunAdmin) {
			return this.perunAdmin;
		} else if (this.self) {
			return editableUsers.contains(id);
		}
		return false;
	}

	/**
	 * Add a VO, which user can edit
	 *
	 * @param voId VO, which can user edit
	 */
	public void addEditableVo(int voId) {
		if (!this.editableVos.contains(voId)) this.editableVos.add(voId);
	}

	/**
	 * Add a VO, which user can view (for VO observer role)
	 *
	 * @param voId VO, which can user view
	 */
	public void addViewableVo(int voId) {
		if (!this.viewableVos.contains(voId)) this.viewableVos.add(voId);
	}

	/**
	 * Add a group, which user can edit
	 *
	 * @param groupId group which can user edit
	 */
	public void addEditableGroup(int groupId) {
		if (!this.editableGroups.contains(groupId)) this.editableGroups.add(groupId);
	}

	/**
	 * Add a Facility, which user can edit
	 *
	 * @param facilityId Facility, which can user edit
	 */
	public void addEditableFacility(int facilityId) {
		if (!this.editableFacilities.contains(facilityId)) this.editableFacilities.add(facilityId);
	}

	/**
	 * Add a User, which user can edit
	 *
	 * @param userId User, which can user edit
	 */
	public void addEditableUser(int userId) {
		if (!this.editableUsers.contains(userId)) this.editableUsers.add(userId);
	}

	/**
	 * Return list of editable groups IDs
	 *
	 * @return groups
	 */
	public ArrayList<Integer> getEditableGroups() {
		return editableGroups;
	}


	/**
	 * Return list of editable vos IDs
	 *
	 * @return vos
	 */
	public ArrayList<Integer> getEditableVos() {
		return editableVos;
	}

	/**
	 * Return list of viewable vos IDs
	 *
	 * @return vos
	 */
	public ArrayList<Integer> getViewableVos() {
		return viewableVos;
	}


	/**
	 * Return list of editable facilities IDs
	 *
	 * @return facilities
	 */
	public ArrayList<Integer> getEditableFacilities() {
		return editableFacilities;
	}

	/**
	 * Return list of editable users IDs
	 *
	 * @return users
	 */
	public ArrayList<Integer> getEditableUsers() {
		return editableUsers;
	}

	/**
	 * Returns VO, which user currently edits
	 *
	 * @return VO
	 */
	public Vo getActiveVo() {
		return activeVo;
	}

	/**
	 * Sets currently active VO (refresh links in menu)
	 *
	 * @param vo VO which user is editing now
	 */
	public void setActiveVo(Vo vo) {
		this.activeVo = vo;
		//PerunWui.getMenu().buildMenu();
	}

	/**
	 * Returns Group, which user currently edits
	 *
	 * @return Group
	 */
	public Group getActiveGroup() {
		return activeGroup;
	}

	/**
	 * Sets currently active Group (refresh links in menu)
	 *
	 * @param group Group which user is editing now
	 */
	public void setActiveGroup(Group group) {
		this.activeGroup = group;
		//PerunWui.getMenu().buildMenu();
	}

	/**
	 * Returns Facility, which user currently edits
	 *
	 * @return Facility
	 */
	public Facility getActiveFacility() {
		return activeFacility;
	}

	/**
	 * Sets currently active Facility (refresh links in menu)
	 *
	 * @param facility Facility which user is editing now
	 */
	public void setActiveFacility(Facility facility) {
		this.activeFacility = facility;
		//PerunWui.getMenu().buildMenu();
	}

	/**
	 * Returns User, which user currently edits
	 *
	 * @return User
	 */
	public User getActiveUser() {
		if (activeUser == null) {
			return getUser();
		}
		return activeUser;
	}

	/**
	 * Sets currently active User (SELF role) (refresh links in menu)
	 *
	 * @param user User which user is editing now
	 */
	public void setActiveUser(User user) {
		this.activeUser = user;
		//PerunWui.getMenu().buildMenu();
	}

	/**
	 * Sets user's roles and editable entities received from RPC
	 * within PerunPrincipal into Session
	 * <p/>
	 * Call only once when loading GUI!
	 *
	 * @param roles roles returned from PerunPrincipal
	 */
	public void setRoles(Roles roles) {

		this.perunAdmin = roles.hasRole(PERUN_ADMIN_PRINCIPAL_ROLE);
		this.voAdmin = roles.hasRole(VO_ADMIN_PRINCIPAL_ROLE);
		this.facilityAdmin = roles.hasRole(FACILITY_ADMIN_PRINCIPAL_ROLE);
		this.groupAdmin = roles.hasRole(GROUP_ADMIN_PRINCIPAL_ROLE);
		this.self = roles.hasRole(USER_ROLE);
		this.voObserver = roles.hasRole(VO_OBSERVER_PRINCIPAL_ROLE);

		JsArrayInteger array = roles.getEditableEntities("VOADMIN", "Vo");
		for (int i = 0; i < array.length(); i++) {
			addEditableVo(array.get(i));
		}
		JsArrayInteger array2 = roles.getEditableEntities("SELF", "User");
		for (int i = 0; i < array2.length(); i++) {
			addEditableUser(array2.get(i));
		}
		JsArrayInteger array3 = roles.getEditableEntities("FACILITYADMIN", "Facility");
		for (int i = 0; i < array3.length(); i++) {
			addEditableFacility(array3.get(i));
		}
		JsArrayInteger array4 = roles.getEditableEntities("GROUPADMIN", "Group");
		for (int i = 0; i < array4.length(); i++) {
			addEditableGroup(array4.get(i));
		}
		JsArrayInteger array5 = roles.getEditableEntities("VOOBSERVER", "Vo");
		for (int i = 0; i < array5.length(); i++) {
			addViewableVo(array5.get(i));
		}

	}

	/**
	 * Return string with authz information
	 * <p/>
	 * eg. voadmin=21,41,35;groupadmin=23,45,78 etc.
	 *
	 * @return string with authz info
	 */
	public String getRolesString() {

		String result = "";

		if (perunAdmin) {
			result += "PerunAdmin; ";
		}
		if (self) {
			result += "Self=" + editableUsers;
		}
		if (voAdmin) {
			result += "; VoManager=" + editableVos;
		}
		if (voObserver) {
			result += "; VoObserver=" + viewableVos;
		}
		if (groupAdmin) {
			result += "; GroupManager=" + editableGroups;
		}
		if (facilityAdmin) {
			result += "; FacilityManager=" + editableFacilities;
		}

		return result;

	}

	public void setExtendedInfoVisible(boolean visible) {
		this.isExtendedInfoVisible = visible;
	}

	public boolean isExtendedInfoVisible() {
		return this.isExtendedInfoVisible;
	}

	public BasicOverlayObject getConfiguration() {
		return configuration;
	}

	public void setConfiguration(BasicOverlayObject configuration) {
		this.configuration = configuration;
	}

}