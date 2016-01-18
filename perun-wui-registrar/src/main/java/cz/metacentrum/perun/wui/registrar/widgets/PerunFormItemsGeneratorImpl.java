package cz.metacentrum.perun.wui.registrar.widgets;

import com.google.gwt.core.client.GWT;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
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
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunFormItemsGeneratorImpl implements PerunFormItemsGenerator {

	private final PerunForm form;
	private PerunRegistrarTranslation trans = GWT.create(PerunRegistrarTranslation.class);

	public PerunFormItemsGeneratorImpl(PerunForm form) {
		this.form = form;
	}

	@Override
	public List<PerunFormItem> generatePerunFormItems(List<ApplicationFormItemData> items) {

		List<PerunFormItem> perunFormItems = new ArrayList<>();
		int i = 0;
		for (ApplicationFormItemData item : items) {

			if (i != -1) {
				PerunFormItem perunFormItem = generatePerunFormItem(item);
				perunFormItems.add(perunFormItem);
			}

			i++;
		}
		return perunFormItems;
	}

	private PerunFormItem generatePerunFormItem(ApplicationFormItemData data) {

		String lang = PerunConfiguration.getCurrentLocaleName();
		boolean onlyPreview = form.isOnlyPreview();

		switch (data.getFormItem().getType()) {
			case HEADING:
				return new Header(data, lang);
			case TEXTFIELD:
				return new TextField(data, lang, onlyPreview);
			case VALIDATED_EMAIL:
				return new ValidatedEmail(data, lang, onlyPreview);
			case TEXTAREA:
				return new TextArea(data, lang, onlyPreview);
			case USERNAME:
				Username username = new Username(data, lang, onlyPreview);
				if (data.getPrefilledValue() != null && !data.getPrefilledValue().isEmpty()) {
					username.setEnable(false);
				}
				return username;
			case PASSWORD:
				return new Password(data, lang, onlyPreview);
			case TIMEZONE:
				return new Timezone(data, lang, onlyPreview);
			case SELECTIONBOX:
				return new Selectionbox(data, lang, onlyPreview);
			case COMBOBOX:
				return new Combobox(data, lang, onlyPreview);
			case CHECKBOX:
				return new Checkbox(data, lang, onlyPreview);
			case RADIO:
				return new Radiobox(data, lang, onlyPreview);
			case FROM_FEDERATION_SHOW:
				return new FromFederation(data, lang, onlyPreview);
			case FROM_FEDERATION_HIDDEN:
				FromFederation hidden = new FromFederation(data, lang, onlyPreview);
				if (!form.isSeeHiddenItems()) {
					hidden.setVisible(false);
				} else {
					hidden.setStatus(trans.federation(), ValidationState.NONE);
				}
				return hidden;
			case HTML_COMMENT:
				return new HtmlComment(data, lang);
			case SUBMIT_BUTTON:
				return new SubmitButton(data, lang, form, false);
			case AUTO_SUBMIT_BUTTON:
				return new SubmitButton(data, lang, form, true);
			default:
				return new Undefined(data, lang);
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
