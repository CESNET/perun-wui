package cz.metacentrum.perun.wui.profile.pages.identities;

import com.gwtplatform.mvp.client.UiHandlers;
import cz.metacentrum.perun.wui.model.beans.UserExtSource;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface IdentitiesUiHandlers extends UiHandlers {

    void loadUserExtSources();
    void removeUserExtSource(UserExtSource userExtSource);
    void addUserExtSource();

}