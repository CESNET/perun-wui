package cz.metacentrum.perun.wui.profile.pages;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface ResourcesUiHandlers extends UiHandlers {

	void loadDataForVo(int voId);

	void loadVos();
}
