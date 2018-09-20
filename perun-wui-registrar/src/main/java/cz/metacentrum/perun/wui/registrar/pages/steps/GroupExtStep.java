package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;

/**
 * Represents group extension application form step.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class GroupExtStep extends FormStep {

	public GroupExtStep(RegistrarObject registrar, PerunForm form) {
		super(registrar, form);
		this.result = new Result(Type.GROUP_EXT, registrar.getGroup(), registrar.hasGroupFormAutoApprovalExtension());
	}

	@Override
	public void call(final PerunPrincipal pp, Summary summary, final Events<Result> events) {

		events.onLoadingStart();

		if (registrar.getGroupFormExtensionException() != null) {
			result.setException(registrar.getGroupFormExtensionException());
			events.onFinished(getResult());
			return;
		}

		form.setFormItems(registrar.getGroupFormExtension());

		if (!form.containsSubmitButton()) {
			PerunException ex = PerunException.createNew("0", "FormWrongFormedException", "Group extension form is wrongly formed.");
			result.setException(ex);
			events.onError(ex);
			return;
		}

		form.setApp(Application.createNew(registrar.getVo(), registrar.getGroup(), Application.ApplicationType.EXTENSION,
				getFedInfo(pp), pp.getActor(), pp.getExtSource(), pp.getExtSourceType(), pp.getExtSourceLoa(), pp.getUser()));

		form.setOnSubmitEvent(getOnSubmitEvent(events));

		form.performAutoSubmit();
	}

}
