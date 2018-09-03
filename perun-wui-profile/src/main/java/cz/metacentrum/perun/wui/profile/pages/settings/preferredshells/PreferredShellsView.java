package cz.metacentrum.perun.wui.profile.pages.settings.preferredshells;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.profile.client.PerunProfileUtils;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.ListGroup;
import org.gwtbootstrap3.client.ui.ListGroupItem;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.html.Span;
import org.gwtbootstrap3.client.ui.html.Strong;


public class PreferredShellsView extends ViewWithUiHandlers<PreferredShellsUiHandlers> implements PreferredShellsPresenter.MyView {

	interface PreferredShellsViewUiBinder extends UiBinder<Widget, PreferredShellsView> {}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	private Attribute preferredShellsAttribute;
	private HandlerRegistration customShellButtonHandler;

	@UiField ListGroup shellsListGroup;
	@UiField Modal customPreferredShellModal;
	@UiField TextBox customShellValueTextBox;
	@UiField PerunButton customShellValueButton;
	@UiField Alert customShellAlert;
	@UiField PerunLoader preferredShellsLoader;

	@Inject
	public PreferredShellsView(PreferredShellsViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));

		preferredShellsLoader.getElement().getStyle().setMarginTop(40, Style.Unit.PX);
	}

	@Override
	public void setPreferredShells(Attribute shells) {
		preferredShellsAttribute = shells;

		JsArrayString values = shells.getValueAsJsArray();

		shellsListGroup.clear();
		shellsListGroup.setVisible(true);

		preferredShellsLoader.setVisible(false);

		if (values == null || values.length() == 0) {
			return;
		}

		for(int i = 0; i < values.length(); i++) {
			int finalI = i;

			String value = values.get(i);

			ListGroupItem item = new ListGroupItem();
			Strong shellLabel = new Strong(value);

			Span span = new Span();
			span.addStyleName("pull-right");

			ButtonGroup buttonGroup = new ButtonGroup();

			PerunButton editButton = new PerunButton(translation.change());
			editButton.setType(ButtonType.DEFAULT);
			editButton.setToggleCaret(true);
			editButton.setDataToggle(Toggle.DROPDOWN);

			PerunButton removeButton = new PerunButton(translation.remove());
			removeButton.setType(ButtonType.DANGER);
			removeButton.addClickHandler((clickEvent -> getUiHandlers().removePreferredShell(preferredShellsAttribute,
					finalI)));
			removeButton.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);

			DropDownMenu editShellMenu = new DropDownMenu();

			for (String shell : PerunProfileUtils.SHELL_OPTIONS) {
				AnchorListItem shellItem = new AnchorListItem();
				shellItem.setText(shell);
				shellItem.addClickHandler((clickEvent -> getUiHandlers().changePreferredShell(preferredShellsAttribute,
						shell, finalI)));

				editShellMenu.add(shellItem);
			}

			AnchorListItem customValue = new AnchorListItem();
			customValue.setText("-- " + translation.customValue() + " --");
			customValue.addClickHandler(clickEvent -> {
				if (customShellButtonHandler != null) {
					customShellButtonHandler.removeHandler();
				}
				customShellButtonHandler = customShellValueButton.addClickHandler(updateValueClickEvent -> {
					String newShellValue = customShellValueTextBox.getValue();
					if(newShellValue.isEmpty()) {
						customShellAlert.setText(translation.emptyShellValue());
						customShellAlert.setVisible(true);
					}
					else if(!PerunProfileUtils.isShellValid(newShellValue)) {
						customShellAlert.setText(translation.invalidShellValue());
						customShellAlert.setVisible(true);
					} else {
						getUiHandlers().changePreferredShell(preferredShellsAttribute, customShellValueTextBox.getValue(), finalI);
						customPreferredShellModal.hide();
					}
				});
				customShellValueTextBox.clear();
				customShellAlert.setVisible(false);
				customPreferredShellModal.show();
			});

			editShellMenu.add(customValue);

			buttonGroup.add(editButton);
			buttonGroup.add(editShellMenu);

			span.add(buttonGroup);
			span.add(removeButton);

			item.add(shellLabel);
			item.add(span);

			shellsListGroup.add(item);
		}
	}

	@Override
	public void setUpdatePreferredShellsStart() {
		preferredShellsLoader.onLoading(translation.loading());
		preferredShellsLoader.setVisible(true);

		shellsListGroup.setVisible(false);
	}

	@Override
	public void setRemovePreferredShellError(PerunException error, int index) {
		preferredShellsLoader.onError(error, (clickEvent -> getUiHandlers().removePreferredShell(
				preferredShellsAttribute, index)));
	}

	@Override
	public void setAddPreferredShellError(PerunException error) {
		preferredShellsLoader.onError(error, (clickEvent -> getUiHandlers().addPreferredShell(
				preferredShellsAttribute)));
	}

	@Override
	public void setChangePreferredShellError(PerunException error, String newValue, int index) {
		preferredShellsLoader.onError(error, (clickEvent -> getUiHandlers().changePreferredShell(
				preferredShellsAttribute, newValue, index)));
	}

	@Override
	public void setLoadPreferredShellsError(PerunException error) {
		preferredShellsLoader.onError(error, (clickEvent -> getUiHandlers().loadPreferredShells()));
	}

	@UiHandler("addPreferredShellButton")
	public void addPreferredShellButtonAction(ClickEvent e) {
		getUiHandlers().addPreferredShell(preferredShellsAttribute);
	}

	@UiHandler("backButton")
	public void cancelButtonAction(ClickEvent event) {
		getUiHandlers().navigateBack();
	}
}
