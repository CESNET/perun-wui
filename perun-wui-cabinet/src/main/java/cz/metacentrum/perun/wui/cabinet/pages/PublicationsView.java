package cz.metacentrum.perun.wui.cabinet.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.cabinet.client.resources.PerunCabinetTranslation;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.RichPublication;
import cz.metacentrum.perun.wui.model.beans.User;
import cz.metacentrum.perun.wui.model.columnProviders.PublicationColumnProvider;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedSuggestBox;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.List;


public class PublicationsView extends ViewWithUiHandlers<PublicationsUiHandlers> implements PublicationsPresenter.MyView {

	interface PublicationsViewUiBinder extends UiBinder<Widget, PublicationsView> {}

	private PerunCabinetTranslation translation = GWT.create(PerunCabinetTranslation.class);

	@UiField PerunLoader loader;

	@UiField Button searchButton;
	@UiField Div page;
	@UiField Text pageName;
	@UiField Small userName;
	@UiField ExtendedSuggestBox searchTextBox;
	@UiField Button listAllButton;

	@UiField (provided = true)
	PerunDataGrid<RichPublication> publicationsDataGrid;

	@Inject
	public PublicationsView(PublicationsViewUiBinder binder) {

		publicationsDataGrid = new PerunDataGrid<>(new PublicationColumnProvider());

		initWidget(binder.createAndBindUi(this));

		init();
	}

	@Override
	public void loadingPublicationsStart() {
		loader.onLoading(translation.loadingPublications());
		loader.setVisible(true);
		publicationsDataGrid.setVisible(false);
		publicationsDataGrid.clearTable();
		publicationsDataGrid.getLoaderWidget().onLoading();
	}

	@Override
	public void setPublicationsError(PerunException ex) {
		loader.onError(ex, event -> getUiHandlers().loadPublications());
	}

	@Override
	public void setPublications(List<RichPublication> publications) {
		loader.onFinished();
		loader.setVisible(false);
		publicationsDataGrid.setVisible(true);
		publicationsDataGrid.setList(publications);
	}

	@Override
	public void setUser(User user) {
		userName.setText(user.getFullName());
	}


	@UiHandler({"listAllButton"})
	public void onListAll(ClickEvent event) {
		getUiHandlers().loadPublications();
	}

	@UiHandler({"searchButton"})
	public void onFilter(ClickEvent event) {
		getUiHandlers().searchPublications(searchTextBox.getText());
	}

	private void init() {
		//set up search button
		searchButton.setIcon(IconType.SEARCH);
		searchButton.setType(ButtonType.PRIMARY);
		searchButton.setText(translation.search());

		listAllButton.setText(translation.listAll());

		searchTextBox.setPlaceholder(translation.filterText());
		pageName.setText(translation.publications());
	}
}