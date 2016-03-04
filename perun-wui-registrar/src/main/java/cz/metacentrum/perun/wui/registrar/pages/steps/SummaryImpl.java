package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class SummaryImpl implements Summary{

    private Queue<Result> results;
    private RegistrarObject registrar;

    public SummaryImpl(RegistrarObject registrar) {
        this.results = new LinkedList<>();
        this.registrar = registrar;
    }


    public void addResult(Result result) {
        results.add(result);
    }

    public String mustRevalidateEmail() {
        for(Result result : results) {
            if (result.getRegisteredMail() != null) {
                return result.getRegisteredMail();
            }
        }
        return null;
    }

    public boolean containsGroupResult() {
        for (Result result : results) {
            if (result.getType().equals(FormStep.Type.GROUP)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsVoExtResult() {
        for (Result result : results) {
            if (result.getType().equals(FormStep.Type.VO_EXT)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsVoInitResult() {
        for (Result result : results) {
            if (result.getType().equals(FormStep.Type.VO_INIT)) {
                return true;
            }
        }
        return false;
    }

    public Result getGroupResult() {
        for (Result result : results) {
            if (result.getType().equals(FormStep.Type.GROUP)) {
                return result;
            }
        }
        return null;
    }

    public Result getVoExtResult() {
        for (Result result : results) {
            if (result.getType().equals(FormStep.Type.VO_EXT)) {
                return result;
            }
        }
        return null;
    }

    public Result getVoInitResult() {
        for (Result result : results) {
            if (result.getType().equals(FormStep.Type.VO_INIT)) {
                return result;
            }
        }
        return null;
    }

    @Override
    public boolean alreadyAppliedToVo() {
        if (registrar.getVoFormInitialException() == null) {
            return false;
        }
        if (registrar.getVoFormInitialException().getName().equals("DuplicateRegistrationAttemptException")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean alreadyAppliedToGroup() {
        if (registrar.getGroupFormInitialException() == null) {
            return false;
        }
        if (registrar.getGroupFormInitialException().getName().equals("DuplicateRegistrationAttemptException")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean alreadyAppliedForExtension() {
        if (registrar.getVoFormExtensionException() == null) {
            return false;
        }
        if (registrar.getVoFormExtensionException().getName().equals("DuplicateRegistrationAttemptException")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean alreadyMemberOfVo() {
        if (registrar.getVoFormInitialException() == null) {
            return false;
        }
        if (registrar.getVoFormInitialException().getName().equals("AlreadyRegisteredException")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean alreadyMemberOfGroup() {
        if (registrar.getGroupFormInitialException() == null) {
            return false;
        }
        if (registrar.getGroupFormInitialException().getName().equals("AlreadyRegisteredException")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }

}
