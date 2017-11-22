package cz.metacentrum.perun.wui.profile.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.model.columnProviders.GroupColumnProvider;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Heading;
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
	@UiField Heading voLabel;
	@UiField Div voData;

	@UiField(provided = true)
	PerunDataGrid<Group> groupsDataGrid;

	@Inject
	public GroupsView(GroupsViewUiBinder binder) {

		groupsDataGrid = new PerunDataGrid<>(new GroupColumnProvider());
		groupsDataGrid.getLoaderWidget().setEmptyMessage(translation.noGroups());
		groupsDataGrid.setSelectionEnabled(false);
		groupsDataGrid.drawTableColumns();

		initWidget(binder.createAndBindUi(this));

		title.setText(translation.menuMyGroups());

		memberGroupsLabel.setText(translation.memberGroups());

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
		voSelect.setVisible(false);

		loader.setVisible(true);
		loader.onLoading(translation.loadingUserData());
	}

	@Override
	public void loadVoDataStart() {
		voSelect.setEnabled(false);
		groupsDataGrid.setVisible(false);

		loader.setVisible(true);
		loader.onLoading(translation.loadingUserData());

		groupsDataGrid.getLoaderWidget().onLoading();
	}

	@Override
	public void setGroups(List<Group> groups) {
		groupsDataGrid.setList(groups);
		voSelect.setEnabled(true);
		groupsDataGrid.setVisible(true);
		voData.setVisible(true);

		loader.setVisible(false);
	}
}
