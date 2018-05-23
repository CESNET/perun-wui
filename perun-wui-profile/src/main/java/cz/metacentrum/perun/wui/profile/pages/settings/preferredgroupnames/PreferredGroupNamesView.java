package cz.metacentrum.perun.wui.profile.pages.settings.preferredgroupnames;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileResources;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.ListGroup;
import org.gwtbootstrap3.client.ui.ListGroupItem;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Span;
import org.gwtbootstrap3.client.ui.html.Strong;

import java.util.List;


public class PreferredGroupNamesView extends ViewWithUiHandlers<PreferredGroupNamesUiHandlers> implements PreferredGroupNamesPresenter.MyView {

	interface PreferredGroupNamesViewUiBinder extends UiBinder<Widget, PreferredGroupNamesView> {}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField Div panelsDiv;
	@UiField PerunButton editPreferredNameButton;
	@UiField PerunButton newPreferredNameButton;
	@UiField TextBox editPreferredNameTextBox;
	@UiField TextBox newPreferredNameTextBox;
	@UiField Modal editPreferredNameModal;
	@UiField Modal newPreferredNameModal;
	@UiField PerunLoader loader;

	private HandlerRegistration addPreferredNameButtonHandler;
	private HandlerRegistration newPreferredNameButtonHandler;

	@Inject
	public PreferredGroupNamesView(PreferredGroupNamesViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));

		editPreferredNameTextBox.addKeyDownHandler(keyDownEvent -> {
			if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				editPreferredNameButton.click();
			}
		});
		newPreferredNameTextBox.addKeyDownHandler(keyDownEvent -> {
			if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				newPreferredNameButton.click();
			}
		});
		editPreferredNameModal.addShownHandler(modalShownEvent -> editPreferredNameTextBox.setFocus(true));
		newPreferredNameModal.addShownHandler(modalShownEvent -> newPreferredNameTextBox.setFocus(true));
	}

	@Override
	public void setPreferredGroupNamesAttributes(List<Attribute> attributes) {
		panelsDiv.clear();
		panelsDiv.setVisible(true);
		loader.setVisible(false);

		for (Attribute attribute : attributes) {
			addNamespaceWidgets(attribute);
		}
	}

	@Override
	public void setError(PerunException error, ClickHandler retryAction) {
		loader.onError(error, retryAction);
		loader.setVisible(true);
	}

	@Override
	public void setUpdating() {
		loader.onLoading();
		loader.setVisible(true);
		panelsDiv.setVisible(false);
	}


//	------- Private methods to setup the gui -------


	/**
	 * Adds the edit button to preferred group name item
	 */
	private void addNamespaceListItemEditButton(Span span, Attribute attribute, String name, int index) {
		PerunButton editButton = new PerunButton();
		editButton.setType(ButtonType.INFO);
		editButton.setText(translation.edit());
		editButton.addClickHandler(clickEvent -> {
			if (addPreferredNameButtonHandler != null) {
				addPreferredNameButtonHandler.removeHandler();
			}
			addPreferredNameButtonHandler = editPreferredNameButton.addClickHandler(addClickEvent -> {
				String newValue = editPreferredNameTextBox.getValue();
				editPreferredNameModal.hide();
				getUiHandlers().changePreferredName(attribute, newValue, index);
			});

			editPreferredNameTextBox.setText(name);
			editPreferredNameModal.show();
		});
		span.add(editButton);
	}

	/**
	 * Adds list item for preferred group name
	 */
	private void addNamespaceListNameItem(ListGroup listGroup, Attribute attribute, String name, int index) {
		ListGroupItem listGroupItem = new ListGroupItem();

		Strong nameLabel = new Strong(name);
		Span span = new Span();
		span.addStyleName("pull-right");

		addNamespaceListItemEditButton(span, attribute, name, index);
		addNamespaceLIstItemRemoveButton(span, attribute, index);

		listGroupItem.add(nameLabel);
		listGroupItem.add(span);

		listGroup.add(listGroupItem);
	}

	/**
	 * Adds the remove button to span of a preferred name list item
	 */
	private void addNamespaceLIstItemRemoveButton(Span span, Attribute attribute, int index) {
		PerunButton removeButton = new PerunButton();
		removeButton.setType(ButtonType.DANGER);
		removeButton.setText(translation.remove());
		removeButton.setMarginLeft(10);
		removeButton.addClickHandler(clickEvent -> getUiHandlers().changePreferredName(attribute, "", index));

		span.add(removeButton);
	}

	/**
	 * For each preferred name adds an item to the panel
	 */
	private void addNamespaceNameItems(PanelBody body, Attribute attribute) {
		ListGroup listGroup = new ListGroup();
			listGroup.addStyleName(PerunProfileResources.INSTANCE.gss().panelList());

		JsArrayString names = attribute.getValueAsJsArray();
		if (names != null) {
			for (int j = 0; j < names.length(); j++) {
				String name = names.get(j);
				addNamespaceListNameItem(listGroup, attribute, name, j);
			}
		}
		body.add(listGroup);
	}

	/**
	 * Sets up the body of the panel with preferred group names
	 */
	private void addNamespacePanelBody(Panel panel, Attribute attribute) {
		if (attribute.getValue() == null) {
			return;
		}
		PanelBody body = new PanelBody();
		body.addStyleName(PerunProfileResources.INSTANCE.gss().noPadding());

		addNamespaceNameItems(body, attribute);

		panel.add(body);
	}

	/**
	 * Sets up the header for panel with namespace name
	 */
	private void addNamespacePanelHeader(Panel panel, Attribute attribute) {
		PanelHeader header = new PanelHeader();
		header.setText(translation.preferredGroupNameHeaderText() + attribute.getFriendlyNameParameter() + "\'");
		panel.add(header);
	}

	/**
	 * Adds the 'new' button to add preferred group name
	 */
	private void addNamespaceDivAddButton(Attribute attribute) {
		PerunButton addNameButton = new PerunButton();
		addNameButton.setType(ButtonType.SUCCESS);
		addNameButton.setText(translation.addPreferredGroupName());
		addNameButton.setMarginTop(5);
		addNameButton.setMarginBottom(30);
		addNameButton.setIcon(IconType.PLUS);
		addNameButton.addClickHandler(clickEvent -> {
			if (newPreferredNameButtonHandler != null) {
				newPreferredNameButtonHandler.removeHandler();
			}
			newPreferredNameButtonHandler = newPreferredNameButton.addClickHandler(newClickEvent -> {
				String value = newPreferredNameTextBox.getValue();
				newPreferredNameModal.hide();
				getUiHandlers().newPreferredName(attribute, value);
			});
			newPreferredNameModal.show();
			newPreferredNameTextBox.setFocus(true);
			newPreferredNameTextBox.clear();
		});

		panelsDiv.add(addNameButton);
	}

	/**
	 * Sets up widgets for preferred group names
	 */
	private void addNamespaceWidgets(Attribute attribute) {

		addNamespaceDivPanel(attribute);
		addNamespaceDivAddButton(attribute);
	}

	private void addNamespaceDivPanel(Attribute attribute) {
		Panel panel = new Panel();
		panel.addStyleName(PerunProfileResources.INSTANCE.gss().preferredUnixGroupNamesPanel());

		addNamespacePanelHeader(panel, attribute);
		addNamespacePanelBody(panel, attribute);

		panelsDiv.add(panel);
	}

	@UiHandler("backButton")
	public void cancelButtonAction(ClickEvent event) {
		getUiHandlers().navigateBack();
	}
}
