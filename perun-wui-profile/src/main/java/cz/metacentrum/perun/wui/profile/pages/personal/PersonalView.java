package cz.metacentrum.perun.wui.profile.pages.personal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.beans.Locale;
import cz.metacentrum.perun.wui.client.resources.beans.PersonalAttribute;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.JsonUtils;
import cz.metacentrum.perun.wui.json.managers.ConfigManager;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.model.beans.User;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileResources;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.AlertErrorReporter;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedPasswordTextBox;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Span;
import org.gwtbootstrap3.client.ui.html.Text;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * View for displaying personal info about user
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PersonalView extends ViewWithUiHandlers<PersonalUiHandlers> implements PersonalPresenter.MyView {

	private static final Map<String, String> attributeNamesTranslations = new HashMap<>();

	private static final String URN_ORGANIZATION = "urn:perun:user:attribute-def:def:organization";
	private static final String URN_PREFERRED_LANGUAGE = "urn:perun:user:attribute-def:def:preferredLanguage";
	private static final String URN_PREFERRED_EMAIL = "urn:perun:user:attribute-def:def:preferredMail";
	private static final String URN_TIMEZONE = "urn:perun:user:attribute-def:def:timezone";
	private static final String URN_USER_DISPLAY_NAME = "urn:perun:user:attribute-def:core:displayName";
	private static final String URN_PREFERRED_SHELLS = "urn:perun:user:attribute-def:def:preferredShells";
	private static final String URN_BONA_FIDE_STATUS = "urn:perun:user:attribute-def:virt:elixirBonaFideStatus";

	interface PersonalViewUiBinder extends UiBinder<Widget, PersonalView> {
	}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	private PerunProfileResources resources = GWT.create(PerunProfileResources.class);

	@UiField
	PerunLoader loader;

	@UiField
	Text text;
	@UiField
	Small small;

	@UiField
	Div personalInfo;

	@UiField
	Form updateEmailForm;
	@UiField
	Button updateEmailBtn;
	@UiField
	Input newUpdateEmail;
	@UiField
	FormGroup updateEmailFormGroup;
	@UiField
	HelpBlock updateEmailHelpBlock;
	@UiField
	PerunLoader updateEmailLoader;
	@UiField
	Alert alreadyEmailRequests;
	@UiField
	Modal updateEmailModal;
	@UiField
	FormLabel updateEmailLabel;
	@UiField
	Modal verifyEmailModal;
	@UiField
	PerunLoader verifyEmailLoader;
	@UiField
	Button doneButton;
	@UiField
	Alert mailChangeError;

	ExtendedPasswordTextBox oldPass;
	ExtendedPasswordTextBox newPass;
	ExtendedPasswordTextBox newPass2;

	FormGroup formGroup;
	FormGroup formGroup2;
	HelpBlock helpBlock;
	HelpBlock helpBlock2;

	String previousOldPassVal = null;
	String previousNewPassVal = null;
	String previousNewPass2Val = null;

	@Inject
	public PersonalView(PersonalViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));

		loadAttributeNamesTranslations();

		updateEmailBtn.addClickHandler((event) ->
				updateEmailForm.submit()
		);

		updateEmailForm.addSubmitHandler(submitEvent -> {
			submitEvent.cancel();
			getUiHandlers().updateEmail(newUpdateEmail.getValue());
		});

		updateEmailLoader.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
		updateEmailBtn.setText(translation.sendValidationEmail());
		updateEmailModal.setTitle(translation.updateEmailModalTitle());
		updateEmailLabel.setText(translation.newPreferredEmail());

		doneButton.addClickHandler(clickEvent -> Window.Location.replace(Window.Location.getPath()));
	}

	private void loadAttributeNamesTranslations() {
		attributeNamesTranslations.put(URN_USER_DISPLAY_NAME, translation.name());
		attributeNamesTranslations.put(URN_ORGANIZATION, translation.organization());
		attributeNamesTranslations.put(URN_PREFERRED_LANGUAGE, translation.preferredLang());
		attributeNamesTranslations.put(URN_PREFERRED_EMAIL, translation.preferredMail());
		attributeNamesTranslations.put(URN_TIMEZONE, translation.timezone());
		attributeNamesTranslations.put(URN_PREFERRED_SHELLS, translation.preferredShells());
		attributeNamesTranslations.put(URN_BONA_FIDE_STATUS, translation.bonaFideStatus());
	}

	@Override
	public void setUser(RichUser user) {
		personalInfo.clear();
		loader.onFinished();
		loader.setVisible(false);
		personalInfo.setVisible(true);

		text.setText(user.getFullName());

		List<PersonalAttribute> additionalAttributes = PerunConfiguration.getProfilePersonalAttributesToShow();

		for (PersonalAttribute personalAttribute : additionalAttributes) {
			Attribute attribute = user.getAttribute(personalAttribute.getUrn());
			addPersonalData(personalAttribute, attribute);
		}
	}

	@Override
	public void loadingUserStart() {
		loader.onLoading(translation.loadingUserData());
		loader.setVisible(true);
		personalInfo.setVisible(false);
	}

	@Override
	public void loadingUserError(PerunException ex) {
		loader.setVisible(true);
		loader.onError(ex, event -> getUiHandlers().loadUser());
		personalInfo.setVisible(false);
	}

	@Override
	public void checkingEmailUpdatesStart() {
		updateEmailLoader.onLoading("loading pending requests");
		updateEmailLoader.setVisible(true);
		updateEmailForm.setVisible(false);
		alreadyEmailRequests.setVisible(false);
	}

	@Override
	public void checkingEmailUpdatesError(PerunException ex) {
		updateEmailLoader.onError(ex, clickEvent -> getUiHandlers().checkEmailRequestPending());
		updateEmailLoader.setVisible(true);
		updateEmailForm.setVisible(true);
	}

	@Override
	public void setEmailUpdateRequests(List<String> pendingEmails) {
		updateEmailLoader.setVisible(false);
		updateEmailLoader.onFinished();
		updateEmailForm.setVisible(true);
		updateEmailFormGroup.setValidationState(ValidationState.NONE);
		updateEmailHelpBlock.setVisible(false);
		if (!pendingEmails.isEmpty()) {
			alreadyEmailRequests.setVisible(true);
			String emails = "";
			for (String email : pendingEmails) {
				if (pendingEmails.indexOf(email) != 0) {
					emails += ", ";
				}
				emails += email;
			}
			alreadyEmailRequests.setText(translation.haveRequestedEmailUpdate(emails));
		}
		updateEmailModal.hide();
	}

	@Override
	public void requestingEmailUpdateStart() {
		updateEmailLoader.onLoading(translation.requestingEmailUpdate());
		updateEmailFormGroup.setValidationState(ValidationState.NONE);
		updateEmailHelpBlock.setVisible(false);
		updateEmailLoader.setVisible(true);
		updateEmailForm.setVisible(false);
	}

	@Override
	public void requestingEmailUpdateError(PerunException ex, final String email) {
		if (ex == null) {
			updateEmailLoader.setVisible(false);
			updateEmailFormGroup.setValidationState(ValidationState.ERROR);
			updateEmailHelpBlock.setVisible(true);
			updateEmailHelpBlock.setText(translation.wrongEmailFormat());
		} else {
			updateEmailLoader.onError(ex, clickEvent -> getUiHandlers().updateEmail(email));
			updateEmailLoader.setVisible(true);
			updateEmailForm.setVisible(true);
			updateEmailFormGroup.setValidationState(ValidationState.NONE);
			updateEmailHelpBlock.setVisible(false);
		}
	}

	@Override
	public void verifyEmailChangeStart() {
		doneButton.setEnabled(false);
		verifyEmailLoader.getWidget().getElement().getStyle().setMarginTop(0, Style.Unit.PX);
		verifyEmailLoader.setEmptyMessage("");
		verifyEmailModal.setTitle(translation.verifyEmailChangeTitle());
		verifyEmailLoader.onLoading(translation.verifyingEmailChange());
		verifyEmailModal.show();
	}

	@Override
	public void verifyEmailChangeError(PerunException ex) {
		doneButton.setEnabled(true);
		mailChangeError.setVisible(true);
		mailChangeError.setText(translation.mailChangeError());
		verifyEmailLoader.setVisible(false);
	}

	@Override
	public void verifyEmailChangeDone(String email) {
		doneButton.setEnabled(true);
		verifyEmailLoader.setEmptyMessage(translation.verifyingEmailChangeDone(SafeHtmlUtils.fromString(email).asString()));
		verifyEmailLoader.onFinishedEmpty();
	}

	/**
	 * Adds user data to overview. If the given value is null then
	 * '-' is shown. If there is a translated name for urn of given personal attribute,
	 * then it is used. If there is not, the defaultName is shown.
	 * If there is not a translated name and the default name is null,
	 * the name is obtained from given urn.
	 *
	 * @param personalAttribute personal attribute
	 * @param attribute attribute to be shown
	 */
	private void addPersonalData(PersonalAttribute personalAttribute, Attribute attribute) {
		String defaultName = attribute == null ? null : attribute.getDisplayName();
		Row row = new Row();
		row.setPaddingBottom(10);

		Column labelColumn = createLabelColumn(personalAttribute, defaultName);

		Column valueColumn = createValueColumn(attribute, personalAttribute);

		row.add(labelColumn);
		row.add(valueColumn);

		personalInfo.add(row);
	}

	private void addEmailChangeButton(Column column) {
		Button updateButton = new Button();
		updateButton.setSize(ButtonSize.EXTRA_SMALL);
		updateButton.setMarginLeft(10);
		updateButton.setText(translation.updateEmail());
		updateButton.setDataTarget("#updateEmailModal");
		updateButton.setDataToggle(Toggle.MODAL);

		column.add(updateButton);
	}

	private void addPasswordChangeButton(String namespace, String login, Column column) {
		Button changePwdButton = new PerunButton();
		changePwdButton.setSize(ButtonSize.EXTRA_SMALL);
		changePwdButton.setMarginLeft(10);
		changePwdButton.setText(translation.changePassword());
		changePwdButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createModal(namespace, login);
			}
		});
		column.add(changePwdButton);
	}

	private void addPasswordResetButton(String namespace, Column column) {
		Button resetPwdButton = new PerunButton();
		resetPwdButton.setSize(ButtonSize.EXTRA_SMALL);
		resetPwdButton.setMarginLeft(10);
		resetPwdButton.setIcon(IconType.EXTERNAL_LINK);
		resetPwdButton.setIconPosition(IconPosition.RIGHT);
		resetPwdButton.setText(translation.resetPassword());
		resetPwdButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String baseUrl = Window.Location.getProtocol()+"//"+ Window.Location.getHost();
				if (PerunSession.getInstance().getRpcServer() != null) {
					baseUrl += "/" + PerunSession.getInstance().getRpcServer() + "/pwd-reset/?login-namespace="+namespace;
				}
				Window.Location.assign(baseUrl);
			}
		});
		column.add(resetPwdButton);
	}

	private Column createDescriptionColumn(PersonalAttribute personalAttribute) {
		Column column = new Column(ColumnSize.SM_6, ColumnSize.XS_6);

		String descriptionString = personalAttribute.getLocalizedDescription(Utils.getLocale());
		if (descriptionString == null) {
			// fallback to EN version
			descriptionString = personalAttribute.getLocalizedDescription(Locale.EN);
		}

		Widget descriptionWidget = getWidgetForText(descriptionString);
		descriptionWidget.addStyleName(resources.gss().personalDescriptionLabel());

		Span descriptionSpan = new Span();

		descriptionSpan.add(descriptionWidget);

		column.add(descriptionSpan);

		return column;
	}

	/**
	 * If the given String starts with http, then anchor widget is created.
	 * Text widget is returned otherwise
	 *
	 * @param text text
	 * @return appropriate widget for given text
	 */
	private Widget getWidgetForText(String text) {
		Span descriptionSpan = new Span();
		Text textEl = new Text();

		if (text != null) {
			for (String s : text.split("\\s+")) {
				// if the given substring is link
				if (s.startsWith("http")) {
					descriptionSpan.add(textEl);
					textEl = new Text(" ");

					String href;
					String hrefText;
					// check if the link contains text
					if (s.trim().matches(".*\\{.*}.*")) {
						String[] split = s.split("}");

						// append the text after the link text
						if (split.length > 1) {
							textEl.setText("");
							for(int i = 1; i < split.length; i++) {
								textEl.setText(textEl.getText() + split[i]);
							}
						}

						split = s.split("\\{");

						href = split[0];
						hrefText = split[1].substring(0, split[1].indexOf("}"));
					} else {
						href = s;
						hrefText = s;
					}
					descriptionSpan.add(new Anchor(hrefText, href));
				} else {
					textEl.setText(textEl.getText() + s + " ");
				}
			}
		}

		if (textEl.getText().trim().length() > 0) {
			descriptionSpan.add(textEl);
		}

		return descriptionSpan;
	}

	private Widget getWidgetForMap(Map<String, JSONValue> map, String description) {

		if (map.isEmpty()) return new Text("-");

		Div div = new Div();

		//table for description
		if(description != null && !description.equals("")) {
			CellTable<String> header = new CellTable<>();
			TextColumn<String> headerData = new TextColumn<String>() {
				@Override
				public String getValue(String s) {
					return description;
				}
			};
			header.addColumn(headerData);
			header.setBordered(true);
			header.setCondensed(true);
			headerData.setCellStyleNames(resources.gss().noTopAndBottomPadding());
			headerData.setCellStyleNames(resources.gss().personalCellTableHeader());
			ArrayList<String> al = new ArrayList<>();
			al.add(description);
			header.setRowData(al);
			div.add(header);
		}

		//table for data
		CellTable<Map.Entry<String, String>> table = new CellTable<>();

		TextColumn<Map.Entry<String, String>> data0 = new TextColumn<Map.Entry<String, String>>() {
			@Override
			public String getValue(Map.Entry<String, String> entry) {
				return entry.getKey();
			}
		};
		table.addColumn(data0);

		TextColumn<Map.Entry<String, String>> data1 = new TextColumn<Map.Entry<String, String>>() {
			@Override
			public String getValue(Map.Entry<String, String> entry) {
				return entry.getValue();
			}
		};
		table.addColumn(data1);

		Map<String, String> transformedMap = new HashMap<>();
		for (Map.Entry<String, JSONValue> entry : map.entrySet()) {
			transformedMap.put(entry.getKey(), entry.getValue().toString());
		}

		table.setBordered(true);
		table.setCondensed(true);
		table.setStriped(true);
		table.setRowData(new ArrayList<>(transformedMap.entrySet()));
		data0.setCellStyleNames(resources.gss().personalCellTableBody());
		data1.setCellStyleNames(resources.gss().personalCellTableBody());
		table.setColumnWidth(0, "40%");
		table.setColumnWidth(1, "60%");
		div.add(table);
		return div;
	}

	private Widget getWidgetForList(List<String> list) {
		Span span = new Span();
		span.addStyleName(resources.gss().respectNewLine());
		Text text = new Text("");

		for (String s : list) {
			if (text.getText().equals("")) {
				text.setText(s);
			} else {
				text.setText(text.getText() + "\n" + s);
			}
		}
		if (list.isEmpty()) {
			text.setText("-");
		}
		span.add(text);
		return span;
	}

	private Column createLabelColumn(PersonalAttribute personalAttribute, String defaultName) {
		Column nameColumn = new Column(ColumnSize.SM_3, ColumnSize.XS_3);
		nameColumn.addStyleName(PerunProfileResources.INSTANCE.gss().personalInfoLabel());
		String urn = personalAttribute.getUrn();
		String name;

		// check if translated name is available
		String translatedName = attributeNamesTranslations.get(urn);
		if (translatedName == null) {
			if (defaultName != null) {
				translatedName = defaultName;
			} else {
				// parse name from urn
				String[] split = urn.split(":");
				if (split.length == 1) {
					translatedName = split[split.length - 1];
				}
			}
		}

		name = translatedName;

		// check if there is name from configuration for current locale - override code defaults
		String configName = personalAttribute.getLocalizedName(Utils.getLocale());

		if (configName != null) {
			name = configName;
		} else {
			// if value for current locale is not present, override by EN
			configName = personalAttribute.getLocalizedName(Locale.EN);
			if (configName != null) {
				name = configName;
			}
		}

		Text nameText = new Text(name);

		nameColumn.add(nameText);

		return nameColumn;
	}

	private Column createValueColumn(Attribute attribute, PersonalAttribute personalAttribute) {
		Column valueColumn = new Column(ColumnSize.SM_9, ColumnSize.XS_9);

		if (attribute == null) {
			valueColumn.add(attributeIsString("-", personalAttribute));
			return valueColumn;
		}

		if (attribute.getType().equals("java.util.ArrayList")) {

			Column innerValueColumn = new Column(ColumnSize.SM_6, ColumnSize.XS_6);
			innerValueColumn.setPaddingLeft(0);
			innerValueColumn.add(getWidgetForList(attribute.getValueAsList()));
			Column innerDescriptionColumn = createDescriptionColumn(personalAttribute);
			valueColumn.add(innerValueColumn);
			valueColumn.add(innerDescriptionColumn);
			return valueColumn;

		}

		if (attribute.getType().equals("java.util.LinkedHashMap")) {

			Column innerValueColumn = new Column(ColumnSize.SM_12, ColumnSize.XS_12);
			innerValueColumn.setPaddingLeft(0);
			innerValueColumn.add(getWidgetForMap(attribute.getValueAsMap(),
					personalAttribute.getLocalizedDescription(Utils.getLocale())));
			valueColumn.add(innerValueColumn);
			return valueColumn;
		}

		//if it is string or number
		valueColumn.add(attributeIsString(attribute.getValue(), personalAttribute));
		return valueColumn;
	}

	private Row attributeIsString(String value, PersonalAttribute personalAttribute) {
		Column innerValueColumn = new Column(ColumnSize.SM_6, ColumnSize.XS_6);

		Row innerRow = new Row();

		Widget valueWidget = getWidgetForText(value);

		innerValueColumn.add(valueWidget);

		// if it is preferred email, add change button
		if (URN_PREFERRED_EMAIL.matches(personalAttribute.getUrn())) {
			ConfigManager.getPersonalDataChangeConfig(new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					Map<String, JSONValue> config = JsonUtils.parseJsonToMap(result);
					if (Objects.equals("true", config.get("enableCustomEmail").toString())) {
						addEmailChangeButton(innerValueColumn);
					}
				}

				@Override
				public void onError(PerunException error) {

				}

				@Override
				public void onLoadingStart() {

				}
			});
		}

		// FIXME - support other namespaces
		if ("urn:perun:user:attribute-def:def:login-namespace:einfra".matches(personalAttribute.getUrn())) {
			addPasswordChangeButton("einfra", value, innerValueColumn);
			addPasswordResetButton("einfra", innerValueColumn);
		}

		Column innerDescriptionColumn = createDescriptionColumn(personalAttribute);

		innerRow.add(innerValueColumn);
		innerRow.add(innerDescriptionColumn);

		return innerRow;
	}

	void createModal(String namespace, String login) {

		Modal pwdChangeModal = new Modal();
		pwdChangeModal.setTitle(translation.changePasswordModalTitle(login));
		pwdChangeModal.setDataBackdrop(ModalBackdrop.STATIC);

		ModalBody body = new ModalBody();

		Form form = new Form();
		FieldSet fieldSet = new FieldSet();
		formGroup = new FormGroup();
		formGroup2 = new FormGroup();

		FormLabel oldPassLabel = new FormLabel();
		oldPassLabel.setText(translation.oldPassLabel());

		FormLabel newPassLabel = new FormLabel();
		newPassLabel.setText(translation.newPassLabel());

		InputGroup ig = new InputGroup();
		InputGroupAddon iga = new InputGroupAddon();
		iga.setIcon(IconType.KEY);
		ig.add(iga);
		oldPass = new ExtendedPasswordTextBox();
		oldPass.setPlaceholder(translation.enterOldPassPlaceholder());
		ig.add(oldPass);

		helpBlock = new HelpBlock();

		formGroup.add(oldPassLabel);
		formGroup.add(ig);
		formGroup.add(helpBlock);

		InputGroup ig2 = new InputGroup();
		InputGroupAddon iga2 = new InputGroupAddon();
		iga2.setIcon(IconType.KEY);
		ig2.add(iga2);
		newPass = new ExtendedPasswordTextBox();
		newPass2 = new ExtendedPasswordTextBox();
		newPass.setPlaceholder(translation.enterNewPassPlaceholder());
		newPass2.setPlaceholder(translation.repeatNewPassPlaceholder());
		ig2.add(newPass);
		ig2.add(newPass2);

		formGroup2.add(newPassLabel);
		formGroup2.add(ig2);

		helpBlock2 = new HelpBlock();
		formGroup2.add(helpBlock2);

		HelpBlock helpBlock3 = new HelpBlock();
		helpBlock3.setHTML(translation.einfraPasswordHelp());

		oldPass.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (!Objects.equals(previousOldPassVal, oldPass.getValue())) {
					validateOldPass();
					previousOldPassVal = oldPass.getValue();
				}
			}
		});
		oldPass.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (!Objects.equals(previousOldPassVal, oldPass.getValue())) {
					validateOldPass();
					previousOldPassVal = oldPass.getValue();
				}
			}
		});
		newPass.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (!Objects.equals(previousNewPassVal, newPass.getValue())) {
					validateNewPass(login);
					previousNewPassVal = newPass.getValue();
				}
			}
		});
		newPass.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (!Objects.equals(previousNewPassVal, newPass.getValue())) {
					validateNewPass(login);
					previousNewPassVal = newPass.getValue();
				}
			}
		});
		newPass2.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (!Objects.equals(previousNewPass2Val, newPass2.getValue())) {
					validateNewPass(login);
					previousNewPass2Val = newPass2.getValue();
				}
			}
		});
		newPass2.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (!Objects.equals(previousNewPass2Val, newPass2.getValue())) {
					validateNewPass(login);
					previousNewPass2Val = newPass2.getValue();
				}
			}
		});

		fieldSet.add(formGroup);
		fieldSet.add(formGroup2);
		fieldSet.add(helpBlock3);
		form.add(fieldSet);
		body.add(form);

		final AlertErrorReporter alert = new AlertErrorReporter();
		alert.setVisible(false);
		alert.setType(AlertType.DANGER);
		body.add(alert);

		previousOldPassVal = oldPass.getValue();
		previousNewPassVal = newPass.getValue();
		previousNewPass2Val = newPass2.getValue();

		ModalFooter footer = new ModalFooter();

		final PerunButton changePasswordButton = new PerunButton(translation.changePasswordButton());
		changePasswordButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// split method call and condition to force validation of both fields
				boolean oldOk = validateOldPass();
				boolean newOk = validateNewPass(login);
				if (oldOk && newOk) {

					UsersManager.checkPasswordStrength(namespace, newPass.getValue(), new JsonEvents() {
						@Override
						public void onFinished(JavaScriptObject result) {

							UsersManager.changePassword(PerunSession.getInstance().getUserId(), namespace, oldPass.getValue(), newPass.getValue(), new JsonEvents() {
								@Override
								public void onFinished(JavaScriptObject result) {
									changePasswordButton.setProcessing(false);
									pwdChangeModal.hide();
									Notify.notify(translation.passwordHasBeenChanged(login), NotifyType.SUCCESS);
								}

								@Override
								public void onError(PerunException error) {
									changePasswordButton.setProcessing(false);

									if (error.getName().equals("PasswordDoesntMatchException")) {
										// old password doesn't match
										formGroup.setValidationState(ValidationState.ERROR);
										helpBlock.setText(translation.oldPasswordIncorrect());
									} else {
										// other errors
										alert.setVisible(true);
										alert.setReportInfo(error);
										alert.setText(error.getMessage());
									}

								}

								@Override
								public void onLoadingStart() {
								}
							});

						}

						@Override
						public void onError(PerunException error) {

							changePasswordButton.setProcessing(false);

							if (error.getName().equals("PasswordStrengthException")) {
								// just strength error
								formGroup2.setValidationState(ValidationState.ERROR);
								helpBlock2.setText(error.getMessage());
							} else {
								// other errors
								alert.setVisible(true);
								alert.setReportInfo(error);
								alert.setText(error.getMessage());
							}

						}

						@Override
						public void onLoadingStart() {
							alert.setVisible(false);
							changePasswordButton.setProcessing(true);
						}
					});


				}
			}
		});
		changePasswordButton.setType(ButtonType.PRIMARY);

		footer.add(changePasswordButton);

		pwdChangeModal.add(body);
		pwdChangeModal.add(footer);
		pwdChangeModal.show();

		oldPass.setFocus(true);

	}

	public boolean validateOldPass() {

		if (oldPass.getValue() == null || oldPass.getValue().isEmpty()) {
			formGroup.setValidationState(ValidationState.ERROR);
			helpBlock.setText(translation.oldPassCantBeEmpty());
			return false;
		}
		formGroup.setValidationState(ValidationState.NONE);
		helpBlock.setText("");
		return true;

	}

	public boolean validateNewPass(String login) {

		if (newPass.getValue() == null || newPass.getValue().isEmpty()) {
			formGroup2.setValidationState(ValidationState.ERROR);
			helpBlock2.setHTML(translation.passwordCantBeEmpty());
			return false;
		}

		// limit only to ASCII printable chars
		RegExp regExp2 = RegExp.compile("^[\\x20-\\x7E]{1,}$");
		if(regExp2.exec(newPass.getValue()) == null){
			formGroup2.setValidationState(ValidationState.ERROR);
			helpBlock2.setHTML(translation.einfraPasswordFormat());
			return false;
		}

		if (login != null && login.length() > 2) {


			if (PerunSession.getInstance().getUserId() > 0) {
				User user = PerunSession.getInstance().getUser();
				String firstName = Utils.unAccent(user.getFirstName());
				String lastName = Utils.unAccent(user.getLastName());

				String backwardsFirstName = new StringBuilder(firstName).reverse().toString();
				String backwardsLastName = new StringBuilder(lastName).reverse().toString();

				if (newPass.getValue().toLowerCase().contains(firstName.toLowerCase()) ||
						newPass.getValue().toLowerCase().contains(backwardsFirstName.toLowerCase())) {
					formGroup2.setValidationState(ValidationState.ERROR);
					helpBlock2.setHTML(translation.einfraPasswordStrengthForNameLogin());
					return false;
				}

				if (newPass.getValue().toLowerCase().contains(lastName.toLowerCase()) ||
						newPass.getValue().toLowerCase().contains(backwardsLastName.toLowerCase())) {
					formGroup2.setValidationState(ValidationState.ERROR);
					helpBlock2.setHTML(translation.einfraPasswordStrengthForNameLogin());
					return false;
				}

			}

			String backwardsLogin = new StringBuilder(login).reverse().toString();
			if (newPass.getValue().toLowerCase().contains(login.toLowerCase()) ||
					newPass.getValue().toLowerCase().contains(backwardsLogin.toLowerCase())) {
				formGroup2.setValidationState(ValidationState.ERROR);
				helpBlock2.setHTML(translation.einfraPasswordStrengthForNameLogin());
				return false;
			}

		}

		// Check that password contains at least 3 of 4 character groups

		RegExp regExpDigit = RegExp.compile("^.*[0-9].*$");
		RegExp regExpLower = RegExp.compile("^.*[a-z].*$");
		RegExp regExpUpper = RegExp.compile("^.*[A-Z].*$");
		RegExp regExpSpec = RegExp.compile("^.*[\\x20-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7E].*$");

		int matchCounter = 0;
		if (regExpDigit.exec(newPass.getValue()) != null) matchCounter++;
		if (regExpLower.exec(newPass.getValue()) != null) matchCounter++;
		if (regExpUpper.exec(newPass.getValue()) != null) matchCounter++;
		if (regExpSpec.exec(newPass.getValue()) != null) matchCounter++;

		if(matchCounter < 3){
			formGroup2.setValidationState(ValidationState.ERROR);
			helpBlock2.getElement().setInnerHTML(translation.einfraPasswordStrength());
			return false;
		}

		// check length
		if (newPass.getValue().length() < 10) {
			formGroup2.setValidationState(ValidationState.ERROR);
			helpBlock2.getElement().setInnerHTML(translation.einfraPasswordLength());
			return false;
		}

		if (!newPass.getValue().equals(newPass2.getValue())) {
			if (newPass2.getValue() == null || newPass2.getValue().isEmpty()) {
				formGroup2.setValidationState(ValidationState.WARNING);
				helpBlock2.setHTML(translation.secondPasswordIsEmpty());
				return false;
			}
			formGroup2.setValidationState(ValidationState.ERROR);
			helpBlock2.setHTML(translation.passwordMismatch());
			return false;
		}

		formGroup2.setValidationState(ValidationState.NONE);
		helpBlock2.setHTML("");
		return true;

	}

}
