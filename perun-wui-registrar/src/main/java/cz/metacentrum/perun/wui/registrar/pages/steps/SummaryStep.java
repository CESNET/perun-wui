package cz.metacentrum.perun.wui.registrar.pages.steps;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
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
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.ListGroup;
import org.gwtbootstrap3.client.ui.ListGroupItem;
import org.gwtbootstrap3.client.ui.constants.ColumnOffset;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ListGroupItemType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.html.Text;

/**
 * Represents a final step in registration process. Show info.
 * Contains methods caseXxx(...) e.g. caseVoInitGroupInit is called in usecase [ VO initial application -> Group application ] etc.
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

		if (summary.containsGroupInitResult()) {
			if (summary.containsVoInitResult()) {
				caseVoInitGroupInit(summary, title, messages);
			} else if (summary.containsVoExtResult()) {
				caseVoExtGroupInit(summary, title, messages);
			} else {
				caseGroupInit(summary, title, messages);
			}
		} else if (summary.containsGroupExtResult()) {
			if (summary.containsVoExtResult()) {
				caseVoExtGroupExt(summary, title, messages);
			} else {
				caseGroupExt(summary, title, messages);
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

	/**
	 * Fill summary with result about VO initial application
	 *
	 * @param summary
	 * @param title
	 * @param messages
	 */
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
				msg.setText(translation.waitForAcceptation());
			}

			messages.add(msg);
			verifyMailMessage(summary, messages);
		} else if (res.getException() != null && "CantBeApprovedException".equals(res.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO manager can manually handle it.
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();
			title.add(new Text(" "+translation.initTitle()));
			msg.setText(translation.waitForAcceptation());
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


	/**
	 * Fill summary with result about VO extension application
	 *
	 * @param summary
	 * @param title
	 * @param messages
	 */
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
				msg.setText(translation.waitForExtAcceptation());
			}

			messages.add(msg);
			verifyMailMessage(summary, messages);

		} else if (res.getException() != null && "CantBeApprovedException".equals(res.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO manager can manually handle it.
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();
			title.add(new Text(" "+translation.extendTitle()));
			msg.setText(translation.waitForExtAcceptation());
			messages.add(msg);
			verifyMailMessage(summary, messages);

		} else {
			// FIXME - solve this BLEEEH hack in better way.
			if (res.getException().getName().equals("DuplicateRegistrationAttemptException")) {
				res.getException().setName("DuplicateExtensionAttemptException");
			}
			displayException(res.getException(), res.getBean());
		}

		// Show continue button
		PerunButton continueBtn = null;
		if (res.isOk() || res.getException().getName().equals("RegistrarException")) {
			continueBtn = getContinueButton(TARGET_EXTENDED);
		} else if (summary.alreadyAppliedForVoExtension() || summary.alreadyMemberOfVo()) {
			continueBtn = getContinueButton(TARGET_EXISTING);
		}

		displaySummary(title, messages, continueBtn);
	}

	/**
	 * Fill summary with result about Group initial application
	 *
	 * @param summary
	 * @param title
	 * @param messages
	 */
	private void caseGroupInit(Summary summary, Heading title, ListGroup messages) {
		Result res = summary.getGroupInitResult();
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
				msg.setText(translation.waitForAcceptation());
			}

			messages.add(msg);
			verifyMailMessage(summary, messages);

		} else if (res.getException() != null && "CantBeApprovedException".equals(res.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO manager can manually handle it.
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			title.add(new Text(" "+translation.initTitle()));
			msg.setText(translation.waitForAcceptation());

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

	/**
	 * Fill summary with result about Group extension application
	 *
	 * @param summary
	 * @param title
	 * @param messages
	 */
	private void caseGroupExt(Summary summary, Heading title, ListGroup messages) {
		Result res = summary.getGroupExtResult();
		if (res.isOk()) {
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			if (res.hasAutoApproval()) {
				if (summary.alreadyAppliedForVoExtension()) {
					title.add(new Text(" " + translation.extendTitle()));
					msg.setText(translation.waitForVoExtension(((Group) res.getBean()).getShortName()));
				} else {
					title.add(new Text(" " + translation.extendTitleAutoApproval()));
					msg.setText(translation.extended(((Group) res.getBean()).getShortName()));
				}
			} else {
				title.add(new Text(" "+translation.extendTitle()));
				msg.setText(translation.waitForExtAcceptation());
			}

			messages.add(msg);
			verifyMailMessage(summary, messages);

		} else if (res.getException() != null && "CantBeApprovedException".equals(res.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO manager can manually handle it.
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			title.add(new Text(" "+translation.extendTitle()));
			msg.setText(translation.waitForExtAcceptation());

			messages.add(msg);
			verifyMailMessage(summary, messages);

		} else {
			// FIXME - solve this BLEEEH hack in better way.
			if (res.getException().getName().equals("DuplicateRegistrationAttemptException")) {
				res.getException().setName("DuplicateExtensionAttemptException");
			}
			displayException(res.getException(), res.getBean());
		}

		// Show continue button
		PerunButton continueBtn = null;
		if (summary.alreadyMemberOfGroup() || summary.alreadyAppliedForGroupExtension()) {
			continueBtn = getContinueButton(TARGET_EXISTING);
		} else if (res.isOk() || res.getException().getName().equals("RegistrarException")) {
			continueBtn = getContinueButton(TARGET_EXTENDED);
		}

		displaySummary(title, messages, continueBtn);
	}

	/**
	 * Fill summary with result about VO initial and Group initial application
	 *
	 * @param summary
	 * @param title
	 * @param messages
	 */
	private void caseVoInitGroupInit(Summary summary, Heading title, ListGroup messages) {

		Result resultVo = summary.getVoInitResult();
		Result resultGroup = summary.getGroupInitResult();

		// Show summary about initial application to VO
		if (resultVo.isOk()) {
			ListGroupItem msg = new ListGroupItem();

			if (resultVo.hasAutoApproval()) {
				msg.setText(translation.registered(resultVo.getBean().getName()));
			} else {
				// Message from group application is sufficient in this case.
			}

			if (!msg.getText().isEmpty()) {
				messages.add(msg);
			}
		} else if (resultVo.getException() != null && "CantBeApprovedException".equals(resultVo.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO manager can manually handle it.
			ListGroupItem msg = new ListGroupItem();
			msg.setText(translation.waitForAcceptation());
			messages.add(msg);

		} else {
			displayException(resultVo.getException(), resultVo.getBean());
		}

		verifyMailMessage(summary, messages);

		// Show summary about application to group
		if (resultGroup.isOk()) {

			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			if (resultGroup.hasAutoApproval()) {
				if (resultVo.hasAutoApproval()) {
					title.add(new Text(" "+translation.initTitleAutoApproval()));
					msg.setText(translation.registered(((Group) resultGroup.getBean()).getShortName()));
				} else {
					title.add(new Text(" "+translation.initTitle()));
					msg.setText(translation.waitForVoAcceptation(((Group) resultGroup.getBean()).getShortName()));
				}
			} else {
				title.add(new Text(" "+translation.initTitle()));
				msg.setText(translation.waitForAcceptation());
			}

			messages.add(msg);

		} else if (resultGroup.getException() != null && "CantBeApprovedException".equals(resultGroup.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO/Group manager can manually handle it.
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			title.add(new Text(" "+translation.initTitle()));
			msg.setText(translation.waitForAcceptation());

			messages.add(msg);

		} else {
			displayException(resultGroup.getException(), resultGroup.getBean());
		}

		// Show continue button
		PerunButton continueBtn = null;
		if (summary.alreadyMemberOfGroup() || summary.alreadyAppliedToGroup()) {
			continueBtn = getContinueButton(TARGET_EXISTING);
		} else if ((resultGroup.isOk() || resultGroup.getException().getName().equals("RegistrarException"))
				&& (resultVo.isOk() || resultVo.getException().getName().equals("RegistrarException"))) {
			continueBtn = getContinueButton(TARGET_NEW);
		}

		displaySummary(title, messages, continueBtn);
	}

	/**
	 * Fill summary with result about VO extension and Group initial application
	 *
	 * @param summary
	 * @param title
	 * @param messages
	 */
	private void caseVoExtGroupInit(Summary summary, Heading title, ListGroup messages) {

		Result resultVo = summary.getVoExtResult();
		Result resultGroup = summary.getGroupInitResult();

		// Show summary about extension application to VO
		if (resultVo.isOk()) {
			ListGroupItem msg = new ListGroupItem();

			if (resultVo.hasAutoApproval()) {
				msg.setText(translation.extended(resultVo.getBean().getName()));
			} else {
				// Message from group application is sufficient in this case.
			}

			if (!msg.getText().isEmpty()) {
				messages.add(msg);
			}

		} else if (resultVo.getException() != null && "CantBeApprovedException".equals(resultVo.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO manager can manually handle it.
			ListGroupItem msg = new ListGroupItem();
			msg.setText(translation.waitForExtAcceptation());
			messages.add(msg);

		} else {
			// FIXME - solve this BLEEEH hack in better way.
			if (resultVo.getException().getName().equals("DuplicateRegistrationAttemptException")) {
				resultVo.getException().setName("DuplicateExtensionAttemptException");
			}
			displayException(resultVo.getException(), resultVo.getBean());
		}

		// Show summary about application to group
		if (resultGroup.isOk()) {
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			if (resultGroup.hasAutoApproval()) {
				if (resultVo.hasAutoApproval()) { // FIXME - tohle by se mělo vyhodnotit z předchozího stavu (není auto nebo byla chyba)
					title.add(new Text(" "+translation.initTitleAutoApproval()));
					msg.setText(translation.registered(((Group) resultGroup.getBean()).getShortName()));
				} else {
					title.add(new Text(" "+translation.initTitle()));
					msg.setText(translation.waitForVoExtension(((Group) resultGroup.getBean()).getShortName()));
				}
			} else {
				title.add(new Text(" "+translation.initTitle()));
				msg.setText(translation.waitForAcceptation());
			}

			messages.add(msg);

		} else if (resultGroup.getException() != null && "CantBeApprovedException".equals(resultGroup.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO/Group manager can manually handle it.
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			title.add(new Text(" "+translation.initTitle()));
			msg.setText(translation.waitForAcceptation());

			messages.add(msg);

		} else {
			displayException(resultGroup.getException(), resultGroup.getBean());
		}

		// Show continue button
		PerunButton continueBtn = null;
		if (summary.alreadyMemberOfGroup() || summary.alreadyAppliedToGroup()) {
			continueBtn = getContinueButton(TARGET_EXISTING);
		} else if (resultGroup.isOk() || resultGroup.getException().getName().equals("RegistrarException")) {
			continueBtn = getContinueButton(TARGET_NEW);
		}

		displaySummary(title, messages, continueBtn);
	}

	/**
	 * Fill summary with result about VO extension and Group extension application
	 *
	 * @param summary
	 * @param title
	 * @param messages
	 */
	private void caseVoExtGroupExt(Summary summary, Heading title, ListGroup messages) {

		Result resultVo = summary.getVoExtResult();
		Result resultGroup = summary.getGroupInitResult();

		// Show summary about extension application to VO
		if (resultVo.isOk()) {
			ListGroupItem msg = new ListGroupItem();

			if (resultVo.hasAutoApproval()) {
				msg.setText(translation.extended(resultVo.getBean().getName()));
			} else {
				// Message from group application is sufficient in this case.
			}

			if (!msg.getText().isEmpty()) {
				messages.add(msg);
			}

		} else if (resultVo.getException() != null && "CantBeApprovedException".equals(resultVo.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO manager can manually handle it.
			ListGroupItem msg = new ListGroupItem();
			msg.setText(translation.waitForExtAcceptation());
			messages.add(msg);

		} else {
			// FIXME - solve this BLEEEH hack in better way.
			if (resultVo.getException().getName().equals("DuplicateRegistrationAttemptException")) {
				resultVo.getException().setName("DuplicateExtensionAttemptException");
			}
			displayException(resultVo.getException(), resultVo.getBean());
		}

		// Show summary about application to group
		if (resultGroup.isOk()) {
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			if (resultGroup.hasAutoApproval()) {
				if (resultVo.hasAutoApproval()) {
					title.add(new Text(" "+translation.extendTitleAutoApproval()));
					msg.setText(translation.extended(((Group) resultGroup.getBean()).getShortName()));
				} else {
					title.add(new Text(" "+translation.extendTitle()));
					msg.setText(translation.waitForVoExtension(((Group) resultGroup.getBean()).getShortName()));
				}
			} else {
				title.add(new Text(" "+translation.extendTitle()));
				msg.setText(translation.waitForExtAcceptation());
			}

			messages.add(msg);

		} else if (resultGroup.getException() != null && "CantBeApprovedException".equals(resultGroup.getException().getName())) {

			// FIXME - hack to ignore CantBeApprovedException since VO/Group manager can manually handle it.
			title.add(successIcon());
			ListGroupItem msg = new ListGroupItem();

			title.add(new Text(" "+translation.extendTitle()));
			msg.setText(translation.waitForExtAcceptation());

			messages.add(msg);

		} else {
			// FIXME - solve this BLEEEH hack in better way.
			if (resultVo.getException().getName().equals("DuplicateRegistrationAttemptException")) {
				resultVo.getException().setName("DuplicateExtensionAttemptException");
			}
			displayException(resultGroup.getException(), resultGroup.getBean());
		}

		// Show continue button
		PerunButton continueBtn = null;
		if (summary.alreadyMemberOfGroup() || summary.alreadyAppliedForGroupExtension()) {
			continueBtn = getContinueButton(TARGET_EXISTING);
		} else if (resultGroup.isOk() || resultGroup.getException().getName().equals("RegistrarException")) {
			continueBtn = getContinueButton(TARGET_EXTENDED);
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

	private void displaySummary(Heading title, ListGroup messages, PerunButton continueButton) {
		if (title != null || title.getWidgetCount() != 0 || !title.getText().isEmpty()) {
			formView.getForm().add(title);
		}
		if (messages != null || messages.getWidgetCount() != 0) {
			formView.getForm().add(messages);
		}
		if (continueButton != null) {
			formView.getForm().add(continueButton);
		}
	}


	private PerunButton getContinueButton(final String urlParameter) {

		if (Window.Location.getParameter(urlParameter) != null) {

			PerunButton continueButton = PerunButton.getButton(PerunButtonType.CONTINUE);

			continueButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					formView.getForm().clear();

					Heading head = new Heading(HeadingSize.H4, translation.redirectingBackToService());
					Icon spin = new Icon(IconType.SPINNER);
					spin.setSpin(true);
					spin.setSize(IconSize.LARGE);
					spin.setPull(Pull.LEFT);
					spin.setMarginTop(10);

					Column column = new Column(ColumnSize.MD_8, ColumnSize.LG_6, ColumnSize.SM_10, ColumnSize.XS_12);
					column.setOffset(ColumnOffset.MD_2,ColumnOffset.LG_3,ColumnOffset.SM_1,ColumnOffset.XS_0);

					column.add(spin);
					column.add(head);
					column.setMarginTop(30);

					formView.getForm().add(column);

					// WAIT 4 SEC BEFORE REDIRECT
					Timer timer = new Timer() {
						@Override
						public void run() {
							Window.Location.assign(Window.Location.getParameter(urlParameter));
						}
					};
					timer.schedule(7000);
				}
			});
			return continueButton;

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
