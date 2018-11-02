package cz.metacentrum.perun.wui.profile.pages.identities;

import com.gwtplatform.mvp.client.UiHandlers;
import cz.metacentrum.perun.wui.model.beans.UserExtSource;
import cz.metacentrum.perun.wui.profile.model.beans.RichUserExtSource;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface IdentitiesUiHandlers extends UiHandlers {

    void loadUserExtSources();
    void removeUserExtSource(RichUserExtSource userExtSource);
    void addUserExtSource();

}
