package cz.metacentrum.perun.wui.model.common;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;

import java.util.ArrayList;

/**
 * Overlay type for FeedEntities object which represents
 * map of information about each IdP, where key is IdP URL
 * and value is IdP
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FeedEntities extends JavaScriptObject {

	protected FeedEntities() {
	}

	public static FeedEntities createNew() {

		FeedEntities object = new JSONObject().getJavaScriptObject().cast();
		return object;

	}

	/**
	 * Get data about specific IdP
	 *
	 * @param idpUrl String Unique identifier of IdP (URL).
	 * @return Data about IdP
	 */
	public final FeedEntity get(String idpUrl) {
		return JsUtils.getNativePropertyObject(this, idpUrl).cast();
	}

	/**
	 * Set FeedEntity into Entities
	 *
	 * @param idpUrl URL/ID of IdP to set
	 * @param entity IdP Entity to set
	 */
	public final native void set(String idpUrl, FeedEntity entity) /*-{
		this[idpUrl] = entity;
	}-*/;

	/**
	 * Get data about all IdPs
	 *
	 * @return Data about all IdPs
	 */
	public final ArrayList<FeedEntity> getAll() {
		ArrayList<FeedEntity> all = new ArrayList<FeedEntity>();
		for (String name : getKeys()) {
			all.add(get(name));
		}
		return all;
	}

	/**
	 * Return list of all IdPs identifiers
	 *
	 * @return List of all IdPs identifiers
	 */
	public final ArrayList<String> getKeys() {
		return JsUtils.listFromJsArrayString(keys());
	}

	/**
	 * Return array of all IdPs identifiers
	 *
	 * @return Array of all IdPs identifiers
	 */
	private final native JsArrayString keys() /*-{
		var object = this;
		var list = [];
		for (var property in object) {
			if (object.hasOwnProperty(property)) {
				list.push(property);
			}
		}
		list.sort(function(a,b){
			var key1 = "";
			var key2 = "";

			for (var prop in object[a].label) {
				if (object[a].label.hasOwnProperty(prop)) {
					key1 = object[a].label[prop];
				}
			}

			if (object[a].label.hasOwnProperty("fr")) {
				key1 = object[a].label["fr"]
			}
			if (object[a].label.hasOwnProperty("en")) {
				key1 = object[a].label["en"]
			}
			if (object[a].label.hasOwnProperty("cs")) {
				key1 = object[a].label["cs"]
			}

			for (var prop in object[b].label) {
				if (object[b].label.hasOwnProperty(prop)) {
					key2 = object[b].label[prop];
				}
			}
			if (object[b].label.hasOwnProperty("fr")) {
				key2 = object[b].label["fr"]
			}
			if (object[b].label.hasOwnProperty("en")) {
				key2 = object[b].label["en"]
			}
			if (object[b].label.hasOwnProperty("cs")) {
				key2 = object[b].label["cs"]
			}

			return key1.localeCompare(key2);
		});
		return list;
	}-*/;

}
