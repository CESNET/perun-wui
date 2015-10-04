package cz.metacentrum.perun.wui.registrar.widgets;

import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.Checkbox;
import cz.metacentrum.perun.wui.registrar.widgets.items.Combobox;
import cz.metacentrum.perun.wui.registrar.widgets.items.FromFederation;
import cz.metacentrum.perun.wui.registrar.widgets.items.Header;
import cz.metacentrum.perun.wui.registrar.widgets.items.HtmlComment;
import cz.metacentrum.perun.wui.registrar.widgets.items.Password;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;
import cz.metacentrum.perun.wui.registrar.widgets.items.Radiobox;
import cz.metacentrum.perun.wui.registrar.widgets.items.Selectionbox;
import cz.metacentrum.perun.wui.registrar.widgets.items.SubmitButton;
import cz.metacentrum.perun.wui.registrar.widgets.items.TextArea;
import cz.metacentrum.perun.wui.registrar.widgets.items.TextField;
import cz.metacentrum.perun.wui.registrar.widgets.items.Timezone;
import cz.metacentrum.perun.wui.registrar.widgets.items.Undefined;
import cz.metacentrum.perun.wui.registrar.widgets.items.Username;
import cz.metacentrum.perun.wui.registrar.widgets.items.ValidatedEmail;

/**
 * Created by ondrej on 3.10.15.
 */
public class PerunFormItemGeneratorImpl implements PerunFormItemGenerator {


	private final PerunForm form;

	public PerunFormItemGeneratorImpl(PerunForm form) {
		this.form = form;
	}

	@Override
	public PerunFormItem generatePerunFormItem(ApplicationFormItemData data) {
		switch (data.getFormItem().getType()) {
			case HEADING:
				return new Header(data, form.getLang());
			case TEXTFIELD:
				return new TextField(data, form.getLang());
			case VALIDATED_EMAIL:
				return new ValidatedEmail(data, form.getLang());
			case TEXTAREA:
				return new TextArea(data, form.getLang());
			case USERNAME:
				// TODO - Bug. control only already added perun form items to the form.
				if (formContainsClass(Password.class)) {
					return new Username(data, form.getLang(), false);
				}
				return new Username(data, form.getLang(), true);
			case PASSWORD:
				return new Password(data, form.getLang());
			case TIMEZONE:
				return new Timezone(data, form.getLang());
			case SELECTIONBOX:
				return new Selectionbox(data, form.getLang());
			case COMBOBOX:
				return new Combobox(data, form.getLang());
			case CHECKBOX:
				return new Checkbox(data, form.getLang());
			case RADIO:
				return new Radiobox(data, form.getLang());
			case FROM_FEDERATION_SHOW:
				return new FromFederation(data, form.getLang(), true);
			case FROM_FEDERATION_HIDDEN:
				return new FromFederation(data, form.getLang(), false);
			case HTML_COMMENT:
				return new HtmlComment(data, form.getLang());
			case SUBMIT_BUTTON:
				return new SubmitButton(data, form.getLang(), form, false);
			case AUTO_SUBMIT_BUTTON:
				// TODO - Bug. do not submit if it is not last item.
				return new SubmitButton(data, form.getLang(), form, true);
			default:
				return new Undefined(data, form.getLang());
		}
	}

	private boolean formContainsClass(Class clazz) {
		for (PerunFormItem item : form.getPerunFormItems()) {
			if (item.getClass().equals(clazz)) {
				return true;
			}
		}
		return false;
	}
}
