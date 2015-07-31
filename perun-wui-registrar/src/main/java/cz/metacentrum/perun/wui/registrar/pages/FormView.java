package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.*;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;
import cz.metacentrum.perun.wui.registrar.widgets.PerunForm;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * View for displaying registration form of VO / Group
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class FormView extends ViewImpl implements FormPresenter.MyView {

	interface FormViewUiBinder extends UiBinder<Widget, FormView> {
	}

	private RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);

	private Vo vo;
	private Group group;

	@UiField
	PerunForm form;

	@UiField
	Image logo;

	@UiField
	Alert notice;

	private PerunException displayedException;

	@Inject
	public FormView(FormViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));
		draw();
	}

	public void draw() {

		final PerunLoader loader = new PerunLoader();
		form.add(loader);

		final String voName = Window.Location.getParameter("vo");
		final String groupName = Window.Location.getParameter("group");

		Scheduler.get().scheduleDeferred(new Command() {
			@Override
			public void execute() {
				loader.getWidget().getElement().getFirstChildElement().setAttribute("style", "height: "+ (Window.getClientHeight()-100)+"px;");
			}
		});

		if (voName == null || voName.isEmpty()) {
			this.displayedException = PerunException.createNew("0", "WrongURL", "Missing parameters in URL.");
			displayException(loader, displayedException);
			return;
		}

		final PerunPrincipal pp = PerunSession.getInstance().getPerunPrincipal();

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
						//PerunRegistrar.setLogo(a.getValue().replace("https://", "http://"));
						// FIXME - for testing remove https
						if (!Utils.isDevel()) {
							logo.setUrl(a.getValue().replace("https://", "http://"));
						}
						logo.setVisible(true);
					}
				}



				if (registrar.getException() != null) {
					GWT.log("Exception " + registrar.getException().getMessage());
					if (registrar.getException().getName().equals("VoNotExistsException") ||
							registrar.getException().getName().equals("GroupNotExistsException")) {
						displayException(loader, registrar.getException());
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

					if (isApplyingToGroup(registrar)) {

						if (groupInitialFormExists(registrar)) {

							if (voInitialFormExists(registrar)) {

								(new VoInit(new GroupInit(new Summary(Application.ApplicationType.INITIAL)))).call(pp, registrar);

							} else if (voExtensionFormExists(registrar)) {

								// TODO - VoExt vs GroupInit / targetext vs targetnew ?
								(new VoExtOffer(new VoExt(new GroupInit(new Summary(Application.ApplicationType.INITIAL))),
										new GroupInit(new Summary(Application.ApplicationType.INITIAL)))).call(pp, registrar);

							} else {

								(new GroupInit(new Summary(Application.ApplicationType.INITIAL))).call(pp, registrar);

							}

						} else {

							if (voInitialFormExists(registrar)) {

								(new VoInit(new Summary(Application.ApplicationType.INITIAL))).call(pp, registrar);

							} else if (voExtensionFormExists(registrar)) {

								(new VoExtOffer(new VoExt(new Summary(Application.ApplicationType.EXTENSION)),
										new Summary(null))).call(pp, registrar);

							} else {

								(new Summary(null)).call(pp, registrar);

							}

						}

					} else {

						if (voInitialFormExists(registrar)) {

							(new VoInit(new Summary(Application.ApplicationType.INITIAL))).call(pp, registrar);

						} else if (voExtensionFormExists(registrar)) {

							(new VoExt(new Summary(Application.ApplicationType.EXTENSION))).call(pp, registrar);

						} else {

							(new Summary(null)).call(pp, registrar);

						}

					}

					// CHECK SIMILAR USERS
					if (!registrar.getSimilarUsers().isEmpty()) {
						showSimilarUsersDialog(registrar);
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

	}



	private interface Step {
		void call(PerunPrincipal pp, RegistrarObject registrar);
	}
	private abstract class StepImpl implements Step {

		protected Step next;

		public StepImpl(Step next) {
			this.next = next;
		}
	}
	private abstract class StepAsk implements Step {

		protected Step yes;
		protected Step no;

		public StepAsk(Step yes, Step no) {
			this.yes = yes;
			this.no = no;
		}
	}


	private class VoInit extends StepImpl {

		public VoInit(Step next) {
			super(next);
		}

		@Override
		public void call(final PerunPrincipal pp, final RegistrarObject registrar) {

			form.clear();
			form.setFormItems(registrar.getVoFormInitial());
			form.setApp(Application.createNew(vo, null, Application.ApplicationType.INITIAL, getFedInfo(pp), pp.getActor(), pp.getExtSource(), pp.getExtSourceType(), pp.getExtSourceLoa()));
			form.setOnSubmitEvent(new JsonEvents() {

				@Override
				public void onFinished(JavaScriptObject jso) {
					next.call(pp, registrar);
				}

				@Override
				public void onError(PerunException error) {
					displayException(error);
				}

				@Override
				public void onLoadingStart() {

				}
			});
		}

	}



	private class VoExt extends StepImpl {

		public VoExt(Step next) {
			super(next);
		}

		@Override
		public void call(final PerunPrincipal pp, final RegistrarObject registrar) {

			form.clear();
			form.setFormItems(registrar.getVoFormExtension());
			form.setApp(Application.createNew(vo, null, Application.ApplicationType.EXTENSION, getFedInfo(pp), pp.getActor(), pp.getExtSource(), pp.getExtSourceType(), pp.getExtSourceLoa()));
			form.setOnSubmitEvent(new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject jso) {
					next.call(pp, registrar);
				}

				@Override
				public void onError(PerunException error) {
					displayException(error);
				}

				@Override
				public void onLoadingStart() {

				}
			});
		}
	}



	private class GroupInit extends StepImpl {

		public GroupInit(Step next) {
			super(next);
		}

		@Override
		public void call(final PerunPrincipal pp, final RegistrarObject registrar) {

			form.clear();
			form.setFormItems(registrar.getGroupFormInitial());
			form.setApp(Application.createNew(vo, group, Application.ApplicationType.INITIAL, getFedInfo(pp), pp.getActor(), pp.getExtSource(), pp.getExtSourceType(), pp.getExtSourceLoa()));
			form.setOnSubmitEvent(new JsonEvents() {

				@Override
				public void onFinished(JavaScriptObject jso) {
					next.call(pp, registrar);
				}

				@Override
				public void onError(PerunException error) {
					displayException(error);
				}

				@Override
				public void onLoadingStart() {

				}
			});
		}
	}



	private class VoExtOffer extends StepAsk {

		public VoExtOffer(Step yes, Step no) {
			super(yes, no);
		}

		@Override
		public void call(final PerunPrincipal pp, final RegistrarObject registrar) {

			final Modal modal = new Modal();
			modal.setTitle("Vo Extention offer");
			modal.setFade(true);
			modal.setDataKeyboard(false);
			modal.setDataBackdrop(ModalBackdrop.STATIC);
			modal.setClosable(false);
			modal.setWidth("700px");

			ModalBody body = new ModalBody();
			body.add(new Paragraph("BODY text"));


			ModalFooter footer = new ModalFooter();

			final Button noThanks = new Button("No thanks", new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					modal.hide();
					no.call(pp, registrar);
				}
			});
			noThanks.setType(ButtonType.DEFAULT);

			final Button extend = new Button("Extend", new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					modal.hide();
					yes.call(pp, registrar);
				}
			});
			extend.setType(ButtonType.SUCCESS);
			footer.add(extend);
			footer.add(noThanks);

			modal.add(body);
			modal.add(footer);
			modal.show();


		}
	}



	private class Summary implements Step {

		private final Application.ApplicationType redirect;

		public Summary(Application.ApplicationType redirect) {
			this.redirect = redirect;
		}

		@Override
		public void call(final PerunPrincipal pp, final RegistrarObject registrar) {
			form.clear();
			displaySuccess(redirect);
			displaySoftExceptions(registrar);
			displayContinueButton(registrar, redirect);
		}


	}




	protected String getFedInfo(PerunPrincipal pp) {
		return "{" + " displayName=\"" + pp.getAdditionInformation("displayName")+"\"" + " commonName=\"" + pp.getAdditionInformation("cn")+"\""
				+ " givenName=\"" + pp.getAdditionInformation("givenName")+"\"" + " sureName=\"" + pp.getAdditionInformation("sn")+"\""
				+ " loa=\"" + pp.getAdditionInformation("loa")+"\"" + " mail=\"" + pp.getAdditionInformation("mail")+"\""
				+ " organization=\"" + pp.getAdditionInformation("o")+"\"" + " }";
	}

	private boolean voInitialFormExists(RegistrarObject ro) {
		return !ro.getVoFormInitial().isEmpty();
	}
	private boolean voExtensionFormExists(RegistrarObject ro) {
		return !ro.getVoFormExtension().isEmpty();
	}
	private boolean groupInitialFormExists(RegistrarObject ro) {
		return !ro.getGroupFormInitial().isEmpty();
	}
	private boolean isApplyingToGroup(RegistrarObject ro) {
		return (group != null);
	}


	private void displaySuccess(Application.ApplicationType redirect) {
		Heading title = new Heading(HeadingSize.H2);
		if (redirect == null) {
			title.setText("We are sorry but we can do nothing for you now.");
		} else {
			switch (redirect) {
				case INITIAL:
					title.setText("You have successfully applied for membership in VO " + vo.getName());
					break;
				case EXTENSION:
					title.setText("You have successfully applied for extension of membership in VO " + vo.getName());
					break;
			}
		}
		form.add(title);
	}
	private void displaySoftExceptions(RegistrarObject registrar) {

		if (registrar.getVoFormInitialException() != null) {
			Alert voInitEx = new Alert();
			voInitEx.setType(AlertType.INFO);
			switch (registrar.getVoFormInitialException().getName()) {
				case "DuplicateRegistrationAttemptException":
					voInitEx.setText("You have already applied for membership in VO " + vo.getName() + ". Please check your email or contact VO admin." );
					break;
				case "AlreadyRegisteredException":
					voInitEx.setText("You are already registered in VO " + vo.getName() + ".");
					break;
				default:
					voInitEx.setText(registrar.getVoFormInitialException().getMessage());
			}
			form.add(voInitEx);
		}


		if (registrar.getVoFormExtensionException() != null) {
			Alert voExtEx = new Alert();
			voExtEx.setType(AlertType.INFO);
			switch (registrar.getVoFormExtensionException().getName()) {
				case "DuplicateRegistrationAttemptException":
					voExtEx.setText("You have already applied for extension of membership in VO " + vo.getName() + ". Please check your email or contact VO admin." );
					form.add(voExtEx);
					break;
				case "MemberNotExistsException":
					break;
				default:
					voExtEx.setText(registrar.getVoFormExtensionException().getMessage());
					form.add(voExtEx);
			}
		}


		if (registrar.getGroupFormInitialException() != null) {
			Alert groupInitEx = new Alert();
			groupInitEx.setType(AlertType.INFO);
			switch (registrar.getGroupFormInitialException().getName()) {
				case "DuplicateRegistrationAttemptException":
					groupInitEx.setText("You have already applied for membership in group " + group.getName() + ". Please check your email or contact Group admin." );
					break;
				case "AlreadyRegisteredException":
					groupInitEx.setText("You are already member of group " + group.getName() + ".");
					break;
				default:
					groupInitEx.setText(registrar.getGroupFormInitialException().getMessage());
			}
			form.add(groupInitEx);
		}


	}
	private void displayContinueButton(RegistrarObject registrar, Application.ApplicationType redirect) {

		PerunButton cont;
		if (redirect == null) {

			if (Window.Location.getParameter("targetexisting") != null) {
				if (isApplyingToGroup(registrar)) {
					PerunException pEx = registrar.getGroupFormInitialException();
					if (pEx != null) {
						if (pEx.getName().equals("DuplicateRegistrationAttemptsException")
								|| pEx.getName().equals("AlreadyRegisteredException")) {
							cont = PerunButton.getButton(PerunButtonType.CONTINUE, new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									Window.Location.assign(Window.Location.getParameter("targetexisting"));
								}
							});
							form.add(cont);
						}
					}
				} else {
					PerunException pEx = registrar.getVoFormInitialException();
					if (pEx != null) {
						if (pEx.getName().equals("DuplicateRegistrationAttemptsException")
								|| pEx.getName().equals("AlreadyRegisteredException")) {
							cont = PerunButton.getButton(PerunButtonType.CONTINUE, new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									Window.Location.assign(Window.Location.getParameter("targetexisting"));
								}
							});
							form.add(cont);
						}
					}
				}
			}

		} else {
			switch (redirect) {
				case INITIAL:

					if (Window.Location.getParameter("targetnew") != null) {
						cont = PerunButton.getButton(PerunButtonType.CONTINUE, new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								Window.Location.assign(Window.Location.getParameter("targetnew"));
							}
						});
						form.add(cont);
					}

					break;
				case EXTENSION:

					if (Window.Location.getParameter("targetextended") != null) {
						cont = PerunButton.getButton(PerunButtonType.CONTINUE, new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								Window.Location.assign(Window.Location.getParameter("targetextended"));
							}
						});
						form.add(cont);
					}

					break;
			}
		}

	}

	private void displayException(PerunException ex) {
		displayException(null, ex);
	}
	private void displayException(PerunLoader loader, PerunException ex) {

		this.displayedException = ex;
		notice.setType(AlertType.WARNING);

		Button continueButton = new Button(translation.continueButton());
		continueButton.setIcon(IconType.CHEVRON_RIGHT);
		continueButton.setType(ButtonType.WARNING);
		continueButton.setIconPosition(IconPosition.RIGHT);
		continueButton.setMarginTop(20);

		if (ex.getName().equals("ExtendMembershipException")) {

			if (ex.getReason().equals("OUTSIDEEXTENSIONPERIOD")) {

				String exceptionText = "<i>unlimited</i>";
				if (ex.getExpirationDate() != null) exceptionText = ex.getExpirationDate().split(" ")[0];
				notice.getElement().setInnerHTML("<h4>"+translation.cantExtendMembership()+"</h4><p>"+translation.cantExtendMembershipOutside(exceptionText));

				if (Window.Location.getParameter("targetexisting") != null) {

					continueButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							Window.Location.replace(Window.Location.getParameter("targetexisting"));
						}
					});
					notice.add(continueButton);

				}

			} else if (ex.getReason().equals("NOUSERLOA")) {

				notice.getElement().setInnerHTML("<h4>"+translation.cantBecomeMember((group != null) ? group.getShortName() : vo.getName())+"</h4><p>"+translation.cantBecomeMemberLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

			} else if (ex.getReason().equals("INSUFFICIENTLOA")) {

				notice.getElement().setInnerHTML("<h4>"+translation.cantBecomeMember((group != null) ? group.getShortName() : vo.getName())+"</h4><p>"+translation.cantBecomeMemberInsufficientLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

			} else if (ex.getReason().equals("INSUFFICIENTLOAFOREXTENSION")) {

				notice.getElement().setInnerHTML("<h4>"+translation.cantExtendMembership()+"</h4><p>"+translation.cantExtendMembershipInsufficientLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

			}

		} else if (ex.getName().equals("AlreadyRegisteredException")) {

			notice.getElement().setInnerHTML("<h4>"+translation.alreadyRegistered((group != null) ? group.getShortName() : vo.getName())+"</h4>");

			if (Window.Location.getParameter("targetexisting") != null) {

				continueButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Window.Location.replace(Window.Location.getParameter("targetexisting"));
					}
				});
				notice.add(continueButton);

			}

		} else if (ex.getName().equals("DuplicateRegistrationAttemptException")) {

			notice.getElement().setInnerHTML("<h4>"+ translation.alreadySubmitted(((group != null) ? group.getShortName() : vo.getName())) + "</h4><p>"+translation.visitSubmitted(Window.Location.getHref().split("#")[0], translation.submittedTitle()));

			if (Window.Location.getParameter("targetnew") != null) {

				continueButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Window.Location.replace(Window.Location.getParameter("targetnew"));
					}
				});
				notice.add(continueButton);

			}

		} else if (ex.getName().equalsIgnoreCase("MissingRequiredDataException")) {

			String missingItems = "<p><ul>";
			if (!ex.getFormItems().isEmpty()) {
				for (ApplicationFormItemData item : ex.getFormItems()) {
					missingItems += "<li>" + translation.missingAttribute(item.getFormItem().getFederationAttribute());
				}
			}
			missingItems += "<ul/>";

			notice.getElement().setInnerHTML(translation.missingRequiredData(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())) + missingItems);

		} else if (ex.getName().equalsIgnoreCase("VoNotExistsException")) {

			notice.getElement().setInnerHTML("<h4>"+translation.voNotExistsException(Window.Location.getParameter("vo"))+"</h4>");

		} else if (ex.getName().equalsIgnoreCase("GroupNotExistsException")) {

			notice.getElement().setInnerHTML("<h4>"+translation.groupNotExistsException(Window.Location.getParameter("group"))+"</h4>");

		} else if (ex.getName().equalsIgnoreCase("WrongURL")) {

			notice.getElement().setInnerHTML("<h4>"+translation.missingVoInURL()+"</h4>");

		} else {

			notice.getElement().setInnerHTML("<h4>Internal error</h4>");

		}
		// TODO - FormNotExistException

		notice.setVisible(true);
		if (loader != null) {
			loader.onFinished();
			loader.removeFromParent();
		}

	}

	private void showSimilarUsersDialog(RegistrarObject object) {

		final Modal modal = new Modal();
		modal.setTitle(object.getSimilarUsers().size() > 1 ? translation.similarUsersFoundTitle() : translation.similarUserFoundTitle());
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

			ArrayList<ExtSource> sources = identity.getExternalIdentities();
			Collections.sort(sources, new Comparator<ExtSource>() {
				@Override
				public int compare(ExtSource o1, ExtSource o2) {
					return Utils.convertCertCN(Utils.translateIdp(o1.getName())).compareTo(Utils.convertCertCN(Utils.translateIdp(o2.getName())));
				}
			});

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
									Window.Location.replace(Utils.getIdentityConsolidatorLink("cert", true) + "&token=" + token);
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

					AnchorListItem link = new AnchorListItem(Utils.translateIdp(source.getName()));
					menu2.add(link);
					link.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {

							RegistrarManager.getConsolidatorToken(new JsonEvents() {
								@Override
								public void onFinished(JavaScriptObject jso) {
									// FINAL URL must logout from SP, login to SP using specified IdP, redirect to IC and after that return to application form
									String token = ((BasicOverlayObject) jso).getString();
									String consolidatorUrl = Utils.getIdentityConsolidatorLink("fed", true)+URL.encodeQueryString("&token="+token);
									String redirectUrl = Utils.getWayfSpLogoutUrl() + "?return=" + Utils.getWayfSpDsUrl() + URL.encodeQueryString("?entityID=" + source.getName()+"&target="+consolidatorUrl);
									Window.Location.replace(redirectUrl);
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

	private DropDownMenu getCertificatesJoinButton(ButtonGroup certGroup) {

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

	private DropDownMenu getIdpJoinButton(ButtonGroup certGroup) {

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

}