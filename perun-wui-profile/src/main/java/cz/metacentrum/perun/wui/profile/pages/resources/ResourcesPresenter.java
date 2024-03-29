package cz.metacentrum.perun.wui.profile.pages.resources;

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
import cz.metacentrum.perun.wui.json.AbstractRepeatingJsonEvent;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.MembersManager;
import cz.metacentrum.perun.wui.json.managers.ResourcesManager;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Group;
import cz.metacentrum.perun.wui.model.beans.Member;
import cz.metacentrum.perun.wui.model.beans.RichResource;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.profile.client.PerunProfileUtils;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class ResourcesPresenter extends Presenter<ResourcesPresenter.MyView, ResourcesPresenter.MyProxy> implements ResourcesUiHandlers {

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	public interface MyView extends View, HasUiHandlers<ResourcesUiHandlers> {

		void setVos(List<Vo> vos);

		void setVosError(PerunException error);

		void loadVosStart();

		void setResourcesDataError(PerunException error);

		void loadResourcesDataStart();

		void setResources(Map<RichResource, List<Group>> resources);
	}

	@NameToken(PerunProfilePlaceTokens.RESOURCES)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<ResourcesPresenter> {

	}

	@Inject
	public ResourcesPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		loadVos();
	}

	@Override
	public void loadVos() {

		final Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			loadVos(userId);
		}
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

	@Override
	public void loadDataForVo(int voId) {
		final Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		loadMemberAndResources(userId, voId);
	}

	private void loadMemberAndResources(Integer userId, int voId) {
		MembersManager.getMemberByUser(userId, voId, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				Member member = (Member) result;
				loadResources(member);
			}

			@Override
			public void onError(PerunException error) {
				getView().setResourcesDataError(error);
			}

			@Override
			public void onLoadingStart() {
				getView().loadResourcesDataStart();
			}
		});
	}

	private void loadResources(Member member) {
		ResourcesManager.getAssignedRichResources(member.getId(), new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				List<RichResource> resources = JsUtils.jsoAsList(result);
				if (resources.isEmpty()) {
					getView().setResources(new HashMap<>());
				} else {
					loadGroupsForResources(member, resources);
				}
			}

			@Override
			public void onError(PerunException error) {
				getView().setResourcesDataError(error);
			}

			@Override
			public void onLoadingStart() {
				getView().loadResourcesDataStart();
			}
		});
	}

	private void loadGroupsForResources(Member member, List<RichResource> richResources) {
		final Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null) {
			placeManager.revealErrorPlace(request.getNameToken());
		} else {
			loadGroupsFromMember(richResources, member);
		}
	}

	private void loadGroupsFromMember(List<RichResource> resources, Member member) {

		AbstractRepeatingJsonEvent event = new AbstractRepeatingJsonEvent(resources.size()) {

			@Override
			public void done(List<JavaScriptObject> results) {

				// FIXME - this doesnt makes sense as AbstractRepeatingJsonEvent merges all results together
				// and we don't have list of "list", but just the list of all groups assigned to all resources
				// without any mapping (by id or other) between group and resource.
				List<List<Group>> resourcesGroups = new ArrayList<>();

				for (JavaScriptObject result : results) {
					List<Group> groups = JsUtils.jsoAsList(result);
					resourcesGroups.add(groups);

				}
				Map<RichResource, List<Group>> resourceWithGroups = new HashMap<>();
				for (int i = 0; i < resources.size(); i++) {
					resourceWithGroups.put(resources.get(i), resourcesGroups.get(i));
				}

				getView().setResources(resourceWithGroups);
			}

			@Override
			public void erred(PerunException exception) {
				getView().setResourcesDataError(exception);
			}

			@Override
			public void started() {
				// do nothing
			}
		};

		/* FIXME - temporary disabled
		for (RichResource resource : resources) {
			ResourcesManager.getAssignedGroups(resource.getId(), member.getId(), event);
		}
		*/

		// pass with empty groups
		Map<RichResource, List<Group>> resourceWithGroups = new HashMap<>();
		for (RichResource resource : resources) {
			resourceWithGroups.put(resource,new ArrayList<Group>());
		}
		getView().setResources(resourceWithGroups);

	}
}
