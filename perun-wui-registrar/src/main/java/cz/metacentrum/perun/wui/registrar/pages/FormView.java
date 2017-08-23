package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItem;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.ExtSource;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.Identity;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.client.ExceptionResolver;
import cz.metacentrum.perun.wui.registrar.client.ExceptionResolverImpl;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;
import cz.metacentrum.perun.wui.registrar.pages.steps.FormStepManager;
import cz.metacentrum.perun.wui.registrar.pages.steps.GroupInitStep;
import cz.metacentrum.perun.wui.registrar.pages.steps.StepManager;
import cz.metacentrum.perun.wui.registrar.pages.steps.Summary;
import cz.metacentrum.perun.wui.registrar.pages.steps.SummaryImpl;
import cz.metacentrum.perun.wui.registrar.pages.steps.SummaryStep;
import cz.metacentrum.perun.wui.registrar.pages.steps.VoExtOfferStep;
import cz.metacentrum.perun.wui.registrar.pages.steps.VoExtStep;
import cz.metacentrum.perun.wui.registrar.pages.steps.VoInitStep;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.widgets.AlertErrorReporter;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * View for displaying registration form of VO / Group
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FormView extends ViewImpl implements FormPresenter.MyView {

	interface FormViewUiBinder extends UiBinder<Widget, FormView> {
	}

	private FormView formView = this;

	private static PerunRegistrarTranslation translation = GWT.create(PerunRegistrarTranslation.class);

	private Vo vo;
	private Group group;
	private ExceptionResolver exceptionResolver;

	@UiField
	PerunForm form;

	@UiField
	Image logo;

	@UiField
	AlertErrorReporter notice;

	private PerunException displayedException;

	@Inject
	public FormView(FormViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
		exceptionResolver = new ExceptionResolverImpl();
		//draw(); - must be called from outside in order to be loaded only when necessary
	}

	public PerunForm getForm() {
		return form;
	}

	public PerunRegistrarTranslation getTranslation() {
		return translation;
	}


	public void draw() {

		final String voName = (Window.Location.getParameter("vo") != null) ? URL.decodeQueryString(Window.Location.getParameter("vo")) : null;
		final String groupName = (Window.Location.getParameter("group") != null) ? URL.decodeQueryString(Window.Location.getParameter("group")) : null;
		final PerunPrincipal pp = PerunSession.getInstance().getPerunPrincipal();

		if (voName == null || voName.isEmpty()) {
			this.displayedException = PerunException.createNew("0", "WrongURL", "Missing parameters in URL.");
			displayException(displayedException, null);
			return;
		}


		final PerunLoader loader = new PerunLoader();
		form.add(loader);

		RegistrarManager.initializeRegistrar(voName, groupName, new JsonEvents() {

			JsonEvents retry = this;

			@Override
			public void onFinished(JavaScriptObject jso) {

				final RegistrarObject registrar = jso.cast();

				// recreate VO and group
				vo = registrar.getVo();

				if (groupName != null && !groupName.isEmpty()) {
					group = registrar.getGroup();
				}

				ArrayList<Attribute> attrList = registrar.getVoAttributes();
				for (Attribute a : attrList) {
					if (a.getFriendlyName().equals("voLogoURL")) {
						logo.setUrl(a.getValue());
						logo.setVisible(true);
					}
				}



				if (registrar.getException() != null) {
					GWT.log("Exception " + registrar.getException().getMessage());
					if (registrar.getException().getName().equals("VoNotExistsException") ||
							registrar.getException().getName().equals("GroupNotExistsException") ||
							registrar.getException().getName().equals("FormNotExistsException")) {
						if (loader != null) {
							loader.onFinished();
							loader.removeFromParent();
						}
						displayException(registrar.getException(), (group != null ? group : vo));

						return;
					}

					// SEVERE EXCEPTION - DO NOT LOAD FORM
					loader.onError(registrar.getException(), new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							RegistrarManager.initializeRegistrar(voName, groupName, retry);
						}
					});

				} else {

					loader.onFinished();
					loader.removeFromParent();

					// CHECK SIMILAR USERS
					// Make sure we load form only after user decide to skip identity joining

					if (!registrar.getSimilarUsers().isEmpty()) {
						showSimilarUsersDialog(registrar.getSimilarUsers(), new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								loadSteps(pp, registrar);
							}
						});
					} else {
						loadSteps(pp, registrar);
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
				loader.onLoading(translation.preparingForm());
			}
		});

	}

	private void loadSteps(PerunPrincipal pp, RegistrarObject registrar) {

		Summary summary = new SummaryImpl(registrar);
		StepManager stepManager = new FormStepManager(pp, formView, summary);

		//////// This block of code should represent (guess) WHAT USER WANT to do. /////////
		if (isApplyingToGroup(registrar)) {

			if (voInitialFormExists(registrar)) {

				stepManager.addStep(new VoInitStep(registrar, form));
				stepManager.addStep(new GroupInitStep(registrar, form));
				stepManager.addStep(new SummaryStep(formView));
				stepManager.begin();

			} else if (voExtensionFormExists(registrar)) {

				stepManager.addStep(new GroupInitStep(registrar, form));
				for (ApplicationFormItemData item : registrar.getVoFormExtension()) {
					if (!item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.HTML_COMMENT) &&
							!item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.HEADING)) {
						// offer only when VO doesn't have empty or "You are registered" form.
						stepManager.addStep(new VoExtOfferStep(registrar, form)); // will offer only if form is valid
						break;
					}
				}
				stepManager.addStep(new SummaryStep(formView));
				stepManager.begin();

			} else {

				// Because vo initial form can be empty (admin did not create it).
				if (!isMemberOfVo(registrar) && !appliedToVo(registrar))
					stepManager.addStep(new VoInitStep(registrar, form));
				stepManager.addStep(new GroupInitStep(registrar, form));
				stepManager.addStep(new SummaryStep(formView));
				stepManager.begin();

			}

		} else {

			if (voInitialFormExists(registrar)) {

				stepManager.addStep(new VoInitStep(registrar, form));
				stepManager.addStep(new SummaryStep(formView));
				stepManager.begin();

			} else if (voExtensionFormExists(registrar)) {

				stepManager.addStep(new VoExtStep(registrar, form));
				stepManager.addStep(new SummaryStep(formView));
				stepManager.begin();

			} else {

				// However form does not exist user still want to do something here.
				if (isMemberOfVo(registrar)) {
					stepManager.addStep(new VoExtStep(registrar, form));
				} else {
					stepManager.addStep(new VoInitStep(registrar, form));
				}
				stepManager.addStep(new SummaryStep(formView));
				stepManager.begin();

			}

		}

	}

	private boolean voInitialFormExists(RegistrarObject ro) {
		return !ro.getVoFormInitial().isEmpty();
	}

	private boolean voExtensionFormExists(RegistrarObject ro) {
		return !ro.getVoFormExtension().isEmpty();
	}

	private boolean groupFormExists(RegistrarObject ro) {
		return !ro.getGroupFormInitial().isEmpty();
	}

	private boolean isMemberOfVo(RegistrarObject ro) {
		if (ro.getVoFormInitialException() == null) {
			return false;
		}
		if (ro.getVoFormInitialException().getName().equals("AlreadyRegisteredException")) {
			return true;
		} else {
			return false;
		}
	}
	private boolean appliedToVo(RegistrarObject ro) {
		if (ro.getVoFormInitialException() == null) {
			return false;
		}
		if (ro.getVoFormInitialException().getName().equals("DuplicateRegistrationAttemptException")) {
			return true;
		} else {
			return false;
		}
	}
	private boolean appliedForExtension(RegistrarObject ro) {
		if (ro.getVoFormExtensionException() == null) {
			return false;
		}
		if (ro.getVoFormExtensionException().getName().equals("DuplicateRegistrationAttemptException")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isApplyingToGroup(RegistrarObject ro) {
		return (group != null);
	}

	/**
	 * @param ex exception to be displayed.
	 * @param bean Perun bean related to exception (Vo or Group)
	 * @return return if exception is soft. see {@link ExceptionResolver} for more info.
	 */
	public boolean displayException(PerunException ex, GeneralObject bean) {
		Window.scrollTo(0, 0);
		exceptionResolver.resolve(ex, bean);
		notice.setHTML(exceptionResolver.getHTML());
		notice.setVisible(true);
		if (!exceptionResolver.isSoft()) {
			notice.setReportInfo(ex);
			notice.setType(AlertType.DANGER);
		} else {
			notice.setReportInfo(null);
			notice.setType(AlertType.WARNING);
		}
		return exceptionResolver.isSoft();
	}


	public void hideNotice() {
		notice.setVisible(false);
	}

	public static void showSimilarUsersDialog(List<Identity> similar, final ClickHandler handler) {

		final Modal modal = new Modal();
		modal.setTitle(similar.size() > 1 ? translation.similarUsersFoundTitle() : translation.similarUserFoundTitle());
		modal.setFade(true);
		modal.setDataKeyboard(false);
		modal.setDataBackdrop(ModalBackdrop.STATIC);
		modal.setClosable(false);
		modal.setWidth("750px");

		FlexTable ft = new FlexTable();
		ft.setWidth("100%");
		ft.setCellSpacing(5);

		int row = 0;
		for (Identity identity : similar) {

			ft.setHTML(row, 0, "<strong>"+identity.getName()+"</strong><br />"+
					((identity.getOrganization() != null) ? identity.getOrganization() : "<i style='color:grey'>N/A</i>") +"<br />"+identity.getEmail());
			ft.getFlexCellFormatter().setWidth(row, 0, "180px");

			ButtonGroup certGroup = new ButtonGroup();
			DropDownMenu menu = getCertificatesJoinButton(certGroup);

			ButtonGroup idpGroup = new ButtonGroup();
			DropDownMenu menu2 = getIdpJoinButton(idpGroup);

			ButtonGroup othersGroup = new ButtonGroup();
			DropDownMenu menu3 = getOthersJoinButton(othersGroup);

			boolean certFound = false;
			boolean idpFound = false;
			boolean othersFound = false;

			ArrayList<ExtSource> sources = identity.getExternalIdentities();
			Collections.sort(sources, new Comparator<ExtSource>() {
				@Override
				public int compare(ExtSource o1, ExtSource o2) {
					return Utils.convertCertCN(Utils.translateIdp(o1.getName())).compareTo(Utils.convertCertCN(Utils.translateIdp(o2.getName())));
				}
			});

			List<String> proxies = PerunConfiguration.getRegistrarEnforcedProxies();

			for (final ExtSource source : sources) {

				if (source.getType().equals(ExtSource.ExtSourceType.X509.getType())) {

					AnchorListItem link = new AnchorListItem(Utils.convertCertCN(source.getName()));
					menu.add(link);
					link.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							RegistrarManager.getConsolidatorToken(new JsonEvents() {
								@Override
								public void onFinished(JavaScriptObject jso) {
									String token = ((BasicOverlayObject) jso).getString();
									Window.Location.assign(Utils.getIdentityConsolidatorLink("cert", true) + "&token=" + token);
								}

								@Override
								public void onError(PerunException error) {

								}

								@Override
								public void onLoadingStart() {

								}
							});
						}
					});

					if (!certFound) ft.setWidget(row, 1, certGroup);
					certFound = true;

				} else if (source.getType().equals(ExtSource.ExtSourceType.IDP.getType())) {

					String entityId = source.getName();
					String originalIdP = source.getName();

					// IF Perun is behind proxy, offer only allowed proxies !!!
					if (proxies != null && !proxies.isEmpty() && proxies.contains(entityId)) {

					/*
					WE NO LONGER WANT TO SPECIFY source IdPs for ELIXIR, just reference the proxy itself
					if (source.getName().startsWith("https://login.elixir-czech.org/idp/")) {
						// FIXME - hacked for Elixir proxy IdP to work with original user IdP name
						entityId = source.getName().split("@")[0];
						originalIdP = "@" + source.getName().split("@")[1];
					}
					*/

						final String finalEntityId = entityId;

						AnchorListItem link = new AnchorListItem(Utils.translateIdp(originalIdP));
						menu2.add(link);
						link.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {

								RegistrarManager.getConsolidatorToken(new JsonEvents() {
									@Override
									public void onFinished(JavaScriptObject jso) {
										// FINAL URL must logout from SP, login to SP using specified IdP, redirect to IC and after that return to application form
										String token = ((BasicOverlayObject) jso).getString();
										String consolidatorUrl = Utils.getIdentityConsolidatorLink("fed", true) + URL.encodeQueryString("&token=" + token);
										String redirectUrl = PerunConfiguration.getWayfSpLogoutUrl() + "?return=" + PerunConfiguration.getWayfSpLoginUrl() + URL.encodeQueryString("?entityID=" + finalEntityId + "&target=" + consolidatorUrl);
										Window.Location.assign(redirectUrl);
									}

									@Override
									public void onError(PerunException error) {

									}

									@Override
									public void onLoadingStart() {

									}
								});
							}
						});
						if (!idpFound) ft.setWidget(row, 2, idpGroup);
						idpFound = true;

					}

				} else if (source.getType().equals(ExtSource.ExtSourceType.KERBEROS.getType())) {

					if (source.getName().equalsIgnoreCase("EINFRA") ||
							source.getName().equalsIgnoreCase("EINFRA-SERVICES") ||
							source.getName().equalsIgnoreCase("SAGRID") ||
							source.getName().equalsIgnoreCase("ICS.MUNI.CZ")) {

						String toBePrefix = "krb";
						if (source.getName().equalsIgnoreCase("EINFRA-SERVICES")) toBePrefix = "krbes";
						// others goes to /krb/ on their respective instances
						final String prefix = toBePrefix;

						AnchorListItem link = new AnchorListItem(Utils.translateKerberos(source.getName()));
						menu3.add(link);
						link.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {

								RegistrarManager.getConsolidatorToken(new JsonEvents() {
									@Override
									public void onFinished(JavaScriptObject jso) {
										String token = ((BasicOverlayObject) jso).getString();
										String consolidatorUrl = Utils.getIdentityConsolidatorLink(prefix, true) + "&token=" + token;
										Window.Location.replace(consolidatorUrl);
									}

									@Override
									public void onError(PerunException error) {

									}

									@Override
									public void onLoadingStart() {

									}
								});
							}
						});

						if (!othersFound) ft.setWidget(row, 3, othersGroup);
						othersFound = true;

					}

				}

			}

			if (sources.isEmpty() || (!idpFound && !certFound && !othersFound)) {

				// no identity for joining
				ft.setHTML(row, 1, translation.noIdentityForJoining(PerunConfiguration.getBrandSupportMail()));

			}

			row++;

		}

		ModalBody body = new ModalBody();
		body.add(new Paragraph(translation.similarUsersFound()));
		body.add(ft);
		modal.add(body);

		ModalFooter footer = new ModalFooter();

		final Button noThanks = new Button("...5...", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modal.hide();
				// trigger steps loading
				handler.onClick(event);
			}
		});
		noThanks.setType(ButtonType.DANGER);
		noThanks.setEnabled(false);

		Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
			int counter = 5;
			@Override
			public boolean execute() {
				if (counter <= 0) {
					noThanks.setText(translation.noThanks());
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


	private static DropDownMenu getCertificatesJoinButton(ButtonGroup certGroup) {

		Button certButt = new Button(translation.byCertificate(), IconType.CERTIFICATE, new ClickHandler() {
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

	private static DropDownMenu getIdpJoinButton(ButtonGroup certGroup) {

		Button idpButt = new Button(translation.byIdp(), IconType.USER, new ClickHandler() {
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

	private static DropDownMenu getOthersJoinButton(ButtonGroup othersButton) {

		Button othButt = new Button(translation.byLoginPassword(), IconType.KEY, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

			}
		});
		othButt.setPaddingLeft(10);
		othButt.setIconFixedWidth(true);
		othButt.setType(ButtonType.SUCCESS);
		othButt.setDataToggle(Toggle.DROPDOWN);
		othersButton.add(othButt);
		DropDownMenu idpMenu = new DropDownMenu();
		othersButton.add(idpMenu);

		return idpMenu;

	}

}
