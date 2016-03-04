package cz.metacentrum.perun.wui.registrar.pages.steps;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;

/**
 * Step shows only continue button to next step. Usefull to show exception.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class SubStep implements Step {

    private PerunForm form;

    public SubStep(PerunForm form) {
        this.form = form;
    }

    @Override
    public void call(PerunPrincipal pp, Summary summary, final Events<Result> events) {

        PerunButton continueButton = PerunButton.getButton(PerunButtonType.CONTINUE, new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                events.onFinished(null);
            }
        });

        form.add(continueButton);

    }

    @Override
    public Result getResult() {
        return null;
    }
}
