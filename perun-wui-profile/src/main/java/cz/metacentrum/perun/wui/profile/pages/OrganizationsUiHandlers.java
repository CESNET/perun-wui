package cz.metacentrum.perun.wui.profile.pages;

import com.gwtplatform.mvp.client.UiHandlers;
import cz.metacentrum.perun.wui.model.beans.Vo;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public interface OrganizationsUiHandlers extends UiHandlers {

	void extendMembership(Vo vo);

	void loadData();
}
