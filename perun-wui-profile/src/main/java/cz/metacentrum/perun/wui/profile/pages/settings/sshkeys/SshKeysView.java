package cz.metacentrum.perun.wui.profile.pages.settings.sshkeys;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;


public class SshKeysView extends ViewWithUiHandlers<SshKeysUiHandlers> implements SshKeysPresenter.MyView {

	interface SettingsViewUiBinder extends UiBinder<Widget, SshKeysView> {}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField CellTable<String> sshKeysTable;

	@UiField CellTable<String> adminSshKeysTable;
	@UiField Button newAdminKeyButton;
	@UiField Div newAdminKeyButtonDiv;
	@UiField Button newKeyButton;
	@UiField Div newKeyButtonDiv;

	private Attribute sshKeysAttribute;
	private Attribute adminSshKeysAttribute;

	@Inject
	public SshKeysView(SettingsViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));

		initTable(sshKeysTable, translation.noSshKey(),
				(i, value, buttonText) -> getUiHandlers().removeSshKey(sshKeysAttribute, i));
		initTable(adminSshKeysTable, translation.noAdminSshKey(),
				(i, value, buttonText) -> getUiHandlers().removeAdminSshKey(adminSshKeysAttribute, i));
	}

	private void initTable(CellTable<String> table, String emptyMessage, FieldUpdater<String, String> removeAction) {
		TextColumn<String> valueCol = new TextColumn<String>() {
			@Override
			public String getValue(String attr) {
				return attr;
			}
		};

		Column<String, String> removeColumn = new Column<String, String>(
				new ButtonCell(IconType.REMOVE, ButtonType.DANGER, ButtonSize.DEFAULT)) {
			@Override
			public String getValue(final String attr) {
				return translation.remove();
			}
		};
		removeColumn.setFieldUpdater(removeAction);

		PerunLoader pl = new PerunLoader();
		pl.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
		pl.setEmptyMessage(emptyMessage);
		table.setEmptyTableWidget(pl);
		table.addColumn(valueCol, translation.publicKey());
		table.addColumn(removeColumn, "");
		table.setColumnWidth(table.getColumnCount() - 1 , "110px");
		pl.onEmpty();
	}

	@Override
	public void setSshKeys(Attribute attribute) {
		sshKeysAttribute = attribute;
		((PerunLoader) sshKeysTable.getEmptyTableWidget()).onEmpty();
		sshKeysTable.setRowData(parseValues(attribute));
	}

	@Override
	public void setAdminSshKeys(Attribute attribute) {
		adminSshKeysAttribute = attribute;
		((PerunLoader) adminSshKeysTable.getEmptyTableWidget()).onEmpty();
		adminSshKeysTable.setRowData(parseValues(attribute));
	}

	@Override
	public void setSshKeysError(PerunException error) {
		((PerunLoader) sshKeysTable.getEmptyTableWidget()).onError(error, event -> getUiHandlers().loadSshKeys());
	}

	@Override
	public void setAdminSshKeysError(PerunException error) {
		((PerunLoader) sshKeysTable.getEmptyTableWidget()).onError(error, event -> getUiHandlers().loadAdminSshKeys());
	}

	@Override
	public void setSshKeysLoading() {
		sshKeysTable.setRowData(new ArrayList<>());
		((PerunLoader) sshKeysTable.getEmptyTableWidget()).onLoading();
	}

	@Override
	public void setAdminSshKeysLoading() {
		adminSshKeysTable.setRowData(new ArrayList<>());
		((PerunLoader) adminSshKeysTable.getEmptyTableWidget()).onLoading();
	}



	@Override
	public void setRemoveSshKeyError(PerunException error, int n) {
		ClickHandler retry = clickEvent -> getUiHandlers().removeSshKey(sshKeysAttribute, n);

		sshKeysTable.setRowData(new ArrayList<>());
		((PerunLoader) sshKeysTable.getEmptyTableWidget()).onError(error, retry);
	}

	@Override
	public void setRemoveAdminSshKeyError(PerunException error, int n) {
		ClickHandler retry = clickEvent -> getUiHandlers().removeSshKey(adminSshKeysAttribute, n);

		adminSshKeysTable.setRowData(new ArrayList<>());
		((PerunLoader) adminSshKeysTable.getEmptyTableWidget()).onError(error, retry);
	}

	/**
	 * Returns values as a list from JsonArrayObject
	 *
	 * @param attribute attribute
	 * @return List of values
	 */
	private List<String> parseValues(Attribute attribute) {
		JsArrayString jsArrayString = attribute.getValueAsJsArray();
		List<String> values = new ArrayList<>();

		if (jsArrayString != null) {
			for (int i = 0; i < jsArrayString.length(); i++) {
				values.add(jsArrayString.get(i));
			}
		}

		return values;
	}

	@UiHandler("newAdminKeyButton")
	public void newAdminKeyButtonAction(ClickEvent event) {
		Window.Location.assign("#" + PerunProfilePlaceTokens.SETTINGS_SSH_NEWADMINKEY);
	}

	@UiHandler("newKeyButton")
	public void newKeyButtonAction(ClickEvent event) {
		Window.Location.assign("#" + PerunProfilePlaceTokens.SETTINGS_SSH_NEWKEY);
	}

	@UiHandler("backButton")
	public void cancelButtonAction(ClickEvent event) {
		getUiHandlers().navigateBack();
	}
}
