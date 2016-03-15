package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;

/**
 * Represents VO extension application form step.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class VoExtStep extends FormStep {

	public VoExtStep(RegistrarObject registrar, PerunForm form) {
		super(registrar, form);
		this.result = new Result(Type.VO_EXT, registrar.getVo(), registrar.hasVoFormAutoApprovalExtension());
	}

	@Override
	public void call(final PerunPrincipal pp, Summary summary, final Events<Result> events) {

		events.onLoadingStart();

		if (registrar.getVoFormExtensionException() != null) {
			result.setException(registrar.getVoFormExtensionException());
			events.onFinished(getResult());
			return;
		}

		form.setFormItems(registrar.getVoFormExtension());

		if (!form.containsSubmitButton()) {
			PerunException ex = PerunException.createNew("0", "FormWrongFormedException", "Extension form is wrong formed.");
			result.setException(ex);
			events.onError(ex);
			return;
		}

		form.setApp(Application.createNew(registrar.getVo(), null, Application.ApplicationType.EXTENSION,
				getFedInfo(pp), pp.getActor(), pp.getExtSource(), pp.getExtSourceType(), pp.getExtSourceLoa(), pp.getUser()));

		form.setOnSubmitEvent(getOnSubmitEvent(events));

		form.performAutoSubmit();

	}

}
