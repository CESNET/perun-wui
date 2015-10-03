package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.model.beans.ApplicationFormItem;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.pages.FormView;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.form_items.PerunFormItem;

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
			if (ApplicationFormItem.ApplicationFormItemType.VALIDATED_EMAIL.equals(item.getItem().getFormItem().getType())) {
				if (item.getValidator().getReturnCode() == 12) {
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
