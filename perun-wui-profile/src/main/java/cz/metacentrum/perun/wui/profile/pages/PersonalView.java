package cz.metacentrum.perun.wui.profile.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.model.beans.User;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.DescriptionData;
import org.gwtbootstrap3.client.ui.DescriptionTitle;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * View for displaying personal info about user
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PersonalView extends ViewImpl implements PersonalPresenter.MyView {

	private RichUser user;

	interface PersonalViewUiBinder extends UiBinder<Widget, PersonalView> {
	}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField
	PerunLoader loader;

	@UiField
	Text text;

	@UiField
	Small small;
	@UiField
	DescriptionTitle nameLabel;
	@UiField
	DescriptionData nameData;
	@UiField
	DescriptionTitle emailLabel;
	@UiField
	DescriptionData emailData;
	@UiField
	DescriptionTitle orgLabel;
	@UiField
	DescriptionData orgData;
	@UiField
	DescriptionTitle phoneLabel;
	@UiField
	DescriptionData phoneData;
	@UiField
	DescriptionTitle langLabel;
	@UiField
	DescriptionData langData;
	@UiField
	DescriptionTitle timeLabel;
	@UiField
	DescriptionData timeData;

	@Inject
	public PersonalView(PersonalViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
	}

	public void draw() {

		text.setText(user.getFullName());
		//small.setText(translation.profile());

		orgLabel.setText(translation.organization());
		nameLabel.setText(translation.name());
		emailLabel.setText(translation.preferredMail());
		phoneLabel.setText(translation.phone());
		langLabel.setText(translation.preferredLang());
		timeLabel.setText(translation.timezone());

		nameData.setText(user.getFullName());
		emailData.setText(user.getPreferredEmail());
		orgData.setText(user.getOrganization());

		Attribute phone = user.getAttribute("urn:perun:user:attribute-def:def:phone");
		if (phone != null) phoneData.setText(phone.getValue());

		Attribute lang = user.getAttribute("urn:perun:user:attribute-def:def:preferredLanguage");
		if (lang != null) langData.setText(lang.getValue());

		Attribute timezone = user.getAttribute("urn:perun:user:attribute-def:def:timezone");
		if (timezone != null) timeData.setText(timezone.getValue());

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