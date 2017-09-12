package cz.metacentrum.perun.wui.cabinet.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.cabinet.client.resources.PerunCabinetTranslation;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import org.gwtbootstrap3.client.ui.html.Div;

/**
 * Main View for Perun Cabinet.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PerunCabinetView extends ViewImpl implements PerunCabinetPresenter.MyView {

	interface PerunCabinetViewUiBinder extends UiBinder<Widget, PerunCabinetView> {}

	@UiField Div pageContent;

	private PerunCabinetTranslation translations = GWT.create(PerunCabinetTranslation.class);

	@Inject
	PerunCabinetView(final PerunCabinetViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
	}

	@Override
	public void setInSlot(final Object slot, final IsWidget content) {
		if (slot == PerunPresenter.SLOT_MAIN_CONTENT) {
			pageContent.clear();
			if (content != null) {
				pageContent.add(content);
			}
		} else {
			super.setInSlot(slot, content);
		}
	}
}