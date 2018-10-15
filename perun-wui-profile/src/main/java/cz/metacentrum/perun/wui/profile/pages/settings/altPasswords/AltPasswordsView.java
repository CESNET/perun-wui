package cz.metacentrum.perun.wui.profile.pages.settings.altPasswords;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;
import org.gwtbootstrap3.client.ui.html.Div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class AltPasswordsView extends ViewWithUiHandlers<AltPasswordsUiHandlers> implements AltPasswordsPresenter.MyView {

	interface AltPasswordsViewUiBinder extends UiBinder<Widget, AltPasswordsView> {
	}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField
	TextBox passwordBox;

	@UiField
	PerunButton addAltPasswordButton;

	@UiField
	DataGrid<Map.Entry<String, String>> aPDataGrid;

	@UiField
	Div panelsDiv;

	@UiField
	Modal generatedPasswordModal;

	@UiField
	TextBox generatedPasswordBox;

	@UiField
	PerunButton copyPasswordButton;

	@UiField
	PerunButton closeModalButton;

	@UiField
	Alert invalidValueAlert;

	@UiHandler({"addAltPasswordButton"})
	public void onListAll(ClickEvent event) {
		getUiHandlers().newAltPassoword(passwordBox.getText(), generatePassword(16));
	}

	@UiHandler("backButton")
	public void backButtonAction(ClickEvent event) {
		getUiHandlers().navigateBack();
	}

	@UiHandler("closeModalButton")
	public void closeModalButtonAction(ClickEvent event) {
		generatedPasswordModal.hide();
	}

	@UiHandler("copyPasswordButton")
	public void copyPasswordButtonAction(ClickEvent event) {
		generatedPasswordBox.setFocus(true);
		generatedPasswordBox.selectAll();
		jSCopy();
		generatedPasswordBox.setFocus(false);
	}

	@Inject
	public AltPasswordsView(AltPasswordsViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
		passwordBox.setWidth("100%");
		addAltPasswordButton.setWidth("100%");
		initTable(aPDataGrid);
		generatedPasswordModal.addShownHandler(modalShownEvent -> generatedPasswordBox.setFocus(true));

		passwordBox.addKeyUpHandler(keyUpEvent -> {
			if (!passwordBox.getText().equals("") && !getUiHandlers().descriptionIsUsed(passwordBox.getText())) {
				addAltPasswordButton.setEnabled(true);
			} else {
				addAltPasswordButton.setEnabled(false);
			}
		});
	}

	@Override
	public void onError(PerunException e) {
		invalidValueAlert.setVisible(true);
		invalidValueAlert.setText(e.getMessage());
		if (aPDataGrid.getRowCount() == 0) {
			((PerunLoader) aPDataGrid.getEmptyTableWidget()).onEmpty();
		}
	}

	@Override
	public void setData(Map<String, JSONValue> data) {
		invalidValueAlert.setVisible(false);
		addAltPasswordButton.setEnabled(false);
		if (data.isEmpty()) {
			((PerunLoader) aPDataGrid.getEmptyTableWidget()).onEmpty();
			aPDataGrid.setRowCount(0);
		} else {
			Map<String, String> tableData = new HashMap<>();
			for (Map.Entry<String, JSONValue> entry : data.entrySet()) {
				tableData.put(entry.getKey(), entry.getValue().toString());
			}
			aPDataGrid.setRowData(new ArrayList<>(tableData.entrySet()));
		}

	}

	@Override
	public void onLoadingStart() {
		((PerunLoader) aPDataGrid.getEmptyTableWidget()).onLoading(translation.loadingUserData());
	}

	private void initTable(DataGrid<Map.Entry<String, String>> dataGrid) {

		TextColumn<Map.Entry<String, String>> nameCol = new TextColumn<Map.Entry<String, String>>() {

			@Override
			public String getValue(Map.Entry<String, String> entry) {
				return entry.getKey();
			}
		};

		Column<Map.Entry<String, String>, String> deleteCol =
				new Column<Map.Entry<String, String>, String>(new ButtonCell(ButtonType.DANGER)) {

					@Override
					public String getValue(Map.Entry<String, String> entry) {
						return translation.delete();
					}
				};

		deleteCol.setFieldUpdater((i, entry, s) -> getUiHandlers().deleteAltPassword(entry.getValue()));
		PerunLoader pl = new PerunLoader();
		pl.setEmptyMessage(translation.noPasswords());
		pl.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
		dataGrid.setEmptyTableWidget(pl);
		dataGrid.addColumn(nameCol, translation.description());
		dataGrid.addColumn(deleteCol, translation.delete());
		if (PerunConfiguration.getCurrentLocaleName().equals("cs")) {
			dataGrid.setColumnWidth(nameCol, "89%");
			dataGrid.setColumnWidth(deleteCol, "11%");
		} else {
			dataGrid.setColumnWidth(nameCol, "91%");
			dataGrid.setColumnWidth(deleteCol, "9%");
		}
	}

	@Override
	public void showGeneratedPassword(String password) {
		generatedPasswordModal.show();
		generatedPasswordBox.setText(password);
		passwordBox.setText("");
		passwordBox.setPlaceholder(translation.passwordDescription());
	}

	private String generatePassword(int length) {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder password = new StringBuilder();
		Random r = new Random();
		for (int i = 0; i < length; i++) {
			password.append(chars.charAt(r.nextInt(chars.length())));
		}
		return password.toString();
	}

	private static native void jSCopy() /*-{
		$doc.execCommand('copy')
	}-*/;
}
