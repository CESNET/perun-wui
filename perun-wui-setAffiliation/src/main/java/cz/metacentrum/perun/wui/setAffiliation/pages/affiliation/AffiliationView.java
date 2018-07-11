package cz.metacentrum.perun.wui.setAffiliation.pages.affiliation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.*;
import cz.metacentrum.perun.wui.setAffiliation.client.resources.PerunSetAffiliationTranslation;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Text;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * View for setting affiliations
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public class AffiliationView extends ViewWithUiHandlers<AffiliationUiHandlers> implements AffiliationPresenter.MyView {

	interface AffiliationViewUiBinder extends UiBinder<Widget, AffiliationView> {}

	private PerunSetAffiliationTranslation translation = GWT.create(PerunSetAffiliationTranslation.class);
	private static final String PREFIX_DELIMITER = ":";
	private static final String GROUP_PREFIX = "G";
	private static final String VO_PREFIX = "VO";

	@UiField Text title;
	@UiField PerunLoader loader;
	@UiField Row unauthorized;
	@UiField Row form;
	@UiField Heading unauthorizedText;
	@UiField Column voGroupSel;
	@UiField Paragraph voGroupSelectLabel;
	@UiField Select voGroupSelect;
	@UiField Paragraph userSearchbarLabel;
	@UiField TextBox userSearchbar;
	@UiField PerunButton searchUserBtn;
	@UiField Paragraph userSelectLabel;
	@UiField Select userSelect;
	@UiField Paragraph affiliationTitleLabel;
	@UiField CheckBox memberCheckbox;
	@UiField CheckBox facultyCheckbox;
	@UiField CheckBox affiliateCheckbox;
	@UiField Paragraph organizationLabel;
	@UiField Select organizationSelect;
	@UiField PerunButton submitBtn;

	@Inject
	public AffiliationView(AffiliationViewUiBinder binder) {

		initWidget(binder.createAndBindUi(this));

		//texts
		title.setText(translation.setAffiliationTitle());
		voGroupSelectLabel.setText(translation.voGroupSelectLabel());
		userSearchbarLabel.setText(translation.userSearchbarLabel());
		userSearchbar.setPlaceholder(translation.userSearchbarPlaceholder());
		userSelectLabel.setText(translation.userSelectLabel());
		searchUserBtn.setText(translation.searchUserBtn());
		userSelect.setPlaceholder(translation.userSelectPlaceholder());
		affiliationTitleLabel.setText(translation.affiliationTitleLabel());
		memberCheckbox.setText(translation.affiliationMember());
		facultyCheckbox.setText(translation.affiliationFaculty());
		affiliateCheckbox.setText(translation.affiliationAffiliate());
		organizationLabel.setText(translation.organizationLabel());
		submitBtn.setText(translation.submit());
		unauthorizedText.setText(translation.unauthorizedMessage());

		//handlers
		searchUserBtn.addClickHandler(clickEvent -> searchUserCallback());
		submitBtn.addClickHandler(clickEvent -> submitBtnCallback());
		userSearchbar.addKeyUpHandler(keyUpEvent -> userSearchbarKeyUpCallback(keyUpEvent));
		userSelect.addValueChangeHandler(valueChangeEvent -> organizationAndUserSelectCallback());
		organizationSelect.addValueChangeHandler(valueChangeEvent -> organizationAndUserSelectCallback());
	}

	/* EVENT HANDLERS */
	private void userSearchbarKeyUpCallback(KeyUpEvent keyUpEvent) {
		String val = userSearchbar.getValue();
		if (keyUpEvent.getNativeKeyCode() == 13) {
			searchUserCallback();
		} else {
			reset();
			userSearchbar.setValue(val);
		}
	}

	private void submitBtnCallback() {
		Long uid = Long.parseLong(userSelect.getValue());

		getUiHandlers().storeAssignedAffiliations(uid, organizationSelect.getValue(), memberCheckbox.getValue(),
				facultyCheckbox.getValue(), affiliateCheckbox.getValue());
	}

	private void searchUserCallback() {
		if (voGroupSel.isVisible()) {
			String selection = voGroupSelect.getValue();
			String[] parts = selection.split(PREFIX_DELIMITER, 2);
			if (parts[0].equals(VO_PREFIX)) {
				getUiHandlers().loadUsersFromVo(userSearchbar.getText(), Integer.parseInt(parts[1]));
			} else if (parts[0].equals(GROUP_PREFIX)) {
				getUiHandlers().loadUsersFromGroup(userSearchbar.getText(), Integer.parseInt(parts[1]));
			}
		} else {
			getUiHandlers().loadUsers(userSearchbar.getText());
		}
	}

	private void organizationAndUserSelectCallback() {
		boolean userCheckPassed = !(userSelect.getValue() == null || userSelect.getValue().isEmpty());
		boolean orgCheckPassed = !(organizationSelect.getValue() == null || organizationSelect.getValue().isEmpty());

		reloadCheckboxes(userCheckPassed, orgCheckPassed);
	}

	private void reloadCheckboxes(boolean userCheckPassed, boolean orgCheckPassed) {
		if (!(userCheckPassed && orgCheckPassed)) {
			return;
		}
		//both checks have passed
		Option user = userSelect.getSelectedItem();
		Option org = organizationSelect.getSelectedItem();
		if (user == null || user.getValue() == null || user.getValue().isEmpty()
				|| org == null || org.getValue() == null || org.getValue().isEmpty()) {
			return;
		}

		Long uid = Long.parseLong(userSelect.getValue());
		getUiHandlers().loadAssignedAffiliations(uid);
	}

	/* OVERRIDES */

	@Override
	public void onError(PerunException e) {
		loader.onError(e, clickEvent -> loader.setVisible(false));
	}

	@Override
	public void onLoadingStart() {
		loader.setVisible(true);
		loader.onLoading(translation.loadingData());
		setInputsEnabled(false, false);
	}

	@Override
	public void loadUsersUiCallback(List<RichUser> users) {
		onLoadingFinished(false);
		if (users.size() < 1) {
			Window.alert(translation.noUsersFound());
			return;
		}

		for (int i = 0; i < userSelect.getItems().size(); i++) {
			userSelect.remove(i);
		}

		for (RichUser u: users) {
			Option option = new Option();
			option.setText(u.getFullName() + " < " + u.getPreferredEmail() + ">");
			option.setValue(String.valueOf(u.getId()));
			userSelect.add(option);
		}

		userSelect.refresh();
		userSelect.setEnabled(true);
		userSelect.setFocus(true);
		userSelect.setValue("");
	}

	@Override
	public void loadUsersAsMembersUiCallback(List<RichMember> members) {
		onLoadingFinished(false);
		if (members.size() < 1) {
			Window.alert(translation.noUsersFound());
			return;
		}

		List<Option> opts = userSelect.getItems();
		for (Option opt : opts) {
			userSelect.remove(opt);
		}

		for (RichMember m: members) {
			Option option = new Option();
			//we have fetched only user email - it has to be the first and only attribute we have received
			option.setText(m.getUser().getFullName() + " < " + m.getUserAttributes().get(0).getValue() + ">");
			option.setValue(String.valueOf(m.getUserId()));
			userSelect.add(option);
		}

		userSelect.refresh();
		userSelect.setEnabled(true);
		userSelect.setFocus(true);
		userSelect.setValue("");
	}

	@Override
	public void loadAssignedAffiliationsUiCallback(Attribute assignedAffiliations) {
		onLoadingFinished(true);
		submitBtn.setEnabled(true);

		String selectedAffiliation = organizationSelect.getValue();
		setupCheckboxes(false, true);
		Map<String, JSONValue> attributeMap = assignedAffiliations.getValueAsMap();
		for (Map.Entry<String, JSONValue> attr: attributeMap.entrySet()) {
			String[] parts = attr.getKey().replaceAll("\"", "").split("@");
			switch (parts[0]) {
				case "member": {
					if (parts[1].equals(selectedAffiliation)) {
						memberCheckbox.setValue(true);
					}
				} break;
				case "faculty": {
					if (parts[1].equals(selectedAffiliation)) {
						facultyCheckbox.setValue(true);
					}
				} break;
				case "affiliate": {
					if (parts[1].equals(selectedAffiliation)) {
						affiliateCheckbox.setValue(true);
					}
				} break;
			}
		}
	}

	@Override
	public void storeAssignedAffiliationsUiCallback() {
		onLoadingFinished(false);
		reset();
		Window.alert(translation.success());
	}

	@Override
	public void loadVoGroupsAdmin(List<Vo> vos, List<Group> groups) {
		if (vos == null && groups == null) {
			unauthorized();
			return;
			/*-{ console.log() }-*/
		}

		voGroupSel.setVisible(true);
		onLoadingFinished(false);
		userSelect.setEnabled(false);

		if (vos != null) {
			for (Vo vo : vos) {
				Option o = new Option();
				o.setValue(VO_PREFIX + PREFIX_DELIMITER + String.valueOf(vo.getId()));
				o.setText("VO: " + vo.getShortName());
				voGroupSelect.add(o);
			}
		}

		if (groups != null) {
			for (Group group: groups) {
				Option o = new Option();
				o.setValue(GROUP_PREFIX + PREFIX_DELIMITER + String.valueOf(group.getId()));
				o.setText("GROUP: " + group.getShortName());
				voGroupSelect.add(o);
			}
		}

		voGroupSelect.refresh();
	}

	@Override
	public void loadAdminPerun() {
		voGroupSel.setVisible(false);
	}

	@Override
	public void unauthorizedUiCallback() {
		unauthorized();
	}

	@Override
	public void loadAuthorizedAffiliationsUiCallback(Attribute allowedAffiliations) {
		onLoadingFinished(false);
		userSelect.setEnabled(false);
		Map<String, JSONValue> value = allowedAffiliations.getValueAsMap();
		if (value.isEmpty()) {
		    unauthorized();
		    return;
		}

		for (String key: value.keySet()) {
			Option option = new Option();
			String val = value.get(key).toString().replaceAll("\"", "");
			option.setText(val);
			option.setValue(val);
			organizationSelect.add(option);
		}
		organizationSelect.refresh();
	}

	private void unauthorized() {
		unauthorized.setVisible(true);
		form.setVisible(false);
		setInputsEnabled(false, false);
	}

	private void onLoadingFinished(boolean unlockSubmit) {
		loader.onFinished();
		loader.setVisible(false);
		setInputsEnabled(true, unlockSubmit);
	}

	private void setInputsEnabled(boolean enabled, boolean allowSubmit) {
		userSearchbar.setEnabled(enabled);
		userSelect.setEnabled(enabled);
		organizationSelect.setEnabled(enabled);
		searchUserBtn.setEnabled(enabled);
		if (!enabled || allowSubmit) {
			memberCheckbox.setEnabled(enabled);
			facultyCheckbox.setEnabled(enabled);
			affiliateCheckbox.setEnabled(enabled);
			submitBtn.setEnabled(enabled);
		}
	}

	private void setupCheckboxes(boolean checked, boolean enabled) {
		memberCheckbox.setValue(checked);
		facultyCheckbox.setValue(checked);
		affiliateCheckbox.setValue(checked);

		memberCheckbox.setEnabled(enabled);
		facultyCheckbox.setEnabled(enabled);
		affiliateCheckbox.setEnabled(enabled);
	}

	private void reset() {
		userSearchbar.setValue("");
		userSelect.clear();
		userSelect.setEnabled(false);
		submitBtn.setEnabled(false);
		setupCheckboxes(false, false);
		userSearchbar.setFocus(true);
	}
}
