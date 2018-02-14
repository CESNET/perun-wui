package cz.metacentrum.perun.wui.profile.pages.settings.datalimits;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.RichResource;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.model.common.RTMessage;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.profile.widgets.ResourceQuotasPanel;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.metacentrum.perun.wui.profile.client.PerunProfileUtils.A_D_R_USER_SETTINGS_NAME_NAME;


public class DataQuotasView extends ViewWithUiHandlers<DataQuotasUiHandlers> implements DataQuotasPresenter.MyView {

	interface DataQuotesViewUiBinder extends UiBinder<Widget, DataQuotasView> {}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField PerunLoader loader;
	@UiField Div panelsDiv;
	@UiField(provided = true) Select voSelect = new Select();
	@UiField Heading voLabel;
	@UiField Alert noResourcesAlert;
	@UiField Alert successAlert;

	private int numberOfLoadingResources;
	private boolean containsAnyResource = false;
	private int numberOfLoadedResources = 0;

	@Inject
	public DataQuotasView(DataQuotesViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
	}

	@Override
	public void setVos(List<Vo> vos) {
		if (vos.size() == 1) {
			getUiHandlers().loadDataForVo(vos.get(0).getId());
			voSelect.setVisible(false);
		} else {
			voSelect.clear();

			loader.setVisible(false);
			for (Vo vo : vos) {
				Option option = new Option();
				option.setText(vo.getName());
				option.setValue(String.valueOf(vo.getId()));
				voSelect.add(option);
			}

			voSelect.refresh();
			voSelect.setVisible(true);
		}

		panelsDiv.clear();
		voLabel.clear();
		successAlert.setVisible(false);
	}

	@Override
	public void setSendingRTMessage() {
		panelsDiv.setVisible(false);
		successAlert.setVisible(false);
		loader.onLoading(translation.sendingRequest());
		loader.setVisible(true);
		successAlert.setVisible(false);
	}

	@Override
	public void setRTMessageSend(RTMessage message) {
		panelsDiv.setVisible(true);
		loader.setVisible(false);
		successAlert.setText(translation.rtMessageSuccess() + message.getMemberPreferredEmail());
		successAlert.setVisible(true);
	}

	@Override
	public void setRTMessageError(PerunException error, ClickHandler retry) {
		panelsDiv.setVisible(true);
		loader.onError(error, retry);
	}

	@Override
	public void addResource(RichResource resource, List<Attribute> attrs) {

		Map<String, Attribute> indexedAttrs = new HashMap<>();
		for (final Attribute attr : attrs) {
			indexedAttrs.put(attr.getFriendlyName(), attr);
		}

		Attribute settingsName = indexedAttrs.get(A_D_R_USER_SETTINGS_NAME_NAME);

		// skip this resource because it does not have assigned required attribute
		if (settingsName == null || settingsName.getValue() == null || settingsName.getValue().isEmpty()) {
			// check if this was the last resource loaded and if any of the previous is shown
			++numberOfLoadedResources;
			if (!containsAnyResource && numberOfLoadedResources == numberOfLoadingResources) {
				showNoResourcesFound();
			}
			return;
		}

		containsAnyResource = true;
		++numberOfLoadedResources;
		loader.setVisible(false);

		ResourceQuotasPanel panel = new ResourceQuotasPanel(resource, indexedAttrs, getUiHandlers());

		panelsDiv.add(panel);
	}

	@Override
	public void setNumberOfLoadingResources(int count) {
		numberOfLoadingResources = count;

		if (numberOfLoadingResources == 0) {
			showNoResourcesFound();
		}
	}

	@Override
	public void init() {
		voSelect.addValueChangeHandler(new ValueChangeHandler<String>() {

			// FIXME: Hack used HERE
			// This anonymous class remembers value of previous valueChangeEvent to
			// disable calling the same operation multiple times. Basically it this
			// should not happen, when value is changed it should be only fired when
			// the current value is different than the previous one, but it happens.
			// This hack fixes the 'Bug?'
			private String previousValue;

			@Override
			public void onValueChange(ValueChangeEvent<String> valueChangeEvent) {
				// FIXME: HACK
				String currentValue = valueChangeEvent.getValue();
				if (currentValue.equals(previousValue)) {
					return;
				}
				previousValue = currentValue;

				// set heading with vo name
				for (Option o : voSelect.getItems()) {
					if (o.getValue().equals(valueChangeEvent.getValue())) {
						voLabel.setText(o.getText());
					}
				}
				int selectedVoId = Integer.parseInt(valueChangeEvent.getValue());
				DataQuotasView.this.getUiHandlers().loadDataForVo(selectedVoId);
			}
		});
	}

	@Override
	public void setLoadingData() {
		panelsDiv.clear();
		numberOfLoadedResources = 0;
		containsAnyResource = false;

		loader.onLoading();
		loader.setVisible(true);
		noResourcesAlert.setVisible(false);
		successAlert.setVisible(false);
	}

	@Override
	public void setError(PerunException error, ClickHandler retry) {
		panelsDiv.clear();

		loader.onError(error, retry);
		loader.setVisible(true);
	}

	@UiHandler("backButton")
	public void cancelButtonAction(ClickEvent notUsed) {
		getUiHandlers().navigateBack();
	}

	private void showNoResourcesFound() {
		loader.setVisible(false);

		noResourcesAlert.setVisible(true);
		successAlert.setVisible(false);
	}
}
