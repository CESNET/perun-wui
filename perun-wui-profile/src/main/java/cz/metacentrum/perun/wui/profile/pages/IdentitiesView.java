package cz.metacentrum.perun.wui.profile.pages;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ButtonCellBase;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextButtonCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.Events;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.ColumnProvider;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ExtSource;
import cz.metacentrum.perun.wui.model.beans.ExtSource.ExtSourceType;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.model.beans.User;
import cz.metacentrum.perun.wui.model.beans.UserExtSource;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.base.button.AbstractButton;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View for displaying VO membership details
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class IdentitiesView extends ViewWithUiHandlers<IdentitiesUiHandlers> implements IdentitiesPresenter.MyView {

	interface IdentitiesViewUiBinder extends UiBinder<Widget, IdentitiesView> {
	}

	private PerunProfileTranslation translation = GWT.create(PerunProfileTranslation.class);

	@UiField PerunLoader loader;
	@UiField Text text;
	@UiField Small small;
	@UiField CellTable<UserExtSource> federatedIdentitiesTable;
	@UiField PerunButton addFedBtn;
	@UiField CellTable<UserExtSource> x509IdentitiesTable;
	@UiField PerunButton addCertBtn;


	@Inject
	public IdentitiesView(IdentitiesViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));


		text.setText(translation.menuMyIdentities());
		addFedBtn.setText(translation.addFed());
		addCertBtn.setText(translation.addCert());


		TextColumn<UserExtSource> nameCol = new TextColumn<UserExtSource>() {
			@Override
			public String getValue(UserExtSource userExtSource) {
				return getFriendlyExtSourceName(userExtSource);
			}
		};
		TextColumn<UserExtSource> loginCol = new TextColumn<UserExtSource>() {
			@Override
			public String getValue(UserExtSource userExtSource) {
				return userExtSource.getLogin();
			}
		};
		Column<UserExtSource, String> removeColumn = new Column<UserExtSource, String>(
				new ButtonCell(ButtonType.DANGER, ButtonSize.EXTRA_SMALL)) {
			@Override
			public String getValue(final UserExtSource extSource) {
				return "✖";
			}
		};
		removeColumn.setFieldUpdater(new FieldUpdater<UserExtSource, String>() {
			@Override
			public void update(int i, UserExtSource userExtSource, String buttonText) {
				getUiHandlers().removeUserExtSource(userExtSource);
			}
		});


		PerunLoader plFed = new PerunLoader();
		plFed.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
		federatedIdentitiesTable.setEmptyTableWidget(plFed);
		federatedIdentitiesTable.addColumn(nameCol, translation.federatedIdp());
		federatedIdentitiesTable.addColumn(loginCol, translation.federatedLogin());
		federatedIdentitiesTable.addColumn(removeColumn);
		federatedIdentitiesTable.setColumnWidth(federatedIdentitiesTable.getColumnCount()-1, "10%");


		PerunLoader plCert = new PerunLoader();
		plCert.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
		x509IdentitiesTable.setEmptyTableWidget(plCert);
		x509IdentitiesTable.addColumn(nameCol, translation.x509Issuer());
		x509IdentitiesTable.addColumn(loginCol, translation.x509Identity());
		x509IdentitiesTable.addColumn(removeColumn);
		x509IdentitiesTable.setColumnWidth(x509IdentitiesTable.getColumnCount()-1, "10%");


		ClickHandler icLinkHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				getUiHandlers().addUserExtSource();
			}
		};

		addFedBtn.addClickHandler(icLinkHandler);
		addCertBtn.addClickHandler(icLinkHandler);
	}



	@Override
	public void setUserExtSources(List<UserExtSource> userExtSources) {
		List<UserExtSource> federatedIdentities = new ArrayList<>();
		List<UserExtSource> x509Identities = new ArrayList<>();
		for (UserExtSource es : userExtSources) {
			if (ExtSourceType.IDP.getType().equals(es.getExtSource().getType())) {
				federatedIdentities.add(es);
			} else if (ExtSourceType.X509.getType().equals(es.getExtSource().getType())) {
				x509Identities.add(es);
			}
		}
		((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onEmpty();
		federatedIdentitiesTable.setRowData(federatedIdentities);
		((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onEmpty();
		x509IdentitiesTable.setRowData(x509Identities);
	}

	@Override
	public void removingUserExtSourceStart(UserExtSource userExtSource) {
		if (ExtSourceType.IDP.getType().equals(userExtSource.getExtSource().getType())) {
			federatedIdentitiesTable.setRowData(new ArrayList<UserExtSource>());
			((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onLoading(translation.removingIdentity());
		} else if (ExtSourceType.X509.getType().equals(userExtSource.getExtSource().getType())) {
			x509IdentitiesTable.setRowData(new ArrayList<UserExtSource>());
			((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onLoading(translation.removingIdentity());
		}
	}

	@Override
	public void removingUserExtSourceError(PerunException ex, final UserExtSource userExtSource) {
		ClickHandler retry = new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				getUiHandlers().removeUserExtSource(userExtSource);
			}
		};
		if (ExtSourceType.IDP.getType().equals(userExtSource.getExtSource().getType())) {
			federatedIdentitiesTable.setRowData(new ArrayList<UserExtSource>());
			((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onError(ex, retry);
		} else if (ExtSourceType.X509.getType().equals(userExtSource.getExtSource().getType())) {
			x509IdentitiesTable.setRowData(new ArrayList<UserExtSource>());
			((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onError(ex, retry);
		}
	}

	@Override
	public void loadingUserExtSourcesStart() {
		federatedIdentitiesTable.setRowData(new ArrayList<UserExtSource>());
		((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onLoading(translation.loadingIdentities());
		x509IdentitiesTable.setRowData(new ArrayList<UserExtSource>());
		((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onLoading(translation.loadingIdentities());
	}

	@Override
	public void loadingUserExtSourcesError(PerunException ex) {
		ClickHandler retry = new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				getUiHandlers().loadUserExtSources();
			}
		};
		federatedIdentitiesTable.setRowData(new ArrayList<UserExtSource>());
		((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onError(ex, retry);
		x509IdentitiesTable.setRowData(new ArrayList<UserExtSource>());
		((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onError(ex, retry);
	}



	private String getFriendlyExtSourceName(UserExtSource userExtSource) {
		ExtSource extSource = userExtSource.getExtSource();

		if (ExtSourceType.IDP.getType().equals(extSource.getType())) {

			if  (fedNameDict.containsKey(extSource.getName())) {
				return fedNameDict.get(extSource.getName());
			}

			if  (extSource.getName().equals("https://extidp.cesnet.cz/idp/shibboleth")) {
				GWT.log("log");
				String socialId = userExtSource.getLogin().split("@")[1];
				if  (socialNameDict.containsKey(socialId)) {
					return socialNameDict.get(socialId);
				}
			}
		}
		return extSource.getName();
	}

	private static final Map<String, String> fedNameDict;
	static
	{
		fedNameDict = new HashMap<>();
		fedNameDict.put("https://idp.upce.cz/idp/shibboleth", "University in Pardubice");
		fedNameDict.put("https://idp.slu.cz/idp/shibboleth", "University in Opava");
		fedNameDict.put("https://login.feld.cvut.cz/idp/shibboleth", "Faculty of Electrical Engineering, Czech Technical University In Prague");
		fedNameDict.put("https://www.vutbr.cz/SSO/saml2/idp", "Brno University of Technology");
		fedNameDict.put("https://shibboleth.nkp.cz/idp/shibboleth", "The National Library of the Czech Republic");
		fedNameDict.put("https://idp2.civ.cvut.cz/idp/shibboleth", "Czech Technical University In Prague");
		fedNameDict.put("https://shibbo.tul.cz/idp/shibboleth", "Technical University of Liberec");
		fedNameDict.put("https://idp.mendelu.cz/idp/shibboleth", "Mendel University in Brno");
		fedNameDict.put("https://cas.cuni.cz/idp/shibboleth", "Charles University in Prague");
		fedNameDict.put("https://wsso.vscht.cz/idp/shibboleth", "Institute of Chemical Technology Prague");
		fedNameDict.put("https://idp.vsb.cz/idp/shibboleth", "VSB – Technical University of Ostrava");
		fedNameDict.put("https://whoami.cesnet.cz/idp/shibboleth", "CESNET");
		fedNameDict.put("https://helium.jcu.cz/idp/shibboleth", "University of South Bohemia");
		fedNameDict.put("https://idp.ujep.cz/idp/shibboleth", "Jan Evangelista Purkyne University in Usti nad Labem");
		fedNameDict.put("https://idp.amu.cz/idp/shibboleth", "Academy of Performing Arts in Prague");
		fedNameDict.put("https://idp.lib.cas.cz/idp/shibboleth", "Academy of Sciences Library");
		fedNameDict.put("https://shibboleth.mzk.cz/simplesaml/metadata.xml", "Moravian  Library");
		fedNameDict.put("https://idp2.ics.muni.cz/idp/shibboleth", "Masaryk University");
		fedNameDict.put("https://idp.upol.cz/idp/shibboleth", "Palacky University, Olomouc");
		fedNameDict.put("https://idp.fnplzen.cz/idp/shibboleth", "FN Plzen");
		fedNameDict.put("https://id.vse.cz/idp/shibboleth", "University of Economics, Prague");
		fedNameDict.put("https://shib.zcu.cz/idp/shibboleth", "University of West Bohemia");
		fedNameDict.put("https://idptoo.osu.cz/simplesaml/saml2/idp/metadata.php", "University of Ostrava");
		fedNameDict.put("https://login.ics.muni.cz/idp/shibboleth", "MetaCentrum");
		fedNameDict.put("https://idp.hostel.eduid.cz/idp/shibboleth", "eduID.cz Hostel");
		fedNameDict.put("https://shibboleth.techlib.cz/idp/shibboleth", "National Library of Technology");
		fedNameDict.put("https://www.egi.eu/idp/shibboleth", "EGI SSO");
		fedNameDict.put("https://engine.elixir-czech.org/authentication/idp/metadata", "ELIXIR ID");
	}

	private static final Map<String, String> socialNameDict;
	static
	{
		socialNameDict = new HashMap<>();
		socialNameDict.put("google.extidp.cesnet.cz", "Google");
		socialNameDict.put("facebook.extidp.cesnet.cz", "Facebook");
		socialNameDict.put("mojeid.extidp.cesnet.cz", "mojeID");
		socialNameDict.put("linkedin.extidp.cesnet.cz", "LinkedIn");
		socialNameDict.put("twitter.extidp.cesnet.cz", "Twitter");
		socialNameDict.put("seznam.extidp.cesnet.cz", "Seznam");
	}

}