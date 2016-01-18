package cz.metacentrum.perun.wui.admin.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.html.Div;

import java.util.Objects;

/**
 * Perun Admin WUI left menu.
 *
 * @author Pavel Zlámal
 */
public class LeftMenuView extends ViewWithUiHandlers<LeftMenuUiHandlers> implements LeftMenuPresenter.MyView {

	interface LeftMenuUiBinder extends UiBinder<Div, LeftMenuView> {
	}

	@UiField
	PanelCollapse perunManagerMenu;

	@UiField
	PanelCollapse voManagerMenu;

	@UiField
	PanelCollapse groupManagerMenu;

	@UiField
	PanelCollapse facilityManagerMenu;

	@UiField
	Panel perunManager;

	@UiField
	Panel voManager;

	@UiField
	Panel groupManager;

	@UiField
	Panel facilityManager;

	@UiField NavPills perunAdminMenuPills;
	@UiField NavPills voManagerMenuPills;
	@UiField NavPills groupManagerMenuPills;
	@UiField NavPills facilityManagerMenuPills;

	@UiField
	AnchorListItem perunVos;
	@UiField
	AnchorListItem perunFacs;
	@UiField
	AnchorListItem perunUsr;
	@UiField
	AnchorListItem perunAttrs;
	@UiField
	AnchorListItem perunServ;
	@UiField
	AnchorListItem perunOwn;
	@UiField
	AnchorListItem perunExtsrc;
	@UiField
	AnchorListItem perunName;
	@UiField
	AnchorListItem perunSearch;

	@UiField
	AnchorListItem selectVo;

	@Inject
	public LeftMenuView(LeftMenuUiBinder binder) {

		initWidget(binder.createAndBindUi(this));

		// set visibility of menu per roles

		if (PerunSession.getInstance().isPerunAdmin()) {
			perunManager.setVisible(true);
			perunManagerMenu.setIn(true);
		}
		if (PerunSession.getInstance().isVoAdmin() || PerunSession.getInstance().isVoObserver()) {
			voManager.setVisible(true);
			groupManager.setVisible(true);
			if (!PerunSession.getInstance().isPerunAdmin()) {
				voManagerMenu.setIn(true);
			}
		}
		if (PerunSession.getInstance().isGroupAdmin()) {
			groupManager.setVisible(true);
			if (!PerunSession.getInstance().isVoAdmin() && !PerunSession.getInstance().isVoObserver()) {
				groupManagerMenu.setIn(true);
			}
		}
		if (PerunSession.getInstance().isFacilityAdmin()) {
			facilityManager.setVisible(true);
			if (!PerunSession.getInstance().isVoAdmin() && !PerunSession.getInstance().isVoObserver()) {
				// if only group admin, prefer facility admin menu
				facilityManagerMenu.setIn(true);
			}
		}

	}

	@UiHandler("perunVos")
	public void click1(ClickEvent handler) {
		perunVos.setActive(true);
	}

	@UiHandler("perunFacs")
	public void click2(ClickEvent handler) {
		perunFacs.setActive(true);
	}

	@UiHandler("perunUsr")
	public void click3(ClickEvent handler) {
		perunUsr.setActive(true);
	}

	@UiHandler("perunAttrs")
	public void click4(ClickEvent handler) {
		perunAttrs.setActive(true);
	}

	@UiHandler("perunServ")
	public void click5(ClickEvent handler) {
		perunServ.setActive(true);
	}

	@UiHandler("perunOwn")
	public void click6(ClickEvent handler) {
		perunOwn.setActive(true);
	}

	@UiHandler("perunExtsrc")
	public void click7(ClickEvent handler) {
		perunExtsrc.setActive(true);
	}

	@UiHandler("perunName")
	public void click8(ClickEvent handler) {
		perunName.setActive(true);
	}

	@UiHandler("perunExtsrc")
	public void click9(ClickEvent handler) {
		perunExtsrc.setActive(true);
	}

	@UiHandler("selectVo")
	public void click10(ClickEvent handler) {
		getUiHandlers().selectVo();
	}

	@Override
	public void setActiveMenuItem(String token) {

		int count = perunAdminMenuPills.getWidgetCount();
		for (int i=0; i < count; i++) {
			if (perunAdminMenuPills.getWidget(i) instanceof AnchorListItem) {
				AnchorListItem item = (AnchorListItem)perunAdminMenuPills.getWidget(i);
				if (Objects.equals(token, item.getTargetHistoryToken())) {
					item.setActive(true);
				} else {
					item.setActive(false);
				}
			}
		}

		int count2 = voManagerMenuPills.getWidgetCount();
		for (int i=0; i < count2; i++) {
			if (voManagerMenuPills.getWidget(i) instanceof AnchorListItem) {
				AnchorListItem item = (AnchorListItem)voManagerMenuPills.getWidget(i);
				if (Objects.equals(token, item.getTargetHistoryToken())) {
					item.setActive(true);
				} else {
					item.setActive(false);
				}
			}
		}

		int count3 = groupManagerMenuPills.getWidgetCount();
		for (int i=0; i < count3; i++) {
			if (groupManagerMenuPills.getWidget(i) instanceof AnchorListItem) {
				AnchorListItem item = (AnchorListItem)groupManagerMenuPills.getWidget(i);
				if (Objects.equals(token, item.getTargetHistoryToken())) {
					item.setActive(true);
				} else {
					item.setActive(false);
				}
			}
		}

		int count4 = facilityManagerMenuPills.getWidgetCount();
		for (int i=0; i < count4; i++) {
			if (facilityManagerMenuPills.getWidget(i) instanceof AnchorListItem) {
				AnchorListItem item = (AnchorListItem)facilityManagerMenuPills.getWidget(i);
				if (Objects.equals(token, item.getTargetHistoryToken())) {
					item.setActive(true);
				} else {
					item.setActive(false);
				}
			}
		}

	}


}