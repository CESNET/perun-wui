package cz.metacentrum.perun.wui.registrar.widgets.form_items;

import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;

/**
 * Created by ondrej on 3.10.15.
 */
public class PerunItemGeneratorImpl implements PerunFormItemGenerator {


	private final PerunForm form;

	public PerunItemGeneratorImpl(PerunForm form) {
		this.form = form;
	}

	@Override
	public PerunFormItem generatePerunFormItem(ApplicationFormItemData data) {
		switch (data.getFormItem().getType()) {
			case TEXTFIELD:
				return new TextField(data, form.getLang());
			case HTML_COMMENT:
				return new HtmlComment(data, form.getLang());
			default:
				return null;
		}
	}
}
