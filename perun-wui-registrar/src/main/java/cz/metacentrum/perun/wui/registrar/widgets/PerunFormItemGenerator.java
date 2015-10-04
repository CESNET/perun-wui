package cz.metacentrum.perun.wui.registrar.widgets;

import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;

/**
 * Control proper creating of PerunFormItem depends on Perun returned data and other context
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface PerunFormItemGenerator {

	/**
	 * Generate proper object of PerunFormItem. Depends on returned ApplicationFormItemData and its type.
	 *
	 * @param data
	 * @return
	 */
	PerunFormItem generatePerunFormItem(ApplicationFormItemData data);

}
