package cz.metacentrum.perun.wui.json;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Interface for anonymous classes, which are used as event handlers for
 * callbacks to Perun's API. Each callback can have events processed when
 * callback starts and when finishes (ok or error).
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface JsonEvents extends Events<JavaScriptObject> {

}