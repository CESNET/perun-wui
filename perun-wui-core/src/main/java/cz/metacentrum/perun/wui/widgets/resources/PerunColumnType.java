package cz.metacentrum.perun.wui.widgets.resources;

/**
 * Enum identifying column type. It's related to standard and non-standard PerunBeans properties.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public enum PerunColumnType {

	// common columns
	ID,
	NAME,
	DESCRIPTION,

	CREATED_AT,
	CREATED_BY,
	MODIFIED_AT,
	MODIFIED_BY,

	//user columns
	USER_ORGANIZATION,
	USER_EMAIL,
	USER_LOGIN,
	USER_TYPE,

	//attributes
	ATTR_NAME,
	ATTR_TYPE,
	ATTR_DEF,
	ATTR_ENTITY,
	ATTR_URN,
	ATTR_FRIENDLY_NAME,
	ATTR_VALUE,

	// vo columns
	VO_SHORT_NAME,

	// facility columns
	FACILITY_OWNERS,

	//ext source columns
	EXT_SOURCE_TYPE,

	//owner columns
	OWNER_CONTACT,
	OWNER_TYPE,

	// application columns
	APPLICATION_TYPE,
	APPLICATION_STATE,
	APPLICATION_VO_NAME,
	APPLICATION_GROUP_NAME,
	APPLICATION_USER,
	APPLICATION_DETAIL,
	APPLICATION_LOA,

	//publication columns
	PUBLICATION_AUTHORS,
	PUBLICATION_YEAR,
	PUBLICATION_THANKS,
	PUBLICATION_LOCK,
	PUBLICATION_CITE,

	// resource column
	FACILITY_NAME,
}
