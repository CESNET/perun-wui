package cz.metacentrum.perun.wui.registrar.pages.steps;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;
import cz.metacentrum.perun.wui.registrar.pages.FormView;

/**
 * Represents VO initial application form step.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class VoExtStep extends FormStep {

	public VoExtStep(FormView formView, Step next) {
		super(formView, next);
	}

	@Override
	public void call(final PerunPrincipal pp, final RegistrarObject registrar) {

		formView.getNotice().setVisible(false);
		form.clear();
		form.setFormItems(registrar.getVoFormExtension());
		form.setApp(Application.createNew(registrar.getVo(), null, Application.ApplicationType.EXTENSION,
				getFedInfo(pp), pp.getActor(), pp.getExtSource(), pp.getExtSourceType(), pp.getExtSourceLoa(), pp.getUser()));
		form.setOnSubmitEvent(new JsonEvents() {

			@Override
			public void onFinished(JavaScriptObject jso) {
				if (mustRevalidateMail()) {
					formView.setRegisteredUnknownMail();
				}
				formView.getNotice().setVisible(false);
				next.call(pp, registrar);
			}

			@Override
			public void onError(PerunException error) {
				if (formView.resolveException(error)) {
					next.call(pp, registrar);
				}
			}

			@Override
			public void onLoadingStart() {

			}
		});

		form.performAutoSubmit();

	}
}
