package cz.metacentrum.perun.wui.profile.pages.personal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileResources;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		List<String> additionalAttributes = PerunConfiguration.getProfilePersonalAttributesToShow();

		for (String attributeUrn : additionalAttributes) {
			String value;
			String defaultName;
			Attribute attribute = user.getAttribute(attributeUrn);
			value = attribute == null ? null : attribute.getValue();
			defaultName = attribute == null ? null : attribute.getDisplayName();
			addPersonalData(attributeUrn, value, defaultName);
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

	/**
	 * Adds user data to overview. If the given value is null then
	 * '-' is shown. If there is a translated name for given urn,
	 * then it is used. If there is not, the defaultName is shown.
	 * If there is not a translated name and the default name is null,
	 * the name is obtained from given urn.
	 *
	 * @param urn attribute urn
	 * @param value value to be shown
	 * @param defaultName default name used when there is not a translated name for given urn
	 */
	private void addPersonalData(String urn, String value, String defaultName) {
		Row row = new Row();
		row.setPaddingBottom(10);
		Column nameColumn = new Column(ColumnSize.SM_4, ColumnSize.XS_4);
		nameColumn.addStyleName(PerunProfileResources.INSTANCE.gss().personalInfoLabel());
		Column valueColumn = new Column(ColumnSize.SM_8, ColumnSize.XS_8);

		// check if translated name is available
		String translatedName = attributeNamesTranslations.get(urn);
		if (translatedName == null) {
			if (defaultName != null) {
				translatedName = defaultName;
			} else {
				// parse name from urn
				String[] split = urn.split(":");
				if (split.length < 1) {
					return;
				}
				translatedName = split[split.length - 1];
			}
		}

		Text nameText = new Text(translatedName);
		Text valueText = new Text();

		if (value == null) {
			valueText.setText("-");
		} else {
			valueText.setText(value);
		}

		nameColumn.add(nameText);
		valueColumn.add(valueText);

		row.add(nameColumn);
		row.add(valueColumn);

		// if it is preferred email, add change button
		if (URN_PREFERRED_EMAIL.matches(urn)) {
			Button updateButton = new Button();
			updateButton.setSize(ButtonSize.EXTRA_SMALL);
			updateButton.setMarginLeft(10);
			updateButton.setText(translation.updateEmail());
			updateButton.setDataTarget("#updateEmailModal");
			updateButton.setDataToggle(Toggle.MODAL);

			valueColumn.add(updateButton);
		}

		personalInfo.add(row);
	}
}
