package cz.metacentrum.perun.wui.client.resources;

/**
 * Interface for widgets which listens to change
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunContextListener {

	/**
	 * Set specific context (page href) to widget.
	 * This is usually meant for programmatic change of active items etc.
	 *
	 * @param context context to set
	 */
	public void setContext(String context);

}
