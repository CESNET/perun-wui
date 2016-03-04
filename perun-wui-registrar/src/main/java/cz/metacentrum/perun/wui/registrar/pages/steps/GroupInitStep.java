package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;

/**
 * Represents group initial application form step.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class GroupInitStep extends FormStep {

	public GroupInitStep(RegistrarObject registrar, PerunForm form) {
		super(registrar, form);
		this.result = new Result(Type.GROUP, registrar.getGroup(), registrar.hasGroupFormAutoApproval());
	}

	@Override
	public void call(final PerunPrincipal pp, Summary summary, final Events<Result> events) {

		events.onLoadingStart();

		if (registrar.getGroupFormInitialException() != null) {
			result.setException(registrar.getGroupFormInitialException());
			events.onFinished(getResult());
			return;
		}

		form.setFormItems(registrar.getGroupFormInitial());

		if (!form.containsSubmitButton()) {
			PerunException ex = PerunException.createNew("0", "FormWrongFormedException", "Registration group form is wrong formed.");
			result.setException(ex);
			events.onError(ex);
			return;
		}

		form.setApp(Application.createNew(registrar.getVo(), registrar.getGroup(), Application.ApplicationType.INITIAL,
				getFedInfo(pp), pp.getActor(), pp.getExtSource(), pp.getExtSourceType(), pp.getExtSourceLoa(), pp.getUser()));

		form.setOnSubmitEvent(getOnSubmitEvent(events));

		form.performAutoSubmit();
	}

}
