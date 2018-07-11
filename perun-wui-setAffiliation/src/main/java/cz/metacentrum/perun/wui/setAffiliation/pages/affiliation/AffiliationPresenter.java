package cz.metacentrum.perun.wui.setAffiliation.pages.affiliation;


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
import cz.metacentrum.perun.wui.json.managers.*;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.*;
import cz.metacentrum.perun.wui.model.common.Roles;
import cz.metacentrum.perun.wui.setAffiliation.client.PerunSetAffiliationUtils;
import cz.metacentrum.perun.wui.setAffiliation.client.resources.PerunSetAffiliationPlaceTokens;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for setting affiliations
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public class AffiliationPresenter extends Presenter<AffiliationPresenter.MyView, AffiliationPresenter.MyProxy>
		implements AffiliationUiHandlers {

	private PlaceManager placeManager = PerunSession.getPlaceManager();

	public interface MyView extends View, HasUiHandlers<AffiliationUiHandlers> {

		void onLoadingStart();

		void onError(PerunException e);

        void loadUsersUiCallback(List<RichUser> users);

		void loadUsersAsMembersUiCallback(List<RichMember> users);

		void loadAuthorizedAffiliationsUiCallback(Attribute allowedAffiliations);

		void loadAssignedAffiliationsUiCallback(Attribute assignedAffiliations);

		void storeAssignedAffiliationsUiCallback();

		void loadAdminPerun();

        void unauthorizedUiCallback();

		void loadVoGroupsAdmin(List<Vo> vos, List<Group> groups);
	}

	@NameToken(PerunSetAffiliationPlaceTokens.AFFILIATION)
	@ProxyStandard
	public interface MyProxy extends ProxyPlace<AffiliationPresenter> {

	}

	@Inject
	public AffiliationPresenter(final EventBus eventBus, final MyView myView, final MyProxy myProxy) {
		super(eventBus, myView, myProxy, PerunPresenter.SLOT_MAIN_CONTENT);
		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		PerunSession session = PerunSession.getInstance();
		int uid = session.getUserId();
		Roles roles = session.getPerunPrincipal().getRoles();
		if (roles == null) {
			return;
		}

		if (roles.hasRole(PerunSession.PERUN_ADMIN_PRINCIPAL_ROLE)) {
			perunAdminSetup();
		} else if (roles.hasRole(PerunSession.VO_ADMIN_PRINCIPAL_ROLE)
				&& roles.hasRole(PerunSession.GROUP_ADMIN_PRINCIPAL_ROLE)) {
			voAndGroupAdminSetup(uid);
		} else if (roles.hasRole(PerunSession.VO_ADMIN_PRINCIPAL_ROLE)){
			voAdminSetup(uid);
		} else if (roles.hasRole(PerunSession.GROUP_ADMIN_PRINCIPAL_ROLE)) {
		    groupAdminSetup(uid);
		} else {
		    getView().unauthorizedUiCallback();
		    return;
        }

		AttributesManager.getUserAttribute(uid, PerunSetAffiliationUtils.AUTHORIZED_AFFILIATION_SCOPES_ATTR,
				new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject result) {
						Attribute allowedAffiliations = (Attribute) result;
						getView().loadAuthorizedAffiliationsUiCallback(allowedAffiliations);
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

	@Override
    public void loadUsers(String searchString) {
        ArrayList<String> attrs = new ArrayList<>();
		attrs.add("urn:perun:user:attribute-def:def:preferredMail");

		if (searchString == null || searchString.isEmpty()) {
			UsersManager.getRichUsersWithAttributes(attrs, false, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					getView().loadUsersUiCallback(JsUtils.jsoAsList(result));
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
		} else {
			UsersManager.findRichUsersWithAttributes(searchString, attrs, new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					getView().loadUsersUiCallback(JsUtils.jsoAsList(result));
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
    }

    @Override
    public void loadUsersFromGroup(String searchString, int groupId) {
        final PlaceRequest request = placeManager.getCurrentPlaceRequest();

        if (searchString == null) {
            placeManager.revealErrorPlace(request.getNameToken());
        }

        List<String> attrs = new ArrayList<>();
        attrs.add("urn:perun:user:attribute-def:def:preferredMail");
        MembersManager.getCompleteRichMembers(groupId, attrs, false, new JsonEvents(){
            @Override
            public void onFinished(JavaScriptObject result) {
                processMembersResult(searchString, result);
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

    @Override
    public void loadUsersFromVo(String searchString, int voId) {
        final PlaceRequest request = placeManager.getCurrentPlaceRequest();

        if (searchString == null) {
            placeManager.revealErrorPlace(request.getNameToken());
        }

        List<String> attrs = new ArrayList<>();
        attrs.add("urn:perun:user:attribute-def:def:preferredMail");
        MembersManager.getCompleteRichMembers(voId, attrs, new JsonEvents(){
            @Override
            public void onFinished(JavaScriptObject result) {
                processMembersResult(searchString, result);
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

    @Override
	public void loadAssignedAffiliations(Long uid) {
		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (uid <= 0L ) {
			placeManager.revealErrorPlace(request.getNameToken());
		}
		
		AttributesManager.getUserAttribute(uid.intValue(),
				PerunSetAffiliationUtils.EDU_PERSON_SCOPED_AFFILIATIONS_MANUALLY_ASSIGNED_ATTR , new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				Attribute assignedAffiliations = (Attribute) result;
				getView().loadAssignedAffiliationsUiCallback(assignedAffiliations);
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

	@Override
	public void storeAssignedAffiliations(Long uid, String organization, boolean member, boolean faculty,
										  boolean affiliate) {

		final PlaceRequest request = placeManager.getCurrentPlaceRequest();

		if (uid <= 0L ) {
			placeManager.revealErrorPlace(request.getNameToken());
		}

		AttributesManager.getUserAttribute(uid.intValue(),
			PerunSetAffiliationUtils.EDU_PERSON_SCOPED_AFFILIATIONS_MANUALLY_ASSIGNED_ATTR,	new JsonEvents() {
				@Override
				public void onFinished(JavaScriptObject result) {
					storeAttribute(uid, result, member, faculty, affiliate, organization);
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

    private void perunAdminSetup() {
        getView().loadAdminPerun();
    }

    private void voAdminSetup(int uid) {
        UsersManager.getVosWhereUserIsAdmin(uid, new JsonEvents() {
            @Override
            public void onFinished(JavaScriptObject result) {
                getView().loadVoGroupsAdmin(JsUtils.jsoAsList(result), null);
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


	private void voAndGroupAdminSetup(int uid) {
		UsersManager.getVosWhereUserIsAdmin(uid, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject vos) {
				UsersManager.getGroupsWhereUserIsAdmin(uid, new JsonEvents() {
					@Override
					public void onFinished(JavaScriptObject groups) {
						getView().loadVoGroupsAdmin(JsUtils.jsoAsList(vos), JsUtils.jsoAsList(groups));
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

    private void groupAdminSetup(int uid) {
        UsersManager.getGroupsWhereUserIsAdmin(uid, new JsonEvents() {
            @Override
            public void onFinished(JavaScriptObject result) {
                getView().loadVoGroupsAdmin(null, JsUtils.jsoAsList(result));
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

	private void storeAttribute(Long uid, JavaScriptObject result, boolean member, boolean faculty, boolean affiliate,
								String organization) {
		Attribute old = (Attribute) result;
		Attribute attr = PerunSetAffiliationUtils.createAssignedAffiliationsAttribute(
				old, member, faculty, affiliate, organization);
		AttributesManager.setUserAttribute(uid.intValue(), attr, new JsonEvents() {
			@Override
			public void onFinished(JavaScriptObject result) {
				getView().storeAssignedAffiliationsUiCallback();
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

    private void processMembersResult(String searchString, JavaScriptObject result) {
        List<RichMember> members = JsUtils.jsoAsList(result);
        if (searchString == null ) {
            getView().loadUsersAsMembersUiCallback(JsUtils.jsoAsList(result));
            return;
        }

        List<RichMember> res = new ArrayList<>();
        for (RichMember m : members) {
            if (m.getUser().getFullName().contains(searchString) ||
                    m.getUserAttributes().get(0).getValue().contains(searchString)) {
                res.add(m);
            }
        }

        getView().loadUsersAsMembersUiCallback(res);
    }
}
