package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.registrar.pages.FormView;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;

/**
 * Represents a question step in a registration process.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public abstract class OfferStep implements Step {

	protected FormView formView;
	protected PerunForm form;
	protected Step yes;
	protected Step no;

	public OfferStep(FormView formView, Step yes, Step no) {
		this.formView = formView;
		this.yes = yes;
		this.no = no;
		this.form = formView.getForm();
	}
}
