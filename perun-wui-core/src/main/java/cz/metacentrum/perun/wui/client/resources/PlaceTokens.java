package cz.metacentrum.perun.wui.client.resources;

/**
 * Reserved token for places (pages -> Presenters). Every app define own mapping
 * of existing token to Presenter in it's own #configure() method (inside GWTP/GIN module class).
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PlaceTokens {

	// General Pages
	public static final String HOME = "home";
	public static final String NOT_FOUND = "notfound"; // also error page
	public static final String UNAUTHORIZED = "unauthorized";
	public static final String LOGOUT = "logout";
	public static final String HELP = "help";

	// PerunWui pages
	public static final String PERUN_VOS = "perun-vos";
	public static final String PERUN_FACILITIES = "perun-facs";
	public static final String VOS = "vos";
	public static final String VOS_DETAIL = "vos-detail";
	public static final String VOS_SELECT = "vos-select";
	public static final String FACILITY_DETAIL = "facs-detail";

	public static final String FACILITIES = "facs";
	public static final String USERS = "usrs";
	public static final String ATTRIBUTES = "attrs";
	public static final String SERVICES = "srv";
	public static final String OWNERS = "own";
	public static final String EXT_SOURCES = "extsrc";
	public static final String SEARCHER = "srch";
	public static final String NAMESPACES = "nmspc";

	// Getters for usage in UiBinder xml definitions

	public static String getFacilityDetail() {
		return FACILITY_DETAIL;
	}

	public static String getPerunFacilities() {
		return PERUN_FACILITIES;
	}

	public static String getPerunVos() {
		return PERUN_VOS;
	}

	public static String getHome() {
		return HOME;
	}

	public static String getNotFound() {
		return NOT_FOUND;
	}

	public static String getUnauthorized() {
		return UNAUTHORIZED;
	}

	public static String getLogout() {
		return LOGOUT;
	}

	public static String getHelp() {
		return HELP;
	}

	public static String getVos() {
		return VOS;
	}

	public static String getVosDetail() {
		return VOS_DETAIL;
	}

	public static String getFacilities() {
		return FACILITIES;
	}

	public static String getUsers() {
		return USERS;
	}

	public static String getAttributes() {
		return ATTRIBUTES;
	}

	public static String getServices() {
		return SERVICES;
	}

	public static String getOwners() {
		return OWNERS;
	}

	public static String getExtSources() {
		return EXT_SOURCES;
	}

	public static String getSearcher() {
		return SEARCHER;
	}

	public static String getNamespaces() {
		return NAMESPACES;
	}

	public static String getVosSelect() {
		return VOS_SELECT;
	}

}
