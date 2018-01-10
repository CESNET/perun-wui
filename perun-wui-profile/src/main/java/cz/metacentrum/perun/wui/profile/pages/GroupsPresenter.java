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
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.GroupsManager;
import cz.metacentrum.perun.wui.json.managers.MembersManager;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.Member;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.profile.client.PerunProfileUtils;
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

		void setMemberGroups(List<Group> groups);

		void setAdminGroups(List<Group> groups);

		void onLoadFinish();
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
	protected void onReveal() {
		loadVos();
	}

	@Override
	public void loadVos() {

		Integer userId = PerunProfileUtils.getUserId(placeManager);

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
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		loadMemberAndGroups(userId, voId);
	}

	private void loadAdminGroups(int userId, int voId) {
		UsersManager.getGroupsWhereUserIsAdmin(userId, voId, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				getView().setAdminGroups(JsUtils.jsoAsList(result));

				getView().onLoadFinish();
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

	private void loadGroups(int memberId, int userId, int voId) {
		GroupsManager.getMemberGroups(memberId, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				getView().setMemberGroups(JsUtils.jsoAsList(result));

				loadAdminGroups(userId, voId);
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
				loadGroups(member.getId(), userId, voId);
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
