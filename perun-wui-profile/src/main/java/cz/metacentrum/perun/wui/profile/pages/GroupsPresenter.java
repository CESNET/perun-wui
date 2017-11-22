package cz.metacentrum.perun.wui.profile.pages;


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
import cz.metacentrum.perun.wui.client.PerunPresenter;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.client.utils.Utils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.GroupsManager;
import cz.metacentrum.perun.wui.json.managers.MembersManager;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.Member;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

import java.util.List;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class GroupsPresenter extends Presenter<GroupsPresenter.MyView, GroupsPresenter.MyProxy> implements GroupsUiHandlers {

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	public interface MyView extends View, HasUiHandlers<GroupsUiHandlers> {

		void setVosError(PerunException error);

		void setVoDataError(PerunException error);

		void setVos(List<Vo> vos);

		void loadVosStart();

		void loadVoDataStart();

		void setGroups(List<Group> groups);
	}

	@NameToken(PerunProfilePlaceTokens.GROUPS)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<GroupsPresenter> {

	}

	@Inject
	public GroupsPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onBind() {
		loadVos();
	}

	@Override
	public void loadVos() {

		final Integer userId = Utils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			loadVos(userId);
		}
	}

	@Override
	public void loadDataForVo(int voId) {
		final Integer userId = Utils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		loadMemberAndGroups(userId, voId);
	}

	private void loadGroups(int memberId) {
		GroupsManager.getMemberGroups(memberId, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				getView().setGroups(JsUtils.jsoAsList(result));
			}

			@Override
			public void onError(PerunException error) {
				getView().setVoDataError(error);
			}

			@Override
			public void onLoadingStart() {
				// do nothing
			}
		});
	}

	private void loadMemberAndGroups(int userId, int voId) {
		MembersManager.getMemberByUser(userId, voId, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				Member member = (Member) result;
				loadGroups(member.getId());
			}

			@Override
			public void onError(PerunException error) {
				getView().setVoDataError(error);
			}

			@Override
			public void onLoadingStart() {
				getView().loadVoDataStart();
			}
		});

	}

	private void loadVos(int userId) {
		UsersManager.getVosWhereUserIsMember(userId, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				getView().setVos(JsUtils.jsoAsList(result));
			}

			@Override
			public void onError(PerunException error) {
				getView().setVosError(error);
			}

			@Override
			public void onLoadingStart() {
				getView().loadVosStart();
			}
		});
	}
}
