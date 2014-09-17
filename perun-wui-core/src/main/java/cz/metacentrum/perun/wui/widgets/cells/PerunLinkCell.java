package cz.metacentrum.perun.wui.widgets.cells;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import cz.metacentrum.perun.wui.model.GeneralObject;

import java.util.Set;

/**
 * Custom cell which display it's content as clickable link based on authorization.
 *
 * @param <T> is expected to ALWAYS BE PerunLinkCellHandler<T extends JavaScriptObject>
 *            which is anonymous class providing all necessary data to render the cell content.
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class PerunLinkCell<T> extends AbstractSafeHtmlCell<T> {

	/**
	 * Anonymous class passed as cell value for all kind of objects.
	 * Provide cell's value, URL, and authorization. Object passed
	 * to these methods is cell "key" aka object behind the "row".
	 *
	 * @param <T>
	 */
	public interface PerunLinkCellHandler<T extends JavaScriptObject> {

		/**
		 * Decide, if user is authorized to click on cell. If return true,
		 * cell content is drawn as hyperlink, if false, simple text is used.
		 *
		 * @return TRUE if user is authorized
		 */
		public boolean isAuthorized();

		/**
		 * Return URL of destination page within GUI,
		 * aka HistoryToken = part after # in URL
		 * <p/>
		 * If null is returned, then cell is drawn as simple text
		 * instead of hyperlink.
		 *
		 * @return HistoryToken (URL part after #)
		 */
		public String getUrl();

		/**
		 * Perform programmatic action triggered by click on link.
		 * Click on cell outside the link are won't start this action.
		 */
		public void onClick();

		/**
		 * Get textual value presented in cell. If null is returned, then cell is drawn
		 * as simple text cell without hyperlink.
		 *
		 * @return text to display in cell
		 */
		public String getCellValue();

		/**
		 * Get object, which is used to render this row and should be used in all
		 * other methods to get data and perform decisions.
		 *
		 * @return object/key behind current row
		 */
		public T getObject();

	}

	/**
	 * Create new cell instance with default renderer, listening to click and keydown actions.
	 * <p/>
	 * Value of each cell is expected to be anonymous implementation of PerunLinkCellHandler<T extends JavaScriptObject>
	 * which provides all necessary data to draw the cell based on real object behind each row.
	 */
	public PerunLinkCell() {

		this(new SafeHtmlRenderer<T>() {

			public SafeHtml render(T object) {
				if (object != null) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					render(object, sb);
					return sb.toSafeHtml();
				}

				return SafeHtmlUtils.EMPTY_SAFE_HTML;
			}

			public void render(T object, SafeHtmlBuilder sb) {
				if (object != null) {

					PerunLinkCellHandler<GeneralObject> handler = (PerunLinkCellHandler<GeneralObject>) object;

					if (handler.isAuthorized() && handler.getUrl() != null && handler.getCellValue() != null) {
						sb.appendHtmlConstant("<a class=\"linkCell\" href=\"#" + handler.getUrl() + "\">");
						sb.appendEscaped(handler.getCellValue());
						sb.appendHtmlConstant(" <i class=\"fa fa-sign-out\"></i>");
						sb.appendHtmlConstant("</a>");
					} else {
						sb.appendHtmlConstant("<span>");
						sb.appendEscaped(handler.getCellValue());
						sb.appendHtmlConstant("</span>");
					}

				}
			}

		}, "click", "keydown");

	}

	public PerunLinkCell(SafeHtmlRenderer<T> renderer, Set<String> consumedEvents) {
		super(renderer, consumedEvents);
	}

	public PerunLinkCell(SafeHtmlRenderer<T> renderer, String... consumedEvents) {
		super(renderer, consumedEvents);
	}

	@Override
	public void render(Context context, T data, SafeHtmlBuilder sb) {

		PerunLinkCellHandler<GeneralObject> handler = (PerunLinkCellHandler<GeneralObject>) data;

		if (handler.isAuthorized()) {
			sb.appendHtmlConstant("<a class=\"linkCell\" href=\"#" + handler.getUrl() + "\">");
			sb.appendHtmlConstant(handler.getCellValue().replaceAll("\n", "<br>"));
			sb.appendHtmlConstant(" <i class=\"fa fa-sign-out\"></i>");
			sb.appendHtmlConstant("</a>");
		} else {
			sb.appendHtmlConstant("<span>");
			sb.appendHtmlConstant(handler.getCellValue().replaceAll("\n", "<br>"));
			sb.appendHtmlConstant("</span>");
		}

	}

	@Override
	protected void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(value);
		}
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, T value, NativeEvent event, ValueUpdater<T> valueUpdater) {

		PerunLinkCellHandler<GeneralObject> handler = (PerunLinkCellHandler<GeneralObject>) value;

		super.onBrowserEvent(context, parent, value, event, valueUpdater);

		if (parent != null) {

			if (event.getType().equals("click") && parent.getFirstChildElement().isOrHasChild(Element.as(event.getEventTarget())) && parent.getFirstChildElement().getTagName().equals("A")) {
				if (handler.isAuthorized()) {
					// if authorized and link clicked, process click
					handler.onClick();
				} else {
					// if not-authorized process field updater
					valueUpdater.update(value);
				}
			} else if (event.getType().equals("click")) {
				// link not clicked, process field updater
				valueUpdater.update(value);
			}

		} else {

			if (event.getType().equals("click")) {
				// link not clicked, process field updater
				valueUpdater.update(value);
			}

		}

	}

	@Override
	public boolean dependsOnSelection() {
		return false;
	}

	@Override
	public boolean handlesSelection() {
		return false;
	}

}
