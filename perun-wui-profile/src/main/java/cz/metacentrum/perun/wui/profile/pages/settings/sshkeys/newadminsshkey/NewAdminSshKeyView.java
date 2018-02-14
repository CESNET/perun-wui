package cz.metacentrum.perun.wui.profile.pages.settings.sshkeys.newadminsshkey;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.json.ErrorTranslator;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.profile.client.PerunProfileUtils;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Progress;
import org.gwtbootstrap3.client.ui.TextArea;


public class NewAdminSshKeyView extends ViewWithUiHandlers<NewAdminSshKeyUiHandlers> implements NewAdminSshKeyPresenter.MyView {

	interface NewAdminSshKeyViewUiBinder extends UiBinder<Widget, NewAdminSshKeyView> {}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField Button addAdminSshKeyButton;
	@UiField Progress addAdminSshKeyProgress;
	@UiField TextArea newAdminSshKeyTextArea;
	@UiField Alert addAdminSshKeyAlert;
	@UiField Alert invalidValueAlert;

	@Inject
	public NewAdminSshKeyView(NewAdminSshKeyViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
	}
	@Override
	public void setAddAdminSshKeyFinish() {
		addAdminSshKeyProgress.setVisible(false);
		addAdminSshKeyButton.setVisible(true);
		newAdminSshKeyTextArea.setEnabled(true);
		newAdminSshKeyTextArea.clear();
	}

	@Override
	public void setAddAdminSshKeyError(PerunException error) {
		addAdminSshKeyProgress.setVisible(false);
		addAdminSshKeyButton.setVisible(true);
		newAdminSshKeyTextArea.setEnabled(true);
		addAdminSshKeyAlert.setVisible(true);
		addAdminSshKeyAlert.setText(ErrorTranslator.getTranslatedMessage(error));
	}


	@UiHandler("addAdminSshKeyButton")
	public void addAdminSshKeyButtonAction(ClickEvent event) {
		String keyValue = newAdminSshKeyTextArea.getText();

		keyValue = keyValue.trim();

		if(!PerunProfileUtils.isValidSshKey(keyValue)) {
			invalidValueAlert.setVisible(true);
			invalidValueAlert.setText(translation.sshInvalidPrefixText());
			return;
		}

		if(keyValue.contains("\n")) {
			invalidValueAlert.setVisible(true);
			invalidValueAlert.setText(translation.sshInvalidNewLinesText());
			return;
		}

		invalidValueAlert.setVisible(false);
		addAdminSshKeyAlert.setVisible(false);
		addAdminSshKeyProgress.setVisible(true);
		addAdminSshKeyButton.setVisible(false);
		newAdminSshKeyTextArea.setEnabled(false);
		getUiHandlers().addAdminSshKey(keyValue);
	}

	@UiHandler("backButton")
	public void cancelButtonAction(ClickEvent event) {
		getUiHandlers().navigateBack();
	}

	@Override
	public void resetView() {
		invalidValueAlert.setVisible(false);
		newAdminSshKeyTextArea.setText("");
	}
}
