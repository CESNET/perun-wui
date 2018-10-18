package cz.metacentrum.perun.wui.consolidator.pages;

import com.google.gwt.core.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.consolidator.client.resources.PerunConsolidatorTranslation;
import cz.metacentrum.perun.wui.consolidator.widgets.Wayf;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.BasicOverlayObject;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ExtSource;
import cz.metacentrum.perun.wui.widgets.*;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import java.util.Objects;

/**
 * Single page used by consolidator to display it's state
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class SelectPage {

	private String token;
	private String redirect = Window.Location.getParameter("target_url");
	private Widget rootElement;

	interface ConsolidatorPageUiBinder extends UiBinder<Widget, SelectPage> {
	}

	private static ConsolidatorPageUiBinder ourUiBinder = GWT.create(ConsolidatorPageUiBinder.class);

	private PerunConsolidatorTranslation translation = GWT.create(PerunConsolidatorTranslation.class);

	@UiField(provided = true)
	Wayf wayf;

	@UiField PerunLoader loader;
	@UiField Heading heading;
	@UiField Heading joinHeading;
	@UiField Heading identity;
	@UiField Heading login;
	@UiField Alert alert;
	@UiField Alert counter;

	public SelectPage() {
	}

	public Widget draw() {

		wayf = new Wayf(redirect);

		if (rootElement == null) {
			rootElement = ourUiBinder.createAndBindUi(this);
		}

		heading.setText(translation.currentIdentityIs());
		String text = PerunConfiguration.getWayfLinkAnAccountText();
		if (text == null || text.isEmpty()) {
			joinHeading.setText(translation.joinWith());
		} else {
			joinHeading.setText(text);
		}

		// fixme on error loader.onError(error, null);

		if (token == null || token.isEmpty()) {

			RegistrarManager.getConsolidatorToken(new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject jso) {

					token = ((BasicOverlayObject) jso).getString();

					// we do have a valid token
					String extSourceType = PerunSession.getInstance().getPerunPrincipal().getExtSourceType();
					String translatedExtSourceName = PerunSession.getInstance().getPerunPrincipal().getExtSource();
					String translatedActor = PerunSession.getInstance().getPerunPrincipal().getActor();

					// check if identity is sourced from foreign proxy
					String aa = PerunSession.getInstance().getPerunPrincipal().getAdditionInformation("authenticating-authority");
					boolean foreignProxy = (aa != null && !aa.isEmpty());
					String foreignEppn = "";

					if (extSourceType.equals(ExtSource.ExtSourceType.IDP.getType())) {

						translatedExtSourceName = translatedActor.split("@")[1];// Utils.translateIdp(translatedExtSourceName);
						// social identity
						if (translatedActor.endsWith("extidp.cesnet.cz") || translatedActor.endsWith("elixir-europe.org") || Objects.equals(translatedExtSourceName, "https://login.elixir-czech.org/idp/")) {
							translatedExtSourceName = Utils.translateIdp("@"+translatedActor.split("@")[1]);
							translatedActor = translatedActor.split("@")[0];
						}

						translatedActor = translatedActor.split("@")[0];

						if (foreignProxy) {
							// get source identity EPPN value
							String eppn = PerunSession.getInstance().getPerunPrincipal().getAdditionInformation("eppn");
							if (eppn != null && !eppn.isEmpty()) {
								translatedActor = eppn.split("@")[0];
								foreignEppn = eppn;
							}
							// get source identity EntityId
							translatedExtSourceName = Utils.translateIdp(aa);
						}

						// get actor from attributes if present
						String displayName = PerunSession.getInstance().getPerunPrincipal().getAdditionInformation("displayName");
						String commonName = PerunSession.getInstance().getPerunPrincipal().getAdditionInformation("cn");

						if (displayName != null && !displayName.isEmpty()) {
							translatedActor = displayName;
						} else {
							if (commonName != null && !commonName.isEmpty()) {
								translatedActor = commonName;
							}
						}

					} else if (extSourceType.equals(ExtSource.ExtSourceType.X509.getType())) {
						translatedActor = Utils.convertCertCN(translatedActor);
						translatedExtSourceName = Utils.convertCertCN(translatedExtSourceName);
					} else if (extSourceType.equals(ExtSource.ExtSourceType.KERBEROS.getType())) {
						translatedExtSourceName = Utils.translateKerberos(translatedExtSourceName);
					}

					heading.setVisible(true);
					if (PerunSession.getInstance().getUser() != null) {
						login.setText(PerunSession.getInstance().getUser().getFullName());
						login.setSubText("( " + ((foreignProxy) ? foreignEppn : PerunSession.getInstance().getPerunPrincipal().getActor()) + " )");
					} else {
						login.setText(translatedActor);
					}
					login.setVisible(!PerunConfiguration.isWayfLinkAnAccountDisabled());
					identity.setText(translatedExtSourceName);
					identity.setVisible(true);
					joinHeading.setVisible(!PerunConfiguration.isWayfLinkAnAccountDisabled());
					identity.setTitle(translatedActor);

					if (PerunSession.getInstance().getUser() == null) {
						alert.setVisible(true);
						alert.setText(translation.notRegistered());
					}

					wayf.setToken(token);
					wayf.buildWayfGroups();
					loader.onFinished();
					loader.setVisible(false);
					wayf.setVisible(true);
					setTimer();

				}

				@Override
				public void onError(PerunException error) {
					loader.onError(error, null);
				}

				@Override
				public void onLoadingStart() {
					loader.onLoading();
					loader.setVisible(true);
				}
			});

		} else {

			wayf.buildWayfGroups();
			loader.onFinished();
			loader.setVisible(false);
			wayf.setVisible(true);
			setTimer();

		}

		return rootElement;

	}

	private void setTimer() {

		//counter.setText(translation.authorizationTokenWillExpire(300));
		//counter.setVisible(true);

		Timer t = new Timer() {
			int count = 60 * 5;
			public void run() {
				counter.setText(translation.authorizationTokenWillExpire(count));
				count--;
				if (count < 0) {
					counter.setType(AlertType.DANGER);
					counter.setVisible(true);
					counter.setText(translation.authorizationTokenHasExpired());
					this.cancel();
				}
			}
		};
		t.scheduleRepeating(1000);
		t.run();

	}

}
