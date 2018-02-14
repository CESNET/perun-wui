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
import cz.metacentrum.perun.wui.model.beans.RichUser;
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
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.List;

/**
 * View for displaying personal info about user
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PersonalView extends ViewWithUiHandlers<PersonalUiHandlers> implements PersonalPresenter.MyView {

	private static final String NAME = "name";
	private static final String ORGANIZATION = "organization";
	private static final String PREFERRED_EMAIL = "preferredEmail";
	private static final String PREFERRED_LANGUAGE = "preferredLanguage";
	private static final String TIME_ZONE = "timezone";

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
	Column nameLabel;
	@UiField
	Text nameData;
	@UiField
	Column emailLabel;
	@UiField
	Text emailData;
	@UiField
	Column orgLabel;
	@UiField
	Text orgData;
	@UiField
	Column languageLabel;
	@UiField
	Text languageData;
	@UiField
	Column timezoneLabel;
	@UiField
	Text timezoneData;

	@UiField Row nameRow;
	@UiField Row organizationRow;
	@UiField Row preferredEmailRow;
	@UiField Row preferredLanguageRow;
	@UiField Row timezoneRow;

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
	Button updateEmailModalBtn;
	@UiField
	Modal updateEmailModal;
	@UiField
	FormLabel updateEmailLabel;

	@Inject
	public PersonalView(PersonalViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));


		orgLabel.add(new Text(translation.organization()));
		nameLabel.add(new Text((translation.name())));
		emailLabel.add(new Text((translation.preferredMail())));
		languageLabel.add(new Text((translation.preferredLang())));
		timezoneLabel.add(new Text((translation.timezone())));

		updateEmailBtn.addClickHandler((event) ->
				updateEmailForm.submit()
		);

		updateEmailForm.addSubmitHandler(submitEvent -> {
			submitEvent.cancel();
			getUiHandlers().updateEmail(newUpdateEmail.getValue());
		});

		updateEmailLoader.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
		updateEmailModalBtn.setText(translation.updateEmail());
		updateEmailBtn.setText(translation.sendValidationEmail());
		updateEmailModal.setTitle(translation.updateEmailModalTitle());
		updateEmailLabel.setText(translation.newPreferredEmail());

		applyHideConfiguration();
	}

	@Override
	public void setUser(RichUser user) {
		loader.onFinished();
		loader.setVisible(false);
		personalInfo.setVisible(true);

		text.setText(user.getFullName());

		setAttribute(user.getFullName(), nameData);
		setAttribute(user.getOrganization(), orgData);
		setAttribute(user.getPreferredEmail(), emailData);
		setAttribute(user.getPreferredLanguage(), languageData);
		setAttribute(user.getTimezone(), timezoneData);
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

	private void applyHideConfiguration() {

		checkAttribute(NAME, nameRow);
		checkAttribute(ORGANIZATION, organizationRow);
		checkAttribute(PREFERRED_EMAIL, preferredEmailRow);
		checkAttribute(PREFERRED_LANGUAGE, preferredLanguageRow);
		checkAttribute(TIME_ZONE, timezoneRow);
	}

	private void checkAttribute(String name, Row attrRow) {
		List<String> attrsToHide = PerunConfiguration.getProfilePersonalAttributesToHide();
		if (attrsToHide.contains(name)) {
			attrRow.setVisible(false);
		}
	}

	private void setAttribute(String value, Text label) {
		if (value == null) {
			label.setText("-");
		} else {
			label.setText(value);
		}
	}

}
