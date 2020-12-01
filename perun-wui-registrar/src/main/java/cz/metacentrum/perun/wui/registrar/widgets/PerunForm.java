package cz.metacentrum.perun.wui.registrar.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItem;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.model.beans.Identity;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarResources;
import cz.metacentrum.perun.wui.registrar.pages.FormView;
import cz.metacentrum.perun.wui.registrar.widgets.items.Header;
import cz.metacentrum.perun.wui.registrar.widgets.items.HtmlComment;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;
import cz.metacentrum.perun.wui.registrar.widgets.items.SubmitButton;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.constants.ColumnOffset;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Utility class used to handle Perun Application forms.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunForm extends FieldSet {

	public enum FormState {
		PREFILLED,
		PREVIEW,
		EDIT
	}

	// LABEL_SIZE + WIDGET_WITH_TEXT_SIZE should be 12
	// LABEL_OFFSET should be same as LABEL_SIZE
	public static final ColumnSize LABEL_SIZE = ColumnSize.MD_2;
	public static final ColumnOffset LABEL_OFFSET = ColumnOffset.MD_2;
	public static final ColumnSize WIDGET_WITH_TEXT_SIZE = ColumnSize.MD_10;
	// WIDGET_SIZE + STATUS_SIZE should be 12
	public static final ColumnSize WIDGET_SIZE = ColumnSize.SM_6;
	public static final ColumnSize STATUS_SIZE = ColumnSize.SM_6;

	// contains info about onlyPreview and seeHiddenItems
	private PerunFormItemsGenerator generator;
	private FormState formState;
	private boolean seeHiddenItems;
	private Application app;
	private JsonEvents onSubmitEvent;
	private PerunTranslation perunTranslation = GWT.create(PerunTranslation.class);

	private List<PerunFormItem> items = new ArrayList<>();

	private boolean checkSimilarUsersAgain = false;

	/**
	 * Create form instance
	 */
	public PerunForm() {
		generator = new PerunFormItemsGeneratorImpl(this);
	}

	/**
	 * Create form instance with possibility to set 'only preview' state.
	 *
	 * @param formState default is PREFILLED, @see FormState
	 */
	public PerunForm(FormState formState) {
		this();
		this.formState = formState;
	}

	/**
	 * Create form instance with possibility to set 'only preview' state.
	 *
	 * @param formState default is PREFILLED, @see FormState
	 * @param seeHiddenItems TRUE = form will display also hidden items
	 */
	public PerunForm(FormState formState, boolean seeHiddenItems) {
		this(formState);
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
				if (FormState.EDIT.equals(formState) && !item.isUpdatable()) {
					// distinguish non-editable items
					item.addStyleName(PerunRegistrarResources.INSTANCE.gss().help());
				}
				add(item);
			}
			this.items.addAll(items);

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
	 * Get all sourcing form items (not suitable for submit!!)
	 *
	 * @return list of source form items with no changes
	 */
	public List<ApplicationFormItemData> getSourceFormItems() {
		List<ApplicationFormItemData> data = new ArrayList<>();
		for (PerunFormItem item : getPerunFormItems()) {
			data.add(item.getItemData());
		}
		return data;
	}

	/**
	 * perform auto submit if form contains auto submit button. Do nothing otherwise.
	 */
	public void performAutoSubmit() {

		SubmitButton autoSubmit = getAutoSubmitButton(items);
		if (autoSubmit != null && FormState.PREFILLED.equals(getFormState())) {
			submit(autoSubmit.getButton());
		}
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
	 * @return true if contains at least one submit button so it can be submitted.
	 */
	public boolean containsSubmitButton() {
		for (PerunFormItem item : items) {
			if (item instanceof SubmitButton) {
				return true;
			}
		}
		return false;
	}

	/**
	 * TRUE if form contains only textual items like info: "You are already registered"
	 * or "You must be student of PV179".
	 *
	 * @return true if form contains only textual items
	 */
	public boolean containsOnlyTextItems() {
		for (PerunFormItem item : items) {
			if (!(item instanceof Header) && !(item instanceof HtmlComment)) {
				return false;
			}
		}
		return true;
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

					if (checkSimilarUsersAgain && !PerunConfiguration.findSimilarUsersDisabled()) {

						// validation ok - check similar users
						RegistrarManager.checkForSimilarUsers(getFormItemDataToSubmit(), new JsonEvents() {
							@Override
							public void onFinished(JavaScriptObject result) {

								List<Identity> identities = JsUtils.<Identity>jsoAsList(result);
								if (identities.isEmpty()) {
									// no similar identities found - submit application
									createApplication(button);
								} else {
									FormView.showSimilarUsersDialog(identities, new ClickHandler() {
										@Override
										public void onClick(ClickEvent event) {
											// user declined joining - submit application
											createApplication(button);
										}
									});
								}
							}

							@Override
							public void onError(PerunException error) {
								// don't care
								createApplication(button);
							}

							@Override
							public void onLoadingStart() {

							}
						});

					} else {
						// validation ok - create immediately
						createApplication(button);
					}

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

	public void submitEditedForm(final Application application, final Events<Boolean> events) {
		validateAll(items, new Events<Boolean>() {
			@Override
			public void onFinished(Boolean result) {
				if (result) {
					// if everything is valid - submit updated items
					List<ApplicationFormItemData> data = getFormItemDataToUpdate();
					if (data.isEmpty()) {
						events.onError(null);
					} else {
						RegistrarManager.updateFormItemsData(application.getId(), data, new JsonEvents() {
							@Override
							public void onFinished(JavaScriptObject result) {
								events.onFinished(true);
							}
							@Override
							public void onError(PerunException error) {
								events.onError(error);
							}
							@Override
							public void onLoadingStart() {
							}
						});
					}
				} else {
					events.onError(null);
				}
			}

			@Override
			public void onError(PerunException error) {
				events.onError(error);
			}

			@Override
			public void onLoadingStart() {
				events.onLoadingStart();
			}

		});
	}

	/**
	 * Finally send application form to Perun API
	 *
	 * @param button
	 */
	private void createApplication(final PerunButton button) {

		RegistrarManager.createApplication(app, getFormItemDataToSubmit(), new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject jso) {

				button.setProcessing(false);
				button.setEnabled(true);
				if (onSubmitEvent != null) onSubmitEvent.onFinished(jso);

			}

			@Override
			public void onError(PerunException error) {

				// in a case of browser lag, user might submit same application multiple-times
				// AlreadyProcessingException prevents concurrent run on server and must be
				// silently skipped in GUI (first successful request will handle all actions)
				if (!"AlreadyProcessingException".equals(error.getName())) {
					// some real exception
					button.setProcessing(false);
					button.setEnabled(true);
					if (onSubmitEvent != null) onSubmitEvent.onError(error);
				}

			}

			@Override
			public void onLoadingStart() {
				button.setProcessing(true);
				button.setEnabled(false);
				if (onSubmitEvent != null) onSubmitEvent.onLoadingStart();
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

			if (FormState.PREFILLED.equals(formState)) {

				// for anonymous users
				if (PerunSession.getInstance().getUser() == null) {

					String prefilledValue = item.getItemData().getPrefilledValue();

					if (Objects.equals(item.getItemData().getFormItem().getType(), ApplicationFormItem.ApplicationFormItemType.VALIDATED_EMAIL)) {
						if (prefilledValue == null || !prefilledValue.contains(item.getValue())) {
							// mail changed - re-check existing users
							checkSimilarUsersAgain = true;
						}
					} else if (Objects.equals(item.getItemData().getFormItem().getPerunDestinationAttribute(), "urn:perun:user:attribute-def:core:displayName")) {
						if (!Objects.equals(prefilledValue, item.getValue())) {
							// name changed - re-check existing users
							checkSimilarUsersAgain = true;
						}
					}

				}
			}

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
						Window.scrollTo(0, (top - 85 >= 0) ? top - 85 : 0);
						item.focus();
						valid[0] = false;
					}
				}
			});
		}
	}


	/**
	 * Return state of the form, default is PREFILLED, can be PREVIEW and EDIT
	 *
	 * @return FormState
	 */
	public FormState getFormState() {
		return this.formState;
	}

	/**
	 * Set form state, default is PREFILLED, can be PREVIEW and EDIT
	 *
	 * @param  formState default is PREFILLED, @see FormState
	 */
	public void setFormState(FormState formState) {
		this.formState = formState;
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

	private List<ApplicationFormItemData> getFormItemDataToSubmit() {

		// convert data
		List<ApplicationFormItemData> data = new ArrayList<ApplicationFormItemData>();
		for (PerunFormItem item : items) {

			String value = item.getValue();
			String prefilled = item.getItemData().getPrefilledValue();

			// remove text (locale), saves data transfer & removes problem with parsing locale
			ApplicationFormItem formItem = ApplicationFormItem.createNew(item.getItemData().getFormItem().getId(),
					item.getItemData().getFormItem().getShortname(),
					item.getItemData().getFormItem().isRequired(),
					item.getItemData().getFormItem().getType(),
					item.getItemData().getFormItem().getFederationAttribute(),
					item.getItemData().getFormItem().getPerunSourceAttribute(),
					item.getItemData().getFormItem().getPerunDestinationAttribute(),
					item.getItemData().getFormItem().getRegex(),
					item.getItemData().getFormItem().getApplicationTypes(),
					item.getItemData().getFormItem().getOrdnum(),
					item.getItemData().getFormItem().isForDelete());

			// clear pre-filled value if login was generated, since we want server to register new login
			if (ApplicationFormItem.ApplicationFormItemType.USERNAME.equals(formItem.getType()) &&
					item.getItemData().isGenerated()) {
				prefilled = "";
			}

			// prepare package with data
			ApplicationFormItemData itemData = ApplicationFormItemData.construct(formItem, formItem.getShortname(), value, prefilled, item.getItemData().getAssuranceLevel() != null ? item.getItemData().getAssuranceLevel() : "");

			data.add(itemData);

		}

		return data;

	}

	private List<ApplicationFormItemData> getFormItemDataToUpdate() {

		// convert data
		List<ApplicationFormItemData> data = new ArrayList<ApplicationFormItemData>();
		for (PerunFormItem item : items) {

			// skip non-updatable form items
			if (!item.isUpdatable()) continue;

			if (Objects.equals(item.getItemData().getValue(), item.getValue())) {
				// value within item and form item is same -> unchanged -> do not update
				continue;
			}

			String value = item.getValue();
			String prefilled = item.getItemData().getPrefilledValue();

			// clear pre-filled value if login was generated, since we want server to register new login
			if (ApplicationFormItem.ApplicationFormItemType.USERNAME.equals(item.getItemData().getFormItem().getType()) &&
					item.getItemData().isGenerated()) {
				prefilled = "";
			}

			// remove text (locale), saves data transfer & removes problem with parsing locale
			ApplicationFormItem formItem = ApplicationFormItem.createNew(item.getItemData().getFormItem().getId(),
					item.getItemData().getFormItem().getShortname(),
					item.getItemData().getFormItem().isRequired(),
					item.getItemData().getFormItem().getType(),
					item.getItemData().getFormItem().getFederationAttribute(),
					item.getItemData().getFormItem().getPerunSourceAttribute(),
					item.getItemData().getFormItem().getPerunDestinationAttribute(),
					item.getItemData().getFormItem().getRegex(),
					item.getItemData().getFormItem().getApplicationTypes(),
					item.getItemData().getFormItem().getOrdnum(),
					item.getItemData().getFormItem().isForDelete());

			// prepare package with data
			ApplicationFormItemData itemData = ApplicationFormItemData.construct(formItem, item.getItemData().getFormItem().getShortname(), value, prefilled, item.getItemData().getAssuranceLevel() != null ? item.getItemData().getAssuranceLevel() : "");
			itemData.setId(item.getItemData().getId());
			data.add(itemData);

		}

		return data;

	}

}
