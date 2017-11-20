package cz.metacentrum.perun.wui.profile.pages;

import com.gwtplatform.mvp.client.UiHandlers;

public interface GroupsUiHandlers extends UiHandlers {

	void loadVos();

	void loadDataForVo(int voId);
}
