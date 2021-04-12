package cz.metacentrum.perun.wui.registrar.pages.steps;

import com.google.gwt.user.client.Window;
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

		if (!form.containsSubmitButton() && !form.containsOnlyTextItems()) {
			PerunException ex = PerunException.createNew("0", "FormWrongFormedException", "Group extension form is wrongly formed.");
			result.setException(ex);
			events.onError(ex);
			return;
		}

		// If form can't be submitted and user is a member (outside condition), redirect to targetexisting automatically
		if (!form.containsSubmitButton() && form.containsOnlyTextItems() && Window.Location.getParameterMap().containsKey("targetexisting")) {
			Window.Location.assign(Window.Location.getParameter("targetexisting"));
			return;
		}

		form.setApp(Application.createNew(registrar.getVo(), registrar.getGroup(), Application.ApplicationType.EXTENSION,
				getFedInfo(pp, Window.Location.getParameter("targetextended")), pp.getActor(), pp.getExtSource(), pp.getExtSourceType(), pp.getExtSourceLoa(), pp.getUser()));

		form.setOnSubmitEvent(getOnSubmitEvent(events));

		form.performAutoSubmit();
	}

}
