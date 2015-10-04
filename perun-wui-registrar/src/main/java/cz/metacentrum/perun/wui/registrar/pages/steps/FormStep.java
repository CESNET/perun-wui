package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.pages.FormView;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;
import cz.metacentrum.perun.wui.registrar.widgets.items.ValidatedEmail;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;

/**
 * Created by ondrej on 3.10.15.
 */
public abstract class FormStep implements Step {

	protected FormView formView;
	protected PerunForm form;
	protected Step next;

	public FormStep(FormView formView, Step next) {
		this.formView = formView;
		this.next = next;
		this.form = formView.getForm();
	}

	protected boolean mustRevalidateMail() {
		for (PerunFormItem item : form.getPerunFormItems()) {
			if (item instanceof ValidatedEmail) {
				if (item.getLastValidationResult().equals(PerunFormItemValidator.Result.MUST_VALIDATE_EMAIL)) {
					return true;
				}
			}
		}
		return false;
	}

	protected String getFedInfo(PerunPrincipal pp) {
		return "{" + " displayName=\"" + pp.getAdditionInformation("displayName")+"\"" + " commonName=\"" + pp.getAdditionInformation("cn")+"\""
				+ " givenName=\"" + pp.getAdditionInformation("givenName")+"\"" + " sureName=\"" + pp.getAdditionInformation("sn")+"\""
				+ " loa=\"" + pp.getAdditionInformation("loa")+"\"" + " mail=\"" + pp.getAdditionInformation("mail")+"\""
				+ " organization=\"" + pp.getAdditionInformation("o")+"\"" + " }";
	}
}
