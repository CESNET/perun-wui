package cz.metacentrum.perun.wui.consolidator.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Resources for Perun Consolidator WUI app.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunConsolidatorResources extends ClientBundle {

	PerunConsolidatorResources INSTANCE = GWT.create(PerunConsolidatorResources.class);

	interface PerunConsolidatorCss extends CssResource {

		// MAIN LAYOUT

		String webContent();

		// TOP MENU

		String navbarWrapper();

		String navbarFix();

		String logoWrapper();

		// MAIN CONTENT

		String menuAndPageWrapper();

		String pageWrapper();

		String page();

		// FOOTER

		//String footer();

		// PAGE CONTENT

		String grid();

		String wayfGroup();
	}

	@Source("css/PerunConsolidator.gss")
	PerunConsolidatorCss gss();

}
