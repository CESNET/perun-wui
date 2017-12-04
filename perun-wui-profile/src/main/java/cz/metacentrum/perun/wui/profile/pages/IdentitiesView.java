package cz.metacentrum.perun.wui.profile.pages;

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
import cz.metacentrum.perun.wui.model.beans.RichUserExtSource;
import cz.metacentrum.perun.wui.model.beans.UserExtSource;
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
	@UiField CellTable<RichUserExtSource> federatedIdentitiesTable;
	@UiField PerunButton addFedBtn;
	@UiField CellTable<RichUserExtSource> x509IdentitiesTable;
	@UiField PerunButton addCertBtn;
	@UiField CellTable<RichUserExtSource> otherIdentitiesTable;
	@UiField Modal otherUesModal;
	@UiField Button showOtherUesButton;


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
				if (rues.getEmail() == null || rues.getEmail().isEmpty()) {
					return "N/A";
				} else {
					return rues.getEmail();
				}
			}
		};
		TextColumn<RichUserExtSource> nameCol = new TextColumn<RichUserExtSource>() {
			@Override
			public String getValue(RichUserExtSource userExtSource) {
				if (ExtSourceType.IDP.getType().equals(userExtSource.getExtSource().getType())) {
					if (userExtSource.getExtSource().getName().equals("https://extidp.cesnet.cz/idp/shibboleth")) {
						// hack our social IdP so we can tell from where identity is
						return Utils.translateIdp("@"+userExtSource.getLogin().split("@")[1]);
					}
					return Utils.translateIdp(userExtSource.getExtSource().getName());
				} else if (ExtSourceType.X509.getType().equals(userExtSource.getExtSource().getType())) {
					return getCertParam(userExtSource.getExtSource().getName(), "O") +
							", " +
							getCertParam(userExtSource.getExtSource().getName(), "CN");
				} else {
					return userExtSource.getExtSource().getName();
				}
			}
		};
		TextColumn<RichUserExtSource> loginCol = new TextColumn<RichUserExtSource>() {
			@Override
			public String getValue(RichUserExtSource userExtSource) {
				if (ExtSourceType.IDP.getType().equals(userExtSource.getExtSource().getType())) {
					return userExtSource.getLogin().split("@")[0];
				} else if (ExtSourceType.X509.getType().equals(userExtSource.getExtSource().getType())) {
					return Utils.convertCertCN(userExtSource.getLogin());
				} else {
					return userExtSource.getLogin();
				}
			}
		};

		Column<RichUserExtSource, String> removeColumn = new Column<RichUserExtSource, String>(
				new ButtonCell(ButtonType.DANGER, ButtonSize.EXTRA_SMALL)) {
			@Override
			public String getValue(final RichUserExtSource extSource) {
				((ButtonCell) this.getCell()).setEnabled(!extSource.getPersistent());
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


		PerunLoader plCert = new PerunLoader();
		plCert.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
		x509IdentitiesTable.setEmptyTableWidget(plCert);
		x509IdentitiesTable.addColumn(emailCol, translation.uesEmail());
		x509IdentitiesTable.addColumn(nameCol, translation.x509Issuer());
		x509IdentitiesTable.addColumn(loginCol, translation.x509Identity());
		x509IdentitiesTable.addColumn(removeColumn);
		x509IdentitiesTable.setColumnWidth(x509IdentitiesTable.getColumnCount()-1, "5%");

		PerunLoader plOther = new PerunLoader();
		plOther.getElement().getStyle().setMarginTop(20, Style.Unit.PX);
		otherIdentitiesTable.setEmptyTableWidget(plOther);
		otherIdentitiesTable.addColumn(emailCol, translation.uesEmail());
		otherIdentitiesTable.addColumn(nameCol, translation.uesName());
		otherIdentitiesTable.addColumn(loginCol, translation.login());
		otherIdentitiesTable.addColumn(removeColumn);
		otherIdentitiesTable.setColumnWidth(otherIdentitiesTable.getColumnCount()-1, "5%");

		ClickHandler icLinkHandler = clickEvent -> getUiHandlers().addUserExtSource();

		addFedBtn.addClickHandler(icLinkHandler);
		addCertBtn.addClickHandler(icLinkHandler);
	}



	@Override
	public void setUserExtSources(List<RichUserExtSource> richUserExtSources) {
		List<RichUserExtSource> federatedIdentities = new ArrayList<>();
		List<RichUserExtSource> x509Identities = new ArrayList<>();
		List<RichUserExtSource> otherIdentities = new ArrayList<>();

		for (RichUserExtSource es : richUserExtSources) {
			if (ExtSourceType.IDP.getType().equals(es.getExtSource().getType())) {
				federatedIdentities.add(es);
			} else if (ExtSourceType.X509.getType().equals(es.getExtSource().getType())) {
				x509Identities.add(es);
			} else {
				otherIdentities.add(es);
			}
		}
		((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onEmpty();
		federatedIdentitiesTable.setRowData(federatedIdentities);
		((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onEmpty();
		x509IdentitiesTable.setRowData(x509Identities);
		((PerunLoader) otherIdentitiesTable.getEmptyTableWidget()).onEmpty();
		otherIdentitiesTable.setRowData(otherIdentities);
	}

	@Override
	public void removingUserExtSourceStart(UserExtSource userExtSource) {
		if (ExtSourceType.IDP.getType().equals(userExtSource.getExtSource().getType())) {
			federatedIdentitiesTable.setRowData(new ArrayList<>());
			((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onLoading(translation.removingIdentity());
		} else if (ExtSourceType.X509.getType().equals(userExtSource.getExtSource().getType())) {
			x509IdentitiesTable.setRowData(new ArrayList<>());
			((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onLoading(translation.removingIdentity());
		}
	}

	@Override
	public void removingUserExtSourceError(PerunException ex, final UserExtSource userExtSource) {
		ClickHandler retry = clickEvent -> getUiHandlers().removeUserExtSource(userExtSource);

		if (ExtSourceType.IDP.getType().equals(userExtSource.getExtSource().getType())) {
			federatedIdentitiesTable.setRowData(new ArrayList<>());
			((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onError(ex, retry);
		} else if (ExtSourceType.X509.getType().equals(userExtSource.getExtSource().getType())) {
			x509IdentitiesTable.setRowData(new ArrayList<>());
			((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onError(ex, retry);
		}
	}

	@Override
	public void loadingUserExtSourcesStart() {
		federatedIdentitiesTable.setRowData(new ArrayList<>());
		((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onLoading(translation.loadingIdentities());
		x509IdentitiesTable.setRowData(new ArrayList<>());
		((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onLoading(translation.loadingIdentities());
		otherIdentitiesTable.setRowData(new ArrayList<>());
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