package cz.metacentrum.perun.wui.cabinet.pages;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface PublicationsUiHandlers extends UiHandlers {
	void loadPublications();

	void searchPublications(String value);
}