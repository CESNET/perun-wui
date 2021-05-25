package cz.metacentrum.perun.wui.registrar.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItem;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemTexts;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.widgets.items.Checkbox;
import cz.metacentrum.perun.wui.registrar.widgets.items.Combobox;
import cz.metacentrum.perun.wui.registrar.widgets.items.GroupCheckBox;
import cz.metacentrum.perun.wui.registrar.widgets.items.Header;
import cz.metacentrum.perun.wui.registrar.widgets.items.HtmlComment;
import cz.metacentrum.perun.wui.registrar.widgets.items.Password;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItemEditable;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

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
		Map<Integer, ApplicationFormItemData> formItemsByIds = items.stream()
//				.peek(value -> GWT.log(String.valueOf(value.getFormItem().getId())))
				.collect(toMap(item -> item.getFormItem().getId(), Function.identity()));


		List<PerunFormItem> perunFormItems = new ArrayList<>();
		int i = 0;
		for (ApplicationFormItemData item : items) {

			if (i != -1) {
				PerunFormItem perunFormItem = generatePerunFormItem(item);
				if (isItemHidden(perunFormItem, formItemsByIds)) {
					perunFormItem.setVisible(false);
					if (perunFormItem instanceof PerunFormItemEditable) {
						((PerunFormItemEditable) perunFormItem).setRawStatus("", ValidationState.NONE);
					}
				}
				if (isItemDisabled(perunFormItem, formItemsByIds)) {
					if (perunFormItem instanceof PerunFormItemEditable) {
						((PerunFormItemEditable) perunFormItem).setEnabled(false);
					}
				}
				perunFormItems.add(perunFormItem);
			}

			i++;
		}

		return perunFormItems;
	}

	private boolean isItemHidden(PerunFormItem formItem,
	                             Map<Integer, ApplicationFormItemData> allItemsByIds) {
		switch (formItem.getItemData().getFormItem().getHidden()) {
			case NEVER:
				return false;
			case ALWAYS:
				return true;
			case IF_EMPTY:
				return isItemHiddenForValidator(formItem, allItemsByIds, this::isItemEmpty);
			case IF_PREFILLED:
				return isItemHiddenForValidator(formItem, allItemsByIds, this::isItemPrefilled);
			default:
				return false;
		}
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	private boolean isNotBlank(String s) {
		return s != null && !s.trim().isEmpty();
	}

	private boolean isItemDisabled(PerunFormItem formItem,
	                             Map<Integer, ApplicationFormItemData> allItemsByIds) {
		switch (formItem.getItemData().getFormItem().getDisabled()) {
			case NEVER:
				return false;
			case ALWAYS:
				return true;
			case IF_EMPTY:
				return isItemDisabledForValidator(formItem, allItemsByIds, this::isItemEmpty);
			case IF_PREFILLED:
				return isItemDisabledForValidator(formItem, allItemsByIds, this::isItemPrefilled);
			default:
				return false;
		}
	}

	private boolean isItemHiddenForValidator(PerunFormItem formItemWithValue,
	                                         Map<Integer, ApplicationFormItemData> allItemsByIds,
	                                         Function<ApplicationFormItemData, Boolean> validator) {
		ApplicationFormItem formItem = formItemWithValue.getItemData().getFormItem();

		if (formItem.getHiddenDependencyItemId() == null) {
			return validator.apply(allItemsByIds
					.get(formItemWithValue.getItemData().getFormItem().getId()));
		}
		// READ - the parseInt(valueOf()) has to be used, otherwise no item is found - Sadly, I don't know why....
		ApplicationFormItemData dependencyItem = allItemsByIds.get(Integer.parseInt(String.valueOf(formItem.getHiddenDependencyItemId())));
		if (dependencyItem == null) {
			return false;
		} else {
			return validator.apply(dependencyItem);
		}
	}
	private boolean isItemDisabledForValidator(PerunFormItem formItemWithValue,
	                                         Map<Integer, ApplicationFormItemData> allItemsByIds,
	                                         Function<ApplicationFormItemData, Boolean> validator) {
		ApplicationFormItem formItem = formItemWithValue.getItemData().getFormItem();

		if (formItem.getDisabledDependencyItemId() == null) {
			return validator.apply(allItemsByIds
					.get(formItemWithValue.getItemData().getFormItem().getId()));
		}

		// READ - the parseInt(valueOf()) has to be used, otherwise no item is found - Sadly, I don't know why....
		ApplicationFormItemData dependencyItem = allItemsByIds.get(Integer.parseInt(String.valueOf(formItem.getDisabledDependencyItemId())));
		if (dependencyItem == null) {
			return false;
		} else {
			return validator.apply(dependencyItem);
		}
	}

	private boolean isItemEmpty(ApplicationFormItemData formItemWithValue) {
		return formItemWithValue.getPrefilledValue() == null || formItemWithValue.getPrefilledValue().isEmpty();
	}

	private boolean isItemPrefilled(ApplicationFormItemData formItemWithValue) {
		// For now, we support a special behaviour only for checkboxes
		if (formItemWithValue.getFormItem().getType() == ApplicationFormItem.ApplicationFormItemType.CHECKBOX) {
			return isCheckBoxPrefilled(formItemWithValue);
		}
		return isNotBlank(formItemWithValue.getPrefilledValue());
	}

	private boolean isCheckBoxPrefilled(ApplicationFormItemData formItemWithValue) {
		if (isBlank(formItemWithValue.getPrefilledValue())) {
			return false;
		}
		// This should probably be more sophisticated, but if the cs and en options have the same options, it should work
		ApplicationFormItemTexts enTexts = formItemWithValue.getFormItem().getItemTexts("en");
		if (enTexts == null) {
			return false;
		}
		if (enTexts.getOptions() == null) {
			 return false;
		}
		for (String option : enTexts.getOptions().split("\\|")) {
			String value = option.split("#")[0];
			if (formItemWithValue.getPrefilledValue().equals(value)) {
				return true;
			}
		}
		return false;
	}


	private PerunFormItem generatePerunFormItem(ApplicationFormItemData data) {

		String lang = PerunConfiguration.getCurrentLocaleName();
		switch (data.getFormItem().getType()) {
			case HEADING:
				return new Header(form, data, lang);
			case TEXTFIELD:
				return new TextField(form, data, lang);
			case VALIDATED_EMAIL:
				return new ValidatedEmail(form, data, lang);
			case TEXTAREA:
				// FIXME - hack for BBMRI collections to pre-fill value from URL
				if ((data.getValue() == null || data.getValue().isEmpty() || data.getValue().equals("null")) &&
						(data.getPrefilledValue() == null || data.getPrefilledValue().isEmpty() || data.getPrefilledValue().equals("null")) &&
						data.getFormItem().getShortname().equals("Comma or new-line separated list of IDs of collections you are representing:")) {
					final String bbmriCollections = Window.Location.getParameter("col");
					data.setPrefilledValue((bbmriCollections != null) ? JsUtils.unzipString(JsUtils.decodeBase64UrlSafe(bbmriCollections)) : null);
				}
				return new TextArea(form, data, lang);
			case USERNAME:
				Username username = new Username(form, data, lang);
				if (data.getPrefilledValue() != null && !data.getPrefilledValue().isEmpty() && !data.isGenerated()) {
					username.setEnabled(false);
				}
				return username;
			case PASSWORD:
				return new Password(form, data, lang);
			case TIMEZONE:
				return new Timezone(form, data, lang);
			case SELECTIONBOX:
				return new Selectionbox(form, data, lang);
			case COMBOBOX:
				return new Combobox(form, data, lang);
			case CHECKBOX:
				return new Checkbox(form, data, lang);
			case RADIO:
				return new Radiobox(form, data, lang);
			case HTML_COMMENT:
				return new HtmlComment(form, data, lang);
			case SUBMIT_BUTTON:
				return new SubmitButton(form, data, lang, false);
			case AUTO_SUBMIT_BUTTON:
				return new SubmitButton(form, data, lang, true);
			case EMBEDDED_GROUP_APPLICATION:
				return new GroupCheckBox(form, data, lang);
			default:
				return new Undefined(form, data, lang);
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
