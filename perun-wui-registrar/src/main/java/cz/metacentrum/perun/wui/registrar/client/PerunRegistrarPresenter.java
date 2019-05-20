package cz.metacentrum.perun.wui.registrar.client;

import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarResources;
import cz.metacentrum.perun.wui.widgets.recaptcha.ReCaptcha;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarPlaceTokens;
import cz.metacentrum.perun.wui.registrar.client.resources.PerunRegistrarTranslation;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunRegistrarPresenter extends PerunPresenter<PerunRegistrarPresenter.MyView, PerunRegistrarPresenter.MyProxy> {

	private boolean captchaOK = !"non".equals(PerunSession.getInstance().getRpcServer());
	private PerunRegistrarTranslation translation = GWT.create(PerunRegistrarTranslation.class);

	@ProxyStandard
	public interface MyProxy extends Proxy<PerunRegistrarPresenter> {
	}

	public interface MyView extends View {

		void onLoadingStartFooter();

		/**
		 * Show list of contact emails
		 * @param contactEmail null if there was an error meanwhile loading data List otherwise.
		 */
		void onFinishedFooter(List<String> contactEmail);

		void hideNavbar();

		void setActiveMenuItem(String anchor);

	}

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	@Inject
	PerunRegistrarPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void onBind() {
		super.onBind();

		if (Window.Location.getParameterMap().containsKey("page") && "apps".equalsIgnoreCase(Window.Location.getParameter("page"))) {
			History.newItem(PerunRegistrarPlaceTokens.getMyApps());
		}
		if (Window.Location.getParameterMap().containsKey("i") && Window.Location.getParameterMap().containsKey("m")) {
			// skip captcha on mail verification
			captchaOK = true;
			History.newItem(PerunRegistrarPlaceTokens.getVerify(), false);
		}

		loadVoAttributes(new Events<List<Attribute>>() {

			@Override
			public void onFinished(List<Attribute> result) {

				Attribute contactEmailAttr = null;
				for (Attribute attr : result) {
					if (attr.getFriendlyName().equals("contactEmail")) {
						contactEmailAttr = attr;
						break;
					}
				}
				List<String> contactEmails = new ArrayList<>();
				if (contactEmailAttr != null) {
					contactEmails = Arrays.asList(contactEmailAttr.getValue().split(","));
				}

				getView().onFinishedFooter(contactEmails);

			}

			@Override
			public void onError(PerunException error) {
				getView().onFinishedFooter(null);
			}

			@Override
			public void onLoadingStart() {
				getView().onLoadingStartFooter();
			}
		});

		// FIXME - very raw implementation to ensure back compatibility with old Registrar

		if (!captchaOK) {

			final String voName = (Window.Location.getParameter("vo") != null) ? URL.decodeQueryString(Window.Location.getParameter("vo")) : null;
			final String reCaptchaPublicKey = PerunConfiguration.getReCaptchaPublicKey();

			if (voName != null && !voName.isEmpty() && reCaptchaPublicKey != null && !reCaptchaPublicKey.isEmpty()) {
				if (!PerunConfiguration.getVosToSkipReCaptchaFor().contains(voName)) {

					final Modal modal = new Modal();
					modal.setRemoveOnHide(true);
					modal.setDataBackdrop(ModalBackdrop.STATIC);

					ModalHeader header = new ModalHeader();
					header.setTitle(translation.pleaseVerifyCaptcha());
					header.setClosable(false);
					ModalBody body = new ModalBody();

					final PerunLoader loader = new PerunLoader();
					loader.onLoading();
					loader.setVisible(false);
					loader.getElement().getStyle().setMarginTop(20, Style.Unit.PX);

					final ReCaptcha captcha = new ReCaptcha();
					captcha.addStyleName(PerunRegistrarResources.INSTANCE.gss().captcha());
					captcha.setSitekey(reCaptchaPublicKey);

					final Alert alert = new Alert();
					alert.setType(AlertType.DANGER);
					alert.setVisible(false);

					captcha.addResponseHandler(responseEvent ->
											   RegistrarManager.verifyCaptcha(captcha.getResponse(), new JsonEvents() {
						@Override
						public void onFinished(JavaScriptObject result1) {
							loader.setVisible(false);

							BasicOverlayObject bt = result1.cast();
							if (bt.getBoolean()) {
								alert.setVisible(false);
								modal.hide();
							} else {
								captcha.reset();
								alert.setText(translation.captchaFailed());
								alert.setVisible(true);
							}
						}

						@Override
						public void onError(PerunException error) {
							loader.setVisible(true);
							loader.onError(error, null);
							captcha.reset();
						}

						@Override
						public void onLoadingStart() {
							alert.setVisible(false);
							loader.setVisible(true);
							loader.onLoading();
						}
					}));

					body.add(alert);
					body.add(captcha);
					body.add(loader);

					modal.add(header);
					modal.add(body);

					modal.show();

				} else {
					// this VO skips captcha
					captchaOK = true;
				}
			} else {
				// captcha is not ok and req params are missing in URL
				placeManager.revealErrorPlace("");
			}


		}

	}

	private int resets = 0;

	@Override
	protected void onReset() {
		super.onReset();

		// Because NavbarCollapse.hide() doesn't work properly. It shows the NavbarCollapse on application startup.
		resets++;
		if (resets > 2) {
			getView().hideNavbar();
		}

		String token = placeManager.getCurrentPlaceRequest().getNameToken();
		if (token == null || token.isEmpty()) {
			if (Window.Location.getParameterMap().containsKey("i") && Window.Location.getParameterMap().containsKey("m")) {
				token = PerunRegistrarPlaceTokens.getVerify();
			} else if (Window.Location.getParameterMap().containsKey("page") && "apps".equalsIgnoreCase(Window.Location.getParameter("page"))) {
				token = PerunRegistrarPlaceTokens.getMyApps();
			} else {
				token = PerunRegistrarPlaceTokens.getForm();
			}
		}
		getView().setActiveMenuItem(token);

	}

	private void loadVoAttributes(final Events<List<Attribute>> callback) {

		final String voName = (Window.Location.getParameter("vo") != null) ? URL.decodeQueryString(Window.Location.getParameter("vo")) : null;

		if (voName == null || voName.isEmpty()) {
			PerunException exception = PerunException.createNew("0", "WrongURL", "Missing parameters in URL.");
			callback.onError(exception);
			return;
		}

		RegistrarManager.getVoAndGroupAttributes(voName, null, new JsonEvents() {

			@Override
			public void onFinished(JavaScriptObject jso) {
				List<Attribute> attributes = JsUtils.jsoAsList(jso);
				callback.onFinished(attributes);
			}

			@Override
			public void onError(PerunException error) {
				callback.onError(error);
			}

			@Override
			public void onLoadingStart() {
				callback.onLoadingStart();
			}
		});

	}

}
