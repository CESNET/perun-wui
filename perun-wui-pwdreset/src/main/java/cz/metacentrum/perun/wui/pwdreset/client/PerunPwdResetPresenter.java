package cz.metacentrum.perun.wui.pwdreset.client;

import com.google.gwt.core.client.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.resources.PlaceTokens;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.pwdreset.client.resources.PerunPwdResetResources;
import cz.metacentrum.perun.wui.pwdreset.client.resources.PerunPwdResetTranslation;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import cz.metacentrum.perun.wui.widgets.recaptcha.ReCaptcha;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;

/**
 * @author Ondrej Velisek <ondrejvelisek@gmail.com>
 */
public class PerunPwdResetPresenter extends PerunPresenter<PerunPwdResetPresenter.MyView, PerunPwdResetPresenter.MyProxy> {

	private boolean captchaOK = !"non".equals(PerunSession.getInstance().getRpcServer());
	private PerunPwdResetTranslation translation = GWT.create(PerunPwdResetTranslation.class);

	@ProxyStandard
	public interface MyProxy extends Proxy<PerunPwdResetPresenter> {
	}

	public interface MyView extends View {

		void hideNavbar();

	}

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	@Inject
	PerunPwdResetPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void onBind() {

		super.onBind();

		if (PerunSession.getInstance().getUser() == null && !PerunSession.getInstance().getRpcServer().equals("non")) {
			placeManager.revealPlace(new PlaceRequest.Builder().nameToken(PlaceTokens.NOT_USER).build());
			return;
		} else if (PerunSession.getInstance().getUser() == null &&
				PerunSession.getInstance().getRpcServer().equals("non") &&
				!Window.Location.getParameterMap().keySet().contains("token")) {
			placeManager.revealPlace(new PlaceRequest.Builder().nameToken(PlaceTokens.NOT_USER).build());
			return;
		}

		if (!captchaOK) {

			final String reCaptchaPublicKey = PerunConfiguration.getReCaptchaPublicKey();

			if (reCaptchaPublicKey != null && !reCaptchaPublicKey.isEmpty()) {

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
				captcha.addStyleName(PerunPwdResetResources.INSTANCE.gss().captcha());
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

	}

}
