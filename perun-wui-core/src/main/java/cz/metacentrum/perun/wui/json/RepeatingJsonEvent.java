package cz.metacentrum.perun.wui.json;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.model.PerunException;

import java.util.List;

/**
 * Interface for JsonEvent that is passed to multiple JsonCalls.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface RepeatingJsonEvent extends JsonEvents {

	void finished(List<JavaScriptObject> results);

	void erred(PerunException exception);

	void started();
}
