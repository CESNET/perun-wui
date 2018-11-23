package cz.metacentrum.perun.wui.pwdreset.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.ErrorTranslator;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AttributesManager;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.pwdreset.client.resources.PerunPwdResetTranslation;
import cz.metacentrum.perun.wui.widgets.AlertErrorReporter;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedPasswordTextBox;
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Page to display pwd reset form for specified namespace
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PwdResetView extends ViewImpl implements PwdResetPresenter.MyView {

	interface PwdResetUiBinder extends UiBinder<Widget, PwdResetView> {
	}

	private PerunPwdResetTranslation translation = GWT.create(PerunPwdResetTranslation.class);
	private ArrayList<Attribute> logins;
	private String namespace = "";

	private boolean isAccountActivation = Window.Location.getParameterMap().containsKey("activation");

	private PerunButton continueButton = PerunButton.getButton(PerunButtonType.CONTINUE, new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			Window.Location.replace(Window.Location.getParameter("target_url"));
		}
	});

	@UiField Form form;
	@UiField PerunLoader loader;
	@UiField Text text;
	@UiField PerunButton submit;
	@UiField ExtendedPasswordTextBox passwordTextBox;
	@UiField ExtendedPasswordTextBox passwordTextBox2;
	@UiField FormLabel passLabel;
	@UiField FormGroup passItem;
	@UiField HelpBlock itemStatus;
	@UiField HelpBlock help;
	@UiField AlertErrorReporter alert;

	@Inject
	public PwdResetView(PwdResetUiBinder binder) {

		initWidget(binder.createAndBindUi(this));

		text.setText((isAccountActivation) ? translation.activateAppName() : translation.pwdresetAppName());
		submit.setText((isAccountActivation) ? translation.submitActivateButton() : translation.submitPwdResetButton());
		passLabel.setText(translation.pwdresetLabel());

		help.setHTML(translation.dontUseAccents());

		passwordTextBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				validate();
			}
		});
		passwordTextBox2.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				validate();
			}
		});
		passwordTextBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				validate();
			}
		});
		passwordTextBox2.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				validate();
			}
		});

		continueButton.setVisible(false);
		alert.getToolbar().add(continueButton);

		if (Window.Location.getParameterMap().keySet().contains("m") &&
				Window.Location.getParameterMap().keySet().contains("i") &&
				PerunSession.getInstance().getRpcServer().equals("non")) {

			final String i = Window.Location.getParameter("i");
			final String m = Window.Location.getParameter("m");

			drawNonAuthz();

			submit.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					if (validate()) {

						UsersManager.resetNonAuthzPassword(i, m, passwordTextBox.getValue(), new JsonEvents() {

							final JsonEvents events = this;

							@Override
							public void onFinished(JavaScriptObject result) {
								submit.setProcessing(false);
								form.setVisible(false);
								alert.setType(AlertType.SUCCESS);
								alert.setText((isAccountActivation) ? translation.activateSuccess() : translation.resetSuccess());
								alert.setVisible(true);
								if (Window.Location.getParameterMap().containsKey("target_url")) {
									alert.getToolbar().setVisible(true);
									continueButton.setType(ButtonType.SUCCESS);
									continueButton.setVisible(true);
								}

							}

							@Override
							public void onError(PerunException error) {
								submit.setProcessing(false);
								form.setVisible(false);
								alert.setVisible(true);
								alert.setHTML(ErrorTranslator.getTranslatedMessage(error));
								alert.setReportInfo(error);
								alert.setRetryHandler(new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										drawNonAuthz();
										//UsersManager.resetNonAuthzPassword(i, m, passwordTextBox.getValue(), events);
									}
								});
								if (Window.Location.getParameterMap().containsKey("target_url")) {
									continueButton.setVisible(true);
								}
							}

							@Override
							public void onLoadingStart() {
								submit.setProcessing(true);
							}
						});

					}

				}
			});

		} else {

			draw();

			submit.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					if (validate()) {

						UsersManager.resetPassword(PerunSession.getInstance().getUserId(), namespace, passwordTextBox.getValue(), new JsonEvents() {

							final JsonEvents events = this;

							@Override
							public void onFinished(JavaScriptObject result) {
								submit.setProcessing(false);
								form.setVisible(false);
								alert.setType(AlertType.SUCCESS);
								alert.setText((isAccountActivation) ? translation.activateSuccess() : translation.resetSuccess());
								alert.setVisible(true);

								/**
								 * FIXME - Temporary change forcing all extending Metacentrum users to change their password due to need to re-sign all keys in kerberos database
								 */
								// For authorized reset in EINFRA (meta) namespace set sign to not bother users again in registrar
								if (Objects.equals("einfra", namespace) && PerunSession.getInstance().getUserId() > 0) {
									AttributesManager.getUserAttribute(PerunSession.getInstance().getUserId(), "urn:perun:user:attribute-def:def:changedPassMeta", new JsonEvents() {
										@Override
										public void onFinished(JavaScriptObject result) {
											Attribute attribute = result.cast();
											if (attribute.isEmpty()) {
												attribute.setValue(JsUtils.getCurrentDateString());
												AttributesManager.setUserAttribute(PerunSession.getInstance().getUserId(), attribute, null);
											}
										}
										@Override
										public void onError(PerunException error) {
										}

										@Override
										public void onLoadingStart() {
										}
									});
								}

								if (Window.Location.getParameterMap().containsKey("target_url")) {
									alert.getToolbar().setVisible(true);
									continueButton.setType(ButtonType.SUCCESS);
									continueButton.setVisible(true);
								}

							}

							@Override
							public void onError(PerunException error) {
								submit.setProcessing(false);
								form.setVisible(false);
								alert.setVisible(true);
								alert.setHTML(ErrorTranslator.getTranslatedMessage(error));
								alert.setReportInfo(error);
								alert.setRetryHandler(new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										draw();
										//UsersManager.resetPassword(PerunSession.getInstance().getUserId(), namespace, passwordTextBox.getValue(), events);
									}
								});
								if (Window.Location.getParameterMap().containsKey("target_url")) {
									continueButton.setVisible(true);
								}
							}

							@Override
							public void onLoadingStart() {
								submit.setProcessing(true);
							}
						});

					}

				}
			});
		}

	}

	public void draw() {

		alert.setVisible(false);

		if (Window.Location.getParameter("login-namespace") != null && !Window.Location.getParameter("login-namespace").isEmpty()) {
			namespace = SafeHtmlUtils.fromString(Window.Location.getParameter("login-namespace")).asString();
		} else {
			namespace = "";
		}

		AttributesManager.getLogins(PerunSession.getInstance().getUserId(), new JsonEvents() {

			final JsonEvents events = this;

			@Override
			public void onFinished(JavaScriptObject result) {

				loader.setVisible(false);
				loader.onFinished();

				logins = JsUtils.jsoAsList(result);
				if (logins != null && !logins.isEmpty()) {
					for (Attribute a : logins) {
						// if have login in namespace
						if (a.getFriendlyNameParameter().equals(namespace)) {
							boolean found = false;
							for (String name : PerunConfiguration.getSupportedPasswordNamespaces()) {
								if (a.getFriendlyNameParameter().equalsIgnoreCase(name)) {
									found = true;
								}
							}
							if (found) {
								// HAVE LOGIN AND SUPPORTED
								text.setText((isAccountActivation) ?
										translation.activateFor(a.getValue() + "@" + namespace.toUpperCase()) :
										translation.passwordResetFor(a.getValue() + "@" + namespace.toUpperCase()));
								form.setVisible(true);
								return;
							}
						}
					}

					if (!PerunConfiguration.getSupportedPasswordNamespaces().contains(namespace)) {
						// not supported
						alert.setVisible(true);
						alert.setHTML((isAccountActivation) ? translation.namespaceNotSupportedActive(namespace) : translation.namespaceNotSupported(namespace));
					} else {
						// doesn't have login
						alert.setVisible(true);
						alert.setHTML((isAccountActivation) ? translation.dontHaveLoginActive(namespace) : translation.dontHaveLogin(namespace));
					}

				}

			}

			@Override
			public void onError(PerunException error) {

				loader.onError(error, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						AttributesManager.getLogins(PerunSession.getInstance().getUserId(), events);
					}
				});

			}

			@Override
			public void onLoadingStart() {
				loader.onLoading();
			}

		});



	}

	/**
	 * Validate password form it values
	 *
	 * @return TRUE = valid / FALSE otherwise
	 */
	private boolean validate() {

		// TODO - per-namespace regex validation

		if (!Objects.equals(passwordTextBox.getValue(), passwordTextBox2.getValue())) {
			passItem.setValidationState(ValidationState.ERROR);
			itemStatus.setText(translation.passwordsDoesnMatch());
			return false;
		}
		if (passwordTextBox.getValue() == null || passwordTextBox.getValue().isEmpty()) {
			passItem.setValidationState(ValidationState.ERROR);
			itemStatus.setText(translation.passwordCantBeEmpty());
			return false;
		}

		itemStatus.setText("");
		passItem.setValidationState(ValidationState.SUCCESS);
		return true;

	}

	public void drawNonAuthz() {

		loader.setVisible(false);
		loader.onFinished();
		alert.setVisible(false);
		form.setVisible(true);

	}

}
