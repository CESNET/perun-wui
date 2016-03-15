package cz.metacentrum.perun.wui.registrar.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;

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

			setInfo(trans.voNotExistsException(Window.Location.getParameter("vo")), null);

		} else if (exception.getName().equalsIgnoreCase("GroupNotExistsException")) {

			setInfo(trans.groupNotExistsException(Window.Location.getParameter("group")), null);

		} else if (exception.getName().equalsIgnoreCase("WrongURL")) {

			setInfo(trans.missingVoInURL(), null);

		} else if (exception.getName().equalsIgnoreCase("FormNotExistsException")) {

			setInfo(trans.formNotExist(), null, false);

		} else if (exception.getName().equalsIgnoreCase("FormWrongFormedException")) {

			setInfo(trans.formWrongFormed(), null, false);

		} else if (exception.getName().equalsIgnoreCase("ApplicationNotCreatedException")) {

			setInfo(trans.applicationNotCreatedBecauseLogin(), null, false);

		} else if (exception.getName().equalsIgnoreCase("RpcException")) {

			setInfo(trans.applicationNotCreated(), null, false);

		}  else if (exception.getName().equalsIgnoreCase("RegistrarException")) {

			setInfo(trans.registrarException(getBeanName()), null);

		} else {

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

		setInfo(trans.missingRequiredData(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())), missingItems);
	}


	private void resolveExtendMembershipException() {

		if (exception.getReason().equals("OUTSIDEEXTENSIONPERIOD")) {

			String exceptionText = "<i>unlimited</i>";
			if (exception.getExpirationDate() != null) exceptionText = exception.getExpirationDate().split(" ")[0];
			setInfo(trans.cantExtendMembership(), trans.cantExtendMembershipOutside(exceptionText));

		} else if (exception.getReason().equals("NOUSERLOA")) {

			setInfo(trans.cantBecomeMember(getBeanName()), trans.cantBecomeMemberLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

		} else if (exception.getReason().equals("INSUFFICIENTLOA")) {

			setInfo(trans.cantBecomeMember(getBeanName()), trans.cantBecomeMemberInsufficientLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

		} else if (exception.getReason().equals("INSUFFICIENTLOAFOREXTENSION")) {

			setInfo(trans.cantExtendMembership(), trans.cantExtendMembershipInsufficientLoa(Utils.translateIdp(PerunSession.getInstance().getPerunPrincipal().getExtSource())));

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
