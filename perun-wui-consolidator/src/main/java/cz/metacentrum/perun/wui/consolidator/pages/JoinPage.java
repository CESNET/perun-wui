package cz.metacentrum.perun.wui.consolidator.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.consolidator.client.resources.PerunConsolidatorTranslation;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.RegistrarManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.EnrichedExtSource;
import cz.metacentrum.perun.wui.model.beans.EnrichedIdentity;
import cz.metacentrum.perun.wui.model.beans.ExtSource;
import cz.metacentrum.perun.wui.model.beans.UserExtSource;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelFooter;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.*;
import org.gwtbootstrap3.client.ui.html.Div;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Strong;

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
	private boolean consolidationInProgress = false;

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

		// Preview the identity represented by the token before asking user for explicit confirmation.
		loadTokenIdentity();

		return rootElement;

	}

	private void loadTokenIdentity() {

		RegistrarManager.getIdentityByConsolidatorToken(token, new JsonEvents() {

			@Override
			public void onFinished(JavaScriptObject jso) {
				PerunException error = getError(jso);
				if (error != null) {
					showPreviewError(error);
					return;
				}
				EnrichedIdentity identity = jso == null ? null : jso.cast();
				if (!isValidTokenIdentity(identity)) {
					showPreviewError(PerunException.createNew("INVALID_PREVIEW_RESPONSE", "InvalidIdentityResponse",
							translation.joinInvalidPreview()));
					return;
				}
				loader.onFinished();
				loader.setVisible(false);
				showConfirmationDialog(identity);
			}

			@Override
			public void onError(PerunException error) {
				showPreviewError(error);
			}

			@Override
			public void onLoadingStart() {
				loader.setVisible(true);

				String aa = PerunSession.getInstance().getPerunPrincipal().getAdditionInformation("authenticating-authority");
				String eppn = PerunSession.getInstance().getPerunPrincipal().getAdditionInformation("eppn");
				boolean foreignProxy = (aa != null && !aa.isEmpty());
				String foreignEppn = "";
				if (foreignProxy && eppn != null && !eppn.isEmpty()) {
					foreignEppn = eppn;
				}
				String[] displayIdentity = formatIdentity(
						PerunSession.getInstance().getPerunPrincipal().getActor(),
						PerunSession.getInstance().getPerunPrincipal().getExtSource(),
						PerunSession.getInstance().getPerunPrincipal().getExtSourceType(),
						PerunSession.getInstance().getPerunPrincipal().getAdditionInformation("displayName"),
						PerunSession.getInstance().getPerunPrincipal().getAdditionInformation("cn"), eppn, aa);
				String translatedActor = displayIdentity[0];
				String translatedExtSourceName = displayIdentity[1];

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

	}

	private PerunException getError(JavaScriptObject jso) {
		if (jso == null) return null;
		PerunException error = (PerunException) jso;
		if (error.getErrorId() != null && error.getMessage() != null) {
			return error;
		}
		return null;
	}

	private boolean isValidTokenIdentity(EnrichedIdentity identity) {
		if (identity == null) return false;
		ArrayList<EnrichedExtSource> identities = identity.getIdentities();
		if (identities.size() != 1 || identities.get(0) == null) return false;
		EnrichedExtSource tokenIdentity = identities.get(0);
		ExtSource extSource = tokenIdentity.getExtSource();
		String actor = tokenIdentity.getAttribute("actor");
		return extSource != null && actor != null && !actor.isEmpty() &&
				extSource.getName() != null && !extSource.getName().isEmpty() &&
				extSource.getType() != null && !extSource.getType().isEmpty();
	}

	private void showPreviewError(PerunException error) {
		loader.setVisible(true);
		loader.onError(error, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loadTokenIdentity();
			}
		});
	}

	private String[] formatIdentity(String actor, String extSourceName, String extSourceType, String displayName,
									String commonName, String eppn, String authenticatingAuthority) {

		String translatedActor = actor;
		String translatedExtSourceName = extSourceName;
		boolean foreignProxy = (authenticatingAuthority != null && !authenticatingAuthority.isEmpty());

		if (extSourceType.equals(ExtSource.ExtSourceType.IDP.getType())) {

			String actorDomain = null;
			if (translatedActor.contains("@")) {
				actorDomain = translatedActor.split("@")[1];
				translatedExtSourceName = Utils.translateIdp(translatedExtSourceName) + " (" + actorDomain + ")";
			} else {
				translatedExtSourceName = Utils.translateIdp(translatedExtSourceName);
			}

			// social identity
			if (actorDomain != null && (translatedActor.endsWith("extidp.cesnet.cz") ||
					translatedActor.endsWith("elixir-europe.org") ||
					Objects.equals(translatedExtSourceName, "https://login.elixir-czech.org/idp/"))) {
				translatedExtSourceName = Utils.translateIdp("@" + actorDomain);
				translatedActor = translatedActor.split("@")[0];
			}

			translatedActor = translatedActor.split("@")[0];

			if (foreignProxy) {
				if (eppn != null && !eppn.isEmpty()) {
					translatedActor = eppn.split("@")[0];
				}
				translatedExtSourceName = Utils.translateIdp(authenticatingAuthority);
			}

			if (displayName != null && !displayName.isEmpty()) {
				translatedActor = displayName;
			} else if (commonName != null && !commonName.isEmpty()) {
				translatedActor = commonName;
			}

		} else if (extSourceType.equals(ExtSource.ExtSourceType.X509.getType())) {
			translatedActor = Utils.convertCertCN(translatedActor);
			translatedExtSourceName = Utils.convertCertCN(translatedExtSourceName);
		} else if (extSourceType.equals(ExtSource.ExtSourceType.KERBEROS.getType())) {
			translatedExtSourceName = Utils.translateKerberos(translatedExtSourceName);
		}

		return new String[] { translatedActor, translatedExtSourceName };

	}

	private void showConfirmationDialog(EnrichedIdentity target) {

		Modal dialog = new Modal();
		dialog.setClosable(true);
		dialog.setDataBackdrop(ModalBackdrop.STATIC);
		dialog.setDataKeyboard(true);

		HandlerRegistration cancelHandler = dialog.addHiddenHandler(event -> clickBack(null));

		ModalHeader header = new ModalHeader();
		header.add(new Heading(HeadingSize.H4, translation.joinConfirmHeading()));
		dialog.add(header);

		ModalBody body = new ModalBody();
		body.add(new Heading(HeadingSize.H5, translation.joinConfirmQuestion()));
		Paragraph warning = new Paragraph();
		warning.add(new Strong(translation.joinConfirmWarning()));
		body.add(warning);

        // try to show the joining identity in a similar fashion as on other places
		EnrichedExtSource tokenIdentity = target.getIdentities().get(0);
		ExtSource extSource = tokenIdentity.getExtSource();
		String eppn = tokenIdentity.getAttribute("eppn");
		String actor = tokenIdentity.getAttribute("actor");
		String displayName = Objects.equals(target.getName(), actor) ? null : target.getName();
		String[] displayIdentity = formatIdentity(actor, extSource.getName(), extSource.getType(), displayName,
				null, eppn, tokenIdentity.getAttribute("authenticating-authority"));
		Panel identityCard = new Panel();
		PanelHeader identityCardHeader = new PanelHeader();
		identityCardHeader.add(new Heading(HeadingSize.H4, translation.joinConfirmIdentity(displayIdentity[0])));
		identityCard.add(identityCardHeader);
		PanelBody identityCardBody = new PanelBody();
		identityCardBody.add(mutedParagraph(translation.joinConfirmFriendlyProvider(displayIdentity[1])));
		if (target.getEmail() != null && !target.getEmail().isEmpty()) {
			identityCardBody.add(mutedParagraph(translation.joinConfirmEmail(target.getEmail())));
		}
		if (target.getOrganization() != null && !target.getOrganization().isEmpty()) {
			identityCardBody.add(mutedParagraph(translation.joinConfirmOrganization(target.getOrganization())));
		}
		identityCard.add(identityCardBody);

		PanelFooter identityCardFooter = new PanelFooter();
		identityCardFooter.add(rawIdentityParagraph(translation.joinConfirmIdentifier(tokenIdentity.getAttribute("actor"))));
		identityCardFooter.add(rawIdentityParagraph(translation.joinConfirmProvider(extSource.getName())));
		identityCard.add(identityCardFooter);
		body.add(identityCard);

		dialog.add(body);

		ModalFooter footer = new ModalFooter();
		PerunButton cancelButton = new PerunButton(translation.cancel());
		cancelButton.setType(ButtonType.DEFAULT);
		cancelButton.setIcon(IconType.CLOSE);
		cancelButton.setIconPosition(IconPosition.LEFT);
		cancelButton.addClickHandler(event -> dialog.hide());
		footer.add(cancelButton);

		PerunButton linkButton = new PerunButton(translation.joinConfirmLink());
		linkButton.setType(ButtonType.SUCCESS);
		linkButton.setIcon(IconType.LINK);
		linkButton.setIconPosition(IconPosition.RIGHT);
		linkButton.addClickHandler(event -> {
			cancelHandler.removeHandler();
			dialog.hide();
			consolidateIdentityUsingToken();
		});
		footer.add(linkButton);

		dialog.add(footer);
		dialog.show();

	}

	private Paragraph mutedParagraph(String text) {
		Paragraph paragraph = new Paragraph();
		paragraph.setText(text);
		paragraph.setEmphasis(Emphasis.MUTED);
		return paragraph;
	}

	private Paragraph rawIdentityParagraph(String text) {
		Paragraph paragraph = mutedParagraph(text);
		paragraph.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
		return paragraph;
	}

	private void consolidateIdentityUsingToken() {
		if (consolidationInProgress) return;
		consolidationInProgress = true;

		// try to perform joining
		RegistrarManager.consolidateIdentityUsingToken(token, new JsonEvents() {

			final JsonEvents jsonEvent = this;

			@Override
			public void onFinished(JavaScriptObject jso) {
				PerunException error = getError(jso);
				if (error != null) {
					onError(error);
					return;
				}
				ArrayList<UserExtSource> uess = JsUtils.jsoAsList(jso);
				consolidationInProgress = false;
				finishedOk(uess);
			}

			@Override
			public void onError(PerunException error) {

				consolidationInProgress = false;
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
			}
		});

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
