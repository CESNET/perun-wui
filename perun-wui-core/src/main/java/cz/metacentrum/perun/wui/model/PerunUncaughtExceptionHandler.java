package cz.metacentrum.perun.wui.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.UmbrellaException;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;

import java.util.List;
import java.util.Map;

/**
 * Created by ondrejvelisek on 13.11.15.
 */
public class PerunUncaughtExceptionHandler implements GWT.UncaughtExceptionHandler {

    public static final String EXCEPTION_NAME = "exceptionName";
    public static final String EXCEPTION_MSG = "exceptionMessage";
    public static final String CURRENT_URL = "currentURL";

    @Override
    public void onUncaughtException(Throwable wrapped) {

        Throwable t = unwrapUmbrella(wrapped);

        UrlBuilder builder = currentUrlBuilder()
                .setParameter(EXCEPTION_NAME, t.getClass().getName())
                .setParameter(EXCEPTION_MSG, t.getMessage())
                .setHash(PlaceTokens.ERROR);

        // if URL is already set we dont want to recursively set current URL.
        // Unfortunately it can make inconsistence with other data!
        if (Window.Location.getParameter(CURRENT_URL) == null || Window.Location.getParameter(CURRENT_URL).isEmpty()) {
            builder.setParameter(CURRENT_URL, Window.Location.getHref());
        }

        String url = builder.buildString();

        Window.Location.assign(url);

    }

    private Throwable unwrapUmbrella(Throwable e) {
        if(e instanceof UmbrellaException) {
            UmbrellaException ue = (UmbrellaException) e;
            if(ue.getCauses().size() == 1) {
                return unwrapUmbrella(ue.getCauses().iterator().next());
            }
        }
        return e;
    }

    private UrlBuilder currentUrlBuilder() {

        UrlBuilder builder = new UrlBuilder();
        builder.setProtocol(Window.Location.getProtocol());
        builder.setHost(Window.Location.getHost());
        builder.setPath(Window.Location.getPath());
        for (Map.Entry<String, List<String>> entry : Window.Location.getParameterMap().entrySet()) {
            for (String val : entry.getValue()) {
                builder.setParameter(entry.getKey(), val);
            }
        }

        GWT.log(builder.buildString());

        return builder;

    }
}
