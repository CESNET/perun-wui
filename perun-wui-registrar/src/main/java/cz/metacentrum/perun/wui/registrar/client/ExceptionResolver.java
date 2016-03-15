package cz.metacentrum.perun.wui.registrar.client;

import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.PerunException;

/**
 * Provides info about exception which can be shown to user.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface ExceptionResolver {

	/**
	 * resolve means fill this object with especially texts for for user for exception.
	 * All resolved info about exception is accessible through other method.
	 * This method SHOULD be called before other methods. Otherwise they will return null or info about previous exception.
	 *
	 * @param exception Investigating exception
	 * @param perunBean related bean
	 */
	void resolve(PerunException exception, GeneralObject perunBean);

	/**
	 * Main text info about exception for user.
	 * You SHOULD call {@link #resolve(PerunException, GeneralObject)} method before. Otherwise this will return null.
	 *
	 * @return Main info about exception
	 */
	String getText();

	/**
	 * Some exceptions can fill also some additional text info or null otherwise.
	 * You SHOULD call {@link #resolve(PerunException, GeneralObject)} method before. Otherwise this will return null.
	 *
	 * @return additional info about exception
	 */
	String getSubtext();

	/**
	 * Some exceptions can fill also some additional text info or null otherwise.
	 * You SHOULD call {@link #resolve(PerunException, GeneralObject)} method before. Otherwise this will return null.
	 *
	 * @return Text and subtext as html.
	 */
	String getHTML();

	/**
	 * Exception can be mark as hard or soft. Soft is default and means registration process can continue.
	 * Hard means that It is not worth to continue in process.
	 * You SHOULD call {@link #resolve(PerunException, GeneralObject)} method before. Otherwise this will return null.
	 *
	 * @return true if exception is soft.
	 */
	Boolean isSoft();

	/**
	 * You SHOULD call {@link #resolve(PerunException, GeneralObject)} method before. Otherwise this will return null.
	 *
	 * @return related bean to exception if any. Null otherwise.
     */
	GeneralObject getBean();
}
