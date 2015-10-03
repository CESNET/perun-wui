package cz.metacentrum.perun.wui.registrar.widgets.form_items;

import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;

/**
 * Created by ondrej on 3.10.15.
 */
public class TextField extends PerunFormItem {


	public TextField(ApplicationFormItemData item, String lang) {
		super(item, lang);
	}

	@Override
	protected void generateWidget() {

	}

	@Override
	public boolean isValid(boolean b) {
		return false;
	}


	class TextFieldValidator implements PerunFormItemValidator {

		@Override
		public boolean validate(boolean forceNew) {
			return false;
		}

		@Override
		public boolean isProcessing() {
			return false;
		}

		@Override
		public int getReturnCode() {
			return 0;
		}

		@Override
		public void translate() {

		}
	}
}
