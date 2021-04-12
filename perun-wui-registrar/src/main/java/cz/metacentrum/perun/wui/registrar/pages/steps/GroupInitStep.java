package cz.metacentrum.perun.wui.registrar.pages.steps;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AuthzManager;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
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

	PerunPrincipal localPP;

	public GroupInitStep(RegistrarObject registrar, PerunForm form) {
		super(registrar, form);
		this.result = new Result(Type.GROUP_INIT, registrar.getGroup(), registrar.hasGroupFormAutoApproval());
	}

	@Override
	public void call(final PerunPrincipal pp, Summary summary, final Events<Result> events) {

		events.onLoadingStart();
		this.localPP = pp;

		// if there is a VoInitForm then there should be previous step where user submitted VO init application
		// it it`s in an auto-approval mode, then User might have been created and we want to reflect this in
		// group application, hence we will forcefully reload its form and session info.
		if (!registrar.getVoFormInitial().isEmpty() &&
				registrar.getVoFormInitialException() == null &&
				registrar.hasVoFormAutoApproval()) {

			reloadGroupAppData(new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					// continue after all is loaded
					continueWithForm(events);
				}

				@Override
				public void onError(PerunException error) {

				}

				@Override
				public void onLoadingStart() {

				}
			});
		} else {
			// no group reloading
			continueWithForm(events);
		}

	}

	/**
	 * Finish form processing (previously part of call() method).
	 * Called after group form is refreshed.
	 *
	 * @param events events of original call to continue with
	 */
	private void continueWithForm(final Events<Result> events) {

		if (registrar.getGroupFormInitialException() != null) {
			result.setException(registrar.getGroupFormInitialException());
			events.onFinished(getResult());
			return;
		}

		form.setFormItems(registrar.getGroupFormInitial());

		if (!form.containsSubmitButton() && !form.containsOnlyTextItems()) {
			PerunException ex = PerunException.createNew("0", "FormWrongFormedException", "Group registration form is wrongly formed.");
			result.setException(ex);
			events.onError(ex);
			return;
		}

		form.setApp(Application.createNew(registrar.getVo(), registrar.getGroup(), Application.ApplicationType.INITIAL,
				getFedInfo(localPP, Window.Location.getParameter("targetnew")), localPP.getActor(), localPP.getExtSource(), localPP.getExtSourceType(), localPP.getExtSourceLoa(), localPP.getUser()));

		form.setOnSubmitEvent(getOnSubmitEvent(events));

		form.performAutoSubmit();

	}

	/**
	 * In some cases we want to reload form data
	 */
	private void reloadGroupAppData(JsonEvents events) {

		AuthzManager.getPerunPrincipal(new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				localPP = result.cast();

				// since this is a group initi form we know that vo and group are not null.
				RegistrarManager.initializeRegistrar(registrar.getVo().getShortName(), registrar.getGroup().getName(), new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject result) {
						RegistrarObject ro = result.cast();
						registrar.setGroupFormInitial(ro.getGroupFormInitial());
						registrar.setGroupFormInitialException(ro.getGroupFormInitialException());
						// continue with the step
						events.onFinished(null);
					}

					@Override
					public void onError(PerunException error) {
						// ignore and continue with the step
						events.onFinished(null);
					}

					@Override
					public void onLoadingStart() {
					}
				});

			}

			@Override
			public void onError(PerunException error) {
				// ignore it and continue with default form init processing
				events.onFinished(null);
			}

			@Override
			public void onLoadingStart() {
			}
		});


	}

}
