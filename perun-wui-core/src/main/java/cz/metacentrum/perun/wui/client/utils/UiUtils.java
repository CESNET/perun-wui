package cz.metacentrum.perun.wui.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import cz.metacentrum.perun.wui.widgets.PerunButton;
import cz.metacentrum.perun.wui.widgets.PerunDataGrid;
import cz.metacentrum.perun.wui.widgets.PerunLoader;
import cz.metacentrum.perun.wui.widgets.SuggestBox;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumn;
import cz.metacentrum.perun.wui.widgets.resources.PerunColumnType;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.HashMap;
import java.util.Set;

/**
 * Utility class for common actions with Ui widgets and their binding.
 *
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class UiUtils {

	/**
	 * Bind SuggestBox and PerunButton to work together as search widget.
	 *
	 * @param box          SearchBox
	 * @param searchButton Button to trigger searching
	 * @param <C>          Implementation class for SuggestBox extending the base one.
	 */
	public static <C extends SuggestBox> void bindSearchBox(final C box, final PerunButton searchButton) {

		// when value is written or pasted
		box.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (box.getText().trim().isEmpty()) {
					searchButton.setEnabled(false);
				} else {
					searchButton.setEnabled(true);
				}
			}
		});

		// search box on enter
		box.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				DomEvent.fireNativeEvent(Document.get().createChangeEvent(), box);
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && box.isEnabled()) {
					searchButton.click();
				} else {

					if (box.getText().trim().isEmpty()) {
						searchButton.setEnabled(false);
					} else {
						searchButton.setEnabled(true);
					}

				}
			}
		});

		searchButton.setEnabled(false);

		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				box.setFocus(false);
			}
		});

	}

	/**
	 * Bind SuggestBox and PerunButton to work together as filter widget.
	 * Bins DataGrid (table) to work with them too.
	 *
	 * @param table        Table to bind filtering to
	 * @param box          FilterBox
	 * @param filterButton Button to trigger searching
	 * @param <T>          Type of object bound in table.
	 * @param <C>          Implementation class for SuggestBox extending the base one.
	 */
	public static <T extends JavaScriptObject , C extends SuggestBox> void bindFilterBox(final PerunDataGrid<T> table, final C box, final PerunButton filterButton) {

		box.setAutoSelectEnabled(false);

		// start filtering on enter key otherwise show suggestions
		box.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					box.hideSuggestionList();
					filterButton.click();
				} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
					box.hideSuggestionList();
				} else if (!box.isSuggestionListShowing()) {
					// of not already displayed, show suggestion list
					box.showSuggestionList();
				}
			}
		});

		// show suggestions on paste by mouse
		box.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (!box.isSuggestionListShowing()) {
					// of not already displayed, show suggestion list
					box.showSuggestionList();
				}
			}
		});

		//filter table when suggestion is selected
		box.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
				table.filterTable(event.getSelectedItem().getReplacementString());
			}
		});

		// filter on click
		filterButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				box.setFocus(false);
				box.hideSuggestionList();
				table.filterTable(box.getText());
			}
		});

	}

	/**
	 * add dropdown to set
	 *
	 * @param table        Table to bind filtering to
	 */
	public static <T extends JavaScriptObject,  C extends SuggestBox> void bindDropdown(final HashMap<AnchorListItem, PerunColumnType> anchorMap, final PerunDataGrid<T> table){
		Set<AnchorListItem> keySet = anchorMap.keySet();
		for (final AnchorListItem anchor : keySet){
			anchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (anchor.getIcon().equals(IconType.CHECK)){
						anchor.setIcon(IconType.MINUS);
					}else{
						anchor.setIcon(IconType.CHECK);
					}
					table.updateColumn(anchorMap.get(anchor));
				}
			});
		}
	}

	/**
	 * Bind widget to table loading state. When table is loading data, widget is disabled (if implements HasEnabled).
	 * When loaded, widget is enabled (also only if its not a button in processing state).
	 *
	 * @param table          Table to bind loading with.
	 * @param activeOnEmpty  TRUE == Widget will be active if table is in its own loading state, but PerunLoader is either filtering of empty.
	 * @param <T>            Type of object bound in table.
	 */
	public static <T extends JavaScriptObject> void bindTableLoading(final PerunDataGrid<T> table, final HasEnabled widget, final boolean activeOnEmpty) {
		bindTableLoading(table, widget, activeOnEmpty, false);
	}

	/**
	 * Bind widget to table loading state. When table is loading data, widget is disabled (if implements HasEnabled).
	 * When loaded, widget is enabled (also only if its not a button in processing state).
	 *
	 * @param table          Table to bind loading with.
	 * @param activeOnEmpty  TRUE == Widget will be active if table is in its own loading state, but PerunLoader is either filtering of empty.
	 * @param focus          TRUE == Widget will be focused once table is loaded (if focusable).
	 * @param <T>            Type of object bound in table.
	 */
	public static <T extends JavaScriptObject> void bindTableLoading(final PerunDataGrid<T> table, final HasEnabled widget, final boolean activeOnEmpty, final boolean focus) {

		table.addLoadingStateChangeHandler(new LoadingStateChangeEvent.Handler() {
			@Override
			public void onLoadingStateChanged(LoadingStateChangeEvent event) {

				// get actual perun table state
				PerunLoader.PerunLoaderState state = table.getLoaderWidget().getLoaderState();

				if (event.getLoadingState().equals(LoadingStateChangeEvent.LoadingState.LOADING)) {

					// loading or empty table

					if (activeOnEmpty && (PerunLoader.PerunLoaderState.filter.equals(state) || PerunLoader.PerunLoaderState.empty.equals(state))) {

						// ignoring emptiness (for filter buttons, search buttons etc.)

						if (widget instanceof PerunButton) {
							if (!((PerunButton) widget).isProcessing()) widget.setEnabled(true);
						} else {
							widget.setEnabled(true);
						}

						if (widget instanceof Focusable && widget.isEnabled() && focus) {
							((Focusable) widget).setFocus(true);
						}

					} else {

						// care for emptiness as it was loading
						if (widget instanceof PerunButton) {
							if (!((PerunButton) widget).isProcessing()) widget.setEnabled(false);
						} else {
							widget.setEnabled(false);
						}

					}

				} else {

					// loaded not empty

					if (widget instanceof PerunButton) {
						if (!((PerunButton) widget).isProcessing()) widget.setEnabled(true);
					} else {
						widget.setEnabled(true);
					}

					if (widget instanceof Focusable && widget.isEnabled() && focus) {
						((Focusable) widget).setFocus(true);
					}

				}

			}
		});


	}

	/**
	 * Bind widget to table selection state. When no item is selected, widget is disabled (if implements HasEnabled).
	 * When some item is selected, widget is enabled (also only if its not a button in processing state).
	 * On start widget is disabled.
	 *
	 * @param table          Table to bind selection with.
	 * @param <T>            Type of object bound in table.
	 */
	public static <T extends JavaScriptObject> void bindTableSelection(final PerunDataGrid<T> table, final HasEnabled widget) {

		if (widget instanceof PerunButton) {
			((PerunButton)widget).setTableManaged(table);
		}

		widget.setEnabled(false);

		// bind widget to both selection models since tables can switch between them
		if (table.isSingleSelection()) {
			table.getSingleSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				@Override
				public void onSelectionChange(SelectionChangeEvent selectionChangeEvent) {
					if (table.getSingleSelectionModel().getSelectedObject() != null) {
						if (widget instanceof PerunButton) {
							if (!((PerunButton) widget).isProcessing()) widget.setEnabled(true);
						} else {
							widget.setEnabled(true);
						}
					} else {
						widget.setEnabled(false);
					}
				}
			});

		} else {

			table.getMultiSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				@Override
				public void onSelectionChange(SelectionChangeEvent selectionChangeEvent) {
					if (table.getMultiSelectionModel().getSelectedSet() != null && !table.getMultiSelectionModel().getSelectedSet().isEmpty()) {
						if (widget instanceof PerunButton) {
							if (!((PerunButton) widget).isProcessing()) widget.setEnabled(true);
						} else {
							widget.setEnabled(true);
						}
					} else {
						widget.setEnabled(false);
					}
				}
			});

		}

	}

}