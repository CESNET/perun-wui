package cz.metacentrum.perun.wui.consolidator.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
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

	//private ConsolidatorTranslation translation = GWT.create(ConsolidatorTranslation.class);

	@UiField PerunButton finishButton;
	@UiField Div place;

	@UiField PerunLoader loader;
	@UiField Heading heading;
	@UiField Heading identity;
	@UiField Heading myidents;
	@UiField Alert alert;

	@UiHandler("finishButton")
	public void clickFinish(ClickEvent event) {

		if (redirect != null && !redirect.isEmpty()) {
			Window.Location.replace(redirect);
		} else {
			History.back();
		}

	}

	public JoinPage() {

	}

	public Widget draw(String passedToken) {

		History.fireCurrentHistoryState();

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
					alert.setText("Your token for joining identities is no longer valid. Please retry from the start.");
					alert.setVisible(true);
					loader.setVisible(false);
				} else if (error.getName().equals("IdentityUnknownException")) {
					alert.setType(AlertType.DANGER);
					// TODO translate message
					alert.setText("Neither original or current identity is know to Perun. Please use at least one identity known to Perun.");
					alert.setVisible(true);
					loader.setVisible(false);
				} else if (error.getName().equals("IdentityIsSameException")) {
					alert.setType(AlertType.DANGER);
					// TODO translate message
					alert.setText("You tried to join identity with itself. Please go back and select different identity.");
					alert.setVisible(true);
					loader.setVisible(false);
				} else if (error.getName().equals("IdentitiesAlreadyJoinedException")) {
					alert.setType(AlertType.WARNING);
					// TODO translate message
					alert.setText("You already have both identities joined.");
					alert.setVisible(true);
					loader.setVisible(false);
				} else if (error.getName().equals("IdentityAlreadyInUseException")) {
					alert.setType(AlertType.WARNING);
					// TODO translate message
					alert.setText("Your identity is already associated with a different user. If you are really the same person, please contact support to help you.");
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
					finishButton.setText("Back");
					finishButton.setIcon(IconType.CHEVRON_LEFT);
					finishButton.setIconPosition(IconPosition.LEFT);
					finishButton.setVisible(true);
				} else {

					// TODO close button

				}

			}

			@Override
			public void onLoadingStart() {
				loader.setVisible(true);

				// we do have a valid token
				String extSourceType = PerunSession.getInstance().getPerunPrincipal().getExtSourceType();
				String translatedExtSourceName = PerunSession.getInstance().getPerunPrincipal().getExtSource();
				String translatedActor = PerunSession.getInstance().getPerunPrincipal().getActor();

				if (extSourceType.equals(ExtSource.ExtSourceType.IDP.getType())) {
					translatedExtSourceName = Utils.translateIdp(translatedExtSourceName);
					// social identity
					if (translatedActor.endsWith("extidp.cesnet.cz")) {
						translatedExtSourceName = Utils.translateIdp(translatedActor.split("@")[1]);
						translatedActor = translatedActor.split("@")[0];
					}
				} else if (extSourceType.equals(ExtSource.ExtSourceType.X509.getType())) {
					translatedActor = Utils.convertCertCN(translatedActor);
					translatedExtSourceName = Utils.convertCertCN(translatedExtSourceName);
				}

				heading.setVisible(true);
				identity.setText(translatedActor);
				identity.setVisible(true);
				identity.setSubText(translatedExtSourceName);

			}
		});

		return rootElement;

	}

	private void finishedOk(ArrayList<UserExtSource> list) {

		loader.onFinished();
		loader.setVisible(false);

		alert.setType(AlertType.SUCCESS);
		alert.setText("Your identities were successfully joined.");
		alert.setVisible(true);

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
					// social identity
					if (translatedActor.endsWith("extidp.cesnet.cz")) {
						translatedExtSourceName = Utils.translateIdp("@"+translatedActor.split("@")[1]);
						translatedActor = translatedActor.split("@")[0];
					}
				} else if (extSourceType.equals(ExtSource.ExtSourceType.X509.getType())) {
					translatedActor = Utils.convertCertCN(translatedActor);
					translatedExtSourceName = Utils.convertCertCN(translatedExtSourceName);
				}

				place.insert(new Heading(HeadingSize.H4, translatedActor, translatedExtSourceName), 2);

			} else {
				continue;
			}

		}

		myidents.setVisible(true);

		//if (redirect != null && !redirect.isEmpty()) {
		finishButton.setVisible(true);
		//}

	}

}
