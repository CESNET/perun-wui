package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.*;
import cz.metacentrum.perun.wui.registrar.model.ApplicationColumnProvider;
import cz.metacentrum.perun.wui.pages.Page;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * Page to display application form for VO or Group.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class AppsPage extends Page {

	interface AppsPageUiBinder extends UiBinder<Widget, AppsPage> {
	}

	private static AppsPageUiBinder ourUiBinder = GWT.create(AppsPageUiBinder.class);

	@UiField(provided = true)
	PerunDataGrid<Application> grid;

	@UiField
	Text text;

	private Widget rootElement;

	private RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

	public AppsPage() {

		grid = new PerunDataGrid<Application>(false, new ApplicationColumnProvider());
		rootElement = ourUiBinder.createAndBindUi(this);
		text.setText(translation.submittedTitle());

	}

	@Override
	public boolean isPrepared() {
		return true;
	}

	@Override
	public boolean isAuthorized() {
		return true;
	}

	@Override
	public void onResize() {

		Scheduler.get().scheduleDeferred(new Command() {
			@Override
			public void execute() {
				int height = DOM.getElementById("web-content").getAbsoluteBottom();
				if (DOM.getElementById("web-content").getAbsoluteBottom() < Window.getClientHeight()) {
					height = Window.getClientHeight();
					if (Window.getClientHeight() < 700) {
						height = 700;
					}
				}
				grid.setHeight(height - grid.getAbsoluteTop() -10 + "px");
				grid.onResize();
			}
		});

	}

	@Override
	public Widget draw() {

		RegistrarManager.getApplicationsForUser(PerunSession.getInstance().getUserId(), new JsonEvents() {

			JsonEvents loadAgain = this;

			@Override
			public void onFinished(JavaScriptObject jso) {
				grid.setList(JsUtils.<Application>jsoAsList(jso));
			}

			@Override
			public void onError(PerunException error) {
				grid.getLoaderWidget().onError(error, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						RegistrarManager.getApplicationsForUser(PerunSession.getInstance().getUser().getId(), loadAgain);
					}
				});
			}

			@Override
			public void onLoadingStart() {
				grid.clearTable();
				grid.getLoaderWidget().onLoading();
			}
		});

		return rootElement;

	}

	@Override
	public Widget getWidget() {
		return rootElement;
	}

	@Override
	public void open() {

	}

	@Override
	public String getUrl() {
		return "submitted";
	}

	@Override
	public void toggleHelp() {

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return 11;
	}

}