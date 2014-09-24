package cz.metacentrum.perun.wui.admin.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.History;
import cz.metacentrum.perun.wui.client.resources.PerunContentManager;
import cz.metacentrum.perun.wui.client.resources.PerunContextListener;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.pages.NotAuthorizedPage;
import cz.metacentrum.perun.wui.pages.NotFoundPage;
import cz.metacentrum.perun.wui.pages.Page;
import cz.metacentrum.perun.wui.admin.pages.perunManagement.VosManagementPage;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.html.Div;

import java.util.ArrayList;

/**
 * Class for content management (displaying pages) based on URLs
 * and passed Page objects.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ContentManager extends Div implements PerunContentManager {

	private NotFoundPage notFoundPage = new NotFoundPage();
	private NotAuthorizedPage notAuthorizedPage = new NotAuthorizedPage();

	private Page displayedPage;
	private ContentManager contentManager = this;
	private PerunContextListener[] contextListeners = new PerunContextListener[1];

	private String lastContext = "";

	private ArrayList<Page> history = new ArrayList<Page>();
	private static boolean changePageActive = false;

	/**
	 * Create instance of content manager, which handle all actions related to changing the page.
	 *
	 * @param contextListeners classes, which listens to the changes of context
	 */
	public ContentManager(PerunContextListener... contextListeners) {

		if (contextListeners != null) this.contextListeners = contextListeners;

	}

	/**
	 * Open page based on browser history (after # tag in URL aka Context).
	 *
	 * If null or empty, default page is loaded based on user's administrative role.
	 * If admin of single entity, then entity detail is shown. If admin of more, selection is offered.
	 *
	 * After page resolution, openPage(Page page) method is called to actually change the content.
	 *
	 * @param sourceContext history tag in URL (after #)
	 */
	public void openPage(final String sourceContext) {

		String context = sourceContext;

		if (sourceContext == null || sourceContext.isEmpty()) {

			if (PerunSession.getInstance().isPerunAdmin()) {
				context = "perun";
			} else if (PerunSession.getInstance().isVoAdmin() || PerunSession.getInstance().isVoObserver()) {
				context = "vo";
			} else if (PerunSession.getInstance().isFacilityAdmin()) {
				context = "facility";
			} else if (PerunSession.getInstance().isGroupAdmin()) {
				context = "group";
			}

			// TODO - open overview page if admin of 1 entity. Open select page if admin of more entities.

		}

		final String finalContext = context;

		Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand() {
			@Override
			public boolean execute() {

				if (changePageActive) return true;

				changePageActive = true;

				if ("perun".equals(finalContext.split("/")[0])) {

					if (finalContext.split("/").length > 1) {

						if ("vos".equals(finalContext.split("/")[1])) {

							openPage(new VosManagementPage(), true);

						} else {

							// not found
							openPage(notFoundPage, true);

						}

					} else {

						// open default perun admin page
						changePageActive = false;
						History.newItem("perun/vos");
						return false;

					}

				} else if ("vo".equals(finalContext.split("/")[0])) {

					// TODO

					if (finalContext.split("/").length > 1) {

						if ("select".equals(finalContext.split("/")[1])) {

							//openPage(new VosManagementPage());

						} else {

							// not found
							openPage(notFoundPage, true);

						}

					} else {

						// open default perun admin page
						changePageActive = false;
						History.newItem("vo/select");
						return false;

					}

				} else if ("group".equals(finalContext.split("/")[0])) {

					// TODO

					openPage(notFoundPage, true);

				} else if ("facility".equals(finalContext.split("/")[0])) {

					// TODO

					openPage(notFoundPage, true);

				} else if ("notfound".equals(finalContext.split("/")[0])) {

					openPage(notFoundPage, true);

				} else if ("notauthorized".equals(finalContext.split("/")[0])) {

					openPage(notAuthorizedPage, true);

				} else if ("settings".equals(finalContext.split("/")[0])) {

				} else if ("logout".equals(finalContext.split("/")[0])) {

				} else {

					// unknown page - redirect to notfound
					openPage(notFoundPage, true);

				}

				return false;

			}
		}, 200);

	}

	/**
	 * Open passed page if it's prepared and user is authorized. Then context is updated to menu.
	 *
	 * @param page page to open
	 */
	public void openPage(final Page page) {
		openPage(page, false);
	}

	/**
	 * Open passed page if it's prepared and user is authorized. Then context is updated to menu.
	 *
	 * @param pageToShow page to open
	 * @param force force opening even when "page change" is active now.
	 */
	public void openPage(final Page pageToShow, final boolean force) {

		// FIXME & TODO - get/store page from history
		// TODO - handle also context to be passed to open() method

		Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand() {
			@Override
			public boolean execute() {

				if (changePageActive && !force) return true;

				changePageActive = true;

				// if already displayed
				if (displayedPage != null && displayedPage.equals(pageToShow)) {

					displayedPage.open();
					displayedPage.onResize();
					changePageActive = false;

				} else if (history.contains(pageToShow)) {

					// get page from history if possible
					for (Page p : history) {
						if (p.equals(pageToShow)) {
							contentManager.clear();
							displayedPage = p;
							contentManager.add(displayedPage.getWidget());
							displayedPage.open();
							displayedPage.onResize();
							changePageActive = false;

							// update context by page
							for (PerunContextListener listener : contextListeners) {
								listener.setContext(pageToShow.getUrl());
							}

							break;
						}
					}

				} else {

					if (pageToShow.isAuthorized()) {

						// display page when all data all loaded (check every 200ms)
						Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand() {

							boolean firstRun = true;

							@Override
							public boolean execute() {

								// wait for loading data
								if (!pageToShow.isPrepared()) {
									if (firstRun) {
										PerunLoader loader = new PerunLoader();
										contentManager.clear();
										contentManager.add(loader);
										loader.onLoading();
										firstRun = false;
									}
									return true;
								}

								// data loaded
								displayedPage = pageToShow;
								contentManager.clear();
								contentManager.add(pageToShow.draw());
								displayedPage.open();
								displayedPage.onResize();

								// update context by page
								for (PerunContextListener listener : contextListeners) {
									listener.setContext(pageToShow.getUrl());
								}

								history.add(pageToShow);
								changePageActive = false;

								return false;

							}
						}, 200);

					} else {

						// not authorized to view page
						displayedPage = notAuthorizedPage;
						contentManager.clear();
						contentManager.add(displayedPage.draw());
						displayedPage.open();
						displayedPage.onResize();
						changePageActive = false;

					}

				}

				return false;

			}
		}, 200);

	}

	/**
	 * Gets currently displayed page or null.
	 *
	 * @return displayed page
	 */
	public Page getDisplayedPage() {
		return displayedPage;
	}

	/**
	 * Pass GUI resize action to displayed page to adjust it contents.
	 */
	public void onResize() {

		if (getDisplayedPage() != null) {
			getDisplayedPage().onResize();
		}

	}

}