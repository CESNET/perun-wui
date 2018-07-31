package cz.metacentrum.perun.wui.model.beans;

import com.google.gwt.json.client.JSONObject;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.model.GeneralObject;

/**
 * Overlay type for Member object from Perun
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Member extends GeneralObject {

	protected Member() {
	}

	/**
	 * Return new instance of Member with basic properties set.
	 * Sets sponsored to False
	 *
	 * @param id               ID of Member
	 * @param voId             ID of VO member is associated with
	 * @param userId           ID of user member is associated with
	 * @param voId             ID of VO this group belongs to
	 * @param membershipType   type of Membership
	 * @param membershipStatus status of Membership
	 * @return Member object
	 */
	public static final Member createNew(int id, int voId, int userId, String membershipType, String membershipStatus) {
		return createNew(id, voId, userId, membershipType, membershipStatus, false);
	}

	/**
	 * Return new instance of Member with basic properties set.
	 *
	 * @param id               ID of Member
	 * @param voId             ID of VO member is associated with
	 * @param userId           ID of user member is associated with
	 * @param voId             ID of VO this group belongs to
	 * @param membershipType   type of Membership
	 * @param membershipStatus status of Membership
	 * @param sponsored
	 * @return Member object
	 */
	public static final Member createNew(int id, int voId, int userId, String membershipType, String membershipStatus, boolean sponsored) {
		Member member = new JSONObject().getJavaScriptObject().cast();
		member.setId(id);
		member.setVoId(voId);
		member.setUserId(userId);
		member.setMembershipType(membershipType);
		member.setMembershipStatus(membershipStatus);
		member.setSponsored(sponsored);
		member.setObjectType("Member");
		return member;
	}

	/**
	 * Sets ID to Member
	 *
	 * @param id Id to set
	 */
	public final native void setId(int id) /*-{
        this.id = id;
    }-*/;

	/**
	 * Get ID of Member's VO
	 *
	 * @return ID of Member's VO
	 */
	public final int getVoId() {
		return JsUtils.getNativePropertyInt(this, "voId");
	}

	/**
	 * Sets ID of VO to Member which is associated with
	 *
	 * @param id Id to set
	 */
	public final native void setVoId(int id) /*-{
        this.voId = id;
    }-*/;

	/**
	 * Get ID of User this Member is associated with
	 *
	 * @return ID of User associated with member
	 */
	public final int getUserId() {
		return JsUtils.getNativePropertyInt(this, "userId");
	}

	/**
	 * Sets ID of User to Member which is associated with
	 *
	 * @param id Id to set
	 */
	public final native void setUserId(int id) /*-{
        this.userId = id;
    }-*/;

	/**
	 * Get membership type (context is associated on member's retrieval)
	 * <p/>
	 * DIRECT - user is member of group/vo it was retrieved for
	 * INDIRECT - user is member of any subgroup of group it was retrieved for
	 * NOT_DEFINED - context can't be determined
	 *
	 * @return membership type (DIRECT, INDIRECT, NOT_DEFINED, ....)
	 */
	public final String getMembershipType() {
		String type = JsUtils.getNativePropertyString(this, "membershipType");
		if (type == null) return "NOT_DETERMINED";
		return type;
	}

	/**
	 * Set membership type
	 * <p/>
	 * DIRECT - is member of group/vo it was retrieved for
	 * INDIRECT - is member of any subgroup of group it was retrieved for
	 * NOT_DEFINED - context can't be determined
	 *
	 * @param type (DIRECT, INDIRECT, NOT_DEFINED, ....)
	 */
	public final native void setMembershipType(String type) /*-{
        this.membershipType = type;
    }-*/;

	/**
	 * Return membership status of Member in VO
	 * <p/>
	 * VALID - member is properly configured and pushed to VO resources/services
	 * INVALID - member is not correctly configured and is NOT pushed to VO resources/services
	 * SUSPENDED - member violated any of VO rules and account is suspended (NOT pushed)
	 * EXPIRED - membership expired (pushing depends on each service)
	 * DISABLED - member is not active for a long time (NO pushing, no search results,...)
	 *
	 * @return string which defines item status
	 */
	public final String getMembershipStatus() {
		return JsUtils.getNativePropertyString(this, "status");
	}

	/**
	 * Sets membership status of Member in VO
	 *
	 * @param status String which defines member's status:
	 *               <p/>
	 *               VALID - member is properly configured and pushed to VO resources/services
	 *               INVALID - member is not correctly configured and is NOT pushed to VO resources/services
	 *               SUSPENDED - member violated any of VO rules and account is suspended (NOT pushed)
	 *               EXPIRED - membership expired (pushing depends on each service)
	 *               DISABLED - member is not active for a long time (NO pushing, no search results,...)
	 */
	public final native void setMembershipStatus(String status) /*-{
        this.status = status;
    }-*/;

	/**
	 * Is sponsored
	 * @return TRUE if is sponsored, FALSE otherwise
	 */
	public final boolean isSponsored(){
    	return JsUtils.getNativePropertyBoolean(this, "sponsored");
    }

	/**
	 * Set sponsored
	 * @param sponsored
	 */
	public final native void setSponsored(boolean sponsored)/*-{
    	this.sponsored = sponsored;
	}-*/;

	/**
	 * Compares to another object
	 *
	 * @param o Object to compare
	 * @return true, if they are the same
	 */
	public final boolean equals(Member o) {
		return o.getId() == this.getId();
	}

}