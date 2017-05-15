package cz.metacentrum.perun.wui.registrar.pages.steps;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.common.PerunPrincipal;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
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
 * Represents a final step in registration process. Show info.
 * Contains methods caseXxx(...) e.g. caseVoInitGroup is called in usecase [ VO initial application -> Group application ] etc.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class SummaryStep implements Step {

	private FormView formView;
	private PerunRegistrarTranslation translation;

	private final String TARGET_EXISTING = "targetexisting";
	private final String TARGET_NEW = "targetnew";
	private final String TARGET_EXTENDED = "targetextended";

	public SummaryStep(FormView formView) {
		this.formView = formView;
		this.translation = formView.getTranslation();
	}

	@Override
	public void call(final PerunPrincipal pp, Summary summary, Events<Result> events) {

		Heading title = new Heading(HeadingSize.H2);
		ListGroup messages = new ListGroup();

		if (summary.containsGroupResult()) {
			if (summary.containsVoInitResult()) {
				caseVoInitGroup(summary, title, messages);
			} else if (summary.containsVoExtResult()) {
				caseVoExtGroup(summary, title, messages);
			} else {
				caseGroup(summary, title, messages);
			}
		} else {
			if (summary.containsVoInitResult()) {
				caseVoInit(summary, title, messages);
			} else if (summary.containsVoExtResult()) {
				caseVoExt(summary, title, messages);
			} else {
				// Steps should not be empty.
			}
		}

	}



	private void caseVoInit(Summary summary, Heading title, ListGroup messages) {
		Result res = summary.getVoInitResult();
		if (res.isOk()) {
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			if (res.hasAutoApproval()) {
				title.add(new Text(" "+translation.initTitleAutoApproval()));
				msg.setText(translation.registered(res.getBean().getName()));
			} else {
				title.add(new Text(" "+translation.initTitle()));
				msg.setText(translation.waitForAcceptation(res.getBean().getName()));
			}

			messages.add(msg);
			verifyMailMessage(summary, messages);
		} else if (res.getException() != null && "CantBeApprovedException".equals(res.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO manager can manually handle it.
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();
			title.add(new Text(" "+translation.initTitle()));
			msg.setText(translation.waitForAcceptation(res.getBean().getName()));
			messages.add(msg);
			verifyMailMessage(summary, messages);

		} else {
			displayException(res.getException(), res.getBean());
		}

		// Show continue button
		PerunButton continueBtn = null;
		if (summary.alreadyMemberOfVo() || summary.alreadyAppliedToVo()) {
			continueBtn = getContinueButton(TARGET_EXISTING);
		} else if (res.isOk() || res.getException().getName().equals("RegistrarException")) {
			continueBtn = getContinueButton(TARGET_NEW);
		}

		displaySummary(title, messages, continueBtn);
	}



	private void caseVoExt(Summary summary, Heading title, ListGroup messages) {
		Result res = summary.getVoExtResult();
		if (res.isOk()) {
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			if (res.hasAutoApproval()) {
				title.add(new Text(" "+translation.extendTitleAutoApproval()));
				msg.setText(translation.extended(res.getBean().getName()));
			} else {
				title.add(new Text(" "+translation.extendTitle()));
				msg.setText(translation.waitForExtAcceptation(res.getBean().getName()));
			}

			messages.add(msg);
			verifyMailMessage(summary, messages);

		} else if (res.getException() != null && "CantBeApprovedException".equals(res.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO manager can manually handle it.
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();
			title.add(new Text(" "+translation.extendTitle()));
			msg.setText(translation.waitForExtAcceptation(res.getBean().getName()));
			messages.add(msg);
			verifyMailMessage(summary, messages);

		} else {
			// TODO - solve this BLEEEH hack in better way.
			if (res.getException().getName().equals("DuplicateRegistrationAttemptException")) {
				res.getException().setName("DuplicateExtensionAttemptException");
			}
			displayException(res.getException(), res.getBean());
		}

		// Show continue button
		PerunButton continueBtn = null;
		if (res.isOk() || res.getException().getName().equals("RegistrarException")) {
			continueBtn = getContinueButton(TARGET_EXTENDED);
		} else if (summary.alreadyAppliedForExtension() || summary.alreadyMemberOfVo()) {
			continueBtn = getContinueButton(TARGET_EXISTING);
		}

		displaySummary(title, messages, continueBtn);
	}



	private void caseGroup(Summary summary, Heading title, ListGroup messages) {
		Result res = summary.getGroupResult();
		if (res.isOk()) {
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			if (res.hasAutoApproval()) {
				if (summary.alreadyAppliedToVo()) {
					title.add(new Text(" "+translation.initTitle()));
					msg.setText(translation.waitForVoAcceptation(((Group) res.getBean()).getShortName()));
				} else {
					title.add(new Text(" "+translation.initTitleAutoApproval()));
					msg.setText(translation.registered(((Group) res.getBean()).getShortName()));
				}
			} else {
				title.add(new Text(" "+translation.initTitle()));
				msg.setText(translation.waitForAcceptation(((Group) res.getBean()).getShortName()));
			}

			messages.add(msg);
			verifyMailMessage(summary, messages);

		} else if (res.getException() != null && "CantBeApprovedException".equals(res.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO manager can manually handle it.
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			title.add(new Text(" "+translation.initTitle()));
			msg.setText(translation.waitForAcceptation(((Group) res.getBean()).getShortName()));

			messages.add(msg);
			verifyMailMessage(summary, messages);

		} else {
			displayException(res.getException(), res.getBean());
		}

		// Show continue button
		PerunButton continueBtn = null;
		if (summary.alreadyMemberOfGroup() || summary.alreadyAppliedToGroup()) {
			continueBtn = getContinueButton(TARGET_EXISTING);
		} else if (res.isOk() || res.getException().getName().equals("RegistrarException")) {
			continueBtn = getContinueButton(TARGET_NEW);
		}

		displaySummary(title, messages, continueBtn);
	}



	private void caseVoInitGroup(Summary summary, Heading title, ListGroup messages) {
		Result resVo = summary.getVoInitResult();
		Result res = summary.getGroupResult();

		// Show summary about initial application to VO
		if (resVo.isOk()) {
			ListGroupItem msg = new ListGroupItem();

			if (resVo.hasAutoApproval()) {
				msg.setText(translation.registered(resVo.getBean().getName()));
			} else {
				// Message from group application is sufficient in this case.
			}

			if (!msg.getText().isEmpty()) {
				messages.add(msg);
			}
		} else if (resVo.getException() != null && "CantBeApprovedException".equals(resVo.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO manager can manually handle it.
			ListGroupItem msg = new ListGroupItem();
			msg.setText(translation.waitForAcceptation(resVo.getBean().getName()));
			messages.add(msg);

		} else {
			displayException(resVo.getException(), resVo.getBean());
		}

		verifyMailMessage(summary, messages);

		// Show summary about application to group
		if (res.isOk()) {
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			if (res.hasAutoApproval()) {
				if (resVo.hasAutoApproval()) {
					title.add(new Text(" "+translation.initTitleAutoApproval()));
					msg.setText(translation.registered(((Group) res.getBean()).getShortName()));
				} else {
					title.add(new Text(" "+translation.initTitle()));
					msg.setText(translation.waitForVoAcceptation(((Group) res.getBean()).getShortName()));
				}
			} else {
				title.add(new Text(" "+translation.initTitle()));
				msg.setText(translation.waitForAcceptation(((Group) res.getBean()).getShortName()));
			}

			messages.add(msg);

		} else {
			displayException(res.getException(), res.getBean());
		}

		// Show continue button
		PerunButton continueBtn = null;
		if (summary.alreadyMemberOfGroup() || summary.alreadyAppliedToGroup()) {
			continueBtn = getContinueButton(TARGET_EXISTING);
		} else if ((res.isOk() || res.getException().getName().equals("RegistrarException"))
				&& (resVo.isOk() || resVo.getException().getName().equals("RegistrarException"))) {
			continueBtn = getContinueButton(TARGET_NEW);
		}

		displaySummary(title, messages, continueBtn);
	}



	private void caseVoExtGroup(Summary summary, Heading title, ListGroup messages) {
		Result resVo = summary.getVoExtResult();
		Result res = summary.getGroupResult();

		// Show summary about extension application to VO
		if (resVo.isOk()) {
			ListGroupItem msg = new ListGroupItem();

			if (resVo.hasAutoApproval()) {
				msg.setText(translation.extended(resVo.getBean().getName()));
			} else {
				msg.setText(translation.waitForExtAcceptation(resVo.getBean().getName()));
			}

			messages.add(msg);

		} else if (resVo.getException() != null && "CantBeApprovedException".equals(resVo.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO manager can manually handle it.
			ListGroupItem msg = new ListGroupItem();
			msg.setText(translation.waitForExtAcceptation(resVo.getBean().getName()));
			messages.add(msg);

		} else {
			displayException(resVo.getException(), resVo.getBean());
		}

		verifyMailMessage(summary, messages);

		// Show summary about application to group
		if (res.isOk()) {
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			if (res.hasAutoApproval()) {
				title.add(new Text(" "+translation.initTitleAutoApproval()));
				msg.setText(translation.registered(((Group) res.getBean()).getShortName()));
			} else {
				title.add(new Text(" "+translation.initTitle()));
				msg.setText(translation.waitForAcceptation(((Group) res.getBean()).getShortName()));
			}

			messages.add(msg);
		} else {
			displayException(res.getException(), res.getBean());
		}

		// Show continue button
		PerunButton continueBtn = null;
		if (summary.alreadyMemberOfGroup() || summary.alreadyAppliedToGroup()) {
			continueBtn = getContinueButton(TARGET_EXISTING);
		} else if (res.isOk() || res.getException().getName().equals("RegistrarException")) {
			continueBtn = getContinueButton(TARGET_NEW);
		}

		displaySummary(title, messages, continueBtn);
	}




	private Icon successIcon() {
		Icon success = new Icon(IconType.CHECK_CIRCLE);
		success.setColor("#5cb85c");
		return success;
	}

	private void verifyMailMessage(Summary summary, ListGroup messages) {
		if (summary.mustRevalidateEmail() != null) {
			ListGroupItem verifyMail = new ListGroupItem();
			verifyMail.add(new Icon(IconType.WARNING));
			verifyMail.add(new Text(" " + translation.verifyMail(summary.mustRevalidateEmail())));
			verifyMail.setType(ListGroupItemType.WARNING);
			messages.add(verifyMail);
		}
	}

	private void displaySummary(Heading title, ListGroup messgaes, PerunButton continueButton) {
		if (title != null || title.getWidgetCount() != 0 || !title.getText().isEmpty()) {
			formView.getForm().add(title);
		}
		if (messgaes != null || messgaes.getWidgetCount() != 0) {
			formView.getForm().add(messgaes);
		}
		if (continueButton != null) {
			formView.getForm().add(continueButton);
		}
	}


	private PerunButton getContinueButton(final String urlParameter) {

		if (Window.Location.getParameter(urlParameter) != null) {

			return PerunButton.getButton(PerunButtonType.CONTINUE, new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.Location.assign(Window.Location.getParameter(urlParameter));
				}
			});
		}
		return null;
	}

	private void displayException(PerunException ex, GeneralObject bean) {
		formView.displayException(ex, bean);
	}

	@Override
	public Result getResult() {
		// Nobody should be after Summary step so nobody can call it.
		return null;
	}
}
