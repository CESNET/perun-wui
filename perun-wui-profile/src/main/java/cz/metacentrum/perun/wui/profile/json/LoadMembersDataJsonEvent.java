package cz.metacentrum.perun.wui.profile.json;

import com.google.gwt.core.client.JavaScriptObject;
import cz.metacentrum.perun.wui.json.JsonEvents;
import cz.metacentrum.perun.wui.model.PerunException;
import cz.metacentrum.perun.wui.model.beans.RichMember;
import cz.metacentrum.perun.wui.model.beans.Vo;
import cz.metacentrum.perun.wui.profile.pages.CompleteInfoPresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * Classed used for loading RichMembers and setting them to view
 * when all of them are loaded. Before starting the process the method
 * reset() should be called and the number of members must be set.
 *
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public final class LoadMembersDataJsonEvent implements JsonEvents {

	private static Map<RichMember, Vo> memberVoMap = new HashMap<>();

	private static Integer counter;

	private static Integer numberOfMembers;

	private CompleteInfoPresenter.MyView completeInfoView;

	private Vo vo;

	public LoadMembersDataJsonEvent(CompleteInfoPresenter.MyView completeInfoView, Vo vo) {
		this.completeInfoView = completeInfoView;
		this.vo = vo;
	}

	@Override
	public void onFinished(JavaScriptObject result) {
		if (numberOfMembers == null) {
			throw new IllegalStateException("NumberOfMembers needs to be set.");
		}

		RichMember richMember = (RichMember) result;

		memberVoMap.put(richMember, vo);
		counter ++;

		if (counter.equals(numberOfMembers)) {
			completeInfoView.setMembers(memberVoMap);
			counter = 0;
			numberOfMembers = null;
			memberVoMap = new HashMap<>();
		}
	}

	@Override
	public void onError(PerunException error) {
		completeInfoView.onLoadingError(error);
	}

	@Override
	public void onLoadingStart() {

	}

	public static void setNumberOfMembers(int numberOfMembers) {
		LoadMembersDataJsonEvent.numberOfMembers = numberOfMembers;
	}

	public static void reset() {
		counter = 0;
		numberOfMembers = 0;
		memberVoMap = new HashMap<>();
	}
}
