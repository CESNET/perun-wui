package cz.metacentrum.perun.wui.model.resources;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.view.client.ProvidesKey;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Key provider for all basic model classes in Perun for tables
 *
 * @param <T>
 * @author Vaclav Mach <374430@mail.muni.cz>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunKeyProvider<T extends JavaScriptObject> implements ProvidesKey<T> {

	public Object getKey(T o) {
		// returns ID
		GeneralObject go = (GeneralObject) o;

		// TODO - here must be check for non-standard perunBean classes, which can't use ID (like (Rich)Destination).
		return go.getId();

	}

}