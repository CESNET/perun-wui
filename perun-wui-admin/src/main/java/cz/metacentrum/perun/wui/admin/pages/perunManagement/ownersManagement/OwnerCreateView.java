package cz.metacentrum.perun.wui.admin.pages.perunManagement.ownersManagement;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.admin.pages.perunManagement.OwnersManagementView;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.OwnersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Owner;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.*;

/**
 *
 * OWNERS MANAGEMENT - CREATE OWNER VIEW
 *
 * @author Kristyna Kysela
 */
public class OwnerCreateView extends ViewImpl implements OwnerCreatePresenter.MyView {

	interface OwnerCreateViewUiBinder extends UiBinder<Modal, OwnerCreateView> {
	}

	@UiField Modal itself;
	@UiField ModalBody body;

	@UiField
	ExtendedTextBox nameTextBox;

	@UiField
	ExtendedTextBox contactTextBox;

	@UiField
	ListBox typeListBox;

	@UiField
	PerunButton createButton;

	@UiField
	PerunButton cancelButton;

	@Inject
	public OwnerCreateView(OwnerCreateViewUiBinder uiBinder) {

		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler(value = "createButton")
	public void onClick(ClickEvent event) {

			OwnersManager.createOwner(nameTextBox.getText(), contactTextBox.getText(), typeListBox.getSelectedItemText(), new JsonEvents() {

				@Override
				public void onFinished(JavaScriptObject jso) {
					createButton.setProcessing(false);
					itself.hide();
					OwnersManagementView.update((Owner)jso);
				}

				@Override
				public void onError(PerunException error) {
					createButton.setProcessing(false);
				}

				@Override
				public void onLoadingStart() {
					createButton.setProcessing(true);
				}
			});

	}

	@UiHandler(value = "cancelButton")
	public void onCancel(ClickEvent event) {
		itself.hide();
	}


	@Override
	public void show() {

		setBlank();
		createButton.setEnabled(false);
		itself.show();

		nameTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				createButton.setEnabled(!nameTextBox.getText().trim().isEmpty());
			}
		});

		nameTextBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				createButton.setEnabled(!nameTextBox.getText().trim().isEmpty());
			}
		});

		nameTextBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				createButton.setEnabled(!nameTextBox.getText().trim().isEmpty());
			}
		});

	}

	public void setBlank(){
		nameTextBox.setText("");
		contactTextBox.setText("");
	}




}