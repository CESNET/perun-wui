package cz.metacentrum.perun.wui.registrar.client;

import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.Utils;

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
		return "Email with verification link will be sent to provided email address.";

	}

	public static String similarUsersFound() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Podobní uživatelé jsou v systému registrováni. Pokud jste to Vy, prokažte se přihlášením pomocí některé z Vašich registrovaných identit.";
		}
		return "Similar user(s) are already registered in system. If it`s you, prove your identity by logging-in using one of the registered identities.";

	}

	public static String similarUsersFoundTitle(int count) {

		if (PerunSession.LOCALE.equals("cs")) {
			if (count > 1) return "Nalezeni podobní uživatelé";
			return "Nalezen podobný uživatel";
		}
		return "Similar user(s) found";

	}

	public static String noThanks() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Nejsem to já";
		}
		return "It's not me";

	}

	public static String byCertificate() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Pomocí certifikátu";
		}
		return "By certificate";

	}

	public static String byIdp() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Pomocí poskytovatele identit";
		}
		return "By identity provider";

	}

	public static String notSelected() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "--- Nevybráno ---";
		}
		return "--- Not selected ---";

	}

	public static String customValue() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "--- Ruční zadání ---";
		}
		return "--- Custom value ---";

	}

	// -------------- SUBMITTED APPS PAGE ------------------------ //

	public static String submittedTitle() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Moje registrace";
		}
		return "My registrations";

	}

	// --------------- EXCEPTIONS -------------------------------- //

	public static String continueButton() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Pokračovat";
		}
		return "Continue";

	}

	public static String alreadyRegistered() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Již jste registrován(a) v ";
		}
		return "You are already registered in ";

	}

	public static String alreadySubmitted(String voOrGroupName) {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Již máte podanou přihlášku do "+voOrGroupName;
		}
		return "You already have submitted registration to "+voOrGroupName;

	}

	public static String visitSubmitted() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Stav podané přihlášky můžete zkontrolovat v části <a href=\""+Window.Location.getHref().split("#")[0]+"#submitted"+"\">"+ Translations.submittedTitle()+"</a>.";
		}
		return "You can check state of your application in <a href=\""+Window.Location.getHref().split("#")[0]+"#submitted"+"\">"+ Translations.submittedTitle()+"</a>.";

	}

	public static String cantExtendMembership() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Nyní nelze členství prodloužit";
		}
		return "You can't extend membership right now";

	}

	public static String cantExtendMembershipOutside() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Prodlužování členství je aktivní pouze určitou dobu před jeho vypršením a nebo poté.";
		}
		return "Membership can be extended only in a short time before membership expiration or after.";

	}

	public static String cantExtendMembershipInsufficientLoa() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Pro prodloužení členství nemáte dostatečně ověřenou identitu (LoA = Level of Assurance). Kontaktujte svého poskytovatele identity ("+ Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())+") a prokažte mu svoji identitu předložením oficiálního dokladu (občanský průkaz, pas). Pokud máte k dispozici jinou identitu s vyšším stupněm ověření, zkuste se přihlásit pomocí ní.";
		}
		return "You don't have required Level of Assurance (LoA) to extend membership. Contact your IDP ("+ Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())+") and prove your identity to him by official ID card (passport, driving license). If you have another identity with higher Level of Assurance, try to use it instead.";

	}

	public static String cantBecomeMember(String voOrGroupName) {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Nemůžete se registrovat do "+voOrGroupName;
		}
		return "You can't register to "+voOrGroupName;

	}

	public static String cantBecomeMemberLoa() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Poskytovatel Vaší identity, kterého jste použili při přihlášení ("+ Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())+") neposkytuje informaci o stupni Vašeho ověření (LoA = Level of Assurance). Prosím požádejte svého poskytovatele identity o zveřejěnní této informace nebo použijte jiného poskytovatele identity.";
		}
		return "<p>The Identity Provider you used to log-in ("+ Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())+") doesn't provide information about your Level of Assurance (LoA). Please ask your IDP to publish such information or use different IDP.";

	}

	public static String cantBecomeMemberInsufficientLoa() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Pro zobrazení registračního formuláře nemáte dostatečně ověřenou identitu (LoA = Level of Assurance). Kontaktujte svého poskytovatele identity ("+ Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())+") a prokažte mu svoji identitu předložením oficiálního dokladu (občanský průkaz, pas). Pokud máte k dispozici jinou identitu s vyšším stupněm ověření, zkuste se přihlásit pomocí ní.";
		}
		return "You don't have required Level of Assurance (LoA) to display registration form. Contact your IDP ("+ Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())+") and prove your identity to him by official ID card (passport, driving license). If you have another identity with higher Level of Assurance, try to use it instead.";

	}

	public static String missingRequiredData() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "<h4>Nemůžete se registrovat</h4><p>Poskytovatel Vaší identity, kterého jste použili při přihlášení ("+ Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())+") neposkytuje ověřená data vyžadovaná registračním formulářem. Prosím požádejte svého poskytovatele identity, aby zveřejnil následující atributy nebo použijte jiného poskytovatele identity.";
		}
		return "<h4>You can't submit the registration</h4><p>The Identity Provider you used to log-in ("+ Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())+") doesn't provide data required by the registration form. Please ask your IDP to publish following attributes or use different IDP.";

	}

	public static String missingAttribute(String attributeName) {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Chybějící atribut: "+attributeName;
		}
		return "Missing attribute: "+attributeName;

	}

	public static String voNotExistsException(String voName) {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Organizace / projekt s názvem '<i>"+voName+"</i>' neexistuje. Zkontrolujte si prosím správnost odkazu v adresním řádku prohlížeče.";
		}
		return "Organization / project with name '<i>"+voName+"</i>' not exist. Please check the address used in a browser.";

	}

	public static String groupNotExistsException(String groupName) {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Skupina s názvem '<i>"+groupName+"</i>' neexistuje. Zkontrolujte si prosím správnost odkazu v adresním řádku prohlížeče.";
		}
		return "Group with name '<i>"+groupName+"</i>' not exist. Please check the address used in a browser.";

	}

	public static String missingVoInURL() {

		if (PerunSession.LOCALE.equals("cs")) {
			return "Není zadána organizace / projekt do kterého se chcete registrovat. V adresním řádku chybí parametr ?vo=[jméno].";
		}
		return "Organization / project you wish to register to is missing in browser's address bar. Please specify it by ?vo=[name].";

	}

}