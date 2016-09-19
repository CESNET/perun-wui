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
import com.google.gwt.user.client.Window;
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
import cz.metacentrum.perun.wui.widgets.resources.PerunButtonType;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.client.ui.html.Small;
import org.gwtbootstrap3.client.ui.html.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
				return translateUesIdentification(userExtSource);
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
			public void update(int i, final UserExtSource userExtSource, String buttonText) {

				final Modal modal = new Modal();
				modal.setTitle("Remove linked account?");
				modal.setRemoveOnHide(true);

				ModalBody body = new ModalBody();
				modal.add(body);

				body.add(new Paragraph("Do you wish to remove linked account: "+translateUesIdentification(userExtSource) + "?"));

				ModalFooter footer = new ModalFooter();
				modal.add(footer);

				footer.add(PerunButton.getButton(PerunButtonType.REMOVE, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						getUiHandlers().removeUserExtSource(userExtSource);
						modal.hide();
					}
				}));

				footer.add(PerunButton.getButton(PerunButtonType.CANCEL, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						modal.hide();
					}
				}));

				modal.show();

			}
		});

		Column<UserExtSource, String> infoColumn = new Column<UserExtSource, String>(
				new ButtonCell(ButtonType.INFO, ButtonSize.EXTRA_SMALL)) {
			@Override
			public String getValue(final UserExtSource extSource) {
				return "info";
			}
		};
		infoColumn.setFieldUpdater(new FieldUpdater<UserExtSource, String>() {
			@Override
			public void update(int i, UserExtSource userExtSource, String buttonText) {
				if (userExtSource != null && userExtSource.getLogin() != null) {
					Modal modal = new Modal();
					modal.setTitle("Account internal identification");
					modal.setRemoveOnHide(true);
					ModalBody body = new ModalBody();
					body.add(new Paragraph(userExtSource.getLogin()));
					modal.add(body);
					modal.show();
				};
			}
		});


		PerunLoader plFed = new PerunLoader();
		plFed.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
		federatedIdentitiesTable.setEmptyTableWidget(plFed);
		federatedIdentitiesTable.addColumn(nameCol, "");
		//federatedIdentitiesTable.addColumn(loginCol, translation.federatedLogin());
		federatedIdentitiesTable.addColumn(infoColumn);
		federatedIdentitiesTable.addColumn(removeColumn);
		federatedIdentitiesTable.setColumnWidth(federatedIdentitiesTable.getColumnCount()-1, "5%");
		federatedIdentitiesTable.setColumnWidth(federatedIdentitiesTable.getColumnCount()-2, "5%");


		PerunLoader plCert = new PerunLoader();
		plCert.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
		x509IdentitiesTable.setEmptyTableWidget(plCert);
		x509IdentitiesTable.addColumn(nameCol, translation.x509Issuer());
		x509IdentitiesTable.addColumn(loginCol, translation.x509Identity());
		x509IdentitiesTable.addColumn(removeColumn);
		x509IdentitiesTable.setColumnWidth(x509IdentitiesTable.getColumnCount()-1, "5%");


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
				if (!es.getPersistent()) federatedIdentities.add(es);
			} else if (ExtSourceType.X509.getType().equals(es.getExtSource().getType())) {
				x509Identities.add(es);
			}
		}
		((PerunLoader) federatedIdentitiesTable.getEmptyTableWidget()).onEmpty();
		federatedIdentitiesTable.setRowData(federatedIdentities);
		((PerunLoader) x509IdentitiesTable.getEmptyTableWidget()).onEmpty();
		x509IdentitiesTable.setRowData(x509Identities);

		if (!x509Identities.isEmpty()) {
			x509IdentitiesTable.setVisible(true);
			addCertBtn.setVisible(true);
		}

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

	private String translateUesIdentification(UserExtSource userExtSource) {

		if (ExtSourceType.IDP.getType().equals(userExtSource.getExtSource().getType())) {
			String name = userExtSource.getExtSource().getName();
			String translated = Utils.translateIdp(userExtSource.getExtSource().getName());
			GWT.log(name + "=" + translated);
			if (Objects.equals(name, translated)) {
				translated = Utils.translateIdp("@"+userExtSource.getLogin().split("@")[1]);
			}
			return translated;
		} else if (ExtSourceType.X509.getType().equals(userExtSource.getExtSource().getType())) {
			return getCertParam(userExtSource.getExtSource().getName(), "O") +
					", " +
					getCertParam(userExtSource.getExtSource().getName(), "CN");
		} else {
			return userExtSource.getExtSource().getName();
		}

	}

}
