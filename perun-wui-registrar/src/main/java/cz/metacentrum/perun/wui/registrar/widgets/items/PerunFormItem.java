package cz.metacentrum.perun.wui.registrar.widgets.items;

import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.validators.PerunFormItemValidator;
import org.gwtbootstrap3.client.ui.FormGroup;

/**
 * Created by ondrej on 5.10.15.
 */
public abstract class PerunFormItem extends FormGroup {

	private ApplicationFormItemData itemData;
	private String lang;

	public PerunFormItem(ApplicationFormItemData itemData, String lang) {
		this.itemData = itemData;
		this.lang = lang;
		add(initFormItem());
	}

	protected abstract Widget initFormItem();


	public ApplicationFormItemData getItemData() {
		return itemData;
	}

	public String getLang() {
		return lang;
	}

	public abstract String getValue();


	public abstract void validate(Events<Boolean> events);

	public abstract boolean validateLocal();

	public abstract PerunFormItemValidator.Result getLastValidationResult();


	public abstract void focus();





	/**
	 * Safely return items label/description. If nothing defined, return empty string.
	 *
	 * @return items label or shortName
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

}
