package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.pages.FormView;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class FormStepManager implements StepManager {

    private PerunPrincipal pp;
    private FormView formView;

    private Queue<Step> steps;
    private Summary summary;

    public FormStepManager(PerunPrincipal pp, FormView formView, Summary summary) {
        this.pp = pp;
        this.formView = formView;
        this.steps = new LinkedList<>();
        this.summary = summary;
    }

    @Override
    public void addStep(Step step) {
        steps.add(step);
    }

    @Override
    public void begin() {
        formView.hideNotice();
        call(steps.remove());
    }

    private void call(final Step current) {

        formView.getForm().clear();
        current.call(pp, summary, new Events<Result>() {

            @Override
            public void onFinished(Result result) {
                if (result != null) {
                    summary.addResult(result);
                }
                formView.hideNotice();
                call(steps.remove());
            }

            @Override
            public void onError(PerunException error) {
                GeneralObject bean = null;
                Result result = current.getResult();
                if (result != null) {
                    bean = current.getResult().getBean();
                }
                // if exception is soft we can continue in process
                if (formView.displayException(error, bean)) {
                    if (result != null) {
                        summary.addResult(result);
                    }
                    formView.getForm().clear();
                    (new SubStep(formView.getForm())).call(pp, summary, this);
                }
            }

            @Override
            public void onLoadingStart() {

            }
        });
    }

}
