package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.registrar.widgets.items.MapBox;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Validator for MapBox
 *
 * @author Jakub Hejda <Jakub.Hejda@cesnet.cz>
 */
public class MapBoxValidator extends PerunFormItemValidatorImpl<MapBox>{
	@Override
	public boolean validateLocal(MapBox mapBox) {
		if (mapBox.isRequired() && isNullOrEmpty(mapBox.getValue())) {
			setResult(Result.EMPTY);
			mapBox.setRawStatus(getTransl().cantBeEmpty(), ValidationState.ERROR);
			return false;
		}

		Set<String> checkSet = new HashSet<>();
		for (ExtendedTextBox box : mapBox.getKeys()) {
			if (!checkSet.add(box.getValue())) {
				setResult(Result.DUPLICATE_KEYS);
				mapBox.setStatus(getTransl().duplicateKeys(), ValidationState.ERROR);
				return false;
			}
		}

		Map<ExtendedTextBox, ExtendedTextBox> keysAndValues = mapBox.getKeysAndValues();
		String wrongValues = "";
		int index = 1;
		for (ExtendedTextBox key : keysAndValues.keySet()) {
			if (!key.isValid() || !keysAndValues.get(key).isValid()) {
				wrongValues += "<br>" + index + ". " + (key.getValue().isEmpty() ? "\"\"" : key.getValue());
			}
			index++;
		}
		if (!wrongValues.isEmpty()) {
			setResult(Result.INVALID_FORMAT);
			mapBox.setRawStatus(getTransl().incorrectFormatItemMap() + " <b>" + wrongValues + "</b>" , ValidationState.ERROR);
			return false;
		}

		mapBox.setStatus(ValidationState.SUCCESS);
		return true;
	}
}
