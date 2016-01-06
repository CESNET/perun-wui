package cz.metacentrum.perun.wui.userprofile.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.json.managers.VosManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.model.beans.User;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.model.columnProviders.VoColumnProvider;
import cz.metacentrum.perun.wui.userprofile.client.UserProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * View for displaying VO membership details
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class OrganizationsView extends ViewImpl implements OrganizationsPresenter.MyView {

	private RichUser user;

	interface PersonalViewUiBinder extends UiBinder<Widget, OrganizationsView> {
	}

	private UserProfileTranslation translation = GWT.create(UserProfileTranslation.class);

	@UiField
	PerunLoader loader;

	@UiField
	Text text;

	@UiField
	Small small;

	@UiField
	Paragraph paragraph;

	@UiField(provided = true)
	PerunDataGrid<Vo> grid = new PerunDataGrid<Vo>(false, new VoColumnProvider());

	@Inject
	public OrganizationsView(PersonalViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
	}

	public void draw() {

		text.setText(translation.menuOrganizations());

		VosManager.getVos(false, new JsonEvents() {

			final JsonEvents events = this;

			@Override
			public void onFinished(JavaScriptObject result) {
				for (Vo vo : JsUtils.<Vo>jsoAsList(result)) {
					GWT.log(vo.getName());
				}
				grid.setList(JsUtils.<Vo>jsoAsList(result));
			}

			@Override
			public void onError(PerunException error) {
				grid.getLoaderWidget().onError(error, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						VosManager.getVos(false, events);
					}
				});
			}

			@Override
			public void onLoadingStart() {
				grid.getLoaderWidget().onLoading();
			}
		});

	}

	@Override
	public void setUser(User user) {
		this.user = user.cast();
		loader.onFinished();
		loader.setVisible(false);
		draw();
	}

	@Override
	public void onLoadingStart() {
		loader.setVisible(true);
		loader.onLoading();
	}

	@Override
	public void onError(PerunException ex, final JsonEvents retry) {
		loader.onError(ex, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				UsersManager.getRichUserWithAttributes(user.getId(), retry);
			}
		});
	}

}