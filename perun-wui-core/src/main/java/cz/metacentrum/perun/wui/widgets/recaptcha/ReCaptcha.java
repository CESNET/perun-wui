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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * This file was originally taken from https://github.com/pandurangpatil/gwt-recaptcha
 *
 * @author Pandurang Patil 06-Oct-2015
 *
 */
public class ReCaptcha extends Composite {
	private HTMLPanel					container;
	private SimpleEventBus				widgetEventBus	= new SimpleEventBus();
	private ReCaptchaConfig				config;
	private Integer						widgetId;

	static SimpleEventBus				apiEventBus		= new SimpleEventBus();
	static boolean						apiIsReady		= false;
	private static ReCaptchaUiBinder	uiBinder		= GWT.create(ReCaptchaUiBinder.class);

	interface ReCaptchaUiBinder extends UiBinder<HTMLPanel, ReCaptcha> {
	}

	public ReCaptcha() {
		this((ReCaptchaConfig) ReCaptchaConfig.createObject());
	}

	public ReCaptcha(ReCaptchaConfig config) {
		this.config = config;
		container = uiBinder.createAndBindUi(this);
		initWidget(container);

	}

	static {
		loadApi();
	}

	public static native void loadApi()/*-{
		$wnd.recaptcha_api_ready = function() {
			@cz.metacentrum.perun.wui.widgets.recaptcha.ReCaptcha::apiReady()();
		};
		// 2. This code loads Recaptcha apy asynchronously
		// <script src="https://www.google.com/recaptcha/api.js?onload=recaptcha_api_ready&render=explicit" async defer> </script> 
		var tag = $doc.createElement('script');
		tag.src = "https://www.google.com/recaptcha/api.js?onload=recaptcha_api_ready&render=explicit";
		tag.async = true;
		tag.defer = true;
		var firstScriptTag = $doc.getElementsByTagName('script')[0];
		firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
	}-*/;

	public static void apiReady() {
		apiIsReady = true;
		apiEventBus.fireEvent(new ApiReadyEvent());
	}

	/**
	 * Subscribe for ApiReadyEvent.
	 * 
	 * @param handler
	 *            Handler to be called when ApiReadyEvent occurs.
	 * @return
	 */
	public static HandlerRegistration addApiReadyHandler(ApiReadyHandler handler) {
		return apiEventBus.addHandler(ApiReadyEvent.TYPE, handler);
	}

	void responseCallback(String response) {
		widgetEventBus.fireEvent(new ResponseEvent(response));
	}

	/**
	 * Your callback function that's executed when the user submits a successful CAPTCHA response. The user's response,
	 * g-recaptcha-response, will be the input for your callback function.
	 * 
	 * Alternatively you can retrieve the response using getResponse()
	 * 
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addResponseHandler(ResponseHandler handler) {
		return widgetEventBus.addHandler(ResponseEvent.TYPE, handler);
	}

	void expireCallback() {
		widgetEventBus.fireEvent(new ExpiredEvent());
	}

	/**
	 * Your callback function that's executed when the recaptcha response expires and the user needs to solve a new
	 * CAPTCHA.
	 * 
	 * @param handler
	 * @return
	 */
	public HandlerRegistration addExpireHandler(ExpiredHandler handler) {
		return widgetEventBus.addHandler(ExpiredEvent.TYPE, handler);
	}

	/**
	 * set your site key
	 * 
	 * @param sitekey
	 */
	public void setSitekey(String sitekey) {
		config.setSitekey(sitekey);
	}

	/**
	 * Set tabindex
	 * 
	 * @param tabindex
	 */
	public void setTabindex(Integer tabindex) {
		config.setTabindex(tabindex);
	}

	/**
	 * Set Type AUDIO | IMAGE Default is IMAGE
	 * 
	 * @param type
	 */
	public void setType(Type type) {
		config.setType(type);
	}

	/**
	 * Set Theme DARK | LIGHT Default is LIGHT
	 * 
	 * @param theme
	 */
	public void setTheme(Theme theme) {
		config.setTheme(theme);
	}

	/**
	 * Set Size COMPACT | NORMAL default is NORMAL
	 * 
	 * @param size
	 */
	public void setSize(Size size) {
		config.setSize(size);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (widgetId == null) {
			if (apiIsReady) {
				widgetId = loadCaptcha(config, container.getElement(), this);
			} else {
				addApiReadyHandler(new ApiReadyHandler() {

					@Override
					public void onReady(ApiReadyEvent event) {
						widgetId = loadCaptcha(config, container.getElement(), ReCaptcha.this);
					}
				});
			}
		} else {
			reset();
		}
	}

	/**
	 * 
	 * @param config
	 * @param element
	 * @return
	 */
	private static native int loadCaptcha(ReCaptchaConfig config, Element element, ReCaptcha recaptcha)/*-{
		config.callback = function(response) {
			recaptcha.@cz.metacentrum.perun.wui.widgets.recaptcha.ReCaptcha::responseCallback(Ljava/lang/String;)(response);
		};
		config['expired-callback'] = function() {
			recaptcha.@cz.metacentrum.perun.wui.widgets.recaptcha.ReCaptcha::expireCallback()();
		};
		return $wnd.grecaptcha.render(element, config);
	}-*/;

	/**
	 * Resets the reCAPTCHA widget.
	 */
	public void reset() {
		reset(widgetId);
	}

	private native void reset(int widgetId)/*-{
		$wnd.grecaptcha.reset(widgetId);
	}-*/;

	/**
	 * Gets the response for the reCAPTCHA widget.
	 * 
	 * @return
	 */
	public String getResponse() {
		return getResponse(widgetId);
	}

	private native String getResponse(int widgetId)/*-{
		return $wnd.grecaptcha.getResponse(widgetId);
	}-*/;

}
