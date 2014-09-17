package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.pages.Page;
import cz.metacentrum.perun.wui.registrar.client.PerunRegistrar;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.AnchorListItem;

import java.util.ArrayList;

/**
 * Page to display application form for VO or Group.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FormPage extends Page {

	interface FormPageUiBinder extends UiBinder<Widget, FormPage> {
	}

	private static FormPageUiBinder ourUiBinder = GWT.create(FormPageUiBinder.class);

	private Vo vo;
	private Group group;

	@UiField
	PerunForm form;

	/*
	@UiField
	AnchorListItem czech;
	*/

	@UiField
	AnchorListItem english;

	@UiField
	AnchorListItem application;

	@UiField
	AnchorListItem myApplications;

	@UiField
	AnchorListItem help;

	@UiField
	AnchorListItem logout;

	/*
	@UiHandler(value="czech")
	public void czechClick(ClickEvent event) {
		form.setLang("cs");
	}
	*/

	@UiHandler(value="english")
	public void englishClick(ClickEvent event) {
		if (PerunRegistrar.LOCALE.equals("cs")) {
			form.setLang("en");
			application.setText("Registration form");
			myApplications.setText("My registrations");
			help.setText("Help");
			logout.setText("Logout");
			english.setText("Česky");
		} else {
			form.setLang("cs");
			application.setText("Refistrační formulář");
			myApplications.setText("Moje registrace");
			help.setText("Pomoc");
			logout.setText("Odhlásit");
			english.setText("English");
		}
	}

	private Widget rootElement;

	public FormPage() {

		rootElement = ourUiBinder.createAndBindUi(this);

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

	}

	@Override
	public Widget draw() {

		englishClick(null);

		final PerunLoader loader = new PerunLoader();
		form.add(loader);

		final String voName = Window.Location.getParameter("vo");
		final String groupName = Window.Location.getParameter("group");

		RegistrarManager.initialize(voName, groupName, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject jso) {

				ArrayList<Attribute> list = JsUtils.jsoAsList(jso);

				// recreate VO and group
				vo = new JSONObject().getJavaScriptObject().cast();

				if (groupName != null && !groupName.isEmpty()) {
					group = new JSONObject().getJavaScriptObject().cast();
				}

				for (int i=0; i<list.size(); i++) {

					Attribute a = list.get(i);

					if (a.getFriendlyName().equalsIgnoreCase("id")) {
						if (a.getNamespace().equalsIgnoreCase("urn:perun:vo:attribute-def:core")) {
							vo.setId(Integer.parseInt(a.getValue()));
							if (group != null) {
								group.setVoId(Integer.parseInt(a.getValue()));
							}
						} else if (a.getNamespace().equalsIgnoreCase("urn:perun:group:attribute-def:core")) {
							group.setId(Integer.parseInt(a.getValue()));
						}
					} else if (a.getFriendlyName().equalsIgnoreCase("name")) {
						if (a.getNamespace().equalsIgnoreCase("urn:perun:vo:attribute-def:core")) {
							vo.setName(a.getValue());
						} else if (a.getNamespace().equalsIgnoreCase("urn:perun:group:attribute-def:core")) {
							group.setName(a.getValue());
						}
					} else if (a.getFriendlyName().equalsIgnoreCase("shortName")) {
						if (a.getNamespace().equalsIgnoreCase("urn:perun:vo:attribute-def:core")) {
							vo.setShortName(a.getValue());
						}
					} else if (a.getFriendlyName().equalsIgnoreCase("description")) {
						if (a.getNamespace().equalsIgnoreCase("urn:perun:group:attribute-def:core")) {
							group.setDescription(a.getValue());
						}
					} else if (a.getFriendlyName().equalsIgnoreCase("contactEmail")) {
						/*
						if (a.getNamespace().equalsIgnoreCase("urn:perun:vo:attribute-def:def")) {
							// set contact email
							for (int n=0; n<a.getValueAsJsArray().length(); n++) {
								SafeHtmlBuilder s = new SafeHtmlBuilder();
								if (n>0) {
									//others
									s.appendHtmlConstant(voContact.getHTML().concat(", <a href=\"mailto:" + a.getValueAsJsArray().get(n) + "\">" + a.getValueAsJsArray().get(n) + "</a>"));
								} else {
									// first
									s.appendHtmlConstant(voContact.getHTML().concat("<a href=\"mailto:" + a.getValueAsJsArray().get(n) + "\">" + a.getValueAsJsArray().get(n) + "</a>"));
								}

								voContact.setHTML(s.toSafeHtml());
							}
						}
						*/
					}


				}
				// store attrs
				//vo.setAttributes(list);

				JsonEvents formEvents = new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject jso) {

						loader.onFinished();
						loader.removeFromParent();

						form.setFormItems(JsUtils.<ApplicationFormItemData>jsoAsList(jso));

					}

					@Override
					public void onError(PerunException error) {
						// TODO - various exceptions
						loader.onError(error, null);
					}

					@Override
					public void onLoadingStart() {

					}
				};

				if (group != null) {
					RegistrarManager.getFormItemsWithValue(vo.getId(), group.getId(), "en", formEvents);
				} else {
					RegistrarManager.getFormItemsWithValue(vo.getId(), "en", formEvents);
				}



			}

			@Override
			public void onError(PerunException error) {
				loader.onError(error, null);
			}

			@Override
			public void onLoadingStart() {
				loader.onLoading();
			}
		});

		Scheduler.get().scheduleDeferred(new Command() {
			@Override
			public void execute() {
				loader.getWidget().getElement().getFirstChildElement().setAttribute("style", "height: "+ (Window.getClientHeight()-100)+"px;");
			}
		});

		return rootElement;

	}

	@Override
	public void open() {

	}

	@Override
	public String getUrl() {
		return "form";
	}

	@Override
	public void toggleHelp() {

	}

}