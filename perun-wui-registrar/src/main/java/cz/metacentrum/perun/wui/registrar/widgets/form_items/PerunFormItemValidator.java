package cz.metacentrum.perun.wui.registrar.widgets.form_items;

/**
 * Created by ondrej on 3.10.15.
 */
/**
 * Interface for anonymous classes used as customized item validators.
 */
public interface PerunFormItemValidator {

	/**
	 * Validates form item.
	 *
	 * @param forceNew TRUE = force new validation / FALSE = use value from last check
	 * @return TRUE = valid / FALSE = not valid
	 */
	public boolean validate(boolean forceNew);

	/**
	 * Return TRUE if validation is processing (for items with callbacks to core)
	 *
	 * @return TRUE = validation is processing / FALSE = validation is done
	 */
	public boolean isProcessing();

	/**
	 * -1 = no message
	 * 0 = OK
	 * 1 = isEmpty
	 * 2 = isEmptySelect
	 * 3 = tooLong
	 * 4 = invalid format
	 * 5 = invalid format email
	 * 6 = login not available
	 * 7 = can't check login
	 * 9 = checking login...
	 * 10 = password can't be empty
	 * 11 = password mismatch
	 * 12 = must validate email
	 */
	public int getReturnCode();

	/**
	 * Translate error messages based on inner return codes.
	 */
	public void translate();
}
