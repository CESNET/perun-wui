package cz.metacentrum.perun.wui.registrar.widgets;

import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;

/**
 * Created by ondrej on 3.10.15.
 */
public interface PerunFormItemGenerator {

	PerunFormItem generatePerunFormItem(ApplicationFormItemData data);

}
