package cz.metacentrum.perun.wui.profile.pages;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
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
import cz.metacentrum.perun.wui.model.beans.UserExtSource;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfileTranslation;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
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
		TextColumn<UserExtSource> loginCol = new TextColumn<UserExtSource>() {
			@Override
			public String getValue(UserExtSource userExtSource) {
				if (ExtSourceType.IDP.getType().equals(userExtSource.getExtSource().getType())) {
					return userExtSource.getLogin().split("@")[0];
				} else if (ExtSourceType.X509.getType().equals(userExtSource.getExtSource().getType())) {
					return Utils.convertCertCN(userExtSource.getLogin());
				} else {
					return userExtSource.getLogin();
				}
			}
		};
		Column<UserExtSource, String> removeColumn = new Column<UserExtSource, String>(
				new ButtonCell(ButtonType.DANGER, ButtonSize.EXTRA_SMALL)) {
			@Override
			public String getValue(final UserExtSource extSource) {
				((ButtonCell) this.getCell()).setEnabled(!extSource.getPersistent());
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

	private String getCertParam(String string, String param) {

		for (String s : string.split("/")) {
			if (s.startsWith(param+"=")) {
				return Utils.unescapeDN(s.split("=")[1]);
			}
		}
		return "";
	}

}