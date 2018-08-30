package cz.metacentrum.perun.wui.setAffiliation.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;
import cz.metacentrum.perun.wui.model.beans.Attribute;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

/**
 * Class with utility methods
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public class PerunSetAffiliationUtils {

	public static final String EDU_PERSON_SCOPED_AFFILIATIONS_MANUALLY_ASSIGNED_ATTR = "urn:perun:user:attribute-def:def:eduPersonScopedAffiliationsManuallyAssigned";
	public static final String AUTHORIZED_AFFILIATION_SCOPES_ATTR = "urn:perun:user:attribute-def:def:authorizedAffiliationScopes";

	/**
	 * Create EDU_PERSON_SCOPED_AFFILIATIONS_MANUALLY_ASSIGNED_ATTR
	 * @param attr attribute
	 * @param member member affiliation
	 * @param faculty faculty affiliation
	 * @param affiliate affiliate affiliation
	 * @param organization organization to be affiliated with
	 * @return created attribute
	 */
	public static Attribute createAssignedAffiliationsAttribute(Attribute attr, boolean member, boolean faculty,
																boolean affiliate, String organization ) {
		JavaScriptObject value = attr.getValueAsJso();
		// format has to match the format in Perun attribute module for
		// urn:perun:user:attribute-def:def:eduPersonScopedAffiliationsManuallyAssigned
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
		long expiration = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(365L);
		Date date = new Date(expiration);

		attr.setValueAsJso(updateAffiliations(value, organization, member, faculty, affiliate, dateFormat.format(date)));

		return attr;
	}

	/**
	 * Update affiliations attribute
	 */
	public static native JavaScriptObject updateAffiliations(JavaScriptObject jso, String organization, boolean member,
															 boolean faculty, boolean affiliate, String date)
	/*-{
        if (jso === undefined || jso === null) {
            jso = {};
        }

        delete jso["member@" + organization];
        delete jso["faculty@" + organization];
        delete jso["affiliate@" + organization];

        if (member) {
            jso["member@" + organization] = date;
        }
        if (faculty) {
            jso["faculty@" + organization] = date;
        }
        if (affiliate) {
            jso["affiliate@" + organization] = date;
        }

        return jso;
    }-*/;

}
