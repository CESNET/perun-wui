package cz.metacentrum.perun.wui.registrar.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.ErrorTranslator;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.MembersManager;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItem;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.ExtSource;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.Identity;
import cz.metacentrum.perun.wui.model.beans.Member;
import cz.metacentrum.perun.wui.model.beans.RichMember;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.client.ExceptionResolver;
import cz.metacentrum.perun.wui.registrar.client.ExceptionResolverImpl;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;
import cz.metacentrum.perun.wui.registrar.pages.steps.FormStepManager;
import cz.metacentrum.perun.wui.registrar.pages.steps.GroupExtStep;
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
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Alert;
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
import org.gwtbootstrap3.extras.notify.client.constants.NotifyType;
import org.gwtbootstrap3.extras.notify.client.ui.Notify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

	private static final String EXPIRATION_ATTRIBUTE_URN = "urn:perun:member:attribute-def:def:membershipExpiration";

	private Vo vo;
	private Group group;
	private ExceptionResolver exceptionResolver;

	private boolean neverExp = false;

	@UiField
	PerunForm form;

	@UiField
	Image logo;

	@UiField
	AlertErrorReporter notice;

	@UiField
	Alert mailVerificationAlert;

	@UiField
	Paragraph mailVerificationText;

	@UiField
	PerunButton resendNotification;

	@UiField
	AlertErrorReporter mailVerificationNotice;

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

	@UiHandler(value = "resendNotification")
	public void reSendMailVerificationNotification(ClickEvent event) {

		RegistrarManager.sendMessage(exceptionResolver.getApplication().getId(), "MAIL_VALIDATION", null, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				resendNotification.setProcessing(false);
				Notify.notify(translation.mailVerificationRequestSent(exceptionResolver.getMailToVerify()), NotifyType.SUCCESS);
			}

			@Override
			public void onError(PerunException error) {
				if ("ApplicationNotNewException".equalsIgnoreCase(error.getName())) {
					mailVerificationAlert.setVisible(false);
					resendNotification.setVisible(false);
					mailVerificationNotice.setType(AlertType.SUCCESS);
					mailVerificationNotice.setVisible(true);
					mailVerificationNotice.setReportInfo(null);
					mailVerificationNotice.setHTML(translation.resendMailAlreadyApproved(Application.translateState(error.getState())));


				} else {
					mailVerificationNotice.setType(AlertType.DANGER);
					mailVerificationNotice.setVisible(true);
					mailVerificationNotice.setHTML(ErrorTranslator.getTranslatedMessage(error));
					mailVerificationNotice.setReportInfo(error);
					resendNotification.setProcessing(false);
				}
			}

			@Override
			public void onLoadingStart() {
				mailVerificationNotice.setVisible(false);
				resendNotification.setProcessing(true);
			}
		});

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

		// hide previous state
		hideMailVerificationAlert();
		hideNotice();
		form.clear();

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
						if (a.getValue() != null) {
							logo.setUrl(a.getValue());
							logo.setVisible(true);
						}
					}
				}

				if (registrar.getException() != null) {
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

					// if there is a known user
					if (PerunSession.getInstance().getUserId() > 0) {

						// get member and his attributes to decide whether he is valid with no expiration date to know whether to display extension dialog or not
						MembersManager.getMemberByUser(PerunSession.getInstance().getUserId(), vo.getId(), new JsonEvents() {
							@Override
							public void onFinished(JavaScriptObject result) {
								Member member = (Member) result;
								MembersManager.getRichMemberWithAttributes(member.getId(), new JsonEvents() {
									@Override
									public void onFinished(JavaScriptObject result) {
										RichMember richMember = (RichMember) result;
										if (Objects.equals(richMember.getMembershipStatus(), "VALID") &&
												richMember.getAttribute(EXPIRATION_ATTRIBUTE_URN) == null) {
											neverExp = true;
										}
										loader.onFinished();
										loader.removeFromParent();

										// CHECK SIMILAR USERS
										// Make sure we load form only after user decide to skip identity joining

										if (!registrar.getSimilarUsers().isEmpty() &&
												!isApplicationPending(registrar) &&
												!PerunConfiguration.findSimilarUsersDisabled()) {
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

									@Override
									public void onError(PerunException error) {
										loader.onFinished();
										loader.removeFromParent();

										// CHECK SIMILAR USERS
										// Make sure we load form only after user decide to skip identity joining

										if (!registrar.getSimilarUsers().isEmpty() &&
												!isApplicationPending(registrar) &&
												!PerunConfiguration.findSimilarUsersDisabled()) {
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

									@Override
									public void onLoadingStart() {

									}
								});
							}

							@Override
							public void onError(PerunException error) {
								loader.onFinished();
								loader.removeFromParent();

								// CHECK SIMILAR USERS
								// Make sure we load form only after user decide to skip identity joining

								if (!registrar.getSimilarUsers().isEmpty() &&
										!isApplicationPending(registrar) &&
										!PerunConfiguration.findSimilarUsersDisabled()) {
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

							@Override
							public void onLoadingStart() {

							}
						});

					} else {

						// same as onError() of getMemberByUser
						loader.onFinished();
						loader.removeFromParent();

						// CHECK SIMILAR USERS
						// Make sure we load form only after user decide to skip identity joining

						if (!registrar.getSimilarUsers().isEmpty() &&
								!isApplicationPending(registrar) &&
								!PerunConfiguration.findSimilarUsersDisabled()) {
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

			boolean isApplicationPending(RegistrarObject registrar) {
				if (isPending(registrar.getVoFormInitialException())) return true;
				if (isPending(registrar.getGroupFormInitialException())) return true;
				if (isPending(registrar.getVoFormExtensionException())) return true;
				if (isPending(registrar.getGroupFormExtensionException())) return true;
				return false;
			}

			boolean isPending(PerunException ex) {
				return (ex != null && "DuplicateRegistrationAttemptException".equals(ex.getName()));
			}

		});

	}

	private void loadSteps(PerunPrincipal pp, RegistrarObject registrar) {

		Summary summary = new SummaryImpl(registrar);
		StepManager stepManager = new FormStepManager(pp, formView, summary);

		//////// This block of code should represent (guess) WHAT USER WANT to do. /////////
		if (isApplyingToGroup(registrar)) {

			if (voInitialFormExists(registrar)) {

				// vo initial can't have group extension
				stepManager.addStep(new VoInitStep(registrar, form));
				stepManager.addStep(new GroupInitStep(registrar, form));
				stepManager.addStep(new SummaryStep(formView));
				stepManager.begin();

			} else if (voExtensionFormExists(registrar)) {

				if (isMemberOfGroup(registrar) && groupExtensionFormExists(registrar)) {
					if (!neverExp) {
						for (ApplicationFormItemData item : registrar.getVoFormExtension()) {
							if (!item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.HTML_COMMENT) &&
								!item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.HEADING)) {
								// offer only when VO doesn't have empty or "You are registered" form.
								stepManager.addStep(new VoExtOfferStep(registrar, form)); // will offer only if form is valid
								break;
							}
						}
					}

					// only members with correct extension form can extend
					stepManager.addStep(new GroupExtStep(registrar, form));

				} else {

					stepManager.addStep(new GroupInitStep(registrar, form));
					if (!neverExp) {
						for (ApplicationFormItemData item : registrar.getVoFormExtension()) {
							if (!item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.HTML_COMMENT) &&
								!item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.HEADING)) {
								// offer only when VO doesn't have empty or "You are registered" form.
								stepManager.addStep(new VoExtOfferStep(registrar, form)); // will offer only if form is valid
								break;
							}
						}
					}
				}

				stepManager.addStep(new SummaryStep(formView));
				stepManager.begin();

			} else {

				// Because vo initial form can be empty (admin did not create it).
				if (!isMemberOfVo(registrar) && !appliedToVo(registrar))
					stepManager.addStep(new VoInitStep(registrar, form));

				if (isMemberOfGroup(registrar) && groupExtensionFormExists(registrar)) {
					// only members with correct extension form can extend
					stepManager.addStep(new GroupExtStep(registrar, form));
				} else {
					stepManager.addStep(new GroupInitStep(registrar, form));
				}
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

	private boolean groupExtensionFormExists(RegistrarObject ro) {
		return !ro.getGroupFormExtension().isEmpty();
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

	private boolean isMemberOfGroup(RegistrarObject ro) {
		if (ro.getGroupFormInitialException() == null) {
			return false;
		}
		if (ro.getGroupFormInitialException().getName().equals("AlreadyRegisteredException")) {
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

		if (exceptionResolver.isShowMailVerificationReSendButton()) {
			mailVerificationAlert.setVisible(true);
			mailVerificationText.setHTML(translation.mailVerificationText(exceptionResolver.getMailToVerify()));
			resendNotification.setText(translation.reSendMailVerificationButton());
		}

		return exceptionResolver.isSoft();
	}

	public void hideMailVerificationAlert() {
		mailVerificationAlert.setVisible(false);
		mailVerificationNotice.setVisible(false);
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
		ft.addStyleName("table");
		ft.addStyleName("table-striped"); // bootstrap3 style

		int row = 0;
		for (Identity identity : similar) {

			String name = SafeHtmlUtils.fromString((identity.getName()!=null) ? identity.getName() : "").asString();
			String email = SafeHtmlUtils.fromString((identity.getEmail()!=null) ? identity.getEmail() : "").asString();
			ft.setHTML(row, 0, "<strong>"+ name +"</strong><br />"+
					((identity.getOrganization() != null) ? SafeHtmlUtils.fromString(identity.getOrganization()).asString() : "<i style='color:grey'>N/A</i>") +"<br />"+email);
			ft.getFlexCellFormatter().setWidth(row, 0, "180px");

			ButtonGroup certGroup = new ButtonGroup();
			DropDownMenu menu = getCertificatesJoinButton(certGroup);

			ButtonGroup idpGroup = new ButtonGroup();
			DropDownMenu menu2 = getIdpJoinButton(idpGroup);

			ButtonGroup othersGroup = new ButtonGroup();
			DropDownMenu menu3 = getOthersJoinButton(othersGroup);

			HashMap<AnchorListItem,ClickHandler> certItems = new HashMap<>();
			HashMap<AnchorListItem,ClickHandler> idpItems = new HashMap<>();
			HashMap<AnchorListItem,ClickHandler> othersItems = new HashMap<>();

			ArrayList<ExtSource> sources = identity.getExternalIdentities();
			Collections.sort(sources, new Comparator<ExtSource>() {
				@Override
				public int compare(ExtSource o1, ExtSource o2) {
					return Utils.convertCertCN(Utils.translateIdp(o1.getName())).compareTo(Utils.convertCertCN(Utils.translateIdp(o2.getName())));
				}
			});

			List<String> proxies = PerunConfiguration.getRegistrarEnforcedProxies();
			List<String> hiddenProxies = PerunConfiguration.getRegistrarHiddenProxies();
			// just to make list unique (no more duplicate entries from same IdP).
			Set<String> offeredIdPs = new HashSet<>();

			for (final ExtSource source : sources) {

				if (source.getType().equals(ExtSource.ExtSourceType.X509.getType())) {

					AnchorListItem link = new AnchorListItem(Utils.convertCertCN(source.getName()));
					certItems.put(link, new ClickHandler() {
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



				} else if (source.getType().equals(ExtSource.ExtSourceType.IDP.getType())) {

					String entityId = source.getName();

					// skip more identities from same IdP or hidden IdPs
					if (offeredIdPs.contains(entityId) || hiddenProxies.contains(entityId)) continue;

					// IF Perun is behind proxy, offer only allowed proxies !!!
					// IF not proxy, show all
					if (proxies.isEmpty() || proxies.contains(entityId)) {

						offeredIdPs.add(entityId);

						final String finalEntityId = entityId;

						AnchorListItem link = new AnchorListItem(Utils.translateIdp(entityId));
						idpItems.put(link, new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {

								RegistrarManager.getConsolidatorToken(new JsonEvents() {
									@Override
									public void onFinished(JavaScriptObject jso) {
										// FINAL URL must logout from SP, login to SP using specified IdP, redirect to IC and after that return to application form
										String token = ((BasicOverlayObject) jso).getString();
										String consolidatorUrl = Utils.getIdentityConsolidatorLink("fed", true) + URL.encodeQueryString("&token=" + token);
										String redirectUrl = PerunConfiguration.getWayfSpLogoutUrl() + "?return=" + PerunConfiguration.getWayfSpLoginUrl() + URL.encodeQueryString("?authnContextClassRef=urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified%20urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport%20urn:cesnet:proxyidp:template:cesnet%20urn:cesnet:proxyidp:idpentityid:" + finalEntityId + "&target=" + consolidatorUrl);
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

					}

				} else if (source.getType().equals(ExtSource.ExtSourceType.KERBEROS.getType())) {

					if (source.getName().equalsIgnoreCase("EINFRA") ||
							source.getName().equalsIgnoreCase("EINFRA-SERVICES") ||
							source.getName().equalsIgnoreCase("ICS.MUNI.CZ")) {

						String toBePrefix = "krb";
						if (source.getName().equalsIgnoreCase("EINFRA-SERVICES")) toBePrefix = "krbes";
						// others goes to /krb/ on their respective instances
						final String prefix = toBePrefix;

						AnchorListItem link = new AnchorListItem(Utils.translateKerberos(source.getName()));
						othersItems.put(link, new ClickHandler() {
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

					}

				}

				if (!certItems.isEmpty()) {
					ft.getFlexCellFormatter().setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_MIDDLE);
					if (certItems.size() > 1) {
						ft.setWidget(row, 1, certGroup);
						for (AnchorListItem link : certItems.keySet()) {
							link.addClickHandler(certItems.get(link));
							menu.add(link);
						}
					} else {
						AnchorListItem key = certItems.keySet().iterator().next();
						Button button = getCertButton();
						button.addClickHandler(certItems.get(key));
						ft.setWidget(row, 2, button);
					}

				}

				if (!idpItems.isEmpty()) {
					ft.getFlexCellFormatter().setVerticalAlignment(row, 2, HasVerticalAlignment.ALIGN_MIDDLE);
					if (idpItems.size() > 1) {
						ft.setWidget(row, 2, idpGroup);
						for (AnchorListItem link : idpItems.keySet()) {
							link.addClickHandler(idpItems.get(link));
							menu2.add(link);
						}
					} else {
						AnchorListItem key = idpItems.keySet().iterator().next();
						Button button = getIdpButton();
						button.addClickHandler(idpItems.get(key));
						ft.setWidget(row, 2, button);
					}
				}

				if (!othersItems.isEmpty()) {
					ft.getFlexCellFormatter().setVerticalAlignment(row, 3, HasVerticalAlignment.ALIGN_MIDDLE);
					if (othersItems.size() > 1) {
						ft.setWidget(row, 3, othersGroup);
						for (AnchorListItem link : othersItems.keySet()) {
							link.addClickHandler(othersItems.get(link));
							menu3.add(link);
						}
					} else {
						AnchorListItem key = othersItems.keySet().iterator().next();
						Button button = getOthersButton();
						button.addClickHandler(othersItems.get(key));
						ft.setWidget(row, 3, button);
					}

				}

			}

			if (sources.isEmpty() || (idpItems.isEmpty() && certItems.isEmpty() && othersItems.isEmpty())) {

				// no identity for joining
				ft.setHTML(row, 1, translation.noIdentityForJoining(SafeHtmlUtils.fromString(PerunConfiguration.getBrandSupportMail()).asString()));
				ft.getFlexCellFormatter().setColSpan(row, 1, 3);

			} else {
				if (certItems.isEmpty()) ft.setText(row, 1, "");
				if (idpItems.isEmpty()) ft.setText(row, 2, "");
				if (othersItems.isEmpty()) ft.setText(row, 3, "");
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

		Button certButt = getCertButton();
		certButt.setDataToggle(Toggle.DROPDOWN);
		certGroup.add(certButt);
		DropDownMenu certMenu = new DropDownMenu();
		certGroup.add(certMenu);

		return certMenu;

	}

	private static Button getCertButton() {

		Button certButt = new Button(translation.byCertificate(), IconType.CERTIFICATE, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

			}
		});
		certButt.setPaddingLeft(10);
		certButt.setIconFixedWidth(true);
		certButt.setType(ButtonType.SUCCESS);
		return certButt;

	}

	private static DropDownMenu getIdpJoinButton(ButtonGroup idpGroup) {

		Button idpButt = getIdpButton();
		idpButt.setDataToggle(Toggle.DROPDOWN);
		idpGroup.add(idpButt);
		DropDownMenu idpMenu = new DropDownMenu();
		idpGroup.add(idpMenu);

		return idpMenu;

	}

	private static Button getIdpButton() {

		Button idpButt = new Button(translation.byIdp(), IconType.USER, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

			}
		});
		idpButt.setPaddingLeft(10);
		idpButt.setIconFixedWidth(true);
		idpButt.setType(ButtonType.SUCCESS);
		return idpButt;

	}

	private static DropDownMenu getOthersJoinButton(ButtonGroup othersButton) {

		Button othButt = getOthersButton();
		othButt.setDataToggle(Toggle.DROPDOWN);
		othersButton.add(othButt);
		DropDownMenu idpMenu = new DropDownMenu();
		othersButton.add(idpMenu);

		return idpMenu;

	}

	private static Button getOthersButton() {

		Button othButt = new Button(translation.byLoginPassword(), IconType.KEY, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

			}
		});
		othButt.setPaddingLeft(10);
		othButt.setIconFixedWidth(true);
		othButt.setType(ButtonType.SUCCESS);
		return othButt;

	}

}
