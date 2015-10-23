package cz.metacentrum.perun.wui.registrar.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Created by ondrej on 15.7.15.
 */
public interface PerunWuiRegistrarResources extends ClientBundle {

	PerunWuiRegistrarResources INSTANCE = GWT.create(PerunWuiRegistrarResources.class);

	interface PerunWuiRegistrarCss extends CssResource {

		String page();

		String pageHeader();

		String webContent();

		String pageWrapper();

		String grid();

		String header();

		String status();
	}

	@Source("css/PerunWuiRegistrar.gss")
	PerunWuiRegistrarCss gss();

}
