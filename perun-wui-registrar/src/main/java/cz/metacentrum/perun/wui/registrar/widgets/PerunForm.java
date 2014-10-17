package cz.metacentrum.perun.wui.registrar.widgets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.client.resources.Translatable;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.extras.growl.client.ui.Growl;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlOptions;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlPosition;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlType;

import java.util.ArrayList;

/**
 * Utility class used to handle Perun Application forms.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunForm extends FieldSet implements Translatable {

	private String lang = PerunSession.LOCALE;

	ArrayList<PerunFormItem> items = new ArrayList<PerunFormItem>();

	@Override
	public void changeLanguage() {

		lang = PerunSession.LOCALE;
		for (PerunFormItem item : items) {
			item.setTexts();
		}

	}

	public void addFormItem(ApplicationFormItemData itemData) {

		if (itemData != null) {

			PerunFormItem item = new PerunFormItem(itemData, lang);
			item.setForm(this);

			if (item != null) items.add(item);

			// if visible append to form
			if (item.isVisible()) {
				add(item);
			}

		}

	}

	/**
	 * Set form items to form. Form is cleared before set
	 *
	 * @param items Items to set.
	 */
	public void setFormItems(ArrayList<ApplicationFormItemData> items) {

		// clear form
		this.items.clear();
		this.clear();

		// add items
		if (items != null) {
			for (ApplicationFormItemData item : items) {
				addFormItem(item);
			}
		}

	}

	public void addPerunFormItem(PerunFormItem item) {

		if (item != null)  {

			item.setForm(this);
			items.add(item);

			// if visible append to form
			if (item.isVisible()) {
				add(item);
			}

		}

	}

	/**
	 * Set form items to form. Form is cleared before set
	 *
	 * @param items Items to set.
	 */
	public void setPerunFormItems(ArrayList<PerunFormItem> items) {

		// clear form
		this.items.clear();
		this.clear();

		// add items
		if (items != null) {
			for (PerunFormItem item : items) {
				addPerunFormItem(item);
			}
		}

	}

	/**
	 * Submits the form if all form items are in SUCCESS validation state.
	 *
	 * @param button submitting the form
	 */
	public void submit(final PerunButton button) {

		// force validation
		boolean scrolled = false;
		for (PerunFormItem item : items) {
			if (!item.isValid(true) && !scrolled) {
				int top = item.getFormItemWidget().getAbsoluteTop();
				Window.scrollTo(0, (top-85 >= 0) ? top-85 : top);
				scrolled = true;
			}
		}

		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
			@Override
			public boolean execute() {

				boolean processing = false;
				boolean valid = true;

				for (PerunFormItem item : items) {
					if (!item.isValid(false)) {
						valid = false;
						if (item.getValidator() != null) {
							if (item.getValidator().isProcessing()) {
								processing = true;
								break;
							}
						}
					}
				}
				// re-check after 400ms
				if (processing) {

					button.setProcessing(true);
					button.setEnabled(false);

					return true;

				} else if (!valid) {

					button.setProcessing(false);
					button.setEnabled(true);
					return false;

				} else {

					// TODO - SUBMIT THE FORM
					GrowlOptions go = new GrowlOptions();
					go.setPosition(GrowlPosition.TOP_CENTER);
					go.setType(GrowlType.SUCCESS);
					Growl.growl("Form submitted !!", go);
					return false;

				}
			}
		}, 400);

	}

}