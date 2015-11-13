package cz.metacentrum.perun.wui.registrar.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.registrar.client.RegistrarTranslation;
import cz.metacentrum.perun.wui.widgets.AlertErrorReporter;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;
import org.gwtbootstrap3.client.ui.constants.AlertType;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class ExceptionResolver {

	private Vo vo;
	private Group group;
	private PerunException exception;
	private AlertErrorReporter notice;
	private ClickHandler handler;
	private boolean isSoft;
	private RegistrarTranslation translation = GWT.create(RegistrarTranslation.class);


	/**
	 * resolve means display proper info in notice widget, optionally show option to report bug or try call again.
	 * return if exception is soft or hard. Soft means application process should continue.
	 * Hard means process cant continue (usually user should have option to report bug).
	 *
	 * @param exception Investigating exception
	 * @param notice
	 * @param vo related vo
	 * @param group related group
	 * @param handler
	 * @return TRUE if exception is soft
	 */
	public boolean resolve(PerunException exception, AlertErrorReporter notice, Vo vo, Group group, ClickHandler handler) {
		this.handler = handler;
		return resolve(exception, notice, vo, group);
	}
	public boolean resolve(PerunException exception, AlertErrorReporter notice, Vo vo, Group group) {
		this.exception = exception;
		this.group = group;
		this.vo = vo;
		this.notice = notice;
		this.isSoft = true;
		return resolve();
	}


	private boolean resolve() {
		if (exception == null) {
			return false;
		}
		if (notice == null) {
			return false;
		}


		if (exception.getName().equalsIgnoreCase("ExtendMembershipException")) {

			resolveExtendMembershipException(exception, notice, vo, group);

		} else if (exception.getName().equalsIgnoreCase("AlreadyRegisteredException")) {

			notice.setHTML("<h4>" + translation.alreadyRegistered((group != null) ? group.getShortName() : vo.getName()) + "</h4>");
			addContinueButton("targetexisting", notice);

		} else if (exception.getName().equalsIgnoreCase("DuplicateRegistrationAttemptExceptioon")) {

			notice.setHTML("<h4>" + translation.alreadySubmitted(((group != null) ? group.getShortName() : vo.getName())) +
					"</h4><p>" + translation.visitSubmitted(Window.Location.getHref().split("#")[0], translation.submittedTitle()));
			addContinueButton("targetnew", notice);

		} else if (exception.getName().equalsIgnoreCase("DuplicateExtensionAttemptException")) {

			notice.getElement().setInnerHTML("<h4>" + translation.alreadySubmittedExtension((vo.getName())) +
					"</h4><p>" + translation.visitSubmitted(Window.Location.getHref().split("#")[0], translation.submittedTitle()));
			addContinueButton("targetextended", notice);

		} else if (exception.getName().equalsIgnoreCase("MissingRequiredDataException")) {

			resolveMissingRequiredDataException(exception, notice, vo, group);

		} else if (exception.getName().equalsIgnoreCase("VoNotExistsException")) {

			notice.setHTML("<h4>" + translation.voNotExistsException(Window.Location.getParameter("vo")) + "</h4>");

		} else if (exception.getName().equalsIgnoreCase("GroupNotExistsException")) {

			notice.setHTML("<h4>" + translation.groupNotExistsException(Window.Location.getParameter("group")) + "</h4>");

		} else if (exception.getName().equalsIgnoreCase("WrongURL")) {

			notice.setHTML("<h4>" + translation.missingVoInURL() + "</h4>");

		} else if (exception.getName().equalsIgnoreCase("FormNotExistsException")) {

			notice.setHTML("<h4>" + translation.formNotExist() + "</h4>");

		} else if (exception.getName().equalsIgnoreCase("ApplicationNotCreatedException")) {

			notice.setHTML("<h4>" + translation.applicationNotCreated() + "</h4>");
			notice.setReportInfo(exception);
			notice.setType(AlertType.DANGER);
			isSoft = false;

		} else  if (exception.getName().equalsIgnoreCase("RpcException")) {

			notice.setHTML("<h4>" + translation.applicationNotCreated() + "</h4>");
			notice.setReportInfo(exception);
			notice.setType(AlertType.DANGER);
			isSoft = false;

		} else  if (exception.getName().equalsIgnoreCase("RegistrarException")) {

			notice.setHTML("<h4>" + translation.registrarException() + "</h4>");

		} else {

			notice.setHTML("<h4>" + translation.registrarException() + "</h4>");

		}


		if (handler != null) {
			notice.setRetryHandler(handler);
		}

		notice.setVisible(true);
		return isSoft;
	}


	private void resolveMissingRequiredDataException(PerunException exception, AlertErrorReporter notice, Vo vo, Group group) {

		String missingItems = "<p><ul>";
		if (!exception.getFormItems().isEmpty()) {
			for (ApplicationFormItemData item : exception.getFormItems()) {
				missingItems += "<li>" + translation.missingAttribute(item.getFormItem().getFederationAttribute());
			}
		}
		missingItems += "<ul/>";

		notice.setHTML(translation.missingRequiredData(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())) + missingItems);
	}


	private void resolveExtendMembershipException(PerunException exception, AlertErrorReporter notice, Vo vo, Group group) {

		if (exception.getReason().equals("OUTSIDEEXTENSIONPERIOD")) {

			String exceptionText = "<i>unlimited</i>";
			if (exception.getExpirationDate() != null) exceptionText = exception.getExpirationDate().split(" ")[0];
			notice.setHTML("<h4>" + translation.cantExtendMembership() + "</h4><p>" + translation.cantExtendMembershipOutside(exceptionText));

			addContinueButton("targetexisting", notice);

		} else if (exception.getReason().equals("NOUSERLOA")) {

			notice.setHTML("<h4>" + translation.cantBecomeMember((group != null) ? group.getShortName() : vo.getName()) + "</h4><p>" + translation.cantBecomeMemberLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

		} else if (exception.getReason().equals("INSUFFICIENTLOA")) {

			notice.setHTML("<h4>" + translation.cantBecomeMember((group != null) ? group.getShortName() : vo.getName()) + "</h4><p>" + translation.cantBecomeMemberInsufficientLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

		} else if (exception.getReason().equals("INSUFFICIENTLOAFOREXTENSION")) {

			notice.setHTML("<h4>" + translation.cantExtendMembership() + "</h4><p>" + translation.cantExtendMembershipInsufficientLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

		}
	}


	private void addContinueButton(final String target, AlertErrorReporter notice) {

		if (Window.Location.getParameter(target) != null) {

			PerunButton continueButton = PerunButton.getButton(PerunButtonType.CONTINUE, new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.Location.replace(Window.Location.getParameter(target));
				}
			});
			notice.add(continueButton);

		}

	}

}
