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
import cz.metacentrum.perun.wui.json.managers.MembersManager;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Member;
import cz.metacentrum.perun.wui.model.beans.RichMember;
import cz.metacentrum.perun.wui.model.beans.RichUser;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.json.AbstractRepeatingJsonEvent;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class CompleteInfoPresenter extends Presenter<CompleteInfoPresenter.MyView, CompleteInfoPresenter.MyProxy> implements CompleteInfoUiHandlers {

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	public interface MyView extends View, HasUiHandlers<CompleteInfoUiHandlers> {

		void setUser(RichUser user);

		void onLoadingError(PerunException e);

		void onLoadingStart();

		void setMembersWithVo(Map<RichMember, Vo> memberVoMap);
	}

	@NameToken(PerunProfilePlaceTokens.COMPLETE_INFO)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<CompleteInfoPresenter> {

	}

	@Inject
	public CompleteInfoPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		loadUserData();
	}

	@Override
	public void loadUserData() {
		Integer userId = Utils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			getUserData(userId);

			getMembersData(userId);
		}
	}

	private void getUserData(int userId) {
		UsersManager.getRichUserWithAttributes(userId, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				getView().setUser((RichUser) result);
			}

			@Override
			public void onError(PerunException error) {
				getView().onLoadingError(error);
			}

			@Override
			public void onLoadingStart() {
				getView().onLoadingStart();
			}
		});
	}

	private void getVos(int userId) {
		UsersManager.getVosWhereUserIsMember(userId, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				List<Vo> vos = JsUtils.jsoAsList(result);
				getRichMembers(userId, vos);
			}

			@Override
			public void onError(PerunException error) {
				getView().onLoadingError(error);
			}

			@Override
			public void onLoadingStart() {
				// do nothing
			}
		});
	}

	private void getMembersData(int userId) {
		getVos(userId);
	}

	private void getRichMembers(int userId, List<Vo> vos) {
		AbstractRepeatingJsonEvent getMemberByUserRepeating = new AbstractRepeatingJsonEvent(vos.size()){
			@Override
			public void finished(List<JavaScriptObject> results) {
				List<Member> members = JsUtils.jsoListAsList(results);
				getRichMembersFromMembers(members, vos);
			}

			@Override
			public void erred(PerunException exception) {
				getView().onLoadingError(exception);
			}

			@Override
			public void started() {
				// do nothing
			}
		};

		for (Vo vo: vos) {
			MembersManager.getMemberByUser(userId, vo.getId(), getMemberByUserRepeating);
		}
	}

	private void getRichMembersFromMembers(List<Member> members, List<Vo> vos) {
		AbstractRepeatingJsonEvent richMembersRepeatingEvent = new AbstractRepeatingJsonEvent(members.size()) {
			@Override
			public void erred(PerunException exception) {
				getView().onLoadingError(exception);
			}

			@Override
			public void finished(List<JavaScriptObject> results) {
				List<RichMember> richMembers = JsUtils.jsoListAsList(results);
				Map<RichMember, Vo> memberVoMap = new HashMap<>();
				for (RichMember richMember : richMembers) {
					for (Vo vo : vos) {
						if (richMember.getVoId() == vo.getId()) {
							memberVoMap.put(richMember, vo);
							break;
						}
					}
				}

				getView().setMembersWithVo(memberVoMap);
			}

			@Override
			public void started() {
				// do nothing
			}
		};

		for (Member member : members) {
			MembersManager.getRichMemberWithAttributes(member.getId(), richMembersRepeatingEvent);
		}
	}

	@Override
	public void navigateBack() {
		placeManager.navigateBack();
	}
}
