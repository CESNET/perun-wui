package cz.metacentrum.perun.wui.registrar.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItem;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class ExceptionResolverImpl implements ExceptionResolver {

	private GeneralObject bean;
	private PerunException exception;
	private String text;
	private String subtext;
	private boolean isSoft;
	private boolean showReSendButton = false;
	private String mailToVerify = "";
	private Application application;
	private PerunRegistrarTranslation trans = GWT.create(PerunRegistrarTranslation.class);

	public void resolve(PerunException exception, GeneralObject bean) {
		this.exception = exception;
		this.bean = bean;
		this.isSoft = true;
		resolve();
	}


	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getSubtext() {
		return subtext;
	}

	@Override
	public String getHTML() {
		if (exception == null) {
			return null;
		}
		String html = "<h4>"+getText()+"</h4>";
		if (getSubtext() != null) {
			html += "<p>"+getSubtext()+"</p>";
		}
		return html;
	}

	@Override
	public Boolean isSoft() {
		return isSoft;
	}

	@Override
	public GeneralObject getBean() {
		return bean;
	}

	@Override
	public boolean isShowMailVerificationReSendButton() {
		return showReSendButton;
	}

	public String getMailToVerify() {
		return mailToVerify;
	}

	@Override
	public Application getApplication() {
		return application;
	}

	private void resolve() {

		if ("ExtendMembershipException".equalsIgnoreCase(exception.getName())) {

			resolveExtendMembershipException();

		} else if ("AlreadyRegisteredException".equalsIgnoreCase(exception.getName())) {

			setInfo(trans.alreadyRegistered(getBeanName()), null);

		} else if ("DuplicateRegistrationAttemptException".equalsIgnoreCase(exception.getName()) ||
				"DuplicateExtensionAttemptException".equalsIgnoreCase(exception.getName())) {

			application = exception.getApplication();
			ArrayList<ApplicationFormItemData> applicationData = exception.getApplicationData();

			String text = "<p><br/><b>"+trans.submittedOn()+":</b>&nbsp;" + application.getCreatedAt().split("\\.")[0] +
					"<br/><b>"+trans.state()+":</b>&nbsp;" + application.getTranslatedState();

			if (application.getState().equals(Application.ApplicationState.NEW)) {
				for (final ApplicationFormItemData item : applicationData) {
					if (item.getFormItem() != null &&
							item.getFormItem().getType().equals(ApplicationFormItem.ApplicationFormItemType.VALIDATED_EMAIL) &&
							item.getAssuranceLevelAsInt() < 1) {
						showReSendButton = true;
						mailToVerify = SafeHtmlUtils.fromString((item.getValue() != null) ? item.getValue() : "").asString();
					}
				}
			}

			setInfo(trans.alreadySubmitted(getBeanName()),
					text + trans.visitSubmitted(Window.Location.getHref().split("#")[0], trans.submittedTitle()));

		} else if ("MissingRequiredDataException".equalsIgnoreCase(exception.getName())) {

			resolveMissingRequiredDataException();

		} else if ("VoNotExistsException".equalsIgnoreCase(exception.getName())) {

			String voName = (Window.Location.getParameter("vo") != null) ? URL.decodeQueryString(Window.Location.getParameter("vo")) : null;
			setInfo(trans.voNotExistsException(voName), null);

		} else if ("GroupNotExistsException".equalsIgnoreCase(exception.getName())) {

			String groupName = (Window.Location.getParameter("group") != null) ? URL.decodeQueryString(Window.Location.getParameter("group")) : null;
			setInfo(trans.groupNotExistsException(groupName), null);

		} else if ("WrongURL".equalsIgnoreCase(exception.getName())) {

			setInfo(trans.missingVoInURL(), null);

		} else if ("FormNotExistsException".equalsIgnoreCase(exception.getName())) {

			setInfo(trans.formNotExist(), null, false);

		} else if ("FormWrongFormedException".equalsIgnoreCase(exception.getName())) {

			setInfo(trans.formWrongFormed(), null, false);

		} else if ("ApplicationNotCreatedException".equalsIgnoreCase(exception.getName())) {

			setInfo(trans.applicationNotCreatedBecauseLogin(), null, false);

		} else if ("CantBeSubmittedException".equalsIgnoreCase(exception.getName())) {

			resolveCantBeSubmittedException();

		} else if ("CantBeApprovedException".equalsIgnoreCase(exception.getName())) {

			setInfo(trans.cantBeApproved(getBeanName()), null, true);

		} else if ("RpcException".equalsIgnoreCase(exception.getName())) {

			setInfo(trans.applicationNotCreated(), null, false);

		}  else if ("RegistrarException".equalsIgnoreCase(exception.getName())) {

			setInfo(trans.registrarException(getBeanName()), null);

		} else {

			if (JsUtils.checkParseInt(exception.getErrorId())) {
				int exceptionId = Integer.parseInt(exception.getErrorId());
				// assume HTTP error
				if (exceptionId < 600) {
					setInfo(trans.unableToSubmit(), exception.getMessage(), false);
					return;
				}
			}

			setInfo(trans.registrarException(getBeanName()), null);

		}

	}


	private void resolveMissingRequiredDataException() {

		String missingItems = "<ul>";
		if (!exception.getFormItems().isEmpty()) {
			for (ApplicationFormItemData item : exception.getFormItems()) {
				missingItems += "<li>" + trans.missingAttribute(item.getFormItem().getFederationAttribute()) + "</li>";
			}
		}
		missingItems += "</ul>";

		setInfo(trans.missingRequiredData(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())), missingItems, false);
	}


	private void resolveExtendMembershipException() {

		if ("OUTSIDEEXTENSIONPERIOD".equals(exception.getReason())) {

			String exceptionText = "<i>unlimited</i>";
			if (exception.getExpirationDate() != null) exceptionText = exception.getExpirationDate().split(" ")[0];
			String entityName = "";
			if ("Group".equals(getBean().getObjectType())) {
				entityName = ((Group)getBean()).getShortName();
			} else {
				entityName = ((Vo)getBean()).getName();
			}

			setInfo(trans.cantExtendMembership(), trans.cantExtendMembershipOutside(exceptionText, entityName));

		} else if ("NOUSERLOA".equals(exception.getReason())) {

			setInfo(trans.cantBecomeMember(getBeanName()), trans.cantBecomeMemberLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

		} else if ("INSUFFICIENTLOA".equals(exception.getReason())) {

			setInfo(trans.cantBecomeMember(getBeanName()), trans.cantBecomeMemberInsufficientLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

		} else if ("INSUFFICIENTLOAFOREXTENSION".equals(exception.getReason())) {

			setInfo(trans.cantExtendMembership(), trans.cantExtendMembershipInsufficientLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

		}
	}

	private void resolveCantBeSubmittedException() {

		if (Objects.equals("NOT_ACADEMIC", exception.getReason())) {
			setInfo(trans.cantSubmitLoA(), trans.notAcademicLoA(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));
		} else if (Objects.equals("NOT_ELIGIBLE", exception.getReason())) {
			setInfo(trans.cantSubmitLoA(), trans.notEligibleCESNET());
		} else {
			setInfo(trans.cantSubmitLoA(), exception.getMessage());
		}

	}


	private void setInfo(String text, String subtext) {
		setInfo(text, subtext, true);
	}
	private void setInfo(String text, String subtext, boolean isSoft) {
		this.text = text;
		this.subtext = subtext;
		this.isSoft = isSoft;
	}
	private String getBeanName() {
		if (bean != null) {
			if (bean instanceof Vo) {
				return bean.getName();
			} else if (bean instanceof Group) {
				return ((Group) bean).getShortName();
			}
		}
		return "unknown";
	}

}
