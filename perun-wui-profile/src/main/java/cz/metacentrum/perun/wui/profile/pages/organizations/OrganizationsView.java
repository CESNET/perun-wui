package cz.metacentrum.perun.wui.profile.pages.organizations;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.RichMember;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class OrganizationsView extends ViewWithUiHandlers<OrganizationsUiHandlers> implements OrganizationsPresenter.MyView {

	interface GroupsViewUiBinder extends UiBinder<Widget, OrganizationsView> {}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	private static final String EXPIRATION_ATTRIBUTE_URN = "urn:perun:member:attribute-def:def:membershipExpiration";

	@UiField Text title;

	@UiField DataGrid<Map.Entry<RichMember, Vo>> vosDataGrid;

	@Inject
	public OrganizationsView(GroupsViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));

		initTable(vosDataGrid);

		title.setText(translation.menuOrganizations());
	}

	@Override
	public void onError(PerunException e) {

	}

	@Override
	public void onLoadingStart() {
		((PerunLoader)vosDataGrid.getEmptyTableWidget()).onLoading(translation.loadingUserData());
	}

	@Override
	public void setData(Map<RichMember, Vo> data) {

		if (data.isEmpty()) {
			((PerunLoader)vosDataGrid.getEmptyTableWidget()).onEmpty();
		} else {
			List<Map.Entry<RichMember, Vo>> tableData = new ArrayList<>(data.entrySet());
			tableData.sort(Comparator.comparing(o -> o.getValue().getName()));
			vosDataGrid.setRowData(tableData);
		}
	}

	private void initTable(DataGrid<Map.Entry<RichMember, Vo>> dataGrid) {
		TextColumn<Map.Entry<RichMember, Vo>> nameCol = new TextColumn<Map.Entry<RichMember, Vo>>() {
			@Override
			public String getValue(Map.Entry<RichMember, Vo> entry) {
				return entry.getValue().getName();
			}
		};

		TextColumn<Map.Entry<RichMember, Vo>> expirationCol = new TextColumn<Map.Entry<RichMember, Vo>>() {
			@Override
			public String getValue(Map.Entry<RichMember, Vo> entry) {
				Attribute expirationAttribute =
						entry.getKey().getAttribute(EXPIRATION_ATTRIBUTE_URN);
				return expirationAttribute == null ? "never" : expirationAttribute.getValue();
			}
		};


		Column<Map.Entry<RichMember, Vo>, String> membershipCol =
				new Column<Map.Entry<RichMember, Vo>, String>(new ButtonCell(ButtonType.DANGER)) {


			@Override
			public String getValue(Map.Entry<RichMember, Vo> entry) {
				Attribute expirationAttribute =
						entry.getKey().getAttribute(EXPIRATION_ATTRIBUTE_URN);
				if (expirationAttribute == null || expirationAttribute.getValue() == null) {
					((ButtonCell)this.getCell()).setEnabled(false);
				} else {
					((ButtonCell)this.getCell()).setEnabled(true);
				}
				return translation.extendMembership();
			}
		};

		membershipCol.setFieldUpdater((i, entry, s) -> getUiHandlers().extendMembership(entry.getValue()));

		nameCol.setSortable(true);

		PerunLoader pl = new PerunLoader();
		pl.setEmptyMessage(translation.noResources());
		pl.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
		dataGrid.setEmptyTableWidget(pl);
		dataGrid.setLoadingIndicator(pl);
		dataGrid.addColumn(nameCol, translation.vo());
		dataGrid.addColumn(expirationCol, translation.membershipExpiration());
		dataGrid.addColumn(membershipCol, translation.extendMembership());
		dataGrid.setColumnWidth(nameCol, "40%");
		dataGrid.setColumnWidth(expirationCol, "25%");
		dataGrid.setColumnWidth(membershipCol, "35%");
	}
}
