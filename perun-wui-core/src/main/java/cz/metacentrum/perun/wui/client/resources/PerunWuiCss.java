package cz.metacentrum.perun.wui.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Interface for loading common CSS file to Application.
 *
 * Inject styles before loading your application in method onModuleLoad() like:
 *
 * PerunWuiCss.INSTANCE.css().ensureInjected();
 *
 */
public interface PerunWuiCss extends ClientBundle {

	public static final PerunWuiCss INSTANCE =  GWT.create(PerunWuiCss.class);

	@Source("../../../../../../../resources/common/cz/metacentrum/perun/wui/resources/css/PerunWui.css")
	@CssResource.NotStrict
	public CssResource css();

}