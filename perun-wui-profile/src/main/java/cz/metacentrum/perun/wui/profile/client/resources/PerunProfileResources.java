package cz.metacentrum.perun.wui.profile.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Perun Profile app resources
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunProfileResources extends ClientBundle {

	PerunProfileResources INSTANCE = GWT.create(PerunProfileResources.class);

	interface PerunProfileCss extends CssResource {

		// MAIN LAYOUT

		String webContent();

		// TOP MENU

		String navbarWrapper();

		String navbarFix();

		String logoWrapper();

		// MAIN CONTENT

		String menuAndPageWrapper();

		String leftMenu();

		String pageWrapper();

		String page();

		// FOOTER

		//String footer();

		// PAGE CONTENT

		String grid();

	}

	@Source("css/PerunProfile.gss")
	PerunProfileCss gss();

}
