package cz.metacentrum.perun.wui.registrar.widgets.items.validators;

/**
 * Created by ondrej on 3.10.15.
 */

import cz.metacentrum.perun.wui.json.Events;

/**
 * Interface for anonymous classes used as customized item validators.
 */
public interface PerunFormItemValidator<T> {

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
		MUST_VALIDATE_EMAIL
	}

	/**
	 * Validates form item.
	 *
	 * @return TRUE = valid / FALSE = not valid
	 */
	public boolean validateLocal(T item);

	/**
	 * Validates form item.
	 *
	 * @return TRUE = valid / FALSE = not valid
	 */
	public void validate(T item, Events<Boolean> events);

	/**
	 *
	 */
	public Result getLastResult();

}


