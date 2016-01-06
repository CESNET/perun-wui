package cz.metacentrum.perun.wui.userprofile.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface UserProfileResources extends ClientBundle {

	UserProfileResources INSTANCE = GWT.create(UserProfileResources.class);

	interface PerunUserProfileCss extends CssResource {

		String page();

		String pageHeader();

		String webContent();

		String pageWrapper();

		String menuAndPageWrapper();

		String grid();

		String header();

		String footer();

		String menu();
	}

	@Source("css/PerunUserProfile.gss")
	PerunUserProfileCss gss();

}
