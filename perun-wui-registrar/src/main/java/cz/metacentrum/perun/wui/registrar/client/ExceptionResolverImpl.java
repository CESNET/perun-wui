package cz.metacentrum.perun.wui.registrar.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;

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

	private void resolve() {

		if (exception.getName().equalsIgnoreCase("ExtendMembershipException")) {

			resolveExtendMembershipException();

		} else if (exception.getName().equalsIgnoreCase("AlreadyRegisteredException")) {

			setInfo(trans.alreadyRegistered(getBeanName()), null);

		} else if (exception.getName().equalsIgnoreCase("DuplicateRegistrationAttemptException")) {

			setInfo(trans.alreadySubmitted(getBeanName()),
					trans.visitSubmitted(Window.Location.getHref().split("#")[0], trans.submittedTitle()));

		} else if (exception.getName().equalsIgnoreCase("DuplicateExtensionAttemptException")) {

			setInfo(trans.alreadySubmittedExtension(getBeanName()),
					trans.visitSubmitted(Window.Location.getHref().split("#")[0], trans.submittedTitle()));

		} else if (exception.getName().equalsIgnoreCase("MissingRequiredDataException")) {

			resolveMissingRequiredDataException();

		} else if (exception.getName().equalsIgnoreCase("VoNotExistsException")) {

			String voName = (Window.Location.getParameter("vo") != null) ? URL.decodeQueryString(Window.Location.getParameter("vo")) : null;
			setInfo(trans.voNotExistsException(voName), null);

		} else if (exception.getName().equalsIgnoreCase("GroupNotExistsException")) {

			String groupName = (Window.Location.getParameter("group") != null) ? URL.decodeQueryString(Window.Location.getParameter("group")) : null;
			setInfo(trans.groupNotExistsException(groupName), null);

		} else if (exception.getName().equalsIgnoreCase("WrongURL")) {

			setInfo(trans.missingVoInURL(), null);

		} else if (exception.getName().equalsIgnoreCase("FormNotExistsException")) {

			setInfo(trans.formNotExist(), null, false);

		} else if (exception.getName().equalsIgnoreCase("FormWrongFormedException")) {

			setInfo(trans.formWrongFormed(), null, false);

		} else if (exception.getName().equalsIgnoreCase("ApplicationNotCreatedException")) {

			setInfo(trans.applicationNotCreatedBecauseLogin(), null, false);

		} else if (exception.getName().equalsIgnoreCase("CantBeSubmittedException")) {

			resolveCantBeSubmittedException();

		} else if (exception.getName().equalsIgnoreCase("CantBeApprovedException")) {

			setInfo(trans.cantBeApproved(getBeanName()), null, true);

		} else if (exception.getName().equalsIgnoreCase("RpcException")) {

			setInfo(trans.applicationNotCreated(), null, false);

		}  else if (exception.getName().equalsIgnoreCase("RegistrarException")) {

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

		if (exception.getReason().equals("OUTSIDEEXTENSIONPERIOD")) {

			String exceptionText = "<i>unlimited</i>";
			if (exception.getExpirationDate() != null) exceptionText = exception.getExpirationDate().split(" ")[0];
			String entityName = "";
			if ("Group".equals(getBean().getObjectType())) {
				entityName = ((Group)getBean()).getShortName();
			} else {
				entityName = ((Vo)getBean()).getName();
			}

			setInfo(trans.cantExtendMembership(), trans.cantExtendMembershipOutside(exceptionText, entityName));

		} else if (exception.getReason().equals("NOUSERLOA")) {

			setInfo(trans.cantBecomeMember(getBeanName()), trans.cantBecomeMemberLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

		} else if (exception.getReason().equals("INSUFFICIENTLOA")) {

			setInfo(trans.cantBecomeMember(getBeanName()), trans.cantBecomeMemberInsufficientLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

		} else if (exception.getReason().equals("INSUFFICIENTLOAFOREXTENSION")) {

			setInfo(trans.cantExtendMembership(), trans.cantExtendMembershipInsufficientLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

		}
	}

	private void resolveCantBeSubmittedException() {

		if (Objects.equals("NOT_ACADEMIC", exception.getReason())) {
			setInfo(trans.cantSubmitLoA(), trans.notAcademicLoA(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));
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
		if (bean == null) {
			return "unknown";
		}
		if (bean instanceof Group) {
			return ((Group) bean).getShortName();
		} else {
			return bean.getName();
		}
	}

}
