package cz.metacentrum.perun.wui.profile.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class PrivacyView extends ViewWithUiHandlers<PrivacyUiHandlers> implements PrivacyPresenter.MyView {

	interface GroupsViewUiBinder extends UiBinder<Widget, PrivacyView> {}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField Text title;

	@UiField Hyperlink completeInfoLink;
	@UiField Text showAllInfoText;

	@Inject
	public PrivacyView(GroupsViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));

		title.setText(translation.menuPrivacy());

		showAllInfoText.setText(translation.showAllInfoText() + " ");
		completeInfoLink.setText(translation.here() + ".");
	}
}
