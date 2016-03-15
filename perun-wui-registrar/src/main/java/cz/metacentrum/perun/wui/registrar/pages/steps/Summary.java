package cz.metacentrum.perun.wui.registrar.pages.steps;

/**
 * Envelope around collection of Results.
 * Provides methods to interpret data from results.
 * Feel free to add more methods.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface Summary {

    void addResult(Result result);

    boolean containsGroupResult();

    boolean containsVoExtResult();

    boolean containsVoInitResult();

    Result getGroupResult();

    Result getVoExtResult();

    Result getVoInitResult();

    boolean alreadyAppliedToVo();

    boolean alreadyAppliedToGroup();

    boolean alreadyAppliedForExtension();

    boolean alreadyMemberOfVo();

    boolean alreadyMemberOfGroup();

    boolean isEmpty();

    String mustRevalidateEmail();

}
