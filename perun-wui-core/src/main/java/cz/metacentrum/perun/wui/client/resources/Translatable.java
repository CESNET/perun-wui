package cz.metacentrum.perun.wui.client.resources;

/**
 * Interface for translatable pages.
 */
public interface Translatable {

	/**
	 * Any class (widget, page) can change language based on current language.
	 *
	 * Implementation must read EntryPoint.LANG to set proper language app-wide
	 */
	public void changeLanguage();

}