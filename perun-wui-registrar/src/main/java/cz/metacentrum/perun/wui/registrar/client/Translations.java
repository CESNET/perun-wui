package cz.metacentrum.perun.wui.registrar.client;

import cz.metacentrum.perun.wui.client.resources.PerunSession;

/**
 * Class providing dynamic translations to Registrar GUI.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Translations {

	public static String tooLong() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Text je příliš dlouhý!";
		}
		return "Text is too long!";

	}

	public static String incorrectFormat() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Špatný formát vstupu!";
		}
		return "Incorrect input format!";

	}

	public static String cantBeEmpty() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Hodnota nesmí být prázdná!";
		}
		return "Value can`t be empty!";

	}

	public static String cantBeEmptySelect() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Musíte vybrat alesoň jednu možnost!";
		}
		return "You must select at least one option!";

	}

	public static String incorrectEmail() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Neplatný formát e-mailové adresy!";
		}
		return "Incorrect email address format!";

	}

	public static String checkAndSubmit() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Zkontrolovat a odeslat formulář";
		}
		return "Check & submit the form";

	}

	public static String passEmpty() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Heslo nesmí být prázdné!";
		}
		return "Password can't be empty!";

	}

	public static String passMismatch() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Hesla se neshodují!";
		}
		return "Passwords doesn`t match!";

	}

	public static String checkingLogin() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Kontroluji...";
		}
		return "Checking...";

	}

	public static String loginNotAvailable() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Uživatelské jméno není dostupné!";
		}
		return "Login not available!";

	}

	public static String checkingLoginFailed() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Kontrola dostupnosti selhala!";
		}
		return "Unable to check login availability!";

	}

	public static String logoutText() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Byli jste odhlášeni.";
		}
		return "You have been logged out.";

	}

	public static String logoutSubText() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Zavřete prosím okno prohlížeče.";
		}
		return "Please close the browser window.";

	}

	public static String logoutButton() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Přihlásit se zpět";
		}
		return "Log me back";

	}

	public static String customValueEmail() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "--- Jiný ---";
		}
		return "--- Custom ---";

	}

	public static String mustValidateEmail() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Na zadanou adresu bude zaslán email s odkazem pro ověření.";
		}
		return "Email with verifiaction link will be sent to provided email address.";

	}

	// -------------- SUBMITTED APPS PAGE ------------------------ //

	public static String submittedTitle() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Moje registrace";
		}
		return "My registrations";

	}

}