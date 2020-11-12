package cz.metacentrum.perun.wui.registrar.pages.steps;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;
import cz.metacentrum.perun.wui.registrar.widgets.items.ValidatedEmail;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;

/**
 * Represents form step in a registration process.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public abstract class FormStep implements Step {

	protected RegistrarObject registrar;
	protected PerunForm form;
	protected Result result;

	enum Type {
		GROUP_INIT, GROUP_EXT, VO_INIT, VO_EXT;
	}

	public FormStep(RegistrarObject registrar, PerunForm form) {
		this.form = form;
		this.registrar = registrar;
	}

	@Override
	public Result getResult() {
		return result;
	}

	public RegistrarObject getRegistrar() {
		return registrar;
	}

	public PerunForm getForm() {
		return form;
	}

	protected String getUnknownMail() {
		for (PerunFormItem item : form.getPerunFormItems()) {
			if (item instanceof ValidatedEmail) {
				if (item.getLastValidationResult().equals(PerunFormItemValidator.Result.MUST_VALIDATE_EMAIL)) {
					return item.getValue();
				}
			}
		}
		return null;
	}

	protected String getFedInfo(PerunPrincipal pp) {
		return "{" + " displayName=\"" + pp.getAdditionInformation("displayName")+"\"" + " commonName=\"" + pp.getAdditionInformation("cn")+"\""
				+ " givenName=\"" + pp.getAdditionInformation("givenName")+"\"" + " sureName=\"" + pp.getAdditionInformation("sn")+"\""
				+ " loa=\"" + pp.getAdditionInformation("loa")+"\"" + " mail=\"" + pp.getAdditionInformation("mail")+"\""
				+ " organization=\"" + pp.getAdditionInformation("o")+"\"" + " }";
	}

	protected JsonEvents getOnSubmitEvent(final Events<Result> events) {
		return new JsonEvents() {

			@Override
			public void onFinished(JavaScriptObject jso) {
				String unknownMail = getUnknownMail();
				if (unknownMail != null) {
					result.setRegisteredMail(unknownMail);
				}
				result.setApplication(jso.cast());
				result.setException(null);
				events.onFinished(result);
			}

			@Override
			public void onError(PerunException error) {
				result.setException(error);
				events.onError(error);
			}

			@Override
			public void onLoadingStart() {
			}
		};
	}
}
