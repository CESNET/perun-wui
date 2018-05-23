package cz.metacentrum.perun.wui.profile.pages.settings.sshkeys.newsshkey;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.json.ErrorTranslator;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.profile.client.PerunProfileUtils;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Progress;
import org.gwtbootstrap3.client.ui.TextArea;


public class NewSshKeyView extends ViewWithUiHandlers<NewSshKeyUiHandlers> implements NewSshKeyPresenter.MyView {

	interface NewSshKeyViewUiBinder extends UiBinder<Widget, NewSshKeyView> {}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField PerunButton addSshKeyButton;
	@UiField Progress addSshKeyProgress;
	@UiField TextArea newSshKeyTextArea;
	@UiField Alert addSshKeyAlert;
	@UiField Alert invalidValueAlert;

	@Inject
	public NewSshKeyView(NewSshKeyViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
	}

	@Override
	public void setAddSshKeyFinish() {
		addSshKeyProgress.setVisible(false);
		addSshKeyButton.setVisible(true);
		newSshKeyTextArea.setEnabled(true);
		newSshKeyTextArea.clear();
	}

	@Override
	public void setAddSshKeyError(PerunException error) {
		addSshKeyProgress.setVisible(false);
		addSshKeyButton.setVisible(true);
		newSshKeyTextArea.setEnabled(true);
		addSshKeyAlert.setVisible(true);
		addSshKeyAlert.setText(ErrorTranslator.getTranslatedMessage(error));
	}

	@UiHandler("addSshKeyButton")
	public void addSshKeyButtonAction(ClickEvent event) {
		String keyValue = newSshKeyTextArea.getText();

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
		addSshKeyAlert.setVisible(false);
		addSshKeyProgress.setVisible(true);
		addSshKeyButton.setVisible(false);
		newSshKeyTextArea.setEnabled(false);
		getUiHandlers().addSshKey(keyValue);
	}

	@UiHandler("backButton")
	public void cancelButtonAction(ClickEvent event) {
		getUiHandlers().navigateBack();
	}

	@Override
	public void resetView() {
		invalidValueAlert.setVisible(false);
		newSshKeyTextArea.setText("");
	}
}
