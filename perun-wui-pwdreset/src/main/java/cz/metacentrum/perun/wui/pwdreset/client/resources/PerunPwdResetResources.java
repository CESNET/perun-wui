package cz.metacentrum.perun.wui.pwdreset.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * Resources for Perun Pwd Reset WUI app.
 *
 * @author Ondřej Velíšek
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface PerunPwdResetResources extends ClientBundle {

	PerunPwdResetResources INSTANCE = GWT.create(PerunPwdResetResources.class);

	interface PerunPwdResetCss extends CssResource {

		String pageWrapper();

		String navbarWrapper();

		String logoWrapper();

		String navbarFix();

		String captcha();

	}

	@Source("css/PerunPwdReset.gss")
	PerunPwdResetCss gss();

	@Source("vsup_multilang.jpg")
	ImageResource getVsupLogo();

}
