package cz.metacentrum.perun.wui.profile.pages.identities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.ExtSource.ExtSourceType;
import cz.metacentrum.perun.wui.profile.model.beans.RichUserExtSource;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * View for displaying OrganizationsView membership details
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
	@UiField CellTable<RichUserExtSource> federatedIdentitiesTable;
	@UiField PerunButton addFedBtn;
	@UiField CellTable<RichUserExtSource> x509IdentitiesTable;
	@UiField PerunButton addCertBtn;
	@UiField CellTable<RichUserExtSource> otherIdentitiesTable;
	@UiField Modal otherUesModal;
	@UiField Button showOtherUesButton;

	private List<RichUserExtSource> federatedIdentities = new ArrayList<>();
	private List<RichUserExtSource> x509Identities = new ArrayList<>();
	private List<RichUserExtSource> otherIdentities = new ArrayList<>();

	private static final String EMAIL_ATTRIBUTE = "urn:perun:ues:attribute-def:def:mail";
	private static final String ORIGIN_ENTITY_ID_ATTRIBUTE = "urn:perun:ues:attribute-def:def:authenticating-authority";
	private static final String EPPN_ATTRIBUTE = "urn:perun:ues:attribute-def:def:eppn";

	@Inject
	public IdentitiesView(IdentitiesViewUiBinder binder) {
		initWidget(binder.createAndBindUi(this));

		text.setText(translation.menuMyIdentities());
		addFedBtn.setText(translation.addFed());
		addCertBtn.setText(translation.addCert());
		otherUesModal.setTitle(translation.otherIdentities());
		showOtherUesButton.setText(translation.otherIdentities());

		TextColumn<RichUserExtSource> emailCol = new TextColumn<RichUserExtSource>() {
			@Override
			public String getValue(RichUserExtSource rues) {
				if (rues.getAttribute(EMAIL_ATTRIBUTE) == null || rues.getAttribute(EMAIL_ATTRIBUTE).isEmpty()) {
					return "N/A";
				} else {
					return rues.getAttribute(EMAIL_ATTRIBUTE).getValue();
				}
			}
		};
		TextColumn<RichUserExtSource> nameCol = new TextColumn<RichUserExtSource>() {
			@Override
			public String getValue(RichUserExtSource userExtSource) {
				if (ExtSourceType.IDP.getType().equals(userExtSource.getUes().getExtSource().getType())) {
					if (userExtSource.getUes().getExtSource().getName().equals("https://extidp.cesnet.cz/idp/shibboleth") ||
							userExtSource.getUes().getExtSource().getName().equals("https://login.elixir-czech.org/idp/")) {
						// hack our social IdP so we can tell from where identity is
						return Utils.translateIdp("@"+userExtSource.getUes().getLogin().split("@")[1]);
					}

					// show source entityId for proxy of EduTeams
					if (userExtSource.getAttribute(ORIGIN_ENTITY_ID_ATTRIBUTE) != null &&
							userExtSource.getAttribute(ORIGIN_ENTITY_ID_ATTRIBUTE).getValue() != null) {
						return Utils.translateIdp(userExtSource.getAttribute(ORIGIN_ENTITY_ID_ATTRIBUTE).getValue());
					}

					return Utils.translateIdp(userExtSource.getUes().getExtSource().getName());

				} else if (ExtSourceType.X509.getType().equals(userExtSource.getUes().getExtSource().getType())) {
					return getCertParam(userExtSource.getUes().getExtSource().getName(), "O") +
							", " +
							getCertParam(userExtSource.getUes().getExtSource().getName(), "CN");
				} else {
					return userExtSource.getUes().getExtSource().getName();
				}
			}
		};
		TextColumn<RichUserExtSource> loginCol = new TextColumn<RichUserExtSource>() {
			@Override
			public String getValue(RichUserExtSource userExtSource) {
				if (ExtSourceType.IDP.getType().equals(userExtSource.getUes().getExtSource().getType())) {

					// show source EPPN for proxy of EduTeams
					if (userExtSource.getAttribute(ORIGIN_ENTITY_ID_ATTRIBUTE) != null &&
							userExtSource.getAttribute(ORIGIN_ENTITY_ID_ATTRIBUTE).getValue() != null) {
						// get eppn if available
						if (userExtSource.getAttribute(EPPN_ATTRIBUTE) != null &&
								userExtSource.getAttribute(EPPN_ATTRIBUTE).getValue() != null) {
							return userExtSource.getAttribute(EPPN_ATTRIBUTE).getValue().split("@")[0];
						}
					}

					return userExtSource.getUes().getLogin().split("@")[0];

				} else if (ExtSourceType.X509.getType().equals(userExtSource.getUes().getExtSource().getType())) {
					return Utils.convertCertCN(userExtSource.getUes().getLogin());
				} else {
					return userExtSource.getUes().getLogin();
				}
			}
		};

		Column<RichUserExtSource, String> removeColumn = new Column<RichUserExtSource, String>(
				new ButtonCell(ButtonType.DANGER, ButtonSize.EXTRA_SMALL)) {
			@Override
			public String getValue(final RichUserExtSource extSource) {
				((ButtonCell) this.getCell()).setEnabled(!extSource.getUes().getPersistent());
				return "✖";
			}
		};
		removeColumn.setFieldUpdater((i, userExtSource, buttonText) -> getUiHandlers().removeUserExtSource(userExtSource));


		PerunLoader plFed = new PerunLoader();
		plFed.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
		federatedIdentitiesTable.setEmptyTableWidget(plFed);
		federatedIdentitiesTable.addColumn(emailCol, translation.uesEmail());
		federatedIdentitiesTable.addColumn(nameCol, translation.federatedIdp());
		federatedIdentitiesTable.addColumn(loginCol, translation.federatedLogin());
		federatedIdentitiesTable.addColumn(removeColumn);
		federatedIdentitiesTable.setColumnWidth(federatedIdentitiesTable.getColumnCount()-1, "5%");
		((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).setEmptyMessage(translation.noIdentities());

		PerunLoader plCert = new PerunLoader();
		plCert.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
		x509IdentitiesTable.setEmptyTableWidget(plCert);
		x509IdentitiesTable.addColumn(emailCol, translation.uesEmail());
		x509IdentitiesTable.addColumn(nameCol, translation.x509Issuer());
		x509IdentitiesTable.addColumn(loginCol, translation.x509Identity());
		x509IdentitiesTable.addColumn(removeColumn);
		x509IdentitiesTable.setColumnWidth(x509IdentitiesTable.getColumnCount()-1, "5%");
		((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).setEmptyMessage(translation.noIdentities());

		PerunLoader plOther = new PerunLoader();
		plOther.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
		otherIdentitiesTable.setEmptyTableWidget(plOther);
		otherIdentitiesTable.addColumn(emailCol, translation.uesEmail());
		otherIdentitiesTable.addColumn(nameCol, translation.uesName());
		otherIdentitiesTable.addColumn(loginCol, translation.login());
		otherIdentitiesTable.addColumn(removeColumn);
		otherIdentitiesTable.setColumnWidth(otherIdentitiesTable.getColumnCount()-1, "5%");
		((PerunLoader) otherIdentitiesTable.getEmptyTableWidget()).setEmptyMessage(translation.noIdentities());

		ClickHandler icLinkHandler = clickEvent -> getUiHandlers().addUserExtSource();

		addFedBtn.addClickHandler(icLinkHandler);
		addCertBtn.addClickHandler(icLinkHandler);
	}

	@Override
	public void addUserExtSource(RichUserExtSource es) {
		if (ExtSourceType.IDP.getType().equals(es.getUes().getExtSource().getType())) {
			federatedIdentities.add(es);
			federatedIdentitiesTable.setRowData(federatedIdentities);
		} else if (ExtSourceType.X509.getType().equals(es.getUes().getExtSource().getType())) {
			x509Identities.add(es);
			x509IdentitiesTable.setRowData(x509Identities);
		} else {
			otherIdentities.add(es);
			otherIdentitiesTable.setRowData(otherIdentities);
		}

		if (federatedIdentities.isEmpty()) {
			((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onEmpty();
		}
		if (x509Identities.isEmpty()) {
			((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onEmpty();
		}
		if (otherIdentities.isEmpty()) {
			((PerunLoader) otherIdentitiesTable.getEmptyTableWidget()).onEmpty();
		}
	}

	@Override
	public void clearUserExtSources() {
		((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onEmpty();
		((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onEmpty();
		((PerunLoader) otherIdentitiesTable.getEmptyTableWidget()).onEmpty();

		federatedIdentitiesTable.setRowData(new ArrayList<>());
		x509IdentitiesTable.setRowData(new ArrayList<>());
		otherIdentitiesTable.setRowData(new ArrayList<>());

		federatedIdentities = new ArrayList<>();
		x509Identities = new ArrayList<>();
		otherIdentities = new ArrayList<>();
	}

	@Override
	public void removingUserExtSourceStart(RichUserExtSource userExtSource) {
		if (ExtSourceType.IDP.getType().equals(userExtSource.getUes().getExtSource().getType())) {
			federatedIdentitiesTable.setRowData(new ArrayList<>());
			((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onLoading(translation.removingIdentity());
		} else if (ExtSourceType.X509.getType().equals(userExtSource.getUes().getExtSource().getType())) {
			x509IdentitiesTable.setRowData(new ArrayList<>());
			((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onLoading(translation.removingIdentity());
		}
	}

	@Override
	public void removingUserExtSourceError(PerunException ex, final RichUserExtSource userExtSource) {
		ClickHandler retry = clickEvent -> getUiHandlers().removeUserExtSource(userExtSource);

		if (ExtSourceType.IDP.getType().equals(userExtSource.getUes().getExtSource().getType())) {
			federatedIdentitiesTable.setRowData(new ArrayList<>());
			((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onError(ex, retry);
		} else if (ExtSourceType.X509.getType().equals(userExtSource.getUes().getExtSource().getType())) {
			x509IdentitiesTable.setRowData(new ArrayList<>());
			((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onError(ex, retry);
		} else {
			otherIdentitiesTable.setRowData(new ArrayList<>());
			((PerunLoader) otherIdentitiesTable.getEmptyTableWidget()).onError(ex, retry);
		}
	}

	@Override
	public void loadingUserExtSourcesStart() {
		((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onLoading(translation.loadingIdentities());
		((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onLoading(translation.loadingIdentities());
		((PerunLoader) otherIdentitiesTable.getEmptyTableWidget()).onLoading(translation.loadingIdentities());
	}

	@Override
	public void loadingUserExtSourcesError(PerunException ex) {
		ClickHandler retry = clickEvent -> getUiHandlers().loadUserExtSources();

		federatedIdentitiesTable.setRowData(new ArrayList<>());
		((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onError(ex, retry);
		x509IdentitiesTable.setRowData(new ArrayList<>());
		((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onError(ex, retry);
		otherIdentitiesTable.setRowData(new ArrayList<>());
		((PerunLoader) otherIdentitiesTable.getEmptyTableWidget()).onError(ex, retry);
	}

	private String getCertParam(String string, String param) {

		for (String s : string.split("/")) {
			if (s.startsWith(param+"=")) {
				return Utils.unescapeDN(s.split("=")[1]);
			}
		}
		return "";
	}
}
