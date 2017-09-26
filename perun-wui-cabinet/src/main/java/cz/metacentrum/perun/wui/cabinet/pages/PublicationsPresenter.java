package cz.metacentrum.perun.wui.cabinet.pages;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import cz.metacentrum.perun.wui.cabinet.client.resources.PerunCabinetPlaceTokens;
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.CabinetManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.RichPublication;
import cz.metacentrum.perun.wui.model.beans.User;

import java.util.List;


public class PublicationsPresenter extends Presenter<PublicationsPresenter.MyView, PublicationsPresenter.MyProxy> implements PublicationsUiHandlers {

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	public interface MyView extends View, HasUiHandlers<PublicationsUiHandlers> {
		void setUser(User user);

		void setPublications(List<RichPublication> publications);
		void setPublicationsError(PerunException error);
		void loadingPublicationsStart();
	}

	@NameToken(PerunCabinetPlaceTokens.PUBLICATIONS)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<PublicationsPresenter> {
	}

	@Inject
	public PublicationsPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy, PerunPresenter.SLOT_MAIN_CONTENT);

		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		loadPublications();
	}

	private void findPubsByGuiFilter(int id, String title, String isbn, int year, int category, String doi,
									 int yearSince, int yearTill, int userId) {

		getView().setUser(PerunSession.getInstance().getUser());

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			CabinetManager.findPublicationsByGUIFilter(id, title, isbn, year, category, doi, yearSince, yearTill, userId,
					new JsonEvents() {

						@Override
						public void onFinished(JavaScriptObject result) {
							getView().setPublications(JsUtils.jsoAsList(result));
						}

						@Override
						public void onError(PerunException error) {
							getView().setPublicationsError(error);
						}

						@Override
						public void onLoadingStart() {
							getView().loadingPublicationsStart();
						}
					}
			);
		} else {
			placeManager.revealUnauthorizedPlace(request.getNameToken());
		}
	}

	@Override
	public void searchPublications(String value) {
		final int userId = getUserId();
		findPubsByGuiFilter(0, value, null, 0, 0, null, 0, 0, userId);
	}

	@Override
	public void loadPublications() {
		final int userId = getUserId();
		findPubsByGuiFilter(0, null, null, 0, 0, null, 0, 0, userId);
	}

	private Integer getUserId() {
		try {

			String userId = placeManager.getCurrentPlaceRequest().getParameter("id", null);
			if (userId == null) {
				userId = String.valueOf(PerunSession.getInstance().getUserId());
			}

			final int id = Integer.valueOf(userId);

			if (id < 1) {
				placeManager.revealErrorPlace(placeManager.getCurrentPlaceRequest().getNameToken());
			}

			return id;
		} catch (NumberFormatException e) {
			placeManager.revealErrorPlace(placeManager.getCurrentPlaceRequest().getNameToken());
		}
		return null;
	}

}