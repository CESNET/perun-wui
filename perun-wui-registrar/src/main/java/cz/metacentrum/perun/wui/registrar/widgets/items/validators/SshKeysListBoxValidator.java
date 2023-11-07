package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.registrar.widgets.items.ListBox;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Validator for ListBox
 *
 * @author Jakub Hejda <Jakub.Hejda@cesnet.cz>
 */
public class SshKeysListBoxValidator extends ListBoxValidator {

	final Map<String, ValidationState> checkVals = new TreeMap<>();
	final Map<String, Integer> indexMap = new HashMap<>();
	int validatingCounter = 0;
	boolean callFailed = false;

	@Override
	public boolean validateLocal(ListBox listBox) {
		if (listBox.isRequired() && isNullOrEmpty(listBox.getValue())) {
			setResult(Result.EMPTY);
			listBox.setRawStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}
		// FIXME - but it is probably not necessary as API should check it
		for (ExtendedTextBox extendedTextBox : listBox.getListValue()) {
			String sshKey = extendedTextBox.getValue();
			if (sshKey.contains(",")) {
				setResult(Result.INVALID_FORMAT);
				listBox.setStatus(getTransl().sshKeySeparatorNotAllowed(), ValidationState.ERROR);
				return false;
			}
		}
		listBox.setStatus(ValidationState.SUCCESS);
		return true;
	}

	@Override
	public void validate(ListBox listBox, Events<Boolean> events) {

		if (!validateLocal(listBox)) {
			events.onFinished(false);
			return;
		}

		if (listBox.getValue() == null || listBox.getValue().isEmpty()) {
			events.onFinished(true);
			return;
		}

		checkVals.clear();
		indexMap.clear();
		callFailed = false;

		int counter = 1;
		for (ExtendedTextBox extendedTextBox : listBox.getListValue()) {
			String sshKey = extendedTextBox.getValue();
			indexMap.put(sshKey, counter);
			checkVals.put(sshKey, ValidationState.NONE);
			counter++;
		}

		for (String sshKey : checkVals.keySet()) {

			UsersManager.validateSSHKey(sshKey, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					validatingCounter--;
					checkVals.put(sshKey, ValidationState.SUCCESS);
					if (validatingCounter == 0) {
						// last check trigger events
						for (ValidationState state : checkVals.values()) {
							// at least one key is invalid -> switch to error if API call didn't fail
							if (ValidationState.ERROR == state) {
								if (!callFailed) {
									setResult(Result.INVALID_FORMAT);
									listBox.setRawStatus(getTransl().incorrectFormatItemList() + " <b> <br>" + checkVals.entrySet().stream().filter(entry -> (entry.getValue() == ValidationState.ERROR)).map((entry) -> indexMap.get(entry.getKey()) + ". " +
										(entry.getKey().length() > 25 ? entry.getKey().substring(0, 23) + "..." : entry.getKey())).collect(Collectors.joining("<br>")) + "</b>", ValidationState.ERROR);
								}
								// pass to outer event as fail
								events.onFinished(false);
								return;
							}
						}
						// all values were OK -> trigger success
						listBox.setStatus(ValidationState.SUCCESS);
						events.onFinished(true);
					}
				}

				@Override
				public void onError(PerunException error) {
					if (!"SSHKeyNotValidException".equalsIgnoreCase(error.getName())) {
						callFailed = true;
					}
					validatingCounter--;
					checkVals.put(sshKey, ValidationState.ERROR);
					// set error immediately
					if (callFailed) {
						setResult(Result.CANT_CHECK_SSH);
						listBox.setStatus(getTransl().checkingSSHFailed(), ValidationState.ERROR);
					} else {
						setResult(Result.INVALID_FORMAT);
						listBox.setRawStatus(getTransl().incorrectFormatItemList() + " <b> <br>" + checkVals.entrySet().stream().filter(entry -> (entry.getValue() == ValidationState.ERROR)).map((entry) -> indexMap.get(entry.getKey()) + ". " +
							(entry.getKey().length() > 25 ? entry.getKey().substring(0, 23) + "..." : entry.getKey())).collect(Collectors.joining("<br>")) + "</b>", ValidationState.ERROR);
					}
					if (validatingCounter == 0) {
						// if last pass to outer event as fail
						events.onFinished(false);
					}
				}

				@Override
				public void onLoadingStart() {
					if (validatingCounter == 0) {
						listBox.unsetStatus();
						events.onLoadingStart();
						setResult(Result.CHECKING_SSH);
						listBox.setStatus(ValidationState.WARNING);
					}
					validatingCounter++;
				}
			});

		}
	}
}
