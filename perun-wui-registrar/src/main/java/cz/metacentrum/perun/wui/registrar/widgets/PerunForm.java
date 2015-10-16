package cz.metacentrum.perun.wui.registrar.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItem;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;
import cz.metacentrum.perun.wui.registrar.widgets.items.SubmitButton;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.constants.ColumnOffset;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.extras.growl.client.ui.Growl;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlOptions;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class used to handle Perun Application forms.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunForm extends FieldSet {

	// LABEL_SIZE + WIDGET_WITH_TEXT_SIZE should be 12
	// LABEL_OFFSET should be same as LABEL_SIZE
	public static final ColumnSize LABEL_SIZE = ColumnSize.MD_2;
	public static final ColumnOffset LABEL_OFFSET = ColumnOffset.MD_2;
	public static final ColumnSize WIDGET_WITH_TEXT_SIZE = ColumnSize.MD_10;
	// WIDGET_SIZE + STATUS_SIZE should be 12
	public static final ColumnSize WIDGET_SIZE = ColumnSize.SM_6;
	public static final ColumnSize STATUS_SIZE = ColumnSize.SM_6;

	private String lang = LocaleInfo.getCurrentLocale().getLocaleName().equals("default") ? "en" : LocaleInfo.getCurrentLocale().getLocaleName();

	// contains info about onlyPreview and seeHiddenItems
	private PerunFormItemsGenerator generator;
	private boolean onlyPreview;
	private boolean seeHiddenItems;
	private Application app;
	private JsonEvents onSubmitEvent;
	private PerunTranslation perunTranslation = GWT.create(PerunTranslation.class);

	private List<PerunFormItem> items = new ArrayList<>();

	/**
	 * Create form instance
	 */
	public PerunForm() {
		generator = new PerunFormItemsGeneratorImpl(this);
	}

	/**
	 * Create form instance with possibility to set 'only preview' state.
	 *
	 * @param onlyPreview TRUE = form will display only preview / FALSE = form will allow editing
	 */
	public PerunForm(boolean onlyPreview) {
		this();
		this.onlyPreview = onlyPreview;
	}

	/**
	 * Create form instance with possibility to set 'only preview' state.
	 *
	 * @param onlyPreview TRUE = form will display only preview / FALSE = form will allow editing
	 * @param seeHiddenItems TRUE = form will display also hidden items
	 */
	public PerunForm(boolean onlyPreview, boolean seeHiddenItems) {
		this(onlyPreview);
		this.seeHiddenItems = seeHiddenItems;
	}

	public void addFormItems(List<ApplicationFormItemData> items) {

		if (items != null) {

			addPerunFormItems(generator.generatePerunFormItems(items));

		}

	}

	/**
	 * Set form items to form. Form is cleared before set
	 *
	 * @param items Items to set.
	 */
	public void setFormItems(List<ApplicationFormItemData> items) {

		// clear form
		this.items.clear();
		this.clear();

		// add items
		addFormItems(items);

	}

	public void addPerunFormItems(List<PerunFormItem> items) {

		if (items != null)  {

			for (PerunFormItem item : items) {
				add(item);
			}
			this.items.addAll(items);

			// auto submit
			SubmitButton autoSubmit = getAutoSubmitButton(items);
			if (autoSubmit != null && !isOnlyPreview()) {
				submit(autoSubmit.getButton());
			}

		}

		if (this.items.isEmpty()) {
			add(new Heading(HeadingSize.H2, "", perunTranslation.formHasNoFormItems()));
		}

	}

	/**
	 * Set form items to form. Form is cleared before set
	 *
	 * @param items Items to set.
	 */
	public void setPerunFormItems(List<PerunFormItem> items) {

		// clear form
		this.items.clear();
		this.clear();

		addPerunFormItems(items);

	}

	/**
	 * Get form items of form.
	 *
	 * @return unmodifiable list of PerunFormItems
	 */
	public List<PerunFormItem> getPerunFormItems() {
		return Collections.unmodifiableList(items);
	}

	/**
	 * @return SubmitButton if items contains auto submit button, null otherwise.
	 */
	private SubmitButton getAutoSubmitButton(List<PerunFormItem> items) {
		for (PerunFormItem item : items) {
			if (item instanceof SubmitButton) {
				SubmitButton button = (SubmitButton) item;
				if (button.hasAutoSubmit()) {
					return button;
				}
			}
		}
		return null;
	}

	/**
	 * Submits the form if all form items are in SUCCESS validation state.
	 *
	 * @param button submitting the form
	 */
	public void submit(final PerunButton button) {

		validateAll(items, new Events<Boolean>() {
			@Override
			public void onFinished(Boolean result) {

				button.setProcessing(false);
				button.setEnabled(true);

				if (result) {

					// TODO - remove debug logging
					for (PerunFormItem item : items) {
						GWT.log(item.getItemData().getFormItem().getShortname() + " : " + item.getValue());
					}

					RegistrarManager.createApplication(app, getFormItemData(), new JsonEvents() {
						@Override
						public void onFinished(JavaScriptObject jso) {

							button.setProcessing(false);
							button.setEnabled(true);

							GrowlOptions go = new GrowlOptions();
							go.setType(GrowlType.SUCCESS);
							Growl.growl("Form submitted !!", go);

							if (onSubmitEvent != null) onSubmitEvent.onFinished(jso);

						}

						@Override
						public void onError(PerunException error) {
							button.setProcessing(false);
							button.setEnabled(true);
							if (onSubmitEvent != null) onSubmitEvent.onError(error);
						}

						@Override
						public void onLoadingStart() {
							button.setProcessing(true);
							button.setEnabled(false);
							if (onSubmitEvent != null) onSubmitEvent.onLoadingStart();
						}
					});

				}
			}

			@Override
			public void onError(PerunException error) {
				// Shouldn't happen
			}

			@Override
			public void onLoadingStart() {

				button.setProcessing(true);
				button.setEnabled(false);
			}
		});


	}


	/**
	 * Validate all PerunFormItems and return true (in callback.onFinished() method) if all of them are valid.
	 *
	 * @param items
	 * @param events callback events
	 */
	private void validateAll(final List<PerunFormItem> items, final Events<Boolean> events) {

		if (items.size() == 0) {
			events.onLoadingStart();
			events.onFinished(true);
			return;
		}

		final int startCount = items.size();
		final int[] currentCount = {0};
		final boolean[] valid = {true};
		final boolean[] firstLoop = {true};

		for (final PerunFormItem item : items) {

			item.validate(new Events<Boolean>() {
				@Override
				public void onFinished(Boolean result) {
					if (!result) {
						nonvalid();
					}
					increment();
				}

				@Override
				public void onError(PerunException error) {
					nonvalid();
					increment();
				}

				@Override
				public void onLoadingStart() {
					if (firstLoop[0]) {
						events.onLoadingStart();
						firstLoop[0] = false;
					}
				}

				private void increment() {
					currentCount[0]++;
					if (currentCount[0] >= startCount) {
						events.onFinished(valid[0]);
					}
				}

				private void nonvalid() {
					if (valid[0]) {
						int top = item.getAbsoluteTop();
						Window.scrollTo(0, (top - 85 >= 0) ? top - 85 : top);
						item.focus();
						valid[0] = false;
					}
				}
			});
		}
	}


	/**
	 * Is form meant only for preview
	 *
	 * @return TRUE = preview / FALSE = editing
	 */
	public boolean isOnlyPreview() {
		return this.onlyPreview;
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
		return this.seeHiddenItems;
	}

	public void setSeeHiddenItems(boolean seeHiddenItems) {
		this.seeHiddenItems = seeHiddenItems;
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}

	public JsonEvents getOnSubmitEvent() {
		return onSubmitEvent;
	}

	public void setOnSubmitEvent(JsonEvents onSubmitEvent) {
		this.onSubmitEvent = onSubmitEvent;
	}

	private List<ApplicationFormItemData> getFormItemData() {

		// convert data
		List<ApplicationFormItemData> data = new ArrayList<ApplicationFormItemData>();
		for (PerunFormItem item : items) {

			String value = item.getValue();
			String prefilled = item.getItemData().getPrefilledValue();
			JSONObject formItemJSON = new JSONObject(item.getItemData().getFormItem());

			// remove text (locale), saves data transfer & removes problem with parsing locale
			formItemJSON.put("i18n", new JSONObject());

			// cast form item back
			ApplicationFormItem formItem = formItemJSON.getJavaScriptObject().cast();

			// prepare package with data
			ApplicationFormItemData itemData = ApplicationFormItemData.construct(formItem, formItem.getShortname(), value, prefilled, item.getItemData().getAssuranceLevel() != null ? item.getItemData().getAssuranceLevel() : "");

			data.add(itemData);

		}

		return data;

	}

	public String getLang() {
		return lang;
	}
}