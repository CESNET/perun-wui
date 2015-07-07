package cz.metacentrum.perun.wui.admin.pages.vosManagement;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.VosManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Vo;
import org.gwtbootstrap3.client.ui.LinkedGroup;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class VoSelectView extends ViewImpl implements VoSelectPresenter.MyView {

	interface VoSelectViewUiBinder extends UiBinder<Modal, VoSelectView> {
	}

	@UiField Modal itself;
	@UiField ModalBody body;

	@Inject
	public VoSelectView(VoSelectViewUiBinder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void show() {

		VosManager.getVos(false, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject jso) {
				setVos(JsUtils.<Vo>jsoAsList(jso));
			}

			@Override
			public void onError(PerunException error) {

			}

			@Override
			public void onLoadingStart() {

			}
		});


	}

	@Override
	public void setVos(ArrayList<Vo> vos) {
		body.clear();
		ScrollPanel sp = new ScrollPanel();
		sp.setHeight("400px");
		LinkedGroup group = new LinkedGroup();
		sp.add(group);

		Collections.sort(vos, new Comparator<Vo>() {
			@Override
			public int compare(Vo o1, Vo o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		for (Vo vo : vos) {
			LinkedGroupItem item = new LinkedGroupItem(vo.getName(), "#"+PlaceTokens.VOS_DETAIL+";id="+vo.getId());
			item.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					itself.hide();
				}
			});
			group.add(item);
		}

		body.add(sp);
		itself.show();
	}


}
