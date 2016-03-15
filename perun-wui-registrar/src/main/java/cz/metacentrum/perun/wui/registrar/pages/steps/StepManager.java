package cz.metacentrum.perun.wui.registrar.pages.steps;

/**
 * Manager of steps. Calling them and providing them neccessary data and resolve their Results.
 *
 * Example usage:
 *
 * <pre>
 * stepManager.addStep(new VoInitStep(registrar, form));
 * stepManager.addStep(new GroupInitStep(registrar, form));
 * stepManager.addStep(new SummaryStep(formView));
 * stepManager.begin();
 * </pre>
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface StepManager {

    /**
     * Add step to the end.
     *
     * @param step step to be added.
     */
    void addStep(Step step);

    /**
     * Start calling steps. Call first step. Steps shouldn't be empty.
     */
    void begin();

}
