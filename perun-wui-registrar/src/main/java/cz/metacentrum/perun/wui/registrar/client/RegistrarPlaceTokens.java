package cz.metacentrum.perun.wui.registrar.client;

import cz.metacentrum.perun.wui.client.resources.PlaceTokens;

/**
 * Place tokens used in Perun WUI Registrar app.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class RegistrarPlaceTokens extends PlaceTokens {

	// General Pages
	public static final String MY_APPS = "submitted";
	public static final String FORM = "form";
	public static final String APP_DETAIL = "appdetail";
	public static final String VERIFY = "verify";

	public static String getMyApps() {
		return MY_APPS;
	}

	public static String getForm() {
		return FORM;
	}

	public static String getAppDetail() {
		return APP_DETAIL;
	}

	public static String getVerify() {
		return VERIFY;
	}

}
