package cz.metacentrum.perun.wui.admin.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.History;
import cz.metacentrum.perun.wui.client.resources.PerunContextListener;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.pages.NotAuthorizedPage;
import cz.metacentrum.perun.wui.pages.NotFoundPage;
import cz.metacentrum.perun.wui.pages.Page;
import cz.metacentrum.perun.wui.admin.pages.perunManagement.VosManagementPage;
import org.gwtbootstrap3.client.ui.html.Div;

/**
 * Class for content management (displaying pages) based on URLs
 * and passed Page objects.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ContentManager extends Div {

	private NotFoundPage notFoundPage = new NotFoundPage();
	private NotAuthorizedPage notAuthorizedPage = new NotAuthorizedPage();

	private Page displayedPage;
	private ContentManager contentManager = this;
	private PerunContextListener[] contextListeners = new PerunContextListener[1];

	private String lastContext = "";

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
	 * @param context history tag in URL (after #)
	 */
	public void openPage(String context) {

		if (context == null || context.isEmpty()) {

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


		if ("perun".equals(context.split("/")[0])) {

			if (context.split("/").length > 1) {

				if ("vos".equals(context.split("/")[1])) {

					openPage(new VosManagementPage());

				} else {

					// not found
					openPage(notFoundPage);

				}

			} else {

				// open default perun admin page
				History.newItem("perun/vos");
				return;

			}

		} else if ("vo".equals(context.split("/")[0])) {

			// TODO

			if (context.split("/").length > 1) {

				if ("select".equals(context.split("/")[1])) {

					//openPage(new VosManagementPage());

				} else {

					// not found
					openPage(notFoundPage);

				}

			} else {

				// open default perun admin page
				History.newItem("vo/select");
				return;

			}

		} else if ("group".equals(context.split("/")[0])) {

			// TODO

			openPage(notFoundPage);

		} else if ("facility".equals(context.split("/")[0])) {

			// TODO

			openPage(notFoundPage);

		} else if ("notfound".equals(context.split("/")[0])) {

			openPage(notFoundPage);

		} else if ("notauthorized".equals(context.split("/")[0])) {

			openPage(notAuthorizedPage);

		} else if ("settings".equals(context.split("/")[0])) {

		} else if ("logout".equals(context.split("/")[0])) {

		} else {

			// unknown page - redirect to notfound
			openPage(notFoundPage);

		}

	}

	/**
	 * Open passed page if it's prepared and user is authorized. Then context is updated to menu.
	 *
	 * @param page page to open
	 */
	public void openPage(final Page page) {

		// FIXME & TODO - get/store page from history
		// TODO - handle also context to be passed to open() method


		if (displayedPage != null && displayedPage.equals(page)) {

			page.open();
			page.onResize();

		} else {

			if (page.isAuthorized()) {

				// display page when all data all loaded (check every 200ms)
				Scheduler.get().scheduleFixedPeriod(new Scheduler.RepeatingCommand() {
					@Override
					public boolean execute() {

						// wait for loading data
						if (!page.isPrepared()) return true;

						// data loaded
						displayedPage = page;
						contentManager.clear();
						contentManager.add(page.draw());
						page.open();
						page.onResize();

						// update context by page
						for (PerunContextListener listener : contextListeners) {
							listener.setContext(page.getUrl());
						}

						return false;

					}
				}, 200);

			} else {

				// not authorized to view page
				this.displayedPage = notAuthorizedPage;
				this.clear();
				this.add(displayedPage.draw());
				displayedPage.open();
				displayedPage.onResize();

			}

		}

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