package cz.metacentrum.perun.wui.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.URL;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import org.gwtbootstrap3.extras.animate.client.ui.constants.Animation;
import org.gwtbootstrap3.extras.notify.client.ui.NotifySettings;

import java.util.*;

/**
 * Class with support methods used in GUI code
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Utils {

	public static final String GROUP_NAME_MATCHER = "^[- a-zA-Z.0-9_:]+$";
	public static final String GROUP_SHORT_NAME_MATCHER = "^[- a-zA-Z.0-9_]+$";
	public static final String VO_SHORT_NAME_MATCHER = "^[-a-zA-Z0-9_]+$";
	public static final String ATTRIBUTE_FRIENDLY_NAME_MATCHER = "^[-a-zA-Z0-9.]+([:][-a-zA-Z0-9.]+)?$";
	public static final String LOGIN_VALUE_MATCHER = "^[a-zA-Z0-9_][-A-z0-9_.@/]*$";

	private static final HashMap<String, String> organizationsTranslation = new HashMap<String, String>();
	private static boolean firstTranslationCall = true;

	static {

		organizationsTranslation.put("https://idp.upce.cz/idp/shibboleth", "University in Pardubice");
		organizationsTranslation.put("https://idp.slu.cz/idp/shibboleth", "University in Opava");
		organizationsTranslation.put("https://login.feld.cvut.cz/idp/shibboleth", "Faculty of Electrical Engineering, Czech Technical University In Prague");
		organizationsTranslation.put("https://www.vutbr.cz/SSO/saml2/idp", "Brno University of Technology");
		organizationsTranslation.put("https://shibboleth.nkp.cz/idp/shibboleth", "The National Library of the Czech Republic");
		organizationsTranslation.put("https://idp2.civ.cvut.cz/idp/shibboleth", "Czech Technical University In Prague");
		organizationsTranslation.put("https://shibbo.tul.cz/idp/shibboleth", "Technical University of Liberec");
		organizationsTranslation.put("https://idp.mendelu.cz/idp/shibboleth", "Mendel University in Brno");
		organizationsTranslation.put("https://cas.cuni.cz/idp/shibboleth", "Charles University in Prague");
		organizationsTranslation.put("https://wsso.vscht.cz/idp/shibboleth", "Institute of Chemical Technology Prague");
		organizationsTranslation.put("https://idp.vsb.cz/idp/shibboleth", "VSB – Technical University of Ostrava");
		organizationsTranslation.put("https://whoami.cesnet.cz/idp/shibboleth", "CESNET, z.s.p.o.");
		organizationsTranslation.put("https://helium.jcu.cz/idp/shibboleth", "University of South Bohemia");
		organizationsTranslation.put("https://idp.ujep.cz/idp/shibboleth", "Jan Evangelista Purkyne University in Usti nad Labem");
		organizationsTranslation.put("https://idp.amu.cz/idp/shibboleth", "Academy of Performing Arts in Prague");
		organizationsTranslation.put("https://idp.lib.cas.cz/idp/shibboleth", "Academy of Sciences Library");
		organizationsTranslation.put("https://shibboleth.mzk.cz/simplesaml/metadata.xml", "Moravian  Library");
		organizationsTranslation.put("https://idp2.ics.muni.cz/idp/shibboleth", "Masaryk University");
		organizationsTranslation.put("https://idp.upol.cz/idp/shibboleth", "Palacky University, Olomouc");
		organizationsTranslation.put("https://idp.fnplzen.cz/idp/shibboleth", "FN Plzen");
		organizationsTranslation.put("https://id.vse.cz/idp/shibboleth", "University of Economics, Prague");
		organizationsTranslation.put("https://shib.zcu.cz/idp/shibboleth", "University of West Bohemia");
		organizationsTranslation.put("https://idptoo.osu.cz/simplesaml/saml2/idp/metadata.php", "University of Ostrava");
		organizationsTranslation.put("https://login.ics.muni.cz/idp/shibboleth", "MetaCentrum");
		organizationsTranslation.put("https://idp.hostel.eduid.cz/idp/shibboleth", "eduID.cz Hostel");
		organizationsTranslation.put("https://shibboleth.techlib.cz/idp/shibboleth", "National Library of Technology");
		organizationsTranslation.put("https://eduid.jamu.cz/idp/shibboleth", "Janacek Academy of Music and Performing Arts in Brno");
		organizationsTranslation.put("https://marisa.uochb.cas.cz/simplesaml/saml2/idp/metadata.php", "Institute of Organic Chemistry and Biochemistry AS CR");
		organizationsTranslation.put("https://shibboleth.utb.cz/idp/shibboleth", "Tomas Bata University in Zlin");
		organizationsTranslation.put("https://www.egi.eu/idp/shibboleth", "EGI");
		organizationsTranslation.put("https://engine.elixir-czech.org/authentication/idp/metadata", "Elixir Europe");

		// Handle social identities

		//organizationsTranslation.put("https://extidp.cesnet.cz/idp/shibboleth", "Social");
		organizationsTranslation.put("https://extidp.cesnet.cz/idp/shibboleth&authnContextClassRef=urn:cesnet:extidp:authn:google", "Google");
		organizationsTranslation.put("https://extidp.cesnet.cz/idp/shibboleth&authnContextClassRef=urn:cesnet:extidp:authn:facebook", "Facebook");
		organizationsTranslation.put("https://extidp.cesnet.cz/idp/shibboleth&authnContextClassRef=urn:cesnet:extidp:authn:linkedin", "LinkedIn");
		organizationsTranslation.put("https://extidp.cesnet.cz/idp/shibboleth&authnContextClassRef=urn:cesnet:extidp:authn:mojeid", "MojeID");
		organizationsTranslation.put("https://extidp.cesnet.cz/idp/shibboleth&authnContextClassRef=urn:cesnet:extidp:authn:orcid", "OrcID");
		organizationsTranslation.put("https://extidp.cesnet.cz/idp/shibboleth&authnContextClassRef=urn:cesnet:extidp:authn:github", "GitHub");

		organizationsTranslation.put("@google.extidp.cesnet.cz", "Google");
		organizationsTranslation.put("@facebook.extidp.cesnet.cz", "Facebook");
		organizationsTranslation.put("@mojeid.extidp.cesnet.cz", "MojeID");
		organizationsTranslation.put("@linkedin.extidp.cesnet.cz", "LinkedIn");
		organizationsTranslation.put("@twitter.extidp.cesnet.cz", "Twitter");
		organizationsTranslation.put("@seznam.extidp.cesnet.cz", "Seznam");
		organizationsTranslation.put("@elixir-europe.org", "Elixir Europe");
		organizationsTranslation.put("@github.extidp.cesnet.cz", "GitHub");
		organizationsTranslation.put("@orcid.extidp.cesnet.cz", "OrcID");

		organizationsTranslation.put("@google", "Google");
		organizationsTranslation.put("@facebook", "Facebook");
		organizationsTranslation.put("@mojeid", "MojeID");
		organizationsTranslation.put("@linkedin", "LinkedIn");
		organizationsTranslation.put("@twitter", "Twitter");
		organizationsTranslation.put("@seznam", "Seznam");
		organizationsTranslation.put("@github", "GitHub");
		organizationsTranslation.put("@orcid", "OrcID");

		// kerberos
		organizationsTranslation.put("META", "MetaCentrum");
		organizationsTranslation.put("EINFRA", "CESNET eInfrastructure");
		organizationsTranslation.put("SITOLA.FI.MUNI.CZ", "Sitola");
		organizationsTranslation.put("ICS.MUNI.CZ", "Masaryk University");
		organizationsTranslation.put("SAGRID", "SAGrid");

	}

	/*
	 * Database of timezones from http://en.wikipedia.org/wiki/List_of_tz_database_time_zones.
	 */
	final static ArrayList<String> timezones = new ArrayList<String>(Arrays.asList(
			"Africa/Abidjan",
			"Africa/Accra",
			"Africa/Addis_Ababa",
			"Africa/Algiers",
			"Africa/Asmara",
			"Africa/Asmera",
			"Africa/Bamako",
			"Africa/Bangui",
			"Africa/Banjul",
			"Africa/Bissau",
			"Africa/Blantyre",
			"Africa/Brazzaville",
			"Africa/Bujumbura",
			"Africa/Cairo",
			"Africa/Casablanca",
			"Africa/Ceuta",
			"Africa/Conakry",
			"Africa/Dakar",
			"Africa/Dar_es_Salaam",
			"Africa/Djibouti",
			"Africa/Douala",
			"Africa/El_Aaiun",
			"Africa/Freetown",
			"Africa/Gaborone",
			"Africa/Harare",
			"Africa/Johannesburg",
			"Africa/Juba",
			"Africa/Kampala",
			"Africa/Khartoum",
			"Africa/Kigali",
			"Africa/Kinshasa",
			"Africa/Lagos",
			"Africa/Libreville",
			"Africa/Lome",
			"Africa/Luanda",
			"Africa/Lubumbashi",
			"Africa/Lusaka",
			"Africa/Malabo",
			"Africa/Maputo",
			"Africa/Maseru",
			"Africa/Mbabane",
			"Africa/Mogadishu",
			"Africa/Monrovia",
			"Africa/Nairobi",
			"Africa/Ndjamena",
			"Africa/Niamey",
			"Africa/Nouakchott",
			"Africa/Ouagadougou",
			"Africa/Porto-Novo",
			"Africa/Sao_Tome",
			"Africa/Timbuktu",
			"Africa/Tripoli",
			"Africa/Tunis",
			"Africa/Windhoek",
			"AKST9AKDT",
			"America/Adak",
			"America/Anchorage",
			"America/Anguilla",
			"America/Antigua",
			"America/Araguaina",
			"America/Argentina/Buenos_Aires",
			"America/Argentina/Catamarca",
			"America/Argentina/ComodRivadavia",
			"America/Argentina/Cordoba",
			"America/Argentina/Jujuy",
			"America/Argentina/La_Rioja",
			"America/Argentina/Mendoza",
			"America/Argentina/Rio_Gallegos",
			"America/Argentina/Salta",
			"America/Argentina/San_Juan",
			"America/Argentina/San_Luis",
			"America/Argentina/Tucuman",
			"America/Argentina/Ushuaia",
			"America/Aruba",
			"America/Asuncion",
			"America/Atikokan",
			"America/Atka",
			"America/Bahia",
			"America/Bahia_Banderas",
			"America/Barbados",
			"America/Belem",
			"America/Belize",
			"America/Blanc-Sablon",
			"America/Boa_Vista",
			"America/Bogota",
			"America/Boise",
			"America/Buenos_Aires",
			"America/Cambridge_Bay",
			"America/Campo_Grande",
			"America/Cancun",
			"America/Caracas",
			"America/Catamarca",
			"America/Cayenne",
			"America/Cayman",
			"America/Chicago",
			"America/Chihuahua",
			"America/Coral_Harbour",
			"America/Cordoba",
			"America/Costa_Rica",
			"America/Creston",
			"America/Cuiaba",
			"America/Curacao",
			"America/Danmarkshavn",
			"America/Dawson",
			"America/Dawson_Creek",
			"America/Denver",
			"America/Detroit",
			"America/Dominica",
			"America/Edmonton",
			"America/Eirunepe",
			"America/El_Salvador",
			"America/Ensenada",
			"America/Fort_Wayne",
			"America/Fortaleza",
			"America/Glace_Bay",
			"America/Godthab",
			"America/Goose_Bay",
			"America/Grand_Turk",
			"America/Grenada",
			"America/Guadeloupe",
			"America/Guatemala",
			"America/Guayaquil",
			"America/Guyana",
			"America/Halifax",
			"America/Havana",
			"America/Hermosillo",
			"America/Indiana/Indianapolis",
			"America/Indiana/Knox",
			"America/Indiana/Marengo",
			"America/Indiana/Petersburg",
			"America/Indiana/Tell_City",
			"America/Indiana/Vevay",
			"America/Indiana/Vincennes",
			"America/Indiana/Winamac",
			"America/Indianapolis",
			"America/Inuvik",
			"America/Iqaluit",
			"America/Jamaica",
			"America/Jujuy",
			"America/Juneau",
			"America/Kentucky/Louisville",
			"America/Kentucky/Monticello",
			"America/Knox_IN",
			"America/Kralendijk",
			"America/La_Paz",
			"America/Lima",
			"America/Los_Angeles",
			"America/Louisville",
			"America/Lower_Princes",
			"America/Maceio",
			"America/Managua",
			"America/Manaus",
			"America/Marigot",
			"America/Martinique",
			"America/Matamoros",
			"America/Mazatlan",
			"America/Mendoza",
			"America/Menominee",
			"America/Merida",
			"America/Metlakatla",
			"America/Mexico_City",
			"America/Miquelon",
			"America/Moncton",
			"America/Monterrey",
			"America/Montevideo",
			"America/Montreal",
			"America/Montserrat",
			"America/Nassau",
			"America/New_York",
			"America/Nipigon",
			"America/Nome",
			"America/Noronha",
			"America/North_Dakota/Beulah",
			"America/North_Dakota/Center",
			"America/North_Dakota/New_Salem",
			"America/Ojinaga",
			"America/Panama",
			"America/Pangnirtung",
			"America/Paramaribo",
			"America/Phoenix",
			"America/Port_of_Spain",
			"America/Port-au-Prince",
			"America/Porto_Acre",
			"America/Porto_Velho",
			"America/Puerto_Rico",
			"America/Rainy_River",
			"America/Rankin_Inlet",
			"America/Recife",
			"America/Regina",
			"America/Resolute",
			"America/Rio_Branco",
			"America/Rosario",
			"America/Santa_Isabel",
			"America/Santarem",
			"America/Santiago",
			"America/Santo_Domingo",
			"America/Sao_Paulo",
			"America/Scoresbysund",
			"America/Shiprock",
			"America/Sitka",
			"America/St_Barthelemy",
			"America/St_Johns",
			"America/St_Kitts",
			"America/St_Lucia",
			"America/St_Thomas",
			"America/St_Vincent",
			"America/Swift_Current",
			"America/Tegucigalpa",
			"America/Thule",
			"America/Thunder_Bay",
			"America/Tijuana",
			"America/Toronto",
			"America/Tortola",
			"America/Vancouver",
			"America/Virgin",
			"America/Whitehorse",
			"America/Winnipeg",
			"America/Yakutat",
			"America/Yellowknife",
			"Antarctica/Casey",
			"Antarctica/Davis",
			"Antarctica/DumontDUrville",
			"Antarctica/Macquarie",
			"Antarctica/Mawson",
			"Antarctica/McMurdo",
			"Antarctica/Palmer",
			"Antarctica/Rothera",
			"Antarctica/South_Pole",
			"Antarctica/Syowa",
			"Antarctica/Vostok",
			"Arctic/Longyearbyen",
			"Asia/Aden",
			"Asia/Almaty",
			"Asia/Amman",
			"Asia/Anadyr",
			"Asia/Aqtau",
			"Asia/Aqtobe",
			"Asia/Ashgabat",
			"Asia/Ashkhabad",
			"Asia/Baghdad",
			"Asia/Bahrain",
			"Asia/Baku",
			"Asia/Bangkok",
			"Asia/Beirut",
			"Asia/Bishkek",
			"Asia/Brunei",
			"Asia/Calcutta",
			"Asia/Choibalsan",
			"Asia/Chongqing",
			"Asia/Chungking",
			"Asia/Colombo",
			"Asia/Dacca",
			"Asia/Damascus",
			"Asia/Dhaka",
			"Asia/Dili",
			"Asia/Dubai",
			"Asia/Dushanbe",
			"Asia/Gaza",
			"Asia/Harbin",
			"Asia/Hebron",
			"Asia/Ho_Chi_Minh",
			"Asia/Hong_Kong",
			"Asia/Hovd",
			"Asia/Irkutsk",
			"Asia/Istanbul",
			"Asia/Jakarta",
			"Asia/Jayapura",
			"Asia/Jerusalem",
			"Asia/Kabul",
			"Asia/Kamchatka",
			"Asia/Karachi",
			"Asia/Kashgar",
			"Asia/Kathmandu",
			"Asia/Katmandu",
			"Asia/Kolkata",
			"Asia/Krasnoyarsk",
			"Asia/Kuala_Lumpur",
			"Asia/Kuching",
			"Asia/Kuwait",
			"Asia/Macao",
			"Asia/Macau",
			"Asia/Magadan",
			"Asia/Makassar",
			"Asia/Manila",
			"Asia/Muscat",
			"Asia/Nicosia",
			"Asia/Novokuznetsk",
			"Asia/Novosibirsk",
			"Asia/Omsk",
			"Asia/Oral",
			"Asia/Phnom_Penh",
			"Asia/Pontianak",
			"Asia/Pyongyang",
			"Asia/Qatar",
			"Asia/Qyzylorda",
			"Asia/Rangoon",
			"Asia/Riyadh",
			"Asia/Saigon",
			"Asia/Sakhalin",
			"Asia/Samarkand",
			"Asia/Seoul",
			"Asia/Shanghai",
			"Asia/Singapore",
			"Asia/Taipei",
			"Asia/Tashkent",
			"Asia/Tbilisi",
			"Asia/Tehran",
			"Asia/Tel_Aviv",
			"Asia/Thimbu",
			"Asia/Thimphu",
			"Asia/Tokyo",
			"Asia/Ujung_Pandang",
			"Asia/Ulaanbaatar",
			"Asia/Ulan_Bator",
			"Asia/Urumqi",
			"Asia/Vientiane",
			"Asia/Vladivostok",
			"Asia/Yakutsk",
			"Asia/Yekaterinburg",
			"Asia/Yerevan",
			"Atlantic/Azores",
			"Atlantic/Bermuda",
			"Atlantic/Canary",
			"Atlantic/Cape_Verde",
			"Atlantic/Faeroe",
			"Atlantic/Faroe",
			"Atlantic/Jan_Mayen",
			"Atlantic/Madeira",
			"Atlantic/Reykjavik",
			"Atlantic/South_Georgia",
			"Atlantic/St_Helena",
			"Atlantic/Stanley",
			"Australia/ACT",
			"Australia/Adelaide",
			"Australia/Brisbane",
			"Australia/Broken_Hill",
			"Australia/Canberra",
			"Australia/Currie",
			"Australia/Darwin",
			"Australia/Eucla",
			"Australia/Hobart",
			"Australia/LHI",
			"Australia/Lindeman",
			"Australia/Lord_Howe",
			"Australia/Melbourne",
			"Australia/North",
			"Australia/NSW",
			"Australia/Perth",
			"Australia/Queensland",
			"Australia/South",
			"Australia/Sydney",
			"Australia/Tasmania",
			"Australia/Victoria",
			"Australia/West",
			"Australia/Yancowinna",
			"Brazil/Acre",
			"Brazil/DeNoronha",
			"Brazil/East",
			"Brazil/West",
			"Canada/Atlantic",
			"Canada/Central",
			"Canada/Eastern",
			"Canada/East-Saskatchewan",
			"Canada/Mountain",
			"Canada/Newfoundland",
			"Canada/Pacific",
			"Canada/Saskatchewan",
			"Canada/Yukon",
			"CET",
			"Chile/Continental",
			"Chile/EasterIsland",
			"CST6CDT",
			"Cuba",
			"EET",
			"Egypt",
			"Eire",
			"EST",
			"EST5EDT",
			"Etc/GMT",
			"Etc/GMT+0",
			"Etc/UCT",
			"Etc/Universal",
			"Etc/UTC",
			"Etc/Zulu",
			"Europe/Amsterdam",
			"Europe/Andorra",
			"Europe/Athens",
			"Europe/Belfast",
			"Europe/Belgrade",
			"Europe/Berlin",
			"Europe/Bratislava",
			"Europe/Brussels",
			"Europe/Bucharest",
			"Europe/Budapest",
			"Europe/Chisinau",
			"Europe/Copenhagen",
			"Europe/Dublin",
			"Europe/Gibraltar",
			"Europe/Guernsey",
			"Europe/Helsinki",
			"Europe/Isle_of_Man",
			"Europe/Istanbul",
			"Europe/Jersey",
			"Europe/Kaliningrad",
			"Europe/Kiev",
			"Europe/Lisbon",
			"Europe/Ljubljana",
			"Europe/London",
			"Europe/Luxembourg",
			"Europe/Madrid",
			"Europe/Malta",
			"Europe/Mariehamn",
			"Europe/Minsk",
			"Europe/Monaco",
			"Europe/Moscow",
			"Europe/Nicosia",
			"Europe/Oslo",
			"Europe/Paris",
			"Europe/Podgorica",
			"Europe/Prague",
			"Europe/Riga",
			"Europe/Rome",
			"Europe/Samara",
			"Europe/San_Marino",
			"Europe/Sarajevo",
			"Europe/Simferopol",
			"Europe/Skopje",
			"Europe/Sofia",
			"Europe/Stockholm",
			"Europe/Tallinn",
			"Europe/Tirane",
			"Europe/Tiraspol",
			"Europe/Uzhgorod",
			"Europe/Vaduz",
			"Europe/Vatican",
			"Europe/Vienna",
			"Europe/Vilnius",
			"Europe/Volgograd",
			"Europe/Warsaw",
			"Europe/Zagreb",
			"Europe/Zaporozhye",
			"Europe/Zurich",
			"GB",
			"GB-Eire",
			"GMT",
			"GMT+0",
			"GMT0",
			"GMT-0",
			"Greenwich",
			"Hongkong",
			"HST",
			"Iceland",
			"Indian/Antananarivo",
			"Indian/Chagos",
			"Indian/Christmas",
			"Indian/Cocos",
			"Indian/Comoro",
			"Indian/Kerguelen",
			"Indian/Mahe",
			"Indian/Maldives",
			"Indian/Mauritius",
			"Indian/Mayotte",
			"Indian/Reunion",
			"Iran",
			"Israel",
			"Jamaica",
			"Japan",
			"JST-9",
			"Kwajalein",
			"Libya",
			"MET",
			"Mexico/BajaNorte",
			"Mexico/BajaSur",
			"Mexico/General",
			"MST",
			"MST7MDT",
			"Navajo",
			"NZ",
			"NZ-CHAT",
			"Pacific/Apia",
			"Pacific/Auckland",
			"Pacific/Chatham",
			"Pacific/Chuuk",
			"Pacific/Easter",
			"Pacific/Efate",
			"Pacific/Enderbury",
			"Pacific/Fakaofo",
			"Pacific/Fiji",
			"Pacific/Funafuti",
			"Pacific/Galapagos",
			"Pacific/Gambier",
			"Pacific/Guadalcanal",
			"Pacific/Guam",
			"Pacific/Honolulu",
			"Pacific/Johnston",
			"Pacific/Kiritimati",
			"Pacific/Kosrae",
			"Pacific/Kwajalein",
			"Pacific/Majuro",
			"Pacific/Marquesas",
			"Pacific/Midway",
			"Pacific/Nauru",
			"Pacific/Niue",
			"Pacific/Norfolk",
			"Pacific/Noumea",
			"Pacific/Pago_Pago",
			"Pacific/Palau",
			"Pacific/Pitcairn",
			"Pacific/Pohnpei",
			"Pacific/Ponape",
			"Pacific/Port_Moresby",
			"Pacific/Rarotonga",
			"Pacific/Saipan",
			"Pacific/Samoa",
			"Pacific/Tahiti",
			"Pacific/Tarawa",
			"Pacific/Tongatapu",
			"Pacific/Truk",
			"Pacific/Wake",
			"Pacific/Wallis",
			"Pacific/Yap",
			"Poland",
			"Portugal",
			"PRC",
			"PST8PDT",
			"ROC",
			"ROK",
			"Singapore",
			"Turkey",
			"UCT",
			"Universal",
			"US/Alaska",
			"US/Aleutian",
			"US/Arizona",
			"US/Central",
			"US/Eastern",
			"US/East-Indiana",
			"US/Hawaii",
			"US/Indiana-Starke",
			"US/Michigan",
			"US/Mountain",
			"US/Pacific",
			"US/Pacific-New",
			"US/Samoa",
			"UTC",
			"WET",
			"W-SU",
			"Zulu"
	));

	public static NotifySettings getDefaultNotifyOptions() {

		NotifySettings options = NotifySettings.newSettings();
		options.setDelay(10000);
		options.setAllowDismiss(false);
		options.setOffset(20, 65);
		options.setAnimation(Animation.FADE_IN_RIGHT, Animation.FADE_OUT_RIGHT);
		options.setTemplate("<div data-growl=\"container\" class=\"alert\" role=\"alert\">" +
				"<button type=\"button\" class=\"close\" data-growl=\"dismiss\">" +
				"<span aria-hidden=\"true\">×</span>" +
				"<span class=\"sr-only\">Close</span>" +
				"</button>" +
				"<span class=\"growl-icon\" data-growl=\"icon\"> </span>" +
				"<span class=\"growl-title\" data-growl=\"title\"></span>" +
				"<span class=\"growl-message\" data-growl=\"message\"></span>" +
				"<a href=\"#\" data-growl=\"url\"></a>" +
				"</div>");
		return options;
	}

	/**
	 * Return URL to identity consolidator GUI
	 * with optional ?target_url= param having current Perun GUI URL.
	 *
	 * @param target TRUE if use ?target_url= param in identity consolidator URL / FALSE otherwise
	 * @return URL to identity consolidator
	 */
	public static String getIdentityConsolidatorLink(boolean target) {
		return getIdentityConsolidatorLink(null, target);
	}

	/**
	 * Return URL to identity consolidator GUI
	 * with optional ?target= param having current Perun GUI URL.
	 *
	 * @param authz destination authorization ("fed", "krb", "cert")
	 * @param target TRUE if use ?target= param in identity consolidator URL / FALSE otherwise
	 * @return URL to identity consolidator
	 */
	public static String getIdentityConsolidatorLink(String authz, boolean target) {

		// wrong authz, return basic
		if (authz == null || authz.isEmpty()) {
			if (PerunSession.getInstance().getRpcServer() != null) {
				authz = PerunSession.getInstance().getRpcServer();
			}
		}

		String value = null;

		if (PerunSession.getInstance().getLocalConfig() != null) {
			JavaScriptObject wayfConfig = JsUtils.getNativePropertyObject(PerunSession.getInstance().getLocalConfig(), "wayf");
			if (wayfConfig != null && JsUtils.hasOwnProperty(wayfConfig, "getIdentityConsolidatorUrl")) {
				value = JsUtils.getNativePropertyString(wayfConfig, "getIdentityConsolidatorUrl");
			}
		}

		if (PerunSession.getInstance().getConfiguration() != null && (value == null || value.isEmpty())) {
			value = JsUtils.getNativePropertyString(PerunSession.getInstance().getConfiguration(), "getIdentityConsolidatorUrl");
		}

		if (value != null && !value.isEmpty()) {

			if (target) {
				// FIXME - encode query twice only if current is fed ?
				value += "?target_url=" + Window.Location.getProtocol() + "//" + Window.Location.getHost() + Window.Location.getPath() +  URL.encodeQueryString(URL.encodeQueryString(Window.Location.getQueryString()));
			}
			return value;

		} else {

			String baseUrl = "";
			boolean otherFound = false;

			// try to find alternative hostname for cert, since we want to re-force authz
			if ("cert".equalsIgnoreCase(authz) && Window.Location.getPath().startsWith("/cert")) {

				for (String hostname : PerunConfiguration.getWayfCertHostnames()) {
					if (!hostname.equals(Window.Location.getProtocol() + "//" + Window.Location.getHost())) {
						baseUrl = hostname;
						otherFound = true;
						break;
					}
				}

			}

			if (!otherFound) {
				// always use URL of machine, where GUI runs
				baseUrl = Window.Location.getProtocol() + "//" + Window.Location.getHost();
			}

			String link = baseUrl + "/"+authz+"/ic/";

			if (target) {

				link += "?target_url=" + Window.Location.getProtocol() + "//" + Window.Location.getHost() + Window.Location.getPath();

				if (PerunConfiguration.getFedAuthz().contains(authz)) {
					// for fed destination encode return query twice
					link += URL.encodeQueryString(URL.encodeQueryString(Window.Location.getQueryString()));
					return link;
				}

				// not fed-like path
				link += URL.encodeQueryString(Window.Location.getQueryString());

			}

			return link;
		}

	}

	/**
	 * Return URL to Password change GUI for selected namespace
	 *
	 * @param namespace namespace where we want to reset password
	 * @return URL to password reset GUI
	 */
	public static String getPasswordResetLink(String namespace) {

		String value = JsUtils.getNativePropertyString(PerunSession.getInstance().getConfiguration(), "getPasswordResetUrl");

		if (value != null && !value.isEmpty()) {

			// PWD-RESET URL IS CONFIGURED AS FIXED
			if (namespace != null && !namespace.isEmpty()) {
				return value + "?login-namespace=" + namespace;
			} else {
				return value;
			}

		} else {

			// USE RELATIVE PWD-RESET URL

			String baseUrl = Window.Location.getProtocol() + "//" + Window.Location.getHost();

			if (!PerunConfiguration.isDevel()) {

				// VALID URL FOR PRODUCTION

				String rpc = "";
				if (PerunSession.getInstance().getRpcServer() != null) {
					rpc = PerunSession.getInstance().getRpcServer();
					baseUrl += "/" + rpc + "/pwd-reset/";
				} else {
					baseUrl += "/pwd-reset/";
				}

			} else {

				// VALID URL FOR DEVEL
				baseUrl += "/PasswordResetKrb.html";

			}

			if (namespace != null && !namespace.isEmpty()) {
				return baseUrl + "?login-namespace=" + namespace;
			} else {
				return baseUrl;
			}

		}
	}


	public static final native String createWayfFeedFilter(String[] allowedFeeds, String[] allowedIdPs, boolean allowHostel, boolean allowHostelRegistration) /*-{
		var filter = { "allowHostel" : allowHostel ,
			"allowHostelReg" : allowHostelRegistration
		};

		if (allowedFeeds !== null) {
			filter["allowFeeds"] = allowedFeeds;
		}
		if (allowedIdPs !== null) {
			filter["allowIdPs"] = allowedIdPs;
		}

		return btoa(JSON.stringify(filter));
	}-*/;

	/**
	 * Clear all cookies provided by federation IDP in user's browser.
	 */
	public static void clearFederationCookies() {

		final String SHIBBOLETH_COOKIE_FORMAT = "^_shib.+$";

		// retrieves all the cookies
		Collection<String> cookies = Cookies.getCookieNames();

		// regexp
		RegExp regExp = RegExp.compile(SHIBBOLETH_COOKIE_FORMAT);

		for (String cookie : cookies) {
			// shibboleth cookie?
			MatchResult matcher = regExp.exec(cookie);
			boolean matchFound = (matcher != null); // equivalent to regExp.test(inputStr);
			if (matchFound) {
				// remove it
				Cookies.removeCookieNative(cookie, "/");
			}
		}

	}

	public static final native String unAccent(String str) /*-{

		if (!$wnd.defaultDiacriticsRemovalMap || typeof $wnd.defaultDiacriticsRemovalMap === 'undefined' || $wnd.defaultDiacriticsRemovalMap === null) {

			$wnd.defaultDiacriticsRemovalMap = [
				{
					'base': 'A',
					'letters': '\u0041\u24B6\uFF21\u00C0\u00C1\u00C2\u1EA6\u1EA4\u1EAA\u1EA8\u00C3\u0100\u0102\u1EB0\u1EAE\u1EB4\u1EB2\u0226\u01E0\u00C4\u01DE\u1EA2\u00C5\u01FA\u01CD\u0200\u0202\u1EA0\u1EAC\u1EB6\u1E00\u0104\u023A\u2C6F'
				},
				{'base': 'AA', 'letters': '\uA732'},
				{'base': 'AE', 'letters': '\u00C6\u01FC\u01E2'},
				{'base': 'AO', 'letters': '\uA734'},
				{'base': 'AU', 'letters': '\uA736'},
				{'base': 'AV', 'letters': '\uA738\uA73A'},
				{'base': 'AY', 'letters': '\uA73C'},
				{'base': 'B', 'letters': '\u0042\u24B7\uFF22\u1E02\u1E04\u1E06\u0243\u0182\u0181'},
				{'base': 'C', 'letters': '\u0043\u24B8\uFF23\u0106\u0108\u010A\u010C\u00C7\u1E08\u0187\u023B\uA73E'},
				{
					'base': 'D',
					'letters': '\u0044\u24B9\uFF24\u1E0A\u010E\u1E0C\u1E10\u1E12\u1E0E\u0110\u018B\u018A\u0189\uA779'
				},
				{'base': 'DZ', 'letters': '\u01F1\u01C4'},
				{'base': 'Dz', 'letters': '\u01F2\u01C5'},
				{
					'base': 'E',
					'letters': '\u0045\u24BA\uFF25\u00C8\u00C9\u00CA\u1EC0\u1EBE\u1EC4\u1EC2\u1EBC\u0112\u1E14\u1E16\u0114\u0116\u00CB\u1EBA\u011A\u0204\u0206\u1EB8\u1EC6\u0228\u1E1C\u0118\u1E18\u1E1A\u0190\u018E'
				},
				{'base': 'F', 'letters': '\u0046\u24BB\uFF26\u1E1E\u0191\uA77B'},
				{
					'base': 'G',
					'letters': '\u0047\u24BC\uFF27\u01F4\u011C\u1E20\u011E\u0120\u01E6\u0122\u01E4\u0193\uA7A0\uA77D\uA77E'
				},
				{
					'base': 'H',
					'letters': '\u0048\u24BD\uFF28\u0124\u1E22\u1E26\u021E\u1E24\u1E28\u1E2A\u0126\u2C67\u2C75\uA78D'
				},
				{
					'base': 'I',
					'letters': '\u0049\u24BE\uFF29\u00CC\u00CD\u00CE\u0128\u012A\u012C\u0130\u00CF\u1E2E\u1EC8\u01CF\u0208\u020A\u1ECA\u012E\u1E2C\u0197'
				},
				{'base': 'J', 'letters': '\u004A\u24BF\uFF2A\u0134\u0248'},
				{
					'base': 'K',
					'letters': '\u004B\u24C0\uFF2B\u1E30\u01E8\u1E32\u0136\u1E34\u0198\u2C69\uA740\uA742\uA744\uA7A2'
				},
				{
					'base': 'L',
					'letters': '\u004C\u24C1\uFF2C\u013F\u0139\u013D\u1E36\u1E38\u013B\u1E3C\u1E3A\u0141\u023D\u2C62\u2C60\uA748\uA746\uA780'
				},
				{'base': 'LJ', 'letters': '\u01C7'},
				{'base': 'Lj', 'letters': '\u01C8'},
				{'base': 'M', 'letters': '\u004D\u24C2\uFF2D\u1E3E\u1E40\u1E42\u2C6E\u019C'},
				{
					'base': 'N',
					'letters': '\u004E\u24C3\uFF2E\u01F8\u0143\u00D1\u1E44\u0147\u1E46\u0145\u1E4A\u1E48\u0220\u019D\uA790\uA7A4'
				},
				{'base': 'NJ', 'letters': '\u01CA'},
				{'base': 'Nj', 'letters': '\u01CB'},
				{
					'base': 'O',
					'letters': '\u004F\u24C4\uFF2F\u00D2\u00D3\u00D4\u1ED2\u1ED0\u1ED6\u1ED4\u00D5\u1E4C\u022C\u1E4E\u014C\u1E50\u1E52\u014E\u022E\u0230\u00D6\u022A\u1ECE\u0150\u01D1\u020C\u020E\u01A0\u1EDC\u1EDA\u1EE0\u1EDE\u1EE2\u1ECC\u1ED8\u01EA\u01EC\u00D8\u01FE\u0186\u019F\uA74A\uA74C'
				},
				{'base': 'OI', 'letters': '\u01A2'},
				{'base': 'OO', 'letters': '\uA74E'},
				{'base': 'OU', 'letters': '\u0222'},
				{'base': 'P', 'letters': '\u0050\u24C5\uFF30\u1E54\u1E56\u01A4\u2C63\uA750\uA752\uA754'},
				{'base': 'Q', 'letters': '\u0051\u24C6\uFF31\uA756\uA758\u024A'},
				{
					'base': 'R',
					'letters': '\u0052\u24C7\uFF32\u0154\u1E58\u0158\u0210\u0212\u1E5A\u1E5C\u0156\u1E5E\u024C\u2C64\uA75A\uA7A6\uA782'
				},
				{
					'base': 'S',
					'letters': '\u0053\u24C8\uFF33\u1E9E\u015A\u1E64\u015C\u1E60\u0160\u1E66\u1E62\u1E68\u0218\u015E\u2C7E\uA7A8\uA784'
				},
				{
					'base': 'T',
					'letters': '\u0054\u24C9\uFF34\u1E6A\u0164\u1E6C\u021A\u0162\u1E70\u1E6E\u0166\u01AC\u01AE\u023E\uA786'
				},
				{'base': 'TZ', 'letters': '\uA728'},
				{
					'base': 'U',
					'letters': '\u0055\u24CA\uFF35\u00D9\u00DA\u00DB\u0168\u1E78\u016A\u1E7A\u016C\u00DC\u01DB\u01D7\u01D5\u01D9\u1EE6\u016E\u0170\u01D3\u0214\u0216\u01AF\u1EEA\u1EE8\u1EEE\u1EEC\u1EF0\u1EE4\u1E72\u0172\u1E76\u1E74\u0244'
				},
				{'base': 'V', 'letters': '\u0056\u24CB\uFF36\u1E7C\u1E7E\u01B2\uA75E\u0245'},
				{'base': 'VY', 'letters': '\uA760'},
				{'base': 'W', 'letters': '\u0057\u24CC\uFF37\u1E80\u1E82\u0174\u1E86\u1E84\u1E88\u2C72'},
				{'base': 'X', 'letters': '\u0058\u24CD\uFF38\u1E8A\u1E8C'},
				{
					'base': 'Y',
					'letters': '\u0059\u24CE\uFF39\u1EF2\u00DD\u0176\u1EF8\u0232\u1E8E\u0178\u1EF6\u1EF4\u01B3\u024E\u1EFE'
				},
				{
					'base': 'Z',
					'letters': '\u005A\u24CF\uFF3A\u0179\u1E90\u017B\u017D\u1E92\u1E94\u01B5\u0224\u2C7F\u2C6B\uA762'
				},
				{
					'base': 'a',
					'letters': '\u0061\u24D0\uFF41\u1E9A\u00E0\u00E1\u00E2\u1EA7\u1EA5\u1EAB\u1EA9\u00E3\u0101\u0103\u1EB1\u1EAF\u1EB5\u1EB3\u0227\u01E1\u00E4\u01DF\u1EA3\u00E5\u01FB\u01CE\u0201\u0203\u1EA1\u1EAD\u1EB7\u1E01\u0105\u2C65\u0250'
				},
				{'base': 'aa', 'letters': '\uA733'},
				{'base': 'ae', 'letters': '\u00E6\u01FD\u01E3'},
				{'base': 'ao', 'letters': '\uA735'},
				{'base': 'au', 'letters': '\uA737'},
				{'base': 'av', 'letters': '\uA739\uA73B'},
				{'base': 'ay', 'letters': '\uA73D'},
				{'base': 'b', 'letters': '\u0062\u24D1\uFF42\u1E03\u1E05\u1E07\u0180\u0183\u0253'},
				{
					'base': 'c',
					'letters': '\u0063\u24D2\uFF43\u0107\u0109\u010B\u010D\u00E7\u1E09\u0188\u023C\uA73F\u2184'
				},
				{
					'base': 'd',
					'letters': '\u0064\u24D3\uFF44\u1E0B\u010F\u1E0D\u1E11\u1E13\u1E0F\u0111\u018C\u0256\u0257\uA77A'
				},
				{'base': 'dz', 'letters': '\u01F3\u01C6'},
				{
					'base': 'e',
					'letters': '\u0065\u24D4\uFF45\u00E8\u00E9\u00EA\u1EC1\u1EBF\u1EC5\u1EC3\u1EBD\u0113\u1E15\u1E17\u0115\u0117\u00EB\u1EBB\u011B\u0205\u0207\u1EB9\u1EC7\u0229\u1E1D\u0119\u1E19\u1E1B\u0247\u025B\u01DD'
				},
				{'base': 'f', 'letters': '\u0066\u24D5\uFF46\u1E1F\u0192\uA77C'},
				{
					'base': 'g',
					'letters': '\u0067\u24D6\uFF47\u01F5\u011D\u1E21\u011F\u0121\u01E7\u0123\u01E5\u0260\uA7A1\u1D79\uA77F'
				},
				{
					'base': 'h',
					'letters': '\u0068\u24D7\uFF48\u0125\u1E23\u1E27\u021F\u1E25\u1E29\u1E2B\u1E96\u0127\u2C68\u2C76\u0265'
				},
				{'base': 'hv', 'letters': '\u0195'},
				{
					'base': 'i',
					'letters': '\u0069\u24D8\uFF49\u00EC\u00ED\u00EE\u0129\u012B\u012D\u00EF\u1E2F\u1EC9\u01D0\u0209\u020B\u1ECB\u012F\u1E2D\u0268\u0131'
				},
				{'base': 'j', 'letters': '\u006A\u24D9\uFF4A\u0135\u01F0\u0249'},
				{
					'base': 'k',
					'letters': '\u006B\u24DA\uFF4B\u1E31\u01E9\u1E33\u0137\u1E35\u0199\u2C6A\uA741\uA743\uA745\uA7A3'
				},
				{
					'base': 'l',
					'letters': '\u006C\u24DB\uFF4C\u0140\u013A\u013E\u1E37\u1E39\u013C\u1E3D\u1E3B\u017F\u0142\u019A\u026B\u2C61\uA749\uA781\uA747'
				},
				{'base': 'lj', 'letters': '\u01C9'},
				{'base': 'm', 'letters': '\u006D\u24DC\uFF4D\u1E3F\u1E41\u1E43\u0271\u026F'},
				{
					'base': 'n',
					'letters': '\u006E\u24DD\uFF4E\u01F9\u0144\u00F1\u1E45\u0148\u1E47\u0146\u1E4B\u1E49\u019E\u0272\u0149\uA791\uA7A5'
				},
				{'base': 'nj', 'letters': '\u01CC'},
				{
					'base': 'o',
					'letters': '\u006F\u24DE\uFF4F\u00F2\u00F3\u00F4\u1ED3\u1ED1\u1ED7\u1ED5\u00F5\u1E4D\u022D\u1E4F\u014D\u1E51\u1E53\u014F\u022F\u0231\u00F6\u022B\u1ECF\u0151\u01D2\u020D\u020F\u01A1\u1EDD\u1EDB\u1EE1\u1EDF\u1EE3\u1ECD\u1ED9\u01EB\u01ED\u00F8\u01FF\u0254\uA74B\uA74D\u0275'
				},
				{'base': 'oi', 'letters': '\u01A3'},
				{'base': 'ou', 'letters': '\u0223'},
				{'base': 'oo', 'letters': '\uA74F'},
				{'base': 'p', 'letters': '\u0070\u24DF\uFF50\u1E55\u1E57\u01A5\u1D7D\uA751\uA753\uA755'},
				{'base': 'q', 'letters': '\u0071\u24E0\uFF51\u024B\uA757\uA759'},
				{
					'base': 'r',
					'letters': '\u0072\u24E1\uFF52\u0155\u1E59\u0159\u0211\u0213\u1E5B\u1E5D\u0157\u1E5F\u024D\u027D\uA75B\uA7A7\uA783'
				},
				{
					'base': 's',
					'letters': '\u0073\u24E2\uFF53\u00DF\u015B\u1E65\u015D\u1E61\u0161\u1E67\u1E63\u1E69\u0219\u015F\u023F\uA7A9\uA785\u1E9B'
				},
				{
					'base': 't',
					'letters': '\u0074\u24E3\uFF54\u1E6B\u1E97\u0165\u1E6D\u021B\u0163\u1E71\u1E6F\u0167\u01AD\u0288\u2C66\uA787'
				},
				{'base': 'tz', 'letters': '\uA729'},
				{
					'base': 'u',
					'letters': '\u0075\u24E4\uFF55\u00F9\u00FA\u00FB\u0169\u1E79\u016B\u1E7B\u016D\u00FC\u01DC\u01D8\u01D6\u01DA\u1EE7\u016F\u0171\u01D4\u0215\u0217\u01B0\u1EEB\u1EE9\u1EEF\u1EED\u1EF1\u1EE5\u1E73\u0173\u1E77\u1E75\u0289'
				},
				{'base': 'v', 'letters': '\u0076\u24E5\uFF56\u1E7D\u1E7F\u028B\uA75F\u028C'},
				{'base': 'vy', 'letters': '\uA761'},
				{'base': 'w', 'letters': '\u0077\u24E6\uFF57\u1E81\u1E83\u0175\u1E87\u1E85\u1E98\u1E89\u2C73'},
				{'base': 'x', 'letters': '\u0078\u24E7\uFF58\u1E8B\u1E8D'},
				{
					'base': 'y',
					'letters': '\u0079\u24E8\uFF59\u1EF3\u00FD\u0177\u1EF9\u0233\u1E8F\u00FF\u1EF7\u1E99\u1EF5\u01B4\u024F\u1EFF'
				},
				{
					'base': 'z',
					'letters': '\u007A\u24E9\uFF5A\u017A\u1E91\u017C\u017E\u1E93\u1E95\u01B6\u0225\u0240\u2C6C\uA763'
				}
			];
		}

		var diacriticsMap = {};
		for (var i = 0; i < $wnd.defaultDiacriticsRemovalMap.length; i++) {
			var letters = $wnd.defaultDiacriticsRemovalMap[i].letters.split("");
			for (var j = 0; j < letters.length; j++) {
				diacriticsMap[letters[j]] = $wnd.defaultDiacriticsRemovalMap[i].base;
			}
		}
		var letters = str.split("");
		var newStr = "";
		for (var i = 0; i < letters.length; i++) {
			newStr += letters[i] in diacriticsMap ? diacriticsMap[letters[i]] : letters[i];
		}
		return newStr;
	}-*/;

	public static ArrayList<String> getTimezones() {

		return timezones;

	}

	/**
	 * Replacement for String.format in Java
	 *
	 * @param format Source format
	 */
	public static String stringFormat(final String format, final Object... args) {
		final RegExp regex = RegExp.compile("%[a-z]");
		final SplitResult split = regex.split(format);
		final StringBuffer msg = new StringBuffer();
		for (int pos = 0; pos < split.length() - 1; pos += 1) {
			msg.append(split.get(pos));
			msg.append(args[pos].toString());
		}
		msg.append(split.get(split.length() - 1));
		return msg.toString();
	}

	/**
	 * Validates an e-mail address
	 *
	 * @param email
	 * @return
	 */
	public native static boolean isValidEmail(String email) /*-{
		//var reg1 = /(@.*@)|(\.\.)|(@\.)|(\.@)|(^\.)/; // not valid
		//var reg2 = /^.+\@(\[?)[a-zA-Z0-9\-\.]+\.([a-zA-Z]{2,3}|[0-9]{1,3})(\]?)$/; // valid
		//return !reg1.test(email) && reg2.test(email);
		var reg4 = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
		return reg4.test(email);
	}-*/;

	/**
	 * Joins content of any Iterable<String> object into string with specified delimiter.
	 * If null or empty object is provided, empty string is returned.
	 *
	 * @param iterable object we can iterate with string values
	 * @param delimiter string used as values splitter
	 * @return joined string or empty string
	 */
	public static String join(Iterable<String> iterable, String delimiter) {
		if (iterable == null) return "";
		Iterator<String> i = iterable.iterator();
		StringBuilder builder = new StringBuilder();
		// append first
		if (i.hasNext()) builder.append(i.next());
		// append rest
		while (i.hasNext()) {
			builder.append(delimiter).append(i.next());
		}
		if (builder.length() > 0) return builder.toString();
		return "";
	}

	/**
	 * Whether string contains IDs separated by a comma
	 *
	 * @param text
	 * @return
	 */
	static public boolean isStringWithIds(String text) {
		final String IDS_PATTERN = "^[0-9, ]+$";
		RegExp regExp = RegExp.compile(IDS_PATTERN);
		MatchResult matcher = regExp.exec(text);
		return (matcher != null);
	}

	/**
	 * Returns a Set as a List
	 *
	 * @param set Input Set
	 * @return Output List
	 */
	static public <T> ArrayList<T> setToList(Set<T> set) {
		ArrayList<T> list = new ArrayList<T>();
		for (T attr : set) {
			list.add(attr);
		}
		return list;
	}

	/**
	 * Convert string to list
	 *
	 * @param toList string to convert to list
	 * @param delimiter
	 * @return Output List
	 */
	static public ArrayList<String> stringToList(String toList, String delimiter) {
		ArrayList<String> list = new ArrayList<String>();
		if (toList != null && !toList.isEmpty()) {
			String[] names = toList.split(delimiter);
			for (int i = 0; i < names.length; i++) {
				list.add(names[i]);
			}
		}
		return list;
	}

	/**
	 * If passed string is DN of certificate(recognized by "/CN=") then returns only CN part with unescaped chars.
	 * If passed string is not DN of certificate, original string is returned.
	 *
	 * @param toConvert Convert DN of certificate to human readable string
	 * @return CN part of certificate DN in human readable form or original value.
	 */
	static public String convertCertCN(String toConvert) {

		if (toConvert.contains("/CN=")) {
			String[] splitted = toConvert.split("/");
			for (String s : splitted) {
				if (s.startsWith("CN=")) {
					return unescapeDN(s.substring(3));
				}
			}
		}
		return toConvert;

	}

	/**
	 * Replace escaped UTF-8 characters with their actual value.
	 *
	 * @param string string to unescape UTF-8 chars
	 * @return String with all UTF-8 chars in readable form.
	 */
	static public final native String unescapeDN(String string) /*-{
		return decodeURIComponent(string.replace(/\\x/g, '%'))
	}-*/;

	/**
	 * Translate IDP identification (ExtSource name property) into English name of institution.
	 *
	 * @param name ExtSource name from IDP to translate
	 * @return Translated name of IDP or original value if unknown.
	 */
	public static String translateIdp(String name) {

		if (firstTranslationCall) {
			firstTranslationCall = false;
			// overwrite default with custom
			organizationsTranslation.putAll(PerunConfiguration.getCustomIdpTranslations());
		}
		if (organizationsTranslation.get(name) != null) {
			return organizationsTranslation.get(name);
		}
		return name;

	}

	/**
	 * Translate Kerberos realm identification (ExtSource name property) into English name of institution.
	 *
	 * @param name ExtSource name from Kerberos realm to translate
	 * @return Translated name of Kerberos realm or original value if unknown.
	 */
	public static String translateKerberos(String name) {

		if (firstTranslationCall) {
			firstTranslationCall = false;
			// overwrite default with custom
			organizationsTranslation.putAll(PerunConfiguration.getCustomIdpTranslations());
		}
		if (organizationsTranslation.get(name) != null) {
			return organizationsTranslation.get(name);
		}

		return name;

	}


}
