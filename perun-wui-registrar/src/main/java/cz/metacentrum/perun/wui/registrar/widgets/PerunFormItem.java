package cz.metacentrum.perun.wui.registrar.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.*;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItem;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedPasswordTextBox;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextArea;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.base.ValueBoxBase;
import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class wrapping ApplicationFormItem and ApplicationFormItemData objects,
 * which is used to generate widgets for form and to perform tasks like update, check, etc.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunFormItem extends FormGroup {

	public final static int TEXT_AREA_MAX_LENGTH = 3999;
	public final static int TEXT_BOX_MAX_LENGTH = 1024;
	private static final int PERUN_ATTRIBUTE_LOGIN_NAMESPACE_POSITION = 49;

	private RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

	// form item storage
	ApplicationFormItemData item;
	// default is EN
	private String lang = LocaleInfo.getCurrentLocale().getLocaleName().equals("default") ? "en" : LocaleInfo.getCurrentLocale().getLocaleName();
	// pre-filled value
	private String preFilledValue = "";
	// value box used to handle changes
	private ValueBoxBase<String> currentValue;
	// form this item is associated with
	private PerunForm form;
	// widget used for input boxes
	private Widget widget;
	// form item validator
	private PerunFormItemValidator validator;

	// generic select for pre-filled email addresses
	final Select emailSelect = new Select();

	// form item widgets
	private FormLabel formLabel = new FormLabel();
	private Paragraph helpText = new Paragraph();
	private Paragraph statusText = new Paragraph();

	/**
	 * Interface for anonymous classes used as customized item validators.
	 */
	public interface PerunFormItemValidator {

		/**
		 * Validates form item.
		 *
		 * @param forceNew TRUE = force new validation / FALSE = use value from last check
		 * @return TRUE = valid / FALSE = not valid
		 */
		public boolean validate(boolean forceNew);

		/**
		 * Return TRUE if validation is processing (for items with callbacks to core)
		 *
		 * @return TRUE = validation is processing / FALSE = validation is done
		 */
		public boolean isProcessing();

		/**
		 * Translate error messages based on inner return codes.
		 *
		 * -1 = no message
		 * 0 = OK
		 * 1 = isEmpty
		 * 2 = isEmptySelect
		 * 3 = tooLong
		 * 4 = invalid format
		 * 5 = invalid format email
		 * 6 = login not available
		 * 7 = can't check login
		 * 9 = checking login...
		 * 10 = password can't be empty
		 * 11 = password mismatch
		 * 12 = must validate email
		 */
		public void translate();

	}

	public PerunFormItem(PerunForm form, ApplicationFormItemData item) {
		this.form = form;
		this.item = item;
		this.formLabel.getElement().addClassName("formLabel");
		if (item != null) {
			generateFormItem();
		}
	}

	public PerunFormItem(PerunForm form, ApplicationFormItemData item, String lang) {
		this.form = form;
		this.item = item;
		this.lang = lang;
		this.formLabel.getElement().addClassName("formLabel");
		if (item != null) {
			generateFormItem();
		}
	}


	public ApplicationFormItemData getItem() {
		return item;
	}

	public void setItem(ApplicationFormItemData item) {
		this.item = item;
	}

	public FormLabel getFormLabel() {
		return formLabel;
	}

	public Paragraph getHelpText() {
		return helpText;
	}

	public PerunForm getForm() {
		return form;
	}

	public void setForm(PerunForm form) {
		this.form = form;
	}

	public PerunFormItemValidator getValidator() {
		return validator;
	}

	public void setValidator(PerunFormItemValidator validator) {
		this.validator = validator;
	}

	/**
	 * Changes item texts to specified language.
	 */
	public void setTexts() {

		if (!item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.SUBMIT_BUTTON) &&
				!item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.AUTO_SUBMIT_BUTTON)) {
			if (!getLabelOrShortName().isEmpty()) {

				String baseString = getLabelOrShortName();
				if (form.isSeeHiddenItems() && !isVisible()) {
					baseString = baseString + " " + translation.isHidden();
				}

				if (isRequired()) {
					formLabel.setHTML(baseString.replaceAll(" ", "&nbsp;")+"&nbsp;<span style=\"color: red;\">*</span>");
				} else {
					formLabel.setHTML(baseString.replaceAll(" ", "&nbsp;")+"&nbsp;&nbsp;");
				}
			}
		}
		if (item.getFormItem() != null) {
			helpText.setText(item.getFormItem().getItemTexts(lang).getHelp());
		}

		if (validator != null) validator.translate();

		if (!form.isOnlyPreview()) {

			// TODO - regenerate other widgets !!
			if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.HTML_COMMENT)) {
				((HTML) widget).setHTML(getLabelOrShortName());
			} else if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.HEADING)) {
				((Legend) widget).setHTML(getLabelOrShortName());
			} else if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.SELECTIONBOX)) {

				final cz.metacentrum.perun.wui.registrar.widgets.Select select = ((cz.metacentrum.perun.wui.registrar.widgets.Select) getFormItemWidget());

				select.clear();

				Map<String, String> opts = parseSelectionBox(item.getFormItem().getItemTexts(lang).getOptions());

				ArrayList<String> keyList = Utils.setToList(opts.keySet());

				int i = 0;

				if (!isRequired()) {
					select.addItem(translation.notSelected(), "");
					i++;
				}

				for (String key : keyList) {
					boolean selected = getValue().equals(key);
					select.addItem(opts.get(key), key);
					select.setItemSelected(i, selected);
					i++;
				}

				if (select.getItemCount() != 0) {
					// set default value
					currentValue.setValue(select.getAllSelectedValues().get(0));
				}

				select.refresh();

			} else if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.COMBOBOX)) {

				final FlowPanel group = ((FlowPanel) getFormItemWidget());
				final cz.metacentrum.perun.wui.registrar.widgets.Select select = (cz.metacentrum.perun.wui.registrar.widgets.Select) group.getWidget(0);
				int selectedIndex = select.getSelectedIndex();
				select.clear();

				Map<String, String> opts = parseSelectionBox(item.getFormItem().getItemTexts(lang).getOptions());
				ArrayList<String> keyList = Utils.setToList(opts.keySet());

				if (!isRequired()) {
					select.addItem(translation.notSelected(), "");
				}

				select.addItem(translation.customValue(), "custom");
				int customIndex = select.getItemCount() - 1;

				for (String key : keyList) {
					select.addItem(opts.get(key), key);
					// set pre-filled value as selected
					if (preFilledValue.equals(key) && selectedIndex == -1) selectedIndex = select.getItemCount() - 1;
				}

				// when nothing selected
				if (selectedIndex < 0) {

					if (preFilledValue.isEmpty()) {
						selectedIndex = 0;
					} else {
						selectedIndex = customIndex;
					}

				}

				// select proper item
				select.setItemSelected(selectedIndex, true);

				if (select.getSelectedIndex() != customIndex) {
					currentValue.setValue(select.getValue(select.getSelectedIndex()));
					if (!currentValue.getValue().equals("custom")) {
						// if not custom, hide input box
						currentValue.setVisible(false);
					}
					select.removeStyleName("comboboxFormItemFirst");
					// FIXME - hack bug in BootstrapSelect
					select.getElement().getNextSiblingElement().removeClassName("comboboxFormItemFirst");
				} else {
					select.addStyleName("comboboxFormItemFirst");
					currentValue.setVisible(true);
				}

				// rework group
				select.refresh();

			} else if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.CHECKBOX)) {

				((FlowPanel) widget).clear();

				String options = item.getFormItem().getItemTexts(lang).getOptions();
				Map<String, String> boxContents = parseSelectionBox(options);
				final Map<CheckBox, String> boxValueMap = new HashMap<CheckBox, String>();

				ArrayList<String> keyList = Utils.setToList(boxContents.keySet());

				for (String key : keyList) {

					final CheckBox checkbox = new CheckBox(boxContents.get(key));
					// pre-fill
					for (String s : getValue().split("\\|")) {
						if (key.trim().equals(s.trim())) {
							checkbox.setValue(true);
						}
					}
					boxValueMap.put(checkbox, key);

					checkbox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
						@Override
						public void onValueChange(ValueChangeEvent<Boolean> event) {

							// rebuild value
							String value = "";
							for (Map.Entry<CheckBox, String> innerEntry : boxValueMap.entrySet()) {
								if (innerEntry.getKey().getValue()) {
									// put in selected values
									value += innerEntry.getValue() + "|";
								}
							}
							if (value.length() > 1) {
								value = value.substring(0, value.length() - 1);
							}

							currentValue.setValue(value, true);

						}
					});

					((FlowPanel) widget).add(checkbox);

				}

			} else if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.RADIO)) {

				((FlowPanel) widget).clear();

				String options = item.getFormItem().getItemTexts(lang).getOptions();
				Map<String, String> boxContents = parseSelectionBox(options);
				ArrayList<String> keyList = Utils.setToList(boxContents.keySet());

				// fill not selected option
				if (!isRequired()) {

					final Radio radio = new Radio("radio" + item.getFormItem().getId(), translation.notSelected());
					// pre-fill
					if ("".equals(getValue().trim())) {
						radio.setValue(true);
					}

					radio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
						@Override
						public void onValueChange(ValueChangeEvent<Boolean> event) {
							currentValue.setValue("", true);
						}
					});

					((FlowPanel) widget).add(radio);

				}

				for (final String key : keyList) {

					final Radio radio = new Radio("radio" + item.getFormItem().getId(), boxContents.get(key));
					// pre-fill
					if (key.trim().equals(getValue().trim())) {
						radio.setValue(true);
					}
					radio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
						@Override
						public void onValueChange(ValueChangeEvent<Boolean> event) {
							currentValue.setValue(key, true);
						}
					});

					((FlowPanel) widget).add(radio);

				}

			} else if (ApplicationFormItem.ApplicationFormItemType.SUBMIT_BUTTON.equals(item.getFormItem().getType()) ||
					ApplicationFormItem.ApplicationFormItemType.AUTO_SUBMIT_BUTTON.equals(item.getFormItem().getType())) {

				((PerunButton) widget).setText(getLabelOrShortName());
				((PerunButton) widget).getTooltip().setTitle(translation.checkAndSubmit());
				((PerunButton) widget).getTooltip().reconfigure();

			} else if (ApplicationFormItem.ApplicationFormItemType.VALIDATED_EMAIL.equals(item.getFormItem().getType())) {

				if (!preFilledValue.isEmpty()) {
					if (emailSelect.getValue(emailSelect.getItemCount() - 1).equals("custom")) {
						emailSelect.setItemText(emailSelect.getItemCount() - 1, translation.customValueEmail());
						emailSelect.refresh();
					}
				}

			}

		}

	}

	/**
	 * Generate form item
	 */
	private void generateFormItem() {

		if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.HTML_COMMENT)) {

			widget = new HTML(getLabelOrShortName());
			add(widget);

		} else if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.HEADING)) {

			Legend heading = new Legend();
			heading.setHTML(getLabelOrShortName());
			widget = heading;
			add(heading);

		} else {

			// normal items
			preFilledValue = (item.getPrefilledValue() != null && !item.getPrefilledValue().equalsIgnoreCase("null")) ? item.getPrefilledValue() : "";

			statusText.setVisible(false);
			helpText.setEmphasis(Emphasis.MUTED);

			FlowPanel wrapper = new FlowPanel();

			if (form.isOnlyPreview()) {
				widget = new HTML((item.getValue() != null && !item.getValue().equalsIgnoreCase("null") && !item.getValue().isEmpty()) ? item.getValue() : "");
				widget.addStyleName("perunFormItemValue");
				wrapper.add(statusText);
				wrapper.add(widget);
			} else {
				wrapper.add(generateWidget());
				wrapper.add(statusText);
				wrapper.add(helpText);
			}
			setTexts();

			formLabel.addStyleName("col-lg-2");
			wrapper.addStyleName("col-lg-9");

			add(formLabel);
			add(wrapper);

		}

	}

	/**
	 * Generate specific form item widget based on it's properties.
	 *
	 * !! Widget appended to form don't have to be the same as "input" field. !!
	 * @see #getFormItemWidget() to access object representing input field.
	 *
	 * @return widget appended to form
	 */
	private Widget generateWidget() {

		if (ApplicationFormItem.ApplicationFormItemType.TEXTFIELD.equals(item.getFormItem().getType())) {

			ExtendedTextBox box = new ExtendedTextBox();
			box.setMaxLength(TEXT_BOX_MAX_LENGTH);
			box.setWidth("400px");
			box.setValue(preFilledValue);
			currentValue = box;

			if (item.getFormItem().getRegex() != null) {
				box.setRegex(item.getFormItem().getRegex());
			}

			setDefaultInputChecker();
			setDefaultValidationTriggers();

			widget = box;
			return getFormItemWidget();

		} else if (ApplicationFormItem.ApplicationFormItemType.TEXTAREA.equals(item.getFormItem().getType())) {

			ExtendedTextArea box = new ExtendedTextArea();
			box.setMaxLength(TEXT_AREA_MAX_LENGTH);
			box.setValue(preFilledValue);
			box.setSize("600px", "150px");
			currentValue = box;

			if (item.getFormItem().getRegex() != null) {
				box.setRegex(item.getFormItem().getRegex());
			}

			setDefaultInputChecker();
			setDefaultValidationTriggers();

			widget = box;
			return getFormItemWidget();

		} else if (ApplicationFormItem.ApplicationFormItemType.VALIDATED_EMAIL.equals(item.getFormItem().getType())) {

			InputGroup group = new InputGroup();
			group.setWidth("400px");
			InputGroupAddon addon = new InputGroupAddon();
			addon.setIcon(IconType.ENVELOPE);
			final ExtendedTextBox box = new ExtendedTextBox();
			box.setMaxLength(TEXT_BOX_MAX_LENGTH);

			if (preFilledValue.contains(";")) {
				box.setValue(preFilledValue.split(";")[0]);
			} else {
				box.setValue(preFilledValue);
			}

			currentValue = box;

			if (item.getFormItem().getRegex() != null) {
				box.setRegex(item.getFormItem().getRegex());
			}

			setDefaultInputChecker();
			setDefaultValidationTriggers();

			widget = box;

			group.add(addon);
			group.add(box);

			emailSelect.addStyleName("emailFormItem");

			if (!preFilledValue.isEmpty()) {

				InputGroupButton addon2 = new InputGroupButton();

				if (preFilledValue.contains(";")) {
					for (String val : preFilledValue.split(";")) {
						emailSelect.addItem(val, val);
					}
				} else {
					emailSelect.addItem(preFilledValue, preFilledValue);
				}

				emailSelect.addItem(translation.customValueEmail(), "custom");

				emailSelect.addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {

						if (!emailSelect.getValue(emailSelect.getSelectedIndex()).equals("custom")) {
							box.setValue(emailSelect.getValue(emailSelect.getSelectedIndex()));
						} else {
							box.setValue("");
						}
						ValueChangeEvent.fire(box, box.getValue());

					}
				});

				emailSelect.setWidth("35px");
				addon2.add(emailSelect);

				group.add(addon2);

			}

			return group;

		} else if (ApplicationFormItem.ApplicationFormItemType.USERNAME.equals(item.getFormItem().getType())) {

			InputGroup group = new InputGroup();
			group.setWidth("400px");
			final InputGroupAddon addon = new InputGroupAddon();
			addon.setIcon(IconType.USER);
			ExtendedTextBox box = new ExtendedTextBox();
			box.setMaxLength(TEXT_BOX_MAX_LENGTH);
			box.setValue(preFilledValue);
			currentValue = box;

			if (item.getFormItem().getRegex() != null) {
				box.setRegex(item.getFormItem().getRegex());
			}

			setDefaultValidationTriggers();

			if (!getValue().equalsIgnoreCase("")) {
				// item was prefilled
				setDefaultInputChecker();
				box.setEnabled(false);

			} else {

				validator = new PerunFormItemValidator() {

					private int returnCode = -1;
					private boolean processing = false;
					private boolean valid = true;
					private Map<String, Boolean> validMap = new HashMap<String, Boolean>();
					String loginNamespace = item.getFormItem().getPerunAttribute().substring(PERUN_ATTRIBUTE_LOGIN_NAMESPACE_POSITION);

					@Override
					public boolean validate(boolean forceNew) {

						// use value of previous check
						if (!forceNew) return valid;

						valid = true;

						// CHECK IF IS EMPTY (ONLY FOR REQUIRED)
						if (isRequired()) {
							valid = !getValue().isEmpty();
							if (!valid) {
								setValidationState(ValidationState.ERROR, translation.cantBeEmpty());
								returnCode = 1;
								// hard error
								return valid;
							}
						}

						// CHECK MAX-LENGTH
						valid = checkMaxLength();
						if (!valid) {
							setValidationState(ValidationState.ERROR, translation.tooLong());
							returnCode = 3;
							// hard error
							return valid;
						}

						// CHECK NATIVE REGEX VALUE
						valid = checkRegex(Utils.LOGIN_VALUE_MATCHER);
						if (!valid) {
							if (getErrorText().isEmpty()) {
								setValidationState(ValidationState.ERROR, translation.incorrectFormat());
								returnCode = 4;
							} else {
								setValidationState(ValidationState.ERROR);
							}
							return valid;
						}

						// CHECK AVAILABILITY IF NOT CHECKED BEFORE
						if (validMap.get(getValue()) == null) {

							UsersManager.isLoginAvailable(loginNamespace, getValue(), new JsonEvents() {

								private String login = getValue();

								@Override
								public void onFinished(JavaScriptObject jso) {
									// store to map
									BasicOverlayObject object = jso.cast();
									validMap.put(login, object.getBoolean());
									// if value is current
									if (login.equals(getValue())) {
										valid = object.getBoolean();
										processing = false;
										addon.setIcon(IconType.USER);
										addon.setIconSpin(false);
										if (!valid) {
											setValidationState(ValidationState.ERROR, translation.loginNotAvailable());
											returnCode = 6;
										}
									}
									// re-done validation
									validate(true);
								}

								@Override
								public void onError(PerunException error) {
									if (login.equals(getValue())) {
										valid = false;
										processing = false;
										addon.setIcon(IconType.USER);
										addon.setIconSpin(false);
										setValidationState(ValidationState.ERROR, translation.checkingLoginFailed());
										returnCode = 7;
									}
								}

								@Override
								public void onLoadingStart() {
									setValidationState(ValidationState.WARNING, translation.checkingLogin());
									returnCode = 9;
									processing = true;
									addon.setIcon(IconType.SPINNER);
									addon.setIconSpin(true);
								}

							});

						} else {
							processing = false;
							valid = validMap.get(getValue());
							if (!valid) {
								setValidationState(ValidationState.ERROR, translation.loginNotAvailable());
								returnCode = 6;
							}
							addon.setIcon(IconType.USER);
							addon.setIconSpin(false);
						}

						if (valid && !processing) {
							setValidationState(ValidationState.SUCCESS);
							returnCode = 0;
						}
						return valid && !processing;

					}

					@Override
					public boolean isProcessing() {
						return processing;
					}

					@Override
					public void translate() {

						if (returnCode == 0) {
							setValidationState(ValidationState.SUCCESS);
						} else if (returnCode == -1) {
							setValidationState(ValidationState.NONE);
						} else if (returnCode == 1) {
							setValidationState(ValidationState.ERROR, translation.cantBeEmpty());
						} else if (returnCode == 3) {
							setValidationState(ValidationState.ERROR, translation.tooLong());
						} else if (returnCode == 4) {
							if (getErrorText().isEmpty()) {
								setValidationState(ValidationState.ERROR, translation.incorrectFormat());
							} else {
								setValidationState(ValidationState.ERROR);
							}
						} else if (returnCode == 6) {
							setValidationState(ValidationState.ERROR, translation.loginNotAvailable());
						} else if (returnCode == 7) {
							setValidationState(ValidationState.ERROR, translation.checkingLoginFailed());
						} else if (returnCode == 9) {
							setValidationState(ValidationState.ERROR, translation.checkingLogin());
						}

					}

				};

			}

			widget = box;

			group.add(addon);
			group.add(box);

			return group;

		} else if (ApplicationFormItem.ApplicationFormItemType.PASSWORD.equals(item.getFormItem().getType())) {

			FlowPanel fw = new FlowPanel();
			InputGroup group = new InputGroup();
			group.setWidth("400px");
			InputGroupAddon addon = new InputGroupAddon();
			addon.setIcon(IconType.KEY);
			final ExtendedPasswordTextBox box = new ExtendedPasswordTextBox();
			final ExtendedPasswordTextBox box2 = new ExtendedPasswordTextBox();
			box.setMaxLength(TEXT_BOX_MAX_LENGTH);
			box2.setMaxLength(TEXT_BOX_MAX_LENGTH);
			box.addStyleName("passwordFormItemFirst");
			box2.addStyleName("passwordFormItemLast");

			if (item.getFormItem().getRegex() != null) {
				box.setRegex(item.getFormItem().getRegex());
			}

			currentValue = box;
			widget = box;

			validator = new PerunFormItemValidator() {

				private boolean valid = true;
				private int returnCode = -1;

				@Override
				public boolean validate(boolean forceNew) {

					if (!forceNew) return valid;

					valid = true;

					valid = box.getValue().equals(box2.getValue());
					if (!valid) {
						setValidationState(ValidationState.ERROR, translation.passMismatch());
						returnCode = 11;
						// hard error
						return valid;
					}

					// CHECK IF IS EMPTY (ONLY FOR REQUIRED)
					if (isRequired()) {
						valid = (!box.getValue().isEmpty() && !box2.getValue().isEmpty());
						if (!valid) {
							setValidationState(ValidationState.ERROR, translation.passEmpty());
							returnCode = 10;
							// hard error
							return valid;
						}
					}

					// check max-length
					if (box.getValue().length() > TEXT_BOX_MAX_LENGTH || box2.getValue().length() > TEXT_BOX_MAX_LENGTH) {
						valid = false;
						setValidationState(ValidationState.ERROR, translation.tooLong());
						returnCode = 3;
						// hard error
						return valid;
					}

					// check regex
					if (item.getFormItem().getRegex() != null) {
						if (!item.getFormItem().getRegex().isEmpty()) {
							RegExp regExp = RegExp.compile(item.getFormItem().getRegex());
							MatchResult matcher = regExp.exec(box.getValue());
							boolean matchFound = (matcher != null); // equivalent to regExp.test(inputStr);
							if(!matchFound){
								valid = false;
								if (getErrorText().isEmpty()) {
									setValidationState(ValidationState.ERROR, translation.incorrectFormat());
								} else {
									setValidationState(ValidationState.ERROR);
								}
								returnCode = 4;
								return valid;
							}
						}
					}

					if (valid) {
						setValidationState(ValidationState.SUCCESS);
						returnCode = 0;
					}

					return valid;

				}

				@Override
				public boolean isProcessing() {
					return false;
				}

				@Override
				public void translate() {

					if (returnCode == 0) {
						setValidationState(ValidationState.SUCCESS);
					} else if (returnCode == -1) {
						setValidationState(ValidationState.NONE);
					} else if (returnCode == 10) {
						setValidationState(ValidationState.ERROR, translation.passEmpty());
					} else if (returnCode == 3) {
						setValidationState(ValidationState.ERROR, translation.tooLong());
					} else if (returnCode == 4) {
						if (getErrorText().isEmpty()) {
							setValidationState(ValidationState.ERROR, translation.incorrectFormat());
						} else {
							setValidationState(ValidationState.ERROR);
						}
					} else if (returnCode == 11) {
						setValidationState(ValidationState.ERROR, translation.passMismatch());
					}

				}

			};

			setDefaultValidationTriggers();

			box2.addKeyUpHandler(new KeyUpHandler() {
				public void onKeyUp(KeyUpEvent event) {
					DomEvent.fireNativeEvent(Document.get().createChangeEvent(), box2);
				}
			});

			box2.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					isValid(true);
				}
			});

			box2.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					isValid(true);
				}
			});

			group.add(addon);
			group.add(box);
			group.add(box2);

			fw.add(group);

			return fw;

		} else if (ApplicationFormItem.ApplicationFormItemType.SELECTIONBOX.equals(item.getFormItem().getType())) {

			currentValue = new ExtendedTextBox();
			currentValue.setValue(preFilledValue);

			final cz.metacentrum.perun.wui.registrar.widgets.Select select = new cz.metacentrum.perun.wui.registrar.widgets.Select();
			select.setWidth("400px");
			select.setShowTick(true);

			// when changed, update value
			select.addChangeHandler(new ChangeHandler() {
				public void onChange(ChangeEvent event) {
					String value = select.getAllSelectedValues().get(0);
					// fire change event to check for correct input
					currentValue.setValue(value);
					select.refresh();
					ValueChangeEvent.fire(currentValue, currentValue.getValue());
				}
			});

			// check on hidden TextBox
			setDefaultValidationTriggers();
			setDefaultInputChecker();

			widget = select;
			return getFormItemWidget();

		} else if (ApplicationFormItem.ApplicationFormItemType.COMBOBOX.equals(item.getFormItem().getType())) {

			currentValue = new ExtendedTextBox();
			currentValue.setWidth("400px");
			currentValue.setValue(preFilledValue);
			currentValue.addStyleName("comboboxFormItemLast");

			final cz.metacentrum.perun.wui.registrar.widgets.Select select = new cz.metacentrum.perun.wui.registrar.widgets.Select();
			select.setWidth("400px");
			select.setShowTick(true);

			// when changed, update value
			select.addChangeHandler(new ChangeHandler() {
				public void onChange(ChangeEvent event) {
					String value = select.getAllSelectedValues().get(0);
					// fire change event to check for correct input
					if (value.equals("custom")) {
						currentValue.setVisible(true);
						currentValue.setValue("");
						currentValue.setFocus(true);
						select.addStyleName("comboboxFormItemFirst");
					} else {
						currentValue.setVisible(false);
						currentValue.setValue(value);
						select.removeStyleName("comboboxFormItemFirst");
						// FIXME - hack bug in BootstrapSelect
						select.getElement().getNextSiblingElement().removeClassName("comboboxFormItemFirst");

					}
					select.refresh();
					ValueChangeEvent.fire(currentValue, currentValue.getValue());
				}
			});

			currentValue.setVisible(false);

			FlowPanel group = new FlowPanel();
			group.setWidth("400px");
			group.add(select);
			group.add(currentValue);

			// check on hidden TextBox
			setDefaultValidationTriggers();
			setDefaultInputChecker();

			widget = group;
			return getFormItemWidget();

		} else if (ApplicationFormItem.ApplicationFormItemType.CHECKBOX.equals(item.getFormItem().getType())) {

			currentValue = new ExtendedTextBox();
			currentValue.setValue(preFilledValue);

			String options = item.getFormItem().getItemTexts(lang).getOptions();
			Map<String,String> boxContents = parseSelectionBox(options);
			final Map<CheckBox, String> boxValueMap = new HashMap<CheckBox, String>();

			ArrayList<String> keyList = Utils.setToList(boxContents.keySet());

			FlowPanel wrapper = new FlowPanel();
			wrapper.getElement().addClassName("checkboxFormItem");

			for(String key : keyList) {

				final CheckBox checkbox = new CheckBox(boxContents.get(key));
				// pre-fill
				for (String s : getValue().split("\\|")) {
					if (key.trim().equals(s.trim())) {
						checkbox.setValue(true);
					}
				}
				boxValueMap.put(checkbox, key);

				checkbox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {

						// rebuild value
						String value = "";
						for(Map.Entry<CheckBox, String> innerEntry : boxValueMap.entrySet()) {
							if (innerEntry.getKey().getValue()) {
								// put in selected values
								value += innerEntry.getValue()+"|";
							}
						}
						if (value.length() > 1) {
							value = value.substring(0, value.length()-1);
						}

						currentValue.setValue(value, true);

					}
				});

				wrapper.add(checkbox);

			}

			// check on hidden TextBox
			setDefaultValidationTriggers();
			setDefaultInputChecker();

			widget = wrapper;
			return getFormItemWidget();

		} else if (ApplicationFormItem.ApplicationFormItemType.RADIO.equals(item.getFormItem().getType())) {

			currentValue = new ExtendedTextBox();
			currentValue.setValue(preFilledValue);

			String options = item.getFormItem().getItemTexts(lang).getOptions();
			Map<String,String> boxContents = parseSelectionBox(options);
			ArrayList<String> keyList = Utils.setToList(boxContents.keySet());

			FlowPanel wrapper = new FlowPanel();

			// fill not selected option
			if (!isRequired()) {

				final Radio radio = new Radio("radio"+item.getFormItem().getId(), translation.notSelected());
				// pre-fill
				if ("".equals(getValue().trim())) {
					radio.setValue(true);
				}

				radio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						currentValue.setValue("", true);
					}
				});

				wrapper.add(radio);

			}

			// fill standard values
			for(final String key : keyList) {

				final Radio radio = new Radio(boxContents.get(key));
				radio.setName("radio"+item.getFormItem().getId());
				// pre-fill
				if (key.trim().equals(getValue().trim())) {
					radio.setValue(true);
				}

				radio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						currentValue.setValue(key, true);
					}
				});

				wrapper.add(radio);

			}

			// check on hidden TextBox
			setDefaultValidationTriggers();
			setDefaultInputChecker();

			widget = wrapper;
			return getFormItemWidget();

		} else if (ApplicationFormItem.ApplicationFormItemType.FROM_FEDERATION_SHOW.equals(item.getFormItem().getType())) {

			ExtendedTextBox box = new ExtendedTextBox();
			box.setMaxLength(TEXT_BOX_MAX_LENGTH);
			box.setWidth("400px");
			box.setValue(preFilledValue);
			box.setEnabled(false);
			currentValue = box;

			setDefaultInputChecker();

			widget = box;
			return getFormItemWidget();

		} else if (ApplicationFormItem.ApplicationFormItemType.FROM_FEDERATION_HIDDEN.equals(item.getFormItem().getType())) {

			ExtendedTextBox box = new ExtendedTextBox();
			box.setMaxLength(TEXT_BOX_MAX_LENGTH);
			box.setWidth("400px");
			box.setValue(preFilledValue);
			box.setEnabled(false);
			currentValue = box;
			widget = box;
			return getFormItemWidget();

		} else if (ApplicationFormItem.ApplicationFormItemType.SUBMIT_BUTTON.equals(item.getFormItem().getType()) ||
				ApplicationFormItem.ApplicationFormItemType.AUTO_SUBMIT_BUTTON.equals(item.getFormItem().getType())) {

			final PerunButton button = new PerunButton();
			button.setIcon(IconType.CHEVRON_RIGHT);
			button.setIconFixedWidth(true);
			button.setTooltipText(translation.checkAndSubmit());
			button.getTooltip().setPlacement(Placement.TOP);
			button.setType(ButtonType.SUCCESS);
			button.setText(getLabelOrShortName());

			button.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (form != null) {
						form.submit(button);
					}
				}
			});

			widget = button;

			Div wrapper = new Div();
			wrapper.setMarginTop(20);
			wrapper.setMarginBottom(40);
			wrapper.add(button);

			return wrapper;

		}

		return new Paragraph("WIDGET NOT DEFINED");

	}

	/**
	 * Return generated widget
	 *
	 * @return
	 */
	public Widget getFormItemWidget() {

		return this.widget;

	}

	/**
	 * Set default instance of PerunFormItemValidator for this form item.
	 *
	 * Form item is validated by isRequired() and isValid() for TextBoxes
	 */
	public void setDefaultInputChecker() {

		this.validator = new PerunFormItemValidator() {

			// last check value
			boolean valid = true;
			private int returnCode = -1;

			@Override
			public boolean validate(boolean forceNew) {

				// use value of previous check
				if (!forceNew) return valid;

				valid = true;

				// check mail regex
				if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.VALIDATED_EMAIL)) {
					if (isRequired()) {
						// is required = CHECK
						if (!(Utils.isValidEmail(getValue()) && !getValue().isEmpty())) {
							setValidationState(ValidationState.ERROR, translation.incorrectEmail());
							returnCode = 5;
							valid = false;
							// hard error
							return valid;
						}
					} else {
						if (!getValue().isEmpty()) {
							// not required but non empty = CHECK
							if (!Utils.isValidEmail(getValue())) {
								setValidationState(ValidationState.ERROR, translation.incorrectEmail());
								returnCode = 5;
								valid = false;
								// hard error
								return valid;
							}
						}
					}
				}

				// CHECK IF IS EMPTY (ONLY FOR REQUIRED)
				if (isRequired()) {
					valid = !getValue().isEmpty();
					if (!valid) {
						if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.SELECTIONBOX) ||
								item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.COMBOBOX) ||
								item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.RADIO) ||
								item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.CHECKBOX)) {
							setValidationState(ValidationState.ERROR, translation.cantBeEmptySelect());
							returnCode = 2;
						} else {
							setValidationState(ValidationState.ERROR, translation.cantBeEmpty());
							returnCode = 1;
						}
						// hard error
						return valid;
					}
				}

				// CHECK MAX-LENGTH
				valid = checkMaxLength();
				if (!valid) {
					setValidationState(ValidationState.ERROR, translation.tooLong());
					returnCode = 3;
					// hard error
					return valid;
				}

				// CHECK NATIVE REGEX VALUE
				valid = checkRegex();
				if (!valid) {
					if (getErrorText().isEmpty()) {
						setValidationState(ValidationState.ERROR, translation.incorrectFormat());
					} else {
						setValidationState(ValidationState.ERROR);
					}
					returnCode = 4;
				}

				if (valid) {

					// better check on emails
					if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.VALIDATED_EMAIL)) {

						// multi-value pre-fill
						if (preFilledValue.contains(";")) {

							for (String s : preFilledValue.split(";")) {
								if (s.equals(getValue())) {

									setValidationState(ValidationState.SUCCESS);
									returnCode = 0;
									return valid;

								}
							}

						} else {

							// single value pre-fill
							if (getValue().equals(preFilledValue)) {

								setValidationState(ValidationState.SUCCESS);
								returnCode = 0;
								return valid;

							}

						}

						// must validate email
						setValidationState(ValidationState.WARNING, translation.mustValidateEmail());
						returnCode = 12;
						return valid;

					} else {

						// normal form item
						setValidationState(ValidationState.SUCCESS);
						returnCode = 0;

					}

				}

				return valid;

			}

			@Override
			public boolean isProcessing() {
				return false;
			}

			@Override
			public void translate() {

				if (returnCode == 0) {
					setValidationState(ValidationState.SUCCESS);
				} else if (returnCode == -1) {
					setValidationState(ValidationState.NONE);
				} else if (returnCode == 1) {
					setValidationState(ValidationState.ERROR, translation.cantBeEmpty());
				} else if (returnCode == 2) {
					setValidationState(ValidationState.ERROR, translation.cantBeEmptySelect());
				} else if (returnCode == 3) {
					setValidationState(ValidationState.ERROR, translation.tooLong());
				} else if (returnCode == 4) {
					if (getErrorText().isEmpty()) {
						setValidationState(ValidationState.ERROR, translation.incorrectFormat());
					} else {
						setValidationState(ValidationState.ERROR);
					}
				} else if (returnCode == 5) {
					setValidationState(ValidationState.ERROR, translation.incorrectEmail());
				} else if (returnCode == 12) {
					setValidationState(ValidationState.WARNING, translation.mustValidateEmail());
				}


			}

		};

	}

	/**
	 * Set default validation triggers on form item widget.
	 */
	private void setDefaultValidationTriggers() {

		if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.USERNAME)) {

			currentValue.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					isValid(true);
				}
			});

			currentValue.addKeyUpHandler(new KeyUpHandler() {
				public void onKeyUp(KeyUpEvent event) {
					DomEvent.fireNativeEvent(Document.get().createBlurEvent(), currentValue);
				}
			});

		} else {

			currentValue.addKeyUpHandler(new KeyUpHandler() {
				public void onKeyUp(KeyUpEvent event) {
					DomEvent.fireNativeEvent(Document.get().createChangeEvent(), currentValue);

					// select proper value in selection box for emails
					if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.VALIDATED_EMAIL)) {
						for (int i=0; i<emailSelect.getItemCount(); i++) {
							if (emailSelect.getValue(i).equals(getValue())) {
								emailSelect.setSelectedIndex(i);
								emailSelect.refresh();
								return;
							}
						}
						emailSelect.setSelectedIndex(emailSelect.getItemCount()-1);
						emailSelect.refresh();
					}

				}
			});

			currentValue.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					isValid(true);

					// select proper value in selection box for emails
					if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.VALIDATED_EMAIL)) {
						for (int i=0; i<emailSelect.getItemCount(); i++) {
							if (emailSelect.getValue(i).equals(getValue())) {
								emailSelect.setSelectedIndex(i);
								emailSelect.refresh();
								return;
							}
						}
						emailSelect.setSelectedIndex(emailSelect.getItemCount()-1);
						emailSelect.refresh();
					}

				}
			});

			currentValue.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {

					isValid(true);

					// select proper value in selection box for emails
					if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.VALIDATED_EMAIL)) {
						for (int i=0; i<emailSelect.getItemCount(); i++) {
							if (emailSelect.getValue(i).equals(getValue())) {
								emailSelect.setSelectedIndex(i);
								emailSelect.refresh();
								return;
							}
						}
						emailSelect.setSelectedIndex(emailSelect.getItemCount()-1);
						emailSelect.refresh();
					}

				}
			});

		}

	}

	/**
	 * Safely return items label/description. If nothing defined, return empty string.
	 *
	 * @return items label or shortName
	 */
	public String getLabelOrShortName() {

		if (item.getFormItem() != null) {
			if (item.getFormItem().getItemTexts(lang) != null) {
				if (item.getFormItem().getItemTexts(lang).getLabel() == null) return "";
				return item.getFormItem().getItemTexts(lang).getLabel();
			}
			if (item.getFormItem().getShortName() == null) return "";
			return item.getFormItem().getShortName();
		} else {
			if (item.getShortname() == null) return "";
			return item.getShortname();
		}

	}

	/**
	 * Item is considered required only when "required" by definition and user can modify value.
	 *
	 * @return TRUE if required / FALSE = not required
	 */
	public boolean isRequired() {

		if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.FROM_FEDERATION_HIDDEN)) {
			return false;
		}
		if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.FROM_FEDERATION_SHOW)) {
			return false;
		}
		return item.getFormItem().isRequired();

	}

	/**
	 * Return true if form item should be visible on form.
	 *
	 * @return TRUE = visible / FALSE = hidden
	 */
	public boolean isVisible() {

		if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.FROM_FEDERATION_HIDDEN)) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Determine if current form item value is valid.
	 * This method perform check on all item properties like required, regex, max-length etc.
	 *
	 * Form item is colored based on result.
	 *
	 * @param forceNew = TRUE = check value again / FALSE = return value from last check
	 * @return TRUE = valid / FALSE = not valid
	 */
	public boolean isValid(boolean forceNew) {

		if (this.validator != null) {
			return validator.validate(forceNew);
		}
		return true;

	}

	/**
	 * Check current item value against it's regex.
	 * If no regex provided, then check returns TRUE.
	 *
	 * @return TRUE = OK with regex / FALSE = fails with regex
	 */
	public boolean checkRegex() {
		return checkRegex(null);
	}

	/**
	 * Check current item value against it's regex.
	 * If no regex provided, then check returns TRUE.
	 *
	 * @param customRegex check against custom regex first, then use native
	 *
	 * @return TRUE = OK with regex / FALSE = fails with regex
	 */
	public boolean checkRegex(String customRegex) {

		boolean result = true;

		if (customRegex != null && !customRegex.isEmpty()) {

			RegExp regExp = RegExp.compile(customRegex);
			MatchResult matcher = regExp.exec(getValue());
			boolean matchFound = (matcher != null); // equivalent to regExp.test(inputStr);
			if(!matchFound){
				result = false;
			}

		}

		if (item.getFormItem().getRegex() != null) {
			if (!item.getFormItem().getRegex().isEmpty()) {
				RegExp regExp = RegExp.compile(item.getFormItem().getRegex());
				MatchResult matcher = regExp.exec(getValue());
				boolean matchFound = (matcher != null); // equivalent to regExp.test(inputStr);
				if(!matchFound){
					result = false;
				}
			}
		}

		return result;

	}

	/**
	 * Check current item value against max-length constraints.
	 *
	 * @return TRUE = OK with max-length / FALSE = fails with max-length
	 */
	public boolean checkMaxLength() {

		if (item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.TEXTAREA)) {
			return getValue().length() < TEXT_AREA_MAX_LENGTH;
		} else {
			return getValue().length() < TEXT_BOX_MAX_LENGTH;
		}

	}

	@Override
	public void setValidationState(ValidationState state) {

		super.setValidationState(state);

		if (ValidationState.ERROR.equals(state)) {
			if (!getErrorText().isEmpty()) {
				statusText.setEmphasis(Emphasis.DANGER);
				statusText.setVisible(true);
				statusText.setText(getErrorText());
			}
		} else if (ValidationState.WARNING.equals(state)) {
			if (!getErrorText().isEmpty()) {
				statusText.setEmphasis(Emphasis.WARNING);
				statusText.setVisible(true);
				statusText.setText(getErrorText());
			}
		} else {
			statusText.setEmphasis(Emphasis.DEFAULT);
			statusText.setText("");
			statusText.setVisible(false);
		}

	}

	public void setValidationState(ValidationState state, String customMessage) {

		super.setValidationState(state);

		if (ValidationState.ERROR.equals(state)) {
			statusText.setEmphasis(Emphasis.DANGER);
			statusText.setVisible(true);
			statusText.setText(customMessage);
		} else if (ValidationState.WARNING.equals(state)) {
			statusText.setEmphasis(Emphasis.WARNING);
			statusText.setVisible(true);
			statusText.setText(customMessage);
		} else {
			statusText.setEmphasis(Emphasis.DEFAULT);
			statusText.setText("");
			statusText.setVisible(false);
		}

	}

	/**
	 * Parses the "options" into MAP
	 *
	 * Standard HTML selection box, options are in for each locale in ItemTexts.label separated by | with values separated by #.
	 * Thus a language selection box would have for English locale the label <code>cs#Czech|en#English</code>.
	 *
	 * @param options Source string
	 * @return Map with key/value pairs of options
	 */
	static private Map<String, String> parseSelectionBox(String options){

		Map<String, String> map = new HashMap<String, String>();

		if(options == null || options.length() == 0){
			return map;
		}

		String[] keyValue = options.split("\\|");

		for(int i = 0; i < keyValue.length; i++){

			String kv = keyValue[i];

			String[] split = kv.split("#", 2);

			if(split.length != 2){
				continue;
			}

			String key = split[0];
			String value = split[1];
			map.put(key, value);
		}
		return map;
	}

	/**
	 * Return error message from item. If no specified, then return empty string.
	 *
	 * @return Error message or empty string.
	 */
	public String getErrorText() {

		if (item.getFormItem() != null) {
			if (item.getFormItem().getItemTexts(lang) != null) {
				if (item.getFormItem().getItemTexts(lang).getErrorText() != null) {
					return item.getFormItem().getItemTexts(lang).getErrorText();
				}
			}
		}
		return "";

	}

	/**
	 * Get current item`s value (inserted / selected by user)
	 *
	 * @return current safe item`s value
	 */
	public String getValue() {

		if (currentValue != null) return currentValue.getValue();
		return "";

	}

}