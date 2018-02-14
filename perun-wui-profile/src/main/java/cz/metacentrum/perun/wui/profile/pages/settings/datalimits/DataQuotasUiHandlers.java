package cz.metacentrum.perun.wui.profile.pages.settings.datalimits;

import com.gwtplatform.mvp.client.UiHandlers;
import cz.metacentrum.perun.wui.model.beans.Resource;
import cz.metacentrum.perun.wui.profile.widgets.QuotaType;

public interface DataQuotasUiHandlers extends UiHandlers {

	void loadVos();

	void loadDataForVo(int voId);

	void navigateBack();

	void requestChange(String newValue, String reason, Resource resource, QuotaType quotaType);
}
