package cz.metacentrum.perun.wui.profile.pages;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ResourcesUiHandlers extends UiHandlers {

	void loadDataForVo(int voId);

	void loadVos();
}
