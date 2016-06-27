package cz.metacentrum.perun.wui.client.resources;

/**
 * Global UI translations for generic Perun Errors.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunErrorTranslation extends PerunTranslation {

	@DefaultMessage("Error was reported")
	public String reportErrorEnd();

	@DefaultMessage("Internal error occurred, your operation can`t be completed. Please report this error.")
	public String internalErrorException();

	@DefaultMessage("You are not authorized to perform this action.")
	public String notAuthorizedCallback();

	@DefaultMessage("<p>Try to <strong>refresh the browser</strong> window and retry.<br />If problem persist, please report it.")
	public String pleaseRefresh();

	@DefaultMessage("Unexpected error occurred, your operation can`t be completed. Please report this error.")
	public String uncatchedException();

	@DefaultMessage("Error in communication with server.")
	public String rpcException();

	@DefaultMessage("Your request is still processing on server. Please refresh your view (table) to see, if it ended up successfully before trying again.")
	public String requestTimeout();

	// GENERIC HTTP ERRORS

	@DefaultMessage("Server responded with HTTP error {0}: {1}")
	public String httpErrorAny(int code, String text);

	@DefaultMessage("Server encounter internal error while processing your request. Please report this error and retry.")
	public String httpError500();

	@DefaultMessage("You are not authorized to server. Your session might have expired. Please refresh the browser window to re-login.")
	public String httpError401or403();

	@DefaultMessage("Server is temporarily unavailable. Please try again later.")
	public String httpError503();

	@DefaultMessage("Server is probably being restarted at the moment. Please try again later.")
	public String httpError404();

	@DefaultMessage("Can`t contact remote server, connection was lost.")
	public String httpError0();

	@DefaultMessage("Cross-site request was blocked by browser.")
	public String httpError0CrossSite();


}