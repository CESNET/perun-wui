package cz.metacentrum.perun.wui.profile.pages.settings.datalimits;


import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickHandler;
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
import cz.metacentrum.perun.wui.client.resources.PerunConfiguration;
import cz.metacentrum.perun.wui.client.resources.PerunSession;
import cz.metacentrum.perun.wui.client.utils.JsUtils;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.json.managers.AttributesManager;
import cz.metacentrum.perun.wui.json.managers.MembersManager;
import cz.metacentrum.perun.wui.json.managers.RTMessagesManager;
import cz.metacentrum.perun.wui.json.managers.ResourcesManager;
import cz.metacentrum.perun.wui.json.managers.UsersManager;
import cz.metacentrum.perun.wui.json.managers.VosManager;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.Attribute;
import cz.metacentrum.perun.wui.model.beans.Member;
import cz.metacentrum.perun.wui.model.beans.Resource;
import cz.metacentrum.perun.wui.model.beans.RichResource;
import cz.metacentrum.perun.wui.model.beans.User;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.model.common.RTMessage;
import cz.metacentrum.perun.wui.profile.client.PerunProfileUtils;
import cz.metacentrum.perun.wui.profile.client.resources.PerunProfilePlaceTokens;
import cz.metacentrum.perun.wui.profile.widgets.QuotaType;

import java.util.ArrayList;
import java.util.List;

public class DataQuotasPresenter extends Presenter<DataQuotasPresenter.MyView, DataQuotasPresenter.MyProxy> implements DataQuotasUiHandlers {

	public interface MyView extends View, HasUiHandlers<DataQuotasUiHandlers> {

		void setVos(List<Vo> vos);

		void addResource(RichResource resource, List<Attribute> attrs);

		void setLoadingData();

		void setNumberOfLoadingResources(int count);

		void setError(PerunException error, ClickHandler retry);

		void setSendingRTMessage();

		void setRTMessageSend(RTMessage message);

		void setRTMessageError(PerunException error, ClickHandler retry);

		void init();
	}

	private static final String DEFAULT_QUEUE = PerunConfiguration.getRTDefaultQueueName();
	private static final String SIGNATURE = "\n\n-------------------------------------\nSent from Perun GUI";
	private static final String RT_SUBJECT = "QUOTA: Change request";

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	@NameToken(PerunProfilePlaceTokens.SETTINGS_DATAQUOTAS)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<DataQuotasPresenter> {

	}

