/*
MIT License (MIT)
Copyright (c) 2011 François LAROCHE

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cz.metacentrum.perun.wui.widgets.recaptcha;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * This file was originally taken from https://github.com/pandurangpatil/gwt-recaptcha
 *
 * @author Pandurang Patil 06-Oct-2015
 *
 */
public class ReCaptchaConfig extends JavaScriptObject {
	protected ReCaptchaConfig() {
	}

	/**
	 * @return the sitekey
	 */
	public final native String getSitekey() /*-{
		return this.sitekey;
	}-*/;

	/**
	 * @param sitekey
	 *            the sitekey to set
	 */
	public final native void setSitekey(String sitekey) /*-{
		this.sitekey = sitekey;
	}-*/;

	/**
	 * @return the theme
	 */
	private final native String getThemeInternal() /*-{
		return this.theme;
	}-*/;

	/**
	 * @param theme
	 *            the theme to set
	 */
	private final native void setThemeInternal(String theme) /*-{
		this.theme = theme;
	}-*/;

	/**
	 * @return the size
	 */
	private final native String getSizeInternal() /*-{
		return this.size;
	}-*/;

	/**
	 * @param size
	 *            the size to set
	 */
	private final native void setSizeInternal(String size) /*-{
		this.size = size;
	}-*/;

	/**
	 * @return the tabindex
	 */
	public final native Integer getTabindex() /*-{
		return this.tabindex;
	}-*/;

	/**
	 * @param tabindex
	 *            the tabindex to set
	 */
	public final native void setTabindex(Integer tabindex) /*-{
		this.tabindex = tabindex;
	}-*/;

	/**
	 * @return the type
	 */
	private final native String getTypeInternal()/*-{
		return type;
	}-*/;

	/**
	 * @param type
	 *            the type to set
	 */
	private final native void setTypeInternal(String type) /*-{
		this.type = type;
	}-*/;

	/**
	 * @return the type
	 */
	public final Type getType() {
		String type = getTypeInternal();
		return (type != null ? Type.valueOf(type) : null);
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public final void setType(Type type) {
		setTypeInternal(type.getType());
	}

	/**
	 * @return the theme
	 */
	public final Theme getTheme() {
		String theme = getThemeInternal();
		return (theme != null ? Theme.valueOf(theme) : null);
	}

	/**
	 * @param theme
	 *            the theme to set
	 */
	public final void setTheme(Theme theme) {
		setThemeInternal(theme.getTheme());
	}

	/**
	 * @return the size
	 */
	public final Size getSize() {
		String size = getSizeInternal();
		return (size != null ? Size.valueOf(size) : null);
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public final void setSize(Size size) {
		setSizeInternal(size.getSize());
	}

}
