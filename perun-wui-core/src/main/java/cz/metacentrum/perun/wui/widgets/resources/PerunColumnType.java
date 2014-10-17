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

	// vo columns
	SHORT_NAME,

	// facility columns
	OWNERS,

	// application columns
	TYPE,
	STATE,
	CREATED_AT,
	MODIFIED_BY,
	VO_NAME,
	GROUP_NAME

}
