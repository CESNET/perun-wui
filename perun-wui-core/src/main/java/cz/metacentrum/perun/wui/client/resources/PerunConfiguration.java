package cz.metacentrum.perun.wui.client.resources;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.common.WayfGroup;
import org.gwtbootstrap3.client.ui.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * This class allows to read configuration of Perun for WUI apps.
 *
 * Values are filled on app startup from Perun RPC
 * @see cz.metacentrum.perun.wui.client.PerunBootstrapper
 * @see #setPerunConfig(BasicOverlayObject)
 *
 * If any config option is present / has value locally
 * (inside HTML page this app loads into) it takes priority
 * and data loaded from RPC are ignored.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public final class PerunConfiguration {

	// storage for global configuration
	private static BasicOverlayObject perunConfig;

	/**
	 * Set data loaded from Perun RPC to config backend for usage by WUI apps.
	 * @see cz.metacentrum.perun.wui.json.managers.UtilsManager#getGuiConfiguration(JsonEvents)
	 * @see cz.metacentrum.perun.wui.client.PerunBootstrapper
	 *
	 * @param perunConfig Configuration object loaded from RPC.
	 */
	public static void setPerunConfig(BasicOverlayObject perunConfig) {
		PerunConfiguration config = new PerunConfiguration();
		config.setGlobalConfig(perunConfig);
	}

	/**
	 * Returns local config object if present.
	 *
	 * @return local configuration object
	 */
	private static native BasicOverlayObject getLocalConfig() /*-{
		return $wnd.perun_config;
	}-*/;

	/**
	 * Return string value of property from perun config or null.
	 *
	 * @param name property name
	 * @return property value or null if anything fails
	 */
	private static String getConfigPropertyString(String name) {

		if (name == null || name.isEmpty()) return null;

		// stored locally
		if (getLocalConfig() != null && JsUtils.hasOwnProperty(getLocalConfig(), name)) {
			return JsUtils.getNativePropertyString(getLocalConfig(), name);
		}

		// stored globally
		return (perunConfig != null) ? JsUtils.getNativePropertyString(perunConfig, name) : null;

	}

	/**
	 * Return int value of property from perun config or 0.
	 *
	 * @param name property name
	 * @return property value or 0 if anything fails
	 */
	private static int getConfigPropertyInt(String name) {

		if (name == null || name.isEmpty()) return 0;

		// stored locally
		if (getLocalConfig() != null && JsUtils.hasOwnProperty(getLocalConfig(), name)) {
			String value = JsUtils.getNativePropertyString(getLocalConfig(), name);
			return (value != null) ? Integer.parseInt(value) : 0;
		}

		// stored globally
		if (perunConfig != null) {
			String value = JsUtils.getNativePropertyString(perunConfig, name);
			return (value != null) ? Integer.parseInt(value) : 0;
		}

		return 0;

	}

	/**
	 * Return boolean value of property from perun config or FALSE.
	 *
	 * @param name property name
	 * @return property value or FALSE if anything fails
	 */
	private static boolean getConfigPropertyBoolean(String name) {

		if (name == null || name.isEmpty()) return false;

		// stored locally
		if (getLocalConfig() != null && JsUtils.hasOwnProperty(getLocalConfig(), name)) {
			String value = JsUtils.getNativePropertyString(getLocalConfig(), name);
			return (value != null) && Boolean.parseBoolean(value);
		}

		// stored globally
		if (perunConfig != null) {
			String value = JsUtils.getNativePropertyString(perunConfig, name);
			return (value != null) && Boolean.parseBoolean(value);
		}

		return false;

	}

	/**
	 * Return array value of property from perun config or null.
	 *
	 * @param name property name
	 * @return property value or NULL if anything fails
	 */
	private static <T extends JavaScriptObject> JsArray<T> getConfigPropertyArray(String name) {

		if (name == null || name.isEmpty()) return null;

		// stored locally
		if (getLocalConfig() != null && JsUtils.hasOwnProperty(getLocalConfig(), name)) {
			return JsUtils.getNativePropertyArray(getLocalConfig(), name);
		}

		// can't be stored globally
		return null;

	}

	/**
	 * Get code of current user locale in GWT. Wraps "default" locale as "en" (english)
	 * since we use it as default. This saves us one compilation permutation.
	 *
	 * @return Name of current user locale: supported by GWT/WUI or "en".
	 */
	public static String getCurrentLocaleName() {

		String locale = LocaleInfo.getCurrentLocale().getLocaleName();
		if (locale.equals("default")) return "en";
		return locale;

	}

	/**
	 * Return set of supported languages codes, "en" is always supported as default.
	 *
	 * @return set of supported language codes
	 */
	public static HashSet<String> getSupportedLanguages() {

		ArrayList<String> languages = new ArrayList<>();
		languages.add("en");
		languages.addAll(Utils.stringToList(getConfigPropertyString("language.supported"),","));
		Collections.sort(languages);
		return new HashSet<String>(languages);

	}

	/**
	 * Return flag image for specified language code
	 *
	 * @return Flag for language code
	 */
	public static Image getLanguageFlag(String langCode) {
		return new Image(getConfigPropertyString("language.flags."+langCode));
	}

	/**
	 * Return TRUE if language switching should be disabled.
	 * Return FALSE when to allow display of language switcher.
	 *
	 * @return TRUE when language switching should be disabled, FALSE otherwise.
	 */
	public static boolean isLangSwitchingDisabled() {
		return getConfigPropertyBoolean("language.switch.disabled");
	}

	// ---------------- BRAND ------------------ //

	/**
	 * Get brand logo in current user locale. If not present, "en" version is used.
	 * If none is set, Perun brand logo is used.
	 *
	 * @return Brand logo
	 */
	public static Image getBrandLogo() {

		// logo is encoded as base64
		String value = getConfigPropertyString("brand.logo."+getCurrentLocaleName());
		if (value == null || value.isEmpty()) value = getConfigPropertyString("brand.logo.en");
		if (value == null || value.isEmpty()) {
			// generic logo
			return new Image(PerunResources.INSTANCE.getPerunLogo());
		} else {
			// branded localized logo
			return new Image(value);
		}

	}

	/**
	 * Get brand logo URL (link to some webpage) in current user locale. If not present, "en" version is used.
	 * If none is set null is returned.
	 * If url is malformed it is returned anyway!
	 *
	 * @return Brand logo url or null
	 */
	public static String getBrandLogoUrl() {

		String value = getConfigPropertyString("brand.logo.url."+getCurrentLocaleName());
		if (value == null || value.isEmpty()) value = getConfigPropertyString("brand.logo.url.en");
		if (value == null || value.isEmpty()) {
			return null;
		} else {
			return value;
		}

	}

	/**
	 * Get brand name in current user locale. If not present, "en" version is used.
	 * If none is set, Perun brand name is used.
	 *
	 * @return Brand name in current Locale
	 */
	public static String getBrandName() {

		String value = getConfigPropertyString("brand.name."+getCurrentLocaleName());
		if (value == null || value.isEmpty()) value = getConfigPropertyString("brand.name.en");
		if (value == null || value.isEmpty()) return "Perun";
		return value;

	}

	/**
	 * Get brand support mail for current user locale. If not present, "en" version is used.
	 * If none is set, Perun support mail is used.
	 *
	 * @return Brand support mail for current Locale
	 */
	public static String getBrandSupportMail() {

		// logo is encoded as base64
		String value = getConfigPropertyString("brand.supportMail."+getCurrentLocaleName());
		if (value == null || value.isEmpty()) value = getConfigPropertyString("brand.supportMail.en");
		if (value == null || value.isEmpty()) return "perun@cesnet.cz";
		return value;

	}

	/**
	 * Get brand support phone for current user locale. If not present, "en" version is used.
	 * If none is set, Perun support phone is used.
	 *
	 * @return Brand support phone for current Locale
	 */
	public static String getBrandSupportPhone() {

		// logo is encoded as base64
		String value = getConfigPropertyString("brand.supportPhone."+getCurrentLocaleName());
		if (value == null || value.isEmpty()) value = getConfigPropertyString("brand.supportPhone.en");
		if (value == null || value.isEmpty()) return "";
		return value;

	}

	// TODO - header (show/hide, available langs to switch)
	// TODO - footer (show/hide, maybe fill data ?)
	// TODO - handle app-wide plugs

	// ---------------- BRAND ------------------ //

	/**
	 * Return TRUE if default footer should be disabled (brand is using own).
	 * Return FALSE when to use default footer.
	 *
	 * @return TRUE when brand uses own footer, FALSE otherwise.
	 */
	public static boolean isFooterDisabled() {
		return getConfigPropertyBoolean("footer.disabled");
	}

	/**
	 * Return TRUE if default header should be disabled (brand is using own).
	 * Return FALSE when to use default header.
	 *
	 * @return TRUE when brand uses own header, FALSE otherwise.
	 */
	public static boolean isHeaderDisabled() {
		return getConfigPropertyBoolean("header.disabled");
	}

	// --------------------------- TECHNICAL ---------------------------- //

	/**
	 * Returns name of RT queue for bug/support reporting
	 *
	 * @return Name of RT queue
	 */
	public static String getRTDefaultQueueName() {
		return getConfigPropertyString("rt.defaultQueue");
	}

	/**
	 * Returns name of RT queue for bug/support reporting
	 *
	 * @return Name of RT queue
	 */
	public static String getRTDefaultMail() {
		return getConfigPropertyString("rt.defaultMail");
	}

	/**
	 * Returns public key part of Re-Captcha widgetsWrapper (by GOOGLE)
	 * which is used for anonymous access to registration form app etc.
	 * <p/>
	 * If public key is not present, return null
	 *
	 * @return Re-Captcha public key
	 */
	public static String getReCaptchaPublicKey() {
		return getConfigPropertyString("reCaptcha.publicKey");
	}

	/**
	 * Return list of VOs short names for which Re-CAPTCHA verification should be skipped.
	 *
	 * @return list of VOs short names
	 */
	public static ArrayList<String> getVosToSkipReCaptchaFor() {
		return Utils.stringToList(getConfigPropertyString("reCaptcha.skipVos"),",");
	}

	/**
	 * Return list of attribute names to get with members in all tables.
	 *
	 * @return List of attribute names
	 */
	public static ArrayList<String> getAttributesForMemberTable() {
		return Utils.stringToList(getConfigPropertyString("attributesForMemberTables"),",");
	}

	/**
	 * Return list of attribute names to get with members in all tables.
	 *
	 * @return List of attribute names
	 */
	public static ArrayList<String> getAttributesForUserTable() {
		return Utils.stringToList(getConfigPropertyString("attributesForUserTables"),",");
	}

	/**
	 * Return list of unix group name namespaces in which is allowed to set preferences in group names.
	 *
	 * Used to set attribute: urn:perun:user:attribute-def:def:preferredUnixGroupName-namespace:[namespace]
	 *
	 * @return list of supported namespaces
	 */
	public static ArrayList<String> getPreferredUnixGroupNamesNamespaces() {
		return Utils.stringToList(getConfigPropertyString("namespacesForPreferredGroupNames"),",");
	}

	/**
	 * Returns list of supported namespaces names for password change / reset
	 *
	 * @return list of supported namespaces names
	 */
	public static ArrayList<String> getSupportedPasswordNamespaces() {
		String value = getConfigPropertyString("supportedPasswordNamespaces");
		return Utils.stringToList(value, ",");
	}

	/**
	 * Returns name of Perun Instance if present in a config or "Perun" if not present.
	 *
	 * @return name of Perun Instance as string
	 */
	public static String perunInstanceName() {

		String value = getConfigPropertyString("perunInstanceName");
		if (value == null || value.isEmpty()) return "Perun";
		return value;

	}

	/**
	 * Return TRUE if Perun instance is devel.
	 *
	 * @return TRUE if Perun instance is devel, FALSE otherwise
	 */
	public static boolean isDevel() {
		return getConfigPropertyBoolean("isDevel");
	}

	/**
	 * Return name of "members" group.
	 *
	 * @return name of "members" group.
	 */
	public static String getMembersGroupName() {
		String name = getConfigPropertyString("membersGroupName");
		if (name == null || name.isEmpty()) return "members";
		return name;
	}

	/**
	 * Returns list of all /authz/ paths, which are behind Shibboleth SP (federation access).
	 *
	 * @return list of all /fed/-like authz paths
	 */
	public static ArrayList<String> getFedAuthz() {
		String value = getConfigPropertyString("fedAuthz");
		return Utils.stringToList(value, ",");
	}




	// ---------------------------   WAYF   ---------------------------- //

	/**
	 * Returns list of wayf groups (ways of authentication).
	 *
	 * @return List of enabled wayf groups
	 */
	public static ArrayList<WayfGroup> getWayfGroups() {
		return JsUtils.jsoAsList(getConfigPropertyArray("wayf.groups"));
	}

	/**
	 * Returns URL to feeds for Identity Consolidator webapp
	 *
	 * @return URL to feeds
	 */
	public static String getWayfFeedUrl() {
		return getConfigPropertyString("wayf.feedUrl");
	}

	/**
	 * Returns URL to feed images for Identity Consolidator webapp
	 *
	 * @return URL to feed images
	 */
	public static String getWayfFeedImagesUrl() {
		return getConfigPropertyString("wayf.feedImagesUrl");
	}

	/**
	 * Returns list of all hostnames supported for Identity Consolidator using cert authz.
	 * Empty if none supported.
	 *
	 * @return list of all cert hostnames for IC
	 */
	public static ArrayList<String> getWayfCertHostnames() {
		String value = getConfigPropertyString("wayf.cert.hosts");
		return Utils.stringToList(value, ",");
	}

	/**
	 * Returns URL to the Discovery service of consolidator SP
	 * If not set, current hostname is used, replacing path with "/Shibboleth.sso/DS"
	 *
	 * @return SP login URL
	 */
	public static String getWayfSpLoginUrl() {

		// TODO - rename property in a config too
		String SPlogin = getConfigPropertyString("wayf.spDsUrl");
		if (SPlogin == null || SPlogin.isEmpty()) {
			// backup to current hostname
			SPlogin = Window.Location.getProtocol() + "//" + Window.Location.getHostName() + "/Shibboleth.sso/Login";
		}
		return SPlogin;

	}

	/**
	 * Returns logout URL from consolidator SP
	 * If not set, current hostname is used, replacing path with "/Shibboleth.sso/Logout"
	 *
	 * @return SP logout URL
	 */
	public static String getWayfSpLogoutUrl() {
		String SPlogout = getConfigPropertyString("wayf.spLogoutUrl");
		if (SPlogout == null || SPlogout.isEmpty()) {
			// backup to current hostname
			SPlogout = Window.Location.getProtocol() + "//" + Window.Location.getHostName() + "/Shibboleth.sso/Logout";
		}
		return SPlogout;
	}


	public static boolean isWayfLinkAnotherButtonDisabled() {
		return getConfigPropertyBoolean("wayf.linkAnotherButton.disabled");
	}

	public static String getWayfLinkAnotherButtonText() {
		String value = getConfigPropertyString("wayf.linkAnotherButton."+getCurrentLocaleName());
		if (value == null || value.isEmpty()) value = getConfigPropertyString("wayf.linkAnotherButton.en");
		return value;
	}

	public static String getWayfLeaveButtonText() {
		String value = getConfigPropertyString("wayf.leaveButton."+getCurrentLocaleName());
		if (value == null || value.isEmpty()) value = getConfigPropertyString("wayf.leaveButton.en");
		return value;
	}

	/**
	 * Return map of customized IdP (or ext source) translations.
	 *
	 * @return map of translated IdPs
	 */
	public static Map<String,String> getCustomIdpTranslations() {
		String value = getConfigPropertyString("wayf.idpTranslations");

		Map<String,String> translation = new HashMap<>();
		List<String> couples = Utils.stringToList(value, "#");
		for (String couple : couples) {
			String[] cp = couple.split("\\|");
			translation.put(cp[0],cp[1]);
		}
		return translation;

	}

	private void PerunConfiguration() {
	}

	private void setGlobalConfig(BasicOverlayObject jso) {
		this.perunConfig = jso;
	}

}
