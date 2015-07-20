package cz.metacentrum.perun.wui.pages;

/**
 * This Interface is used to mark a View as resizable.
 *
 * In order to resize content of the view, its Presenter must
 * call #onResize() in its own onReset() method, which is called
 * always when the View is displayed.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface ResizableView {

	/**
	 * Pass resize event to the View from Presenter.
	 */
	void onResize();

}