	@Inject
	public DataQuotasPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().init();
		loadVos();
	}

	@Override
	protected void onReveal() {
		GWT.log("REVEAL");
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

		if (PerunSession.getInstance().isSelf(userId)) {
			MembersManager.getMemberByUser(userId, voId, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					Member members = result.cast();
					loadResourcesForMember(members);
				}

				@Override
				public void onError(PerunException error) {
					getView().setError(error, action -> loadDataForVo(voId));
				}

				@Override
				public void onLoadingStart() {
					getView().setLoadingData();
				}
			});
		}
	}

	/**
	 * Load rich resources for given member
	 * @param member member
	 */
	private void loadResourcesForMember(Member member) {
		ResourcesManager.getAssignedRichResources(member.getId(), new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				List<RichResource> resources = JsUtils.jsoAsList(result);
				loadRequiredAttributesForResources(resources, member);
			}

			@Override
			public void onError(PerunException error) {
				getView().setError(error, action -> loadDataForVo(member.getVoId()));
			}

			@Override
			public void onLoadingStart() {
				// do nothing
			}
		});
	}

	/**
	 * Load required attributes for given resources
	 * @param resources list of resources
	 */
	private void loadRequiredAttributesForResources(List<RichResource> resources, Member member) {

		getView().setNumberOfLoadingResources(resources.size());

		for (RichResource resource : resources) {
			AttributesManager.getRequiredAttributes(resource.getId(), member.getId(), resource.getId(), true, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					checkWhichAttributesToLoad(resource, JsUtils.jsoAsList(result));
				}

				@Override
				public void onError(PerunException error) {
					getView().setError(error, clickEvent -> loadDataForVo(resource.getVoId()));
				}

				@Override
				public void onLoadingStart() {
					// do nothing
				}
			});
		}
	}

	/**
	 * This methods checks which attributes should be load for given RichResources
	 * based on their required attributes
	 * @param resource resource
	 * @param attrs required attributes
	 */
	private void checkWhichAttributesToLoad(RichResource resource, List<Attribute> attrs) {
		List<Attribute> resourcesToShow = new ArrayList<>();

		List<String> namesOfAttributesToShow = new ArrayList<>();

		// load attributes for settings
		namesOfAttributesToShow.add(PerunProfileUtils.A_D_R_USER_SETTINGS_NAME_NAME);
		namesOfAttributesToShow.add(PerunProfileUtils.A_D_R_USER_SETTINGS_DESCRIPTION_NAME);

		for (final Attribute attr : attrs) {
			if (PerunProfileUtils.A_D_R_DATA_LIMIT_NAME.equals(attr.getFriendlyName())) {
				resourcesToShow.add(attr);
				namesOfAttributesToShow.add(PerunProfileUtils.A_D_R_DEFAULT_DATA_LIMIT_NAME);
			} else if (PerunProfileUtils.A_D_R_FILES_LIMIT_NAME.equals(attr.getFriendlyName())) {
				resourcesToShow.add(attr);
				namesOfAttributesToShow.add(PerunProfileUtils.A_D_R_DEFAULT_FILES_LIMIT_NAME);
			}
		}

		loadAttributesForResources(resource, resourcesToShow, namesOfAttributesToShow);
	}

	/**
	 * For given resource loads attributes which names are given in list resourceAttributesToLoad
	 * @param resource resource
	 * @param attrs already obtained attributes
	 * @param resourceAttributesToLoad names of attributes that needs to be loaded
	 */
	private void loadAttributesForResources(RichResource resource, List<Attribute> attrs, List<String> resourceAttributesToLoad) {

		List<Attribute> additionalData = new ArrayList<>();

		AttributesManager.getResourceAttributes(resource.getId(), new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				List<Attribute> attributesToShow = new ArrayList<>();
				List<Attribute> attributes = JsUtils.jsoAsList(result);
				for (final Attribute attribute : attributes) {
					if (resourceAttributesToLoad.contains(attribute.getFriendlyName())) {
						attributesToShow.add(attribute);
					}
				}
				additionalData.addAll(attributesToShow);
				additionalData.addAll(attrs);

				getView().addResource(resource, additionalData);
			}

			@Override
			public void onError(PerunException error) {
				getView().setError(error,  retry -> loadDataForVo(resource.getVoId()));
			}

			@Override
			public void onLoadingStart() {
				// do nothing
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
				getView().setError(error, retry -> loadVos(userId));
			}

			@Override
			public void onLoadingStart() {
				// do nothing
			}
		});
	}

	@Override
	public void requestChange(String newValue, String reason, Resource resource, QuotaType quotaType) {
		Integer userId = PerunProfileUtils.getUserId(placeManager);

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (userId == null || userId < 1) {
			placeManager.revealErrorPlace(request.getNameToken());
			return;
		}

		if (PerunSession.getInstance().isSelf(userId)) {
			VosManager.getVoById(resource.getVoId(), new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					Vo vo = result.cast();
					loadUser(userId, vo, newValue, reason, resource, quotaType);
				}

				@Override
				public void onError(PerunException error) {
					getView().setRTMessageError(error, clickEvent -> requestChange(newValue, reason, resource, quotaType));
				}

				@Override
				public void onLoadingStart() {
					getView().setSendingRTMessage();
				}
			});
		}
	}

	private void loadUser(int userId, Vo vo, String newValue, String reason, Resource resource, QuotaType quotaType) {
		UsersManager.getUserById(userId, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				User user = result.cast();
				String requestText = getRequestText(newValue, reason, vo, user, resource, quotaType);
				sendRequestToRt(requestText, vo, newValue, reason, resource, quotaType);
			}

			@Override
			public void onError(PerunException error) {
				getView().setRTMessageError(error, clickEvent -> requestChange(newValue, reason, resource, quotaType));
			}

			@Override
			public void onLoadingStart() {
				// do nothing
			}
		});
	}

	private void sendRequestToRt(String requestText, Vo vo, String newValue, String reason, Resource resource, QuotaType quotaType) {
		RTMessagesManager.sentMessageToRT(vo.getId(), DEFAULT_QUEUE, RT_SUBJECT, requestText, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				getView().setRTMessageSend(result.cast());
			}

			@Override
			public void onError(PerunException error) {
				getView().setRTMessageError(error, clickEvent -> requestChange(newValue, reason, resource, quotaType));
			}

			@Override
			public void onLoadingStart() {
				// do nothing
			}
		});
	}

	@Override
	public void navigateBack() {
		placeManager.navigateBack();
	}

	private String getRequestText(String newQuota, String reason, Vo vo, User user, Resource resource, QuotaType quotaType) {

		String text = "QUOTA CHANGE REQUEST\n\n";
		text += "User: " + user.getFullName() + " (user ID: " + user.getId() +")\n";
		if (vo != null) {
			text += "VO: " + vo.getShortName() + " / "+ vo.getName() + " (vo ID: " + vo.getId() +")\n";
		}
		text += "Resource: " + resource.getName() + " (resource ID: " + resource.getId() +")\n";
		text += getQuotaTypeAsString(quotaType) + " quota\n";
		text += "Requested quota: " + newQuota + "\n";
		text += "Reason: " + reason + "\n";
		text += SIGNATURE;

		text = text.replace("\n", "\n ");
		return text;

	}

	private String getQuotaTypeAsString(QuotaType quotaType) {

		switch(quotaType) {
			case FILES:
				return "Files";
			case DATA:
				return "Data";
		}
		return "Unknown";

	}
}
