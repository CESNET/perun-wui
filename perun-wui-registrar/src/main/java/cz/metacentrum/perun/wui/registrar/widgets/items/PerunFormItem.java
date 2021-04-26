package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItem;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import org.gwtbootstrap3.client.ui.FormGroup;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents general form item. Encapsulate and view of ApplicationFormItemData returned from RPC.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public abstract class PerunFormItem extends FormGroup {

	private ApplicationFormItemData itemData;
	private String lang;
	private PerunForm form;

	PerunRegistrarTranslation translation;

	Set<ApplicationFormItem.ApplicationFormItemType> notUpdatableTypes = new HashSet<>(Arrays.asList(
		ApplicationFormItem.ApplicationFormItemType.USERNAME,
		ApplicationFormItem.ApplicationFormItemType.PASSWORD,
		ApplicationFormItem.ApplicationFormItemType.HEADING,
		ApplicationFormItem.ApplicationFormItemType.HTML_COMMENT,
		ApplicationFormItem.ApplicationFormItemType.SUBMIT_BUTTON,
		ApplicationFormItem.ApplicationFormItemType.AUTO_SUBMIT_BUTTON,
		ApplicationFormItem.ApplicationFormItemType.EMBEDDED_GROUP_APPLICATION
	));

	public PerunFormItem(PerunForm form, ApplicationFormItemData itemData, String lang) {
		this.form = form;
		this.itemData = itemData;
		this.lang = lang;
		this.translation = GWT.create(PerunRegistrarTranslation.class);
	}

	public PerunForm getForm() {
		return form;
	}

	/**
	 * generate whole UI form item described in itemData. With all texts and functionality.
	 * It will be added automatically to the DOM. Do not use add() method inside.
	 *
	 * @return specific FormItem describes in itemData.
	 */
	protected abstract Widget initFormItem();

	protected PerunRegistrarTranslation getTranslation() {
		return translation;
	}

	public ApplicationFormItemData getItemData() {
		return itemData;
	}

	public String getLang() {
		return lang;
	}

	/**
	 * Get current Value of item. It have to be consistent with Perun RPC documentation for each item.
	 * It should be value which user see in the visible form.
	 *
	 * @return Value of item.
	 */
	public abstract String getValue();

	/**
	 * Set value (usually prefilled value). It have to be consistent with Perun RPC documentation for each item.
	 * It should be displayed visible in the form.
	 * FORM ITEM HAS TO HAVE PARENT WHEN YOU CALL THIS METHOD. use add(this). It is because refreshing of Select.
	 *
	 * @param value
	 */
	public abstract void setValue(String value);

	/**
	 * Validate current value localy. It doesn't use Perun RPC.
	 * Be carefull, because of it, true doesn't mean field is filled correctly.
	 * It should visible show result of controll (e.g. color widget, red = error, green = success, ...)
	 *
	 * @return true if item is filled with valid value.
	 */
	public abstract boolean validateLocal();

	/**
	 * Validate current value localy and remotely. It can call Perun RPC.
	 * It should visible show result of controll (e.g. color widget, red = error, green = success, ...)
	 *
	 * @param events callback events.
	 */
	public abstract void validate(Events<Boolean> events);

	/**
	 * @return result of last validation.
	 */
	public abstract PerunFormItemValidator.Result getLastValidationResult();

	/**
	 * focus editable widget.
	 *
	 * @return true if item was focused, false if item cant be focused. (e.g. non editable static item)
	 */
	public abstract boolean focus();



	/**
	 * Safely return items label/description or if not defined than shortname.
	 * If nothing defined, return empty string.
	 *
	 * @return items label or shortname
	 */
	public String getLabelOrShortName() {

		if (getItemData() == null) {
			return "";
		}

		if (getItemData().getFormItem() != null) {
			if (getItemData().getFormItem().getItemTexts(getLang()) != null) {
				if ((getItemData().getFormItem().getItemTexts(getLang()).getLabel() != null)
						&& (!getItemData().getFormItem().getItemTexts(getLang()).getLabel().isEmpty())) {
					return getItemData().getFormItem().getItemTexts(getLang()).getLabel();
				}
			}
			if (getItemData().getFormItem().getItemTexts("en") != null) {
				if ((getItemData().getFormItem().getItemTexts("en").getLabel() != null)
						&& (!getItemData().getFormItem().getItemTexts("en").getLabel().isEmpty())) {
					return getItemData().getFormItem().getItemTexts("en").getLabel();
				}
			}
			if ((getItemData().getFormItem().getShortname() != null)
					&& (!getItemData().getFormItem().getShortname().isEmpty())) {
				return getItemData().getFormItem().getShortname();
			}
		}
		if (getItemData().getShortname() != null) {
			return getItemData().getShortname();
		}

		return "";
	}

	/**
	 * Return TRUE if form item can be "updated" once form is submitted.
	 * We can update only items with non-null "ApplicationFormItem" property.
	 *
	 * @return default TRUE
	 */
	public boolean isUpdatable() {
		if (getItemData().getFormItem() == null) {
			return false;
		}
		if (notUpdatableTypes.contains(getItemData().getFormItem().getType())) {
			return false;
		}
		return getItemData().getFormItem().isUpdatable();
	}

}
