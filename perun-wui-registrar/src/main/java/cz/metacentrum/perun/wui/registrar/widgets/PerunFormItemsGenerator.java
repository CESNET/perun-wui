package cz.metacentrum.perun.wui.registrar.widgets;

import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;

import java.util.List;

/**
 * Convertor from ApplicationFormItemData to PerunFormItem objects.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface PerunFormItemsGenerator {

	List<PerunFormItem> generatePerunFormItems(List<ApplicationFormItemData> data);

}
