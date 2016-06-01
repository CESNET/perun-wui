package cz.metacentrum.perun.wui.profile.pages;

import com.google.gwt.core.client.JavaScriptObject;
import com.gwtplatform.mvp.client.UiHandlers;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.model.beans.UserExtSource;
import cz.metacentrum.perun.wui.widgets.PerunLoader;

import java.util.List;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface IdentitiesUiHandlers extends UiHandlers {

    void loadUserExtSources();
    void removeUserExtSource(UserExtSource userExtSource);
    void addUserExtSource();

}