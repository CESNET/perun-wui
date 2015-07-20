package cz.metacentrum.perun.wui.pages;

/**
 * This Interface is used to mark a View as focusable.
 *
 * In order to focus content of the view, its Presenter must
 * call #focus() in its own onReset() method, which is called
 * always when the View is displayed.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface FocusableView {

	/**
	 * Pass focus event to the View from Presenter.
	 */
	void focus();

}
