package cz.metacentrum.perun.wui.client.resources;

/**
 * Global UI translations for generic Perun Errors.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunErrorTranslation extends PerunTranslation {

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



}