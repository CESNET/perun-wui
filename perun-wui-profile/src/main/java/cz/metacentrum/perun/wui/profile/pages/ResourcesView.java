package cz.metacentrum.perun.wui.profile.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.RichResource;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.model.columnProviders.ResourceColumnProvider;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Text;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.List;


public class ResourcesView extends ViewWithUiHandlers<ResourcesUiHandlers> implements ResourcesPresenter.MyView {

	interface GroupsViewUiBinder extends UiBinder<Widget, ResourcesView> {}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField Text title;
	@UiField PerunLoader loader;
	@UiField Select voSelect;
	@UiField Heading voLabel;
	@UiField Div resourceData;
	@UiField Div voHead;

	@UiField(provided = true)
	PerunDataGrid<RichResource> resourcesDataGrid;

	@Inject
	public ResourcesView(GroupsViewUiBinder binder) {
		resourcesDataGrid = new PerunDataGrid<>(new ResourceColumnProvider());

		initWidget(binder.createAndBindUi(this));

		resourcesDataGrid.setSelectionEnabled(false);
		resourcesDataGrid.drawTableColumns();
		resourcesDataGrid.getLoaderWidget().setEmptyMessage(translation.noResources());

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
		}
	}

	@Override
	public void setResourcesDataError(PerunException ex) {
		voSelect.setEnabled(true);
		loader.onError(ex, event -> getUiHandlers()
											.loadDataForVo(Integer.parseInt(voSelect.getSelectedItem().getValue())));
	}

	@Override
	public void loadResourcesDataStart() {
		voSelect.setEnabled(false);

		loader.setVisible(true);
		loader.onLoading(translation.loadingUserData());
	}

	@Override
	public void setResources(List<RichResource> resources) {
		voSelect.setEnabled(true);

		loader.onFinished();
		loader.setVisible(false);

		resourcesDataGrid.setList(resources);
		resourceData.setVisible(true);
	}
}
