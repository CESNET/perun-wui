package cz.metacentrum.perun.wui.model.beans;


import java.util.List;

/**
 * @author Vojtech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class RichResourceWithGroups {

	private RichResource richResource;

	private List<Group> groups;

	public RichResourceWithGroups(RichResource richResource, List<Group> groups) {
		this.richResource = richResource;
		this.groups = groups;
	}

	public RichResource getRichResource() {
		return richResource;
	}

	public void setRichResource(RichResource richResource) {
		this.richResource = richResource;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
}
