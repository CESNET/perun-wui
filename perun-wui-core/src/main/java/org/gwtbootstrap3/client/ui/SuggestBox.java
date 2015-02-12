package org.gwtbootstrap3.client.ui;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2015 GwtBootstrap3
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.List;

import org.gwtbootstrap3.client.ui.base.HasAutoComplete;
import org.gwtbootstrap3.client.ui.base.HasId;
import org.gwtbootstrap3.client.ui.base.HasPlaceholder;
import org.gwtbootstrap3.client.ui.base.HasResponsiveness;
import org.gwtbootstrap3.client.ui.base.HasSize;
import org.gwtbootstrap3.client.ui.base.ValueBoxBase;
import org.gwtbootstrap3.client.ui.base.helper.StyleHelper;
import org.gwtbootstrap3.client.ui.base.mixin.EnabledMixin;
import org.gwtbootstrap3.client.ui.base.mixin.ErrorHandlerMixin;
import org.gwtbootstrap3.client.ui.base.mixin.IdMixin;
import org.gwtbootstrap3.client.ui.constants.DeviceSize;
import org.gwtbootstrap3.client.ui.constants.InputSize;
import org.gwtbootstrap3.client.ui.constants.Styles;
import org.gwtbootstrap3.client.ui.form.error.ErrorHandler;
import org.gwtbootstrap3.client.ui.form.error.ErrorHandlerType;
import org.gwtbootstrap3.client.ui.form.error.HasErrorHandler;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Wrapper for a {@link com.google.gwt.user.client.ui.SuggestBox}.<br/>
 * <br/>
 * The default style is inherited from the {@link Styles#DROPDOWN_MENU}. Styling of the suggestions items need
 * a bit of css in order to be pleasing to the eye.
 *
 * <pre>
 *  .dropdown-menu .item {
 *      padding: 5px;
 *  }
 *
 *  .dropdown-menu .item-selected {
 *      background-color: #eee;
 *  }
 * </pre>
 *
 * @author Steven Jardine
 */
public class SuggestBox extends com.google.gwt.user.client.ui.SuggestBox implements HasId, HasResponsiveness, HasPlaceholder,
		HasAutoComplete, HasSize<InputSize>, HasEditorErrors<String>, HasErrorHandler {

	private final EnabledMixin<SuggestBox> enabledMixin = new EnabledMixin<SuggestBox>(this);

	private final ErrorHandlerMixin<SuggestBox, String> errorHandlerMixin = new ErrorHandlerMixin<SuggestBox, String>(this);

	private final IdMixin<SuggestBox> idMixin = new IdMixin<SuggestBox>(this);

	/**
	 * Constructor for {@link SuggestBox}. Creates a {@link MultiWordSuggestOracle} and {@link TextBox} to use
	 * with this {@link SuggestBox}.
	 */
	public SuggestBox() {
		this(new MultiWordSuggestOracle());
	}

	/**
	 * Constructor for {@link SuggestBox}. Creates a {@link TextBox} to use with this {@link SuggestBox}.
	 *
	 * @param oracle the oracle for this <code>SuggestBox</code>
	 */
	public SuggestBox(SuggestOracle oracle) {
		this(oracle, new TextBox());
	}

	/**
	 * Constructor for {@link SuggestBox}. The text box will be removed from it's current location and wrapped
	 * by the {@link SuggestBox}.
	 *
	 * @param oracle supplies suggestions based upon the current contents of the text widget
	 * @param box the text widget
	 */
	public SuggestBox(SuggestOracle oracle, ValueBoxBase<String> box) {
		this(oracle, box, new DefaultSuggestionDisplay());
	}

	/**
	 * Constructor for {@link SuggestBox}. The text box will be removed from it's current location and wrapped
	 * by the {@link SuggestBox}.
	 *
	 * @param oracle supplies suggestions based upon the current contents of the text widget
	 * @param box the text widget
	 * @param suggestDisplay the class used to display suggestions
	 */
	public SuggestBox(SuggestOracle oracle, ValueBoxBase<String> box, SuggestionDisplay suggestDisplay) {
		super(oracle, box, suggestDisplay);
		setStyleName(Styles.FORM_CONTROL);
	}

	/** {@inheritDoc} */
	@Override
	public String getAutoComplete() {
		return getElement().getAttribute(AUTO_COMPLETE);
	}

	/** {@inheritDoc} */
	@Override
	public ErrorHandler getErrorHandler() {
		return errorHandlerMixin.getErrorHandler();
	}

	/** {@inheritDoc} */
	@Override
	public ErrorHandlerType getErrorHandlerType() {
		return errorHandlerMixin.getErrorHandlerType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return idMixin.getId();
	}

	/** {@inheritDoc} */
	@Override
	public String getPlaceholder() {
		return getElement().getAttribute(PLACEHOLDER);
	}

	/** {@inheritDoc} */
	@Override
	public InputSize getSize() {
		return InputSize.fromStyleName(getStyleName());
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEnabled() {
		return enabledMixin.isEnabled();
	}

	/** {@inheritDoc} */
	@Override
	public void setAutoComplete(final boolean autoComplete) {
		getElement().setAttribute(AUTO_COMPLETE, autoComplete ? ON : OFF);
	}

	/** {@inheritDoc} */
	@Override
	public void setEnabled(final boolean enabled) {
		enabledMixin.setEnabled(enabled);
	}

	/** {@inheritDoc} */
	@Override
	public void setErrorHandler(ErrorHandler handler) {
		errorHandlerMixin.setErrorHandler(handler);
	}

	/** {@inheritDoc} */
	@Override
	public void setErrorHandlerType(ErrorHandlerType type) {
		errorHandlerMixin.setErrorHandlerType(type);
	}

	/** {@inheritDoc} */
	@Override
	public void setHiddenOn(final DeviceSize deviceSize) {
		StyleHelper.setHiddenOn(this, deviceSize);
	}

	/** {@inheritDoc} */
	@Override
	public void setId(final String id) {
		idMixin.setId(id);
	}

	/** {@inheritDoc} */
	@Override
	public void setPlaceholder(final String placeHolder) {
		getElement().setAttribute(PLACEHOLDER, placeHolder != null ? placeHolder : "");
	}

	/** {@inheritDoc} */
	@Override
	public void setSize(final InputSize size) {
		StyleHelper.addUniqueEnumStyleName(this, InputSize.class, size);
	}

	/** {@inheritDoc} */
	@Override
	public void setVisibleOn(final DeviceSize deviceSize) {
		StyleHelper.setVisibleOn(this, deviceSize);
	}

	/** {@inheritDoc} */
	@Override
	public void showErrors(List<EditorError> errors) {
		errorHandlerMixin.showErrors(errors);
	}

}
