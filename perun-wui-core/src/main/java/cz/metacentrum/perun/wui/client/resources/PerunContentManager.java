package cz.metacentrum.perun.wui.client.resources;

import cz.metacentrum.perun.wui.pages.Page;

/**
 * Interface for classes managing page displays inside webapp.
 *
 * ContentManager should handle all page changes and resize actions.
 * It also sets context from page to related widgets like menus.
 *
 * @author Pavel Zlámal
 */
public interface PerunContentManager {

	/**
	 * Opens specific page by context (from URL). Implementation must honor:
	 *
	 * - authorization before displaying.
	 * - open page only if it's prepared.
	 * - call Page#draw() on first run.
	 * - call Page.open() every time (page was once opened).
	 *
	 * @param context Context to open page by.
	 */
	public void openPage(String context);

	/**
	 * Opens specific page. Implementation must honor:
	 *
	 * - authorization before displaying.
	 * - open page only if it's prepared.
	 * - call Page#draw() on first run.
	 * - call Page.open() every time (page was once opened).
	 *
	 * @param page Instance of page to display.
	 */
	public void openPage(Page page);

	/**
	 * Opens specific page. Implementation must honor:
	 *
	 * - authorization before displaying.
	 * - open page only if it's prepared.
	 * - call Page#draw() on first run.
	 * - call Page.open() every time (page was once opened).
	 *
	 * @param page Instance of page to display.
	 * @param force TRUE = force opening even if page change is processing / FALSE = default
	 */
	public void openPage(Page page, boolean force);

	/**
	 * Return instance of currently displayed page.
	 *
	 * @return Displayed page or NULL.
	 */
	public Page getDisplayedPage();

	/**
	 * Pass any resize action triggered from EntryPoint class into page so it's content could be adjusted.
	 */
	public void onResize();

}