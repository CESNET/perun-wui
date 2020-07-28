package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.model.beans.Application;

/**
 * Envelope around collection of Results.
 * Provides methods to interpret data from results.
 * Feel free to add more methods.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface Summary {

	void addResult(Result result);

	boolean containsGroupInitResult();

	boolean containsGroupExtResult();

	boolean containsVoExtResult();

	boolean containsVoInitResult();

	Result getGroupInitResult();

	Result getGroupExtResult();

	Result getVoExtResult();

	Result getVoInitResult();

	boolean alreadyAppliedToVo();

	boolean alreadyAppliedToGroup();

	boolean alreadyAppliedForVoExtension();

	boolean alreadyAppliedForGroupExtension();

	boolean alreadyMemberOfVo();

	boolean alreadyMemberOfGroup();

	boolean isEmpty();

	String mustRevalidateEmail();

	Application getApplication();

}
