package cz.metacentrum.perun.wui.registrar.pages.steps;

import com.google.gwt.user.client.Window;
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

		if (!form.containsSubmitButton() && !form.containsOnlyTextItems()) {
			PerunException ex = PerunException.createNew("0", "FormWrongFormedException", "Extension form is wrong formed.");
			result.setException(ex);
			events.onError(ex);
			return;
		}

		// If form can't be submitted and user is a member (outside condition), redirect to targetexisting automatically
		if (!form.containsSubmitButton() && form.containsOnlyTextItems() && Window.Location.getParameterMap().containsKey("targetexisting")) {
			Window.Location.assign(Window.Location.getParameter("targetexisting"));
			return;
		}

		form.setApp(Application.createNew(registrar.getVo(), null, Application.ApplicationType.EXTENSION,
				getFedInfo(pp), pp.getActor(), pp.getExtSource(), pp.getExtSourceType(), pp.getExtSourceLoa(), pp.getUser()));

		form.setOnSubmitEvent(getOnSubmitEvent(events));

		form.performAutoSubmit();

	}

}
