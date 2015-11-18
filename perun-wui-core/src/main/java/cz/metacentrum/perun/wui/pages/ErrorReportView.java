package cz.metacentrum.perun.wui.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.PerunUncaughtExceptionHandler;
import cz.metacentrum.perun.wui.widgets.AlertErrorReporter;

/**
 * Error page.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class ErrorReportView extends ViewImpl implements ErrorReportPresenter.MyView {
    interface ReportErrorUiBinder extends UiBinder<Widget, ErrorReportView> {
    }

    @UiField
    protected AlertErrorReporter alert;

    private PerunTranslation translation = GWT.create(PerunTranslation.class);

    @Inject
    ErrorReportView(final ReportErrorUiBinder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        draw();
    }

    private void draw() {

        alert.setHTML("<h4>" + translation.errorOccured() + "</h4>");

        // Check required attributes or null exception can occure.
        if ((Window.Location.getParameter(PerunUncaughtExceptionHandler.EXCEPTION_NAME) == null)
                || (Window.Location.getParameter(PerunUncaughtExceptionHandler.EXCEPTION_MSG) == null)
                || (Window.Location.getParameter(PerunUncaughtExceptionHandler.CURRENT_URL) == null)) {
            return;
        }

        PerunException exception = PerunException.createNew(
                "WUI_EXCEPTION",
                Window.Location.getParameter(PerunUncaughtExceptionHandler.EXCEPTION_NAME),
                Window.Location.getParameter(PerunUncaughtExceptionHandler.EXCEPTION_MSG));
        exception.setRequestURL(Window.Location.getParameter(PerunUncaughtExceptionHandler.CURRENT_URL));
        exception.setType("WuiException");
        exception.setPostData("{}");

        alert.setReportInfo(exception);
    }

}