package cz.metacentrum.perun.wui.profile.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Text;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.List;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class GroupsView extends ViewWithUiHandlers<GroupsUiHandlers> implements GroupsPresenter.MyView {

	interface GroupsViewUiBinder extends UiBinder<Widget, GroupsView> {}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField PerunLoader loader;
	@UiField Text title;
	@UiField Select voSelect;
	@UiField Heading memberGroupsLabel;
	@UiField Heading adminGroupsLabel;
	@UiField Heading voLabel;
	@UiField Div voData;
	@UiField Div voHead;
//	@UiField ButtonGroup voButtonGroup;

	@UiField CellTable<Group> memberGroupsTable;
	@UiField CellTable<Group> adminGroupsTable;

	@Inject
	public GroupsView(GroupsViewUiBinder binder) {

		initWidget(binder.createAndBindUi(this));

		initTable(memberGroupsTable);
		initTable(adminGroupsTable);

		title.setText(translation.menuMyGroups());

		memberGroupsLabel.setText(translation.memberGroups());
		adminGroupsLabel.setText(translation.adminGroups());

		voSelect.setTitle(translation.selectVo() + ":");

		voSelect.addValueChangeHandler(valueChangeEvent -> {
			// set heading with vo name
			for (Option o : voSelect.getItems()) {
				if (o.getValue().equals(valueChangeEvent.getValue())) {
					voLabel.setText(o.getText());
				}
			}
			int selectedVoId = Integer.parseInt(valueChangeEvent.getValue());
			getUiHandlers().loadDataForVo(selectedVoId);
		});
	}

	@Override
	public void setVos(List<Vo> vos) {
		if (vos.size() == 1) {
			getUiHandlers().loadDataForVo(vos.get(0).getId());
			voHead.setVisible(false);
			voSelect.setVisible(false);
		} else {
			voSelect.clear();

			voSelect.setVisible(true);

			loader.setVisible(false);
			for (Vo vo : vos) {
				GWT.log(vo.getName());
				Option option = new Option();
				option.setText(vo.getName());
				option.setValue(String.valueOf(vo.getId()));
				voSelect.add(option);
			}
			voSelect.refresh();

			voData.setVisible(false);

			/*for (Vo vo : vos) {
				Button button = new Button();
				button.setText(vo.getName());
				voButtonGroup.add(button);
			}*/
		}
	}

	@Override
	public void setVosError(PerunException ex) {
		loader.onError(ex, event -> getUiHandlers().loadVos());
	}

	@Override
	public void setVoDataError(PerunException error) {
		voSelect.setEnabled(true);
		loader.onError(error, event -> getUiHandlers()
											   .loadDataForVo(Integer.parseInt(voSelect.getSelectedItem().getValue())));
	}

	@Override
	public void loadVosStart() {
		loader.setVisible(true);
		loader.onLoading(translation.loadingUserData());
	}

	@Override
	public void loadVoDataStart() {
		voSelect.setEnabled(false);

		loader.setVisible(true);
		loader.onLoading(translation.loadingUserData());
	}

	@Override
	public void setMemberGroups(List<Group> groups) {
		((PerunLoader)memberGroupsTable.getEmptyTableWidget()).onEmpty();
		memberGroupsTable.setRowData(groups);
	}

	@Override
	public void setAdminGroups(List<Group> groups) {
		((PerunLoader)adminGroupsTable.getEmptyTableWidget()).onEmpty();
		adminGroupsTable.setRowData(groups);
	}

	@Override
	public void onLoadFinish() {
		voSelect.setEnabled(true);
		voData.setVisible(true);

		loader.setVisible(false);
	}

	private void initTable(CellTable<Group> table) {
		TextColumn<Group> nameCol = new TextColumn<Group>() {
			@Override
			public String getValue(Group group) {
				return group.getName();
			}
		};
		TextColumn<Group> descriptionCol = new TextColumn<Group>() {
			@Override
			public String getValue(Group group) {
				return group.getDescription();
			}
		};

		PerunLoader pl = new PerunLoader();
		pl.setEmptyMessage(translation.noGroups());
		pl.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
		table.setEmptyTableWidget(pl);
		table.addColumn(nameCol, translation.name());
		table.addColumn(descriptionCol, translation.description());
	}
}
