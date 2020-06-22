package cz.metacentrum.perun.wui.profile.pages.settings.samba;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import java.util.Map;

/**
 * View for settings SAMBA password
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class SambaPasswordView extends ViewWithUiHandlers<SambaPasswordUiHandlers> implements SambaPasswordPresenter.MyView {

	interface SambaPasswordViewUiBinder extends UiBinder<Widget, SambaPasswordView> {
	}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	public SambaPasswordValidator validator = new SambaPasswordValidator() {
		@Override
		public boolean validate() {

			// validate password
			if (passwordBox.getValue() == null || passwordBox.getValue().isEmpty()) {
				formGroup.setValidationState(ValidationState.ERROR);
				formGroupNote.getElement().setInnerHTML(translation.cannotBeEmpty());
				return false;
			}

			// limit only to ASCII printable chars
			RegExp regExp2 = RegExp.compile("^[\\x20-\\x7E]{1,}$");
			if(regExp2.exec(passwordBox.getValue()) == null){
				formGroup.setValidationState(ValidationState.ERROR);
				formGroupNote.getElement().setInnerHTML(translation.einfraPasswordFormat());
				return false;
			}

			// Check that password contains at least 3 of 4 character groups

			RegExp regExpDigit = RegExp.compile("^.*[0-9].*$");
			RegExp regExpLower = RegExp.compile("^.*[a-z].*$");
			RegExp regExpUpper = RegExp.compile("^.*[A-Z].*$");
			RegExp regExpSpec = RegExp.compile("^.*[\\x20-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7E].*$");

			int matchCounter = 0;
			if (regExpDigit.exec(passwordBox.getValue()) != null) matchCounter++;
			if (regExpLower.exec(passwordBox.getValue()) != null) matchCounter++;
			if (regExpUpper.exec(passwordBox.getValue()) != null) matchCounter++;
			if (regExpSpec.exec(passwordBox.getValue()) != null) matchCounter++;

			if(matchCounter < 3){
				formGroup.setValidationState(ValidationState.ERROR);
				formGroupNote.getElement().setInnerHTML(translation.einfraPasswordStrength());
				return false;
			}

			// check length
			if (passwordBox.getValue().length() < 10) {
				formGroup.setValidationState(ValidationState.ERROR);
				formGroupNote.getElement().setInnerHTML(translation.einfraPasswordLength());
				return false;
			}

			formGroup.setValidationState(ValidationState.SUCCESS);
			formGroupNote.getElement().setInnerHTML("");
			return true;

		}
	};

	@UiField
	Input passwordBox;

	@UiField
	PerunButton setSambaPasswordButton;

	@UiField
	Alert invalidValueAlert;

	@UiField Alert sambaAlert;

	@UiField HelpBlock passwordNote;
	@UiField FormGroup formGroup;
	@UiField HelpBlock formGroupNote;

	@UiHandler({"setSambaPasswordButton"})
	public void onListAll(ClickEvent event) {

		SambaPasswordView view = this;

		if (!validator.validate()) return;

		getUiHandlers().changeSambaPassword(passwordBox.getValue(),  new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				passwordBox.setText("");
				setSambaPasswordButton.setProcessing(false);
				Notify.notify(translation.sambaPasswordSetNotif(), "", NotifyType.SUCCESS);
			}

			@Override
			public void onError(PerunException error) {
				setSambaPasswordButton.setProcessing(false);
				view.onError(error);
			}

			@Override
			public void onLoadingStart() {
				setSambaPasswordButton.setProcessing(true);
				view.onLoadingStart();
			}
		});

	}

	@UiHandler("backButton")
	public void backButtonAction(ClickEvent event) {
		getUiHandlers().navigateBack();
	}

	@Inject
	public SambaPasswordView(SambaPasswordViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
		passwordBox.setWidth("100%");
		setSambaPasswordButton.setWidth("100%");
		passwordBox.addKeyUpHandler(keyUpEvent -> {
			if (passwordBox.getValue() == null || passwordBox.getValue().isEmpty()) {
				setSambaPasswordButton.setEnabled(false);
			} else {
				setSambaPasswordButton.setEnabled(true);
			}
		});
		passwordNote.getElement().setInnerHTML(translation.einfraPasswordHelp());

		passwordBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				validator.validate();
			}
		});

	}

	@Override
	public void onError(PerunException e) {
		invalidValueAlert.setVisible(true);
		invalidValueAlert.setText(e.getMessage());
	}

	@Override
	public void setData(Map<String, JSONValue> data) {
		if (data.isEmpty()) {
			sambaAlert.getElement().setInnerHTML(translation.sambaPasswordNotSet());
		} else {
			sambaAlert.getElement().setInnerHTML(translation.sambaPasswordSet());
		}
	}

	@Override
	public void onLoadingStart() {
	}

	public interface SambaPasswordValidator {
		boolean validate();
	}

}
