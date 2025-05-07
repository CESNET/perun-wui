package cz.metacentrum.perun.wui.consolidator.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.common.WayfGroup;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ColumnOffset;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.ArrayList;

/**
 * Widget representing WAYF (Where are you from) also called as "Discovery service".
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class Wayf extends Composite {

	private String redirect;
	private String token;

	Wayf wayf = this;

	@UiField Span mainContent;

	interface WayfUiBinder extends UiBinder<Widget, Wayf> {
	}

	private static WayfUiBinder ourUiBinder = GWT.create(WayfUiBinder.class);

	public Wayf() {
		this(null);
	}

	public Wayf(String redirect) {
		initWidget(ourUiBinder.createAndBindUi(this));
		this.redirect = redirect;
	}

	public void buildWayfGroups() {

		ArrayList<WayfGroup> wayfGroups = PerunConfiguration.getWayfGroups();
		Row row = new Row();
		mainContent.add(row);

		for (final WayfGroup group : wayfGroups) {

			// skip groups hidden on specific prefix
			String currentPrefix = PerunSession.getInstance().getRpcServer();
			if (group.hideOnPrefixes().contains(currentPrefix)) continue;

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
						Window.Location.assign(url);
					}
				});

			} else if (group.getAuthzType().equals("cert")) {

				groupButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent clickEvent) {
						// if we are on cert already, change hostname
						for (String hostname : PerunConfiguration.getWayfCertHostnames()) {
							if (!hostname.equals(Window.Location.getProtocol() + "//" + Window.Location.getHost())) {

								String url = hostname + "/"+group.getUrl()+"/ic/?token=" + token;
								if (redirect != null && !redirect.isEmpty()) {
									url = url + "&target_url=" + URL.encodeQueryString(redirect);
								}
								Window.Location.assign(url);
								return;
							}
						}

						// We use the same hostname
						String url = Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/"+group.getUrl()+"/ic/?token=" + token;
						if (redirect != null && !redirect.isEmpty()) {
							url = url + "&target_url=" + URL.encodeQueryString(redirect);
						}
						Window.Location.assign(url);
					}
				});

			} else if (group.getAuthzType().equals("fed")) {

				groupButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent clickEvent) {
						String target = "";
						if (redirect != null && !redirect.isEmpty()) {
							target = "&target_url=" + redirect;
						}

						// TODO - we won't support template in a future
						String consolidatorUrl = Utils.getIdentityConsolidatorLink(group.getUrl(), false) + URL.encodeQueryString("?token=" + token + target);

						String authnContextClassRef = PerunConfiguration.getAuthnContextClassRef();
						// button is single IdP - pass it to the proxy
						if (group.getIdpEntityID() != null && !group.getIdpEntityID().isEmpty()) {
							authnContextClassRef += "%20urn:cesnet:proxyidp:idpentityid:"+group.getIdpEntityID();
						}
						// button is "efilter" - pass it to the proxy
						if (group.getEFilter() != null && !group.getEFilter().isEmpty()) {
							authnContextClassRef += "%20urn:cesnet:proxyidp:efilter:"+Utils.getWayfEfilterURL(group.getEFilter());
						}

						// button is "filter" - pass it to the proxy
						if (group.getFilter() != null && !group.getFilter().isEmpty()) {
							authnContextClassRef += "%20urn:cesnet:proxyidp:filter:"+group.getFilter();
						}

						final String redirectUrl = PerunConfiguration.getWayfSpLogoutUrl() + "?return=" + PerunConfiguration.getWayfSpLoginUrl() + URL.encodeQueryString("?authnContextClassRef=" + authnContextClassRef +"&target="+consolidatorUrl);
						Window.Location.assign(redirectUrl);
					}
				});

			} else if (group.getAuthzType().equals("direct")) {

				groupButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent clickEvent) {
						// Directly redirect to URL with appending return url with token
						String target = "";
						if (redirect != null && !redirect.isEmpty()) {
							target = "&target_url=" + redirect;
						}
						String consolidatorUrl = Utils.getIdentityConsolidatorLink(group.getUrl(), false) + URL.encodeQueryString("?token=" + token + target);
						final String redirectUrl = PerunConfiguration.getWayfSpLogoutUrl() + "?return=" + consolidatorUrl;
						Window.Location.assign(redirectUrl);
					}
				});

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
	 * Set token for joining identities which will be used as a part
	 * of redirect URL when button clicked.
	 *
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}

}
