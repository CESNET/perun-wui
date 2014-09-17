package cz.metacentrum.perun.wui.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.model.resources.PerunComparator;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class used for sorting list of different types of objects.
 *
 * @author Pavel Zlamal <zlamal@cesnet.cz>
 */
public class TableSorter {

	/**
	 * Returns sorted list of objects by their IDs (ascending)
	 *
	 * @param list    of objects to be sorted
	 * @param reverse TRUE if sorting should be descending
	 * @return ArrayList<T> sorted list of objects by their IDs
	 */
	public static <T extends JavaScriptObject> ArrayList<T> sortById(ArrayList<T> list, boolean reverse) {
		if (list == null) return null;
		if (reverse) {
			Collections.sort(list, Collections.reverseOrder(new PerunComparator<T>(PerunColumnType.ID)));
		} else {
			Collections.sort(list, new PerunComparator<T>(PerunColumnType.ID));
		}
		return list;
	}

	/**
	 * Returns sorted list of objects by their Names (ascending)
	 * <p/>
	 * Name in this case is value returned by GeneralObject.getName()
	 * which can be made from different parameters.
	 *
	 * @param list    of objects to be sorted
	 * @param reverse TRUE if sorting should be descending
	 * @return ArrayList<T> sorted list of objects by their Names
	 */
	public static <T extends JavaScriptObject> ArrayList<T> sortByName(ArrayList<T> list, boolean reverse) {
		if (list == null) return null;
		if (reverse) {
			Collections.sort(list, Collections.reverseOrder(new PerunComparator<T>(PerunColumnType.NAME)));
		} else {
			Collections.sort(list, new PerunComparator<T>(PerunColumnType.NAME));
		}
		return list;
	}

	/**
	 * Returns sorted list of objects by their Description
	 * <p/>
	 * Description in this case is value returned by GeneralObject.getDescription()
	 *
	 * @param list    of objects to be sorted
	 * @param reverse TRUE if sorting should be descending
	 * @return ArrayList<T> sorted list of objects by their Description
	 */
	public static <T extends JavaScriptObject> ArrayList<T> sortByDescription(ArrayList<T> list, boolean reverse) {
		if (list == null) return null;
		if (reverse) {
			Collections.sort(list, Collections.reverseOrder(new PerunComparator<T>(PerunColumnType.DESCRIPTION)));
		} else {
			Collections.sort(list, new PerunComparator<T>(PerunColumnType.DESCRIPTION));
		}
		return list;
	}

	/**
	 * Returns sorted list of VOs by their ShortName
	 *
	 * @param list    of VOs to be sorted
	 * @param reverse TRUE if sorting should be descending
	 * @return ArrayList<T> sorted list of VOs by their shortName
	 */
	public static <T extends JavaScriptObject> ArrayList<T> sortByShortName(ArrayList<T> list, boolean reverse) {
		if (list == null) return null;
		if (reverse) {
			Collections.sort(list, Collections.reverseOrder(new PerunComparator<T>(PerunColumnType.SHORT_NAME)));
		} else {
			Collections.sort(list, new PerunComparator<T>(PerunColumnType.SHORT_NAME));
		}

		return list;
	}

	/**
	 * Returns sorted list of Facilities by their Owner's names (only technical)
	 *
	 * @param list    of Facilities to be sorted
	 * @param reverse TRUE if sorting should be descending
	 * @return ArrayList<T> sorted list of Facilities by their owner's names
	 */
	public static <T extends JavaScriptObject> ArrayList<T> sortByOwnersNames(ArrayList<T> list, boolean reverse) {
		if (list == null) return null;
		if (reverse) {
			Collections.sort(list, Collections.reverseOrder(new PerunComparator<T>(PerunColumnType.OWNERS)));
		} else {
			Collections.sort(list, new PerunComparator<T>(PerunColumnType.OWNERS));
		}

		return list;
	}

}
