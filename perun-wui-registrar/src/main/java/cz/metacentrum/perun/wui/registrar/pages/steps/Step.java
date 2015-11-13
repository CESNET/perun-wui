package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;

/**
 * Represents one step in a process (e.g. one form in registration process)
 *
 * Example usage:
 *
 * <pre>
 * {@code
 * (new VoInitStep(formView,
 *      new GroupInitStep(formView,
 *          new Summary(formView, ApplicationType.INITIAL, ApplicationType.INITIAL)
 * ))).call(pp, registrar);
 * }
 * </pre>
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface Step {


	/**
	 * Entry point of this step. Call it when you want to begin this step.
	 * If it is not final step you should call call() method of next the step inside this method.
	 *
	 * @param pp
	 * @param registrar
	 */
	void call(PerunPrincipal pp, RegistrarObject registrar);

}
