package cz.metacentrum.perun.wui.userprofile.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunUserProfileResources extends ClientBundle {

	PerunUserProfileResources INSTANCE = GWT.create(PerunUserProfileResources.class);

	interface PerunUserProfileCss extends CssResource {

		String page();

		String pageHeader();

		String webContent();

		String pageWrapper();

		String grid();

		String header();

		String status();

		String preview();

		String checkbox(); // External
	}

	@Source("css/PerunUserProfile.gss")
	PerunUserProfileCss gss();

}
