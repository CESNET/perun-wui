package cz.metacentrum.perun.wui.admin.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Created by ondrej on 15.7.15.
 */
public interface PerunWuiAdminResources extends ClientBundle {

	PerunWuiAdminResources INSTANCE = GWT.create(PerunWuiAdminResources.class);

	interface PerunWuiAdminCss extends CssResource {

		String grid();

		String pageHeader();

		String page();

		String menuAndPageWrapper();

		String pageWrapper();

		String webContent();

		String menu();

		String header();
	}

	@Source("css/PerunWuiAdmin.gss")
	PerunWuiAdminCss gss();

}
