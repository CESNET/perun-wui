package cz.metacentrum.perun.wui.profile.pages;


import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;
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
import cz.metacentrum.perun.wui.json.AbstractRepeatingJsonEvent;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.MembersManager;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Member;
import cz.metacentrum.perun.wui.model.beans.RichMember;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.profile.client.PerunProfileUtils;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class OrganizationsPresenter extends Presenter<OrganizationsPresenter.MyView, OrganizationsPresenter.MyProxy> implements OrganizationsUiHandlers {

	public interface MyView extends View, HasUiHandlers<OrganizationsUiHandlers> {
		void onError(PerunException e);

		void onLoadingStart();

		void setData(Map<RichMember, Vo> data);
	}

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	@NameToken(PerunProfilePlaceTokens.ORGANIZATIONS)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<OrganizationsPresenter> {

	}

	@Inject
	public OrganizationsPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		loadData();
	}

	@Override
	public void extendMembership(Vo vo) {
		Window.Location.assign(Utils.getMembershipExtendLink(vo));
	}

	@Override
	public void loadData() {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
		} else {
			loadData(userId);
		}
	}

	private void loadData(Integer userId) {
		UsersManager.getVosWhereUserIsMember(userId, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				List<Vo> vos = JsUtils.jsoAsList(result);

				loadMembersToVos(userId, vos);
			}

			@Override
			public void onError(PerunException error) {
				getView().onError(error);
			}

			@Override
			public void onLoadingStart() {
				getView().onLoadingStart();
			}
		});
	}

	private void loadMembersToVos(int userId, List<Vo> vos) {
		AbstractRepeatingJsonEvent getMemberByUserRepeating = new AbstractRepeatingJsonEvent(vos.size()){
			@Override
			public void done(List<JavaScriptObject> results) {
				List<Member> members = JsUtils.jsListAsList(results);
				getRichMembersFromMembers(members, vos);
			}

			@Override
			public void erred(PerunException exception) {
				getView().onError(exception);
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
				getView().onError(exception);
			}

			@Override
			public void done(List<JavaScriptObject> results) {
				List<RichMember> richMembers = JsUtils.jsListAsList(results);
				Map<RichMember, Vo> memberVoMap = new HashMap<>();
				for (RichMember richMember : richMembers) {
					for (Vo vo : vos) {
						if (richMember.getVoId() == vo.getId()) {
							memberVoMap.put(richMember, vo);
							break;
						}
					}
				}

				getView().setData(memberVoMap);
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
}
