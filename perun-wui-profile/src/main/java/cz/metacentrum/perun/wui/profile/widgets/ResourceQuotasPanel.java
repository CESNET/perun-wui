package cz.metacentrum.perun.wui.profile.widgets;

import com.google.gwt.core.client.GWT;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.Resource;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileResources;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.profile.pages.settings.datalimits.DataQuotasUiHandlers;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.ListGroup;
import org.gwtbootstrap3.client.ui.ListGroupItem;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.html.Strong;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.Map;

import static cz.metacentrum.perun.wui.profile.client.PerunProfileUtils.A_D_R_DATA_LIMIT_NAME;
import static cz.metacentrum.perun.wui.profile.client.PerunProfileUtils.A_D_R_DEFAULT_DATA_LIMIT_NAME;
import static cz.metacentrum.perun.wui.profile.client.PerunProfileUtils.A_D_R_DEFAULT_FILES_LIMIT_NAME;
import static cz.metacentrum.perun.wui.profile.client.PerunProfileUtils.A_D_R_FILES_LIMIT_NAME;
import static cz.metacentrum.perun.wui.profile.client.PerunProfileUtils.A_D_R_USER_SETTINGS_NAME_NAME;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class ResourceQuotasPanel extends Panel {

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	private Resource resource;
	private Map<String, Attribute> indexedAttrs;
	private DataQuotasUiHandlers handlers;

	public ResourceQuotasPanel(Resource resource, Map<String, Attribute> indexedAttrs, DataQuotasUiHandlers handlers) {
		super();

		this.resource = resource;
		this.indexedAttrs = indexedAttrs;
		this.handlers = handlers;

		setUp();
	}

	private void setUp() {

		Attribute settingsName = indexedAttrs.get(A_D_R_USER_SETTINGS_NAME_NAME);

		createPanelHeader(settingsName);
		createPanelBody();
	}

	/**
	 * Creates heading for vo panel
	 */
	private void createPanelHeader(Attribute nameAttribute) {
		PanelHeader header = new PanelHeader();

		Heading heading = new Heading(HeadingSize.H4);

		Anchor anchor = new Anchor();
		anchor.setIcon(IconType.DATABASE);
		anchor.setDataToggle(Toggle.COLLAPSE);
		anchor.setDataTarget("#" + resource.getId());
		anchor.setText(nameAttribute.getValue());

		heading.add(anchor);

		header.add(heading);

		this.add(header);
	}

	/**
	 * Creates body for vo panel
	 */
	private void createPanelBody() {

		PanelCollapse collapse = new PanelCollapse();
		collapse.setId(String.valueOf(resource.getId()));

		PanelBody body = new PanelBody();
		body.addStyleName(PerunProfileResources.INSTANCE.gss().noPadding());

		createBodyItems(body);

		collapse.add(body);

		this.add(collapse);
	}

	private void addRequestChangeButton(ListGroupItem item, QuotaType quotaType) {
		Button requestChangeButton = new Button();
		requestChangeButton.setType(ButtonType.INFO);
		requestChangeButton.setText(translation.requestChange());
		requestChangeButton.addStyleName("pull-right");
		item.add(requestChangeButton);

		QuotaChangeModal modal = new QuotaChangeModal(resource, indexedAttrs, quotaType, handlers);
		this.add(modal);
		requestChangeButton.addClickHandler(e -> modal.draw());
	}

	private void createBodyItems(PanelBody body) {
		ListGroup listGroup = new ListGroup();
		listGroup.addStyleName(PerunProfileResources.INSTANCE.gss().panelList());

		if (indexedAttrs.containsKey(A_D_R_DEFAULT_DATA_LIMIT_NAME)) {
			createLimitItem(listGroup, indexedAttrs.get(A_D_R_DEFAULT_DATA_LIMIT_NAME),
					indexedAttrs.get(A_D_R_DATA_LIMIT_NAME), QuotaType.DATA);
		}

		if (indexedAttrs.containsKey(A_D_R_DEFAULT_FILES_LIMIT_NAME)) {
			createLimitItem(listGroup, indexedAttrs.get(A_D_R_DEFAULT_FILES_LIMIT_NAME),
					indexedAttrs.get(A_D_R_FILES_LIMIT_NAME), QuotaType.FILES);
		}

		body.add(listGroup);
	}

	private void createLimitItem(ListGroup listGroup, Attribute defaultAttr, Attribute attr, QuotaType type) {
		ListGroupItem item = new ListGroupItem();
		Strong label = new Strong();
		Text value = new Text();

		if (type.equals(QuotaType.DATA)) {
			label.setText(translation.dataQuota() + ": ");
		} else if (type.equals(QuotaType.FILES)) {
			label.setText(translation.filesQuota() + ": ");
		}

		String defaultValue;

		if (defaultAttr == null || defaultAttr.isEmpty()) {
			defaultValue = translation.notSet();
		} else {
			defaultValue = defaultAttr.getValue();
		}
		value.setText((attr.getValue() != null ? attr.getValue() : translation.usingDefault()) +
							  " (" + translation.defaultValue() + ": " + defaultValue + ")");

		item.add(label);
		item.add(value);

		addRequestChangeButton(item, type);

		listGroup.add(item);
	}
}
