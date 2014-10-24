package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.Translatable;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.*;
import cz.metacentrum.perun.wui.pages.Page;
import cz.metacentrum.perun.wui.registrar.client.PerunRegistrar;
import cz.metacentrum.perun.wui.registrar.client.Translations;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.ArrayList;

/**
 * Page to display application form for VO or Group.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FormPage extends Page implements Translatable {

	interface FormPageUiBinder extends UiBinder<Widget, FormPage> {
	}

	private static FormPageUiBinder ourUiBinder = GWT.create(FormPageUiBinder.class);

	private Vo vo;
	private Group group;

	@UiField
	PerunForm form;

	@UiField
	Image logo;

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

		final PerunLoader loader = new PerunLoader();
		form.add(loader);

		final String voName = Window.Location.getParameter("vo");
		final String groupName = Window.Location.getParameter("group");

		RegistrarManager.initializeRegistrar(voName, groupName, new JsonEvents() {

			JsonEvents retry = this;

			@Override
			public void onFinished(JavaScriptObject jso) {

				RegistrarObject object = jso.cast();

				// recreate VO and group
				vo = object.getVo();

				if (groupName != null && !groupName.isEmpty()) {
					group = object.getGroup();
				}

				ArrayList<Attribute> attrList = object.getVoAttributes();
				for (Attribute a : attrList) {
					if (a.getFriendlyName().equals("voLogoURL")) {
						//PerunRegistrar.setLogo(a.getValue().replace("https://", "http://"));
						// FIXME - for testing remove https
						logo.setUrl(a.getValue().replace("https://", "http://"));
						logo.setVisible(true);
					}
				}

				if (object.getException() != null) {

					// SEVERE EXCEPTION - DO NOT LOAD FORM
					loader.onError(object.getException(), new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							RegistrarManager.initializeRegistrar(voName, groupName, retry);
						}
					});

				} else {

					// TODO - properly handle all cases and exceptions in object
					if (object.getVoFormInitial() != null) {

						// VO initial
						loader.onFinished();
						loader.removeFromParent();
						form.setFormItems(object.getVoFormInitial());
						// TODO - if group and groupForm != null then after submission redirect to group form

					} else if (object.getVoFormExtension() != null) {

						if (group == null) {

							// offer VO membership extension
							loader.onFinished();
							loader.removeFromParent();
							form.setFormItems(object.getVoFormExtension());

						} else {

							if (object.getGroupFormInitial() != null) {

								// TODO - ask for optional membership extension
								// YES - display VO extension then redirect to Group init after
								// NO - display Group init
								loader.onFinished();
								loader.removeFromParent();
								form.setFormItems(object.getGroupFormInitial());

							} else {

								// TODO - can't be member of group, offer VO extension
								// YES - display VO extension
								// NO - display Group error
								loader.onFinished();
								loader.removeFromParent();
								form.setFormItems(object.getVoFormExtension());

							}

						}

					} else {

						// not init / not extension / check group
						if (group != null) {

							if (object.getGroupFormInitial() != null) {

								loader.onFinished();
								loader.removeFromParent();
								form.setFormItems(object.getGroupFormInitial());

							} else {

								// FIXME - can't be member of group -> display notice
								// check extension exceptions
								loader.onError(object.getGroupFormInitialException(), new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										RegistrarManager.initializeRegistrar(voName, groupName, retry);
									}
								});

							}

						} else {


							// FIXME - can't become member of VO / extend membership -> display better notice

							if (object.getVoFormInitialException().getName().equals("AlreadyRegisteredException")) {

								// check extension exceptions
								loader.onError(object.getVoFormExtensionException(), new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										RegistrarManager.initializeRegistrar(voName, groupName, retry);
									}
								});


							} else {

								// not registered in VO, show initial exceptions
								loader.onError(object.getVoFormInitialException(), new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										RegistrarManager.initializeRegistrar(voName, groupName, retry);
									}
								});

							}

						}

					}

					// CHECK SIMILAR USERS
					if (object.getSimilarUsers() != null && !object.getSimilarUsers().isEmpty()) {
						showSimilarUsersDialog(object);
					}

				}

			}

			@Override
			public void onError(PerunException error) {
				loader.onError(error, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						RegistrarManager.initializeRegistrar(voName, groupName, retry);
					}
				});
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
	public Widget getWidget() {
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

	@Override
	public void changeLanguage() {
		if (form != null) {
			form.changeLanguage();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	private void showSimilarUsersDialog(RegistrarObject object) {

		final Modal modal = new Modal();
		modal.setTitle(Translations.similarUsersFoundTitle(object.getSimilarUsers().size()));
		modal.setFade(true);
		modal.setDataKeyboard(false);
		modal.setDataBackdrop(ModalBackdrop.STATIC);
		modal.setClosable(false);
		modal.setWidth("700px");

		FlexTable ft = new FlexTable();
		ft.setWidth("100%");
		ft.setCellSpacing(5);

		int row = 0;
		for (Identity identity : object.getSimilarUsers()) {

			ft.setHTML(row, 0, "<strong>"+identity.getName()+"</strong><br />"+
					identity.getOrganization()+"<br />"+identity.getEmail());
			ft.getFlexCellFormatter().setWidth(row, 0, "200px");

			ButtonGroup certGroup = new ButtonGroup();
			DropDownMenu menu = getCertificatesJoinButton(certGroup);

			ButtonGroup idpGroup = new ButtonGroup();
			DropDownMenu menu2 = getIdpJoinButton(idpGroup);

			boolean certFound = false;
			boolean idpFound = false;
			for (ExtSource source : identity.getExternalIdentities()) {

				if (source.getType().equals("cz.metacentrum.perun.core.impl.ExtSourceX509")) {

					String name[] = source.getName().split("\\/");
					for (String cn : name) {
						if (cn.startsWith("CN=")) {
							AnchorListItem link = new AnchorListItem(cn.substring(3));
							menu.add(link);
							link.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									// TODO - get safe hash of own for non-authz
									Window.Location.replace(Utils.getIdentityConsolidatorLink("cert", true));
								}
							});
							break;
						}
					}

					if (!certFound) ft.setWidget(row, 1, certGroup);
					certFound = true;

				} else if (source.getType().equals("cz.metacentrum.perun.core.impl.ExtSourceIdp")) {

					AnchorListItem link = new AnchorListItem(Utils.translateIdp(source.getName()));
					menu2.add(link);
					link.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							// TODO - get safe hash of own for non-authz
							Window.Location.replace(Utils.getIdentityConsolidatorLink("fed", true));
						}
					});
					if (!idpFound) ft.setWidget(row, 2, idpGroup);
					idpFound = true;

				}

			}

			row++;

		}

		ModalBody body = new ModalBody();
		body.add(new Paragraph(Translations.similarUsersFound()));
		body.add(ft);
		modal.add(body);

		ModalFooter footer = new ModalFooter();

		final Button noThanks = new Button("...5...", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modal.hide();
			}
		});
		noThanks.setType(ButtonType.DANGER);
		noThanks.setEnabled(false);

		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
			int counter = 5;
			@Override
			public boolean execute() {
				if (counter <= 0) {
					noThanks.setText(Translations.noThanks());
					noThanks.setEnabled(true);
					return false;
				}
				counter--;
				noThanks.setText("..."+counter+"...");
				return true;
			}
		}, 1000);
		footer.add(noThanks);

		modal.add(footer);
		modal.show();

	}

	private DropDownMenu getCertificatesJoinButton(ButtonGroup certGroup) {

		Button certButt = new Button(Translations.byCertificate(), IconType.CERTIFICATE, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

			}
		});
		certButt.setPaddingLeft(10);
		certButt.setIconFixedWidth(true);
		certButt.setType(ButtonType.SUCCESS);
		certButt.setDataToggle(Toggle.DROPDOWN);
		certGroup.add(certButt);
		DropDownMenu certMenu = new DropDownMenu();
		certGroup.add(certMenu);

		return certMenu;

	}

	private DropDownMenu getIdpJoinButton(ButtonGroup certGroup) {

		Button idpButt = new Button(Translations.byIdp(), IconType.USER, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

			}
		});
		idpButt.setPaddingLeft(10);
		idpButt.setIconFixedWidth(true);
		idpButt.setType(ButtonType.SUCCESS);
		idpButt.setDataToggle(Toggle.DROPDOWN);
		certGroup.add(idpButt);
		DropDownMenu idpMenu = new DropDownMenu();
		certGroup.add(idpMenu);

		return idpMenu;

	}

}