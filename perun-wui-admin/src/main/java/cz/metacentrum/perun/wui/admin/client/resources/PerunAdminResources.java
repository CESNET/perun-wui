package cz.metacentrum.perun.wui.admin.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Resources for Perun Admin WUI app.
 *
 * @author Ondřej Velíšek
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunAdminResources extends ClientBundle {

	PerunAdminResources INSTANCE = GWT.create(PerunAdminResources.class);

	interface PerunWuiAdminCss extends CssResource {

		String grid();

		String pageHeader();

		String page();

		String menuAndPageWrapper();

		String pageWrapper();

		String webContent();

		String menu();

		String header();

		String footer();

	}

	@Source("css/PerunAdmin.gss")
	PerunWuiAdminCss gss();

}
