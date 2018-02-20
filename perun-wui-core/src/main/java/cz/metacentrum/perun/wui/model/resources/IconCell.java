package cz.metacentrum.perun.wui.model.resources;


import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class IconCell extends com.google.gwt.cell.client.ButtonCell {

	private IconType iconType;
	private IconSize iconSize;

	public IconCell(IconType iconType) {
		this.iconType = iconType;
		this.iconSize = IconSize.TIMES2;
	}

	public IconCell(IconType iconType, IconSize iconSize) {
		this.iconType = iconType;
		this.iconSize = iconSize;
	}

	@Override
	public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
		String html = "<i class=\"" + "fa" + " " + this.iconType.getCssName() + " " + this.iconSize.getCssName() + "\"></i> ";
		sb.appendHtmlConstant(html);

		if(data != null) {
			sb.append(data);
		}

		sb.appendHtmlConstant("</button>");
	}

	public void setIconType(IconType iconType) {
		this.iconType = iconType;
	}

	public void setIconSize(IconSize iconSize) {
		this.iconSize = iconSize;
	}
}
