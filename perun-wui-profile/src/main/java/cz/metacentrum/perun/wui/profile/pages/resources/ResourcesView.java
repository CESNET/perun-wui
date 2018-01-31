package cz.metacentrum.perun.wui.profile.pages.resources;

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
import cz.metacentrum.perun.wui.model.beans.RichResource;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.gwt.DataGrid;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Text;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class ResourcesView extends ViewWithUiHandlers<ResourcesUiHandlers> implements ResourcesPresenter.MyView {

	interface GroupsViewUiBinder extends UiBinder<Widget, ResourcesView> {}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField Text title;
	@UiField PerunLoader loader;
	@UiField Select voSelect;
	@UiField Heading voLabel;
	@UiField Div resourceData;
	@UiField Div voHead;
	@UiField DataGrid<Map.Entry<RichResource, List<Group>>> resourcesDataGrid;

	@Inject
	public ResourcesView(GroupsViewUiBinder binder) {

		initWidget(binder.createAndBindUi(this));

		title.setText(translation.menuMyResources());

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

		initTable(resourcesDataGrid);
	}

	@Override
	public void setVosError(PerunException ex) {
		loader.onError(ex, event -> getUiHandlers().loadVos());
	}

	@Override
	public void loadVosStart() {
		loader.setVisible(true);
		loader.onLoading(translation.loadingUserData());
	}

	@Override
	public void setVos(List<Vo> vos) {
		if (vos.size() == 1) {
			getUiHandlers().loadDataForVo(vos.get(0).getId());
			voHead.setVisible(false);
			voSelect.removeFromParent();
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

			resourceData.setVisible(false);
			resourcesDataGrid.setVisible(false);
		}
	}

	@Override
	public void setResourcesDataError(PerunException ex) {
		voSelect.setEnabled(true);
		resourceData.setVisible(false);
		resourcesDataGrid.setVisible(false);
		loader.onError(ex, event -> getUiHandlers()
											.loadDataForVo(Integer.parseInt(voSelect.getSelectedItem().getValue())));
	}

	@Override
	public void loadResourcesDataStart() {
		voSelect.setEnabled(false);
		resourceData.setVisible(false);
		resourcesDataGrid.setVisible(false);

		loader.onLoading(translation.loading());
		loader.setVisible(true);
	}

	@Override
	public void setResources(Map<RichResource, List<Group>> richResourceWithGroups) {
		voSelect.setEnabled(true);

		loader.setVisible(false);

		((PerunLoader)resourcesDataGrid.getEmptyTableWidget()).onEmpty();
		List<Map.Entry<RichResource, List<Group>>> tableData = new ArrayList<>(richResourceWithGroups.entrySet());
		tableData.sort(Comparator.comparing(o -> o.getKey().getName()));
		resourcesDataGrid.setRowData(tableData);
		resourceData.setVisible(true);
		resourcesDataGrid.setVisible(true);
	}

	private void initTable(DataGrid<Map.Entry<RichResource, List<Group>>> table) {
		TextColumn<Map.Entry<RichResource, List<Group>>> nameCol = new TextColumn<Map.Entry<RichResource, List<Group>>>() {
			@Override
			public String getValue(Map.Entry<RichResource, List<Group>> richResourceWithGroups) {
				return richResourceWithGroups.getKey().getName();
			}
		};

		TextColumn<Map.Entry<RichResource, List<Group>>> descriptionCol = new TextColumn<Map.Entry<RichResource, List<Group>>>() {
			@Override
			public String getValue(Map.Entry<RichResource, List<Group>> richResourceWithGroups) {
				return richResourceWithGroups.getKey().getDescription();
			}
		};

		TextColumn<Map.Entry<RichResource, List<Group>>> groupsCol = new TextColumn<Map.Entry<RichResource, List<Group>>>() {
			@Override
			public String getValue(Map.Entry<RichResource, List<Group>> richResourceWithGroups) {
				StringBuilder str = new StringBuilder();
				List<Group> groups = richResourceWithGroups.getValue();
				for (int i = 0; i < groups.size(); i++) {
					Group group = groups.get(i);
					str.append(group.getShortName());
					if (i != groups.size()-1) {
						str.append(", ");
					}
				}

				return str.toString();
			}
		};

		PerunLoader pl = new PerunLoader();
		pl.setEmptyMessage(translation.noResources());
		pl.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
		table.setEmptyTableWidget(pl);
		table.addColumn(nameCol, translation.name());
		table.addColumn(descriptionCol, translation.description());
		table.addColumn(groupsCol, translation.resourceGroups());
		table.setColumnWidth(nameCol, "40%");
		table.setColumnWidth(descriptionCol, "30%");
		table.setColumnWidth(groupsCol, "30%");
	}
}
