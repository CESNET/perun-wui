package cz.metacentrum.perun.wui.admin.pages.vosManagement;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.pages.ResizableView;
import org.gwtbootstrap3.client.ui.*;

/**
 * PERUN/VO ADMIN - VO DETAIL
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class VoDetailView extends ViewImpl implements VoDetailPresenter.MyView, ResizableView {

	@UiField Heading pageTitle;

	interface VoDetailViewUiBinder extends UiBinder<Widget, VoDetailView> {
	}

	@Inject
	public VoDetailView(VoDetailViewUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setVo(Vo vo) {
		pageTitle.setText(vo.getName());
		pageTitle.setSubText(vo.getShortName());
	}

	@Override
	public void onResize() {

		// TODO

	}

}