package cz.metacentrum.perun.wui.setAffiliation.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Perun Set Affiliation app resources
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public interface PerunSetAffiliationResources extends ClientBundle {

	PerunSetAffiliationResources INSTANCE = GWT.create(PerunSetAffiliationResources.class);

	interface PerunSetAffiliationCss extends CssResource {

		String webContent();

		String pageWrapper();

		String page();

		String logoWrapper();

		String mobileContainer();

		String mb1();

		String pl0();

		String mt0();
	}

	@Source("css/PerunSetAffiliation.gss")
	PerunSetAffiliationCss gss();

}
