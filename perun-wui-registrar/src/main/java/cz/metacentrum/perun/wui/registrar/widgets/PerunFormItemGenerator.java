package cz.metacentrum.perun.wui.registrar.widgets;

import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface PerunFormItemGenerator {

	PerunFormItem generatePerunFormItem(ApplicationFormItemData data);

}
