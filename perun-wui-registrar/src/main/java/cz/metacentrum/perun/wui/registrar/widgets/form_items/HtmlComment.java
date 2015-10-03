package cz.metacentrum.perun.wui.registrar.widgets.form_items;

import com.google.gwt.user.client.ui.HTML;
import cz.metacentrum.perun.wui.model.beans.ApplicationFormItemData;

/**
 * Created by ondrej on 3.10.15.
 */
public class HtmlComment extends PerunFormItem {

	public HtmlComment(ApplicationFormItemData item, String lang) {
		super(item, lang);
	}

	@Override
	protected void generateWidget() {
		add(new HTML(getLabelOrShortName()));
	}

	@Override
	public boolean isValid(boolean forceNew) {
		return false;
	}






	class HtmlCommentValidator implements PerunFormItemValidator {

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
