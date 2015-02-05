package cz.metacentrum.perun.wui.registrar.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
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
public class PerunForm extends FieldSet {

	private String lang = LocaleInfo.getCurrentLocale().getLocaleName().equals("default") ? "en" : LocaleInfo.getCurrentLocale().getLocaleName();

	// TRUE if only text preview with (prefilled)value should be shown
	private boolean onlyPreview = false;
	private boolean seeHiddenItems = false;

	private PerunTranslation perunTranslation = GWT.create(PerunTranslation.class);

	ArrayList<PerunFormItem> items = new ArrayList<PerunFormItem>();

	/**
	 * Create form instance
	 */
	public PerunForm() {}

	/**
	 * Create form instance with possibility to set 'only preview' state.
	 *
	 * @param onlyPreview TRUE = form will display only preview / FALSE = form will allow editing
	 */
	public PerunForm(boolean onlyPreview) {
		this.onlyPreview = onlyPreview;
	}

	/**
	 * Create form instance with possibility to set 'only preview' state.
	 *
	 * @param onlyPreview TRUE = form will display only preview / FALSE = form will allow editing
	 */
	public PerunForm(boolean onlyPreview, boolean seeHidden) {
		this.onlyPreview = onlyPreview;
		this.seeHiddenItems = seeHidden;
	}

	public void addFormItem(ApplicationFormItemData itemData) {

		if (itemData != null) {

			PerunFormItem item = new PerunFormItem(this, itemData, lang);

			if (item != null) items.add(item);

			// if visible append to form
			if (item.isVisible() || seeHiddenItems) {
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

		if (items.isEmpty()) {
			add(new Heading(HeadingSize.H2, "", perunTranslation.formHasNoFormItems()));
		}

	}

	public void addPerunFormItem(PerunFormItem item) {

		if (item != null)  {

			item.setForm(this);
			items.add(item);

			// if visible append to form
			if (item.isVisible() || seeHiddenItems) {
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

		if (items.isEmpty()) {
			add(new Heading(HeadingSize.H2, "", perunTranslation.formHasNoFormItems()));
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

	/**
	 * Is form meant only for preview
	 *
	 * @return TRUE = preview / FALSE = editing
	 */
	public boolean isOnlyPreview() {
		return onlyPreview;
	}

	/**
	 * Set form as meant only for preview
	 *
	 * @param onlyPreview TRUE = for preview / FALSE = for editing
	 */
	public void setOnlyPreview(boolean onlyPreview) {
		this.onlyPreview = onlyPreview;
	}

	public boolean isSeeHiddenItems() {
		return seeHiddenItems;
	}

	public void setSeeHiddenItems(boolean seeHiddenItems) {
		this.seeHiddenItems = seeHiddenItems;
	}

}