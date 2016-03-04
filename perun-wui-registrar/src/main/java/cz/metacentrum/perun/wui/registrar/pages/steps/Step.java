package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;

/**
 * Represents one step in a process (e.g. one form in registration process)
 *
 * Steps are managed by StepManager.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface Step {

	/**
	 * Entry point of this step. Call it when you want to begin this step.
	 * If it is not final step you should call call() method of next the step inside this method.
	 *
	 * @param pp
	 * @param summary summary (collection of Results) of previous steps.
	 * @param events callback object to give flow to StepManager.
	 *               onFinished(Result) everything is ok and we can continue. (Result can contains exception which is shown in the Summary step)
	 *               onError(PerunException) show Exception. And with respect to ExceptionResolver (isSoft method) allow user to continue or not
	 *               		(Result does not have to contains exception, so it won't be shown in the Summary step)
	 */
	void call(PerunPrincipal pp, Summary summary, Events<Result> events);

	/**
	 * @return Result of step. Info what happened inside step. Should be called after call() method.
	 */
	Result getResult();

}
