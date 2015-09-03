package cz.metacentrum.perun.wui.consolidator.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.http.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.consolidator.client.ConsolidatorTranslation;
import cz.metacentrum.perun.wui.consolidator.model.Feed;
import cz.metacentrum.perun.wui.consolidator.model.FeedFilter;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.JsonUtils;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Widget representing WAYF (Where are you from) also called as "Discovery service".
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Wayf extends Composite {

	private ConsolidatorTranslation translation = GWT.create(ConsolidatorTranslation.class);

	private int callCounter = 0;
	private HashMap<String, Feed> feeds = new HashMap<>();
	private FeedFilter filter;
	private String redirect;
	private String token;

	final ArrayList<Button> idpButtons = new ArrayList<Button>();
	final ArrayList<Button> socialButtons = new ArrayList<Button>();

	ScrollPanel idpGroup = new ScrollPanel();
	ScrollPanel socialGroup = new ScrollPanel();

	TextBox socialFilterBox = new TextBox();
	Modal socialModal = new Modal();

	TextBox idpFilterBox = new TextBox();
	Modal idpModal = new Modal();

	Wayf wayf = this;

	@UiField PerunButton idpButton;
	@UiField PerunButton certButton;
	@UiField PerunButton krbButton;
	@UiField PerunButton socialButton;

	@UiField Span mainContent;

	interface WayfUiBinder extends UiBinder<Widget, Wayf> {
	}

	private static WayfUiBinder ourUiBinder = GWT.create(WayfUiBinder.class);

	public Wayf() {
		this(Window.Location.getParameter("filter"), null);
	}

	public Wayf(String filter, String redirect) {
		initWidget(ourUiBinder.createAndBindUi(this));
		if (filter != null && !filter.isEmpty()) {
			setFilter(filter);
		}
		idpButton.setText(translation.idpButton());
		certButton.setText(translation.certButton());
		krbButton.setText(translation.krbButton());
		socialButton.setText(translation.socialButton());
		this.redirect = redirect;
		initModals();
	}

	/**
	 * Set filter for this WAYF
	 *
	 * @param filter filter encoded as base64 string
	 */
	public void setFilter(String filter) {
		this.filter = ((FeedFilter) JsonUtils.parseJson(JsUtils.decodeBase64(filter)));
	}

	/**
	 * Get FeedFiler object for this wayf
	 *
	 * @return filter
	 */
	public FeedFilter getFilter() {
		return filter;
	}

	/**
	 * Get all Feed data loaded to WAYF
	 *
	 * @return all Feed data
	 */
	public HashMap<String, Feed> getFeeds() {
		return feeds;
	}

	/**
	 * Set token for joining identities which will be used as a part
	 * of redirect URL when button clicked.
	 *
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	@UiHandler("idpButton")
	public void clickIdp(ClickEvent event) {
		idpModal.show();
		idpFilterBox.setFocus(true);
	}

	@UiHandler("socialButton")
	public void clickSocial(ClickEvent event) {

		socialModal.show();
		socialFilterBox.setFocus(true);

	}

	@UiHandler("certButton")
	public void certCert(ClickEvent event) {

		// if we are on cert already, change hostname
		if (Window.Location.getPath().startsWith("/cert/")) {
			for (String hostname : Utils.getCertWayfHostnames()) {
				if (!hostname.equals(Window.Location.getProtocol() + "//" + Window.Location.getHost())) {

					String url = hostname + "/cert/ic/?token=" + token;
					if (redirect != null && !redirect.isEmpty()) {
						url = url + "&target_url=" + URL.encodeQueryString(redirect);
					}
					Window.Location.replace(url);
					return;
				}
			}
		}

		// We use the same hostname
		String url = Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/cert/ic/?token=" + token;
		if (redirect != null && !redirect.isEmpty()) {
			url = url + "&target_url=" + URL.encodeQueryString(redirect);
		}
		Window.Location.replace(url);

	}

	@UiHandler("krbButton")
	public void clickKrb(ClickEvent event) {

		String url = Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/krb/ic/?token=" + token;
		if (redirect != null && !redirect.isEmpty()) {
			url = url + "&target_url=" + URL.encodeQueryString(redirect);
		}
		Window.Location.replace(url);

	}

	/**
	 * Load all WAYF data and display identity provider selection.
	 *
	 * @param extEvents Events done on callback
	 */
	public void loadWayf(final JsonEvents extEvents) {

		wayf.getFeeds(new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject jso) {

				extEvents.onFinished(jso);

				if (Utils.wayfShowAllInOne()) {
					buildAllInOne();
					return;
				}

				if (Utils.isCertWayfSupported()) {
					certButton.setVisible(true);
				}
				if (Utils.isKrbWayfSupported()) {
					krbButton.setVisible(true);
				}

				// prepare button groups

				VerticalButtonGroup btngrp = new VerticalButtonGroup();
				btngrp.setWidth("100%");
				idpGroup.add(btngrp);

				VerticalButtonGroup btngrp2 = new VerticalButtonGroup();
				btngrp2.setWidth("100%");
				socialGroup.add(btngrp2);

				// build filter
				buildFilterBox(idpFilterBox, idpButtons);
				buildFilterBox(socialFilterBox, socialButtons);

				// fill buttons
				ArrayList<String> fds = new ArrayList<String>();
				if (wayf.getFilter() == null || wayf.getFilter().getAllowedFeeds().isEmpty()) {
					fds = Utils.getWayfFeeds();
				} else {
					fds = wayf.getFilter().getAllowedFeeds();
				}

				for (String feedId : fds) {

					Feed feed = wayf.getFeeds().get(feedId);
					for (final String key : feed.getEntities().getKeys()) {

						if (wayf.getFilter() == null || wayf.getFilter().isIdPAllowed(key)) {

							Button button = new Button();
							button.setBlock(true);
							button.getElement().setAttribute("style", "text-align: left; vertical-align: middle; overflow-x: auto; word-wrap: break-word; white-space:normal !important; border-radius: 0px;");

							Image img = new Image(feed.getEntities().get(key).getLogoUrl());
							img.setPull(Pull.RIGHT);
							button.getElement().insertFirst(img.getElement());

							String label = feed.getEntities().get(key).getLabel("cs");
							if (label != null && !label.isEmpty()) {
								//oracle.add(label);
								button.setText(label);
							}
							button.getElement().insertFirst(img.getElement());

							if (feedId.equals("Social")) {
								btngrp2.add(button);
								socialButton.setVisible(true);
								socialButtons.add(button);

								GWT.log(key);

							} else {
								idpButton.setVisible(true);
								btngrp.add(button);
								idpButtons.add(button);
							}

							button.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									// hack to make extIdp feature to work
									String usedKey = key.replace("&amp;", "&");
									String consolidatorUrl = Utils.getIdentityConsolidatorLink("fed", false) + URL.encodeQueryString("?token=" + token);
									if (redirect != null && !redirect.isEmpty()) {
										consolidatorUrl = consolidatorUrl + "&target_url=" + URL.encodeQueryString(URL.encodeQueryString(redirect));
									}
									String redirectUrl = Utils.getWayfSpLogoutUrl() + "?return=" + Utils.getWayfSpDsUrl() + URL.encodeQueryString("?entityID=" + usedKey + "&target=" + consolidatorUrl);
									Window.Location.replace(redirectUrl);
								}
							});

						}

					}

				}

				if (idpButtons.size() > 7) {
					idpFilterBox.setVisible(true);
				} else {
					idpFilterBox.setVisible(false);
				}

				if (socialButtons.size() > 7) {
					socialFilterBox.setVisible(true);
				} else {
					socialFilterBox.setVisible(false);
				}

				wayf.setVisible(true);

			}

			@Override
			public void onError(PerunException error) {

			}

			@Override
			public void onLoadingStart() {

			}
		});

	}

	/**
	 * Init modal dialogs for IdP selection.
	 */
	private void initModals() {

		idpFilterBox.setWidth("450px");
		idpFilterBox.getElement().setAttribute("style", "margin: 5px 0px;");
		idpFilterBox.setPlaceholder(translation.typeToSearch());

		idpGroup.getElement().setAttribute("style", "margin-top: 10px; max-height: 460px;" + idpGroup.getElement().getAttribute("style"));
		idpGroup.setWidth("460px");

		idpModal.setTitle(translation.selectIdP());
		idpModal.setWidth("492px");
		idpModal.setDataBackdrop(ModalBackdrop.STATIC);

		ModalBody body = new ModalBody();
		body.add(idpFilterBox);
		body.add(idpGroup);

		idpModal.add(body);

		socialFilterBox.setWidth("450px");
		socialFilterBox.getElement().setAttribute("style", "margin: 5px 0px;");
		socialFilterBox.setPlaceholder(translation.typeToSearch());

		socialGroup.getElement().setAttribute("style", "margin-top: 10px; max-height: 460px;" + idpGroup.getElement().getAttribute("style"));
		socialGroup.setWidth("460px");

		socialModal.setTitle(translation.selectIdP());
		socialModal.setWidth("492px");
		socialModal.setDataBackdrop(ModalBackdrop.STATIC);

		ModalBody body2 = new ModalBody();
		body2.add(socialFilterBox);
		body2.add(socialGroup);

		socialModal.add(body2);

	}

	/**
	 * Assign search action above buttons to text box.
	 *
	 * @param textBox TexBox to assign action
	 * @param buttons Buttons to filter through
	 */
	private void buildFilterBox(final TextBox textBox, final ArrayList<Button> buttons) {

		textBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				for (Button butt : buttons) {

					if (textBox.getValue() == null || textBox.getValue().isEmpty()) {
						butt.setVisible(true);
						continue;
					}

					if (Utils.unAccent(butt.getText().replaceAll(" ", "").toLowerCase()).contains(Utils.unAccent(textBox.getValue().toLowerCase()))) {
						butt.setVisible(true);
					} else {
						butt.setVisible(false);
					}

				}
			}
		});

	}

	/**
	 * Build WAYF as all in one (usefull for only-fed authz)
	 */
	private void buildAllInOne() {

		mainContent.clear();

		idpFilterBox.setWidth("450px");
		idpFilterBox.getElement().setAttribute("style", "margin: 5px 0px;");
		idpFilterBox.setPlaceholder(translation.typeToSearch());

		idpGroup.getElement().setAttribute("style", "margin-top: 10px; max-height: 460px;" + idpGroup.getElement().getAttribute("style"));
		idpGroup.setWidth("460px");

		VerticalButtonGroup btngrp = new VerticalButtonGroup();
		btngrp.setWidth("100%");
		idpGroup.add(btngrp);

		buildFilterBox(idpFilterBox, idpButtons);

		// fill buttons
		ArrayList<String> fds = new ArrayList<String>();
		if (wayf.getFilter() == null || wayf.getFilter().getAllowedFeeds().isEmpty()) {
			fds = Utils.getWayfFeeds();
		} else {
			fds = wayf.getFilter().getAllowedFeeds();
		}

		for (String feedId : fds) {

			Feed feed = wayf.getFeeds().get(feedId);
			for (final String key : feed.getEntities().getKeys()) {

				if (wayf.getFilter() == null || wayf.getFilter().isIdPAllowed(key)) {

					Button button = new Button();
					button.setBlock(true);
					button.getElement().setAttribute("style", "text-align: left; vertical-align: middle; overflow-x: auto; word-wrap: break-word; white-space:normal !important; border-radius: 0px;");

					Image img = new Image(feed.getEntities().get(key).getLogoUrl());
					img.setPull(Pull.RIGHT);
					button.getElement().insertFirst(img.getElement());

					String label = feed.getEntities().get(key).getLabel("cs");
					if (label != null && !label.isEmpty()) {
						//oracle.add(label);
						button.setText(label);
					}
					button.getElement().insertFirst(img.getElement());

					idpButton.setVisible(true);
					btngrp.add(button);
					idpButtons.add(button);

					button.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							String consolidatorUrl = Utils.getIdentityConsolidatorLink("fed", false) + URL.encodeQueryString("?token=" + token);
							if (redirect != null && !redirect.isEmpty()) {
								consolidatorUrl = consolidatorUrl + "&target_url=" + URL.encodeQueryString(URL.encodeQueryString(redirect));
							}
							String redirectUrl = Utils.getWayfSpLogoutUrl() + "?return=" + Utils.getWayfSpDsUrl() + URL.encodeQueryString("?entityID=" + key + "&target=" + consolidatorUrl);
							Window.Location.replace(redirectUrl);
						}
					});

				}

			}

		}

		if (idpButtons.size() > 7) {
			idpFilterBox.setVisible(true);
		} else {
			idpFilterBox.setVisible(false);
		}

		mainContent.add(idpFilterBox);
		mainContent.add(idpGroup);

		Scheduler.get().scheduleDeferred(new Command() {
			@Override
			public void execute() {
				idpFilterBox.setFocus(true);
			}
		});

		wayf.setVisible(true);

	}

	/**
	 * Load all pre-configured IdP feeds from it's source
	 *
	 * @param events Events done on callback results
	 */
	private void getFeeds(final JsonEvents events) {

		ArrayList<String> fds = new ArrayList<String>();

		if (filter == null || filter.getAllowedFeeds().isEmpty()) {
			fds.addAll(Utils.getWayfFeeds());
		} else {
			fds.addAll(filter.getAllowedFeeds());
		}

		for (final String feed : fds) {

			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Utils.getWayfFeedUrl() + feed + ".js");
			callCounter++;
			try {
				builder.sendRequest(null, new RequestCallback() {
							@Override
							public void onResponseReceived(Request request, Response response) {
								feeds.put(feed, (Feed)convertFeed(response.getText()));
								callCounter--;
								if (callCounter == 0) {
									events.onFinished(null);
								}
							}

							@Override
							public void onError(Request request, Throwable exception) {
								callCounter--;
								if (callCounter == 0) {
									events.onError(null);
								}
							}
						}
				);
			} catch (RequestException e) {
				e.printStackTrace();
			}
		}

		// load wayf without FED feeds
		if (fds.isEmpty()) {
			events.onFinished(null);
		}

	}

	/**
	 * Convert native IdP feed data from JSON to GWTs OverlayObject
	 * @param feed feed sources
	 * @return Feed overlay type
	 */
	private final native JavaScriptObject convertFeed(String feed) /*-{
		var object = $wnd.jQuery.parseJSON(feed);
		return object;
	}-*/;

}
