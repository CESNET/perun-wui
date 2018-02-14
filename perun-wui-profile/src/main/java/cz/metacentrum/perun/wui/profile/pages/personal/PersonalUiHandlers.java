package cz.metacentrum.perun.wui.profile.pages.personal;

import com.gwtplatform.mvp.client.UiHandlers;
import cz.metacentrum.perun.wui.model.beans.UserExtSource;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface PersonalUiHandlers extends UiHandlers {

    void loadUser();

    void checkEmailRequestPending();

    void updateEmail(String email);

}