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
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static cz.metacentrum.perun.wui.model.beans.Application.ApplicationType;

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
						logo.setUrl(a.getValue());
						logo.setVisible(true);
					}
				}



				if (registrar.getException() != null) {
					GWT.log("Exception " + registrar.getException().getMessage());
					if (registrar.getException().getName().equals("VoNotExistsException") ||
							registrar.getException().getName().equals("GroupNotExistsException") ||
							registrar.getException().getName().equals("FormNotExistsException")) {
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

						if (groupFormExists(registrar)) {

							if (voInitialFormExists(registrar)) {

								(new VoInit(new GroupInit(new Summary(ApplicationType.INITIAL, ApplicationType.INITIAL)))).call(pp, registrar);

							} else if (voExtensionFormExists(registrar)) {

								(new VoExtOffer(new VoExt(new GroupInit(new Summary(ApplicationType.EXTENSION, ApplicationType.INITIAL))),
										new GroupInit(new Summary(null, ApplicationType.INITIAL)))).call(pp, registrar);

							} else {

								(new GroupInit(new Summary(null, ApplicationType.INITIAL))).call(pp, registrar);

							}

						} else {

							if (voInitialFormExists(registrar)) {

								(new VoInit(new Summary(ApplicationType.INITIAL, null))).call(pp, registrar);

							} else if (voExtensionFormExists(registrar)) {

								(new VoExtOffer(new VoExt(new Summary(ApplicationType.EXTENSION, null)),
										new Summary(null, null))).call(pp, registrar);

							} else {

								(new Summary(null, null)).call(pp, registrar);

							}

						}

					} else {

						if (voInitialFormExists(registrar)) {

							(new VoInit(new Summary(ApplicationType.INITIAL, null))).call(pp, registrar);

						} else if (voExtensionFormExists(registrar)) {

							(new VoExt(new Summary(ApplicationType.EXTENSION, null))).call(pp, registrar);

						} else {

							(new Summary(null, null)).call(pp, registrar);

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


	private class VoInit extends StepImpl {

		public VoInit(Step next) {
			super(next);
		}

		@Override
		public void call(final PerunPrincipal pp, final RegistrarObject registrar) {

			form.clear();
			form.setFormItems(registrar.getVoFormInitial());
			form.setApp(Application.createNew(vo, null, ApplicationType.INITIAL, getFedInfo(pp), pp.getActor(), pp.getExtSource(), pp.getExtSourceType(), pp.getExtSourceLoa()));
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
			form.setApp(Application.createNew(vo, null, ApplicationType.EXTENSION, getFedInfo(pp), pp.getActor(), pp.getExtSource(), pp.getExtSourceType(), pp.getExtSourceLoa()));
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
			form.setApp(Application.createNew(vo, group, ApplicationType.INITIAL, getFedInfo(pp), pp.getActor(), pp.getExtSource(), pp.getExtSourceType(), pp.getExtSourceLoa()));
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



	private class VoExtOffer implements Step {
		protected Step yes;
		protected Step no;

		public VoExtOffer(Step yes, Step no) {
			this.yes = yes;
			this.no = no;
		}

		@Override
		public void call(final PerunPrincipal pp, final RegistrarObject registrar) {

			final Modal modal = new Modal();
			modal.setTitle(translation.offerMembershipExtensionTitle());
			modal.setFade(true);
			modal.setDataKeyboard(false);
			modal.setDataBackdrop(ModalBackdrop.STATIC);
			modal.setClosable(false);

			ModalBody body = new ModalBody();
			body.add(new Paragraph(translation.offerMembershipExtensionMessage(vo.getName())));


			ModalFooter footer = new ModalFooter();

			final Button noThanks = new Button(translation.offerMembershipExtensionNoThanks(), new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					modal.hide();
					no.call(pp, registrar);
				}
			});
			noThanks.setType(ButtonType.DEFAULT);

			final Button extend = new Button(translation.offerMembershipExtensionExtend(), new ClickHandler() {
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

		private final ApplicationType voApplication;
		private final ApplicationType groupApplication;

		public Summary(ApplicationType voApplication, ApplicationType groupApplication) {
			this.voApplication = voApplication;
			this.groupApplication = groupApplication;
		}

		@Override
		public void call(final PerunPrincipal pp, final RegistrarObject registrar) {
			form.clear();
			displaySummaryTitle(registrar, voApplication, groupApplication);
			displaySummaryMessage(registrar, voApplication, groupApplication);
			displayContinueButton(registrar, (groupApplication != null) ? groupApplication : voApplication);
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
	private boolean groupFormExists(RegistrarObject ro) {
		return !ro.getGroupFormInitial().isEmpty();
	}
	private boolean isApplyingToGroup(RegistrarObject ro) {
		return (group != null);
	}

	private void displaySummaryTitle(RegistrarObject registrar, ApplicationType voApp, ApplicationType groupApp) {
		Heading title = new Heading(HeadingSize.H2);
		Text text = new Text();

		if (voApp == null && groupApp == null) {

			text.setText(translation.canDoNothing());

		} else {
			Icon success = new Icon(IconType.CHECK_CIRCLE);
			success.setColor("#5cb85c");
			title.add(success);
			if (groupApp == ApplicationType.INITIAL) {

				if (registrar.hasGroupFormAutoApproval()) {
					text.setText(" "+translation.initTitleAutoApproval());
				} else {
					text.setText(" "+translation.initTitle());
				}

			} else if (voApp == ApplicationType.EXTENSION) {

				if (registrar.hasVoFormAutoApprovalExtension()) {
					text.setText(" "+translation.extendTitleAutoApproval());
				} else {
					text.setText(" "+translation.extendTitle());
				}

			} else if (voApp == ApplicationType.INITIAL) {

				if (registrar.hasVoFormAutoApproval()) {
					text.setText(" "+translation.initTitleAutoApproval());
				} else {
					text.setText(" "+translation.initTitle());
				}

			}
		}
		title.add(text);
		form.add(title);
	}
	private void displaySummaryMessage(RegistrarObject registrar, ApplicationType voApp, ApplicationType groupApp) {
		ListGroup message = new ListGroup();

		if (voApp == null && groupApp == null) {

			if (registrar.getVoFormInitialException() != null) {
				ListGroupItem voStat = new ListGroupItem();
				switch (registrar.getVoFormInitialException().getName()) {
					case "DuplicateRegistrationAttemptException":
						voStat.setText(translation.alreadySubmitted(vo.getName()));
						break;
					case "AlreadyRegisteredException":
						voStat.setText(translation.alreadyRegistered(vo.getName()));
						break;
					default:
						voStat.setText(registrar.getVoFormInitialException().getMessage());
				}
				message.add(voStat);
			}
			if (registrar.getVoFormExtensionException() != null) {
				ListGroupItem voStatExt = new ListGroupItem();
				switch (registrar.getVoFormExtensionException().getName()) {
					case "DuplicateRegistrationAttemptException":
						voStatExt.setText(translation.alreadySubmittedExtension(vo.getName()));
						message.add(voStatExt);
						break;
					case "MemberNotExistsException":
						break;
					default:
						voStatExt.setText(registrar.getVoFormExtensionException().getMessage());
						message.add(voStatExt);
				}
			}
			if (registrar.getGroupFormInitialException() != null) {
				ListGroupItem groupStat = new ListGroupItem();
				switch (registrar.getGroupFormInitialException().getName()) {
					case "DuplicateRegistrationAttemptException":
						groupStat.setText(translation.alreadySubmitted(group.getName()));
						break;
					case "AlreadyRegisteredException":
						groupStat.setText(translation.alreadyRegistered(group.getName()));
						break;
					default:
						groupStat.setText(registrar.getGroupFormInitialException().getMessage());
				}
				message.add(groupStat);
			}

		} else {
			ListGroupItem groupStat = new ListGroupItem();
			if (groupApp == ApplicationType.INITIAL) {

				if (registrar.hasGroupFormAutoApproval()) {
					groupStat.setText(translation.registered(group.getName()));
				} else {
					groupStat.setText(translation.waitForAcceptation(group.getName()));
				}
				message.add(groupStat);
			}
			ListGroupItem voStat = new ListGroupItem();
			if (voApp == ApplicationType.EXTENSION) {

				if (registrar.hasVoFormAutoApprovalExtension()) {
					voStat.setText(translation.extended(vo.getName()));
				} else {
					voStat.setText(translation.waitForExtAcceptation(vo.getName()));
				}
				message.add(voStat);

			} else if (voApp == ApplicationType.INITIAL) {

				if (registrar.hasVoFormAutoApproval()) {
					voStat.setText(translation.registered(vo.getName()));
				} else {
					voStat.setText(translation.waitForAcceptation(vo.getName()));
				}
				message.add(voStat);

			}
			if (groupApp == null) {

				if (registrar.getGroupFormInitialException() != null) {
					switch (registrar.getGroupFormInitialException().getName()) {
						case "DuplicateRegistrationAttemptException":
							groupStat.setText(translation.groupFailedAlreadyApplied(group.getName()));
							break;
						case "AlreadyRegisteredException":
							groupStat.setText(translation.groupFailedAlreadyRegistered(group.getName()));
							break;
						default:
							groupStat.setText(registrar.getGroupFormInitialException().getMessage());
					}
					message.add(groupStat);
				}
			}
			ListGroupItem verifyMail = new ListGroupItem();
			verifyMail.add(new Icon(IconType.WARNING));
			verifyMail.add(new Text(" " + translation.verifyMail()));
			message.add(verifyMail);
		}
		form.add(message);










	}
	private void displayContinueButton(RegistrarObject registrar, ApplicationType redirect) {

		PerunButton cont;
		if (redirect == null) {

			if (Window.Location.getParameter("targetexisting") != null) {
				if (isApplyingToGroup(registrar)) {
					PerunException pEx = registrar.getGroupFormInitialException();
					if (pEx != null) {
						if (pEx.getName().equals("DuplicateRegistrationAttemptException")
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
						if (pEx.getName().equals("DuplicateRegistrationAttemptException")
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

		} else if (ex.getName().equalsIgnoreCase("FormNotExistsException")) {

			notice.getElement().setInnerHTML("<h4>"+translation.formNotExist()+"</h4>");

		} else if (ex.getName().equalsIgnoreCase("ApplicationNotCreatedException")) {

			notice.getElement().setInnerHTML("<h4>"+translation.applicationNotCreated()+"</h4>");

		} else  if (ex.getName().equalsIgnoreCase("RegistrarException")) {

			notice.getElement().setInnerHTML("<h4>"+translation.registrarException()+"</h4>");

		} else {

			notice.getElement().setInnerHTML("<h4>"+translation.registrarException()+"</h4>");

		}

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