package cz.metacentrum.perun.wui.registrar.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Resources for PerunRegistrar WUI app.
 *
 * @author Ondřej Velíšek
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunRegistrarResources extends ClientBundle {

	PerunRegistrarResources INSTANCE = GWT.create(PerunRegistrarResources.class);

	interface PerunRegistrarCss extends CssResource {

		String page();

		String pageHeader();

		String webContent();

		String pageWrapper();

		String grid();

		String status();

		String preview();

		String checkbox(); // External

		//String footer();

		String navbarWrapper();

		String logoWrapper();

		String navbarFix();
	}

	@Source("css/PerunRegistrar.gss")
	PerunRegistrarCss gss();

}
