package cz.metacentrum.perun.wui.profile.pages.privacy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.ListGroup;
import org.gwtbootstrap3.client.ui.ListGroupItem;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PrivacyView extends ViewWithUiHandlers<PrivacyUiHandlers> implements PrivacyPresenter.MyView {

	interface GroupsViewUiBinder extends UiBinder<Widget, PrivacyView> {}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField PerunLoader loader;
	@UiField Text title;
	@UiField Hyperlink completeInfoLink;
	@UiField Text showAllInfoText;
	@UiField Heading aupHeading;
	@UiField ListGroup aupListGroup;
	@UiField Panel aupPanel;

	@Inject
	public PrivacyView(GroupsViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));

		title.setText(translation.menuPrivacy());

		showAllInfoText.setText(translation.showAllInfoText() + " ");
		completeInfoLink.setText(translation.here() + ".");

		aupHeading.setText(translation.aupHeader());
	}

	@Override
	public void clearVos() {
		aupListGroup.clear();
	}

	@Override
	public void addVoWithAulAttribute(Vo vo, Attribute attr) {
		loader.onFinished();
		loader.setVisible(false);

		if (vo == null || attr == null || attr.getValue() == null) {
			return;
		}

		aupPanel.setVisible(true);
		HTML link = new HTML("<a href=\"" + attr.getValue() + "\">" + vo.getName() + "</a>");
		ListGroupItem item = new ListGroupItem();
		item.add(link);
		aupListGroup.setUnstyled(true);
		aupListGroup.add(item);
	}

	@Override
	public void setLoadingError(PerunException e) {
		loader.onError(e, event -> getUiHandlers().loadVosData());
	}

	@Override
	public void setLoadingStart() {

		loader.setVisible(true);
		loader.onLoading(translation.loadingUserData());
	}
}
