package cz.metacentrum.perun.wui.profile.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AttributesManager;
import cz.metacentrum.perun.wui.json.managers.MembersManager;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.json.managers.VosManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Member;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.model.beans.User;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.model.columnProviders.VoColumnProvider;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Span;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.ArrayList;
import java.util.Collections;

/**
 * View for displaying VO membership details
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class OrganizationsView extends ViewImpl implements OrganizationsPresenter.MyView {

	private RichUser user;

	interface PersonalViewUiBinder extends UiBinder<Widget, OrganizationsView> {
	}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField
	PerunLoader loader;

	@UiField
	Text text;

	@UiField
	Small small;

	@UiField
	Alert paragraph;

	@UiField
	Div page;

	//@UiField(provided = true)
	//PerunDataGrid<Vo> grid = new PerunDataGrid<Vo>(false, new VoColumnProvider());

	@Inject
	public OrganizationsView(PersonalViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
	}

	public void draw() {

		text.setText(translation.menuOrganizations());

		UsersManager.getVosWhereUserIsMember(user.getId(), new JsonEvents() {

			final JsonEvents events = this;

			@Override
			public void onFinished(JavaScriptObject result) {
				//grid.setList(JsUtils.<Vo>jsoAsList(result));

				ArrayList<Vo> vos = JsUtils.<Vo>jsoAsList(result);
				Collections.sort(vos, new PerunComparator<Vo>(PerunColumnType.NAME));
				boolean in = false;
				for (final Vo vo : vos) {

					final Panel p = new Panel();
					PanelCollapse pc = new PanelCollapse();
					if (!in) {
						pc.setIn(true);
						in = true;
					}
					final PanelHeader ph = new PanelHeader();
					final PanelBody body = new PanelBody();
					Heading head = new Heading(HeadingSize.H4, vo.getName());
					ph.add(head);
					ph.setDataToggle(Toggle.COLLAPSE);
					ph.setDataTargetWidget(pc);
					p.add(ph);
					p.add(pc);
					pc.add(body);
					body.add(new Paragraph("There will be content"));
					page.add(p);

					MembersManager.getMemberByUser(user.getId(), vo.getId(), new JsonEvents() {
						@Override
						public void onFinished(JavaScriptObject result) {
							Member member = result.cast();

							Paragraph par = new Paragraph("Membership status: "+member.getMembershipStatus());
							body.clear();
							body.add(par);

							if (member.getMembershipStatus().equals("VALID")) p.setType(PanelType.DEFAULT);
							if (member.getMembershipStatus().equals("EXPIRED")) p.setType(PanelType.WARNING);
							if (member.getMembershipStatus().equals("DISABLED")) p.setType(PanelType.DANGER);
							if (member.getMembershipStatus().equals("INVALID")) p.setType(PanelType.DANGER);
							if (member.getMembershipStatus().equals("SUSPENDED")) p.setType(PanelType.DANGER);


						}

						@Override
						public void onError(PerunException error) {

						}

						@Override
						public void onLoadingStart() {

						}
					});


				}

			}

			@Override
			public void onError(PerunException error) {
				/*grid.getLoaderWidget().onError(error, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						VosManager.getVos(false, events);
					}
				});*/
			}

			@Override
			public void onLoadingStart() {
				//grid.getLoaderWidget().onLoading();
				page.clear();
			}
		});

	}

	@Override
	public void setUser(User user) {
		loader.onFinished();
		loader.setVisible(false);
		if (this.user == null || this.user.getId() != user.getId()) {
			this.user = user.cast();
			draw();
		}
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