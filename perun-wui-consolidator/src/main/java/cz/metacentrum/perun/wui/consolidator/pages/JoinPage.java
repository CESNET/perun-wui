package cz.metacentrum.perun.wui.consolidator.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.consolidator.client.resources.PerunConsolidatorTranslation;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ExtSource;
import cz.metacentrum.perun.wui.model.beans.UserExtSource;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Single page used by consolidator to display it's state
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class JoinPage {

	private String token;
	private String redirect = Window.Location.getParameter("target_url");
	private Widget rootElement;

	interface ConsolidatorPageUiBinder extends UiBinder<Widget, JoinPage> {
	}

	private static ConsolidatorPageUiBinder ourUiBinder = GWT.create(ConsolidatorPageUiBinder.class);

	private PerunConsolidatorTranslation translation = GWT.create(PerunConsolidatorTranslation.class);

	@UiField PerunButton finishButton;
	@UiField PerunButton backButton;
	@UiField Div place;

	@UiField PerunLoader loader;
	@UiField Heading heading;
	@UiField Heading identity;
	@UiField Heading myidents;
	@UiField Heading login;
	@UiField Alert alert;

	@UiHandler("finishButton")
	public void clickFinish(ClickEvent event) {

		if (redirect != null && !redirect.isEmpty()) {
			Window.Location.assign(redirect);
		}

	}

	@UiHandler("backButton")
	public void clickBack(ClickEvent event) {

		if (redirect != null && !redirect.isEmpty()) {
			Window.Location.assign(Utils.getIdentityConsolidatorLink(false)+"?target_url="+redirect);
		} else {
			Window.Location.assign(Utils.getIdentityConsolidatorLink(false));
		}

	}

	public JoinPage() {

	}

	public Widget draw(String passedToken) {

		this.token = passedToken;

		if (rootElement == null) {
			rootElement = ourUiBinder.createAndBindUi(this);
		}

		if (this.token == null || this.token.isEmpty()) {

			alert.setType(AlertType.DANGER);
			alert.setText("Token for joining identity is missing.");
			alert.setVisible(true);

			return rootElement;

		}

		if (PerunConfiguration.getWayfLinkAnotherButtonText() != null) {
			backButton.setText(PerunConfiguration.getWayfLinkAnotherButtonText());
		} else {
			backButton.setText(translation.backButton());
		}
		backButton.setVisible(!PerunConfiguration.isWayfLinkAnotherButtonDisabled());

		heading.setText(translation.currentIdentityIs());
		myidents.setText(translation.myIdents());

		// try to perform joining
		RegistrarManager.consolidateIdentityUsingToken(token, new JsonEvents() {

			final JsonEvents jsonEvent = this;

			@Override
			public void onFinished(JavaScriptObject jso) {
				ArrayList<UserExtSource> uess = JsUtils.jsoAsList(jso);
				finishedOk(uess);
			}

			@Override
			public void onError(PerunException error) {

				// TODO - print more verbose exception texts

				if (error.getName().equals("InvalidTokenException")) {
					alert.setType(AlertType.DANGER);
					// TODO translate message
					alert.setText(translation.invalidTokenException());
					alert.setVisible(true);
					loader.setVisible(false);
					backButton.setVisible(true);
				} else if (error.getName().equals("IdentityUnknownException")) {
					alert.setType(AlertType.DANGER);
					// TODO translate message
					alert.setText(translation.identityUnknownException());
					alert.setVisible(true);
					loader.setVisible(false);
					backButton.setVisible(true);
				} else if (error.getName().equals("IdentityIsSameException")) {
					alert.setType(AlertType.DANGER);
					// TODO translate message
					alert.setText(translation.identityIsSameException(Utils.convertCertCN(Utils.translateIdp(JsUtils.getNativePropertyString(error, "login")))+" / "+Utils.convertCertCN(Utils.translateIdp(JsUtils.getNativePropertyString(error, "source")))));
					alert.setVisible(true);
					loader.setVisible(false);
					backButton.setVisible(true);
				} else if (error.getName().equals("IdentitiesAlreadyJoinedException")) {
					alert.setType(AlertType.WARNING);
					// TODO translate message
					alert.setText(translation.identitiesAlreadyJoinedException());
					alert.setVisible(true);
					loader.setVisible(false);
					backButton.setVisible(true);
				} else if (error.getName().equals("IdentityAlreadyInUseException")) {
					alert.setType(AlertType.WARNING);
					// TODO translate message
					alert.setText(translation.identityAlreadyInUseException(PerunConfiguration.getBrandSupportMail()));
					alert.setVisible(true);
					loader.setVisible(false);
				} else {
					// generic error
					loader.onError(error, new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							RegistrarManager.consolidateIdentityUsingToken(token, jsonEvent);
						}
					});
				}

				if (redirect != null && !redirect.isEmpty()) {
					if (PerunConfiguration.getWayfLeaveButtonText() != null) {
						finishButton.setText(PerunConfiguration.getWayfLeaveButtonText());
					} else {
						finishButton.setText(translation.finishButtonLeave());
					}
					finishButton.setIcon(IconType.CHEVRON_RIGHT);
					finishButton.setIconPosition(IconPosition.RIGHT);
					finishButton.setVisible(true);
				}

			}

			@Override
			public void onLoadingStart() {
				loader.setVisible(true);

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
				identity.setTitle(translatedActor);

			}
		});

		return rootElement;

	}

	private void finishedOk(ArrayList<UserExtSource> list) {

		loader.onFinished();
		loader.setVisible(false);

		alert.setType(AlertType.SUCCESS);
		alert.setText(translation.joinedMessage());
		alert.setVisible(true);

		/* TODO - temporary disabled

		for (UserExtSource ues : list) {

			if (ues.getExtSource().getType().equals(ExtSource.ExtSourceType.IDP.getType()) ||
					ues.getExtSource().getType().equals(ExtSource.ExtSourceType.X509.getType()) ||
					ues.getExtSource().getType().equals(ExtSource.ExtSourceType.KERBEROS.getType())) {

				// we do have a valid token
				String extSourceType = ues.getExtSource().getType();
				String translatedExtSourceName = ues.getExtSource().getName();
				String translatedActor = ues.getLogin();

				if (extSourceType.equals(ExtSource.ExtSourceType.IDP.getType())) {
					translatedExtSourceName = Utils.translateIdp(translatedExtSourceName);
					// social identity or proxy IdP
					if (translatedActor.endsWith("extidp.cesnet.cz") || translatedActor.endsWith("elixir-europe.org") || translatedExtSourceName.equals("https://login.elixir-czech.org/idp/")) {
						translatedExtSourceName = Utils.translateIdp("@"+translatedActor.split("@")[1]);
						translatedActor = translatedActor.split("@")[0];
					}
				} else if (extSourceType.equals(ExtSource.ExtSourceType.X509.getType())) {
					translatedActor = Utils.convertCertCN(translatedActor);
					translatedExtSourceName = Utils.convertCertCN(translatedExtSourceName);
				} else if (extSourceType.equals(ExtSource.ExtSourceType.KERBEROS.getType())) {
					translatedExtSourceName = Utils.translateKerberos(translatedExtSourceName);
				}

				place.insert(new Heading(HeadingSize.H4, translatedActor, translatedExtSourceName), 4);

			}

		}

		myidents.setVisible(true);

		*/

		if (redirect != null && !redirect.isEmpty()) {
			if (PerunConfiguration.getWayfLeaveButtonText() != null) {
				finishButton.setText(PerunConfiguration.getWayfLeaveButtonText());
			} else {
				finishButton.setText(translation.finishButtonLeave());
			}
			finishButton.setIcon(IconType.CHEVRON_RIGHT);
			finishButton.setIconPosition(IconPosition.RIGHT);
			finishButton.setVisible(true);
		}

		backButton.setVisible(!PerunConfiguration.isWayfLinkAnotherButtonDisabled());
		backButton.setIcon(IconType.CHEVRON_LEFT);
		backButton.setIconPosition(IconPosition.LEFT);
		if (PerunConfiguration.getWayfLinkAnotherButtonText() != null) {
			backButton.setText(PerunConfiguration.getWayfLinkAnotherButtonText());
		} else {
			backButton.setText(translation.backButton());
		}

	}

}
