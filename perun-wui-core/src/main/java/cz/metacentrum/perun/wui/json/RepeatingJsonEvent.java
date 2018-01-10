package cz.metacentrum.perun.wui.json;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.model.PerunException;

import java.util.List;

/**
 * Interface for JsonEvent that is passed to multiple JsonCalls.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface RepeatingJsonEvent extends JsonEvents {

	/**
	 * This method is called when the last call is finished.
	 *
	 * @param results List of results from each call
	 */
	void done(List<JavaScriptObject> results);

	/**
	 * This method is called when the first error occurs. It is called only
	 * by the first error. Other errors are ignored.
	 *
	 * @param exception esception
	 */
	void erred(PerunException exception);

	/**
	 * This method is called before the first call.
	 */
	void started();
}
