package cz.metacentrum.perun.wui.json;

import com.google.gwt.core.client.GWT;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunErrorTranslation;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.Group;

/**
 * Manual translation of Perun error messages to user-friendly messages.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class ErrorTranslator {

	private static PerunErrorTranslation translation = GWT.create(PerunErrorTranslation.class);

	public static String getTranslatedMessage(PerunException error) {

		String errorName = error.getName();

		String pleaseRefresh = translation.pleaseRefresh();

		// RPC ERRORS
		if ("RpcException".equalsIgnoreCase(errorName)) {

			if ("UNCATCHED_EXCEPTION".equalsIgnoreCase(error.getType())) {
				return translation.uncatchedException();
			} else {
				return translation.rpcException() + " " + pleaseRefresh;
			}

		} else if ("PrivilegeException".equalsIgnoreCase(errorName)) {

			return translation.notAuthorizedCallback();

		} else if ("WrongAttributeAssignmentException".equalsIgnoreCase(errorName)) {

			return "You tried to set wrong attribute for entity. Please report this error.";

		} else if ("WrongAttributeValueException".equalsIgnoreCase(errorName)) {

			Attribute a = error.getAttribute();
			GeneralObject holder = error.getAttributeHolder();
			GeneralObject secondHolder = error.getAttributeHolderSecondary();

			String text = "Wrong value of attribute (value or format).<p>";

			if (holder != null) {
				if (!holder.getName().equalsIgnoreCase("undefined")) {
					text += "<strong>" + holder.getObjectType() + ":</strong>&nbsp;" + holder.getName() + "<br />";
				}
			}
			if (secondHolder != null) {
				if (!secondHolder.getName().equalsIgnoreCase("undefined")) {
					text += "<strong>" + secondHolder.getObjectType() + ":</strong>&nbsp;" + secondHolder.getName() + "<br />";
				}
			}
			if (a != null) {
				String attrName = a.getName();
				String attrValue = a.getValue();
				text += "<strong>Attribute:&nbsp;</strong>" + attrName + "<br /><strong>Value:&nbsp;</strong>" + attrValue;
			} else {
				text += "<i>Attribute is null</i>";
			}

			return text;

		} else if ("WrongReferenceAttributeValueException".equalsIgnoreCase(errorName)) {

			String text = "Value of one of related attributes is incorrect.";

			Attribute a = error.getAttribute();
			Attribute a2 = error.getReferenceAttribute();

			if (a != null) {
				String attrName = a.getName();
				String attrValue = a.getValue();
				String entity = a.getEntity();
				text += "<p><strong>Attribute&nbsp;1:</strong>&nbsp;" + attrName + " (" + entity + ")";
				text += "<br/><strong>Value&nbsp;1:</strong>&nbsp;" + attrValue;
			} else {
				text += "<p><i>Attribute 1 is null</i>";
			}

			if (a2 != null) {
				String attrName = a2.getName();
				String attrValue = a2.getValue();
				String entity = a2.getEntity();
				text += "<p><strong>Attribute&nbsp;2:</strong>&nbsp;" + attrName + " (" + entity + ")";
				text += "<br/><strong>Value&nbsp;2:</strong>&nbsp;" + attrValue;
			} else {
				text += "<p><i>Attribute 2 is null</i>";
			}

			return text;

		} else if ("AttributeNotExistsException".equalsIgnoreCase(errorName)) {

			Attribute a = error.getAttribute();
			if (a != null) {
				return "Attribute definition for attribute <i>" + a.getName() + "</i> doesn't exist.";
			} else {
				return "Attribute definition for attribute <i>null</i> doesn't exist.";
			}

			// ALL CABINET EXCEPTIONS
		} else if ("CabinetException".equalsIgnoreCase(errorName)) {

			String text = "";
			if (error.getType().equalsIgnoreCase("NO_IDENTITY_FOR_PUBLICATION_SYSTEM")) {
				text = "You don't have registered identity in Perun related to selected publication system.<p>Please visit <a target=\"new\" href=\"" + Utils.getIdentityConsolidatorLink(false) + "\">identity consolidator</a> to add more identities.";
			}

			return text;

			// STANDARD ERRORS ALPHABETICALLY
		} else if ("AlreadyAdminException".equalsIgnoreCase(errorName)) {

			String text = "";
			if (error.getUser() != null) {
				text = error.getUser().getFullName();
			} else {
				text = "User";
			}
			if (error.getVo() != null) {
				text += " is already manager of VO: " + error.getVo().getName();
			} else if (error.getFacility() != null) {
				text += " is already manager of Facility: " + error.getFacility().getName();
			} else if (error.getGroup() != null) {
				text += " is already manager of Group: " + error.getGroup().getName();
			}
			/* TODO - add SecurityTeam bean
			else if (error.getSecurityTeam() != null) {
				text += " is already manager of SecurityTeam: " + error.getSecurityTeam().getName();
			}
			*/
			return text;

		} else if ("AlreadyMemberException".equalsIgnoreCase(errorName)) {

			// TODO - this exception must contain user first !!
			return "User is already member of VO / Group.";

		} else if ("AlreadyReservedLoginException".equalsIgnoreCase(errorName)) {

			String text = "";
			if (error.getLogin() != null) {
				text += "Login: " + error.getLogin();
				if (error.getNamespace() != null) {
					text += " in namespace: " + error.getNamespace() + " is already reserved.";
				} else {
					text += " is already reserved.";
				}
			} else {
				text += "Login";
				if (error.getNamespace() != null) {
					text += " in namespace: " + error.getNamespace() + " is already reserved.";
				} else {
					text += " is already reserved in selected namespace.";
				}
			}
			return text;

		} else if ("AttributeAlreadyAssignedException".equalsIgnoreCase(errorName)) {

			if (error.getAttribute() != null) {
				return "Attribute <i>" + error.getAttribute().getName() + "</i> is already set as required by service.";
			} else {
				return "Attribute is already set as required by service.";
			}

		} else if ("AttributeExistsException".equalsIgnoreCase(errorName)) {

			return "Same attribute definition already exists in Perun.";

		} else if ("AttributeNotAssignedException".equalsIgnoreCase(errorName)) {

			if (error.getAttribute() != null) {
				return "Attribute <i>" + error.getAttribute().getName() + "</i> is already NOT required by service.";
			} else {
				return "Attribute is already NOT required by service.";
			}

		} else if ("AttributeNotExistsException".equalsIgnoreCase(errorName)) {

			// FIXME - attribute object inside is never used, but has good description
			return error.getMessage();

		} else if ("AttributeValueException".equalsIgnoreCase(errorName)) {

			// FIXME - core always uses extensions of this exception
			return error.getMessage();

		} else if ("ApplicationNotCreatedException".equalsIgnoreCase(errorName)) {

			// FIXME - now handled in registrar ??
			//return ApplicationMessages.INSTANCE.errorWhileCreatingApplicationMessage();

		} else if ("CandidateNotExistsException".equalsIgnoreCase(errorName)) {

			return "Candidate for VO membership doesn't exists in external source.";

		} else if ("ClusterNotExistsException".equalsIgnoreCase(errorName)) {

			return "Facility is not of type <i>cluster</i> or <i>virtual cluster</i>";

		} else if ("ConsistencyErrorException".equalsIgnoreCase(errorName)) {

			return "Your operation can't be completed. There seems to be a problem with DB consistency, please report this error.";

		} else if ("DestinationAlreadyAssignedException".equalsIgnoreCase(errorName)) {

			/* TODO - Add destination bean
			if (error.getDestination() != null) {
				return "Destination <i>" + error.getDestination().getDestination() + "</i> already exists for facility/service.";
			} else {
				return "Same destination already exists for facility/service combination.";
			}
			*/

		} else if ("DestinationAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			/* TODO - Add destination bean
			if (error.getDestination() != null) {
				return "Destination <i>" + error.getDestination().getDestination() + "</i> already removed for facility/service.";
			} else {
				return "Destination is already removed from facility/service combination.";
			}
			*/

		} else if ("DestinationExistsException".equalsIgnoreCase(errorName)) {

			return "Same destination already exists.";

		} else if ("DestinationNotExistsException".equalsIgnoreCase(errorName)) {

			return "Destination of this name/id doesn't exists.";

		} else if ("DiacriticNotAllowedException".equalsIgnoreCase(errorName)) {

			// has meaningful info
			return error.getMessage();

			// FIXME - ENTITY exceptions are always extended - we will use specific types

		} else if ("ExtendMembershipException".equalsIgnoreCase(errorName)) {

			String text = "Membership in VO can't be established or extended. ";
			if ("NOUSERLOA".equalsIgnoreCase(error.getReason())) {
				text += " User's IDP does not provide Level of Assurance but VO requires it.";
			} else if ("INSUFFICIENTLOA".equalsIgnoreCase(error.getReason())) {
				text += " User's Level of Assurance is not sufficient for VO.";
			} else if ("INSUFFICIENTLOAFOREXTENSION".equalsIgnoreCase(error.getReason())) {
				text += " User's Level of Assurance is not sufficient for VO.";
			} else if ("OUTSIDEEXTENSIONPERIOD".equalsIgnoreCase(error.getReason())) {
				text += " It can be done usually in short time before and after membership expiration.";
			}
			return text;

		} else if ("ExtSourceAlreadyAssignedException".equalsIgnoreCase(errorName)) {

			if (error.getExtSource() != null) {
				return "Same external source is already assigned to your VO." +
						"<p><strong>Name:</strong> " + error.getExtSource().getName() + "</br>" +
						"<strong>Type:</strong> " + error.getExtSource().getType();
			} else {
				return "Same external source is already assigned to your VO.";
			}

		} else if ("ExtSourceAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			if (error.getExtSource() != null) {
				return "Same external source was already removed from your VO." +
						"<p><strong>Name:</strong> " + error.getExtSource().getName() + "</br>" +
						"<strong>Type:</strong> " + error.getExtSource().getType();
			} else {
				return "Same external source was already removed from your VO.";
			}

		} else if ("ExtSourceExistsException".equalsIgnoreCase(errorName)) {

			if (error.getExtSource() != null) {
				return "Same external source already exists." +
						"<p><strong>Name:</strong> " + error.getExtSource().getName() + "</br>" +
						"<strong>Type:</strong> " + error.getExtSource().getType();
			} else {
				return "Same external source already exists.";
			}

		} else if ("ExtSourceInUseException".equalsIgnoreCase(errorName)) {

			// TODO - ext source not in exception
			return "Selected external source is currently in use and can't be removed from VO or deleted.";

		} else if ("ExtSourceNotAssignedException".equalsIgnoreCase(errorName)) {

			// TODO - ext source not in exception
			return "Selected external source is not assigned to your VO and can't be removed.";

		} else if ("ExtSourceNotExistsException".equalsIgnoreCase(errorName)) {

			// TODO - better text ?? + ext source not in exception
			return "External source of this ID or name doesn't exists.";

		} else if ("ExtSourceUnsupportedOperationException".equalsIgnoreCase(errorName)) {

			// TODO - probably is never thrown to GUI ??
			return error.getMessage();

		} else if ("FacilityAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			return "Facility of the same name and type was already deleted.";

		} else if ("FacilityExistsException".equalsIgnoreCase(errorName)) {

			return "Facility of the same name and type already exists.";

		} else if ("FacilityNotExistsException".equalsIgnoreCase(errorName)) {

			return "Requested Facility (by id or name/type) doesn't exists.";

		} else if ("FacilityNotExistsException".equalsIgnoreCase(errorName)) {

			return "Requested Facility (by id or name/type) doesn't exists.";

		} else if ("GroupAlreadyAssignedException".equalsIgnoreCase(errorName)) {

			Group g = error.getGroup();
			if (g != null) {
				return "Group: " + g.getName() + " is already assigned to Resource.";
			} else {
				return "Group is already assigned to Resource.";
			}

		} else if ("GroupExistsException".equalsIgnoreCase(errorName)) {

			return "Group with same name already exists in your VO. Group names must be unique in VO.";

		} else if ("GroupAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			return "Same group was already removed from your VO/Group.";

		} else if ("GroupAlreadyRemovedFromResourceException".equalsIgnoreCase(errorName)) {

			return "Same group was already removed from resource.";

		} else if ("GroupNotDefinedOnResourceException".equalsIgnoreCase(errorName)) {

			return "Group is not assigned to Resource and therefore can't be removed from it.";

		} else if ("GroupNotExistsException".equalsIgnoreCase(errorName)) {

			return "Requested Group (by id or name/vo) doesn't exists.";

		} else if ("GroupResourceMismatchException".equalsIgnoreCase(errorName)) {

			return "Group and Resource doesn't belong to the same VO.";

		} else if ("GroupOperationsException".equalsIgnoreCase(errorName)) {

			return "Action is not permitted, since it violates group arithmetic rules.";

		} else if ("GroupRelationAlreadyExists".equalsIgnoreCase(errorName)) {

			return "Groups are already in a relation. Please refresh your view/table to see current state.";

		} else if ("GroupRelationCannotBeRemoved".equalsIgnoreCase(errorName)) {

			return "Relation can't be removed, since groups are in a direct hierarchy. If necessary, please delete the sub-group.";

		} else if ("GroupRelationDoesNotExist".equalsIgnoreCase(errorName)) {

			return "Groups are not in a relation. Please refresh your view/table to see current state.";

		} else if ("GroupRelationNotAllowed".equalsIgnoreCase(errorName)) {

			return "You can't add groups to relation. It would create a cycle.";

		} else if ("GroupSynchronizationAlreadyRunningException".equalsIgnoreCase(errorName)) {

			return "Can't start group synchronization between Perun and external source, because it's already running.";

		} else if ("HostAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			return "Same host was already removed from facility.";

		} else if ("HostExistsException".equalsIgnoreCase(errorName)) {

			// TODO - Facility object in this exception would really help
			return "Either same host already exists on Facility or you are trying to add more than one host to (v)host Facility type (can have only one).";

		} else if ("HostNotExistsException".equalsIgnoreCase(errorName)) {

			// TODO - host object is not filled on core side
			return "Requested Host (by name) doesn't exists.";

		} else if ("IllegalArgumentException".equalsIgnoreCase(errorName)) {

			// FIXME - is this generic error ??
			return "Your operation can't be completed. Illegal argument exception occurred. Please report this error.";

		} else if ("InternalErrorException".equalsIgnoreCase(errorName)) {

			// FIXME - is this generic error ??
			return translation.internalErrorException();

		} else if ("LoginNotExistsException".equalsIgnoreCase(errorName)) {

			// TODO - login + namespace should be in exception
			return "User doesn't have login set for selected namespace.";

		} else if ("MaxSizeExceededException".equalsIgnoreCase(errorName)) {

			// has meaningfull message
			return error.getMessage();

		} else if ("MemberAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			return "Member was already removed from group/VO.";

		} else if ("MemberNotAdminException".equalsIgnoreCase(errorName)) {

			// FIXME - will be removed in favor of UserNotAdminException
			return "Can't remove user from administrators (user is not an administrator).";

		} else if ("MemberNotExistsException".equalsIgnoreCase(errorName)) {

			return "Requested member (by id or ues) doesn't exists.";

		} else if ("MemberNotValidYetException".equalsIgnoreCase(errorName)) {

			return "Can't disable membership for VO member if not valid yet.";

		} else if ("MembershipMismatchException".equalsIgnoreCase(errorName)) {

			return "Can't add member to group. They are from different VOs.";

		} else if ("MessageParsingFailException".equalsIgnoreCase(errorName)) {

			return error.getMessage();

		} else if ("ModuleNotExistsException".equalsIgnoreCase(errorName)) {

			return "Module for virtual attribute doesn't exists. Please report this error.";

		} else if ("NotGroupMemberException".equalsIgnoreCase(errorName)) {

			return "Can't remove user from group. User already isn't group member.";

		} else if ("NotMemberOfParentGroupException".equalsIgnoreCase(errorName)) {

			return "Can't add user to this group. User must be member of parent group first.";

		} else if ("NotSpecificUserExpectedException".equalsIgnoreCase(errorName)) {

			return "Operation can't be done. Expected person type of user, but service type was provided instead.";

		} else if ("NumberNotInRangeException".equalsIgnoreCase(errorName)) {

			return error.getMessage();

		} else if ("NumbersNotAllowedException".equalsIgnoreCase(errorName)) {

			return error.getMessage();

		} else if ("OwnerAlreadyAssignedException".equalsIgnoreCase(errorName)) {

			return "Can't add owner to Facility. Owner is already assigned.";

		} else if ("OwnerAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			return "Can't remove owner from Facility. Owner is already removed.";

		} else if ("OwnerNotExistsException".equalsIgnoreCase(errorName)) {

			return "Requested Owner (by id) doesn't exists.";

		} else if ("ParentGroupNotExistsException".equalsIgnoreCase(errorName)) {

			return "Group doesn't have parent group.";

		} else if ("PasswordChangeFailedException".equalsIgnoreCase(errorName)) {

			return "Changing password failed due to an internal error. Please report it.";

		} else if ("PasswordCreationFailedException".equalsIgnoreCase(errorName)) {

			return "Password creation failed due to an internal error. Please report it.";

		} else if ("PasswordDeletionFailedException".equalsIgnoreCase(errorName)) {

			return "Password deletion failed due to an internal error. Please report it.";

		} else if ("PasswordDoesntMatchException".equalsIgnoreCase(errorName)) {

			return "Can't set new password. Old password doesn't match.";

		} else if ("PasswordStrengthFailedException".equalsIgnoreCase(errorName)) {

			if (PerunConfiguration.perunInstanceName().equals("VŠUP")) {
				return translation.passwordStrengthVSUP();
			}
			return "Used password doesn't match required strength constraints.";

		} else if ("PasswordOperationTimeoutException".equalsIgnoreCase(errorName)) {

			return "Operation with password exceeded expected time limit. Please try again.";

		} else if ("RelationExistsException".equalsIgnoreCase(errorName)) {

			// FIXME - better text on core side
			return error.getMessage();

		} else if ("RelationNotExistsException".equalsIgnoreCase(errorName)) {

			// FIXME - better text on core side
			return error.getMessage();

		} else if ("ResourceAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			return "Same resource was already removed from facility (deleted).";

		} else if ("ResourceNotExistsException".equalsIgnoreCase(errorName)) {

			return "Requested resource (by id) doesn't exists.";

		} else if ("ResourceTagAlreadyAssignedException".equalsIgnoreCase(errorName)) {

			// FIXME - must contain also resource
			return "Same tag is already assigned to resource.";

		} else if ("ResourceTagNotAssignedException".equalsIgnoreCase(errorName)) {

			// FIXME - must contain also resource
			return "Tag is not assigned to resource.";

		} else if ("ResourceTagNotExistsException".equalsIgnoreCase(errorName)) {

			// FIXME - must contain also resource
			return error.getMessage();

		} else if ("SecurityTeamAlreadyAssignedException".equalsIgnoreCase(errorName)) {

			/* - TODO
			if (error.getSecurityTeam() != null) {
				return "SecurityTeam <i>" + error.getSecurityTeam().getName() + "</i> is already assigned to facility.";
			} else {
				return "Same SecurityTeam is already assigned to facility.";
			}
			*/

		} else if ("ServiceAlreadyAssignedException".equalsIgnoreCase(errorName)) {

			// FIXME - must contain also resource
			if (error.getService() != null) {
				return "Service " + error.getService().getName() + " is already assigned to resource.";
			} else {
				return "Same service is already assigned to resource.";
			}

		} else if ("ServiceAlreadyBannedException".equalsIgnoreCase(errorName)) {

			if (error.getService() != null && error.getFacility() != null) {
				return "Service " + error.getService().getName() + " is already banned on facility "+error.getFacility().getName()+".";
			} else {
				return "Same service is already banned on facility.";
			}

		} else if ("ServiceExistsException".equalsIgnoreCase(errorName)) {

			if (error.getService() != null) {
				return "Service " + error.getService().getName() + " already exists in Perun. Choose different name.";
			} else {
				return "Service with same name already exists in Perun.";
			}

		} else if ("ServiceNotAssignedException".equalsIgnoreCase(errorName)) {

			// FIXME - must contain also resource
			if (error.getService() != null) {
				return "Service " + error.getService().getName() + " is not assigned to resource.";
			} else {
				return "Service is not assigned to resource.";
			}

		} else if ("ServiceAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			return "Same service was already deleted.";

		} else if ("ServiceNotExistsException".equalsIgnoreCase(errorName)) {

			return "Requested service (by id or name) doesn't exists.";

		} else if ("ServicesPackageExistsException".equalsIgnoreCase(errorName)) {

			// TODO - we don't support service packages yet
			return error.getMessage();

		} else if ("ServiceAlreadyRemovedFromServicePackageException".equalsIgnoreCase(errorName)) {

			return "Same service was already removed from service package.";

		} else if ("ServiceAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			return "Same service was already deleted.";

		} else if ("SpecificUserExpectedException".equalsIgnoreCase(errorName)) {

			return "Operation can't be done. Expected specific type of user, but person type was provided instead.";

		} else if ("SpecificUserAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			return "Same specific user was already removed from user.";

		} else if ("SpecificUserOwnerAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			return "Same user was already removed from owners of specific user.";

		} else if ("SpecificUserMustHaveOwnerException".equalsIgnoreCase(errorName)) {

			return "Specific type user must have at least 1 person type user assigned, which is responsible for it.";

		} else if ("SpaceNotAllowedException".equalsIgnoreCase(errorName)) {

			return error.getMessage();

		} else if ("SpecialCharsNotAllowedException".equalsIgnoreCase(errorName)) {

			return error.getMessage() + " You can use only letters, numbers and spaces.";

		} else if ("SpecialCharsNotAllowedException".equalsIgnoreCase(errorName)) {

			return error.getMessage() + " You can use only letters, numbers and spaces.";

		} else if ("SubGroupCannotBeRemovedException".equalsIgnoreCase(errorName)) {

			return "Subgroup can't be removed from resource. Only directly assigned groups can be removed.";

		} else if ("SubjectNotExistsException".equalsIgnoreCase(errorName)) {

			// FIXME - probably never thrown to GUI ?? + better exception text.
			return "Requested user by login in LDAP external source doesn't exists or more than one was found.";

		} else if ("UserExtSourceExistsException".equalsIgnoreCase(errorName)) {

			// TODO - user ext source object in exception
			return "Same user external identity already exists and is used by different user.";

		} else if ("UserExtSourceNotExistsException".equalsIgnoreCase(errorName)) {

			return "Requested user external identity doesn't exists.";

		} else if ("UserExtSourceAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			return "Same user's external identity was already removed from him/her.";

		} else if ("UserNotAdminException".equalsIgnoreCase(errorName)) {

			// FIXME - add vo, group or facility !!
			return "Can't remove user from managers of VO/Group/Facility. User is not manager.";

		} else if ("UserNotExistsException".equalsIgnoreCase(errorName)) {

			// TODO - get user from exception
			return "Requested user (by id or external identity) doesn't exists.";

		} else if ("UserAlreadyRemovedException".equalsIgnoreCase(errorName)) {

			// TODO - shoud contain user objects
			return "Same user was already deleted.";

		} else if ("VoExistsException".equalsIgnoreCase(errorName)) {

			return "VO with same name already exists. Please choose different name.";

		} else if ("VoNotExistsException".equalsIgnoreCase(errorName)) {

			// TODO - get vo from exception
			return "Requested VO (by id or name) doesn't exists.";

		} else if ("WrongModuleTypeException".equalsIgnoreCase(errorName)) {

			return error.getMessage();

		} else if ("WrongRangeOfCountException".equalsIgnoreCase(errorName)) {

			return error.getMessage();

		} else if ("WrongPatternException".equalsIgnoreCase(errorName)) {

			// meaningful message
			return error.getMessage();

		} else if ("MissingRequiredDataException".equalsIgnoreCase(errorName)) {

			String result = "Your IDP doesn't provide all required data for this application form. Please contact your IDP to resolve this issue or log-in using different IDP.";

			String missingItems = "<p>";
			for (ApplicationFormItemData item : error.getFormItems()) {
				missingItems += "<strong>Missing attribute: </strong>";
				missingItems += item.getFormItem().getFederationAttribute();
				missingItems += "<br />";
			}

			result += missingItems;

			return result;

		} else if ("RequestTimeout".equalsIgnoreCase(errorName)) {

			return translation.requestTimeout();

		}


		//default text
		return errorName + ": " + error.getMessage();

	}

}
