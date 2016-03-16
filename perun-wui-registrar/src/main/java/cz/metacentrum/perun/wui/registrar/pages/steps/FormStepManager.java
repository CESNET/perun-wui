package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.client.ExceptionResolver;
import cz.metacentrum.perun.wui.registrar.client.ExceptionResolverImpl;
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
    private ExceptionResolver exceptionResolver;

    public FormStepManager(PerunPrincipal pp, FormView formView, Summary summary) {
        this.pp = pp;
        this.formView = formView;
        this.steps = new LinkedList<>();
        this.summary = summary;
        this.exceptionResolver = new ExceptionResolverImpl();
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
                    if (result.getException() != null) {
                        exceptionResolver.resolve(result.getException(), result.getBean());
                        if (!exceptionResolver.isSoft()) {
                            // if exception is hard. show it and stay on form (do nothing).
                            return;
                        }
                    }
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

                exceptionResolver.resolve(result.getException(), result.getBean());
                if (exceptionResolver.isSoft()) {
                    // if exception is soft we show it and continue in process
                    formView.displayException(error, bean);
                    if (result != null) {
                        summary.addResult(result);
                    }
                    formView.getForm().clear();
                    (new SubStep(formView.getForm())).call(pp, summary, this);
                } else {
                    // if exception is hard we show it and stay on form (do nothing).
                    formView.displayException(error, bean);
                }
            }

            @Override
            public void onLoadingStart() {

            }
        });
    }

}
