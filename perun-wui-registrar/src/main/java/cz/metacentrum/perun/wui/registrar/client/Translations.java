package cz.metacentrum.perun.wui.registrar.client;

/**
 * Class providing dynamic translations to Registrar GUI.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Translations {

	public static String tooLong() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Text je příliš dlouhý!";
		}
		return "Text is too long!";

	}

	public static String incorrectFormat() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Špatný formát vstupu!";
		}
		return "Incorrect input format!";

	}

	public static String cantBeEmpty() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Hodnota nesmí být prázdná!";
		}
		return "Value can`t be empty!";

	}

	public static String cantBeEmptySelect() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Musíte vybrat alesoň jednu možnost!";
		}
		return "You must select at least one option!";

	}

	public static String incorrectEmail() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Neplatný formát e-mailové adresy!";
		}
		return "Incorrect email address format!";

	}

	public static String checkAndSubmit() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Zkontrolovat a odeslat formulář";
		}
		return "Check & submit the form";

	}

	public static String passEmpty() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Heslo nesmí být prázdné!";
		}
		return "Password can't be empty!";

	}

	public static String passMismatch() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Hesla se neshodují!";
		}
		return "Passwords doesn`t match!";

	}

	public static String checkingLogin() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Kontroluji...";
		}
		return "Checking...";

	}

	public static String loginNotAvailable() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Uživatelské jméno není dostupné!";
		}
		return "Login not available!";

	}

	public static String checkingLoginFailed() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Kontrola dostupnosti selhala!";
		}
		return "Unable to check login availability!";

	}

	public static String logoutText() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Byli jste odhlášeni.";
		}
		return "You have been logged out.";

	}

	public static String logoutSubText() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Zavřete prosím okno prohlížeče.";
		}
		return "Please close the browser window.";

	}

	public static String logoutButton() {

		if (PerunRegistrar.LOCALE.equals("cs")) {
			return "Přihlásit se zpět";
		}
		return "Log me back";

	}

}