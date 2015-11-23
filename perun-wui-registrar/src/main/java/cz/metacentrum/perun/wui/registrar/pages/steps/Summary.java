package cz.metacentrum.perun.wui.registrar.pages.steps;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;
import cz.metacentrum.perun.wui.registrar.pages.FormView;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.ListGroup;
import org.gwtbootstrap3.client.ui.ListGroupItem;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ListGroupItemType;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * Represents a final step in registration process.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class Summary implements Step {

	private Application.ApplicationType voApplication;
	private Application.ApplicationType groupApplication;
	private FormView formView;
	private RegistrarTranslation translation;

	public Summary(FormView formView, Application.ApplicationType voApplication, Application.ApplicationType groupApplication) {
		this.formView = formView;
		this.translation = formView.getTranslation();
		this.voApplication = voApplication;
		this.groupApplication = groupApplication;
	}

	@Override
	public void call(final PerunPrincipal pp, final RegistrarObject registrar) {

		formView.getForm().clear();
		displaySummaryTitle(registrar);
		displaySummaryMessage(registrar);
		displayContinueButton(registrar, (groupApplication != null) ? groupApplication : voApplication);

	}

	private void displaySummaryTitle(RegistrarObject registrar) {
		Heading title = new Heading(HeadingSize.H2);
		Text text = new Text();

		if (voApplication == null && groupApplication == null) {

			//text.setText(translation.canDoNothing());

		} else {
			Icon success = new Icon(IconType.CHECK_CIRCLE);
			success.setColor("#5cb85c");
			title.add(success);
			if (groupApplication == Application.ApplicationType.INITIAL) {

				if (registrar.hasGroupFormAutoApproval()
						&& (voApplication == Application.ApplicationType.INITIAL && !registrar.hasVoFormAutoApproval())) {
					text.setText(" "+translation.initTitleAutoApproval());
				} else if (registrar.hasGroupFormAutoApproval()
						&& (voApplication != Application.ApplicationType.INITIAL)) {
					text.setText(" "+translation.initTitleAutoApproval());
				} else {
					text.setText(" "+translation.initTitle());
				}

			} else if (voApplication == Application.ApplicationType.EXTENSION) {

				if (registrar.hasVoFormAutoApprovalExtension()) {
					text.setText(" "+translation.extendTitleAutoApproval());
				} else {
					text.setText(" "+translation.extendTitle());
				}

			} else if (voApplication == Application.ApplicationType.INITIAL) {

				if (registrar.hasVoFormAutoApproval()) {
					text.setText(" "+translation.initTitleAutoApproval());
				} else {
					text.setText(" "+translation.initTitle());
				}

			}
		}

		if (!text.getText().isEmpty()) {
			title.add(text);
			formView.getForm().add(title);
		}
	}

	private void displaySummaryMessage(RegistrarObject registrar) {

        // We can do nothing for user => Everything is fine.
		if (voApplication == null && groupApplication == null) {

			if (registrar.getGroupFormInitialException() != null) {

			    displayException(registrar.getGroupFormInitialException());

			} else if (registrar.getVoFormExtensionException() != null) {

                if (registrar.getVoFormExtensionException().getName().equals("MemberNotExistsException")) {
                    if (registrar.getVoFormInitialException() != null) {
                        displayException(registrar.getVoFormInitialException());
                    }
                } else {
                    // TODO - solve this BLEEEH hack in better way.
                    if (registrar.getVoFormExtensionException().getName().equals("DuplicateRegistrationAttemptException")) {
                        registrar.getVoFormExtensionException().setName("DuplicateExtensionAttemptException");
                    }

                    displayException(registrar.getVoFormExtensionException());

                }
			} else if (registrar.getVoFormInitialException() != null) {

                displayException(registrar.getVoFormInitialException());

			}

        // user filled some form.
		} else {
			ListGroup message = new ListGroup();

			ListGroupItem voStat = new ListGroupItem();
			if (voApplication == Application.ApplicationType.EXTENSION) {

				if (registrar.hasVoFormAutoApprovalExtension()) {
					voStat.setText(translation.extended(registrar.getVo().getName()));
				} else {
					voStat.setText(translation.waitForExtAcceptation(registrar.getVo().getName()));
				}
				message.add(voStat);

			} else if (voApplication == Application.ApplicationType.INITIAL) {

				if (registrar.hasVoFormAutoApproval()) {
					voStat.setText(translation.registered(registrar.getVo().getName()));
				} else {
					voStat.setText(translation.waitForAcceptation(registrar.getVo().getName()));
				}
				message.add(voStat);

			}

			if (formView.getRegisteredUnknownMail()) {
				ListGroupItem verifyMail = new ListGroupItem();
				verifyMail.add(new Icon(IconType.WARNING));
				verifyMail.add(new Text(" " + translation.verifyMail()));
				verifyMail.setType(ListGroupItemType.WARNING);
				message.add(verifyMail);
			}

			ListGroupItem groupStat = new ListGroupItem();
			if (groupApplication == Application.ApplicationType.INITIAL) {

				if (registrar.hasGroupFormAutoApproval()
						&& (voApplication == Application.ApplicationType.INITIAL && !registrar.hasVoFormAutoApproval())) {
					groupStat.setText(translation.waitForVoAcceptation(registrar.getGroup().getName()));
				} else if (registrar.hasGroupFormAutoApproval()
						&& (voApplication != Application.ApplicationType.INITIAL)) {
					groupStat.setText(translation.registered(registrar.getGroup().getName()));
				} else {
					groupStat.setText(translation.waitForAcceptation(registrar.getGroup().getName()));
				}
				message.add(groupStat);

			} else if (groupApplication == null) {

				if (registrar.getGroupFormInitialException() != null) {
					switch (registrar.getGroupFormInitialException().getName()) {
						case "DuplicateRegistrationAttemptException":
							groupStat.setText(translation.groupFailedAlreadyApplied(registrar.getGroup().getName()));
							break;
						case "AlreadyRegisteredException":
							groupStat.setText(translation.groupFailedAlreadyRegistered(registrar.getGroup().getName()));
							break;
						default:
							groupStat.setText(registrar.getGroupFormInitialException().getMessage());
					}
					message.add(groupStat);
				}
			}

			formView.getForm().add(message);
		}

	}

	private void displayContinueButton(RegistrarObject registrar, Application.ApplicationType redirect) {

		PerunButton cont;
		if (redirect == null) {

			if (Window.Location.getParameter("targetexisting") != null) {

				PerunException exception;
				if (registrar.getGroup() != null) {
					exception = registrar.getGroupFormInitialException();
				} else {
					exception = registrar.getVoFormInitialException();
				}

                if (exception != null) {
                    if (exception.getName().equals("DuplicateRegistrationAttemptException")
                            || exception.getName().equals("AlreadyRegisteredException")) {

                        cont = PerunButton.getButton(PerunButtonType.CONTINUE, new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                Window.Location.assign(Window.Location.getParameter("targetexisting"));
                            }
                        });
                        formView.getForm().add(cont);
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
						formView.getForm().add(cont);
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
						formView.getForm().add(cont);
					}

					break;
			}
		}

	}

	private void displayException(PerunException ex) {
		formView.resolveException(ex);
	}
}