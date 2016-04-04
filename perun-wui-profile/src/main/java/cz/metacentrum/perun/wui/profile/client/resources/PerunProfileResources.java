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

		String app();

		String logoWrapper();

	}

	@Source("css/PerunProfile.gss")
	PerunProfileCss gss();

}
