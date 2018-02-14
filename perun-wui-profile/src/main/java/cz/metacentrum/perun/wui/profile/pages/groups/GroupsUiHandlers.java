package cz.metacentrum.perun.wui.profile.pages.groups;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface GroupsUiHandlers extends UiHandlers {

	void loadVos();

	void loadDataForVo(int voId);
}
