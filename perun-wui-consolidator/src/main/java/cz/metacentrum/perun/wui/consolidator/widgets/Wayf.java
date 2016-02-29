package cz.metacentrum.perun.wui.consolidator.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
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
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import cz.metacentrum.perun.wui.widgets.boxes.ExtendedTextBox;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ColumnOffset;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
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
		Row row = new Row();
		mainContent.add(row);

		for (final WayfGroup group : wayfGroups) {

			int size = wayfGroups.size();

			// If IdP is allowed, build button
			final WayfGroupButton groupButton = new WayfGroupButton(group);

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

					groupButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent clickEvent) {

							// Build widgets
							final ArrayList<Button> idpButtons = new ArrayList<Button>();

							final InputGroup inputGroup = new InputGroup();
							InputGroupAddon inputGroupAddonSearch = new InputGroupAddon();

							inputGroupAddonSearch.setIcon(IconType.SEARCH);
							inputGroupAddonSearch.setIconFixedWidth(true);

							final ExtendedTextBox idpFilterBox = new ExtendedTextBox();
							final Modal idpModal = new Modal();

							inputGroup.add(inputGroupAddonSearch);
							inputGroup.add(idpFilterBox);

							ScrollPanel idpGroup = new ScrollPanel();
							final VerticalButtonGroup btngrp = new VerticalButtonGroup();
							//btngrp.setWidth("100%");
							btngrp.getElement().setAttribute("style", "margin-top: 5px; width: 100%");
							idpGroup.add(btngrp);

							final PerunLoader loader = new PerunLoader();
							loader.onLoading(translation.loadingOrganizations());
							btngrp.add(loader);

							// build filter
							buildFilterBox(idpFilterBox, idpButtons, btngrp);

							idpFilterBox.setPlaceholder(translation.searchYouOrganization());
							idpFilterBox.setEnabled(false);

							idpGroup.getElement().setAttribute("style", "margin-top: 10px; max-height: 460px;" + idpGroup.getElement().getAttribute("style"));
							idpGroup.setWidth("460px");

							idpModal.setTitle(translation.selectIdP());
							idpModal.setWidth("492px");
							idpModal.setDataBackdrop(ModalBackdrop.STATIC);

							ModalBody body = new ModalBody();
							body.add(inputGroup);
							body.add(idpGroup);

							idpModal.add(body);

							idpModal.show();
							idpFilterBox.setFocus(true);

							// load feed data from IdP/SP federation
							getFeeds(group, new Events<FeedEntities>() {

								@Override
								public void onFinished(FeedEntities result) {

									loader.onFinished();
									loader.removeFromParent();

									idpFilterBox.setEnabled(true);
									idpFilterBox.setFocus(true);

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

											String label = result.get(key).getLabel(PerunConfiguration.getCurrentLocaleName());
											if (label == null || label.isEmpty()) result.get(key).getLabel("en");
											if (label != null && !label.isEmpty()) {
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
											inputGroup.setVisible(true);
										} else if (idpButtons.size() > 0){
											inputGroup.setVisible(false);
										} else {
											inputGroup.setVisible(false);
											Heading notFound = new Heading(HeadingSize.H4, translation.noOrganizationFound());
											btngrp.add(notFound);
										}

									}

								}

								@Override
								public void onError(PerunException error) {
									// We can never get perun exception here since we load custom URL JSON data
								}

								@Override
								public void onLoadingStart() {
									//
								}
							});

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
							Window.Location.replace(redirectUrl);
						}
					});

				}

			}

			// nice align when less then 3 columns
			if (size >= 3) {
				Column col = new Column(ColumnSize.MD_4, ColumnSize.LG_4, ColumnSize.SM_6, ColumnSize.XS_12);
				col.add(groupButton.getWidget());
				row.add(col);
			} else {
				Column col = new Column(ColumnSize.MD_4, ColumnSize.LG_4, ColumnSize.SM_6, ColumnSize.XS_12);
				col.setOffset(ColumnOffset.MD_4, ColumnOffset.LG_4, ColumnOffset.SM_3);
				col.add(groupButton.getWidget());
				row.add(col);
			}

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
	 * @param btngrp Buttons group wrapper
	 */
	private void buildFilterBox(final ExtendedTextBox textBox, final ArrayList<Button> buttons, final VerticalButtonGroup btngrp) {

		// Timer for delayed search on type
		final Timer timer = new Timer() {
			@Override
			public void run() {
				ValueChangeEvent.fire(textBox, textBox.getValue());
			}
		};
		timer.schedule(500);

		// typing triggers search
		textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {

				if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
					// trigger now
					timer.run();
				} else if (KeyCodes.KEY_ESCAPE == event.getNativeKeyCode() ||
						KeyCodes.KEY_LEFT == event.getNativeKeyCode() ||
						KeyCodes.KEY_UP == event.getNativeKeyCode() ||
						KeyCodes.KEY_RIGHT == event.getNativeKeyCode() ||
						KeyCodes.KEY_DOWN == event.getNativeKeyCode() ||
						KeyCodes.KEY_SHIFT == event.getNativeKeyCode() ||
						KeyCodes.KEY_CTRL == event.getNativeKeyCode() ||
						KeyCodes.KEY_ALT == event.getNativeKeyCode() ||
						KeyCodes.KEY_CAPS_LOCK == event.getNativeKeyCode() ||
						KeyCodes.KEY_SHIFT == event.getNativeKeyCode() ||
						KeyCodes.KEY_WIN_IME == event.getNativeKeyCode() ||
						KeyCodes.KEY_WIN_KEY == event.getNativeKeyCode() ||
						KeyCodes.KEY_WIN_KEY_FF_LINUX == event.getNativeKeyCode() ||
						KeyCodes.KEY_WIN_KEY_LEFT_META == event.getNativeKeyCode() ||
						KeyCodes.KEY_WIN_KEY_RIGHT == event.getNativeKeyCode() ||
						KeyCodes.KEY_CONTEXT_MENU == event.getNativeKeyCode()) {
					return;
					// skip timer
				} else {
					// delay timer
					timer.schedule(500);
				}

			}
		});

		final Heading notFound = new Heading(HeadingSize.H4, translation.noOrganizationFound());

		// search on value change (called manually also by typing and timer)
		textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				if (textBox.getValue() != null && !textBox.getValue().isEmpty() && textBox.getValue().length() < 2) return;

				boolean found = false;

				for (Button butt : buttons) {

					if (textBox.getValue() == null || textBox.getValue().isEmpty()) {
						butt.setVisible(true);
						found = true;
						continue;
					}

					if (Utils.unAccent(butt.getText().replaceAll(" ", "").toLowerCase()).contains(Utils.unAccent(textBox.getValue().toLowerCase()))) {
						butt.setVisible(true);
						found = true;
					} else {
						butt.setVisible(false);
					}

				}

				if (!found) {
					btngrp.add(notFound);
				} else {
					notFound.removeFromParent();
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
