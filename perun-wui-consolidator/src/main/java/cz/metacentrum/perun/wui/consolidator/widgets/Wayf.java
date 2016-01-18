package cz.metacentrum.perun.wui.consolidator.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.consolidator.client.resources.PerunConsolidatorTranslation;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.model.common.Feed;
import cz.metacentrum.perun.wui.model.common.FeedEntities;
import cz.metacentrum.perun.wui.model.common.FeedFilter;
import cz.metacentrum.perun.wui.json.JsonUtils;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.common.WayfGroup;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.VerticalButtonGroup;
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

	private PerunConsolidatorTranslation translation = GWT.create(PerunConsolidatorTranslation.class);

	private HashMap<String, Integer> callCounter = new HashMap<>();
	private FeedFilter filter;
	private String redirect;
	private String token;

	Wayf wayf = this;

	@UiField VerticalButtonGroup buttonGroups;

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

		this.redirect = redirect;

	}

	public void buildWayfGroups() {

		ArrayList<WayfGroup> wayfGroups = PerunConfiguration.getWayfGroups();

		for (final WayfGroup group : wayfGroups) {

			// If IdP is allowed, build button
			final Button groupButton = new Button();
			groupButton.setBlock(true);
			groupButton.getElement().setAttribute("style", "text-align: center; vertical-align: middle; overflow-x: auto; word-wrap: break-word; white-space:normal !important; border-radius: 0px;");

			String locale = PerunConfiguration.getCurrentLocaleName();

			String uri = group.getIconUrl();
			if (uri != null) {
				Image img = new Image();
				img.getElement().setAttribute("src", uri);
				img.getElement().setAttribute("height", "40px");
				//img.setPull(Pull.LEFT);
				groupButton.getElement().insertFirst(img.getElement());
				groupButton.setTitle(group.getName(locale));
			} else {
				groupButton.setText(group.getName(locale));
			}

			//groupButton.getElement().insertFirst(img.getElement());


			if (group.getAuthzType().equals("krb")) {

				groupButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent clickEvent) {
						String url = Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/"+group.getUrl()+"/ic/?token=" + token;
						if (redirect != null && !redirect.isEmpty()) {
							url = url + "&target_url=" + URL.encodeQueryString(redirect);
						}
						Window.Location.replace(url);
					}
				});

			} else if (group.getAuthzType().equals("cert")) {

				groupButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent clickEvent) {
						// if we are on cert already, change hostname
						if (Window.Location.getPath().startsWith("/"+group.getUrl()+"/")) {
							for (String hostname : PerunConfiguration.getWayfCertHostnames()) {
								if (!hostname.equals(Window.Location.getProtocol() + "//" + Window.Location.getHost())) {

									String url = hostname + "/"+group.getUrl()+"/ic/?token=" + token;
									if (redirect != null && !redirect.isEmpty()) {
										url = url + "&target_url=" + URL.encodeQueryString(redirect);
									}
									Window.Location.replace(url);
									return;
								}
							}
						}

						// We use the same hostname
						String url = Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/"+group.getUrl()+"/ic/?token=" + token;
						if (redirect != null && !redirect.isEmpty()) {
							url = url + "&target_url=" + URL.encodeQueryString(redirect);
						}
						Window.Location.replace(url);
					}
				});

			} else if (group.getAuthzType().equals("fed")) {

				// load external feed data
				if (group.getFeeds() != null && !group.getFeeds().isEmpty()) {

					// wait to load feeds
					groupButton.setEnabled(false);

					// load feed data from IdP/SP federation
					getFeeds(group, new Events<FeedEntities>() {
						@Override
						public void onFinished(FeedEntities result) {

							groupButton.setEnabled(true);

							// Build widgets
							final TextBox idpFilterBox = new TextBox();
							final Modal idpModal = new Modal();
							final ArrayList<Button> idpButtons = new ArrayList<Button>();

							ScrollPanel idpGroup = new ScrollPanel();
							final VerticalButtonGroup btngrp = new VerticalButtonGroup();
							btngrp.setWidth("100%");
							idpGroup.add(btngrp);
							// build filter
							buildFilterBox(idpFilterBox, idpButtons);

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

							// get sorted idp keys
							for (final String key : result.getKeys()) {

								if (wayf.getFilter() == null || wayf.getFilter().isIdPAllowed(key)) {

									// If IdP is allowed, build button
									Button button = new Button();
									button.setBlock(true);
									button.getElement().setAttribute("style", "text-align: left; vertical-align: middle; overflow-x: auto; word-wrap: break-word; white-space:normal !important; border-radius: 0px;");

									Image img = new Image(result.get(key).getLogoUrl());
									img.setPull(Pull.RIGHT);
									button.getElement().insertFirst(img.getElement());

									String label = result.get(key).getLabel("cs");
									if (label != null && !label.isEmpty()) {
										//oracle.add(label);
										button.setText(label);
									}
									button.getElement().insertFirst(img.getElement());

									btngrp.add(button);
									idpButtons.add(button);

									button.addClickHandler(new ClickHandler() {
										@Override
										public void onClick(ClickEvent event) {
											// hack to make extIdp feature to work
											String usedKey = key.replace("&amp;", "&");
											String target = "";
											if (redirect != null && !redirect.isEmpty()) {
												target = "&target_url=" + redirect;
											}
											String consolidatorUrl = Utils.getIdentityConsolidatorLink("fed", false) + URL.encodeQueryString("?token=" + token + target);
											final String redirectUrl = PerunConfiguration.getWayfSpLogoutUrl() + "?return=" + PerunConfiguration.getWayfSpDsUrl() + URL.encodeQueryString("?entityID=" + usedKey + "&target=" + consolidatorUrl);
											Window.Location.replace(redirectUrl);
										}
									});

								}

								if (idpButtons.size() > 7) {
									idpFilterBox.setVisible(true);
								} else {
									idpFilterBox.setVisible(false);
								}

								// add click to group button
								if (idpButtons.isEmpty()) {
									groupButton.setEnabled(false);
								} else {
									groupButton.addClickHandler(new ClickHandler() {
										@Override
										public void onClick(ClickEvent clickEvent) {
											idpModal.show();
											idpFilterBox.setFocus(true);
										}
									});
								}

							}
						}

						@Override
						public void onError(PerunException error) {
							// TODO - feed not loaded for button, show error ?
							groupButton.setEnabled(false);
						}

						@Override
						public void onLoadingStart() {
							//
						}
					});



				} else {

					// group is single item IdP
					groupButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent clickEvent) {
							String target = "";
							if (redirect != null && !redirect.isEmpty()) {
								target = "&target_url=" + redirect;
							}
							String consolidatorUrl = Utils.getIdentityConsolidatorLink(group.getUrl(), false) + URL.encodeQueryString("?token=" + token + target);
							final String redirectUrl = PerunConfiguration.getWayfSpLogoutUrl() + "?return=" + consolidatorUrl;
							Window.alert(redirectUrl + "\n" + group.getUrl());
							Window.Location.replace(redirectUrl);
						}
					});

				}

			}

			buttonGroups.add(groupButton);

		}

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
	 * Set token for joining identities which will be used as a part
	 * of redirect URL when button clicked.
	 *
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
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
	 * Load all pre-configured IdP feeds from it's source
	 *
	 * @param events Events done on callback results
	 */
	private void getFeeds(final WayfGroup group, final Events<FeedEntities> events) {

		final FeedEntities entities = FeedEntities.createNew();
		final String groupName = group.getName();
		callCounter.put(groupName, 0);

		// decide feeds
		ArrayList<String> fds = new ArrayList<String>();
		if (wayf.getFilter() == null || wayf.getFilter().getAllowedFeeds().isEmpty()) {
			fds = Utils.stringToList(group.getFeeds(), ",");
		} else {
			// keep only allowed
			fds = Utils.stringToList(group.getFeeds(), ",");
			fds.retainAll(wayf.getFilter().getAllowedFeeds());
		}

		for (final String feed : fds) {

			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, PerunConfiguration.getWayfFeedUrl() + feed + ".js");
			callCounter.put(groupName, callCounter.get(groupName)+1);
			try {
				builder.sendRequest(null, new RequestCallback() {
							@Override
							public void onResponseReceived(Request request, Response response) {
								Feed feed = (Feed)convertFeed(response.getText());
								for (String key :feed.getEntities().getKeys()) {
									entities.set(key, feed.getEntities().get(key));
								}
								callCounter.put(groupName, callCounter.get(groupName)-1);
								if (callCounter.get(groupName) == 0) {
									// append local data
									for (String key : group.getFeedData().getKeys()) {
										entities.set(key, group.getFeedData().get(key));
									}
									events.onFinished(entities);
								}
							}

							@Override
							public void onError(Request request, Throwable exception) {
								callCounter.put(groupName, callCounter.get(groupName)-1);
								if (callCounter.get(groupName) == 0) {
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
			// load only local data
			events.onFinished(group.getFeedData());
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
