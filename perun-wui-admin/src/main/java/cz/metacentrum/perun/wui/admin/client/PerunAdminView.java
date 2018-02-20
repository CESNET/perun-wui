package cz.metacentrum.perun.wui.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunTranslation;
import cz.metacentrum.perun.wui.client.resources.PerunWebConstants;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import org.gwtbootstrap3.client.ui.Navbar;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Span;

/**
 * Implementation of View for generic PerunPresenter used in this app.
 * It defines GUI layout and sets pages (other presenters/views) to prepared content slots.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunAdminView extends ViewImpl implements PerunAdminPresenter.MyView {

	@Override
	public void setActiveMenuItem(String token) {
		if (leftMenu != null) leftMenu.setActiveMenuItem(token);
	}

	interface PerunWuiViewUiBinder extends UiBinder<Widget, PerunAdminView> {}

	PerunTranslation translation = GWT.create(PerunTranslation.class);

	@UiField(provided = true)
	Navbar navbar;

	@UiField
	Div menu;

	@UiField
	Div pageContent;

	@UiField
	Span footerLeft;

	@UiField
	Span footerRight;

	private static TopMenu topMenu;
	LeftMenuPresenter leftMenu;

	@Inject
	PerunAdminView(final PerunWuiViewUiBinder binder, LeftMenuPresenter leftMenu) {

		// CREATE LAYOUT
		topMenu = new TopMenu();
		this.leftMenu = leftMenu;
		navbar = topMenu.getWidget();
		initWidget(binder.createAndBindUi(this));
		menu.clear();
		menu.add(leftMenu.asWidget());

		bindSlot(PerunAdminPresenter.SLOT_LEFT_MENU, menu);
		bindSlot(PerunAdminPresenter.SLOT_MAIN_CONTENT, pageContent);

		// TODO - more advanced footer
		footerLeft.setHTML(translation.supportAt(SafeHtmlUtils.fromString(PerunConfiguration.getBrandSupportMail()).asString()));
		footerRight.setHTML(translation.credits(JsUtils.getCurrentYear()) + " | " +  translation.version(PerunWebConstants.INSTANCE.guiVersion()));

	}

	@Override
	public void setInSlot(final Object slot, final IsWidget content) {
		if (slot == PerunPresenter.SLOT_MAIN_CONTENT) {
			pageContent.clear();
			if (content != null) {
				pageContent.add(content);
			}
		} else {
			super.setInSlot(slot, content);
		}
	}

}
