package cz.metacentrum.perun.wui.registrar.pages.steps;

import cz.metacentrum.perun.wui.model.beans.Application;
import cz.metacentrum.perun.wui.registrar.model.RegistrarObject;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class SummaryImpl implements Summary {

	private Queue<Result> results;
	private RegistrarObject registrar;

	public SummaryImpl(RegistrarObject registrar) {
		this.results = new LinkedList<>();
		this.registrar = registrar;
	}

	@Override
	public Application getApplication() {
		for (Result result : results) {
			if (result.getApplication() != null) {
				return result.getApplication();
			}
		}
		return null;
	}

	@Override
	public void addResult(Result result) {
		results.add(result);
	}

	@Override
	public String mustRevalidateEmail() {
		for(Result result : results) {
			if (result.getRegisteredMail() != null &&
				!result.getApplication().getState().equals(Application.ApplicationState.VERIFIED) &&
				!result.getApplication().getState().equals(Application.ApplicationState.APPROVED)) {
				return result.getRegisteredMail();
			}
		}
		return null;
	}

	@Override
	public boolean containsGroupInitResult() {
		for (Result result : results) {
			if (result.getType().equals(FormStep.Type.GROUP_INIT)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsGroupExtResult() {
		for (Result result : results) {
			if (result.getType().equals(FormStep.Type.GROUP_EXT)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsVoExtResult() {
		for (Result result : results) {
			if (result.getType().equals(FormStep.Type.VO_EXT)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsVoInitResult() {
		for (Result result : results) {
			if (result.getType().equals(FormStep.Type.VO_INIT)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Result getGroupInitResult() {
		for (Result result : results) {
			if (result.getType().equals(FormStep.Type.GROUP_INIT)) {
				return result;
			}
		}
		return null;
	}

	@Override
	public Result getGroupExtResult() {
		for (Result result : results) {
			if (result.getType().equals(FormStep.Type.GROUP_EXT)) {
				return result;
			}
		}
		return null;
	}

	@Override
	public Result getVoExtResult() {
		for (Result result : results) {
			if (result.getType().equals(FormStep.Type.VO_EXT)) {
				return result;
			}
		}
		return null;
	}

	@Override
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
	public boolean alreadyAppliedForVoExtension() {
		if (registrar.getVoFormExtensionException() == null) {
			return false;
		}
		if (registrar.getVoFormExtensionException().getName().equals("DuplicateRegistrationAttemptException") ||
				registrar.getVoFormExtensionException().getName().equals("DuplicateExtensionAttemptException")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean alreadyAppliedForGroupExtension() {
		if (registrar.getGroupFormExtensionException() == null) {
			return false;
		}
		if (registrar.getGroupFormExtensionException().getName().equals("DuplicateRegistrationAttemptException") ||
				registrar.getGroupFormExtensionException().getName().equals("DuplicateExtensionAttemptException")) {
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
