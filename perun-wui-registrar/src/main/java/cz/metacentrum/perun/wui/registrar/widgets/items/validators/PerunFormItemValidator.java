package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.registrar.widgets.items.PerunFormItem;

/**
 * Interface for anonymous classes used as customized item validators.
 *
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public interface PerunFormItemValidator<T extends PerunFormItem> {

	enum Result {
		NOT_CHECKED,
		OK,
		EMPTY,
		EMPTY_SELECT,
		TOO_LONG,
		INVALID_FORMAT,
		INVALID_FORMAT_EMAIL,
		LOGIN_NOT_AVAILABLE,
		CHECKING_LOGIN,
		CANT_CHECK_LOGIN,
		EMPTY_PASSWORD,
		PASSWORD_MISSMATCH,
		MUST_VALIDATE_EMAIL,
		SECOND_PASSWORD_EMPTY,
		DUPLICATE_KEYS,

		WEAK_PASSWORD,

		CHECKING_WEAK_PASSWORD,

		CANT_CHECK_WEAK_PASSWORD
	}

	/**
	 * Validates form item without calling RPC.
	 *
	 * @return TRUE = valid / FALSE = not valid
	 */
	public boolean validateLocal(T item);

	/**
	 * Validates form item. Can call RPC.
	 */
	public void validate(T item, Events<Boolean> events);

	/**
	 * @return last validation Result.
	 */
	public Result getLastResult();

}


