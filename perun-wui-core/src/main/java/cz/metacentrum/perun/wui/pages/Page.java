package cz.metacentrum.perun.wui.pages;

import com.google.gwt.user.client.ui.Widget;

/**
 * Base class defining common methods related to every page and it's content.
 * Every page should extend this class.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public abstract class Page {

	/**
	 * Return TRUE, if tab content is loaded and
	 * can be drawn by draw() method.
	 *
	 * @return TRUE = prepared / FALSE = not prepared
	 */
	public abstract boolean isPrepared();

	/**
	 * Return TRUE is user is authorized to view this
	 * tab. If not, user should be redirected to
	 * NotAuthorizedTabItem.
	 *
	 * @return TRUE if user is authorized to view tab
	 */
	public abstract boolean isAuthorized();

	/**
	 * Pass resize action of window (if any) to the page content.
	 */
	public abstract void onResize();

	/**
	 * Draw whole tab content and loads any additional data.
	 * <p/>
	 * Should be called only once when adding tab to content
	 * widget.
	 *
	 * @return tab content wrapper
	 */
	public abstract Widget draw();

	/**
	 * Return pages wrapper widget without any action.
	 *
	 * @return tab content wrapper
	 */
	public abstract Widget getWidget();

	/**
	 * Perform any necessary action on tab opening,
	 * like setting focus to search inputs etc.
	 * <p/>
	 * It's called every time tab is displayed
	 * in content widget.
	 */
	public abstract void open();

	/**
	 * Get tab's URL based on Context + tab specific parameters.
	 * <p/>
	 * This value can be used to uniquely address
	 * any tab.
	 *
	 * @return URL part after # (representing History.getItem() entry)
	 */
	public abstract String getUrl();

	/**
	 * Switch page content between standard content and help related to this page if any.
	 *
	 * If not implemented, not help id displayed.
	 */
	public abstract void toggleHelp();


}