package cz.metacentrum.perun.wui.userprofile.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.userprofile.client.UserProfileTranslation;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * View for displaying VO/Group registration detail.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PersonalView extends ViewImpl implements PersonalPresenter.MyView {

	PlaceManager placeManager = PerunSession.getPlaceManager();

	interface PersonalViewUiBinder extends UiBinder<Widget, PersonalView> {
	}

	private UserProfileTranslation translation = GWT.create(UserProfileTranslation.class);

	@UiField
	Text text;

	@UiField
	Small small;

	@Inject
	public PersonalView(PersonalViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
		draw();
	}

	public void draw() {

		text.setText(PerunSession.getInstance().getUser().getFullName());
		small.setText(translation.profile());

	}

}