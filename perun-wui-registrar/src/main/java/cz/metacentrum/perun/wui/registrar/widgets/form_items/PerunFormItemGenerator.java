package cz.metacentrum.perun.wui.registrar.widgets.form_items;

import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;

/**
 * Created by ondrej on 3.10.15.
 */
public interface PerunFormItemGenerator {

	PerunFormItem generatePerunFormItem(ApplicationFormItemData data);

}
