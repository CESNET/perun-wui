package cz.metacentrum.perun.wui.model.resources;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.GeneralObject;
import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.model.beans.Facility;
import cz.metacentrum.perun.wui.model.beans.Owner;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Comparator for any Perun's object - it makes a GeneralObject from them.
 *
 * @author Vaclav Mach <374430@mail.muni.cz>
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunComparator<T extends JavaScriptObject> implements Comparator<T> {

	private PerunColumnType column;

	/**
	 * Creates a new Comparator with specified column/property to sort by
	 *
	 * @param column
	 */
	public PerunComparator(PerunColumnType column) {
		this.column = column;
	}

	/**
	 * Safely compares two strings using browser's locale settings.
	 *
	 * @param o1 string to compare
	 * @param o2 string to compare with
	 * @return comparison result used in comparators
	 */
	public static final native int nativeCompare(String o1, String o2) /*-{
        if (o1 == null) return -1;
        if (o2 == null) return 1;
        return o1.localeCompare(o2);
    }-*/;

	/**
	 * Return Comparator<String> which uses browser's localeCompare().
	 *
	 * @return localized Comparator<String>
	 */
	public static final Comparator<String> getNativeComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (o1 == null) return -1;
				if (o2 == null) return 1;
				return nativeCompare(o1, o2);
			}
		};
	}

	/**
	 * Compares the two objects
	 *
	 * @param obj1 First object
	 * @param obj2 Second object
	 */
	@Override
	public int compare(T obj1, T obj2) {

		if (obj1 == null) return -1;
		if (obj2 == null) return 1;

		GeneralObject o1 = obj1.cast();
		GeneralObject o2 = obj2.cast();

		if (PerunColumnType.ID.equals(this.column)) return this.compareById(o1, o2);
		if (PerunColumnType.NAME.equals(this.column)) return this.compareByName(o1, o2);
		if (PerunColumnType.DESCRIPTION.equals(this.column)) return this.compareByDescription(o1, o2);

		if (PerunColumnType.CREATED_AT.equals(this.column)) return this.compareByCreatedAt(o1, o2);
		if (PerunColumnType.MODIFIED_AT.equals(this.column)) return this.compareByModifiedAt(o1, o2);
		if (PerunColumnType.CREATED_BY.equals(this.column)) return this.compareByCreatedBy(o1, o2);
		if (PerunColumnType.MODIFIED_BY.equals(this.column)) return this.compareByModifiedBy(o1, o2);

		// VO columns
		if (PerunColumnType.VO_SHORT_NAME.equals(this.column)) return this.compareByShortName(o1, o2);

		// Facility columns
		if (PerunColumnType.FACILITY_OWNERS.equals(this.column)) return this.compareByOwnersNames(o1, o2);

		// Application columns
		if (PerunColumnType.APPLICATION_USER.equals(this.column)) return this.compareByApplicationUser(o1, o2);
		if (PerunColumnType.APPLICATION_STATE.equals(this.column)) return this.compareByApplicationState(o1, o2);
		if (PerunColumnType.APPLICATION_TYPE.equals(this.column)) return this.compareByApplicationType(o1, o2);
		if (PerunColumnType.APPLICATION_VO_NAME.equals(this.column)) return this.compareByApplicationVoName(o1, o2);
		if (PerunColumnType.APPLICATION_GROUP_NAME.equals(this.column)) return this.compareByApplicationGroupName(o1, o2);

		return 0;

	}

	/**
	 * Compares GeneralObjects by ID.
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareById(GeneralObject o1, GeneralObject o2) {
		return o1.getId() - o2.getId();
	}

	/**
	 * Compares GeneralObjects by the name
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByName(GeneralObject o1, GeneralObject o2) {
		return nativeCompare(o1.getName(), o2.getName());
	}

	/**
	 * Compares GeneralObjects by the description
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByDescription(GeneralObject o1, GeneralObject o2) {
		return nativeCompare(o1.getDescription(), o2.getDescription());
	}

	/**
	 * Compares VirtualOrganizations by ShortName
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByShortName(GeneralObject o1, GeneralObject o2) {

		Vo vo1 = o1.cast();
		Vo vo2 = o2.cast();
		return nativeCompare(vo1.getShortName(), vo2.getShortName());

	}

	/**
	 * Compares Facilities by their Owner's names (only technical)
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByOwnersNames(GeneralObject o1, GeneralObject o2) {

		Facility facility1 = o1.cast();
		Facility facility2 = o2.cast();

		ArrayList<String> result = new ArrayList<>();
		for (Owner o : facility1.getOwners()) {
			if (o.getType().equals("technical")) result.add(o.getName());
		}

		ArrayList<String> result2 = new ArrayList<>();
		for (Owner o : facility2.getOwners()) {
			if (o.getType().equals("technical")) result2.add(o.getName());
		}

		Collections.sort(result, getNativeComparator());
		Collections.sort(result2, getNativeComparator());

		return nativeCompare(Utils.join(result, ", "), Utils.join(result2, ", "));

	}

	/**
	 * Compares PerunBeans by createdAt
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByCreatedAt(GeneralObject o1, GeneralObject o2) {

		if (o1.getObjectType().equals("Application") && o2.getObjectType().equals("Application")) {

			Application app1 = o1.cast();
			Application app2 = o2.cast();
			return nativeCompare(app1.getCreatedAt(), app2.getCreatedAt());

		} else {

			return nativeCompare(o1.getCreatedAt(), o2.getCreatedAt());

		}

	}

	/**
	 * Compares PerunBeans by modifiedAt
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByModifiedAt(GeneralObject o1, GeneralObject o2) {

		if (o1.getObjectType().equals("Application") && o2.getObjectType().equals("Application")) {

			Application app1 = o1.cast();
			Application app2 = o2.cast();
			return nativeCompare(app1.getModifiedAt(), app2.getModifiedAt());

		} else {

			return nativeCompare(o1.getModifiedAt(), o2.getModifiedAt());

		}

	}

	/**
	 * Compares PerunBeans by createdBy
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByCreatedBy(GeneralObject o1, GeneralObject o2) {

		if (o1.getObjectType().equals("Application") && o2.getObjectType().equals("Application")) {

			Application app1 = o1.cast();
			Application app2 = o2.cast();
			return nativeCompare(app1.getCreatedBy(), app2.getCreatedBy());

		} else {

			return nativeCompare(o1.getCreatedBy(), o2.getCreatedBy());

		}

	}

	/**
	 * Compares PerunBeans by modifiedBy
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByModifiedBy(GeneralObject o1, GeneralObject o2) {

		if (o1.getObjectType().equals("Application") && o2.getObjectType().equals("Application")) {

			Application app1 = o1.cast();
			Application app2 = o2.cast();
			return nativeCompare(Utils.convertCertCN(app1.getModifiedBy()), Utils.convertCertCN(app2.getModifiedBy()));

		} else {

			return nativeCompare(o1.getModifiedBy(), o2.getModifiedBy());

		}

	}

	/**
	 * Compares Application by user name or ext source login and ext source name.
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByApplicationUser(GeneralObject o1, GeneralObject o2) {

		Application app1 = o1.cast();
		Application app2 = o2.cast();

		String compare1 = "";
		String compare2 = "";
		if (app1.getUser() != null) {
			compare1 = app1.getUser().getFullName();
		} else {
			compare1 = Utils.convertCertCN(app1.getCreatedBy()) + " / " + Utils.translateIdp(Utils.convertCertCN(app1.getExtSourceName()));
		}
		if (app2.getUser() != null) {
			compare2 = app2.getUser().getFullName();
		} else {
			compare2 = Utils.convertCertCN(app2.getCreatedBy()) + " / " + Utils.translateIdp(Utils.convertCertCN(app2.getExtSourceName()));
		}

		return nativeCompare(compare1, compare2);

	}

	/**
	 * Compares Application by state
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByApplicationState(GeneralObject o1, GeneralObject o2) {

		Application app1 = o1.cast();
		Application app2 = o2.cast();

		return app1.getState().compareTo(app2.getState());

	}

	/**
	 * Compares Application by type
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByApplicationType(GeneralObject o1, GeneralObject o2) {

		Application app1 = o1.cast();
		Application app2 = o2.cast();

		return app1.getType().compareTo(app2.getType());

	}

	/**
	 * Compares Application by VO name
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByApplicationVoName(GeneralObject o1, GeneralObject o2) {

		Application app1 = o1.cast();
		Application app2 = o2.cast();

		String compare1 = "";
		String compare2 = "";
		if (app1.getVo() != null) {
			compare1 = app1.getVo().getName();
		}
		if (app2.getVo() != null) {
			compare2 = app2.getVo().getName();
		}

		return nativeCompare(compare1, compare2);

	}

	/**
	 * Compares Application by Group name
	 *
	 * @param o1
	 * @param o2
	 * @return
	 */
	private int compareByApplicationGroupName(GeneralObject o1, GeneralObject o2) {

		Application app1 = o1.cast();
		Application app2 = o2.cast();

		String compare1 = "";
		String compare2 = "";
		if (app1.getGroup() != null) {
			compare1 = app1.getGroup().getShortName();
		}
		if (app2.getGroup() != null) {
			compare2 = app2.getGroup().getShortName();
		}

		return nativeCompare(compare1, compare2);

	}

}